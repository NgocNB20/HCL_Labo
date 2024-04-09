/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;

/**
 * クーポン新規登録ロジックのインタフェースクラス。
 * <pre>
 * クーポン管理画面の登録処理から利用する。
 * </pre>
 *
 * @author Kimura Kanae (itec)
 */
public interface CouponRegistLogic {

    /**
     * クーポン新規登録処理。
     * <pre>
     * クーポン情報を元にクーポンテーブル新規反映。
     * クーポンインデックス新規反映。
     * </pre>
     *
     * @param coupon 登録対象のクーポン
     */
    void execute(CouponEntity coupon);

}