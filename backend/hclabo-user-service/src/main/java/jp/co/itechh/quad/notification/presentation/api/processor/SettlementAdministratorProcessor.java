package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementAdministratorMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 支払督促／支払期限切れ処理結果
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class SettlementAdministratorProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementAdministratorProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param settlementAdministratorMailRequest 受注決済期限切れメール送信リクエスト
     */
    public void processor(SettlementAdministratorMailRequest settlementAdministratorMailRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_SETTLEMENT_EXPIRED_NOTIFICATION.getLabel());
            valueMap.put("SHOP_SEQ", "1001");
            valueMap.put("RESULT", settlementAdministratorMailRequest.getResult());
            if (settlementAdministratorMailRequest.getMailMessage().length() == 0) {
                valueMap.put("LIST", "");
            } else {
                valueMap.put("LIST", settlementAdministratorMailRequest.getMailMessage());
            }

            if (LOGGER.isDebugEnabled()) {
                valueMap.put("DEBUG", "1");
            } else {
                valueMap.put("DEBUG", "0");
            }

            mailContentsMap.put("admin", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.SETTLEMENT_ADMINISTRATOR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.settlementadministrator.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.settlementadministrator.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_SETTLEMENT_EXPIRED_NOTIFICATION.getLabel()
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