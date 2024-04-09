/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.order.OrderMessageDto;
import jp.co.itechh.quad.admin.entity.order.goods.OrderGoodsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 処理履歴詳細モデル
 *
 * @author kimura
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderHistoryDetailsModel extends AbstractOrderDetailsModel {

    /** コンストラクタ */
    public OrderHistoryDetailsModel() {
        // 変更箇所特定オブジェクト名 設定
        diffObjectNameOrderGoods = ApplicationContextUtility.getBean(OrderGoodsEntity.class).getClass().getSimpleName();
    }

    /** 受注詳細共通モデル（修正前の前回の状態） */
    private OrderDetailsCommonModel originalOrderDetailsCommonModel;

    /** 受注詳細共通モデル（修正後の最新の状態） */
    private OrderDetailsCommonModel modifiedOrderDetailsCommonModel;

    /**
     * modifiedReceiveOrder に処理履歴詳細画面で取得した受注情報が入っている場合は true
     *
     * <pre>
     * 受注詳細修正確認画面　⇒　処理履歴詳細画面　⇒　ブラウザバックで修正実行
     * とすると処理履歴の内容で更新してしまう為、フラグでチェックする
     * </pre>
     */
    private boolean historyDetailsFlag;

    /************************************
     ** Diffスタイル用の項目群<br/>
     ************************************/

    /** 差分スタイルクラス */
    private String DIFF_STYLE_CLASS = "diff";

    /** デフォルトスタイルクラス */
    private String DEFAULT_STYLE_CLASS = "";

    /** 受注サマリ（OrderSummaryEntity）の変更箇所リスト */
    private List<String> modifiedOrderSummaryList;

    /** 受注お客様（OrderPersonEntity）の変更箇所リスト */
    private List<String> modifiedOrderPersonList;

    /** 受注配送（OrderdeliveryEntity）の変更箇所リスト */
    private List<String> modifiedOrderDeliveryList;

    /** 受注配送（DeliveryMethodEntity）の変更箇所リスト */
    private List<String> modifiedDeliveryMethod;

    /** 受注商品（OrderGoodsEntity）の変更箇所リスト */
    private List<List<String>> modifiedOrderGoodsList;

    /** 受注決済（OrderSettlementEntity）の変更箇所リスト */
    private List<String> modifiedOrderSettlementList;

    /** 受注追加料金（OrderAdditionalChargeEntity）の変更箇所リスト */
    private List<List<String>> modifiedAdditionalChargeList;

    /** 受注請求（OrderBillEntity）の変更箇所リスト */
    private List<String> modifiedOrderBillList;

    /** 受注メモ（OrderMemoEntity）の変更箇所リスト */
    private List<String> modifiedMemoList;

    /** 受注インデックス（OrderIndexEntity）の変更箇所リスト */
    private List<String> modifiedOrderIndexList;

    /** 受注決済詳細（SettlementMethodEntity）の変更箇所リスト */
    private List<String> modifiedSettlementMethodList;

    /** マルチペイメント請求（MulPayBillEntity）の変更箇所リスト */
    private List<String> modifiedMulPayBillList;

    /** 請求先（OrderdeliveryEntity）の変更箇所リスト ※フェーズ2の新規項目 */
    private List<String> modifiedBillingAddressList;

    /** 受注商品 */
    public final String diffObjectNameOrderGoods;

    /** クーポン割引額 */
    private String diffCouponDiscountPriceClass;

    /** クーポン利用制限対象種別 */
    private String diffCouponLimitTargetTypeClass;

    /**
     * ダイナミックプロパティ<br/>
     * 商品数量が変更されてたら修正用のスタイルシート名を返します。<br/>
     * 上記項目はEntityには無く、ユーティリティで判定する
     *
     * @return スタイルシート名
     */
    public String getDiffGoodsCountTotalStyleClass() {
        if (this.originalOrderDetailsCommonModel != null
            && this.originalOrderDetailsCommonModel.getOrderSlipResponse() != null
            && this.modifiedOrderDetailsCommonModel.getOrderSlipResponse() != null) {
            if (this.originalOrderDetailsCommonModel.getOrderSlipResponse().getTotalItemCount() != null
                && this.modifiedOrderDetailsCommonModel.getOrderSlipResponse().getTotalItemCount() != null) {
                BigDecimal originalGoodsCountTotal =
                                new BigDecimal(this.originalOrderDetailsCommonModel.getOrderSlipResponse()
                                                                                   .getTotalItemCount());
                BigDecimal modifyGoodsCountTotal =
                                new BigDecimal(this.modifiedOrderDetailsCommonModel.getOrderSlipResponse()
                                                                                   .getTotalItemCount());
                if (originalGoodsCountTotal.compareTo(modifyGoodsCountTotal) != 0) {
                    return DIFF_STYLE_CLASS;
                }
            }
        }
        return DEFAULT_STYLE_CLASS;
    }

    /** 異常値の項目の表示スタイル */
    private String errStyleClass;

    /** エラー内容 */
    private String errContent;

    /** 注文チェックメッセージDTO */
    private OrderMessageDto orderMessageDto;

    /**
     * ステータスがキャンセルかどうか
     *
     * @return the cancelFlag is ON or OFF
     */
    public boolean isStateCancel() {
        if (cancelTime != null && cancelTime.compareTo(getProcessTime()) <= 0) {
            return true;
        }
        return false;
    }
}