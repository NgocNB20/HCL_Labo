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
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShipmentStatusConfirmCode;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.shipping.service.OpenShippingSlipForRevisionUseCaseExecuter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 改訂用出荷実績登録ユースケース
 */
@Service
public class RegistShipmentResultForRevisionUseCase {

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** 改訂用配送伝票確定ユースケース内部ロジック */
    OpenShippingSlipForRevisionUseCaseExecuter openShippingSlipForRevisionUseCaseExecuter;

    /** 在庫プロキシサービス */
    private final InventoryProxyService inventoryProxyService;

    /** コンストラクタ */
    @Autowired
    public RegistShipmentResultForRevisionUseCase(IShippingSlipForRevisionRepository shippingSlipForRevisionRepository,
                                                  OpenShippingSlipForRevisionUseCaseExecuter openShippingSlipForRevisionUseCaseExecuter,
                                                  InventoryProxyService inventoryProxyService) {
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
        this.openShippingSlipForRevisionUseCaseExecuter = openShippingSlipForRevisionUseCaseExecuter;
        this.inventoryProxyService = inventoryProxyService;
    }

    /**
     * 改訂用出荷実績を登録する
     *
     * @param transactionRevisionId     改訂用取引ID
     * @param shipmentStatusConfirmCode 配送状況確認番号
     * @param completeShipmentDate      出荷完了日時
     * @param revisionOpenFlag          改訂確定フラグ
     * @return 在庫対象商品リスト
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public List<Integer> registShipmentResult(String transactionRevisionId,
                                              String shipmentStatusConfirmCode,
                                              Date completeShipmentDate,
                                              boolean revisionOpenFlag) {

        // 戻り値
        List<Integer> stockChangedGoodsSeqList = null;

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 配送伝票が取得できない、または配送商品が存在しない場合はエラー
        if (shippingSlipForRevisionEntity == null || CollectionUtils.isEmpty(
                        shippingSlipForRevisionEntity.getSecuredShippingItemList())) {
            throw new DomainException("LOGISTIC-SSRE0002-E");
        }

        // 出荷済みの場合はスキップ
        if (shippingSlipForRevisionEntity.getShippingStatus() == ShippingStatus.SHIPMENT_COMPLETED) {
            // 改訂確定フラグが立っている場合は配送伝票の確定を行う
            if (revisionOpenFlag) {
                openShippingSlipForRevisionUseCaseExecuter.openShippingSlipForRevisionInnerLogic(
                                transactionRevisionId, true);
            }
            return stockChangedGoodsSeqList;
        }

        // 改訂用出荷実績を登録する
        // 出荷実績を登録する（在庫確定）を実行
        inventoryProxyService.registShipmentResult(
                        toInventoryTargetItem(shippingSlipForRevisionEntity.getSecuredShippingItemList()));

        // 配送伝票を出荷完了にする
        shippingSlipForRevisionEntity.completeShipment(
                        new ShipmentStatusConfirmCode(shipmentStatusConfirmCode), completeShipmentDate);

        // 配送伝票を更新する
        shippingSlipForRevisionRepository.update(shippingSlipForRevisionEntity);

        // 在庫登録更新結果を商品サービス側に反映
        if (CollectionUtils.isNotEmpty(shippingSlipForRevisionEntity.getSecuredShippingItemList())) {
            stockChangedGoodsSeqList = shippingSlipForRevisionEntity.getSecuredShippingItemList()
                                                                    .stream()
                                                                    .map(id -> Integer.parseInt(id.getItemId()))
                                                                    .collect(Collectors.toList());
        }

        // 改訂確定フラグが立っている場合は配送伝票の確定を行う
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
        if (org.springframework.util.CollectionUtils.isEmpty(securedShippingItemList)) {
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