/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.relation.presentation.api;

import jp.co.itechh.quad.core.base.utility.ArrayFactoryUtility;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.CalculatePriceUtility;
import jp.co.itechh.quad.core.utility.GoodsUtility;
import jp.co.itechh.quad.relation.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.relation.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsResponse;
import jp.co.itechh.quad.relation.presentation.api.param.StockStatusDisplayResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 関連商品 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class RelationHelper {

    /** 商品系ヘルパークラス */
    private final GoodsUtility goodsUtility;

    /** 金額計算Utilityクラス　 */
    private final CalculatePriceUtility calculatePriceUtility;

    /** 配列ファクトリユーティリティクラス */
    private final ArrayFactoryUtility arrayFactoryUtility;

    /**
     * コンストラクタ
     *
     * @param goodsUtility
     * @param calculatePriceUtility
     * @param arrayFactoryUtility
     */
    @Autowired
    public RelationHelper(GoodsUtility goodsUtility,
                          CalculatePriceUtility calculatePriceUtility,
                          ArrayFactoryUtility arrayFactoryUtility) {
        this.goodsUtility = goodsUtility;
        this.calculatePriceUtility = calculatePriceUtility;
        this.arrayFactoryUtility = arrayFactoryUtility;
    }

    /**
     * 商品情報一覧ページアイテムリストの作成
     *
     * @param goodsRelationDtoList 関連商品Dtoリスト
     * @return 関連商品一覧レスポンス
     */
    public RelationGoodsListResponse toRelationGoodsListResponse(List<GoodsRelationDto> goodsRelationDtoList)
                    throws Exception {

        if (CollectionUtils.isEmpty(goodsRelationDtoList)) {
            return null;
        }

        RelationGoodsListResponse relationGoodsListResponse = new RelationGoodsListResponse();
        List<RelationGoodsResponse> relationGoodsList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();

        goodsRelationDtoList.forEach(item -> {
            if (ObjectUtils.isNotEmpty(item)) {
                String keyMap = item.getGoodsRelationEntity().getGoodsRelationGroupSeq() + "_"
                                + item.getGoodsRelationEntity().getGoodsGroupCode();
                map.put(keyMap.toUpperCase(), item.getGoodsRelationEntity());
            }
        });

        if (ObjectUtils.isEmpty(map)) {
            return null;
        }

        for (GoodsRelationDto dto : goodsRelationDtoList) {
            GoodsGroupEntity goodsGroupEntity = dto.getGoodsGroupDto().getGoodsGroupEntity();
            RelationGoodsResponse relationGoodsResponse = new RelationGoodsResponse();

            String keyMap = goodsGroupEntity.getGoodsGroupSeq() + "_" + goodsGroupEntity.getGoodsGroupCode();
            GoodsRelationEntity relationEntity = (GoodsRelationEntity) map.get(keyMap.toUpperCase());

            if (dto.getGoodsGroupDto().getGoodsGroupImageEntityList() != null) {
                // 商品画像リストを取り出す。
                List<GoodsGroupImageResponse> goodsGroupImageResponseList = new ArrayList<>();
                for (GoodsGroupImageEntity goodsGroupImageEntity : dto.getGoodsGroupDto()
                                                                      .getGoodsGroupImageEntityList()) {
                    GoodsGroupImageResponse goodsGroupImageResponse = new GoodsGroupImageResponse();
                    goodsGroupImageResponse.setImageFileName(
                                    goodsUtility.getGoodsImagePath(goodsGroupImageEntity.getImageFileName()));
                    goodsGroupImageResponseList.add(goodsGroupImageResponse);
                }
                relationGoodsResponse.setGoodsGroupImageList(goodsGroupImageResponseList);
            }

            if (dto.getGoodsGroupDto().getGoodsGroupDisplayEntity() != null
                && dto.getGoodsGroupDto().getGoodsGroupDisplayEntity().getGoodsTag() != null) {
                relationGoodsResponse.setGoodsTag(arrayFactoryUtility.arrayToList(
                                dto.getGoodsGroupDto().getGoodsGroupDisplayEntity().getGoodsTag()));
            }

            if (ObjectUtils.isNotEmpty(goodsGroupEntity) && ObjectUtils.isNotEmpty(relationEntity)) {

                relationGoodsResponse.setGoodsGroupSeq(relationEntity.getGoodsGroupSeq());
                relationGoodsResponse.setGoodsGroupCode(relationEntity.getGoodsGroupCode());
                relationGoodsResponse.setGoodsGroupName(relationEntity.getGoodsGroupName());
                relationGoodsResponse.setGoodsOpenStatus(EnumTypeUtil.getValue(relationEntity.getGoodsOpenStatusPC()));
                relationGoodsResponse.setOrderDisplay(relationEntity.getOrderDisplay());
                relationGoodsResponse.setRegistTime(relationEntity.getRegistTime());
                relationGoodsResponse.setUpdateTime(relationEntity.getUpdateTime());
                relationGoodsResponse.setGoodsRelationGroupSeq(relationEntity.getGoodsRelationGroupSeq());

                relationGoodsResponse.setOpenStartTime(goodsGroupEntity.getOpenStartTimePC());
                relationGoodsResponse.setOpenEndTime(goodsGroupEntity.getOpenEndTimePC());
                // 税率
                relationGoodsResponse.setTaxRate(goodsGroupEntity.getTaxRate());

                // 通常価格 - 税込計算
                relationGoodsResponse.setGoodsPrice(goodsGroupEntity.getGoodsPrice());
                relationGoodsResponse.setGoodsPriceInTax(
                                calculatePriceUtility.getTaxIncludedPrice(goodsGroupEntity.getGoodsPrice(),
                                                                          goodsGroupEntity.getTaxRate()
                                                                         ));

                // 新着日付
                relationGoodsResponse.setWhatsnewDate(goodsGroupEntity.getWhatsnewDate());
            }

            // 在庫状況表示
            if (dto.getGoodsGroupDto().getBatchUpdateStockStatus() != null) {
                StockStatusDisplayEntity batchUpdateStockStatus = dto.getGoodsGroupDto().getBatchUpdateStockStatus();
                StockStatusDisplayResponse stockStatusDisplayResponse = new StockStatusDisplayResponse();
                stockStatusDisplayResponse.setGoodsGroupSeq(batchUpdateStockStatus.getGoodsGroupSeq());
                stockStatusDisplayResponse.setStockStatus(
                                EnumTypeUtil.getValue(batchUpdateStockStatus.getStockStatusPc()));
                stockStatusDisplayResponse.setRegistTime(batchUpdateStockStatus.getRegistTime());
                stockStatusDisplayResponse.setUpdateTime(batchUpdateStockStatus.getUpdateTime());
                relationGoodsResponse.setBatchUpdateStockStatus(stockStatusDisplayResponse);
            }

            // アイコン情報の取得
            List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList =
                            dto.getGoodsGroupDto().getGoodsInformationIconDetailsDtoList();
            List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponses = new ArrayList<>();
            if (goodsInformationIconDetailsDtoList != null) {
                for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsInformationIconDetailsDtoList) {
                    GoodsInformationIconDetailsResponse goodsInformationIconDetailsResponse =
                                    new GoodsInformationIconDetailsResponse();
                    goodsInformationIconDetailsResponse.setGoodsGroupSeq(
                                    goodsInformationIconDetailsDto.getGoodsGroupSeq());
                    goodsInformationIconDetailsResponse.setIconSeq(goodsInformationIconDetailsDto.getIconSeq());
                    goodsInformationIconDetailsResponse.setIconName(goodsInformationIconDetailsDto.getIconName());
                    goodsInformationIconDetailsResponse.setColorCode(goodsInformationIconDetailsDto.getColorCode());
                    goodsInformationIconDetailsResponse.setOrderDisplay(
                                    goodsInformationIconDetailsDto.getOrderDisplay());

                    goodsInformationIconDetailsResponses.add(goodsInformationIconDetailsResponse);
                }
            }
            relationGoodsResponse.setGoodsInformationIconDetailsList(goodsInformationIconDetailsResponses);

            relationGoodsList.add(relationGoodsResponse);
        }

        relationGoodsListResponse.setRelationGoodsList(relationGoodsList);

        return relationGoodsListResponse;
    }
}