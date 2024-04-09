package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.base.utility.NetworkUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MultiPaymentAlertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * マルチペイメントアラート
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class MultipaymentAlertProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipaymentAlertProcessor.class);

    /** 定数：アラート検証対象のPREFIX名 */
    protected static final String TARGET_PREFIX = "mulpay_";

    /**
     * メール送信
     *
     * @param multiPaymentAlertRequest マルチペイメントアラートリクエスト
     */
    public void processor(MultiPaymentAlertRequest multiPaymentAlertRequest) {
        LOGGER.info("【Subscribe】ルーティングキー： user-multi-payment-alert-routing");

        // プレースホルダの準備
        LOGGER.debug("プレースホルダを準備");

        Map<String, String> placeHolders = new LinkedHashMap<>();
        // ネットワークHelper取得
        NetworkUtility networkUtility = ApplicationContextUtility.getBean(NetworkUtility.class);

        placeHolders.put("SERVER", networkUtility.getLocalHostName());
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        placeHolders.put("TIME", dateUtility.format(dateUtility.getCurrentTime(), DateUtility.YMD_SLASH_HMS));
        placeHolders.put("API", multiPaymentAlertRequest.getApi());
        placeHolders.put("ERROR", multiPaymentAlertRequest.getText());
        placeHolders.put("ORDERID", multiPaymentAlertRequest.getOrderId());

        /*
         * ユーザ情報をメールに追加 バッチ等、オンライン以外から呼ばれている想定を考慮しての徹底的に nullチェックと例外処理
         * 例外発生時は、なかったことにし、WARNログだけ出力する。
         */
        // null があるので、初期化する
        placeHolders.put("SESSIONID", "");
        placeHolders.put("AUI", "");
        placeHolders.put("MEMBERINFOSEQ", "");
        if (multiPaymentAlertRequest.getSessionId() != null) {
            placeHolders.put("SESSIONID", multiPaymentAlertRequest.getSessionId());
        }
        if (multiPaymentAlertRequest.getAccessUid() != null) {
            placeHolders.put("AUI", multiPaymentAlertRequest.getAccessUid());
        }
        if (multiPaymentAlertRequest.getMemberInfoSeq() != null) {
            placeHolders.put("MEMBERINFOSEQ", multiPaymentAlertRequest.getMemberInfoSeq());
        }

        // メールに記載するシステム名
        String mailSystem = PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "mail_system");
        placeHolders.put("SYSTEM", mailSystem);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SERVER:" + placeHolders.get("SERVER"));
            LOGGER.debug("TIME:" + placeHolders.get("TIME"));
            LOGGER.debug("API:" + placeHolders.get("API"));
            LOGGER.debug("ERROR:" + placeHolders.get("ERROR"));
            LOGGER.debug("ORDERID:" + placeHolders.get("ORDERID"));

            LOGGER.debug("SESSIONID:" + placeHolders.get("SESSIONID"));
            LOGGER.debug("MEMBERINFOSEQ:" + placeHolders.get("MEMBERINFOSEQ"));
            LOGGER.debug("AUI:" + placeHolders.get("AUI"));
            LOGGER.debug("SYSTEM:" + placeHolders.get("SYSTEM"));
        }

        // 簡易メール送信の準備
        LOGGER.debug("簡易メールの設定を準備");

        String[] recipients = PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "recipient")
                                            .replaceAll("\"", "")
                                            .split(" *, *");

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

            mailDto.setMailTemplateType(HTypeMailTemplateType.MULTI_PAYMENT_ALERT);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue(TARGET_PREFIX + "mail_from"));
            mailDto.setToList(Arrays.asList(recipients));
            mailDto.setSubject("【" + mailSystem + " 要確認】GMOマルチペイメントエラー報告");
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