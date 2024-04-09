/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.logic.goods.stock.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.DiffUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.stock.StockSettingDao;
import jp.co.itechh.quad.core.entity.goods.stock.StockSettingEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockSettingRegistLogic;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在庫設定登録
 *
 * @author kimura
 */
@Component
public class StockSettingRegistLogicImpl extends AbstractShopLogic implements StockSettingRegistLogic {

    /** 在庫設定DAO */
    private final StockSettingDao stockSettingDao;

    @Autowired
    public StockSettingRegistLogicImpl(StockSettingDao stockSettingDao) {
        this.stockSettingDao = stockSettingDao;
    }

    /**
     * 在庫設定登録
     *
     * @param goodsSeqList           商品SEQリスト
     * @param stockSettingEntityList 在庫設定エンティティリスト
     * @return 結果格納用商品SEQリスト
     */
    @Override
    public List<Integer> execute(List<Integer> goodsSeqList, List<StockSettingEntity> stockSettingEntityList) {

        // (1) パラメータチェック
        checkParameter(goodsSeqList, stockSettingEntityList);

        // (2) 在庫設定情報リストマップ（KEY:商品SEQ）を作成する
        Map<Integer, StockSettingEntity> masterStockSettingEntityMap = createMasterStockSettingEntityMap(goodsSeqList);

        // （更新用）在庫設定エンティティリスト
        List<StockSettingEntity> entityListForUpdate = new ArrayList<>();
        // （登録用）在庫設定エンティティリスト
        List<StockSettingEntity> entityListForRegist = new ArrayList<>();

        // (3) （登録用/更新用）在庫設定エンティティリストの編集
        setEntityList(stockSettingEntityList, masterStockSettingEntityMap, entityListForUpdate, entityListForRegist);

        // 結果格納用商品SEQリスト
        List<Integer> goodsSeqResultList = new ArrayList<>();

        // (4) 在庫設定情報の更新処理
        if (CollectionUtils.isNotEmpty(entityListForUpdate)) {
            updateStockSetting(entityListForUpdate, goodsSeqResultList);
        }

        // (5) 在庫設定情報の登録処理
        if (CollectionUtils.isNotEmpty(entityListForRegist)) {
            registStockSetting(entityListForRegist, goodsSeqResultList);
        }

        // (6) 戻り値
        return goodsSeqResultList;
    }

    /**
     * パラメータチェック
     *
     * @param goodsSeqList           商品SEQリスト
     * @param stockSettingEntityList 在庫設定エンティティリスト
     */
    protected void checkParameter(List<Integer> goodsSeqList, List<StockSettingEntity> stockSettingEntityList) {
        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("goodsSeqList", goodsSeqList);
        // 在庫設定エンティティリストが null でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("stockSettingEntityList", stockSettingEntityList);
    }

    /**
     * 在庫設定情報リストマップ（KEY:商品SEQ）を作成する
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 在庫設定情報リストマップ
     */
    protected Map<Integer, StockSettingEntity> createMasterStockSettingEntityMap(List<Integer> goodsSeqList) {
        // 在庫設定情報リスト取得
        List<StockSettingEntity> masterStockSettingEntityList = getMasterStockSettingEntityList(goodsSeqList);

        // 在庫設定情報リストマップ（KEY:商品SEQ）を作成する
        Map<Integer, StockSettingEntity> masterStockSettingEntityMap = new HashMap<>();
        for (StockSettingEntity stockSettingEntity : masterStockSettingEntityList) {
            masterStockSettingEntityMap.put(stockSettingEntity.getGoodsSeq(), stockSettingEntity);
        }
        return masterStockSettingEntityMap;
    }

    /**
     * 在庫設定情報リスト取得
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 在庫設定情報リスト
     */
    protected List<StockSettingEntity> getMasterStockSettingEntityList(List<Integer> goodsSeqList) {
        return stockSettingDao.getStockSettingListByGoodsGroupSeq(goodsSeqList);
    }

