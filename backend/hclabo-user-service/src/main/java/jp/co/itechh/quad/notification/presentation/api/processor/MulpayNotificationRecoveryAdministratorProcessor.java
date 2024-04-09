package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 入金結果受付予備処理
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class MulpayNotificationRecoveryAdministratorProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MulpayNotificationRecoveryAdministratorProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param mulpayNotificationRecoveryAdministratorRequest 入金結果受付予備処理結果メールリクエスト
     */
    public void processor(MulpayNotificationRecoveryAdministratorRequest mulpayNotificationRecoveryAdministratorRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY_MAIL.getLabel());
            valueMap.put("SHOP_SEQ", "1001");
            String mailMsg = null;
            if (mulpayNotificationRecoveryAdministratorRequest.getNormalWorkCount() > 0) {
                mailMsg = mulpayNotificationRecoveryAdministratorRequest.getNormalWorkCount() + "件の入金がありました。\r\n";
                if (mulpayNotificationRecoveryAdministratorRequest.getNormalErrorCount() > 0) {
                    mailMsg = mailMsg + "そのうち" + mulpayNotificationRecoveryAdministratorRequest.getNormalErrorCount() + "件はエラーのため正しく処理できませんでした。";
                }
            }
            valueMap.put("RESULT", mailMsg);

            valueMap.put("LIST", mulpayNotificationRecoveryAdministratorRequest.getMessage());

            if (LOGGER.isDebugEnabled()) {
                valueMap.put("DEBUG", "1");
            } else {
                valueMap.put("DEBUG", "0");
            }

            mailContentsMap.put("admin", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.MULPAY_NOTIFICATION_RECOVERY_ADMINISTRATOR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.mulpaynotificationrecoveryadministrator.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.mulpaynotificationrecoveryadministrator.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY_MAIL.getLabel() + "報告");
            mailDto.setMailContentsMap(mailContentsMap);

            mailSendService.execute(mailDto);
            LOGGER.info("管理者へ通知メールを送信しました。");

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }
}