package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorErrorRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 入金結果受付予備処理結果異常
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class MulpayNotificationRecoveryAdministratorErrorProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MulpayNotificationRecoveryAdministratorErrorProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param mulpayNotificationRecoveryAdministratorErrorRequest 入金結果受付予備処理結果異常メールリクエスト
     */
    public void processor(MulpayNotificationRecoveryAdministratorErrorRequest mulpayNotificationRecoveryAdministratorErrorRequest) {
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
            if (mulpayNotificationRecoveryAdministratorErrorRequest.getExceptionName() != null) {
                valueMap.put("RESULT", "処理中に" + mulpayNotificationRecoveryAdministratorErrorRequest.getExceptionName() + "が発生しました。");
            } else {
                String resultMsg = "キャンセル要求があったため処理は中断されました。";
                if (mulpayNotificationRecoveryAdministratorErrorRequest.getWorkCount() > 0) {
                    resultMsg = resultMsg + "\r\n" + "中断までに下記の入金処理、AmazonPayの請求決済エラー処理が完了しています。";
                }
                valueMap.put("RESULT", resultMsg);
            }
            valueMap.put("LIST", mulpayNotificationRecoveryAdministratorErrorRequest.getMessage());

            if (LOGGER.isDebugEnabled()) {
                valueMap.put("DEBUG", "1");
            } else {
                valueMap.put("DEBUG", "0");
            }

            mailContentsMap.put("error", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.MULPAY_NOTIFICATION_RECOVERY_ADMINISTRATOR_ERROR_MAIL);
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