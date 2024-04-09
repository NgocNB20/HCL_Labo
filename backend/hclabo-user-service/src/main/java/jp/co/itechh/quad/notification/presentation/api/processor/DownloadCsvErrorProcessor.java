package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvErrorRequest;
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
 * 受注CSVエラー
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class DownloadCsvErrorProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadCsvErrorProcessor.class);

    /**
     * メール送信
     *
     * @param downloadCsvErrorRequest CSVダウンロードエラーメールリクエスト
     */
    public void processor(DownloadCsvErrorRequest downloadCsvErrorRequest) {
        try {
            // メールに記載する情報
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");
            LocalDateTime processingTime = downloadCsvErrorRequest.getProcessingTime()
                                                                  .toInstant()
                                                                  .atZone(ZoneId.systemDefault())
                                                                  .toLocalDateTime();

            // プレースホルダーへ結果セット
            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", downloadCsvErrorRequest.getBatchName());
            valueMap.put(
                            "PROCESS_START_DATE",
                            DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm:ss").format(processingTime).toString()
                        );
            mailContentsMap.put("error", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.DOWNLOAD_CSV_ADMINISTRATOR_ERROR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.downloadCsvAsynchronous.mail.from"));
            mailDto.setToList(new ArrayList<String>(Arrays.asList(downloadCsvErrorRequest.getMail())));
            mailDto.setSubject("【" + systemName + "】" + downloadCsvErrorRequest.getBatchName() + "ダウンロードのエラー報告");
            mailDto.setMailContentsMap(mailContentsMap);

            // メール送信処理
            Boolean result = mailSendService.execute(mailDto);
            if (result) {
                LOGGER.info("管理者へ受注CSV生成バッチのエラーメールを送信しました。");
            }
        } catch (Exception e) {
            LOGGER.warn("管理者への受注CSV生成バッチのエラーメール送信に失敗しました。", e);
            throw e;
        }
    }
}