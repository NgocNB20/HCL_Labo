/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 請求伝票発行ユースケース
 */
@Service
public class PublishBillingSlipUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 注文決済ドメインサービス */
    private final OrderPaymentEntityService orderPaymentService;

    /** コンストラクタ */
    @Autowired
    public PublishBillingSlipUseCase(IBillingSlipRepository billingSlipRepository,
                                     OrderPaymentEntityService orderPaymentService) {
        this.billingSlipRepository = billingSlipRepository;
        this.orderPaymentService = orderPaymentService;
    }

    /**
     * 請求伝票を発行する
     *
     * @param customerId    顧客ID
     * @param addressId     住所ID
     * @param transactionId 取引ID
     * @param orderCode     受注番号
     */
    public void publishBillingSlip(String customerId, String addressId, String transactionId, String orderCode) {

        // 注文決済を作成する
        OrderPaymentEntity orderPaymentEntity = new OrderPaymentEntity(orderCode);
        // 請求伝票を発行する
        BillingSlipEntity billingSlipEntity = new BillingSlipEntity(transactionId, new Date(), orderPaymentEntity);

        // 住所IDが設定されている場合
        if (StringUtils.isNotBlank(addressId)) {
            // 請求先設定
            billingSlipEntity.settingBillingAddress(new BillingAddressId(addressId));
        }

        // デフォルト決済方法を設定
        orderPaymentService.setDefaultPaymentMethod(customerId, billingSlipEntity, orderPaymentEntity);

        // 請求伝票を登録する
        billingSlipRepository.save(billingSlipEntity);
    }

}