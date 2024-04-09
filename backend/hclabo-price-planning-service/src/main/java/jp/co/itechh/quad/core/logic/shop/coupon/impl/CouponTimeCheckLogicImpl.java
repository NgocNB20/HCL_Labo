/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon.impl;

import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponTimeCheckLogic;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * クーポン開催日時チェックロジックの実装クラス。
 *
 * @author K.Harada (itec)
 */
@Component
public class CouponTimeCheckLogicImpl extends AbstractShopLogic implements CouponTimeCheckLogic {

    /**
     * クーポン利用開始日時と現在のチェック処理。
     *
     * @param couponStartTime クーポン開始日時
     * @return 開始日時＞現在時刻のとき-1、開始日時＝現在時刻のとき0、開始日時＜現在時刻のとき+1
     */
    @Override
    public int execute(Timestamp couponStartTime) {

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // 利用開始日時と現在時刻の関係を返す
        return couponStartTime.compareTo(currentTime);
    }
}