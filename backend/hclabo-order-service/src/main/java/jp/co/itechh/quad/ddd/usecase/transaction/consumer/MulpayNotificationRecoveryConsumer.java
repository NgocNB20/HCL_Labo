/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.usecase.transaction.consumer;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.usecase.transaction.processor.MulpayNotificationRecoveryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 決済代行入金結果受取予備処理実行クラス Consumer
 *
 * @author Pham Quang Dieu (VJP)
 */
@Component
public class MulpayNotificationRecoveryConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MulpayNotificationRecoveryConsumer.class);

    /** コンストラクタ */
    public MulpayNotificationRecoveryConsumer() {
    }

    /**
     * Consumerメソッド <br/>
     * 決済代行入金結果受取予備処理バッチ実行
     *
     * @param batchQueueMessage キューメッセージ
     */
    @RabbitListener(queues = "#{mulpayNotificationRecoveryQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void consumer(BatchQueueMessage batchQueueMessage) {
        MulpayNotificationRecoveryProcessor mulpayNotificationRecoveryProcessor =
                        ApplicationContextUtility.getBean(MulpayNotificationRecoveryProcessor.class);

        try {
            mulpayNotificationRecoveryProcessor.processor(batchQueueMessage);
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