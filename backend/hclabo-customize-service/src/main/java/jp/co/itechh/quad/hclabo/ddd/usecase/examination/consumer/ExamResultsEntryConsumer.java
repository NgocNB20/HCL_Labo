/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.ddd.usecase.examination.consumer;

import jp.co.itechh.quad.hclabo.core.base.exception.AppLevelException;
import jp.co.itechh.quad.hclabo.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.hclabo.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.hclabo.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import jp.co.itechh.quad.hclabo.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.processor.ExamResultsEntryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 検査結果登録 Consumer
 *
 */
@Component
public class ExamResultsEntryConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsEntryConsumer.class);

    /** コンストラクタ */
    public ExamResultsEntryConsumer() {
    }

    /**
     * Consumerメソッド <br/>
     * 検査結果登録バッチ実行
     *
     * @param batchQueueMessage キューメッセージ
     */
    @RabbitListener(queues = "#{examResultsEntryQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void consumer(BatchQueueMessage batchQueueMessage) {
        ExamResultsEntryProcessor examResultsProcessor = ApplicationContextUtility.getBean(ExamResultsEntryProcessor.class);

        try {
            examResultsProcessor.processor(batchQueueMessage);
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