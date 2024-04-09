package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderRegistAlertRequest;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注文データ作成アラート
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class OrderRegistAlertProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRegistAlertProcessor.class);

    /** 注文登録失敗 */
    private static final String MSGCD_ORDER_REGIST_FAIL = "LOX000102";

    /**
     * 注文エラーメール送信用パラメータ保持クラス<br/>
     *
     * @author sm21603
     */
    @Data
    protected static class OrderRegistAlertMailProperties {
        /** メールサーバ */
        private String mailServer;

        /** メールのFROM */
        private String mailFrom;

        /** アラートメール受信者 */
        private String[] recipients;

        /** アラートメール対象外エラーコード */
        private List<String> excludeErrorCode;

        /** メールに記載するシステム名 */
        private String mailSystem;
    }

    /**
     * メール送信
     *
     * @param orderRegistAlertRequest 注文データ作成アラートリクエスト
     */
    public void processor(OrderRegistAlertRequest orderRegistAlertRequest) {
        OrderRegistAlertMailProperties props = getOrderRegistAlertMailProperties();
        sendAdministratorErrorMail(orderRegistAlertRequest, MSGCD_ORDER_REGIST_FAIL, props);
    }

    /**
     * 注文エラーメール送信用パラメータ取得
     *
     * @return 注文エラーメール送信用パラメータ保持クラス
     */
    protected OrderRegistAlertMailProperties getOrderRegistAlertMailProperties() {
        OrderRegistAlertMailProperties mailProps = new OrderRegistAlertMailProperties();
        mailProps.setMailServer(PropertiesUtil.getSystemPropertiesValue("mail_server"));
        mailProps.setMailFrom(PropertiesUtil.getSystemPropertiesValue("mail_from"));
        mailProps.setRecipients(
                        PropertiesUtil.getSystemPropertiesValue("recipient").replaceAll("\"", "").split(" *, *"));
        mailProps.setExcludeErrorCode(new ArrayList<>());
        String excludeErrorCodes = PropertiesUtil.getSystemPropertiesValue("exclude_error_code");
        if (StringUtils.isNotEmpty(excludeErrorCodes)) {
            String[] excludeErrorCodeArray = excludeErrorCodes.replaceAll("\"", "").split(" *, *");
            mailProps.getExcludeErrorCode().addAll(Arrays.asList(excludeErrorCodeArray));
        }
        mailProps.setMailSystem(PropertiesUtil.getSystemPropertiesValue("mail_system"));
        return mailProps;
    }

    /**
     * 受注登録に失敗した旨を管理者へ通知するメールを送信する
     *
     * @param orderRegistAlertRequest 注文データ作成アラートリクエスト
     * @param messageId エラーメッセージID
     * @param props 注文エラーメール送信用パラメータ
     * @return 送信成功:true、送信失敗：false
     */
    public void sendAdministratorErrorMail(OrderRegistAlertRequest orderRegistAlertRequest,
                                           String messageId,
                                           OrderRegistAlertMailProperties props) {
        try {
            HTypeSettlementMethodType settlement = EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                                 orderRegistAlertRequest.getSettlement()
                                                                                );

            StringBuilder errorMsg = new StringBuilder();
            if (StringUtil.isNotEmpty(messageId)) {
                errorMsg.append(AppLevelFacesMessageUtil.getAllMessage(messageId, null).getMessage() + "\r\n");
            }
            if (StringUtil.isNotEmpty(orderRegistAlertRequest.getMessage())) {
                errorMsg.append(orderRegistAlertRequest.getMessage());
            }

            Map<String, String> valueMap = new HashMap<>();
            String orderCode = orderRegistAlertRequest.getOrderCode();

            if (settlement == null) {
                valueMap.put("SETTLEMENTNAME", "");
            } else {
                valueMap.put("SETTLEMENTNAME", settlement.getLabel());
            }

            String accessId = "(受注番号：" + orderCode + ")";
            if (orderRegistAlertRequest.getAccessId() != null) {
                accessId = orderRegistAlertRequest.getAccessId() + "(受注番号：" + orderCode + ")";
            }
            valueMap.put("ACCESSID", accessId);

            valueMap.put("ERROR", errorMsg.toString());
            if (LOGGER.isDebugEnabled()) {
                valueMap.put("DEBUG", "1");
            } else {
                valueMap.put("DEBUG", "0");
            }

            // メールに記載するシステム名
            valueMap.put("SYSTEM", props.getMailSystem());

            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            Map<String, Object> mailContentsMap = new HashMap<>();

            mailContentsMap.put("mailContentsMap", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.ORDER_REGIST_ALERT);
            mailDto.setFrom(props.getMailFrom());
            mailDto.setToList(Arrays.asList(props.getRecipients()));
            mailDto.setSubject("【" + props.getMailSystem() + " 要確認】注文処理エラー報告");
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