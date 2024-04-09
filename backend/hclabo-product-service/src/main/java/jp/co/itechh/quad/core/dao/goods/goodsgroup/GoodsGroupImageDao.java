/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupImageOpenstatusDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 商品グループ画像Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface GoodsGroupImageDao {

    /**
     * インサート
     *
     * @param goodsGroupImageEntity 商品グループ画像エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(GoodsGroupImageEntity goodsGroupImageEntity);

    /**
     * アップデート
     *
     * @param goodsGroupImageEntity 商品グループ画像エンティティ
     * @return 処理件数
     */
    @Update
    int update(GoodsGroupImageEntity goodsGroupImageEntity);

    /**
     * デリート
     *
     * @param goodsGroupImageEntity 商品グループ画像エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(GoodsGroupImageEntity goodsGroupImageEntity);

    /**
     * エンティティ取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 商品グループ画像エンティティ
     */
    @Select
    GoodsGroupImageEntity getEntity(Integer goodsGroupSeq);

    /**
     * 商品グループ画像情報リスト取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 商品グループ画像エンティティリスト
     */
    @Select
    List<GoodsGroupImageEntity> getGoodsGroupImageListByGoodsGroupSeq(Integer goodsGroupSeq);

    /**
     * 商品グループ画像情報取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループ画像エンティティリスト
     */
    @Select
    List<GoodsGroupImageEntity> getGoodsGroupImageList(List<Integer> goodsGroupSeqList);

    /**
     * 商品グループ画像テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * 全商品画像を取得します。
     *
     * @param offset 取得開始件数
     * @param limit  最大取得件数
     * @return GoodsGroupImageOpenstatusDto
     */
    @Select
    List<GoodsGroupImageOpenstatusDto> getGoodsGroupImageOpenstatusList(int offset, int limit);
}
