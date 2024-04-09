package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamkitReceivedEntryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 検査キット受領登録
 *
 */
@Component
@Scope("prototype")
public class ExamkitReceivedEntryProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamkitReceivedEntryProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param examkitReceivedEntryRequest 検査キット受領登録リクエスト（エラー通知）
     */
    public void processor(ExamkitReceivedEntryRequest examkitReceivedEntryRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();

            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.EXAMKIT_RECEIVED_ENTRY.getLabel());
            valueMap.put("SHOP_SEQ", "1001");
            if (examkitReceivedEntryRequest.getErrorMailMessage() == null
                || examkitReceivedEntryRequest.getErrorMailMessage().length() == 0) {
                valueMap.put("LIST", "");
            } else {
                valueMap.put("LIST", examkitReceivedEntryRequest.getErrorMailMessage());
            }
            if (LOGGER.isDebugEnabled()) {
                valueMap.put("DEBUG", "1");
            } else {
                valueMap.put("DEBUG", "0");
            }

            mailContentsMap.put("error", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.EXAMKIT_RECEIVED_ENTRY_ADMINISTRATOR_ERROR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.examkit.received.entry.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.examkit.received.entry.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.EXAMKIT_RECEIVED_ENTRY.getLabel() + "報告");
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