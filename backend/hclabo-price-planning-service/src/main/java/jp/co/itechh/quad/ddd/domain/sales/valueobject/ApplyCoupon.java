/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPrice;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * クーポン 値オブジェクト
 */
@Getter
public class ApplyCoupon {

    /** クーポンコード */
    private final String couponCode;

    /** クーポンSEQ */
    private final Integer couponSeq;

    /** クーポン連番 */
    private final Integer couponVersionNo;

    /** クーポン名 */
    private final String couponName;

    /** クーポン支払い額 */
    // TODO クーポン支払い額は、クーポン値オブジェクトの外に出したい
    //  他のフィールドはクーポン適用時・最新化時に生成される項目で、本項目は料金計算の責務となる（セットタイミングが異なる）
    //  値オブジェクトは不変性を持たないといけない（計算のたびに、商品・送料などの影響で支払い額だけ変わるのはNG）
    private final CouponPaymentPrice couponPaymentPrice;

    /** クーポン利用フラグ */
    private final boolean couponUseFlag;

    /** クーポンコードの最大文字列長 */
    private static final int LENGTH_COUPON_CODE_MAXIMUM = 20;

    /** コンストラクタ */
    public ApplyCoupon(String couponCode,
                       Integer couponSeq,
                       Integer couponVersionNo,
                       String couponName,
                       CouponPaymentPrice couponPaymentPrice,
                       boolean couponUseFlag) {

        // チェック
        if (StringUtils.isNotBlank(couponCode)) {
            if (couponCode.length() > LENGTH_COUPON_CODE_MAXIMUM) {
                throw new DomainException("PRICE-PLANNING-COPE0005-E");
            }
            AssertChecker.assertNotNull("couponSeq is null", couponSeq);
            AssertChecker.assertNotNull("couponVersionNo is null", couponVersionNo);
            AssertChecker.assertNotNull("couponName is null", couponName);
        } else {
            if (couponUseFlag) {
                throw new DomainException("PRICE-PLANNING-COPE0004-E");
            }
        }

        AssertChecker.assertNotNull("ApplyCoupon : couponPaymentPrice is null", couponPaymentPrice);

        // 設定
        this.couponCode = couponCode;
        this.couponPaymentPrice = couponPaymentPrice;
        this.couponVersionNo = couponVersionNo;
        this.couponName = couponName;
        this.couponSeq = couponSeq;
        this.couponUseFlag = couponUseFlag;
    }

}
