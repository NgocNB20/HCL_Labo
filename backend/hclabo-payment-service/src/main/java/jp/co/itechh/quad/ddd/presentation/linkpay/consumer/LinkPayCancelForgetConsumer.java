package jp.co.itechh.quad.ddd.presentation.linkpay.consumer;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.usecase.processor.LinkPayCancelForgetProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GMO決済キャンセル漏れ検知
 *
 * @author Doan Thang (VJP)
 */
@Component
public class LinkPayCancelForgetConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LinkPayCancelForgetConsumer.class);

    /**
     * GMO決済キャンセル漏れ検知
     */
    @RabbitListener(queues = "#{linkPayCancelForgetQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail() {
        LinkPayCancelForgetProcessor linkPayCancelForgetProcessor =
                        ApplicationContextUtility.getBean(LinkPayCancelForgetProcessor.class);

        try {
            linkPayCancelForgetProcessor.processor();
        } catch (Exception e) {
            // AppLevelListExceptionの場合は、ErrorListを出力する
            if (e instanceof AppLevelListException) {
                for (AppLevelException ae : ((AppLevelListException) e).getErrorList()) {
                    LOGGER.error(ae.getMessage());
                }
            }
            // AppLevelListException以外の場合は、エラーメッセージを出力する
            else if (e instanceof DomainException) {
                Set<Map.Entry<String, List<ExceptionContent>>> entries =
                                ((DomainException) e).getMessageMap().entrySet();

                for (Map.Entry<String, List<ExceptionContent>> entry : entries) {
                    for (ExceptionContent ec : entry.getValue()) {
                        LOGGER.error(ec.getMessage());
                    }
                }
            } else {
                LOGGER.error(e.getMessage());
            }
        }
    }
}