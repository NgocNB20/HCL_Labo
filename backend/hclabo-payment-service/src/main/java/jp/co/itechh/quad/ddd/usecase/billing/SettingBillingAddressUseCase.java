/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 請求先設定ユースケース
 */
@Service
public class SettingBillingAddressUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public SettingBillingAddressUseCase(IBillingSlipRepository billingSlipRepository) {
        this.billingSlipRepository = billingSlipRepository;
    }

    /**
     * 請求先を設定する
     *
     * @param transactionId    取引ID
     * @param billingAddressId 請求先住所ID
     */
    public void settingBillingAddress(String transactionId, String billingAddressId) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = this.billingSlipRepository.getByTransactionId(transactionId);
        // 請求伝票が取得できない場合、不正な呼び出しとみなしエラーリストをセット
        if (billingSlipEntity == null) {
            // 請求伝票取得失敗
            throw new DomainException("PAYMENT_EPAR0002-E", new String[] {transactionId});
        }

        // 請求先を設定する
        billingSlipEntity.settingBillingAddress(new BillingAddressId(billingAddressId));

        // 請求伝票を更新する
        this.billingSlipRepository.update(billingSlipEntity);
    }
}