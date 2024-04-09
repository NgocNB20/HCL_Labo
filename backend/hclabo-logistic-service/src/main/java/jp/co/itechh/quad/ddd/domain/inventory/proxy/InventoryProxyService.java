/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.inventory.proxy;

import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.ddd.domain.inventory.repository.IInventoryRepository;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.InventoryTargetItem;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

/**
 * 在庫プロキシサービス
 */
@Service
public class InventoryProxyService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryProxyService.class);

    /** 在庫リポジトリ */
    private final IInventoryRepository inventoryRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** コンストラクタ */
    @Autowired
    public InventoryProxyService(IInventoryRepository inventoryRepository, IProductAdapter productAdapter) {
        this.inventoryRepository = inventoryRepository;
        this.productAdapter = productAdapter;
    }

    /**
     * 在庫を引き当てる<br/>
     * フェーズ１のStockReserveUpdateLogic#executeと同様の処理（引数は異なる）
     *
     * @param itemList 対象商品リスト
     * @return result 更新件数
     */
    public int secureInventory(List<InventoryTargetItem> itemList) {

        // チェック
        AssertChecker.assertNotEmpty("InventoryTargetItemList is empty", itemList);

        int result = 0;
        for (InventoryTargetItem item : itemList) {
            result += this.inventoryRepository.secureStock(item.getItemId(), item.getItemCount().getValue());
        }

        return result;
    }

    /**
     * 在庫引当を戻す<br/>
     * フェーズ１のStockRollbackReserveUpdateLogic#executeと同様の処理（引数は異なる）
     *
     * @param itemList 対象商品リスト
     * @return result 更新件数
     */
    public int rollbackReserveInventory(List<InventoryTargetItem> itemList) {

        // チェック
        AssertChecker.assertNotEmpty("InventoryTargetItemList is empty", itemList);

        int result = 0;
        for (InventoryTargetItem item : itemList) {
            // 商品数量のパラメーターがポジティブなので在庫引当を戻すためマイナスに変換する必要がある
            result += this.inventoryRepository.updateSecuredStockRollback(item.getItemId(),
                                                                          item.getItemCount().getValue() * -1
                                                                         );
        }

        return result;
    }

    /**
     * 出荷実績を登録する（在庫確定）<br/>
     * フェーズ１のStockShipmentUpdateLogic#executeと同様の処理（引数は異なる）
     *
     * @param itemList 対象商品リスト
     */
    public void registShipmentResult(List<InventoryTargetItem> itemList) {

        // チェック
        AssertChecker.assertNotEmpty("InventoryTargetItemList is empty", itemList);

        int result = 0;
        for (InventoryTargetItem item : itemList) {
            result += this.inventoryRepository.updateStockShipping(item.getItemId(), item.getItemCount().getValue());
        }

        if (result != itemList.size()) {
            // 在庫確定失敗
            throw new DomainException("LOGISTIC-RESH0001-E");
        }
    }

    /**
     * 出荷後に在庫を戻す<br/>
     * フェーズ１StockRollBackShipmentUpdateLogicImpl参考
     *
     * @param itemList 対象商品リスト
     */
    public void rollbackInventoryAfterShipmentCompleted(List<InventoryTargetItem> itemList) {

        // チェック
        AssertChecker.assertNotEmpty("InventoryTargetItemList is empty", itemList);

        int input = 0;
        int result = 0;

        for (InventoryTargetItem item : itemList) {

            // 商品情報取得
            GoodsDetailsDto goodsDetailsDto = productAdapter.getGoodsDetailsDto(Integer.parseInt(item.getItemId()));

            // 在庫管理の場合、在庫を戻す
            if (goodsDetailsDto.getStockManagementFlag() == HTypeStockManagementFlag.ON) {
                input++;
                result += this.inventoryRepository.updateStockShippingRollback(item.getItemId(),
                                                                               item.getItemCount().getValue()
                                                                              );
            }
        }

        if (input != result) {
            // 在庫戻し失敗
            throw new DomainException("LOGISTIC-RISC0001-E");
        }
    }

    /**
     * 在庫登録更新結果を商品サービスに反映
     *
     * @param goodsSeqList
     */
    public void asyncAfterProcessGoodsStockDisplay(List<Integer> goodsSeqList) {

        if (CollectionUtils.isEmpty(goodsSeqList)) {
            return;
        }

        try {
            BatchExecuteResponse batchExecuteResponse = productAdapter.syncUpsertGoodsStockDisplay(goodsSeqList);
            if (batchExecuteResponse.getExecuteCode() == null || batchExecuteResponse.getExecuteCode()
                                                                                     .equals(HTypeBatchResult.FAILED.getValue())) {
                throw new DomainException("STOCK-002-");
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw e;
        }
    }

}