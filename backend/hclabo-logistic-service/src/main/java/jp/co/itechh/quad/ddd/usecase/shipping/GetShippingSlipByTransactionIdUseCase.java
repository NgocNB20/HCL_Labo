/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 取引に紐づく配送伝票取得ユースケース
 */
@Service
public class GetShippingSlipByTransactionIdUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public GetShippingSlipByTransactionIdUseCase(IShippingSlipRepository shippingSlipRepository) {
        this.shippingSlipRepository = shippingSlipRepository;
    }

    /**
     * 取引IDに紐づく配送伝票を取得する
     *
     * @param transactionId 取引ID
     * @return 存在する ... 配送伝票 / 存在しない ... Null
     */
    public ShippingSlipEntity getShippingSlipByTransactionId(String transactionId) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = this.shippingSlipRepository.getByTransactionId(transactionId);

        // 配送伝票が取得できなかった場合、nullを返却する
        return shippingSlipEntity;
    }
}
