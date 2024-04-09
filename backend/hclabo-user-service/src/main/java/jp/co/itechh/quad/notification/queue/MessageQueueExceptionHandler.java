package jp.co.itechh.quad.notification.queue;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueExceptionHandler implements RabbitListenerErrorHandler {
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueueExceptionHandler.class);

    /**
     * エラーハンドリング
     *
     * @param amqpMessage
     * @param message
     * @param exception
     * @return
     */
    @Override
    public Object handleError(Message amqpMessage,
                              org.springframework.messaging.Message<?> message,
                              ListenerExecutionFailedException exception) {

        // ログ出力
        LOGGER.error(exception.getMessage());
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(exception);

        // スロー
        throw exception;
    }
}