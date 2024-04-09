/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.utility;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.dto.order.ReceiveOrderDto;
import jp.co.itechh.quad.admin.entity.order.settlement.OrderSettlementEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * クーポン用のUtilityクラス
 *
 * @author EntityGenerator
 */
@Component
public class CouponUtility {

    /** 自動生成するクーポンコードの桁数 */
    private static final String AUTO_GENERATION_COUPON_CODE_LENGTH = "auto.generation.coupon.code.length";

    /** クーポンコードとして利用可能な文字列 */
    private static final String COUPON_CODE_USABLE_CHARACTER = "coupon.code.usable.character";

    /** クーポンコードが再利用不可能期間（日） */
    private static final String COUPON_CODE_CANT_RECYCLE_TERM = "coupon.code.cant.recycle.term";

    /** 全額クーポン払い時の決済方法 */
    private static final String COUPON_SETTLEMENT_METHOD_SEQ = "coupon.settlement.method.seq";

    /**
     * 選択されている決済方法が全額クーポン決済であるか判定する。
     *
     * @param order 注文情報
     * @return 選択されている決済方法が全額クーポン決済の場合 true
     */
    public boolean isCouponSettlementMethod(ReceiveOrderDto order) {
        OrderSettlementEntity orderSettlementEntity = order.getOrderSettlementEntity();
        Integer settlementSeq = getCouponSettlementMethodSeq();
        return orderSettlementEntity.getSettlementMethodSeq().compareTo(settlementSeq) == 0;
    }

    /**
     * クーポン割引を考慮して決済金額を算出する。<br />
     * ※決済金額とは手数料算出の基になる金額。<br />
     * 商品金額合計＋送料＋その他追加料金+決済手数料+消費税－クーポン割引額
     *
     * @param receiveOrderDto 受注Dto
     * @return 決済金額
     */
    public BigDecimal getSettlementCharge(ReceiveOrderDto receiveOrderDto) {
        OrderSettlementEntity orderSettlementEntity = receiveOrderDto.getOrderSettlementEntity();

        // 割引前受注金額（商品金額合計＋送料＋その他追加料金+決済手数料+消費税）
        BigDecimal beforeDiscountOrderPrice = orderSettlementEntity.getBeforeDiscountOrderPrice();
        // クーポン割引額
        BigDecimal couponDiscountPrice = orderSettlementEntity.getCouponDiscountPrice();
        // 決済金額
        return beforeDiscountOrderPrice.subtract(couponDiscountPrice);
    }

    /**
     * 自動生成するクーポンコードの桁数を取得する
     * @return 自動生成するクーポンコードの桁数
     */
    public Integer getAutoGenerationCouponCodeLength() {
        return PropertiesUtil.getSystemPropertiesValueToInt(AUTO_GENERATION_COUPON_CODE_LENGTH);
    }

    /**
     * クーポンコードとして利用可能な文字列を取得する。
     * @return クーポンコードとして利用可能な文字列
     */
    public String getCouponCodeUsableCharacter() {
        return PropertiesUtil.getSystemPropertiesValue(COUPON_CODE_USABLE_CHARACTER);
    }

    /**
     * クーポンコードが再利用不可能期間（日） を取得する。
     * @return クーポンコードが再利用不可能期間（日）
     */
    public Integer getCouponCodeCantRecycleTerm() {
        return PropertiesUtil.getSystemPropertiesValueToInt(COUPON_CODE_CANT_RECYCLE_TERM);
    }

    /**
     * 全額クーポン払い時の決済方法 SEQを取得する。
     *
     * @return 全額クーポン払い時の決済方法SEQ
     */
    public Integer getCouponSettlementMethodSeq() {
        return PropertiesUtil.getSystemPropertiesValueToInt(COUPON_SETTLEMENT_METHOD_SEQ);
    }

}