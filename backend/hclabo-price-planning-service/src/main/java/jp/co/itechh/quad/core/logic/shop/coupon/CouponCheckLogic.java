/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;

/**
 * クーポン利用制限数チェックLogic
 *
 * @author s_tsuru
 *
 */
public interface CouponCheckLogic {

    /** 新規登録クーポンのIDが既存クーポンと重複した場合エラー */
    public static final String MSGCD_REPETITION_COUPONID = "LCP000101";

    /** 登録・更新時に再利用不可期間のクーポンコードと重複した場合エラー */
    public static final String MSGCD_REPETITION_COUPONCODE = "LCP000102";

    /** 登録・更新時に現在日時よりも過去に利用開始日時を指定した場合エラー */
    public static final String MSGCD_CANNOT_SET_STRATTIME = "LCP000103";

    /** 利用期間中に利用開始日を変更した場合エラー */
    public static final String MSGCD_CANNOT_CHANGE_STARTTIME = "LCP000104";

    /** 利用期間中にクーポンコードを変更した場合エラー */
    public static final String MSGCD_CANNOT_CHANGE_COUPONCODE = "LCP000105";

    /** 利用期間終了後にクーポンを変更した場合エラー */
    public static final String MSGCD_CANNOT_CAHNGE_COUPONDATA = "LCP000106";

    /** 利用期間中に利用割引種別を変更した場合エラー */
    public static final String MSGCD_CANNOT_CHANGE_DISCOUNTTYPE = "LCP000107";

    /** 対象商品が重複した場合エラー：PKG-3555-001-L- */
    public static final String MSGCD_DUPLICATION_TARGET_GOODS = "PKG-3555-001-L-";

    /** 対象商品が存在しない場合エラー：PKG-3555-002-L- */
    public static final String MSGCD_NOT_EXIST_TARGET_GOODS = "PKG-3555-002-L-";

    /** 対象会員が重複した場合エラー：PKG-3555-003-L- */
    public static final String MSGCD_DUPLICATION_TARGET_MEMBERS = "PKG-3555-003-L-";

    /** 対象会員が存在しない場合エラー：PKG-3555-004-L- */
    public static final String MSGCD_NOT_EXIST_TARGET_MEMBERS = "PKG-3555-004-L-";

    /**
     * 新規登録のクーポンチェックを行う。
     * <pre>
     * クーポンID・開始日時・クーポンコードに対しチェックを行う。
     * </pre>
     *
     * @param couponEntity チェック対象のクーポン
     */
    void checkForRegist(CouponEntity couponEntity);

    /**
     * 更新のクーポンチェックを行う。
     * <pre>
     * 開催前：開始日時・クーポンコードに対しチェックを行う。
     * 開催中：開始日時・クーポンコードに対しチェックを行う。
     * クーポンが終了していた場合、エラーメッセージを返す。
     * </pre>
     *
     * @param preUpdateCoupon 更新前のクーポン
     * @param postUpdateCoupon 更新後のクーポン
     */
    void checkForUpdate(CouponEntity preUpdateCoupon, CouponEntity postUpdateCoupon);

}
