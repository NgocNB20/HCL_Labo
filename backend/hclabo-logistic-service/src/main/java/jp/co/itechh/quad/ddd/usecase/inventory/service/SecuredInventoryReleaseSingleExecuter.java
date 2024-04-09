/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.inventory.service;

import jp.co.itechh.quad.ddd.domain.inventory.proxy.InventoryProxyService;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.InventoryTargetItem;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.ItemCount;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 在庫解放1件処理 クラス
 */
@Service
public class SecuredInventoryReleaseSingleExecuter {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SecuredInventoryReleaseSingleExecuter.class);

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 在庫プロキシサービス */
    private final InventoryProxyService inventoryProxyService;

    /** コンストラクタ */
    @Autowired
    public SecuredInventoryReleaseSingleExecuter(IShippingSlipRepository shippingSlipRepository,
                                                 InventoryProxyService inventoryProxyService) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.inventoryProxyService = inventoryProxyService;
    }

    /**
     * 在庫解放処理（1件コミット）
     *
     * @param shippingSlipEntity
     * @return true ... 成功 / true ... 失敗
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean execute(ShippingSlipEntity shippingSlipEntity) throws Exception {

        // 配送商品が存在しない場合 ※本来あり得ない、かつ在庫プロキシサービスで例外発生するが、対象配送伝票の情報をログに出すため、ここでチェック
        if (CollectionUtils.isEmpty(shippingSlipEntity.getSecuredShippingItemList())) {
            LOGGER.error("[" + SecuredInventoryReleaseSingleExecuter.class + "]" + "取引IDが"
                         + shippingSlipEntity.getTransactionId() + "の配送伝票には、配送商品が設定されていないため在庫解放処理を中断し終了します。");
            return false;
        }

        // 配送伝票から配送商品リストを取得する
        List<SecuredShippingItem> shippingItemList = shippingSlipEntity.getSecuredShippingItemList();

        // 対象取引の在庫を開放する
        int result = this.inventoryProxyService.rollbackReserveInventory(toInventoryTargetItem(shippingItemList));

        if (result != shippingItemList.size()) {
            // 在庫戻し失敗
            int errorCnt = shippingItemList.size() - result;
            LOGGER.error("[" + SecuredInventoryReleaseSingleExecuter.class + "]" + "取引IDが"
                         + shippingSlipEntity.getTransactionId() + "の配送伝票で、確保対象数" + shippingItemList.size() + "件のうち"
                         + errorCnt + "件の解放に失敗。処理を中断し終了します。");
            return false;
        }

        // 対象配送伝票を下書きに戻す
        shippingSlipEntity.revertDraft();
        this.shippingSlipRepository.update(shippingSlipEntity);
        return true;
    }

    /**
     * 処理実行後非同期処理 TODO 呼出し元の非同期処理はこのメソッドを使うこと
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