package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.NetworkUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GmoMemberCardAlertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GMO会員カードアラート
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class GmoMemberCardAlertProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GmoMemberCardAlertProcessor.class);

    /** 定数：アラート検証対象のPREFIX名 */
    protected static final String TARGET_PREFIX = "gmoMember_";

    /**
     * メール送信
     *
     * @param gmoMemberCardAlertRequest GMO会員カードアラートリクエスト
     */
    public void processor(GmoMemberCardAlertRequest gmoMemberCardAlertRequest) {
        LOGGER.info("【Subscribe】ルーティングキー： user-gmo-member-card-alert-routing");

        // プレースホルダの準備
        LOGGER.debug("プレースホルダを準備");

        Map<String, String> placeHolders = new LinkedHashMap<>();
        // ネットワーク関連Utility
        NetworkUtility networkUtility = ApplicationContextUtility.getBean(NetworkUtility.class);
        placeHolders.put("SERVER", networkUtility.getLocalHostName());
        placeHolders.put("TIME", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        placeHolders.put("API", gmoMemberCardAlertRequest.getApi());
        placeHolders.put("ERROR", gmoMemberCardAlertRequest.getText());
        placeHolders.put("MEMBERID", gmoMemberCardAlertRequest.getMemberId());
        placeHolders.put("CARDSEQ", gmoMemberCardAlertRequest.getCardSeq());
        // メールに記載するシステム名
        String mailSystem = PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "mail_system");
        placeHolders.put("SYSTEM", mailSystem);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SERVER:" + placeHolders.get("SERVER"));
            LOGGER.debug("TIME:" + placeHolders.get("TIME"));
            LOGGER.debug("API:" + placeHolders.get("API"));
            LOGGER.debug("ERROR:" + placeHolders.get("ERROR"));
            LOGGER.debug("MEMBERID:" + placeHolders.get("MEMBERID"));
            LOGGER.debug("CARDSEQ:" + placeHolders.get("CARDSEQ"));
            LOGGER.debug("SYSTEM:" + placeHolders.get("SYSTEM"));
        }

        // 簡易メール送信の準備
        LOGGER.debug("簡易メールの設定を準備");

        String[] recipients = PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "recipient").split(" *, *");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("mail_server:" + PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "mail_server"));
            LOGGER.debug("mail_from:" + PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "mail_from"));
            LOGGER.debug("recipient:" + PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "recipient"));
        }

        // メールを送信する
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            Map<String, Object> mailContentsMap = new HashMap<>();

            mailContentsMap.put("mailContentsMap", placeHolders);

            mailDto.setMailTemplateType(HTypeMailTemplateType.GMO_MEMBER_CARD_ALERT);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "mail_from"));
            mailDto.setToList(Arrays.asList(recipients));
            mailDto.setSubject("【" + mailSystem + " 要確認】GMO会員カードエラー報告");
            mailDto.setMailContentsMap(mailContentsMap);

            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("アラートメールを送信しました。");
        } catch (Exception e) {
            LOGGER.warn("アラートメール送信に失敗しました。", e);
            throw e;
        }

    }
}