    /**
     * （登録用/更新用）在庫設定エンティティリストの編集
     *
     * @param stockSettingEntityList      在庫設定エンティティリスト
     * @param masterStockSettingEntityMap （DB）在庫設定エンティティリスト
     * @param entityListForUpdate         （更新用）在庫設定エンティティリスト
     * @param entityListForRegist         （登録用）在庫設定エンティティリスト
     */
    protected void setEntityList(List<StockSettingEntity> stockSettingEntityList,
                                 Map<Integer, StockSettingEntity> masterStockSettingEntityMap,
                                 List<StockSettingEntity> entityListForUpdate,
                                 List<StockSettingEntity> entityListForRegist) {
        for (StockSettingEntity stockSettingEntity : stockSettingEntityList) {
            // （パラメータ）在庫設定の商品SEQが存在する場合
            if (masterStockSettingEntityMap.get(stockSettingEntity.getGoodsSeq()) != null) {
                // （更新用）リストに追加
                setEntityListForUpdate(masterStockSettingEntityMap, entityListForUpdate, stockSettingEntity);
            }
            // （パラメータ）関連商品の関連商品グループSEQが存在しない場合
            else {
                // （登録用）リストに追加
                setEntityListForRegist(entityListForRegist, stockSettingEntity);
            }
        }
    }

    /**
     * （更新用）リストに追加
     *
     * @param masterStockSettingEntityMap （DB）在庫設定エンティティリスト
     * @param entityListForUpdate         （更新用）在庫設定エンティティリスト
     * @param stockSettingEntity          在庫設定エンティ
     */
    protected void setEntityListForUpdate(Map<Integer, StockSettingEntity> masterStockSettingEntityMap,
                                          List<StockSettingEntity> entityListForUpdate,
                                          StockSettingEntity stockSettingEntity) {
        List<String> diffResults = DiffUtil.diff(masterStockSettingEntityMap.get(stockSettingEntity.getGoodsSeq()),
                                                 stockSettingEntity
                                                );

        // 日付関連Utility取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        for (String diffResult : diffResults) {
            // 登録日時・更新日時以外が異なる場合
            if (!diffResult.endsWith(".registTime") && !diffResult.endsWith(".updateTime")) {
                // 更新日時の設定
                stockSettingEntity.setUpdateTime(dateUtility.getCurrentTime());
                // （更新用）リストに追加
                entityListForUpdate.add(stockSettingEntity);
                break;
            }
        }
    }

    /**
     * （登録用）リストに追加
     *
     * @param entityListForRegist （登録用）在庫設定エンティティリスト
     * @param stockSettingEntity  在庫設定エンティ
     */
    protected void setEntityListForRegist(List<StockSettingEntity> entityListForRegist,
                                          StockSettingEntity stockSettingEntity) {
        // 登録・更新日時の設定
        Timestamp currentTime = ApplicationContextUtility.getBean(DateUtility.class).getCurrentTime();
        stockSettingEntity.setRegistTime(currentTime);
        stockSettingEntity.setUpdateTime(currentTime);
        // （登録用）リストに追加
        entityListForRegist.add(stockSettingEntity);
    }

    /**
     * 在庫設定情報の更新処理
     *
     * @param entityListForUpdate （更新用）在庫設定エンティティリスト
     * @param goodsSeqResultList  結果格納用商品SEQリスト
     */
    protected void updateStockSetting(List<StockSettingEntity> entityListForUpdate, List<Integer> goodsSeqResultList) {
        // 在庫設定更新件数
        for (StockSettingEntity stockSettingEntity : entityListForUpdate) {
            if (stockSettingDao.update(stockSettingEntity) <= 0) {
                throwMessage(MSGCD_REGIST_UPDATE_FAIL);
            }
            goodsSeqResultList.add(stockSettingEntity.getGoodsSeq());
        }
    }

    /**
     * 在庫設定情報の登録処理
     *
     * @param entityListForRegist （登録用）在庫設定エンティティリスト
     * @param goodsSeqResultList  結果格納用商品SEQリスト
     */
    protected void registStockSetting(List<StockSettingEntity> entityListForRegist, List<Integer> goodsSeqResultList) {
        // 在庫設定登録件数
        for (StockSettingEntity stockSettingEntity : entityListForRegist) {
            if (stockSettingDao.insert(stockSettingEntity) <= 0) {
                throwMessage(MSGCD_REGIST_UPDATE_FAIL);
            }
            goodsSeqResultList.add(stockSettingEntity.getGoodsSeq());
        }
    }

}