/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.noveltypresent;

import jp.co.itechh.quad.core.dto.shop.noveltypresent.NoveltyGoodsCountConditionDto;

/**
 * ノベルティプレゼント条件検索サービス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
public interface NoveltyPresentSearchService {

    /**
     * 商品条件に一致する商品数を取得
     *
     * @param conditionDto ノベルティプレゼント商品数確認用検索条件
     * @return 商品数
     */
    int getTargetGoodsCount(NoveltyGoodsCountConditionDto conditionDto);

}