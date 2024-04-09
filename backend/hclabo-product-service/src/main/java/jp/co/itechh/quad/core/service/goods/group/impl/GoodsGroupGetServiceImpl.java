/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockDisplayGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDetailsGetByCodeLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupGetService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品グループ取得サービス実装クラス
 *
 * @author hirata
 * @version $Revision: 1.7 $
 */
@Service
public class GoodsGroupGetServiceImpl extends AbstractShopService implements GoodsGroupGetService {

    /**
     * 商品グループ詳細取得（商品グループコード）ロジック
     */
    private final GoodsGroupDetailsGetByCodeLogic goodsGroupDetailsGetByCodeLogic;

    /**
     * カテゴリ登録商品マップ取得ロジック
     */
    private final CategoryGoodsMapGetLogic categoryGoodsMapGetLogic;

    /**
     * 商品在庫表示リスト取得
     */
    private final GoodsStockDisplayGetLogic goodsStockDisplayGetLogic;

    @Autowired
    public GoodsGroupGetServiceImpl(GoodsGroupDetailsGetByCodeLogic goodsGroupDetailsGetByCodeLogic,
                                    CategoryGoodsMapGetLogic categoryGoodsMapGetLogic,
                                    GoodsStockDisplayGetLogic goodsStockDisplayGetLogic) {

        this.goodsGroupDetailsGetByCodeLogic = goodsGroupDetailsGetByCodeLogic;
        this.categoryGoodsMapGetLogic = categoryGoodsMapGetLogic;
        this.goodsStockDisplayGetLogic = goodsStockDisplayGetLogic;
    }

    /**
     * 実行メソッド
     *
     * @param goodsGroupCode 商品グループコード
     * @param shopSeq        ショップSEQ
     * @param siteType       サイト種別
     * @return 商品グループDto
     */
    public GoodsGroupDto execute(String goodsGroupCode, Integer shopSeq, HTypeSiteType siteType) {

        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupCode", goodsGroupCode);

        // (2)Logic処理を実行
        GoodsGroupDto goodsGroupDto =
                        goodsGroupDetailsGetByCodeLogic.execute(shopSeq, goodsGroupCode, null, siteType, null, null);

        if (goodsGroupDto == null) {
            // 商品グループなしの場合はnullを返す
            return null;
        }

        // 商品グループDTOから商品グループSEQリスト(1件)を作成
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        goodsGroupSeqList.add(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());

        // (3)Logic処理を実行
        Map<Integer, List<CategoryGoodsEntity>> categoryGoodsMap = categoryGoodsMapGetLogic.execute(goodsGroupSeqList);

        // 取得した商品グループDTO．商品DTOから 商品SEQリストを作成
        List<Integer> goodsSeqList = new ArrayList<>();
        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            goodsSeqList.add(goodsDto.getGoodsEntity().getGoodsSeq());
        }

        // (4)Logic処理を実行
        List<GoodsStockDisplayEntity> goodsStockDisplayEntityList = goodsStockDisplayGetLogic.execute(goodsSeqList);
        List<StockDto> stockEntityList = toStockDtoList(goodsStockDisplayEntityList);
        // 在庫マップを作成
        Map<Integer, StockDto> stockMap = new HashMap<>();
        for (StockDto stockDto : stockEntityList) {
            stockMap.put(stockDto.getGoodsSeq(), stockDto);
        }

        // サイト区分＝"管理画面" の場合のみ(5)の処理を行う
        if (siteType.isBack()) {
            // (5) 商品グループエンティティの編集
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
        return stockDtoList;
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