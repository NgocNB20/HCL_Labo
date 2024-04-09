/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.dao.goods.stock.StockResultDao;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockResultRegistByStockManagementFlagLogic;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.AdministratorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 在庫管理フラグ変更の入庫実績を登録
 * <pre>
 * 入庫数は0固定
 * 商品稼働率分析の棚卸在庫数を計算する際に、
 * 指定された集計日の商品情報が必要となるため、
 * 在庫管理フラグ変更時に入庫実績を登録する。
 * </pre>
 *
 * @author ito
 *
 */
@Component
public class StockResultRegistByStockManagementFlagLogicImpl extends AbstractShopLogic
                implements StockResultRegistByStockManagementFlagLogic {

    /** 管理者業務ヘルパークラス */
    private final AdministratorUtility administratorUtility;

    /** 入庫実績Dao */
    private final StockResultDao stockResultDao;

    @Autowired
    public StockResultRegistByStockManagementFlagLogicImpl(AdministratorUtility administratorUtility,
                                                           StockResultDao stockResultDao) {
        this.administratorUtility = administratorUtility;
        this.stockResultDao = stockResultDao;
    }

    /**
     * 実行メソッド
     *
     * @param administratorSeq 管理者SEQ
     * @param shopSeq ショップSEQ
     * @param goodsSeq 商品SEQ
     * @param stockManagementFlag 在庫管理フラグ
     * @return 登録件数
     */
    @Override
    public int execute(Integer administratorSeq, Integer shopSeq, Integer goodsSeq, String stockManagementFlag) {

        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("administratorSeq is null", administratorSeq);
        ArgumentCheckUtil.assertNotNull("shopSeq is null", shopSeq);
        ArgumentCheckUtil.assertNotNull("goodsSeq is null", goodsSeq);
        ArgumentCheckUtil.assertNotEmpty("stockManagementFlag is empty", stockManagementFlag);

        // (2)入庫実績エンティティセット
        StockResultEntity stockResultEntity = createStockResultEntity(administratorSeq, goodsSeq, stockManagementFlag);

        // (3)入庫実績情報の登録処理
        return stockResultDao.insertStockSupplementHistory(shopSeq, goodsSeq, stockResultEntity);

    }

    /**
     * 商品エンティティから入庫実績エンティティ作成
     *
     * @param administratorSeq 管理者SEQ
     * @param goodsSeq 商品SEQ
     * @param stockManagementFlag 在庫管理フラグ
     * @return 入庫実績エンティティ
     */
    protected StockResultEntity createStockResultEntity(Integer administratorSeq,
                                                        Integer goodsSeq,
                                                        String stockManagementFlag) {
        StockResultEntity stockResultEntity = ApplicationContextUtility.getBean(StockResultEntity.class);
        stockResultEntity.setGoodsSeq(goodsSeq);
        stockResultEntity.setSupplementCount(BigDecimal.ZERO);
        stockResultEntity.setStockManagementFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class, stockManagementFlag));

        // 処理担当者名取得
        String administratorName = administratorUtility.getAdministratorName(administratorSeq);
        stockResultEntity.setProcessPersonName(administratorName);

        // 入庫実績SEQ取得
        Integer stockResultSeq = stockResultDao.getStockResultSeqNextVal();
        stockResultEntity.setStockResultSeq(stockResultSeq);

        return stockResultEntity;
    }
}