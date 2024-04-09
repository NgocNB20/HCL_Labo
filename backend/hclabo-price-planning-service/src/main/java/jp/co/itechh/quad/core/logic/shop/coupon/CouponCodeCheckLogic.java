/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.coupon;

import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;

/**
 * クーポンコード重複チェックロジックのインタフェースクラス。
 *
 * @author k.harada (itec)
 */
public interface CouponCodeCheckLogic {

    /**
     * 既存のクーポンとクーポンコードが重複していないかをチェックする。
     * <pre>
     * 開催終了日が一定期間過ぎたクーポンコードの再利用は可能とする。
     * </pre>
     * ※ バックでクーポンコードの自動生成時に使用。
     *
     * @param couponCode クーポンコード
     * @return 登録可能なクーポンコードのときtrueを返す
     */
    boolean execute(String couponCode);

    /**
     * 登録済のクーポンとクーポンコードが重複していないかをチェックする。
     * <dl>
     * <dt>利用可能
     * <dd>終了したクーポンより利用開始日が未来、かつ、再利用不可期間以上<dd>
     * <dd>利用開始前のクーポンより利用終了日が過去、かつ、再利用不可期間以下<dd>
     * </dl>
     * @param couponEntity 登録対象クーポン
     * @return 登録可能なクーポンコードのときtrueを返す
     */
    boolean execute(CouponEntity couponEntity);
}