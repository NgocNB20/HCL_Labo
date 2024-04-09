/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.inventory.proxy.InventoryProxyService;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderItem;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntityService;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingCount;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在庫確保ユースケース
 */
@Service
public class SecureInventoryUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 注文アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送伝票ドメインサービス */
    private final ShippingSlipEntityService shippingSlipEntityService;

    /** 在庫プロキシサービス */
    private final InventoryProxyService inventoryProxyService;

    /** コンストラクタ */
    @Autowired
    public SecureInventoryUseCase(IShippingSlipRepository shippingSlipRepository,
                                  IOrderSlipAdapter orderSlipAdapter,
                                  ShippingSlipEntityService shippingSlipEntityService,
                                  InventoryProxyService inventoryProxyService) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.orderSlipAdapter = orderSlipAdapter;
        this.shippingSlipEntityService = shippingSlipEntityService;
        this.inventoryProxyService = inventoryProxyService;
    }

    /**
     * 配送商品の在庫を確保する
     *
     * @param transactionId 取引ID
     */
    public List<Integer> secureShippingProductInventory(String transactionId) {

        // 戻り値
        List<Integer> stockChangedGoodsSeqList = null;

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = this.shippingSlipRepository.getByTransactionId(transactionId);

        // 配送伝票が存在しない、または下書き状態でない場合は処理をスキップする
        if (shippingSlipEntity == null || shippingSlipEntity.getShippingStatus() != ShippingStatus.DRAFT) {
            return stockChangedGoodsSeqList;
        }

        // 下書き注文票を取得する
        OrderSlip orderSlip = this.orderSlipAdapter.getDraftOrderSlip(transactionId);
        if (orderSlip == null) {
            return stockChangedGoodsSeqList;
        }

        // 配送商品リストを取得する
        List<SecuredShippingItem> shippingItemList = createShippingItemList(orderSlip.getOrderItemList());

        // 在庫確保して配送商品に設定
        shippingSlipEntityService.secureInventoryForShippingItem(shippingItemList, shippingSlipEntity);

        // 配送伝票を更新する
        this.shippingSlipRepository.update(shippingSlipEntity);

        // 在庫登録更新結果を商品サービス側に反映
        if (CollectionUtils.isNotEmpty(shippingSlipEntity.getSecuredShippingItemList())) {
            stockChangedGoodsSeqList = shippingSlipEntity.getSecuredShippingItemList()
                                                         .stream()
                                                         .map(id -> Integer.parseInt(id.getItemId()))
                                                         .collect(Collectors.toList());
            return stockChangedGoodsSeqList;
        }

        return stockChangedGoodsSeqList;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param stockChangedGoodsSeqList
     */
    public void asyncAfterProcessInnerLogic(List<Integer> stockChangedGoodsSeqList) {

        if (!org.springframework.util.CollectionUtils.isEmpty(stockChangedGoodsSeqList)) {
            inventoryProxyService.asyncAfterProcessGoodsStockDisplay(stockChangedGoodsSeqList);
        }
    }

    /**
     * 配送商品リストを取得<br/>
     * 注文商品リストから配送商品リストを作成する
     *
     * @param orderItemList 注文商品リスト
     * @return 配送商品リスト
     */
    private List<SecuredShippingItem> createShippingItemList(List<OrderItem> orderItemList) {

        // 戻り値生成
        List<SecuredShippingItem> securedShippingItemList = new ArrayList<>();

        // 注文商品リストをループして、配送商品リストを作成
        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getOrderCount() != 0) {
                SecuredShippingItem securedShippingItem =
                                new SecuredShippingItem(orderItem.getOrderItemSeq(), orderItem.getItemId(),
                                                        orderItem.getItemName(), orderItem.getUnitTitle1(),
                                                        orderItem.getUnitValue1(), orderItem.getUnitTitle2(),
                                                        orderItem.getUnitValue2(),
                                                        new ShippingCount(orderItem.getOrderCount())
                                );
                securedShippingItemList.add(securedShippingItem);
            }
        }

        // ソート
        securedShippingItemList.sort(Comparator.comparing(SecuredShippingItem::getShippingItemSeq));

        return securedShippingItemList;
    }
}