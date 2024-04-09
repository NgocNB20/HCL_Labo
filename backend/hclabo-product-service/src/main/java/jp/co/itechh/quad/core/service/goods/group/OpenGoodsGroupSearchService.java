/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;

import java.util.List;

/**
 * 公開商品グループ情報検索
 * 検索条件に該当する公開中の商品情報の商品グループのリストを取得する
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 */
public interface OpenGoodsGroupSearchService {

    // SGP0003

    /**
     * 公開関商品グループ情報検索
     * 検索条件に該当する公開中の商品情報の商品グループのリストを取得する
     *
     * @param siteType     サイト種別
     * @param conditionDto 商品グループ検索条件DTO
     * @return 商品情報DTO
     */
    List<GoodsGroupDto> execute(HTypeSiteType siteType, GoodsGroupSearchForDaoConditionDto conditionDto);
}
