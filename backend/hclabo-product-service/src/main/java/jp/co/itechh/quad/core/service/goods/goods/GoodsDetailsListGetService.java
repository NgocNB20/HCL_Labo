/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;

import java.util.List;

/**
 * 商品詳細DTOリスト取得
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.2 $
 */
public interface GoodsDetailsListGetService {

    /**
     * 実行メソッド
     *
     * @param siteType     サイト種別
     * @param goodsSeqList 商品SEQリスト
     * @return 商品詳細DTOリスト list
     */
    List<GoodsDetailsDto> execute(HTypeSiteType siteType, List<Integer> goodsSeqList);
}
