/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * 関連商品Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface GoodsRelationDao {

    /**
     * インサート
     *
     * @param goodsRelationEntity 関連商品エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(GoodsRelationEntity goodsRelationEntity);

    /**
     * アップデート
     *
     * @param goodsRelationEntity 関連商品エンティティ
     * @return 処理件数
     */
    @Update
    int update(GoodsRelationEntity goodsRelationEntity);

    /**
     * デリート
     *
     * @param goodsRelationEntity 関連商品エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(GoodsRelationEntity goodsRelationEntity);

    /**
     * エンティティ取得
     *
     * @param goodsGroupSeq         商品グループSEQ
     * @param goodsRelationGroupSeq 関連商品グループSEQ
     * @return 関連商品エンティティ
     */
    @Select
    GoodsRelationEntity getEntity(Integer goodsGroupSeq, Integer goodsRelationGroupSeq);

    /**
     * 関連商品エンティティリスト取得
     *
     * @param conditionDto 関連商品検索条件DTO
     * @return 関連商品エンティティリスト
     */
    @Select
    List<GoodsRelationEntity> getSearchGoodsRelation(GoodsRelationSearchForDaoConditionDto conditionDto,
                                                     SelectOptions selectOptions);

    /**
     * 関連商品DTOリスト取得<br />
     * 指定した商品グループの関連商品エンティティ
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 関連商品エンティティリスト
     */
    @Select
    List<GoodsRelationEntity> getGoodsRelationEntityListByGoodsGroupSeq(Integer goodsGroupSeq);

    /**
     * 関連商品DTOリスト取得<br />
     * 指定した商品グループの関連商品DTO
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 関連商品エンティティリスト
     */
    @Select
    List<GoodsRelationEntity> getGoodsRelationListByGoodsGroupSeq(Integer goodsGroupSeq);

    /**
     * 関連商品テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();
}