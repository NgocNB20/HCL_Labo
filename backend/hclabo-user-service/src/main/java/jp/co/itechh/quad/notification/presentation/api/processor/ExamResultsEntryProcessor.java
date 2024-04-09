/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsEntryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 検査結果登録異常 Processor
 *
 */
@Component
@Scope("prototype")
public class ExamResultsEntryProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsEntryProcessor.class);

    /** PDFファイル名の拡張子 */
    private static final String PDF_FILE_NAME_EXTENSION = ".pdf";

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param examResultsEntryRequest 検査結果登録リクエスト（エラー通知）
     */
    public void processor(ExamResultsEntryRequest examResultsEntryRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();

            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.EXAM_RESULTS_ENTRY.getLabel());
            valueMap.put("SHOP_SEQ", "1001");

            // 検査結果PDFの場合は
            String uploadType = "0";

            if (examResultsEntryRequest.getErrorMailMessage() == null
                || examResultsEntryRequest.getErrorMailMessage().length() == 0) {
                valueMap.put("LIST", "");
            } else {
                valueMap.put("LIST", examResultsEntryRequest.getErrorMailMessage());
                if (examResultsEntryRequest.getErrorMailMessage().contains(PDF_FILE_NAME_EXTENSION)) {
                    uploadType = "1";
                }
            }
            if (LOGGER.isDebugEnabled()) {
                valueMap.put("DEBUG", "1");
            } else {
                valueMap.put("DEBUG", "0");
            }
            valueMap.put("UPLOAD_TYPE", uploadType);

            mailContentsMap.put("error", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.EXAM_RESULTS_ENTRY_ADMINISTRATOR_ERROR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.exam.results.entry.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.exam.results.entry.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.EXAM_RESULTS_ENTRY.getLabel() + "報告");
            mailDto.setMailContentsMap(mailContentsMap);

            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("管理者へ通知メールを送信しました。");

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }
}