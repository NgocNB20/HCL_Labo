/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.coupon;

import jp.co.itechh.quad.core.dto.shop.coupon.CouponSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.sql.Timestamp;
import java.util.List;

/**
 * クーポンDAOクラス。
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface CouponDao {

    /**
     * クーポンを登録する。
     *
     * @param coupon クーポン
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(CouponEntity coupon);

    /**
     * クーポンを更新する。
     *
     * @param coupon クーポン
     * @return 更新件数
     */
    @Update
    int update(CouponEntity coupon);

    /**
     * クーポンを削除する。
     *
     * @param coupon クーポン
     * @return 削除件数
     */
    @Delete
    int delete(CouponEntity coupon);

    /**
     * クーポンを取得する。
     *
     * @param couponSeq クーポンSEQ
     * @return クーポンエンティティ
     */
    @Select
    CouponEntity getEntity(Integer couponSeq);

    /**
     * クーポンエンティティList取得。<br />
     * <pre>
     * 検索条件を元に対象のクーポン情報リストを取得する。
     * クーポン検索で利用する。
     * </pre>
     *
     * @param conditionDto 検索条件
     * @param selectOptions 検索オプション
     * @return クーポンエンティティリスト
     */
    @Select
    List<CouponEntity> getCouponSearchResultList(CouponSearchForDaoConditionDto conditionDto,
                                                 SelectOptions selectOptions);

    /**
     * クーポンSEQからクーポン情報を取得する。<br />
     * <pre>
     * 取得できない場合はNULL。
     * 更新・削除画面表示で利用する。
     * </pre>
     *
     * @param couponSeq クーポンSEQ
     * @param shopSeq ショップSEQ
     * @return クーポン
     */
    @Select
    CouponEntity getCouponByCouponSeq(Integer couponSeq, Integer shopSeq);

    /**
     * クーポンコードと現在の日付から利用可能なクーポン情報を取得する。<br />
     * <pre>
     * 複数取得できた場合は最新の１件の情報を取得する。
     * 取得できない場合はエラー。
     * バックからの新規登録時のクーポン適用チェック時に利用する。
     * </pre>
     *
     * @param couponCode クーポンコード
     * @return クーポン
     */
    @Select
    CouponEntity getCouponByCouponCode(String couponCode);

    /**
     * クーポンSEQとクーポン枝番を指定してクーポンを取得する。<br />
     * <pre>
     * 受注履歴の取得時に利用する。
     * </pre>
     *
     * @param couponSeq クーポンSEQ
     * @param couponVersionNo クーポン枝番
     * @return クーポンエンティティ
     */
    @Select
    CouponEntity getCouponByCouponVersionNo(Integer couponSeq, Integer couponVersionNo);

    /**
     * 一つ先のクーポンSEQを取得する。<br />
     * <pre>
     * クーポン新規登録時に利用する。
     * </pre>
     *
     * @return クーポンSEQ
     */
    @Select
    Integer getCouponSeqNextVal();

    /**
     * クーポン情報を削除する。<br />
     * <pre>
     * 同じクーポンSEQの変更前の情報を含むクーポン情報を削除する。
     * クーポン削除時に利用する。
     * </pre>
     *
     * @param couponSeq クーポンSEQ
     * @param shopSeq ショップSEQ
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteCoupon(Integer couponSeq, Integer shopSeq);

    /**
     * 引数と同じクーポンIDの利用件数を返す。<br />
     * <pre>
     * 新規登録時にクーポンID重複チェックで利用する。
     * </pre>
     *
     * @param couponId クーポンID
     * @param shopSeq ショップSEQ
     * @return 利用件数
     */
    @Select
    int checkCouponId(String couponId, Integer shopSeq);

    /**
     * 引数と同じクーポンコードの利用件数を返す。<br />
     * <pre>
     * 再利用不可期間のクーポンの中で同じクーポンコードを利用している件数を返す。
     * クーポン登録更新時のクーポンコードチェックで利用する。
     * </pre>
     *
     * @param shopSeq ショップSEQ
     * @param couponCode クーポンコード
     * @param reUseEndTime 再利用不可能期間
     * @return 利用件数
     */
    @Select
    int checkCouponCode(Integer shopSeq, String couponCode, Timestamp reUseEndTime);

    /**
     * 引数と同じクーポンコードの利用件数を返す。<br />
     * <pre>
     * 再利用不可期間のクーポンの中で同じクーポンコードを利用している件数を返す。
     * クーポン登録更新時のクーポンコードチェックで利用する。
     * </pre>
     *
     * @param shopSeq ショップSEQ
     * @param couponCode クーポンコード
     * @param reUsePastTime 再利用不可能期間(過去)
     * @param reUseFutureTime 再利用不可能期間(未来)
     * @param couponSeq クーポンSEQ
     * @return 利用件数
     */
    @Select
    int checkCouponCodeByCouponTime(Integer shopSeq,
                                    String couponCode,
                                    Timestamp reUsePastTime,
                                    Timestamp reUseFutureTime,
                                    Integer couponSeq);

}