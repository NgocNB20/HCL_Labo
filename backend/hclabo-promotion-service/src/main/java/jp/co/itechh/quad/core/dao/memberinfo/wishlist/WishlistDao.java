/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.memberinfo.wishlist;

import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * お気に入りDAO
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface WishlistDao {

    /**
     * 追加
     * @param wishlistEntity お気に入り情報
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(WishlistEntity wishlistEntity);

    /**
     * 更新
     * @param wishlistEntity お気に入り情報
     * @return 更新件数
     */
    @Update
    int update(WishlistEntity wishlistEntity);

    /**
     * 削除
     * @param wishlistEntity お気に入り情報
     * @return 削除件数
     */
    @Delete
    int delete(WishlistEntity wishlistEntity);

    /**
     * エンティティ取得
     * @param memberInfoSeq 会員SEQ
     * @param goodsSeq 商品SEQ
     * @return お気に入りエンティティ
     */
    @Select
    WishlistEntity getEntity(Integer memberInfoSeq, Integer goodsSeq);

    /**
     * お気に入りエンティティリスト取得
     * @param conditionDto お気に入り検索条件DTO
     * @return お気に入りエンティティリスト
     */
    @Select
    List<WishlistEntity> getSearchWishlistList(WishlistSearchForDaoConditionDto conditionDto,
                                               SelectOptions selectOptions);

    /**
     * 会員のお気に入り登録商品SEQリストを取得
     *
     * @param memberInfoSeq 会員SEQ
     * @return お気に入り商品登録件数
     */
    @Select
    List<Integer> getGoodsSeqList(Integer memberInfoSeq);

    /**
     * 複数削除
     * @param memberInfoSeq 会員SEQ
     * @param goodsSeqs 商品SEQ配列
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteList(Integer memberInfoSeq, Integer[] goodsSeqs);

    /**
     * 会員に紐づくお気に入り商品を全て削除する
     *
     * @param memberInfoSeq 会員SEQ
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteListByMemberInfoSeq(Integer memberInfoSeq);

    /**
     * 会員に紐づくお気に入り商品を削除する
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsCodes 商品コード配列
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteListByGoodsCode(Integer memberInfoSeq, String[] goodsCodes);

}