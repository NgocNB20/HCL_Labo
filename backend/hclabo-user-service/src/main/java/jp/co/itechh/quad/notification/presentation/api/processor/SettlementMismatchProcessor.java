package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 請求不整合報告
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class SettlementMismatchProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementMismatchProcessor.class);

    /**
     * メール送信
     *
     * @param settlementMismatchRequest 通知サブドメインリストリクエスト
     */
    public void processor(SettlementMismatchRequest settlementMismatchRequest) {
        LOGGER.info("【Subscribe】ルーティングキー： user-settlement-mismatch-routing");

        // プレースホルダの準備
        Map<String, String> placeHolders = settlementMismatchRequest.getPlaceHolders();

        // 簡易メール送信の準備
        String mailFrom = PropertiesUtil.getSystemPropertiesValue("mail_from");
        List<String> notificationReceivers = Arrays.asList(PropertiesUtil.getSystemPropertiesValue("recipient")
                                                                         .replaceAll("\"", "")
                                                                         .split(" *, *"));
        String mailSystem = PropertiesUtil.getSystemPropertiesValue("mail_system");
        placeHolders.put("SYSTEM", mailSystem);

        // メールを送信する
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            Map<String, Object> mailContentsMap = new HashMap<>();

            mailContentsMap.put("mailContentsMap", placeHolders);

            mailDto.setMailTemplateType(HTypeMailTemplateType.SETTLEMENT_MISMATCH);
            mailDto.setFrom(mailFrom);
            mailDto.setToList(notificationReceivers);
            mailDto.setSubject("【" + mailSystem + " 要確認】請求不整合報告");
            mailDto.setMailContentsMap(mailContentsMap);

            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("請求不整合報告メールを送信しました。");
        } catch (Exception e) {
            LOGGER.warn("請求不整合報告メール送信に失敗しました。", e);
            throw e;
        }

    }
}