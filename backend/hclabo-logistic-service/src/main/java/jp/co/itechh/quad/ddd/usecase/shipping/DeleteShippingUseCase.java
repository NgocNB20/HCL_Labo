/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.inventory.proxy.InventoryProxyService;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.InventoryTargetItem;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.ItemCount;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配送伝票削除ユースケース
 */
@Service
public class DeleteShippingUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 在庫プロキシサービス */
    private final InventoryProxyService inventoryProxyService;

    /** コンストラクタ */
    @Autowired
    public DeleteShippingUseCase(IShippingSlipRepository shippingSlipRepository,
                                 InventoryProxyService inventoryProxyService) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.inventoryProxyService = inventoryProxyService;
    }

    /**
     * 配送伝票除する
     *
     * @param transactionId    取引ID
     * @param stockReleaseFlag 在庫開放フラグ
     */
    public List<Integer> deleteUnnecessaryByTransactionId(String transactionId, boolean stockReleaseFlag) {

        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);

        // 戻り値
        List<Integer> stockChangedGoodsSeqList = null;

        // 在庫開放処理
        if (stockReleaseFlag) {
            // 取引IDに紐づく配送伝票を取得する
            ShippingSlipEntity shippingSlipEntity = shippingSlipRepository.getByTransactionId(transactionId);
            if (shippingSlipEntity != null && shippingSlipEntity.getShippingStatus() == ShippingStatus.OPEN) {
                // 配送伝票から配送商品リストを取得する
                List<SecuredShippingItem> shippingItemList = shippingSlipEntity.getSecuredShippingItemList();
                // 対象取引の在庫を開放する
                inventoryProxyService.rollbackReserveInventory(toInventoryTargetItem(shippingItemList));

                stockChangedGoodsSeqList = shippingSlipEntity.getSecuredShippingItemList()
                                                             .stream()
                                                             .map(id -> Integer.parseInt(id.getItemId()))
                                                             .collect(Collectors.toList());
            }
        }

        // 不要配送伝票削除
        shippingSlipRepository.deleteUnnecessaryByTransactionId(transactionId);

        return stockChangedGoodsSeqList;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param stockChangedGoodsSeqList
     */
    public void asyncAfterProcess(List<Integer> stockChangedGoodsSeqList) {

        if (!CollectionUtils.isEmpty(stockChangedGoodsSeqList)) {
            inventoryProxyService.asyncAfterProcessGoodsStockDisplay(stockChangedGoodsSeqList);
        }
    }

    /**
     * 配送商品リストから在庫対象商品リストへ変換
     *
     * @param shippingItemList 配送商品リスト
     * @return list 在庫対象商品リスト
     */
    private List<InventoryTargetItem> toInventoryTargetItem(List<SecuredShippingItem> shippingItemList) {

        // 引数のリストが存在しない場合はNullで返却
        if (CollectionUtils.isEmpty(shippingItemList)) {
            return null;
        }

        List<InventoryTargetItem> list = new ArrayList<>();

        for (int i = 0; i < shippingItemList.size(); i++) {
            list.add(new InventoryTargetItem(
                            shippingItemList.get(i).getItemId(),
                            new ItemCount(shippingItemList.get(i).getShippingCount().getValue())
            ));
        }

        return list;
    }
}