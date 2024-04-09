/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction.service;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.CancelResult;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 改訂用取引取消ユースケース内部ロジック（取引確定も行う）<br/>
 * ※親トランザクションがある場合の呼び出し用（ユースケースから取引改訂確定ユースケース呼出したい場合に利用）
 */
@Service
public class CancelTransactionForRevisionUseCaseExecuter {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CancelTransactionForRevisionUseCaseExecuter.class);

    /** 改訂取引用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送伝票アダプター */
    private final IShippingSlipAdapter shippingSlipAdapter;

    /** 請求伝票アダプター */
    private final IBillingSlipAdapter billingSlipAdapter;

    /** 販売伝票アダプター */
    private final ISalesSlipAdapter salesSlipAdapter;

    /** 取引改訂確定ユースケース内部ロジック */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 請求不整合エラーメール文言取得失敗用 */
    private final static String MAIL_CONTENT_FAIL = "(設定失敗)";

    /** コンストラクタ */
    @Autowired
    public CancelTransactionForRevisionUseCaseExecuter(ITransactionForRevisionRepository transactionForRevisionRepository,
                                                       ITransactionRepository transactionRepository,
                                                       IOrderReceivedRepository orderReceivedRepository,
                                                       IOrderSlipAdapter orderAdapter,
                                                       IShippingSlipAdapter shippingAdapter,
                                                       IBillingSlipAdapter billingAdapter,
                                                       ISalesSlipAdapter salesAdapter,
                                                       OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter,
                                                       INotificationSubAdapter notificationSubAdapter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.transactionRepository = transactionRepository;
        this.orderReceivedRepository = orderReceivedRepository;
        this.orderSlipAdapter = orderAdapter;
        this.shippingSlipAdapter = shippingAdapter;
        this.billingSlipAdapter = billingAdapter;
        this.salesSlipAdapter = salesAdapter;
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
        this.notificationSubAdapter = notificationSubAdapter;
    }

    /**
     * 取引改訂を取消して改訂確定する内部ロジック（DB楽観ロックチェックあり）
     *
     * @param transactionRevisionId                改訂取引ID
     * @param adminMemo                            管理メモ
     * @param messageMapForResponse                警告メッセージ
     * @return OrderReceivedEntity
     * @return
     */
    public OrderReceivedEntity cancelTransactionForRevisionInnerLogic(String transactionRevisionId,
                                                                      String adminMemo,
                                                                      Map<String, List<WarningContent>> messageMapForResponse) {

        // 改訂用取引を取得
        TransactionRevisionId transactionRevisionIdVo = new TransactionRevisionId(transactionRevisionId);
        TransactionForRevisionEntity transactionForRevisionEntity =
                        transactionForRevisionRepository.get(transactionRevisionIdVo);
        if (transactionForRevisionEntity == null) {
            // 取引取得失敗
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
        }

        // 受注を取得
        OrderReceivedEntity orderReceivedEntity =
                        orderReceivedRepository.get(transactionForRevisionEntity.getOrderReceivedId());
        if (orderReceivedEntity == null) {
            // 受注取得失敗
            throw new DomainException("ORDER-ODER0002-E",
                                      new String[] {transactionForRevisionEntity.getOrderReceivedId().getValue()}
            );
        }

        // 改訂用注文票を取消する
        orderSlipAdapter.cancelOrderSlipForRevision(transactionRevisionIdVo);

        // 改訂用販売伝票を取消する
        salesSlipAdapter.cancelSalesSlipForRevision(transactionRevisionIdVo);

        // 改訂用取引を取消する
        transactionForRevisionEntity.cancelTransaction(adminMemo, transactionForRevisionEntity.isPaidFlag(),
                                                       transactionForRevisionEntity.getPaymentStatusDetail()
                                                      );

        // 改訂用取引を更新する
        transactionForRevisionRepository.update(transactionForRevisionEntity);

        //　受注へ取消日時を設定する
        orderReceivedEntity.settingCancelDate();

        // 受注を更新する（楽観的排他チェックあり）
        orderReceivedRepository.updateWithTranCheck(
                        orderReceivedEntity, transactionForRevisionEntity.getTransactionId().getValue());

        // 改訂用取引を確定する（「改訂用請求伝票を確定」「改訂用配送伝票を確定」をスキップする）
        orderReceivedEntity =
                        openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(transactionRevisionId,
                                                                                             null,
                                                                                             HTypeProcessType.CANCELLATION,
                                                                                             true, true, true
                                                                                            );

        // 改訂用請求伝票を取消する
        CancelResult cancelResult = billingSlipAdapter.cancelBillingSlipForRevision(transactionRevisionIdVo, true);
        // 改訂用請求伝票のレスポンスがない場合
        if (cancelResult == null) {
            throw new DomainException("ORDER-BILS0001-E", new String[] {transactionRevisionId});
        }

        // APIレスポンスに「警告メッセージ」を追加
        if (messageMapForResponse != null && MapUtils.isNotEmpty(cancelResult.getWarningMessage())) {
            messageMapForResponse.putAll(cancelResult.getWarningMessage());
        }

        try {
            // 改訂用請求伝票結果から入金状態設定
            if (transactionForRevisionEntity.isPaidFlag() != cancelResult.isPaidFlag()
                || transactionForRevisionEntity.getPaymentStatusDetail().isInsufficientMoneyFlag()
                   != cancelResult.isInsufficientMoneyFlag()
                || transactionForRevisionEntity.getPaymentStatusDetail().isOverMoneyFlag()
                   != cancelResult.isOverMoneyFlag()) {
                // 取引を取得
                TransactionEntity transactionEntity =
                                transactionRepository.get(new TransactionId(transactionRevisionIdVo.getValue()));
                // 取引が取得できない場合エラー
                if (transactionEntity == null) {
                    // 取引取得失敗
                    throw new DomainException("ORDER-TRAN0007-E", new String[] {transactionRevisionIdVo.getValue()});
                }

                // 入金状態設定
                transactionEntity.paymentStatusSetting(cancelResult.isPaidFlag(),
                                                       new PaymentStatusDetail(cancelResult.isInsufficientMoneyFlag(),
                                                                               cancelResult.isOverMoneyFlag()
                                                       )
                                                      );

                // デポジット更新後改訂用取引を更新する。
                transactionRepository.update(transactionEntity);
            }

            // 改訂用配送伝票を取消する
            shippingSlipAdapter.cancelShippingSlipForRevision(transactionRevisionIdVo, true);

        } catch (Exception e) {

            LOGGER.error("改訂用取引取消実行中にエラーが発生しました。 -- 改訂用取引ID：" + transactionRevisionIdVo.getValue() + "-- 受注番号："
                         + orderReceivedEntity.getOrderCode().getValue(), e);

            //　決済済みの場合は請求不整合エラーメール送信
            if (cancelResult.isGmoCommunicationFlag()) {

                String orderCodeSendMail = MAIL_CONTENT_FAIL;
                if (orderReceivedEntity.getOrderCode() != null
                    && orderReceivedEntity.getOrderCode().getValue() != null) {
                    orderCodeSendMail = orderReceivedEntity.getOrderCode().getValue();
                }

                sendReportMail(orderCodeSendMail, transactionRevisionId);

                throw new DomainException("PAYMENT_EPAR0006-E");
            }

            throw e;
        }

        return orderReceivedEntity;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcessInnerLogic(OrderReceivedEntity orderReceivedEntity) {
        openTransactionReviseUseCaseExecuter.asyncAfterProcessInnerLogic(orderReceivedEntity);
    }

    /**
     * 請求不整合報告メールを送信する
     *
     * @param orderCode             受注番号
     * @param transactionRevisionId 改訂用取引ID
     */
    private void sendReportMail(String orderCode, String transactionRevisionId) {

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
            process = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.processtype.cancelslip");
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

        Map<String, String> placeHolders = new LinkedHashMap<>();
        // メールに記載するシステム名
        placeHolders.put("SYSTEM", system);
        placeHolders.put("TIME", time);
        placeHolders.put("SERVER", server);
        placeHolders.put("ORDERCODE", orderCode);
        placeHolders.put("PROCESS", process);
        placeHolders.put("MULPAY_PROCESS", "改訂用取引ID：" + transactionRevisionId);
        placeHolders.put("ERROR", error);
        placeHolders.put("RECOVERY", recovery);

        // メールを送信する
        SettlementMismatchRequest request = new SettlementMismatchRequest();
        request.setPlaceHolders(placeHolders);

        notificationSubAdapter.settlementMisMatch(request);

        LOGGER.info("請求不整合報告メールを送信しました。改訂用取引ID：" + transactionRevisionId);
    }
}