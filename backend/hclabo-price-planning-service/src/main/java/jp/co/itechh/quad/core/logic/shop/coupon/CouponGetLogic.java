/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;

/**
 * クーポン情報取得ロジックのインタフェースクラス。
 * <pre>
 * クーポン管理画面の一覧画面で選択されたクーポンの詳細情報を取得する為に利用する。
 * </pre>
 *
 * @author Kimura Kanae (itec)
 */
public interface CouponGetLogic {

    /**
     * クーポン情報を取得する。
     * <pre>
     * クーポンSEQよりクーポン情報を取得する。
     * </pre>
     *
     * @param couponSeq クーポンSEQ
     * @return クーポン
     */
    CouponEntity execute(Integer couponSeq);

    /**
     * クーポン情報を取得する。
     * <pre>
     * クーポンSEQよりクーポン情報を取得する。
     * </pre>
     *
     * @param couponSeq クーポンSEQ
     * @param versionNo クーポンSEQ
     * @return クーポン
     */
    CouponEntity execute(Integer versionNo, Integer couponSeq);

}