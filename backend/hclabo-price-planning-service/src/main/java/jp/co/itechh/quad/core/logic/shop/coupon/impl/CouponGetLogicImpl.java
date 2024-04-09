/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon.impl;

import jp.co.itechh.quad.core.dao.shop.coupon.CouponDao;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * クーポン取得ロジックの実装クラス。。
 *
 * @author Kimura Kanae (itec)
 */
@Component
public class CouponGetLogicImpl extends AbstractShopLogic implements CouponGetLogic {

    /** クーポンDAO */
    private final CouponDao couponDao;

    @Autowired
    public CouponGetLogicImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    /**
     * クーポンSEQからクーポン情報を取得する。
     *
     * @param couponSeq クーポンSEQ
     * @return クーポン
     */
    @Override
    public CouponEntity execute(Integer couponSeq) {

        // shopseqを取得する
        Integer shopSeq = 1001;

        // クーポン情報を取得する
        return couponDao.getCouponByCouponSeq(couponSeq, shopSeq);
    }

    @Override
    public CouponEntity execute(Integer couponSeq, Integer versionNo) {

        // 受注履歴の取得時に利用する
        return couponDao.getCouponByCouponVersionNo(couponSeq, versionNo);
    }
}