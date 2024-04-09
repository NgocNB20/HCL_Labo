package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.DownloadCsvErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 受注CSVエラー
 *
 * @author Doan Thang (VJP)
 */
@Component
public class DownloadCsvErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DownloadCsvErrorConsumer.class);

    public DownloadCsvErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param downloadCsvErrorRequest CSVダウンロードエラーメールエラーリクエスト
     */
    @RabbitListener(queues = "#{downloadCsvErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendErrorMail(DownloadCsvErrorRequest downloadCsvErrorRequest) {
        DownloadCsvErrorProcessor downloadCsvErrorProcessor =
                        ApplicationContextUtility.getBean(DownloadCsvErrorProcessor.class);

        try {
            downloadCsvErrorProcessor.processor(downloadCsvErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}