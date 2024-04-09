/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.browsinghistory.presentation.api;

import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsGroupResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsInformationIconDetailResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.StockStatusDisplayResponse;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * あしあとスエンドポイント Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class BrowsingHistoryHelper {

    /**
     * あしあと商品情報一覧レスポンスに変換
     *
     * @param goodsGroupDtoList 商品グループDTO一覧
     * @return あしあと商品情報一覧レスポンス browsinghistory list response
     */
    public BrowsingHistoryListResponse toBrowsingHistoryListResponse(List<GoodsGroupDto> goodsGroupDtoList) {

        BrowsingHistoryListResponse browsingHistoryListResponse = new BrowsingHistoryListResponse();
        List<BrowsingHistoryResponse> historyResponseList = new ArrayList();
        if (CollectionUtil.isNotEmpty(goodsGroupDtoList)) {
            for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
                BrowsingHistoryResponse browsingHistoryResponse = new BrowsingHistoryResponse();

                GoodsGroupResponse goodsGroupResponse = toGoodsGroupResponse(goodsGroupDto.getGoodsGroupEntity());
                browsingHistoryResponse.setGoodsGroupResponse(goodsGroupResponse);

                List<GoodsGroupImageResponse> goodsGroupImageResponses =
                                toGoodsGroupImageResponseList(goodsGroupDto.getGoodsGroupImageEntityList());
                browsingHistoryResponse.setGoodsGroupImageResponseList(goodsGroupImageResponses);

                StockStatusDisplayResponse stockStatusDisplayResponse =
                                toStockStatusDisplayResponse(goodsGroupDto.getBatchUpdateStockStatus());
                browsingHistoryResponse.setStockStatusDisplayResponse(stockStatusDisplayResponse);

                List<GoodsInformationIconDetailResponse> goodsInformationIconDetailResponseList =
                                toGoodsInformationIconDetailResponseList(
                                                goodsGroupDto.getGoodsInformationIconDetailsDtoList());
                browsingHistoryResponse.setGoodsInformationIconDetailResponse(goodsInformationIconDetailResponseList);

                historyResponseList.add(browsingHistoryResponse);
            }
        }

        browsingHistoryListResponse.setBrowsingHistoryResponse(historyResponseList);

        return browsingHistoryListResponse;
    }

    /**
     * 商品グループレスポンスに変換
     *
     * @param entity 商品グループクラス
     * @return 商品グループレスポンス
     */
    public GoodsGroupResponse toGoodsGroupResponse(GoodsGroupEntity entity) {
        GoodsGroupResponse goodsGroupResponse = new GoodsGroupResponse();

        goodsGroupResponse.setGoodsGroupSeq(entity.getGoodsGroupSeq());
        goodsGroupResponse.setGoodsGroupCode(entity.getGoodsGroupCode());
        goodsGroupResponse.setGoodsGroupName(entity.getGoodsGroupName());
        goodsGroupResponse.setGoodsPrice(entity.getGoodsPrice());
        goodsGroupResponse.setGoodsPriceInTax(entity.getGoodsPriceInTax());
        goodsGroupResponse.setWhatsnewDate(entity.getWhatsnewDate());
        goodsGroupResponse.setGoodsOpenStatusPC(EnumTypeUtil.getValue(entity.getGoodsOpenStatusPC()));
        goodsGroupResponse.setOpenStartTimePC(entity.getOpenStartTimePC());
        goodsGroupResponse.setOpenEndTimePC(entity.getOpenEndTimePC());
        goodsGroupResponse.setGoodsTaxType(EnumTypeUtil.getValue(entity.getGoodsTaxType()));
        goodsGroupResponse.setTaxRate(entity.getTaxRate());
        goodsGroupResponse.setAlcoholFlag(EnumTypeUtil.getValue(entity.getAlcoholFlag()));
        goodsGroupResponse.setSnsLinkFlag(EnumTypeUtil.getValue(entity.getSnsLinkFlag()));
        goodsGroupResponse.setShopSeq(entity.getShopSeq());
        goodsGroupResponse.setVersionNo(entity.getVersionNo());
        goodsGroupResponse.setRegistTime(entity.getRegistTime());
        goodsGroupResponse.setUpdateTime(entity.getUpdateTime());

        return goodsGroupResponse;
    }

    /**
     * 商品グループ画像レスポンスリストに変換
     *
     * @param goodsGroupImageEntityList　商品グループ画像リスト
     * @return 商品グループ画像レスポンスリスト
     */
    public List<GoodsGroupImageResponse> toGoodsGroupImageResponseList(List<GoodsGroupImageEntity> goodsGroupImageEntityList) {
        List<GoodsGroupImageResponse> goodsGroupImageResponseList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(goodsGroupImageEntityList)) {
            goodsGroupImageEntityList.forEach(item -> {
                GoodsGroupImageResponse goodsGroupImageResponse = new GoodsGroupImageResponse();

                goodsGroupImageResponse.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsGroupImageResponse.setImageTypeVersionNo(item.getImageTypeVersionNo());
                goodsGroupImageResponse.setImageFileName(item.getImageFileName());
                goodsGroupImageResponse.setRegistTime(item.getRegistTime());
                goodsGroupImageResponse.setUpdateTime(item.getUpdateTime());

                goodsGroupImageResponseList.add(goodsGroupImageResponse);
            });
        }

        return goodsGroupImageResponseList;
    }

    /**
     * 在庫状態表示用レスポンスに変換
     *
     * @param entity 商品グループ在庫表示クラス
     * @return 在庫状態表示用レスポンス
     */
    public StockStatusDisplayResponse toStockStatusDisplayResponse(StockStatusDisplayEntity entity) {

        // 商品グループ在庫表示更新バッチの処理前は存在しないためnullを返す
        if (entity == null) {
            return null;
        }

        StockStatusDisplayResponse stockStatusDisplayResponse = new StockStatusDisplayResponse();

        stockStatusDisplayResponse.setGoodsGroupSeq(entity.getGoodsGroupSeq());
        stockStatusDisplayResponse.setStockStatusPc(EnumTypeUtil.getValue(entity.getStockStatusPc()));
        stockStatusDisplayResponse.setRegistTime(entity.getRegistTime());
        stockStatusDisplayResponse.setUpdateTime(entity.getUpdateTime());

        return stockStatusDisplayResponse;
    }

    /**
     * アイコン詳細レスポンスのリストに変換
     *
     * @param goodsInformationIconDetailsDtoList アイコン詳細リスト
     * @return アイコン詳細レスポンスのリスト
     */
    public List<GoodsInformationIconDetailResponse> toGoodsInformationIconDetailResponseList(List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList) {
        List<GoodsInformationIconDetailResponse> goodsInformationIconDetailResponseList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(goodsInformationIconDetailsDtoList)) {
            goodsInformationIconDetailsDtoList.forEach(item -> {
                GoodsInformationIconDetailResponse goodsInformationIconDetailResponse =
                                new GoodsInformationIconDetailResponse();

                goodsInformationIconDetailResponse.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsInformationIconDetailResponse.setIconSeq(item.getIconSeq());
                goodsInformationIconDetailResponse.setIconName(item.getIconName());
                goodsInformationIconDetailResponse.setColorCode(item.getColorCode());
                goodsInformationIconDetailResponse.setOrderDisplay(item.getOrderDisplay());

                goodsInformationIconDetailResponseList.add(goodsInformationIconDetailResponse);
            });
        }

        return goodsInformationIconDetailResponseList;
    }
}