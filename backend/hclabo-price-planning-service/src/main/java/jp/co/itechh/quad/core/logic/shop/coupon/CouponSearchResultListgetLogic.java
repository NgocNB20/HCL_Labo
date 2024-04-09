/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

import jp.co.itechh.quad.core.dto.shop.coupon.CouponSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;

import java.util.List;

/**
 * クーポン検索ロジックのインタフェースクラス。
 * <pre>
 * クーポン検索画面で利用する。
 * </pre>
 *
 * @author Kimura Kanae (itec)
 */
public interface CouponSearchResultListgetLogic {

    /**
     * クーポン検索結果を取得する。
     * <pre>
     * 検索条件を元に、クーポン情報を取得する。
     * </pre>
     *
     * @param condition 検索条件
     * @return クーポンリスト
     */
    List<CouponEntity> execute(CouponSearchForDaoConditionDto condition);

}