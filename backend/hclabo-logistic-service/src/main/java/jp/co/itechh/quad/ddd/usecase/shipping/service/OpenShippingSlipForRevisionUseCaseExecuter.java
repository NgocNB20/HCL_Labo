/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping.service;

import jp.co.itechh.quad.ddd.domain.inventory.proxy.InventoryProxyService;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 改訂用配送伝票確定ユースケース内部ロジック<br/>
 * ※親トランザクションがある場合の呼び出し用（ユースケースから取引改訂確定ユースケース呼出したい場合に利用）
 */
@Service
public class OpenShippingSlipForRevisionUseCaseExecuter {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** 在庫プロキシサービス */
    private final InventoryProxyService inventoryProxyService;

    /** 改訂用配送伝票ドメインサービス */
    private final ShippingSlipForRevisionEntityService shippingSlipForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public OpenShippingSlipForRevisionUseCaseExecuter(IShippingSlipRepository shippingSlipRepository,
                                                      IShippingSlipForRevisionRepository shippingSlipForRevisionRepository,
                                                      InventoryProxyService inventoryProxyService,
                                                      ShippingSlipForRevisionEntityService shippingSlipForRevisionEntityService) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
        this.inventoryProxyService = inventoryProxyService;
        this.shippingSlipForRevisionEntityService = shippingSlipForRevisionEntityService;
    }

    /**
     * 改訂用配送伝票を確定する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param inventorySkipFlag     在庫調整スキップフラグ（受注修正用に在庫をここで調整する場合はtrue）
     * @return 在庫対象商品リスト
     */
    public List<Integer> openShippingSlipForRevisionInnerLogic(String transactionRevisionId,
                                                               boolean inventorySkipFlag) {

        // 戻り値
        List<Integer> stockChangedGoodsSeqList = null;

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (shippingSlipForRevisionEntity == null) {
            throw new DomainException("LOGISTIC-SSRE0001-E", new String[] {transactionRevisionId});
        }

        // 在庫スキップフラグ=falseの場合は在庫調整を行う
        if (!inventorySkipFlag) {
            // 在庫調整して配送商品を更新
            shippingSlipForRevisionEntityService.adjustInventoryForShippingItem(shippingSlipForRevisionEntity);

            // 改訂用配送伝票を更新する
            shippingSlipForRevisionRepository.update(shippingSlipForRevisionEntity);

            // 在庫登録更新結果を商品サービス側に反映用データ設定
            if (CollectionUtils.isNotEmpty(shippingSlipForRevisionEntity.getSecuredShippingItemList())) {
                stockChangedGoodsSeqList = shippingSlipForRevisionEntity.getSecuredShippingItemList()
                                                                        .stream()
                                                                        .map(id -> Integer.parseInt(id.getItemId()))
                                                                        .collect(Collectors.toList());
            }
        }

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (shippingSlipForRevisionEntity == null) {
            throw new DomainException("LOGISTIC-SSRE0001-E", new String[] {transactionRevisionId});
        }

        // 配送伝票改訂
        ShippingSlipEntity shippingSlipEntity = new ShippingSlipEntity(shippingSlipForRevisionEntity, new Date());

        // 改訂済み配送伝票を登録
        shippingSlipRepository.save(shippingSlipEntity);

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
}