/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.core.dto.shop.noveltypresent.NoveltyGoodsCountConditionDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;

import java.util.List;

/**
 * 商品取得Logic
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public interface GoodsGetLogic {

    /**
     * ノベルティプレゼント商品の対象件数を取得する
     *
     * @param conditionDto ノベルティプレゼント商品数確認用検索条件
     * @return 商品件数
     */
    int getNoveltyGoodsCount(NoveltyGoodsCountConditionDto conditionDto);

    /**
     * 商品コードの一覧からエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品コードのリスト
     * @return エンティティのリスト
     */
    List<GoodsEntity> getEntityByGoodsCodeList(Integer shopSeq, List<String> goodsCodeList);

    /**
     * 商品名（部分一致）からエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsName 商品名
     * @return エンティティのリスト
     */
    List<GoodsEntity> getEntityByGoodsName(Integer shopSeq, String goodsName);

    /**
     * 商品コードのリストから商品グループを結合して公開状態、販売状態を取得する
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品コードのリスト
     * @return 検索結果
     */
    List<GoodsSearchResultDto> getStatusByGoodsCodeList(Integer shopSeq, List<String> goodsCodeList);

}
