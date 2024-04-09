/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.browsinghistory.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dao.goods.browsinghistory.BrowsinghistoryDao;
import jp.co.itechh.quad.core.dto.goods.browsinghistory.BrowsinghistorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.entity.goods.browsinghistory.BrowsinghistoryEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.browsinghistory.GoodsBrowsingHistoryListGetLogic;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * あしあと商品リスト取得<br/>
 * あしあと商品のリストを取得する。<br/>
 *
 * @author ozaki
 * @author matsumoto(itec) 2012/02/06 #2761 対応
 *
 */
@Component
public class GoodsBrowsingHistoryListGetLogicImpl extends AbstractShopLogic
                implements GoodsBrowsingHistoryListGetLogic {

    /** ログクラス */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsBrowsingHistoryListGetLogicImpl.class);

    /** あしあと情報DAO */
    private final BrowsinghistoryDao browsinghistoryDao;

    /**
     * 商品アダプター
     */
    private final IProductAdapter productAdapter;

    /** デフォルトページ番号 */
    private static final Integer DEFAULT_PNUM = 1;

    /** あしあと商品リスト取得 : ソート項目 */
    private static final String DEFAULT_GOODSBROWSINGHISTORYLISTSEARCH_ORDER_FIELD = "goodsGroupSeq";

    @Autowired
    public GoodsBrowsingHistoryListGetLogicImpl(BrowsinghistoryDao browsinghistoryDao, IProductAdapter productAdapter) {
        this.browsinghistoryDao = browsinghistoryDao;
        this.productAdapter = productAdapter;
    }

    /**
     * あしあと商品リスト取得<br/>
     * あしあと商品のリストを取得する。<br/>
     *
     * @param browsinghistorySearchForDaoConditionDto あしあと商品Dao用検索条件Dto
     * @return 商品グループ一覧DTO
     */
    @Override
    public List<GoodsGroupDto> execute(BrowsinghistorySearchForDaoConditionDto browsinghistorySearchForDaoConditionDto) {

        // (1) パラメータチェック
        // あしあと商品Dao用検索条件Dtoが null でないかをチェック
        ArgumentCheckUtil.assertNotNull(
                        "browsinghistorySearchForDaoConditionDto", browsinghistorySearchForDaoConditionDto);

        // (2) あしあと商品リスト取得
        // あしあと商品Daoのあしあと商品リスト検索処理を実行する。
        List<BrowsinghistoryEntity> browsinghistoryEntityList =
                        browsinghistoryDao.getSearchBrowsinghistoryList(browsinghistorySearchForDaoConditionDto,
                                                                        browsinghistorySearchForDaoConditionDto.getPageInfo()
                                                                                                               .getSelectOptions()
                                                                       );

        // (2) で取得したあしあと商品エンティティリストからあしあと商品エンティティ．商品SEQのリストを作成する。
        List<GoodsGroupDto> goodsGroupDtoList = new ArrayList<>();
        if (CollectionUtil.isEmpty(browsinghistoryEntityList)) {
            return goodsGroupDtoList;
        }
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        for (BrowsinghistoryEntity browsinghistoryEntity : browsinghistoryEntityList) {
            goodsGroupSeqList.add(browsinghistoryEntity.getGoodsGroupSeq());
        }

        // (3) 商品グループマップ取得
        // (2) で取得したあしあと商品エンティティリストからあしあと商品エンティティ．商品グループSEQのリストを作成する。
        // 商品グループマップ取得Logicを利用して、商品グループマップを取得する
        ProductListGetRequest productListGetRequest = new ProductListGetRequest();
        productListGetRequest.setGoodsGroupSeqList(goodsGroupSeqList);
        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット
        pageInfoModule.setupPageRequest(pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE,
                                        DEFAULT_GOODSBROWSINGHISTORYLISTSEARCH_ORDER_FIELD, true
                                       );

        List<GoodsGroupDto> goodsGroupDtos = productAdapter.getGoodsGroupDtos(productListGetRequest, pageInfoRequest);

        // (4) （戻り値用）商品グループ一覧DTOを作成・編集する。
        // （戻り値用）商品グループ一覧DTOを初期化する。
        // 商品グループDTOリストを初期化する。
        // tmp未取得商品グループ数 ＝ ０ をセットする。
        // ・(2)で取得したあしあと商品エンティティリストの件数分、以下の処理を行う
        // ①あしあと商品エンティティ．商品グループSEQをキー項目として、(3)で取得した商品グループマップから商品グループDTOを取得する。
        // ・マップから商品グループDTOが取得できた場合、商品グループDTOリストに追加する
        // ・マップから商品グループDTOを取得できなかった場合、ログ出力して、tmp未取得商品グループ数をカウントアップする。
        // 商品グループDTOリストを（戻り値用）商品グループ一覧DTOにセットする。
        // （（パラメータ）あしあと商品Dao用検索条件DTO．COUNT － tmp未取得商品グループ数）
        // を（戻り値用）商品グループ一覧DTO．全件数にセットする。

        for (GoodsGroupDto goodsGroupDto : goodsGroupDtos) {
            if (ObjectUtils.isEmpty(goodsGroupDto)
                || goodsGroupDto.getGoodsGroupEntity().getGoodsOpenStatusPC() != HTypeOpenDeleteStatus.OPEN) {
                if (!ObjectUtils.isEmpty(goodsGroupDto) && !ObjectUtils.isEmpty(goodsGroupDto.getGoodsGroupEntity())) {
                    LOGGER.warn("商品グループSEQが『" + goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq()
                                + "』の商品グループが取得できませんでした。");
                } else {
                    LOGGER.warn("商品グループが取得できませんでした。");
                }
            } else {
                goodsGroupDtoList.add(goodsGroupDto);
            }
        }

        // (5) 戻り値
        // 戻り値用商品一覧Dtoを返す。
        return goodsGroupDtoList;
    }

}