/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon.impl;

import jp.co.itechh.quad.core.dao.shop.coupon.CouponDao;
import jp.co.itechh.quad.core.dao.shop.coupon.CouponIndexDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponDeleteLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * クーポン削除ロジック。
 *
 * @author Kimura Kanae (itec)
 */
@Component
public class CouponDeleteLogicImpl extends AbstractShopLogic implements CouponDeleteLogic {

    /** クーポンDAO */
    private final CouponDao couponDao;

    /** クーポンインデックスDAO */
    private final CouponIndexDao couponIndexDao;

    @Autowired
    public CouponDeleteLogicImpl(CouponDao couponDao, CouponIndexDao couponIndexDao) {
        this.couponDao = couponDao;
        this.couponIndexDao = couponIndexDao;
    }

    /**
     * クーポン削除処理。
     *
     * @param couponSeq クーポンSEQ
     * @return 削除件数
     */
    @Override
    public int execute(Integer couponSeq) {

        // クーポンSEQが同じものを全て削除する（変更前の情報も削除する）
        Integer shopSeq = 1001;
        if (couponDao.deleteCoupon(couponSeq, shopSeq) == 0) {
            // 他の人が削除している場合は削除件数が0となり、クーポンインデックスも削除できないため
            return 0;
        }

        // クーポンインデックス情報を削除する
        return couponIndexDao.deleteCouponIndex(couponSeq, shopSeq);

    }
}