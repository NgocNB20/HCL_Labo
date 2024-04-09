/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用請求伝票　請求先更新ユースケース
 */
@Service
public class SettingBillingAddressForRevisionUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public SettingBillingAddressForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
    }

    /**
     * 請求先を設定する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param billingAddressId      請求先住所ID
     */
    public void settingBillingAddressForRevision(String transactionRevisionId, String billingAddressId) {
        // 改訂用取引IDに紐づく請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        this.billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 請求伝票が取得できない場合、不正な呼び出しとみなしエラーリストをセット
        if (billingSlipForRevisionEntity == null) {
            // 請求伝票取得失敗
            throw new DomainException("PAYMENT_EPAR0001-E", new String[] {transactionRevisionId});
        }

        // 請求先を設定する
        billingSlipForRevisionEntity.settingBillingAddress(new BillingAddressId(billingAddressId));

        // 請求伝票を更新する
        this.billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);
    }
}