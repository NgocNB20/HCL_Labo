/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.usecase.shipping.service.OpenShippingSlipForRevisionUseCaseExecuter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 改訂用配送伝票確定ユースケース
 */
@Service
public class OpenShippingSlipForRevisionUseCase {

    /** 改訂用配送伝票確定ユースケース内部ロジック */
    OpenShippingSlipForRevisionUseCaseExecuter openShippingSlipForRevisionUseCaseExecuter;

    /** コンストラクタ */
    @Autowired
    public OpenShippingSlipForRevisionUseCase(OpenShippingSlipForRevisionUseCaseExecuter openShippingSlipForRevisionUseCaseExecuter) {
        this.openShippingSlipForRevisionUseCaseExecuter = openShippingSlipForRevisionUseCaseExecuter;
    }

    /**
     * 改訂用配送伝票を確定する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param inventorySkipFlag     在庫調整スキップフラグ（受注修正用に在庫をここで調整する場合はtrue）
     * @return 在庫対象商品リスト
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public List<Integer> openShippingSlipForRevision(String transactionRevisionId, boolean inventorySkipFlag) {

        return openShippingSlipForRevisionUseCaseExecuter.openShippingSlipForRevisionInnerLogic(
                        transactionRevisionId, inventorySkipFlag);
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param stockChangedGoodsSeqList
     */
    public void asyncAfterProcess(List<Integer> stockChangedGoodsSeqList) {

        if (!CollectionUtils.isEmpty(stockChangedGoodsSeqList)) {
            openShippingSlipForRevisionUseCaseExecuter.asyncAfterProcessInnerLogic(stockChangedGoodsSeqList);
        }
    }
}