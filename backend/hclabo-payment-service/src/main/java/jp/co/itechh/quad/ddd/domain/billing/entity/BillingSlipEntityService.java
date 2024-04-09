/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodGetLogic;
import jp.co.itechh.quad.core.thymeleaf.NumberConverterViewUtil;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 請求伝票サービス
 */
@Service
public class BillingSlipEntityService {

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
    public BillingSlipEntityService(IShippingAdapter shippingAdapter,
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
     * @param billingSlipEntity  対象請求伝票
     * @param orderPaymentEntity 対象注文決済
     */
    public void checkBilling(BillingSlipEntity billingSlipEntity, OrderPaymentEntity orderPaymentEntity) {

        // 入力チェック
        AssertChecker.assertNotNull("billingSlipEntity is null", billingSlipEntity);
        AssertChecker.assertNotNull("orderPaymentEntity is null", orderPaymentEntity);

        // チェックエラーリスト
        ApplicationException applicationException = new ApplicationException();

        if (StringUtils.isBlank(orderPaymentEntity.getPaymentMethodId())) {

            // 決済方法IDが設定されていない場合
            applicationException.addMessage("VALIDATE_REQUIRED_SELECT", new String[] {"お支払い方法"});
            // 後続チェックは不要
            throw applicationException;
        }
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
        checkTargetShippingMethod(billingSlipEntity.getTransactionId(), paymentMethodDbEntity, applicationException);

        // リンク決済でない場合は、決済可能金額をチェック
        checkPaymentAmount(billingSlipEntity.getTransactionId(), paymentMethodDbEntity, applicationException);

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
        // 公開中以外の場合はエラー
        // TODO 削除にすると現状画面が真っ白になる #166 起票済 ※非公開は動作確認済
        if (openStatus != HTypeOpenDeleteStatus.OPEN) {
            applicationException.addMessage("PAYMENT_BLSS0002-E");
        }
    }

    /**
     * 配送伝票を取得し、決済方法に紐づく配送方法をチェック
     *
     * @param transactionId         取引ID
     * @param paymentMethodDbEntity 決済方法
     * @param applicationException  妥当性チェックエラー発生時にスローされる例外
     */
    private void checkTargetShippingMethod(String transactionId,
                                           SettlementMethodEntity paymentMethodDbEntity,
                                           ApplicationException applicationException) {

        // 物流サービスから配送伝票を取得
        ShippingSlip shippingSlip = this.shippingAdapter.getShippingSlip(transactionId);
        // 配送伝票が取得できない場合はエラー
        if (shippingSlip == null) {
            throw new DomainException("PAYMENT_BLSS0003-E", new String[] {transactionId});
        }

        // 配送伝票に配送方法IDが設定されていない場合は処理をスキップする
        if (StringUtils.isBlank(shippingSlip.getShippingMethodId())) {
            return;
        }

        // 決済方法に対象配送方法が設定されている場合
        if (paymentMethodDbEntity.getDeliveryMethodSeq() != null) {
            // 決済方法の対象配送方法と配送伝票の配送方法が一致しない場合はエラー
            if (!this.conversionUtility.toString(paymentMethodDbEntity.getDeliveryMethodSeq())
                                       .equals(shippingSlip.getShippingMethodId())) {
                applicationException.addMessage(
                                "PAYMENT_BLSS0004-E",
                                new String[] {paymentMethodDbEntity.getSettlementMethodDisplayNamePC(),
                                                shippingSlip.getShippingMethodName()}
                                               );
            }
        }
    }

    /**
     * 販売伝票を取得し、決済方法の決済可能金額をチェック
     *
     * @param transactionId         取引ID
     * @param paymentMethodDbEntity 決済方法
     * @param applicationException  妥当性チェックエラー発生時にスローされる例外
     */
    private void checkPaymentAmount(String transactionId,
                                    SettlementMethodEntity paymentMethodDbEntity,
                                    ApplicationException applicationException) {

        // 販売企画サービスから販売伝票を取得
        SalesSlip salesSlip = this.saleAdapter.getSalesSlip(transactionId);
        // 販売伝票が取得できない場合はエラー
        if (salesSlip == null) {
            throw new DomainException("PAYMENT_EPAU0001-E", new String[] {transactionId});
        }

        // 決済方法のDBが取得できているのに手数料が計算できていない場合は、金額別手数料の該当区間がない状態 ※最大金額オーバー
        // 送料が決定できずに手数料計算ができない場合は、先に配送伝票チェックでエラーになっている前提
        if (salesSlip.getCommission() == null) {
            applicationException.addMessage("PAYMENT_EPAU0004-E");
        }

        // 販売伝票に請求金額が設定されていない場合は、以降の処理をスキップする
        if (salesSlip.getBillingAmount() == null) {
            return;
        }

        // ショップ別最大決済金額
        String orderMaxAmount = PropertiesUtil.getSystemPropertiesValue("order.max.amount");
        // 注文金額の上限を確認する
        if (salesSlip.getBillingAmount() > Integer.parseInt(orderMaxAmount)) {
            throw new DomainException("PAYMENT_EPAU0003-E", new String[] {this.formatYenPrice(orderMaxAmount)});
        }

        // リンク決済でない場合は、最大最小金額をチェック
        if (ObjectUtils.isNotEmpty(paymentMethodDbEntity.getSettlementMethodType())
            && !HTypeSettlementMethodType.LINK_PAYMENT.equals(paymentMethodDbEntity.getSettlementMethodType())) {

            // 決済方法に最大購入金額が設定されている場合
            if (paymentMethodDbEntity.getMaxPurchasedPrice() != null) {
                // 請求金額が決済方法の最大購入金額を超過している場合はエラー
                if (salesSlip.getBillingAmount() > paymentMethodDbEntity.getMaxPurchasedPrice().intValue()) {
                    applicationException.addMessage(
                                    "PAYMENT_BLSS0006-E",
                                    new String[] {paymentMethodDbEntity.getSettlementMethodDisplayNamePC(),
                                                    String.valueOf(paymentMethodDbEntity.getMaxPurchasedPrice()
                                                                                        .intValue())}
                                                   );
                }
            }

            // 決済方法に最小購入金額が設定されている場合
            if (paymentMethodDbEntity.getMinPurchasedPrice() != null) {
                // 請求金額が決済方法の最小購入金額満の場合はエラー
                if (salesSlip.getBillingAmount() < paymentMethodDbEntity.getMinPurchasedPrice().intValue()) {
                    applicationException.addMessage(
                                    "PAYMENT_BLSS0007-E",
                                    new String[] {paymentMethodDbEntity.getSettlementMethodDisplayNamePC(),
                                                    String.valueOf(paymentMethodDbEntity.getMinPurchasedPrice()
                                                                                        .intValue())}
                                                   );
                }
            }
        }
    }

    /**
     * 数値を指定フォーマットに変換
     *
     * @param maxOrderAmount 変換対象の数値の文字列表現
     * @return 変換結果
     */
    private String formatYenPrice(String maxOrderAmount) {
        NumberConverterViewUtil util = new NumberConverterViewUtil();
        return util.convert(maxOrderAmount, "#,###") + '円';
    }

}
