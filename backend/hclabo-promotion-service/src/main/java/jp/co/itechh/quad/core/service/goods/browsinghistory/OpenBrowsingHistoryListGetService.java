/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.browsinghistory;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.browsinghistory.BrowsinghistorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;

import java.util.List;

/**
 * 公開あしあと商品情報取得Service
 *
 * @author ozaki
 *
 */
public interface OpenBrowsingHistoryListGetService {

    /**
     * 公開あしあと商品リスト取得<br/>
     * 端末識別番号を元にあしあと商品情報のリストを取得する<br/>
     * @param accessUid 端末識別情報
     * @param siteType サイト種別
     * @param conditionDto あしあと商品検索条件DTO
     * @return 商品グループDTOリスト
     */
    List<GoodsGroupDto> execute(String accessUid,
                                HTypeSiteType siteType,
                                BrowsinghistorySearchForDaoConditionDto conditionDto);

    /**
     * 公開あしあと商品情報取得
     * 端末識別番号を元にあしあと商品情報のリストを取得する<br/>
     *
     * @param accessUid 端末識別情報
     * @param siteType サイト種別
     * @param limit 取得件数
     * @param exceptGoodsGroupSeq 取得対象外の商品
     * @return 商品グループDTO一覧
     */
    List<GoodsGroupDto> execute(String accessUid, HTypeSiteType siteType, int limit, Integer exceptGoodsGroupSeq);

}