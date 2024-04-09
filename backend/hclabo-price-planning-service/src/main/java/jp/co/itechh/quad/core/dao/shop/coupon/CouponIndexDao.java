/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.coupon;

import jp.co.itechh.quad.core.entity.shop.coupon.CouponIndexEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * クーポンインデックスDAOクラス。
 * <pre>
 * versionNoを利用した排他制御を行う為、CheckSingleRowUpdateを付与していない。
 * </pre>
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface CouponIndexDao {

    /**
     * クーポンインデックスを登録する。
     *
     * @param couponIndex クーポンインデックス
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(CouponIndexEntity couponIndex);

    /**
     * クーポンインデックスを更新する。
     *
     * @param couponIndex クーポンインデックス
     * @return 更新件数
     */
    @Update
    int update(CouponIndexEntity couponIndex);

    /**
     *
     * クーポンインデックスを削除する。
     *
     * @param couponSeq クーポンSEQ
     * @param shopSeq ショップSEQ
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteCouponIndex(Integer couponSeq, Integer shopSeq);
}