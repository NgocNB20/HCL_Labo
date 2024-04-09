package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.AuthTimeLimitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * オーソリ期限切れ間近注文通知
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class AuthTimeLimitProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTimeLimitProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param authTimeLimitRequest オーソリ期限切れ間近注文通知リクエスト
     */
    public void processor(AuthTimeLimitRequest authTimeLimitRequest) {
        try {

            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            final Map<String, String> valueMap = new HashMap<>();
            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_AUTHTIMELIMITORDER_NOTIFICATION.getLabel());
            if (authTimeLimitRequest.getSubjectFlg()) {
                // TRUEの場合は、検出あり
                valueMap.put("SUB_WORD", "検出");
            } else {
                // FALSEの場合は、検出なし(0件)
                valueMap.put("SUB_WORD", "作動");
            }
            valueMap.put("RESULT", authTimeLimitRequest.getResult());
            if (authTimeLimitRequest.getMailMessage().length() == 0) {
                valueMap.put("LIST", "");
            } else {
                valueMap.put("LIST", authTimeLimitRequest.getMailMessage());
            }

            mailContentsMap.put("admin", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.AUTH_TIME_LIMIT_ADMINISTRATOR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.auth.time.limit.order.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.auth.time.limit.order.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_AUTHTIMELIMITORDER_NOTIFICATION.getLabel()
                               + "報告");
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