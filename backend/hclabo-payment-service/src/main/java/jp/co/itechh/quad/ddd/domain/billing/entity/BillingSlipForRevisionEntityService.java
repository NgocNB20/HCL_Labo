/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodGetLogic;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlipForRevision;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用請求伝票サービス
 */
@Service
public class BillingSlipForRevisionEntityService {

    /** 配送アダプター */
    private final IShippingAdapter shippingAdapter;

    /** 販売アダプター */
    private final ISalesAdapter saleAdapter;

    /** 決済方法取得ロジック */
    private final SettlementMethodGetLogic settlementMethodGetLogic;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public BillingSlipForRevisionEntityService(IShippingAdapter shippingAdapter,
                                               ISalesAdapter saleAdapter,
                                               SettlementMethodGetLogic settlementMethodGetLogic,
                                               ConversionUtility conversionUtility) {
        this.shippingAdapter = shippingAdapter;
        this.saleAdapter = saleAdapter;
        this.settlementMethodGetLogic = settlementMethodGetLogic;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 請求チェック
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     * @param billingSlipEntity            請求伝票
     */
    public void checkBillingSlipForRevision(BillingSlipForRevisionEntity billingSlipForRevisionEntity,
                                            BillingSlipEntity billingSlipEntity) {
        OrderPaymentEntity orderPaymentEntity = billingSlipEntity.getOrderPaymentEntity();
        OrderPaymentForRevisionEntity orderPaymentForRevisionEntity =
                        billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity();

        // 入力チェック
        AssertChecker.assertNotNull("orderPaymentForRevisionEntity is null", orderPaymentForRevisionEntity);
        AssertChecker.assertNotNull("orderPaymentEntity is null", orderPaymentEntity);

        // チェックエラーリスト
        ApplicationException applicationException = new ApplicationException();

        // 決済方法を取得する
        SettlementMethodEntity paymentMethodDbEntity = this.settlementMethodGetLogic.execute(
                        this.conversionUtility.toInteger(orderPaymentEntity.getPaymentMethodId()));
        // 決済方法が取得できない場合はエラー
        if (paymentMethodDbEntity == null) {
            throw new DomainException("PAYMENT_BLSS0001-E", new String[] {orderPaymentEntity.getPaymentMethodId()});
        }

        // 決済方法公開状態チェック
        checkPaymentMethodStatus(paymentMethodDbEntity.getOpenStatusPC(), applicationException);

        // 決済方法に紐づく配送方法をチェック
        checkTargetShippingMethod(billingSlipForRevisionEntity.getTransactionRevisionId(), paymentMethodDbEntity,
                                  applicationException
                                 );

        if (paymentMethodDbEntity.getSettlementMethodType() != HTypeSettlementMethodType.LINK_PAYMENT) {
            // 決済方法の決済可能金額をチェック
            checkPaymentAmount(billingSlipForRevisionEntity.getTransactionRevisionId(), paymentMethodDbEntity,
                               applicationException
                              );
        }

        // 決済方法変更チェック
        checkPaymentMethodChange(orderPaymentForRevisionEntity, orderPaymentEntity, applicationException);

        // リンク決済金額チェック
        if (paymentMethodDbEntity.getSettlementMethodType() == HTypeSettlementMethodType.LINK_PAYMENT) {
            checkLinkPayAmount(billingSlipForRevisionEntity.getTransactionRevisionId(), billingSlipEntity,
                               applicationException
                              );
        }

        // コンビニ・ペイジー関連のため後回し
        // -------------------------------------------
        // ④コンビニ決済電話番号桁数チェック ※参照：改訂用注文決済 × 住所
        // ⑤クレジット決済変更制約チェック ※参照：改訂用注文決済 × 決済方法
        // ⑥コンビニ決済変更制約チェック ※参照：改訂用注文決済 × 決済方法 × 注文決済
        // ⑦ペイジー決済変更制約チェック ※参照：改訂用注文決済 × 決済方法 × 注文決済

        // TODO クレジット取引状態相違チェック
        //checkCreditDiff();

        if (applicationException.hasMessage()) {
            throw applicationException;
        }
    }

    /**
     * 決済方法公開状態チェック
     *
     * @param openStatus           公開状態
     * @param applicationException 妥当性チェックエラー発生時にスローされる例外
     */
    private void checkPaymentMethodStatus(HTypeOpenDeleteStatus openStatus, ApplicationException applicationException) {
        // 削除の場合はエラー
        if (openStatus == HTypeOpenDeleteStatus.DELETED) {
            applicationException.addMessage("PAYMENT_BLSS0002-E");
        }
    }

    /**
     * 配送伝票を取得し、決済方法に紐づく配送方法をチェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param paymentMethodDbEntity 決済方法
     * @param applicationException  妥当性チェックエラー発生時にスローされる例外
     */
    private void checkTargetShippingMethod(String transactionRevisionId,
                                           SettlementMethodEntity paymentMethodDbEntity,
                                           ApplicationException applicationException) {
        // TODO BillingSlipEntityServiceと処理フローは全く同じ（改訂用か通常用かは異なる）ため将来的に共通化したい

        // 物流サービスから配送伝票を取得
        ShippingSlipForRevision shippingSlipForRevision =
                        this.shippingAdapter.getShippingSlipForRevision(transactionRevisionId);  // 配送伝票が取得できない場合はエラー
        if (shippingSlipForRevision == null) {
            throw new DomainException("PAYMENT_BSRS0003-E", new String[] {transactionRevisionId});
        }

        // 配送伝票に配送方法IDが設定されていない場合は処理をスキップする
        if (StringUtils.isBlank(shippingSlipForRevision.getShippingMethodId())) {
            return;
        }

        // 決済方法に対象配送方法が設定されている場合
        if (paymentMethodDbEntity.getDeliveryMethodSeq() != null) {
            // 決済方法の対象配送方法と配送伝票の配送方法が一致しない場合はエラー
            if (!this.conversionUtility.toString(paymentMethodDbEntity.getDeliveryMethodSeq())
                                       .equals(shippingSlipForRevision.getShippingMethodId())) {
                applicationException.addMessage("PAYMENT_BLSS0004-E",
                                                new String[] {paymentMethodDbEntity.getSettlementMethodDisplayNamePC(),
                                                                shippingSlipForRevision.getShippingMethodName()}
                                               );
            }
        }
    }

    /**
     * 販売伝票を取得し、決済方法の決済可能金額をチェック
     *
     * @param transactionRevisionId 取引ID
     * @param paymentMethodDbEntity 決済方法
     * @param applicationException  妥当性チェックエラー発生時にスローされる例外
     */
    private void checkPaymentAmount(String transactionRevisionId,
                                    SettlementMethodEntity paymentMethodDbEntity,
                                    ApplicationException applicationException) {
        // TODO BillingSlipEntityServiceと処理フローは全く同じ（改訂用か通常用かは異なる）ため将来的に共通化したい

        // 販売企画サービスから改訂用販売伝票を取得
        SalesSlipForRevision salesSlipForRevision = this.saleAdapter.getSalesSlipForRevision(transactionRevisionId);
        // 改訂用販売伝票が取得できない場合はエラー
        if (salesSlipForRevision == null) {
            throw new DomainException("PAYMENT_BSRS0005-E", new String[] {transactionRevisionId});
        }

        // 手数料チェック
        checkCommission(salesSlipForRevision, applicationException);

        // 改訂用販売伝票に請求金額が設定されていない場合は、以降の処理をスキップする
        if (salesSlipForRevision.getBillingAmount() == null) {
            return;
        }

        // 決済方法に最大購入金額が設定されている場合
        if (paymentMethodDbEntity.getMaxPurchasedPrice() != null) {
            // 請求金額が決済方法の最大購入金額を超過している場合はエラー
            if (salesSlipForRevision.getBillingAmount() > paymentMethodDbEntity.getMaxPurchasedPrice().intValue()) {
                applicationException.addMessage("PAYMENT_BLSS0006-E",
                                                new String[] {paymentMethodDbEntity.getSettlementMethodDisplayNamePC(),
                                                                String.valueOf(paymentMethodDbEntity.getMaxPurchasedPrice()
                                                                                                    .intValue())}
                                               );
            }
        }

        // 決済方法に最小購入金額が設定されている場合
        if (paymentMethodDbEntity.getMinPurchasedPrice() != null) {
            // 請求金額が決済方法の最小購入金額満の場合はエラー
            if (salesSlipForRevision.getBillingAmount() < paymentMethodDbEntity.getMinPurchasedPrice().intValue()) {
                applicationException.addMessage("PAYMENT_BLSS0007-E",
                                                new String[] {paymentMethodDbEntity.getSettlementMethodDisplayNamePC(),
                                                                String.valueOf(paymentMethodDbEntity.getMinPurchasedPrice()
                                                                                                    .intValue())}
                                               );
            }
        }
    }

    /**
     * 決済方法変更チェック
     *
     * @param orderPaymentForRevisionEntity 改訂用注文決済
     * @param orderPaymentEntity            注文決済
     * @param applicationException          妥当性チェックエラー発生時にスローされる例外
     */
    private void checkPaymentMethodChange(OrderPaymentForRevisionEntity orderPaymentForRevisionEntity,
                                          OrderPaymentEntity orderPaymentEntity,
                                          ApplicationException applicationException) {

        // 決済方法を変更場合はエラー
        if (StringUtils.isNotEmpty(orderPaymentForRevisionEntity.getPaymentMethodId())
            && !orderPaymentForRevisionEntity.getPaymentMethodId().equals(orderPaymentEntity.getPaymentMethodId())) {
            applicationException.addMessage("PAYMENT_BLSS0010-E");
        }
    }

    /**
     * リンク決済金額チェック
     *
     * @param transactionRevisionId 取引ID
     * @param billingSlipEntity     請求伝票
     * @param applicationException  妥当性チェックエラー発生時にスローされる例外
     */
    private void checkLinkPayAmount(String transactionRevisionId,
                                    BillingSlipEntity billingSlipEntity,
                                    ApplicationException applicationException) {

        // 販売企画サービスから改訂用販売伝票を取得
        SalesSlipForRevision salesSlipForRevision = this.saleAdapter.getSalesSlipForRevision(transactionRevisionId);
        // 改訂用販売伝票が取得できない場合はエラー
        if (salesSlipForRevision == null) {
            throw new DomainException("PAYMENT_BSRS0005-E", new String[] {transactionRevisionId});
        }

        // 手数料チェック
        checkCommission(salesSlipForRevision, applicationException);

        // リンク決済の場合、決済金額が変更された場合はエラー
        if (salesSlipForRevision.getBillingAmount() != null && !salesSlipForRevision.getBillingAmount()
                                                                                    .equals(billingSlipEntity.getBilledPrice())) {
            applicationException.addMessage("PAYMENT_BLSS0011-E");
        }
    }

    /**
     * 手数料チェック
     *
     * @param salesSlipForRevision 改訂用販売伝票
     * @param applicationException 妥当性チェックエラー発生時にスローされる例外
     */
    private void checkCommission(SalesSlipForRevision salesSlipForRevision, ApplicationException applicationException) {

        // 決済方法のDBが取得できているのに手数料が計算できていない場合は、金額別手数料の該当区間がない状態 ※最大金額オーバー
        // 送料が決定できずに手数料計算ができない場合は、先に配送伝票チェックでエラーになっている前提
        if (salesSlipForRevision.getCommission() == null) {
            applicationException.addMessage("PAYMENT_EPAU0005-E");
        }
    }

}
