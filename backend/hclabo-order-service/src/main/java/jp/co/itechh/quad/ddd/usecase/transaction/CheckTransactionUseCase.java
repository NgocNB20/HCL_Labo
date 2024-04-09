/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 取引全体チェックユースケース
 */
@Service
public class CheckTransactionUseCase {

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 販売アダプター */
    private final ISalesSlipAdapter salesAdapter;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** コンストラクタ */
    @Autowired
    public CheckTransactionUseCase(IOrderSlipAdapter orderSlipAdapter,
                                   IShippingSlipAdapter shippingAdapter,
                                   ISalesSlipAdapter salesAdapter,
                                   IBillingSlipAdapter billingAdapter) {
        this.orderSlipAdapter = orderSlipAdapter;
        this.shippingAdapter = shippingAdapter;
        this.salesAdapter = salesAdapter;
        this.billingAdapter = billingAdapter;
    }

    /**
     * 取引全体をチェックする
     *
     * @param transactionId       取引ID
     * @param customerBirthday    顧客生年月日
     * @param contractConfirmFlag 契約確定フラグ
     */
    public void checkTransaction(String transactionId, Date customerBirthday, boolean contractConfirmFlag) {

        // 販売伝票を計算&チェックする（販売企画サービス）
        salesAdapter.calcAndCheckSalesSlip(new TransactionId(transactionId), contractConfirmFlag);

        // 注文票をチェックする（プロモーションサービス）
        orderSlipAdapter.checkDraftOrderSlip(new TransactionId(transactionId), customerBirthday);

        // 配送伝票をチェックする（物流サービス）
        shippingAdapter.checkShippingSlip(new TransactionId(transactionId));

        // 請求伝票をチェックする（決済サービス）
        billingAdapter.checkBillingSlip(new TransactionId(transactionId));
    }
}