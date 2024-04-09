/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionOpenSalesResponse;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PreClaimBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 改訂用売上確定ユースケース
 */
@Service
public class OpenSalesForRevisionUseCase {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OpenSalesForRevisionUseCase.class);

    /** 改訂用請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 注文決済ドメインサービス */
    private final OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService;

    /** 改訂用請求伝票確定ユースケース */
    private final OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 請求不整合エラーメール文言取得失敗用 */
    private final static String MAIL_CONTENT_FAIL = "(設定失敗)";

    /** コンストラクタ */
    @Autowired
    public OpenSalesForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                       OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService,
                                       OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase,
                                       INotificationSubAdapter notificationSubAdapter) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.orderPaymentForRevisionEntityService = orderPaymentForRevisionEntityService;
        this.openBillingSlipReviseUseCase = openBillingSlipReviseUseCase;
        this.notificationSubAdapter = notificationSubAdapter;
    }

    /**
     * 改訂用売上を確定する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param revisionOpenFlag      改訂確定フラグ
     * @return true ... 売上実行正常 or 売上済み（前払い請求） / false ... 売上実行異常
     */
    public BillingSlipForRevisionOpenSalesResponse openSalesForRevision(String transactionRevisionId,
                                                                        boolean revisionOpenFlag,
                                                                        String administratorId) {

        BillingSlipForRevisionOpenSalesResponse billingSlipForRevisionOpenSalesResponse =
                        new BillingSlipForRevisionOpenSalesResponse();

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 請求伝票が存在しない場合はエラー
        if (billingSlipForRevisionEntity == null) {
            throw new DomainException("PAYMENT_EPAR0001-E", new String[] {transactionRevisionId});
        }
        // 前請求の場合は処理をスキップする
        if (billingSlipForRevisionEntity.getBillingStatus() instanceof PreClaimBillingStatus) {
            // 改訂確定フラグが立っている場合は、改訂用請求伝票を確定する
            if (revisionOpenFlag) {
                openBillingSlipReviseUseCase.openBillingSlipRevise(transactionRevisionId, true, administratorId);
            }
            billingSlipForRevisionOpenSalesResponse.setSalesFlag(true);
            billingSlipForRevisionOpenSalesResponse.setGmoCommunicationSuccessFlag(false);
            return billingSlipForRevisionOpenSalesResponse;
        }

        try {
            // 注文決済を売上計上する ※クレジット決済以外の場合は呼び出し先で処理をスキップする
            boolean gmoCommunicationSuccessFlag = orderPaymentForRevisionEntityService.recordSales(
                            billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity());
            billingSlipForRevisionOpenSalesResponse.setGmoCommunicationSuccessFlag(gmoCommunicationSuccessFlag);
        } catch (ApplicationException e) {
            LOGGER.error("例外処理が発生しました", e);
            billingSlipForRevisionOpenSalesResponse.setGmoCommunicationSuccessFlag(false);

            // ApplicationExceptionの場合は、決済代行の通信結果でエラー発生を考慮したエラーハンドリングを実施
            e.getMessageMap().forEach((fieldName, exceptionContentList) -> {
                for (ExceptionContent exceptionContent : exceptionContentList) {
                    // ログに出力
                    LOGGER.error("オーソリエラーが発生しました。 -- 受注番号："
                                 + billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode()
                                 + " 情報：" + exceptionContent.getMessage());
                }
            });

            // 決済代行への異常終了した場合、売上を一時停止する
            billingSlipForRevisionEntity.suspendSlip();

            // 請求伝票を更新する
            billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);

            // 改訂確定フラグが立っている場合は、改訂用請求伝票を確定する
            if (revisionOpenFlag) {
                openBillingSlipReviseUseCase.openBillingSlipRevise(transactionRevisionId, true, administratorId);
            }

            billingSlipForRevisionOpenSalesResponse.setSalesFlag(false);
            return billingSlipForRevisionOpenSalesResponse;
        }

        try {
            // 売上を確定する
            billingSlipForRevisionEntity.openSales(new Date(), billingSlipForRevisionEntity.getBilledPrice());

            // クレジット決済 かつ 後払い請求の場合、オーソリ期限日を削除する
            if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                            .getPaymentRequest() instanceof CreditPayment
                && billingSlipForRevisionEntity.getBillingType() == HTypeBillType.POST_CLAIM) {
                orderPaymentForRevisionEntityService.removeAuthLimitDate(billingSlipForRevisionEntity);
            }

            // 改訂用請求伝票を更新する
            billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);
            billingSlipForRevisionOpenSalesResponse.setSalesFlag(true);

            // 改訂確定フラグが立っている場合は、改訂用請求伝票を確定する
            if (revisionOpenFlag) {
                openBillingSlipReviseUseCase.openBillingSlipRevise(transactionRevisionId, true, administratorId);
            }

        } catch (Exception e) {

            LOGGER.error("改訂用請求伝票確定（キャンセル）でエラー -- 受注番号："
                         + billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode()
                         + "  -- 改訂用取引ID：" + transactionRevisionId, e);

            //　決済済みの場合は請求不整合エラーメール送信
            if (billingSlipForRevisionOpenSalesResponse.getGmoCommunicationSuccessFlag()) {

                String orderCodeSendMail = MAIL_CONTENT_FAIL;
                if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode() != null) {
                    orderCodeSendMail = billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode();
                }

                sendSettlementMisMatchMail(orderCodeSendMail, transactionRevisionId);

                throw new DomainException("PAYMENT_EPAR0006-E");
            }
            throw e;
        }

        return billingSlipForRevisionOpenSalesResponse;
    }

    /**
     * 請求不整合報告メールを送信する
     *
     * @param orderCode             受注番号
     * @param transactionRevisionId 改訂用取引ID
     */
    private void sendSettlementMisMatchMail(String orderCode, String transactionRevisionId) {
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
            process = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.processtype.opensales");
            error = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.error.message");
            recovery = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.recovery.message");
        } catch (Exception e) {
            LOGGER.error("請求不整合メール用文言取得失敗しました。", e);
            system = MAIL_CONTENT_FAIL;
            time = MAIL_CONTENT_FAIL;
            server = MAIL_CONTENT_FAIL;
            process = MAIL_CONTENT_FAIL;
            error = MAIL_CONTENT_FAIL;
            recovery = MAIL_CONTENT_FAIL;
        }

        // メールに記載するシステム名
        Map<String, String> placeHolders = new LinkedHashMap<>();
        placeHolders.put("SYSTEM", system);
        placeHolders.put("TIME", time);
        placeHolders.put("SERVER", server);
        placeHolders.put("ORDERCODE", orderCode);
        placeHolders.put("PROCESS", process);
        placeHolders.put("MULPAY_PROCESS", "改訂用取引ID：" + transactionRevisionId);
        placeHolders.put("ERROR", error);
        placeHolders.put("RECOVERY", recovery);
        SettlementMismatchRequest request = new SettlementMismatchRequest();
        request.setPlaceHolders(placeHolders);

        // メールを送信する
        notificationSubAdapter.settlementMisMatch(request);

        LOGGER.info("請求不整合報告メールを送信しました。改訂用取引ID：" + transactionRevisionId);
    }
}