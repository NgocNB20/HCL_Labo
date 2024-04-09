/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDetailsGetByCodeLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.group.OpenGoodsGroupDetailsGetService;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListGetRequest;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公開商品グループ詳細情報取得
 * 代表商品SEQを元に、同じ代表商品を持つ公開中の商品情報のリストを取得する。
 *
 * @author ozaki
 * @author kaneko　(itec) チケット対応　#2644　訪問者数にクローラが含まれている
 * @author Nishigaki (itec) 2011/12/28 チケット #2699 対応
 */
@Service
public class OpenGoodsGroupDetailsGetServiceImpl extends AbstractShopService
                implements OpenGoodsGroupDetailsGetService {

    /**
     * 商品グループ情報取得ロジッククラス
     */
    private final GoodsGroupDetailsGetByCodeLogic goodsGroupDetailsGetByCodeLogic;

    /**
     * カテゴリ登録商品マップ取得ロジック
     */
    private final CategoryGoodsMapGetLogic categoryGoodsMapGetLogic;

    /**  変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 在庫API */
    private final StockApi stockApi;

    /** 日付ユーティリティ */
    private final DateUtility dateUtility;

    @Autowired
    public OpenGoodsGroupDetailsGetServiceImpl(GoodsGroupDetailsGetByCodeLogic goodsGroupDetailsGetByCodeLogic,
                                               CategoryGoodsMapGetLogic categoryGoodsMapGetLogic,
                                               ConversionUtility conversionUtility,
                                               StockApi stockApi,
                                               DateUtility dateUtility) {
        this.goodsGroupDetailsGetByCodeLogic = goodsGroupDetailsGetByCodeLogic;
        this.categoryGoodsMapGetLogic = categoryGoodsMapGetLogic;
        this.conversionUtility = conversionUtility;
        this.stockApi = stockApi;
        this.dateUtility = dateUtility;
    }

    /**
     * 公開商品グループ詳細情報取得
     * 代表商品SEQを元に、同じ代表商品を持つ公開中の商品情報のリストを取得する。
     *
     * @param goodsGroupCode            商品グループコード
     * @param goodsCode                 商品コード
     * @param siteType                  サイト種別：列挙型
     * @param openStatus                削除状態付き公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループ情報DTO
     */
    @Override
    public GoodsGroupDto execute(String goodsGroupCode,
                                 String goodsCode,
                                 HTypeSiteType siteType,
                                 HTypeOpenDeleteStatus openStatus,
                                 Date frontDisplayReferenceDate) {

        // パラメータNULLチェックは商品グループコードか商品コードのどちらかがあればOK
        String goodsInfo = null;
        if (StringUtil.isNotEmpty(goodsGroupCode)) {
            goodsInfo = goodsGroupCode;
        }
        if (StringUtil.isNotEmpty(goodsCode)) {
            goodsInfo = goodsCode;
        }

        // パラメータチェック
        // ・商品グループコードor商品コード ： null(or 空文字) の場合 エラーとして処理を終了する
        ArgumentCheckUtil.assertNotEmpty("goodsGroupCode", goodsInfo);

        // 商品グループ情報を取得する
        GoodsGroupDto goodsGroupDto =
                        goodsGroupDetailsGetByCodeLogic.execute(1001, goodsGroupCode, goodsCode, siteType, openStatus,
                                                                this.conversionUtility.toTimeStamp(
                                                                                frontDisplayReferenceDate)
                                                               );

        if (goodsGroupDto == null) {
            // 商品グループなしの場合はnullを返す
            return null;
        }

        // 商品グループDTOから商品グループSEQリスト(1件)を作成
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        goodsGroupSeqList.add(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());

        // Logic処理を実行
        Map<Integer, List<CategoryGoodsEntity>> categoryGoodsMap = categoryGoodsMapGetLogic.execute(goodsGroupSeqList);

        // 取得した商品グループDTO．商品DTOから 商品SEQリストを作成
        List<Integer> goodsSeqList = new ArrayList<>();
        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            goodsSeqList.add(goodsDto.getGoodsEntity().getGoodsSeq());
        }

        // Logic処理を実行
        StockDetailListGetRequest request = new StockDetailListGetRequest();
        request.setGoodsSeqList(goodsSeqList);

        StockDetailListResponse response = this.stockApi.getDetails(request);
        List<GoodsStockDisplayEntity> goodsStockDisplayEntityList = toGoodsStockDisplayEntityList(response);

        List<StockDto> stockEntityList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(goodsStockDisplayEntityList)) {
            stockEntityList = toStockDtoList(goodsStockDisplayEntityList);
        }
        // 在庫マップを作成
        Map<Integer, StockDto> stockMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(stockEntityList)) {
            for (StockDto stockDto : stockEntityList) {
                stockMap.put(stockDto.getGoodsSeq(), stockDto);
            }
        }

        // サイト区分＝"管理画面" の場合
        if (siteType.isBack()) {
            // 商品グループエンティティの編集
            // 商品グループエンティティ．カテゴリ登録商品リストのセット
            goodsGroupDto.setCategoryGoodsEntityList(
                            categoryGoodsMap.get(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq()));
            // 商品グループエンティティ．商品DTO．在庫DTOのセット
            for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
                goodsDto.setStockDto(ObjectUtils.isNotEmpty(stockMap.get(goodsDto.getGoodsEntity().getGoodsSeq())) ?
                                                     stockMap.get(goodsDto.getGoodsEntity().getGoodsSeq()) :
                                                     new StockDto());
            }
        }

        return goodsGroupDto;
    }

    private List<StockDto> toStockDtoList(List<GoodsStockDisplayEntity> goodsStockDisplayEntityList) {
        List<StockDto> stockDtoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(goodsStockDisplayEntityList)) {
            goodsStockDisplayEntityList.forEach(item -> {
                StockDto stockDto = new StockDto();

                stockDto.setGoodsSeq(item.getGoodsSeq());
                stockDto.setShopSeq(1001);
                stockDto.setSalesPossibleStock(toSalesPossibleStock(item.getRealStock(), item.getOrderReserveStock(),
                                                                    item.getSafetyStock()
                                                                   ));
                stockDto.setRealStock(item.getRealStock());
                stockDto.setOrderReserveStock(item.getOrderReserveStock());
                stockDto.setRemainderFewStock(item.getRemainderFewStock());
                stockDto.setOrderPointStock(item.getOrderPointStock());
                stockDto.setSafetyStock(item.getSafetyStock());
                stockDto.setRegistTime(item.getRegistTime());
                stockDto.setUpdateTime(item.getUpdateTime());
                stockDtoList.add(stockDto);
            });
        }

        return stockDtoList;
    }

    /**
     * 商品在庫表示エンティティリストに変換
     *
     * @param stockDetailListResponse
     * @return List<GoodsStockDisplayEntity>
     */
    private List<GoodsStockDisplayEntity> toGoodsStockDisplayEntityList(StockDetailListResponse stockDetailListResponse) {

        if (ObjectUtils.isEmpty(stockDetailListResponse) || CollectionUtils.isEmpty(
                        stockDetailListResponse.getStockDetailsList())) {
            return null;
        }

        // 現在日時取得
        Timestamp currentTime = this.dateUtility.getCurrentTime();

        List<GoodsStockDisplayEntity> list = new ArrayList<>();
        stockDetailListResponse.getStockDetailsList().forEach(item -> {
            GoodsStockDisplayEntity entity = new GoodsStockDisplayEntity();
            entity.setGoodsSeq(item.getGoodsSeq());
            entity.setRemainderFewStock(item.getRemainderFewStock());
            entity.setOrderPointStock(item.getOrderPointStock());
            entity.setSafetyStock(item.getSafetyStock());
            entity.setRealStock(item.getRealStock());
            entity.setOrderReserveStock(item.getOrderReserveStock());
            entity.setRegistTime(currentTime);
            entity.setUpdateTime(currentTime);
            list.add(entity);
        });
        return list;
    }

    /**
     * 販売可能在庫数に変換
     *
     * @param realStock         実在庫数
     * @param orderReserveStock 注文確保在庫数
     * @param safeStock         安全在庫数
     * @return 販売可能在庫数
     */
    private BigDecimal toSalesPossibleStock(BigDecimal realStock, BigDecimal orderReserveStock, BigDecimal safeStock) {
        BigDecimal salesPossibleStock = BigDecimal.valueOf(
                        realStock.longValue() - orderReserveStock.longValue() - safeStock.longValue());
        return salesPossibleStock;
    }

}