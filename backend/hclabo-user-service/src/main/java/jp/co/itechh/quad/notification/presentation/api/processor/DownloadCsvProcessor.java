package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 受注CSV
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class DownloadCsvProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadCsvProcessor.class);

    /**
     * メール送信
     *
     * @param downloadCsvRequest CSVダウンロードメールリクエスト
     */
    public void processor(DownloadCsvRequest downloadCsvRequest) {
        try {
            // メールに記載する情報
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            String DL_URL = PropertiesUtil.getSystemPropertiesValue("downloadCsvAsynchronous.url");
            String RETENTION_PERIOD =
                            PropertiesUtil.getSystemPropertiesValue("downloadCsvAsynchronous.retention.period");

            Map<String, Object> mailContentsMap = new HashMap<>();
            Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            LocalDateTime processingTime = downloadCsvRequest.getProcessingTime()
                                                             .toInstant()
                                                             .atZone(ZoneId.systemDefault())
                                                             .toLocalDateTime();

            // プレースホルダーへ結果セット
            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", downloadCsvRequest.getBatchName());
            valueMap.put(
                            "PROCESS_START_DATE",
                            DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm:ss").format(processingTime)
                        );
            valueMap.put("PROCESS_COUNT", String.valueOf(downloadCsvRequest.getProcessingCnt()));
            valueMap.put("FILE_DL_URL", DL_URL + downloadCsvRequest.getDownloadUrl());
            valueMap.put("PERIOD", RETENTION_PERIOD);
            valueMap.put("DELETE_DATE", DateTimeFormatter.ofPattern("yyyy年MM月dd日")
                                                         .format(processingTime.plusDays(
                                                                         Integer.valueOf(RETENTION_PERIOD))));
            mailContentsMap.put("admin", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.DOWNLOAD_CSV_ADMINISTRATOR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.downloadCsvAsynchronous.mail.from"));
            mailDto.setToList(new ArrayList<String>(Arrays.asList(downloadCsvRequest.getMail())));
            mailDto.setSubject("【" + systemName + "】" + downloadCsvRequest.getBatchName() + "ダウンロードURLのご案内");
            mailDto.setMailContentsMap(mailContentsMap);

            // メール送信処理
            Boolean result = mailSendService.execute(mailDto);
            if (result) {
                LOGGER.info("管理者へ受注CSVのDLメールを送信しました。");
            }
        } catch (Exception e) {
            LOGGER.warn("管理者への受注CSVのDLメール送信に失敗しました。", e);
            throw e;
        }
    }
}