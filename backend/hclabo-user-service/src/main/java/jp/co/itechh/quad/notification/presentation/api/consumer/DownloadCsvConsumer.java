package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.DownloadCsvProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 受注CSV
 *
 * @author Doan Thang (VJP)
 */
@Component
public class DownloadCsvConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DownloadCsvConsumer.class);

    public DownloadCsvConsumer() {
    }

    /**
     * メール送信
     *
     * @param downloadCsvRequest CSVダウンロードメールリクエスト
     */
    @RabbitListener(queues = "#{downloadCsvQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(DownloadCsvRequest downloadCsvRequest) {
        DownloadCsvProcessor downloadCsvProcessor = ApplicationContextUtility.getBean(DownloadCsvProcessor.class);

        try {
            downloadCsvProcessor.processor(downloadCsvRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
