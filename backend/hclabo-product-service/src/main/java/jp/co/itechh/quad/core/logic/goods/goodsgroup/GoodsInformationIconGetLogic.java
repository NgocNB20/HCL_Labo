/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsInformationIconEntity;

import java.util.List;

/**
 *
 * 商品アイコン表示情報リストを取得する。
 *
 * @author ozaki
 * @version $Revision: 1.4 $
 *
 */
public interface GoodsInformationIconGetLogic {

    /**
     * 商品インフォメーションアイコン表示情報リスト取得
     * 商品インフォメーションアイコン表示情報DTOのリストを取得する
     *
     * @param informationIconSeqList 対象商品インフォメーションアイコンリスト
     * @return 商品インフォメーションアイコン表示情報DTOリスト
     */
    List<GoodsInformationIconDetailsDto> execute(List<Integer> informationIconSeqList);

    /**
     * アイコンSEQのリストからエンティティのリストを取得
     *
     * @param iconSeqList アイコンSEQのリスト
     * @return エンティティのリスト
     */
    List<GoodsInformationIconEntity> getEntityListBySeqList(List<Integer> iconSeqList);

    /**
     * アイコン名のリストからエンティティのリストを取得
     *
     * @param iconNameList アイコン名のリスト
     * @return エンティティのリスト
     */
    List<GoodsInformationIconEntity> getEntityListByNameList(List<String> iconNameList);
}