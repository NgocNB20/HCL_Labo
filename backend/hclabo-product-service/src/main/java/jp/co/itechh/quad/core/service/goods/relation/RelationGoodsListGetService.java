/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.relation;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;

import java.util.List;

/**
 関連商品情報取得ロジック
 *
 * @author ozaki
 * @version $Revision: 1.3 $
 */
public interface RelationGoodsListGetService {

    /**
     * 関連商品情報取得（公開状態のみ取得の場合はコンディションに設定すること）
     *
     * @param conditionDto 関連商品検索条件DTO
     * @return 関連商品リスト情報DTO
     */
    List<GoodsRelationDto> execute(GoodsRelationSearchForDaoConditionDto conditionDto);
}
