/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.browsinghistory.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.browsinghistory.BrowsinghistorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.logic.goods.browsinghistory.GoodsBrowsingHistoryListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.browsinghistory.OpenBrowsingHistoryListGetService;
import jp.co.itechh.quad.core.web.PageInfoModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 公開あしあと商品情報取得<br/>
 * 公開されているあしあと商品情報を取得します。<br/>
 *
 * @author ozaki
 * @author kaneko　(itec) チケット対応　#2644　訪問者数にクローラが含まれている
 */
@Service
public class OpenBrowsingHistoryListGetServiceImpl extends AbstractShopService
                implements OpenBrowsingHistoryListGetService {

    /**
     * あしあと商品詳細リスト取得ロジック
     */
    private final GoodsBrowsingHistoryListGetLogic goodsBrowsingHistoryListGetLogic;

    @Autowired
    public OpenBrowsingHistoryListGetServiceImpl(GoodsBrowsingHistoryListGetLogic goodsBrowsingHistoryListGetLogic) {
        this.goodsBrowsingHistoryListGetLogic = goodsBrowsingHistoryListGetLogic;
    }

    /**
     * 公開あしあと商品リスト取得<br/>
     * 端末識別番号を元にあしあと商品情報のリストを取得する<br/>
     *
     * @param accessUid 端末識別情報
     * @param siteType サイト種別
     * @param conditionDto あしあと商品検索条件DTO
     * @return 商品グループDTOリスト
     */
    @Override
    public List<GoodsGroupDto> execute(String accessUid,
                                       HTypeSiteType siteType,
                                       BrowsinghistorySearchForDaoConditionDto conditionDto) {
        // (1) 共通情報チェック
        // ・ショップSEQ ： null（or 0）の場合 エラーとして処理を終了する
        // ・端末識別情報 ： null（or空文字）の場合 エラーとして処理を終了する
        // ・サイト区分 ： null(or空文字) の場合 エラーとして処理を終了する
        Integer shopSeq = 1001;
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotEmpty("accessUid", accessUid);
        ArgumentCheckUtil.assertNotNull("siteType", siteType);

        // (2) 検索条件作成
        // 共通情報
        conditionDto.setShopSeq(shopSeq);
        conditionDto.setAccessUid(accessUid);

        // ・あしあと商品リスト取得処理実行
        // ※『logic基本設計（あしあと商品リスト取得）.xls』参照
        // Logic GoodsBrowsingHistoryListGetLogic
        // パラメータ あしあと商品Dao用検索条件Dto
        // 戻り値 商品グループ一覧DTODTO
        List<GoodsGroupDto> goodsGroupDtoList = goodsBrowsingHistoryListGetLogic.execute(conditionDto);

        return goodsGroupDtoList;
    }

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
    @Override
    public List<GoodsGroupDto> execute(String accessUid,
                                       HTypeSiteType siteType,
                                       int limit,
                                       Integer exceptGoodsGroupSeq) {
        BrowsinghistorySearchForDaoConditionDto conditionDto =
                        getComponent(BrowsinghistorySearchForDaoConditionDto.class);
        // PageInfoモジュール取得
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        // ページングセットアップ
        conditionDto = pageInfoModule.setupPageInfoForSkipCount(conditionDto, limit, "updateTime", false);
        if (exceptGoodsGroupSeq != null) {
            conditionDto.setGoodsGroupSeqList(new ArrayList<>());
            conditionDto.getGoodsGroupSeqList().add(exceptGoodsGroupSeq);
        }
        return execute(accessUid, siteType, conditionDto);
    }

}