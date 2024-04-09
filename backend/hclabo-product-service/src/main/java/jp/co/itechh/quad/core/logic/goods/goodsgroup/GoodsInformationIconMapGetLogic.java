/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;

import java.util.List;
import java.util.Map;

/**
 *
 * 商品アイコン情報Mapを取得する。
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface GoodsInformationIconMapGetLogic {

    /**
     * 商品インフォメーションアイコンMap取得
     * 商品インフォメーションアイコン情報DTOのMapを取得する
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param siteType サイト区分
     * @return 商品インフォメーションアイコンDTOMap
     */
    Map<Integer, List<GoodsInformationIconDetailsDto>> execute(List<Integer> goodsGroupSeqList, HTypeSiteType siteType);

}