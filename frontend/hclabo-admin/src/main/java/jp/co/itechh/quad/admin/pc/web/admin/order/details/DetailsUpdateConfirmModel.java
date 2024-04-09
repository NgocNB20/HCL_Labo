/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.diff.DiffIgnore;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 受注詳細修正確認モデル
 *
 * @author yt23807
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DetailsUpdateConfirmModel extends AbstractOrderDetailsModel {

    /** 変更箇所リスト */
    @DiffIgnore
    private List<String> diffList;

    /**
     * 改訂前レスポンス保持モデル<br/>
     * ※OrderDetailsCommonModel継承
     */
    protected static class PreRevisionResponseTmpModel extends OrderDetailsCommonModel {
        // 親クラスと全く同じ
    }

    /**
     * 改訂後レスポンス保持モデル<br/>
     * ※OrderDetailsCommonModel継承
     */
    @Data
    protected static class RevisedResponseTmpModel extends OrderDetailsCommonModel {
        /** 改訂用取引レスポンス */
        TransactionForRevisionResponse transactionForRevisionResponse;

        /** 改訂用配送伝票レスポンス */
        ShippingSlipResponse shippingSlipForRevisionResponse;

        /** 改訂用請求伝票レスポンス */
        BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipForRevisionResponse;

        /** 改訂用販売伝票レスポンス */
        GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse;

        /** 改訂用注文票レスポンス */
        OrderSlipForRevisionResponse orderSlipForRevisionResponse;

        @Override
        @Deprecated
        public ShippingSlipResponse getShippingSlipResponse() {
            throwException();
            return null;
        }

        @Override
        @Deprecated
        public void setShippingSlipResponse(ShippingSlipResponse shippingSlipResponse) {
            throwException();
        }

        @Override
        @Deprecated
        public BillingSlipResponse getBillingSlipResponse() {
            throwException();
            return null;
        }

        @Override
        @Deprecated
        public void setBillingSlipResponse(BillingSlipResponse billingSlipResponse) {
            throwException();
        }

        @Override
        @Deprecated
        public SalesSlipResponse getSalesSlipResponse() {
            throwException();
            return null;
        }

        @Override
        @Deprecated
        public void setSalesSlipResponse(SalesSlipResponse salesSlipResponse) {
            throwException();
        }

        @Override
        @Deprecated
        public OrderSlipResponse getOrderSlipResponse() {
            throwException();
            return null;
        }

        @Override
        @Deprecated
        public void setOrderSlipResponse(OrderSlipResponse orderSlipResponse) {
            throwException();
        }

        private void throwException() {
            throw new IllegalStateException("不正な呼び出しです。改訂用モデルに改訂前伝票のレスポンスは参照できません。");
        }
    }

}