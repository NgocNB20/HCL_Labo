/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.logic.goods.stock.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.stock.StockDao;
import jp.co.itechh.quad.core.dao.goods.stock.StockResultDao;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockEntity;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockRegistAndSupplementLogic;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 在庫登録と入庫処理用ロジック
 *
 * @author kimura
 */
@Component
public class StockRegistAndSupplementLogicImpl extends AbstractShopLogic implements StockRegistAndSupplementLogic {

    /** 在庫DAO */
    private final StockDao stockDao;

    /** 入庫実績Dao */
    private final StockResultDao stockResultDao;

    /** 日付ユーティリティ */
    private final DateUtility dateUtility;

    @Autowired
    public StockRegistAndSupplementLogicImpl(StockDao stockDao,
                                             StockResultDao stockResultDao,
                                             DateUtility dateUtility) {
        this.stockDao = stockDao;
        this.stockResultDao = stockResultDao;
        this.dateUtility = dateUtility;
    }

    /**
     * 在庫登録または入庫処理を行う
     *
     * @param stockDtoList     在庫Dtoリスト
     * @return 結果格納用商品SEQリスト
     */
    @Override
    public List<Integer> execute(List<StockDto> stockDtoList) {

        // 在庫Dtoリストが null でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("stockDtoList", stockDtoList);

        // 結果格納用商品SEQリスト
        List<Integer> goodsSeqResultList = new ArrayList<>();

        for (StockDto dto : stockDtoList) {

            StockDto stockDtoDb = this.stockDao.getStock(dto.getGoodsSeq());

            if (ObjectUtils.isNotEmpty(stockDtoDb)) {

                // 入庫日時が設定されている場合、入庫処理を行う
                if (ObjectUtils.isNotEmpty(dto.getSupplementTime())) {
                    if (this.stockDao.updateStockSupplement(1001, dto.getGoodsSeq(), dto.getSupplementCount()) <= 0) {
                        throwMessage(MSGCD_SUPPLEMENT_FAIL);
                    }

                    // 入庫実績SEQ取得
                    int stockResultSeq = this.stockResultDao.getStockResultSeqNextVal();
                    // 入庫実績情報の登録処理
                    if (this.stockResultDao.insertStockSupplementHistory(
                                    1001, dto.getGoodsSeq(), toStockResultEntityForNoRealStock(dto, stockResultSeq))
                        <= 0) {
                        throwMessage(MSGCD_SUPPLEMENT_FAIL);
                    }

                    // 処理結果設定
                    goodsSeqResultList.add(dto.getGoodsSeq());
                }
            } else {

                // 「入庫数がマイナス」の場合は、登録させない
                if (dto.getSupplementCount() != null && dto.getSupplementCount().compareTo(BigDecimal.ZERO) < 0) {
                    throwMessage(MSGCD_SUPPLEMENT_FAIL);
                }

                // 在庫情報を新規登録
                if (this.stockDao.insert(toStockEntity(dto)) <= 0) {
                    throwMessage(MSGCD_REGIST_FAIL);
                }

                // 入庫日時が設定されている場合、入庫処理を行う
                if (ObjectUtils.isNotEmpty(dto.getSupplementTime())) {
                    if (this.stockResultDao.insert(toStockResultEntity(dto)) <= 0) {
                        throwMessage(MSGCD_SUPPLEMENT_FAIL);
                    }
                }

                // 処理結果設定
                goodsSeqResultList.add(dto.getGoodsSeq());
            }
        }

        return goodsSeqResultList;
    }

    /**
     * 在庫エンティティに変換
     *
     * @param stockDto 在庫Dto
     * @return StockEntity
     */
    public StockEntity toStockEntity(StockDto stockDto) {

        // 現在日時取得
        Timestamp currentTime = this.dateUtility.getCurrentTime();

        StockEntity stockEntity = new StockEntity();
        stockEntity.setGoodsSeq(stockDto.getGoodsSeq());
        stockEntity.setShopSeq(1001);
        // 初回登録時に入庫数が指定されている場合は実在庫とみなす
        stockEntity.setRealStock(stockDto.getSupplementCount());
        stockEntity.setOrderReserveStock(new BigDecimal(0));
        stockEntity.setRegistTime(currentTime);
        stockEntity.setUpdateTime(currentTime);
        return stockEntity;
    }

    /**
     * 入庫実績エンティティに変換（実在庫未設定）
     *
     * @param stockDto       在庫Dto
     * @param stockResultSeq 最新入庫実績SEQ
     * @return StockResultEntity
     */
    public StockResultEntity toStockResultEntityForNoRealStock(StockDto stockDto, Integer stockResultSeq) {

        // 現在日時取得
        Timestamp currentTime = this.dateUtility.getCurrentTime();

        StockResultEntity stockResultEntity = new StockResultEntity();
        stockResultEntity.setStockResultSeq(stockResultSeq);
        stockResultEntity.setGoodsSeq(stockDto.getGoodsSeq());
        stockResultEntity.setSupplementTime(stockDto.getSupplementTime());
        stockResultEntity.setSupplementCount(stockDto.getSupplementCount());
        stockResultEntity.setProcessPersonName(stockDto.getProcessPersonName());
        stockResultEntity.setStockManagementFlag(stockDto.getStockManagementFlag());
        stockResultEntity.setRegistTime(currentTime);
        stockResultEntity.setUpdateTime(currentTime);
        return stockResultEntity;
    }

    /**
     * 入庫実績エンティティに変換（実在庫設定）
     *
     * @param stockDto 在庫Dto
     * @return StockResultEntity
     */
    public StockResultEntity toStockResultEntity(StockDto stockDto) {

        // 現在日時取得
        Timestamp currentTime = this.dateUtility.getCurrentTime();

        StockResultEntity stockResultEntity = new StockResultEntity();
        stockResultEntity.setGoodsSeq(stockDto.getGoodsSeq());
        stockResultEntity.setSupplementTime(stockDto.getSupplementTime());
        stockResultEntity.setSupplementCount(stockDto.getSupplementCount());
        // 初回登録時に入庫数が指定されている場合は実在庫とみなす
        stockResultEntity.setRealStock(stockDto.getSupplementCount());
        stockResultEntity.setProcessPersonName(stockDto.getProcessPersonName());
        stockResultEntity.setStockManagementFlag(stockDto.getStockManagementFlag());
        stockResultEntity.setRegistTime(currentTime);
        stockResultEntity.setUpdateTime(currentTime);
        return stockResultEntity;
    }

}