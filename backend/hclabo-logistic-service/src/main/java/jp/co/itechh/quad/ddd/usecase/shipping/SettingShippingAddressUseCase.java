/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingAddressId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配送先設定ユースケース
 */
@Service
public class SettingShippingAddressUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public SettingShippingAddressUseCase(IShippingSlipRepository shippingSlipRepository) {
        this.shippingSlipRepository = shippingSlipRepository;
    }

    /**
     * 配送先を設定する
     *
     * @param transactionId     取引ID
     * @param shippingAddressId 配送先住所ID
     */
    public void settingShippingAddress(String transactionId, String shippingAddressId) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = this.shippingSlipRepository.getByTransactionId(transactionId);

        // 配送伝票が取得できない場合、不正な呼び出しとみなしエラーリストをセット
        if (shippingSlipEntity == null) {
            // 配送伝票取得失敗
            throw new DomainException("LOGISTIC-SHIS0001-E", new String[] {transactionId});
        }

        // 配送先を設定する
        shippingSlipEntity.settingShippingAddress(new ShippingAddressId(shippingAddressId));

        // 配送伝票を更新する
        this.shippingSlipRepository.update(shippingSlipEntity);
    }
}