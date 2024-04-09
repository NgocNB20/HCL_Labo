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
import jp.co.itechh.quad.core.logic.shop.coupon.CouponRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * クーポン新規登録ロジックの実装クラス。
 *
 * @author Kimura Kanae (itec)
 */
@Component
public class CouponRegistLogicImpl extends AbstractShopLogic implements CouponRegistLogic {

    /** クーポンDAO */
    private final CouponDao couponDao;

    /** クーポンインデックスDAO */
    private final CouponIndexDao couponIndexDao;

    @Autowired
    public CouponRegistLogicImpl(CouponDao couponDao, CouponIndexDao couponIndexDao) {
        this.couponDao = couponDao;
        this.couponIndexDao = couponIndexDao;
    }

    /**
     * クーポン新規登録処理。
     *
     * @param couponEntity 登録対象のクーポン情報
     */
    @Override
    public void execute(CouponEntity couponEntity) {

        // クーポン情報にクーポンSEQをセットする
        Integer couponSeq = couponDao.getCouponSeqNextVal();
        couponEntity.setCouponSeq(couponSeq);

        // クーポンテーブルに新規登録を行う
        registCouponEntity(couponEntity);

        // クーポンインデックスに新規登録を行う
        registCouponIndexEntity(couponEntity);
    }

    /**
     *
     * クーポンテーブルに新規登録を行う
     *
     * @param couponEntity 登録対象のクーポン
     */
    protected void registCouponEntity(CouponEntity couponEntity) {
        // クーポン情報にshopSEQをセットする
        Integer shopSeq = 1001;
        couponEntity.setShopSeq(shopSeq);
        // クーポン情報に管理者情報をセットする
        couponEntity.setAdministratorId(couponEntity.getAdministratorId());
        // 新規登録なのでクーポン枝番は0固定にする
        // 0固定にしているのはS2DaoのversionNo機能を利用する為
        // S2DaoのversionNoは0からの採番である
        couponEntity.setCouponVersionNo(0);
        // クーポン情報に登録日時・更新日時をセットする
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();
        couponEntity.setRegistTime(currentTime);
        couponEntity.setUpdateTime(currentTime);
        // クーポンテーブルにクーポン情報を登録する
        couponDao.insert(couponEntity);
    }

    /**
     *
     * クーポンインデックスに新規登録を行う
     *
     * @param couponEntity 登録対象のクーポン情報
     */
    protected void registCouponIndexEntity(CouponEntity couponEntity) {
        // クーポンインデックス情報を作成する
        CouponIndexEntity couponIndexEntity = ApplicationContextUtility.getBean(CouponIndexEntity.class);
        // クーポンSEQ
        couponIndexEntity.setCouponSeq(couponEntity.getCouponSeq());
        // クーポン枝番
        couponIndexEntity.setCouponVersionNo(couponEntity.getCouponVersionNo());
        // shopSEQ
        couponIndexEntity.setShopSeq(couponEntity.getShopSeq());
        // 登録時間
        couponIndexEntity.setRegistTime(couponEntity.getRegistTime());
        // 更新時間
        couponIndexEntity.setUpdateTime(couponEntity.getUpdateTime());
        // クーポンインデックステーブルにクーポンインデックス情報を登録する
        couponIndexDao.insert(couponIndexEntity);
    }
}