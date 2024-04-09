/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupPopularityDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupPopularityEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.stream.Stream;

/**
 * 商品グループ人気Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface GoodsGroupPopularityDao {

    /**
     * インサート
     *
     * @param goodsGroupPopularityEntity 商品グループ人気エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(GoodsGroupPopularityEntity goodsGroupPopularityEntity);

    /**
     * アップデート
     *
     * @param goodsGroupPopularityEntity 商品グループ人気エンティティ
     * @return 処理件数
     */
    @Update
    int update(GoodsGroupPopularityEntity goodsGroupPopularityEntity);

    /**
     * デリート
     *
     * @param goodsGroupPopularityEntity 商品グループ人気エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(GoodsGroupPopularityEntity goodsGroupPopularityEntity);

    /**
     * エンティティ取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 商品グループ人気エンティティ
     */
    @Select
    GoodsGroupPopularityEntity getEntity(Integer goodsGroupSeq);

    /**
     * 公開状態でない商品の人気カウントを0へ更新
     *
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updatePopularityOfNonOpenToZero();

    /**
     * 人気カウント集計用に公開中商品一覧取得
     *
     * @return 更新件数
     */
    @Select
    Stream<GoodsGroupPopularityDetailsDto> getOpenGoodsGroupPopularityList();
}
