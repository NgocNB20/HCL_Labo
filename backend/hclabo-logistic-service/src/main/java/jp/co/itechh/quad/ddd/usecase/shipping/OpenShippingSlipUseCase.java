/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配送伝票確定ユースケース
 */
@Service
public class OpenShippingSlipUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public OpenShippingSlipUseCase(IShippingSlipRepository shippingSlipRepository) {
        this.shippingSlipRepository = shippingSlipRepository;
    }

    /**
     * 配送伝票を確定する
     *
     * @param transactionId 取引ID
     */
    public void openShippingSlip(String transactionId) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = this.shippingSlipRepository.getByTransactionId(transactionId);
        // 配送伝票が存在しない、または在庫確保状態でない場合は処理をスキップする
        if (shippingSlipEntity == null || shippingSlipEntity.getShippingStatus() != ShippingStatus.SECURED_INVENTORY) {
            return;
        }

        // 配送伝票を確定する
        shippingSlipEntity.openSlip();

        // 商品リストを個数単位で分割し、数量をすべて1にする
        shippingSlipEntity.itemListDivision();

        // 配送伝票を更新する
        shippingSlipRepository.update(shippingSlipEntity);
    }

}
