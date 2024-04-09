/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 商品グループ表示Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface GoodsGroupDisplayDao {

    /**
     * インサート
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(GoodsGroupDisplayEntity goodsGroupDisplayEntity);

    /**
     * アップデート
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @return 処理件数
     */
    @Update
    int update(GoodsGroupDisplayEntity goodsGroupDisplayEntity);

    /**
     * デリート
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(GoodsGroupDisplayEntity goodsGroupDisplayEntity);

    /**
     * エンティティ取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 商品グループ表示エンティティ
     */
    @Select
    GoodsGroupDisplayEntity getEntity(Integer goodsGroupSeq);

    /**
     * リスト取得
     * 品質安定化プロジェクトで以下のチケットに対応するために暫定的にフロント展示用商品情報Dtoを返却するように修正
     * 商品グループ表示マスタの情報と商品マスタの商品種別を保持する
     * #2311, #2312, #2313, #2314, #2385, #2386
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return フロント展示用商品情報エンティティのリスト
     */
    @Select
    List<GoodsGroupDisplayEntity> getGoodsGroupDisplayList(List<Integer> goodsGroupSeqList);

    /**
     * 商品グループ表示テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * 商品検索キーワードからエンティティのリストを取得
     *
     * @param shopSeq ショップSEQ
     * @param searchKeyword 商品検索キーワード
     * @return エンティティのリスト
     */
    @Select
    List<GoodsGroupDisplayEntity> getEntityListByKeyword(Integer shopSeq, String searchKeyword);
}
