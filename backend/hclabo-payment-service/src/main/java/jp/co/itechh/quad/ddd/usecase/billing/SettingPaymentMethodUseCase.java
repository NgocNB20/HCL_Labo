/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntityService;
import jp.co.itechh.quad.ddd.domain.billing.entity.SettingPaymentCreditParam;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 決済方法設定ユースケース
 */
@Service
public class SettingPaymentMethodUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 注文決済ドメインサービス */
    private final OrderPaymentEntityService orderPaymentService;

    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public SettingPaymentMethodUseCase(IBillingSlipRepository billingSlipRepository,
                                       OrderPaymentEntityService orderPaymentService,
                                       ConversionUtility conversionUtility) {
        this.billingSlipRepository = billingSlipRepository;
        this.orderPaymentService = orderPaymentService;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 決済方法を設定する
     *
     * @param transactionId       取引ID
     * @param paymentMethodId     決済方法ID（決済方法SEQ）
     * @param paymentToken        決済トークン
     * @param maskedCardNo        マスク済みカード番号
     * @param expirationMonth     有効期限(月)
     * @param expirationYear      有効期限(年)
     * @param paymentType         支払区分（1：一括, 2：分割, 5：リボ）
     * @param dividedNumber       分割回数
     * @param registCardFlag      カード保存フラグ（保存時true）
     * @param useRegistedCardFlag 登録済カード使用フラグ（登録済みtrue）
     */
    public void settingPaymentMethod(String transactionId,
                                     String paymentMethodId,
                                     String paymentToken,
                                     String maskedCardNo,
                                     String expirationMonth,
                                     String expirationYear,
                                     String paymentType,
                                     String dividedNumber,
                                     boolean registCardFlag,
                                     boolean useRegistedCardFlag) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = this.billingSlipRepository.getByTransactionId(transactionId);
        // 請求伝票が存在しない場合はエラー
        if (billingSlipEntity == null) {
            throw new DomainException("PAYMENT_EPAR0002-E", new String[] {transactionId});
        }

        SettingPaymentCreditParam creditParam = new SettingPaymentCreditParam();
        creditParam.setPaymentToken(paymentToken);
        creditParam.setMaskedCardNo(maskedCardNo);
        creditParam.setExpirationMonth(expirationMonth);
        creditParam.setExpirationYear(expirationYear);
        creditParam.setPaymentType(paymentType);
        creditParam.setDividedNumber(dividedNumber);
        creditParam.setRegistCardFlag(registCardFlag);
        creditParam.setUseRegistedCardFlag(useRegistedCardFlag);

        // 決済方法を設定する
        orderPaymentService.settingPaymentMethod(
                        paymentMethodId, creditParam, billingSlipEntity, billingSlipEntity.getOrderPaymentEntity());

        // 請求伝票を更新する
        billingSlipRepository.update(billingSlipEntity);
    }
}