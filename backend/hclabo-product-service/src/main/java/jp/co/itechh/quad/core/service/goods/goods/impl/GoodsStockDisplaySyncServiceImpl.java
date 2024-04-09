/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.service.goods.goods.impl;

import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryProductBatchRequest;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsStockDisplayDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockDisplaySyncLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsStockDisplaySyncService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListGetRequest;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品在庫表示ロジック実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class GoodsStockDisplaySyncServiceImpl extends AbstractShopService implements GoodsStockDisplaySyncService {

    /** 在庫API */
    private final StockApi stockApi;

    /** 商品在庫表示Daoクラス */
    private final GoodsStockDisplayDao goodsStockDisplayDao;

    /** 商品在庫表示非同期ロジック */
    private final GoodsStockDisplaySyncLogic goodsStockDisplaySyncLogic;

    /** 日付ユーティリティ */
    private final DateUtility dateUtility;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** カテゴリApi */
    private final CategoryApi categoryApi;

    /** 商品DAO */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsStockDisplaySyncServiceImpl(StockApi stockApi,
                                            GoodsStockDisplayDao goodsStockDisplayDao,
                                            GoodsStockDisplaySyncLogic goodsStockDisplaySyncLogic,
                                            DateUtility dateUtility,
                                            AsyncService asyncService,
                                            CategoryApi categoryApi,
                                            GoodsDao goodsDao) {
        this.stockApi = stockApi;
        this.goodsStockDisplayDao = goodsStockDisplayDao;
        this.goodsStockDisplaySyncLogic = goodsStockDisplaySyncLogic;
        this.dateUtility = dateUtility;
        this.asyncService = asyncService;
        this.categoryApi = categoryApi;
        this.goodsDao = goodsDao;
    }

    /**
     * 在庫情報アップサート
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 件数
     */
    @Override
    public int syncUpsertStock(List<Integer> goodsSeqList) {

        StockDetailListGetRequest request = new StockDetailListGetRequest();
        request.setGoodsSeqList(goodsSeqList);

        StockDetailListResponse response = this.stockApi.getDetails(request);
        // 更新後情報
        List<GoodsStockDisplayEntity> targetList = toGoodsStockDisplayEntityList(response);

        // 更新前情報
        List<GoodsStockDisplayEntity> originList = goodsStockDisplayDao.getGoodsStockDisplayList(goodsSeqList);

        // 商品在庫表示ロジック
        int cnt = goodsStockDisplaySyncLogic.syncUpsertStock(targetList);

        // バッチ更新対象リスト
        List<Integer> goodsSeqChangeList = new ArrayList<>();

        if (originList != null && targetList != null) {
            Map<Object, GoodsStockDisplayEntity> targetMap = new HashMap<>();

            for (GoodsStockDisplayEntity targetGoodStockDisplayEntity : targetList) {
                if (targetGoodStockDisplayEntity.getGoodsSeq() != null) {
                    targetMap.put(targetGoodStockDisplayEntity.getGoodsSeq(), targetGoodStockDisplayEntity);
                }
            }

            for (GoodsStockDisplayEntity originGoodStockDisplayEntity : originList) {
                if (originGoodStockDisplayEntity.getGoodsSeq() != null) {
                    GoodsStockDisplayEntity targetGoodStockDisplayEntity =
                                    targetMap.get(originGoodStockDisplayEntity.getGoodsSeq());
                    BigDecimal salesPossibleStock = toSalesPossibleStock(targetGoodStockDisplayEntity);
                    if (salesPossibleStock != null
                        && salesPossibleStock.compareTo(toSalesPossibleStock(originGoodStockDisplayEntity)) != 0) {
                        goodsSeqChangeList.add(originGoodStockDisplayEntity.getGoodsSeq());
                    }
                }
            }
        }

        // sizeが1以上ならバッチ処理を行う
        if (goodsSeqChangeList.size() > 0) {

            CategoryProductBatchRequest categoryProductBatchRequest = new CategoryProductBatchRequest();
            List<Integer> goodsGroupSeqList = goodsDao.getGoodsGroupSeqListByGoodsSeqList(goodsSeqChangeList);
            categoryProductBatchRequest.setGoodsGroupSeqList(goodsGroupSeqList);

            Object[] objectRequest = new Object[] {categoryProductBatchRequest};
            Class<?>[] typeClass = new Class<?>[] {CategoryProductBatchRequest.class};

            // カテゴリ商品更新バッチを呼び出す
            asyncService.asyncService(categoryApi, "updateCategoryProductBatch", objectRequest, typeClass);
        }

        return cnt;
    }

    /**
     * 商品在庫表示エンティティリストに変換
     *
     * @param stockDetailListResponse 在庫詳細一覧レスポンス
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
     * @param goodsStockDisplayEntity  商品在庫表示
     * @return 販売可能在庫数
     */
    private BigDecimal toSalesPossibleStock(GoodsStockDisplayEntity goodsStockDisplayEntity) {

        BigDecimal salesPossibleStock = null;

        if (goodsStockDisplayEntity != null && goodsStockDisplayEntity.getRealStock() != null
            && goodsStockDisplayEntity.getOrderReserveStock() != null
            && goodsStockDisplayEntity.getSafetyStock() != null) {
            salesPossibleStock = BigDecimal.valueOf(goodsStockDisplayEntity.getRealStock().longValue()
                                                    - goodsStockDisplayEntity.getOrderReserveStock().longValue()
                                                    - goodsStockDisplayEntity.getSafetyStock().longValue());
        }

        return salesPossibleStock;
    }

}