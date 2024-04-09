/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingAddressId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用配送伝票の配送先更新 ユースケース
 */
@Service
public class UpdateShippingAddressForRevisionUseCase {

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public UpdateShippingAddressForRevisionUseCase(IShippingSlipForRevisionRepository shippingSlipForRevisionRepository) {
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
    }

    /**
     * 改訂用配送伝票の配送先更新
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param shippingAddressId     配送先住所ID
     */
    public void settingShippingAddress(String transactionRevisionId, String shippingAddressId) {

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (shippingSlipForRevisionEntity == null) {
            throw new DomainException("LOGISTIC-SSRE0001-E", new String[] {transactionRevisionId});
        }

        // 配送先を設定する
        shippingSlipForRevisionEntity.updateShippingAddress(new ShippingAddressId(shippingAddressId));

        // 配送伝票を更新する
        shippingSlipForRevisionRepository.update(shippingSlipForRevisionEntity);
    }
}