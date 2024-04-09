package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.LinkpayCancelReminderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GMO決済キャンセル漏れProcessor
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class LinkpayCancelReminderProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkpayCancelReminderProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param request GMO決済キャンセル漏れメール送信要求リクエスト
     */
    public void processor(LinkpayCancelReminderRequest request) throws Exception {

        LOGGER.info("【Subscribe】ルーティングキー： linkpay-cancel-reminder-routing");

        // 送信先アドレスを設定
        String targetMailAddress =
                        PropertiesUtil.getSystemPropertiesValue("mail.setting.gmo.cancel.forget.mail.receivers");

        // システム名を取得
        String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

        // 送信先取得
        List<String> toList = Arrays.asList(targetMailAddress.split(COMMA_DELIMITER));

        // メールDto作成
        MailDto mailDto = new MailDto();

        mailDto.setMailTemplateType(HTypeMailTemplateType.GMO_PAYMENT_CANCEL_FORGOT);
        mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.GMO_PAYMENT_CANCEL_FORGOT.getLabel() + "アラート");
        mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.gmo.cancel.forget.mail.from"));
        mailDto.setToList(toList);

        // メール用値マップの作成
        Map<String, Object> mailContentsMap = new HashMap<>();
        mailContentsMap.put("targetOrderList", request.getTargetOrderList());
        mailContentsMap.put("systemName", systemName);

        mailDto.setMailContentsMap(mailContentsMap);

        // メール送信
        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
        mailSendService.execute(mailDto);

        LOGGER.info("GMO決済キャンセル漏れアラートメールを送信しました。");
    }

}