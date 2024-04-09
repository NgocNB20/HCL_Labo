/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import com.gmo_pg.g_pay.client.input.SearchTradeMultiInput;
import com.gmo_pg.g_pay.client.output.SearchTradeMultiOutput;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dao.linkpay.SettlementMethodLinkDao;
import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.IMultiTradeSearchAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.usecase.billing.service.CreditLineReleaseExecuter;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 請求伝票削除ユースケース
 */
@Service
public class DeleteBillingSlipUseCase {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DeleteBillingSlipUseCase.class);

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** GMOマルチ決済取引検索アダプター */
    private final IMultiTradeSearchAdapter multiTradeSearchAdapter;

    /** 与信枠解放ユースケース */
    private final CreditLineReleaseExecuter creditLineReleaseExecuter;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** リンク決済個別決済手段 マスタDao */
    private final SettlementMethodLinkDao settlementMethodLinkDao;

    /** 請求不整合エラーメール文言取得失敗用 */
    private final static String MAIL_CONTENT_FAIL = "(設定失敗)";

    /** コンストラクタ */
    @Autowired
    public DeleteBillingSlipUseCase(IBillingSlipRepository billingSlipRepository,
                                    IMultiTradeSearchAdapter multiTradeSearchAdapter,
                                    CreditLineReleaseExecuter creditLineReleaseUseCase,
                                    INotificationSubAdapter notificationSubAdapter,
                                    SettlementMethodLinkDao settlementMethodLinkDao) {
        this.billingSlipRepository = billingSlipRepository;
        this.multiTradeSearchAdapter = multiTradeSearchAdapter;
        this.creditLineReleaseExecuter = creditLineReleaseUseCase;
        this.notificationSubAdapter = notificationSubAdapter;
        this.settlementMethodLinkDao = settlementMethodLinkDao;
    }

    /**
     * 請求伝票削除する
     *
     * @param transactionId 取引ID
     * @param orderCode
     */
    public void deleteUnnecessaryByTransactionId(String transactionId, String orderCode) {

        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);

        if (StringUtils.isNoneBlank(orderCode)) {

            // 取引IDに紐づく請求伝票を取得する
            BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
            // 請求伝票が存在しない場合は処理をスキップする
            if (billingSlipEntity == null) {
                return;
            }

            OrderPaymentEntity orderPaymentEntity = billingSlipEntity.getOrderPaymentEntity();

            if (orderPaymentEntity.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {

                // （クレジット）与信枠開放処理
                creditLineReleaseExecuter.creditLineRelease(orderCode);

            } else if (orderPaymentEntity.getSettlementMethodType() == HTypeSettlementMethodType.LINK_PAYMENT) {

                // （リンク決済）請求不整合メール通知
                if (orderPaymentEntity.getOrderPaymentStatus() == OrderPaymentStatus.OPEN) {

                    // リンク決済で確定状態の場合
                    sendReportMail(orderCode, transactionId);

                } else if (orderPaymentEntity.getOrderPaymentStatus() == OrderPaymentStatus.UNDER_SETTLEMENT) {

                    // GMO決済方法取得 TODO リポジトリ
                    List<SettlementMethodLinkEntity> settlementMethodLinkEntityList = settlementMethodLinkDao.getList();

                    for (SettlementMethodLinkEntity settlementMethodLink : settlementMethodLinkEntityList) {
                        // リンク決済で途中決済状態の場合
                        LinkPayment linkPayment = (LinkPayment) orderPaymentEntity.getPaymentRequest();
                        // GMOマルチペイメントより決済状況を取得する
                        SearchTradeMultiInput searchTradeMultiInput = new SearchTradeMultiInput();
                        searchTradeMultiInput.setOrderId(orderCode);
                        searchTradeMultiInput.setPayType(settlementMethodLink.getPayType());
                        SearchTradeMultiOutput multiOutput =
                                        multiTradeSearchAdapter.doSearchTradeMulti(searchTradeMultiInput);

                        if (!StringUtils.isEmpty(multiOutput.getStatus())) {
                            if ("PAYSUCCESS".equals(multiOutput.getStatus()) || "CAPTURE".equals(
                                            multiOutput.getStatus()) || "SALES".equals(multiOutput.getStatus())
                                || "TRADING".equals(multiOutput.getStatus()) || (linkPayment.getLinkPaymentType()
                                                                                 == HTypePaymentLinkType.LATERDATEPAYMENT
                                                                                 && "REQSUCCESS ".equals(
                                            multiOutput.getStatus()))) {
                                // 請求不整合メール送信
                                sendReportMail(orderCode, transactionId);
                            }
                            break;
                        }
                    }
                }
            }
        }

        // 請求伝票削除
        this.billingSlipRepository.deleteUnnecessaryByTransactionId(transactionId);
    }

    /**
     * 請求不整合報告メールを送信する
     *
     * @param orderCode     受注番号
     * @param transactionId 取引ID
     */
    private void sendReportMail(String orderCode, String transactionId) {

        // プレースホルダの準備
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        String system = MAIL_CONTENT_FAIL;
        String time = MAIL_CONTENT_FAIL;
        String server = MAIL_CONTENT_FAIL;
        String process = MAIL_CONTENT_FAIL;
        String error = MAIL_CONTENT_FAIL;
        String recovery = MAIL_CONTENT_FAIL;

        try {
            system = PropertiesUtil.getSystemPropertiesValue("alert.mail.system");
            time = dateUtility.format(dateUtility.getCurrentTime(), DateUtility.YMD_SLASH_HMS);
            server = PropertiesUtil.getSystemPropertiesValue("system.name");
            process = PropertiesUtil.getSystemPropertiesValue(
                            "settlement.mismatch.processtype.unnecessarybillingslipcancellation");
            error = PropertiesUtil.getSystemPropertiesValue(
                            "settlement.mismatch.unnecessarybillingslipcancellation.error.message");
            recovery = PropertiesUtil.getSystemPropertiesValue(
                            "settlement.mismatch.unnecessarybillingslipcancellation.recovery.message");
        } catch (Exception e) {
            LOGGER.error("請求不整合メール用文言取得失敗しました。", e);
            system = MAIL_CONTENT_FAIL;
            time = MAIL_CONTENT_FAIL;
            server = MAIL_CONTENT_FAIL;
            process = MAIL_CONTENT_FAIL;
            error = MAIL_CONTENT_FAIL;
            recovery = MAIL_CONTENT_FAIL;
        }

        Map<String, String> placeHolders = new LinkedHashMap<>();
        // メールに記載するシステム名
        placeHolders.put("SYSTEM", system);
        placeHolders.put("TIME", time);
        placeHolders.put("SERVER", server);
        placeHolders.put("ORDERCODE", orderCode);
        placeHolders.put("PROCESS", process);
        placeHolders.put("MULPAY_PROCESS", "取引ID：" + transactionId);
        placeHolders.put("ERROR", error);
        placeHolders.put("RECOVERY", recovery);

        // メールを送信する
        SettlementMismatchRequest request = new SettlementMismatchRequest();
        request.setPlaceHolders(placeHolders);

        notificationSubAdapter.settlementMisMatch(request);

        LOGGER.info("請求不整合報告メールを送信しました。取引ID：" + transactionId);
    }
}