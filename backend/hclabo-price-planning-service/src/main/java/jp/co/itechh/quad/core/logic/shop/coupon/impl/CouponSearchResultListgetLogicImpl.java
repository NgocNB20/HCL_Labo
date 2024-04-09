/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon.impl;

import jp.co.itechh.quad.core.dao.shop.coupon.CouponDao;
import jp.co.itechh.quad.core.dto.shop.coupon.CouponSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponSearchResultListgetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * クーポン検索ロジックの実装クラス。
 *
 * @author Kimura Kanae (itec)
 */
@Component
public class CouponSearchResultListgetLogicImpl extends AbstractShopLogic implements CouponSearchResultListgetLogic {

    /** クーポンDAO */
    private final CouponDao couponDao;

    @Autowired
    public CouponSearchResultListgetLogicImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    /**
     * 検索結果を取得する。
     * <pre>
     * 検索条件を元に検索結果を返す。
     * </pre>
     *
     * @param condition 検索条件
     * @return クーポン検索結果リスト
     */
    @Override
    public List<CouponEntity> execute(CouponSearchForDaoConditionDto condition) {

        // shopseqをセットする
        Integer shopSeq = 1001;
        condition.setShopSeq(shopSeq);

        // 検索結果を取得する
        List<CouponEntity> couponList =
                        couponDao.getCouponSearchResultList(condition, condition.getPageInfo().getSelectOptions());

        return couponList;
    }
}