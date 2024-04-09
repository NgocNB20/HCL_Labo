/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.presentation.inventory;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.dto.goods.stock.StockDetailsDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockResultSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockStatusDisplayConditionDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListGetRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayGetRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 在庫 helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class InventoryHelper {

    /**  ここからボトムアップ定義のhelperメソッド */

    /**
     * 変換Utility取得
     */
    private final ConversionUtility conversionUtility;

    public InventoryHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 在庫レスポンスに変換処理
     *
     * @param stockDetailsDto 在庫詳細dto
     * @return 在庫レスポンス
     */
    public InventoryResponse toInventoryResponse(StockDetailsDto stockDetailsDto) {

        InventoryResponse inventoryResponse = new InventoryResponse();

        inventoryResponse.setGoodsSeq(stockDetailsDto.getGoodsSeq());
        inventoryResponse.setGoodsGroupSeq(stockDetailsDto.getGoodsGroupSeq());
        inventoryResponse.setGoodsGroupName(stockDetailsDto.getGoodsGroupName());
        inventoryResponse.setGoodsCode(stockDetailsDto.getGoodsCode());
        inventoryResponse.setGoodsPriceInTax(stockDetailsDto.getGoodsPriceInTax());
        inventoryResponse.setSaleStatusPC(EnumTypeUtil.getValue(stockDetailsDto.getSaleStatusPC()));
        inventoryResponse.setSaleStartTimePC(stockDetailsDto.getSaleStartTimePC());
        inventoryResponse.setSaleEndTimePC(stockDetailsDto.getSaleEndTimePC());
        inventoryResponse.setStockManagementFlag(EnumTypeUtil.getValue(stockDetailsDto.getStockManagementFlag()));
        inventoryResponse.setUnitValue1(stockDetailsDto.getUnitValue1());
        inventoryResponse.setUnitValue2(stockDetailsDto.getUnitValue2());
        inventoryResponse.setJanCode(stockDetailsDto.getJanCode());
        inventoryResponse.setSalesPossibleStock(stockDetailsDto.getSalesPossibleStock());
        inventoryResponse.setRealStock(stockDetailsDto.getRealStock());
        inventoryResponse.setOrderReserveStock(stockDetailsDto.getOrderReserveStock());
        inventoryResponse.setRemainderFewStock(stockDetailsDto.getRemainderFewStock());
        inventoryResponse.setOrderPointStock(stockDetailsDto.getOrderPointStock());
        inventoryResponse.setSafetyStock(stockDetailsDto.getSafetyStock());
        inventoryResponse.setGoodsGroupCode(stockDetailsDto.getGoodsGroupCode());
        inventoryResponse.setGoodsOpenStatusPC(EnumTypeUtil.getValue(stockDetailsDto.getGoodsOpenStatusPC()));
        inventoryResponse.setOpenStartTimePC(stockDetailsDto.getOpenStartTimePC());
        inventoryResponse.setOpenEndTimePC(stockDetailsDto.getOpenEndTimePC());
        inventoryResponse.setUnitTitle1(stockDetailsDto.getUnitTitle1());
        inventoryResponse.setUnitTitle2(stockDetailsDto.getUnitTitle2());

        return inventoryResponse;
    }

    /**
     * 在庫リクエストからDtoに変換
     *
     * @param inventoryResultListGetRequest 入庫実績一覧リクエスト
     * @return 入庫実績Dao用検索条件Dtoクラス
     */
    public StockResultSearchForDaoConditionDto toStockResultSearchForDaoConditionDto(InventoryResultListGetRequest inventoryResultListGetRequest) {

        StockResultSearchForDaoConditionDto stockResultSearchForDaoConditionDto =
                        new StockResultSearchForDaoConditionDto();

        stockResultSearchForDaoConditionDto.setGoodsSeq(inventoryResultListGetRequest.getGoodsSeq());

        return stockResultSearchForDaoConditionDto;
    }

    /**
     * 入庫実績一覧レスポンスに変換
     *
     * @param stockResultEntities 入庫実績エンティティ
     * @return 入庫実績一覧レスポンス
     */
    public InventoryResultListResponse toInventoryResultListResponse(List<StockResultEntity> stockResultEntities) {

        InventoryResultListResponse inventoryResultListResponse = new InventoryResultListResponse();
        List<InventoryResultResponse> inventoryResultListResponses = new ArrayList<>();

        for (StockResultEntity stockResultEntity : stockResultEntities) {
            InventoryResultResponse inventoryResultResponse = new InventoryResultResponse();
            inventoryResultResponse.setStockResultSeq(stockResultEntity.getStockResultSeq());
            inventoryResultResponse.setGoodsSeq(stockResultEntity.getGoodsSeq());
            inventoryResultResponse.setSupplementTime(stockResultEntity.getSupplementTime());
            inventoryResultResponse.setSupplementCount(stockResultEntity.getSupplementCount());
            inventoryResultResponse.setProcessPersonName(stockResultEntity.getProcessPersonName());
            inventoryResultResponse.setStockManagementFlag(
                            EnumTypeUtil.getValue(stockResultEntity.getStockManagementFlag()));
            inventoryResultResponse.setNote(stockResultEntity.getNote());
            inventoryResultResponse.setRegistTime(stockResultEntity.getRegistTime());
            inventoryResultResponse.setRealStock(stockResultEntity.getRealStock());
            inventoryResultResponse.setUpdateTime(stockResultEntity.getUpdateTime());
            inventoryResultListResponses.add(inventoryResultResponse);
        }

        inventoryResultListResponse.setInventoryResultList(inventoryResultListResponses);

        return inventoryResultListResponse;
    }

    /**
     * 在庫状態表示判定用DTOに変換
     *
     * @param inventoryStatusDisplayGetRequest 在庫状況表示取得リクエスト
     * @return 在庫状態表示判定用DTO
     */
    public StockStatusDisplayConditionDto toStockStatusDisplayConditionDto(InventoryStatusDisplayGetRequest inventoryStatusDisplayGetRequest) {
        StockStatusDisplayConditionDto stockStatusDisplayConditionDto = new StockStatusDisplayConditionDto();
        if (inventoryStatusDisplayGetRequest != null) {
            if (inventoryStatusDisplayGetRequest.getSaleStatus() != null) {
                HTypeGoodsSaleStatus goodsSaleStatus = EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                                     inventoryStatusDisplayGetRequest.getSaleStatus()
                                                                                    );
                stockStatusDisplayConditionDto.setSaleStatus(goodsSaleStatus);
            }
            stockStatusDisplayConditionDto.setSaleStartTime(
                            conversionUtility.toTimestamp(inventoryStatusDisplayGetRequest.getSaleStartTime()));
            stockStatusDisplayConditionDto.setSaleEndTime(
                            conversionUtility.toTimestamp(inventoryStatusDisplayGetRequest.getSaleEndTime()));
            if (inventoryStatusDisplayGetRequest.getStockManagementFlag() != null) {
                HTypeStockManagementFlag stockManagementFlag =
                                EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                              inventoryStatusDisplayGetRequest.getStockManagementFlag()
                                                             );
                stockStatusDisplayConditionDto.setStockManagementFlag(stockManagementFlag);
            }
            stockStatusDisplayConditionDto.setRemainderFewStock(
                            inventoryStatusDisplayGetRequest.getRemainderFewStock());
            stockStatusDisplayConditionDto.setSalesPossibleStock(
                            inventoryStatusDisplayGetRequest.getSalesPossibleStock());
        }
        return stockStatusDisplayConditionDto;
    }

    /**
     * 在庫状況表示レスポンスに変換
     *
     * @param currentStatus 現在処理中の在庫状況
     * @return 在庫状況表示レスポンス
     */
    public InventoryStatusDisplayResponse toInventoryStatusDisplayResponse(String currentStatus) {
        InventoryStatusDisplayResponse inventoryStatusDisplayResponse = new InventoryStatusDisplayResponse();

        inventoryStatusDisplayResponse.setCurrentStatus(currentStatus);

        return inventoryStatusDisplayResponse;
    }

    /**  ここまでボトムアップ定義のhelperメソッド */
}