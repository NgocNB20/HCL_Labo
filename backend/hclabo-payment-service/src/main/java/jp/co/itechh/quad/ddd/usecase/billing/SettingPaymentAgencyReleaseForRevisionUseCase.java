/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用請求伝票　決済代行連携解除フラグ設定ユースケース
 */
@Service
public class SettingPaymentAgencyReleaseForRevisionUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 改訂用注文決済サービス */
    private final OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public SettingPaymentAgencyReleaseForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                                         OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.orderPaymentForRevisionEntityService = orderPaymentForRevisionEntityService;
    }

    /**
     * 請求先を設定する
     *
     * @param transactionRevisionId    改訂用取引ID
     * @param paymentAgencyReleaseFlag 決済代行連携解除フラグ
     */
    public void settingPaymentAgencyReleaseForRevision(String transactionRevisionId, boolean paymentAgencyReleaseFlag) {

        // 改訂用取引IDに紐づく請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        this.billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 請求伝票が取得できない場合は、不正な呼び出しとみなしエラーリストをセット
        if (billingSlipForRevisionEntity == null) {
            // 請求伝票取得失敗
            throw new DomainException("PAYMENT_EPAR0001-E", new String[] {transactionRevisionId});
        }

        // 連携解除フラグを設定
        this.orderPaymentForRevisionEntityService.settingPaymentAgencyRelease(
                        billingSlipForRevisionEntity, paymentAgencyReleaseFlag);

        // 請求伝票を更新する
        this.billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);
    }
}