/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.inventory.proxy.InventoryProxyService;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.InventoryTargetItem;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.ItemCount;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.shipping.service.OpenShippingSlipForRevisionUseCaseExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 改訂用配送伝票取消ユースケース
 */
@Service
public class CancelShippingSlipForRevisionUseCase {

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** 在庫プロキシサービス */
    private final InventoryProxyService inventoryProxyService;

    /** 改訂用配送伝票確定ユースケース内部ロジック */
    OpenShippingSlipForRevisionUseCaseExecuter openShippingSlipForRevisionUseCaseExecuter;

    /** コンストラクタ */
    @Autowired
    public CancelShippingSlipForRevisionUseCase(IShippingSlipForRevisionRepository shippingSlipForRevisionRepository,
                                                InventoryProxyService inventoryProxyService,
                                                OpenShippingSlipForRevisionUseCaseExecuter openShippingSlipForRevisionUseCaseExecuter) {
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
        this.inventoryProxyService = inventoryProxyService;
        this.openShippingSlipForRevisionUseCaseExecuter = openShippingSlipForRevisionUseCaseExecuter;
    }

    /**
     * 改訂用配送伝票を取消する
     *
     * @param transactionRevisionId
     * @param revisionOpenFlag
     * @return 在庫対象商品リスト
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public List<Integer> cancelShippingSlipForRevision(String transactionRevisionId, boolean revisionOpenFlag) {

        // 戻り値
        List<Integer> stockChangedGoodsSeqList = null;

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        this.shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 配送伝票が存在しない場合は処理をスキップする
        if (shippingSlipForRevisionEntity == null) {
            // 改訂用配送伝票を確定する
            if (revisionOpenFlag) {
                openShippingSlipForRevisionUseCaseExecuter.openShippingSlipForRevisionInnerLogic(
                                transactionRevisionId, true);
            }
            return stockChangedGoodsSeqList;
        }

        // 在庫開放処理
        if (shippingSlipForRevisionEntity.getShippingStatus() == ShippingStatus.SECURED_INVENTORY
            || shippingSlipForRevisionEntity.getShippingStatus() == ShippingStatus.OPEN) {
            //未出荷の場合

            // 出荷前在庫引当の戻し処理を実行
            int result = inventoryProxyService.rollbackReserveInventory(
                            toInventoryTargetItem(shippingSlipForRevisionEntity.getSecuredShippingItemList()));
            if (result != shippingSlipForRevisionEntity.getSecuredShippingItemList().size()) {
                // 在庫戻し失敗
                throw new DomainException("LOGISTIC-SSRS0009-E");
            }

            // 配送商品の在庫更新対象となる商品SEQリスト
            stockChangedGoodsSeqList = shippingSlipForRevisionEntity.getSecuredShippingItemList()
                                                                    .stream()
                                                                    .map(id -> Integer.parseInt(id.getItemId()))
                                                                    .collect(Collectors.toList());

        } else if (shippingSlipForRevisionEntity.getShippingStatus() == ShippingStatus.SHIPMENT_COMPLETED) {
            // 出荷済みの場合

            // 出荷後在庫引当の戻し処理を実行
            inventoryProxyService.rollbackInventoryAfterShipmentCompleted(
                            toInventoryTargetItem(shippingSlipForRevisionEntity.getSecuredShippingItemList()));

            // 配送商品の在庫更新対象となる商品SEQリスト
            stockChangedGoodsSeqList = shippingSlipForRevisionEntity.getSecuredShippingItemList()
                                                                    .stream()
                                                                    .map(id -> Integer.parseInt(id.getItemId()))
                                                                    .collect(Collectors.toList());
        }

        // 改訂用配送伝票を取り消す
        shippingSlipForRevisionEntity.cancelSlip();

        // 改訂用配送伝票を更新する
        shippingSlipForRevisionRepository.update(shippingSlipForRevisionEntity);

        // 改訂用配送伝票を確定する
        if (revisionOpenFlag) {
            openShippingSlipForRevisionUseCaseExecuter.openShippingSlipForRevisionInnerLogic(
                            transactionRevisionId, true);
        }

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
     * @param securedShippingItemList 配送商品リスト
     * @return list 在庫対象商品リスト
     */
    private List<InventoryTargetItem> toInventoryTargetItem(List<SecuredShippingItem> securedShippingItemList) {

        // 引数のリストが存在しない場合はNullで返却
        if (CollectionUtils.isEmpty(securedShippingItemList)) {
            return null;
        }

        List<InventoryTargetItem> list = new ArrayList<>();

        for (int i = 0; i < securedShippingItemList.size(); i++) {
            list.add(new InventoryTargetItem(securedShippingItemList.get(i).getItemId(),
                                             new ItemCount(securedShippingItemList.get(i).getShippingCount().getValue())
            ));
        }

        return list;
    }
}