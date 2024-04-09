/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.product.adapter;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsGroupDisplayDto;
import jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse;

import java.util.List;

/**
 * 商品アダプター
 */
public interface IProductAdapter {

    /**
     * 商品詳細情報取得
     *
     * @param goodsSeq
     * @return GoodsDetailsDto
     */
    GoodsDetailsDto getGoodsDetailsDto(Integer goodsSeq);

    /**
     * 商品マイクロサービス<br/>
     * 商品詳細情報リスト取得
     *
     * @param productIdList 商品IDリスト
     * @return 商品詳細リスト
     */
    List<GoodsDetailsDto> getDetails(List<String> productIdList);

    /**
     * 商品グループコードで商品グループ表示DTOを取得する
     *
     * @param goodsGroupCode 商品グループコード
     * @return 商品グループ表示DTO
     */
    GoodsGroupDisplayDto getGoodsGroupDisplayByGoodsGroupCode(String goodsGroupCode);

    /**
     * 商品在庫表示テーブルアップサート<br/>
     * 在庫関連のテーブルが更新されコミットされた後に呼び出すこと
     *
     * @param goodsSeqList 商品SEQリスト
     * @return BatchExecuteResponse
     */
    BatchExecuteResponse syncUpsertGoodsStockDisplay(List<Integer> goodsSeqList);
}
