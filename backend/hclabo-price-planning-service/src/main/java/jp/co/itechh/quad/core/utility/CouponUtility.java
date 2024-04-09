/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import org.springframework.stereotype.Component;

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

}
