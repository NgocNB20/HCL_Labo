package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentExcessAlertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入金過不足アラート
 */
@Component
@Scope("prototype")
public class PaymentExcessAlertProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentExcessAlertProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param request 入金過不足アラートリクエスト
     */
    public void processor(PaymentExcessAlertRequest request) throws Exception {

        LOGGER.info("【Subscribe】ルーティングキー： payment-excess-alert-routing");

        // 送信先アドレスを設定
        String targetMailAddress =
                        PropertiesUtil.getSystemPropertiesValue("mail.setting.payment.excess.mail.receivers");

        // システム名を取得
        String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

        // 送信先取得
        List<String> toList = Arrays.asList(targetMailAddress.split(COMMA_DELIMITER));

        // メールDto作成
        MailDto mailDto = new MailDto();
        mailDto.setMailTemplateType(HTypeMailTemplateType.PAYMENT_EXCESS_ALERT);
        mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.PAYMENT_EXCESS_ALERT.getLabel() + "アラート");
        mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.payment.excess.mail.from"));
        mailDto.setToList(toList);
        // 値マップの作成
        Map<String, Object> mailContentsMap = new HashMap<>();
        mailContentsMap.put("orderCodeList", request.getOrderCodeList());
        mailContentsMap.put("systemName", systemName);
        mailContentsMap.put("overFlag", request.getOverFlag());

        mailDto.setMailContentsMap(mailContentsMap);

        // メール送信
        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
        mailSendService.execute(mailDto);

        LOGGER.info("入金過不足アラートメールを送信しました。");
    }

}