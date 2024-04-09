/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

/**
 * クーポン削除ロジックのインタフェースクラス。
 * <pre>
 * クーポン管理画面の削除処理から利用する。
 * </pre>
 *
 * @author Kimura Kanae (itec)
 */
public interface CouponDeleteLogic {

    /**
     * 指定したクーポンSEQのクーポンテーブルの情報を全て削除する。
     * <pre>
     * 指定したクーポンSEQのクーポンインデックステーブルの情報を削除する。
     * </pre>
     *
     * @param couponSeq クーポンSEQ
     * @return 削除件数
     */
    int execute(Integer couponSeq);

}
