/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 取引に紐づく請求伝票取得ユースケース
 */
@Service
public class GetBillingSlipByTransactionIdUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public GetBillingSlipByTransactionIdUseCase(IBillingSlipRepository billingSlipRepository) {
        this.billingSlipRepository = billingSlipRepository;
    }

    /**
     * 取引IDに紐づく請求伝票を取得する
     *
     * @param transactionId 取引ID
     * @return GetBillingSlipByTransactionIdUseCaseDto
     */
    public GetBillingSlipByTransactionIdUseCaseDto getBillingSlipByTransactionId(String transactionId) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = this.billingSlipRepository.getByTransactionId(transactionId);
        // 請求伝票が取得できなかった場合、nullを返却する
        if (billingSlipEntity == null) {
            return null;
        }

        // 請求伝票と注文決済を返却する
        return toGetBillingSlipByTransactionIdUseCaseDto(billingSlipEntity, billingSlipEntity.getOrderPaymentEntity());
    }

    /**
     * 請求伝票と注文決済の情報をユースケース返却用Dtoへ変換
     *
     * @param billingSlipEntitySlipEntity 請求伝票
     * @param orderPaymentEntity          注文決済
     * @return dto ユースケース返却用Dto
     */
    private GetBillingSlipByTransactionIdUseCaseDto toGetBillingSlipByTransactionIdUseCaseDto(BillingSlipEntity billingSlipEntitySlipEntity,
                                                                                              OrderPaymentEntity orderPaymentEntity) {

        GetBillingSlipByTransactionIdUseCaseDto dto = new GetBillingSlipByTransactionIdUseCaseDto();
        dto.setBillingSlipId(billingSlipEntitySlipEntity.getBillingSlipId());
        dto.setBillingAddressId(billingSlipEntitySlipEntity.getBillingAddressId());
        dto.setBillingStatus(billingSlipEntitySlipEntity.getBillingStatus());
        dto.setBilledPrice(billingSlipEntitySlipEntity.getBilledPrice());
        dto.setMoneyReceiptTime(billingSlipEntitySlipEntity.getMoneyReceiptTime());

        // 注文決済が存在する場合
        if (orderPaymentEntity != null) {
            dto.setOrderPaymentId(orderPaymentEntity.getOrderPaymentId());
            dto.setPaymentMethodId(orderPaymentEntity.getPaymentMethodId());
            dto.setPaymentMethodName(orderPaymentEntity.getPaymentMethodName());
            // クレジットカード決済情報
            if (orderPaymentEntity.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {
                CreditPayment creditPayment = (CreditPayment) orderPaymentEntity.getPaymentRequest();

                dto.setPaymentToken(creditPayment.getPaymentToken());
                if (creditPayment.getExpirationDate() != null) {
                    dto.setExpirationMonth(creditPayment.getExpirationDate().getExpirationMonth());
                    dto.setExpirationYear(creditPayment.getExpirationDate().getExpirationYear());
                }
                dto.setPaymentType(EnumTypeUtil.getValue(creditPayment.getPaymentType()));
                dto.setDividedNumber(EnumTypeUtil.getValue(creditPayment.getDividedNumber()));
                dto.setUseRegistedCardFlag(creditPayment.isRegistCardFlag());
                dto.setUseRegistedCardFlag(creditPayment.isUseRegistedCardFlag());
                dto.setCardMaskNo(creditPayment.getMaskedCardNo());
                dto.setAuthLimitDate(creditPayment.getAuthLimitDate());
                dto.setPaymentAgencyReleaseFlag(creditPayment.isGmoReleaseFlag());

            } else if (orderPaymentEntity.getSettlementMethodType() == HTypeSettlementMethodType.LINK_PAYMENT) {

                LinkPayment linkPayment = (LinkPayment) orderPaymentEntity.getPaymentRequest();

                dto.setGmoPaymentCancelStatus(linkPayment.getGmoPaymentCancelStatus());
                dto.setPaymethod(linkPayment.getPayMethod());
                dto.setPayType(linkPayment.getPayType());
                dto.setLinkPayType(linkPayment.getLinkPaymentType());
                dto.setPayTypeName(linkPayment.getPayTypeName());
                dto.setCancelLimit(linkPayment.getCancelLimit());
                dto.setLaterDateLimit(linkPayment.getLaterDateLimit());

                // 期限切れ日
                if (linkPayment.getLaterDateLimit() != null) {
                    // 日付関連Helper取得
                    DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
                    // 支払猶予期限
                    int leftDaysToRemind = PropertiesUtil.getSystemPropertiesValueToInt("linkpay.leftdays.expire");

                    Timestamp cancelExpiredDate = dateUtility.getAmountDayTimestamp(leftDaysToRemind, true,
                                                                                    linkPayment.getLaterDateLimit()
                                                                                   );
                    // 23:59:59を繰り上げる
                    cancelExpiredDate = dateUtility.getAmountTimestamp(1, true, cancelExpiredDate, Calendar.SECOND);
                    dto.setCancelExpiredDate(cancelExpiredDate);
                }
            }
        }

        return dto;
    }

}