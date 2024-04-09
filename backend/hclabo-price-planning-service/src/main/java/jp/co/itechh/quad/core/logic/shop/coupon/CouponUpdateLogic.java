/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;

/**
 * クーポン更新ロジックのインタフェースクラス。
 * <pre>
 * クーポン管理画面の更新処理から利用する。
 * </pre>
 *
 * @author Kimura Kanae (itec)
 */
public interface CouponUpdateLogic {

    /** 修正対象のクーポンが事前に修正されていた場合エラー（排他） */
    public static final String MSGCD_EXCLUSION_ERROR = "LCP000201";

    /**
     * クーポン更新処理。
     * <pre>
     * クーポン情報を元にクーポンテーブルに新規反映。
     * クーポンインデックスに更新反映。
     * </pre>
     *
     * @param coupon 更新対象クーポン
     */
    void execute(CouponEntity coupon);
}