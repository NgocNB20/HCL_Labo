/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用取引に紐づく改訂用配送伝票取得ユースケース
 */
@Service
public class GetShippingSlipForRevisionByTransactionRevisionIdUseCase {

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public GetShippingSlipForRevisionByTransactionRevisionIdUseCase(IShippingSlipForRevisionRepository shippingSlipForRevisionRepository) {
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
    }

    /**
     * 改訂用取引IDに紐づく改訂用配送伝票を取得する
     *
     * @param transactionRevisionId
     * @return 存在する ... 配送伝票 / 存在しない ... Null
     */
    public ShippingSlipEntity getShippingSlipByTransactionId(String transactionRevisionId) {

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);

        //改訂用 配送伝票を返却する
        return shippingSlipForRevisionEntity;
    }

}