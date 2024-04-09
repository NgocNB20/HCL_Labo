/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.coupon.CouponDao;
import jp.co.itechh.quad.core.dao.shop.coupon.CouponIndexDao;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponIndexEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponUpdateLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * クーポン更新ロジックの実装クラス。
 *
 * @author Kimura Kanae (itec)
 */
@Component
public class CouponUpdateLogicImpl extends AbstractShopLogic implements CouponUpdateLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponUpdateLogicImpl.class);

    /** クーポンDAO */
    private final CouponDao couponDao;

    /** クーポンインデックスDAo */
    private final CouponIndexDao couponIndexDao;

    @Autowired
    public CouponUpdateLogicImpl(CouponDao couponDao, CouponIndexDao couponIndexDao) {
        this.couponDao = couponDao;
        this.couponIndexDao = couponIndexDao;
    }

    /**
     * クーポン更新処理。
     *
     * @param coupon 更新対象クーポン
     */
    @Override
    public void execute(CouponEntity coupon) {

        // クーポンインデックのクーポン枝番を利用して排他制御を行う為、
        // クーポンインデックスの更新, クーポンの登録の順で処理を行う
        /*
         * クーポンインデックステーブルに更新を行う
         */
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();

        CouponIndexEntity couponIndex = ApplicationContextUtility.getBean(CouponIndexEntity.class);
        // クーポンSEQ
        couponIndex.setCouponSeq(coupon.getCouponSeq());
        // クーポン枝番
        couponIndex.setCouponVersionNo(coupon.getCouponVersionNo());
        // shopSEQ
        couponIndex.setShopSeq(coupon.getShopSeq());
        // 登録時間
        couponIndex.setRegistTime(coupon.getRegistTime());
        // 更新時間
        couponIndex.setUpdateTime(currentTime);

        try {
            // クーポンインデックスにクーポンインデックス情報を更新する
            couponIndexDao.update(couponIndex);
        } catch (DataAccessException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 更新対象が他の人に更新されていたので排他エラーを投げる
            throwMessage(MSGCD_EXCLUSION_ERROR);
        }

        /*
         * クーポンテーブルに新規登録を行う
         */
        // クーポンインデックスのクーポン枝番、更新日時をセットする
        // クーポン枝番はS2Daoにより自動的インクリメントされている
        coupon.setCouponVersionNo(couponIndex.getCouponVersionNo());
        coupon.setUpdateTime(couponIndex.getUpdateTime());
        // クーポンテーブルに更新後クーポン情報を登録する
        couponDao.insert(coupon);
    }
}