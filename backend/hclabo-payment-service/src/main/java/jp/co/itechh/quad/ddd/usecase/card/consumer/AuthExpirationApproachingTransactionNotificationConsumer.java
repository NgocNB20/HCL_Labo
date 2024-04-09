/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.usecase.card.consumer;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.usecase.card.processor.AuthExpirationApproachingTransactionNotificationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * オーソリ期限切れ間近取引警告通知 Consumer
 *
 * @author kimura
 */
@Component
public class AuthExpirationApproachingTransactionNotificationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER =
                    LoggerFactory.getLogger(AuthExpirationApproachingTransactionNotificationConsumer.class);

    /** コンストラクタ */
    public AuthExpirationApproachingTransactionNotificationConsumer() {
    }

    /**
     * Consumerメソッド <br/>
     * オーソリ期限間近の取引に警告通知
     *
     * @param batchQueueMessage メッセージ
     */
    @RabbitListener(queues = "#{authNotificationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void consumer(BatchQueueMessage batchQueueMessage) {
        AuthExpirationApproachingTransactionNotificationProcessor authExpiration = ApplicationContextUtility.getBean(
                        AuthExpirationApproachingTransactionNotificationProcessor.class);

        try {
            authExpiration.processor(batchQueueMessage);
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