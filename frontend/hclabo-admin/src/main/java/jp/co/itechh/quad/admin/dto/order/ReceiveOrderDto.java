/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.dto.order.delivery.OrderDeliveryDto;
import jp.co.itechh.quad.admin.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.admin.entity.order.OrderSummaryEntity;
import jp.co.itechh.quad.admin.entity.order.additional.OrderAdditionalChargeEntity;
import jp.co.itechh.quad.admin.entity.order.bill.OrderBillEntity;
import jp.co.itechh.quad.admin.entity.order.bill.OrderReceiptOfMoneyEntity;
import jp.co.itechh.quad.admin.entity.order.index.OrderIndexEntity;
import jp.co.itechh.quad.admin.entity.order.member.SimultaneousOrderExclusionEntity;
import jp.co.itechh.quad.admin.entity.order.memo.OrderMemoEntity;
import jp.co.itechh.quad.admin.entity.order.orderperson.OrderPersonEntity;
import jp.co.itechh.quad.admin.entity.order.settlement.OrderSettlementEntity;
import jp.co.itechh.quad.admin.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 受注Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class ReceiveOrderDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注サマリエンティティ */
    private OrderSummaryEntity orderSummaryEntity;

    /** 受注インデックスエンティティ */
    private OrderIndexEntity orderIndexEntity;

    /** 受注ご注文主エンティティ */
    private OrderPersonEntity orderPersonEntity;

    /** 受注配送Dto */
    private OrderDeliveryDto orderDeliveryDto;

    /** 受注決済エンティティ */
    private OrderSettlementEntity orderSettlementEntity;

    /** 2回目受注決済エンティティ */
    private OrderSettlementEntity orderNextSettlementEntity;

    /** 受注追加料金エンティティリスト */
    private List<OrderAdditionalChargeEntity> orderAdditionalChargeEntityList;

    /** 受注請求エンティティ */
    private OrderBillEntity orderBillEntity;

    /** 受注入金エンティティリスト */
    private List<OrderReceiptOfMoneyEntity> orderReceiptOfMoneyEntityList;

    /** 受注メモエンティティ */
    private OrderMemoEntity orderMemoEntity;

    /** 決済方法エンティティ */
    private SettlementMethodEntity settlementMethodEntity;

    /** 2回目決済方法エンティティ */
    private SettlementMethodEntity nextSettlementMethodEntity;

    /** マルチペイ請求 */
    private MulPayBillEntity mulPayBillEntity;

    /** 注文開始時に取得したマスタ情報 */
    private OrderInfoMasterDto masterDto;

    /** 同時注文排他情報 */
    private SimultaneousOrderExclusionEntity simultaneousOrderExclusionEntity;

    /**
     * クーポンエンティティ。<br />
     * <pre>
     * 注文に対して適用しているクーポン。
     * 未適用時はnull。
     * </pre>
     */
    private CouponEntity coupon;

    /**
     * クーポン適用フラグ<br/>
     * 決済方法選択：クーポン適用ボタンを押下した場合
     * trueとする。
     *
     * クーポンチェックに使用
     *
     */
    private boolean applyCouponFlg = false;

    /** 受注一時情報Dto */
    private OrderTempDto orderTempDto;

    /** 受注その他情報Dto */
    private OrderOtherDataDto orderOtherDataDto;

    /** オーソリ期限日（決済日付＋オーソリ保持期間） */
    private Timestamp authoryLimitDate;

    /**
     * 再オーソリフラグ
     * true:再オーソリ処理時 false：通常受注修正時
     */
    private boolean reAuthoryFlag = false;

    /**
     * カード登録、変更により定期注文リカバリフラグ
     * true:カード登録、変更により定期注文リカバリ false：通常受注修正時
     */
    private boolean recoveryAuthoryFlag = false;

    /**
     * 3Dセキュアを必要とする注文かを判定
     *
     * @return true..必要
     */
    public boolean is3DSecureOrder() {
        return mulPayBillEntity != null && "1".equals(mulPayBillEntity.getAcs());
    }

    /**
     * クーポンコード。<br />
     * <pre>
     * 画面で入力されたクーポンコード。
     * 料金計算処理内でクーポンの利用可否を判定する為に必要。
     * </pre>
     */
    private String couponCode;

    /**
     * 前回決済手数料<br/>
     */
    private BigDecimal originalCommission;

    /**
     * 前回送料<br/>
     */
    private BigDecimal originalCarriage;

    /** 納品書要否保持フラグ */
    private HTypeInvoiceAttachmentFlag invoiceAttachmentSetFlag = HTypeInvoiceAttachmentFlag.OFF;

    /** 2回目お届け日 */
    private Timestamp nextDeliveryDateTime;

    /** 次回送料 */
    private BigDecimal nextCarriage;

    /** 注文保留理由 */
    private String orderWaitingMemo;

    /** 受注状態が出荷済み
     * @return true:出荷済み false:出荷済み以外
     *  */
    public boolean isShipment() {
        return HTypeOrderStatus.SHIPMENT_COMPLETION == orderSummaryEntity.getOrderStatus();
    }
}