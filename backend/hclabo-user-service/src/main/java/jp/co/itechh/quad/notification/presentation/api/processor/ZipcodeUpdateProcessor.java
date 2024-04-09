package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ZipcodeUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 事業所郵便番号更新
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class ZipcodeUpdateProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipcodeUpdateProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param zipcodeUpdateRequest 事業所郵便番号更新リクエスト
     */
    public void processor(ZipcodeUpdateRequest zipcodeUpdateRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            // プレースホルダーへ結果セット
            valueMap.put("SYSTEM", systemName);
            if (HTypeBatchName.BATCH_ZIP_CODE.getValue().equals(zipcodeUpdateRequest.getBatchName())) {
                valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_ZIP_CODE.getLabel());
            } else {
                valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_OFFICE_ZIP_CODE.getLabel());
            }
            valueMap.put("RESULT", zipcodeUpdateRequest.getMsg());

            if (zipcodeUpdateRequest.getResult()) {
                mailContentsMap.put("admin", valueMap);
                mailDto.setMailTemplateType(HTypeMailTemplateType.ZIPCODE_ADMINISTRATOR_MAIL);
            } else {
                mailContentsMap.put("error", valueMap);
                mailDto.setMailTemplateType(HTypeMailTemplateType.ZIPCODE_ADMINISTRATOR_ERROR_MAIL);
            }

            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.zipcode.mail.from"));
            mailDto.setToList(
                            Arrays.asList(PropertiesUtil.getSystemPropertiesValue("mail.setting.zipcode.mail.receivers")
                                                        .split(COMMA_DELIMITER)));

            if (HTypeBatchName.BATCH_ZIP_CODE.getValue().equals(zipcodeUpdateRequest.getBatchName())) {
                mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_ZIP_CODE.getLabel() + "報告");
            } else {
                mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_OFFICE_ZIP_CODE.getLabel() + "報告");
            }
            mailDto.setMailContentsMap(mailContentsMap);

            mailSendService.execute(mailDto);

            LOGGER.info("管理者へ通知メールを送信しました。");
        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }
}