/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

import java.sql.Timestamp;

/**
 * クーポン開催日時チェックロジックのインタフェースクラス。
 * <pre>
 * クーポン開始日時と現在＋リードタイムの時間関係を返す。
 * </pre>
 *
 * @author Kimura Kanae (itec)
 *
 */
public interface CouponTimeCheckLogic {

    /**
     * クーポン開催日時と現在との関係をチェックする。
     *
     * @param couponStartTime クーポン開始日時
     * @return 開始日時＞現在時刻のとき-1、開始日時＝現在時刻のとき0、開始日時＜現在時刻のとき+1
     */
    int execute(Timestamp couponStartTime);
}