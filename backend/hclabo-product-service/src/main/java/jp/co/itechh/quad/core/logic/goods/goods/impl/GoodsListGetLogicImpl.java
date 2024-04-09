/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dao.goods.stock.StockDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsListGetLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockDisplayGetLogic;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 商品リスト取得
 *
 * @author ozaki
 * @version $Revision: 1.7 $
 *
 */
@Component
public class GoodsListGetLogicImpl extends AbstractShopLogic implements GoodsListGetLogic {

    /** 商品DAO */
    private final GoodsDao goodsDao;

    /** 在庫DAO */
    private final StockDao stockDao;

    private final GoodsStockDisplayGetLogic goodsStockDisplayGetLogic;

    @Autowired
    public GoodsListGetLogicImpl(GoodsDao goodsDao,
                                 StockDao stockDao,
                                 GoodsStockDisplayGetLogic goodsStockDisplayGetLogic) {
        this.goodsDao = goodsDao;
        this.stockDao = stockDao;
        this.goodsStockDisplayGetLogic = goodsStockDisplayGetLogic;
    }

    /**
     *
     * 商品リスト取得
     * 商品情報リストを取得する。
     *
     * @param goodsSearchForDaoConditionDto 商品Dao用検索条件DTO
     * @return 商品DTOリスト
     */
    @Override
    public List<GoodsDto> execute(GoodsSearchForDaoConditionDto goodsSearchForDaoConditionDto) {

        // (1) パラメータチェック
        // 商品Dao用検索条件DTOが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsSearchForDaoConditionDto", goodsSearchForDaoConditionDto);

        // (2) 商品情報リスト取得処理
        // 商品Daoの商品リスト取得処理を実行する。
        // DAO GoodsDao
        // メソッド List<商品エンティティ> getSearchGoodsList( （パラメータ）商品Dao用検索条件DTO)
        List<GoodsEntity> goodsEntityList = goodsDao.getSearchGoodsList(goodsSearchForDaoConditionDto);
        // (2) で取得した商品エンティティリストから商品エンティティ．商品SEQのリストを作成する。
        List<Integer> goodsSeqList = new ArrayList<>();
        for (int i = 0; i < goodsEntityList.size(); i++) {
            GoodsEntity goodsEntity = goodsEntityList.get(i);
            goodsSeqList.add(goodsEntity.getGoodsSeq());
        }

        // 在庫情報取得
        Map<Integer, StockDto> stockDtoMap = new HashMap<>();
        if (goodsSeqList.size() > 0) {
            List<GoodsStockDisplayEntity> goodsStockDisplayEntityList = goodsStockDisplayGetLogic.execute(goodsSeqList);
            List<StockDto> stockDtoList = toStockDtoList(goodsStockDisplayEntityList);
            for (StockDto stockDto : stockDtoList) {
                stockDtoMap.put(stockDto.getGoodsSeq(), stockDto);
            }
        }
        // (4) （戻り値用）商品DTOリストを編集する。
        // （戻り値用）商品DTOリストを初期化する。
        // ・(2)で取得した商品エンティティリストについて以下の処理を行う
        // ①商品DTOオブジェクトを初期生成する。
        // ②商品DTOに商品エンティティをセットする
        // ③商品エンティティ．商品SEQをキー項目として(3)で取得した商品画像マップから商品画像エンティティリストを取得し、商品DTOにセットする。
        List<GoodsDto> goodsDtoList = new ArrayList<>();
        for (int i = 0; i < goodsEntityList.size(); i++) {
            GoodsDto goodsDto = ApplicationContextUtility.getBean(GoodsDto.class);
            goodsDto.setGoodsEntity(goodsEntityList.get(i));
            goodsDto.setStockDto(ObjectUtils.isNotEmpty(stockDtoMap.get(goodsDto.getGoodsEntity().getGoodsSeq())) ?
                                                 stockDtoMap.get(goodsDto.getGoodsEntity().getGoodsSeq()) :
                                                 new StockDto());

            // ⑤商品DTOを（戻り値用）商品DTOリストに追加する。
            goodsDtoList.add(goodsDto);
        }

        // (5) 戻り値
        // （戻り値用）商品DTOリストを返す。
        return goodsDtoList;
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