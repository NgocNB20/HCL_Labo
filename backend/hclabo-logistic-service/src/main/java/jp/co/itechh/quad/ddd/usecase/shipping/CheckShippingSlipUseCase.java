/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntityService;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配送伝票チェックユースケース
 */
@Service
public class CheckShippingSlipUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 配送伝票ドメインサービス */
    private final ShippingSlipEntityService shippingSlipEntityService;

    /** コンストラクタ */
    @Autowired
    public CheckShippingSlipUseCase(IShippingSlipRepository shippingSlipRepository,
                                    ShippingSlipEntityService shippingSlipEntityService) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.shippingSlipEntityService = shippingSlipEntityService;
    }

    /**
     * 配送伝票をチェックする
     *
     * @param transactionId 取引ID
     */
    public void checkShippingSlip(String transactionId) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = this.shippingSlipRepository.getByTransactionId(transactionId);
        // 配送伝票が存在しない場合は処理をスキップする
        if (shippingSlipEntity == null) {
            return;
        }

        // 配送チェック
        this.shippingSlipEntityService.checkShipping(shippingSlipEntity);
    }

}