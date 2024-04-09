/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 改訂用取引に紐づく改訂用請求伝票取得ユースケース
 */
@Service
public class GetBillingSlipForRevisionByTransactionRevisionIdUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public GetBillingSlipForRevisionByTransactionRevisionIdUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
    }

    /**
     * 改訂用取引IDに紐づく改訂用請求伝票を取得する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return GetBillingSlipByTransactionIdUseCaseDto
     */
    public GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto getBillingSlipForRevisionByTransactionRevisionId(
                    String transactionRevisionId) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        this.billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 請求伝票が取得できなかった場合、nullを返却する
        if (billingSlipForRevisionEntity == null) {
            return null;
        }

        // 請求伝票と注文決済を返却する
        return toGetBillingSlipByTransactionIdUseCaseDto(
                        billingSlipForRevisionEntity, billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity());
    }

    /**
     * 請求伝票と注文決済の情報をユースケース返却用Dtoへ変換
     *
     * @param billingSlipForRevisionEntity  請求伝票
     * @param orderPaymentForRevisionEntity 注文決済
     * @return dto ユースケース返却用Dto
     */
    private GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto toGetBillingSlipByTransactionIdUseCaseDto(
                    BillingSlipForRevisionEntity billingSlipForRevisionEntity,
                    OrderPaymentForRevisionEntity orderPaymentForRevisionEntity) {

        GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto dto =
                        new GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto();

        // 改訂専用項目
        dto.setBillingSlipRevisionId(billingSlipForRevisionEntity.getBillingSlipRevisionId());
        dto.setOrderPaymentRevisionId(orderPaymentForRevisionEntity.getOrderPaymentRevisionId());

        // TODO 以降、注文決済と同じ処理（共通化したいな。。一旦TODOコメント付けて先に進む）

        dto.setBillingSlipId(billingSlipForRevisionEntity.getBillingSlipId());
        dto.setBillingAddressId(billingSlipForRevisionEntity.getBillingAddressId());
        dto.setMoneyReceiptTime(billingSlipForRevisionEntity.getMoneyReceiptTime());

        // 注文決済が存在する場合
        if (orderPaymentForRevisionEntity != null) {
            dto.setOrderPaymentId(orderPaymentForRevisionEntity.getOrderPaymentId());
            dto.setPaymentMethodId(orderPaymentForRevisionEntity.getPaymentMethodId());
            dto.setPaymentMethodName(orderPaymentForRevisionEntity.getPaymentMethodName());
            // クレジットカード決済情報
            if (orderPaymentForRevisionEntity.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {
                CreditPayment creditPayment = (CreditPayment) orderPaymentForRevisionEntity.getPaymentRequest();

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

            } else if (orderPaymentForRevisionEntity.getSettlementMethodType()
                       == HTypeSettlementMethodType.LINK_PAYMENT) {
                LinkPayment linkPayment = (LinkPayment) orderPaymentForRevisionEntity.getPaymentRequest();

                dto.setGmoPaymentCancelStatus(linkPayment.getGmoPaymentCancelStatus());
                dto.setPaymethod(linkPayment.getPayMethod());
                dto.setPayType(linkPayment.getPayType());
                dto.setLinkPayType(linkPayment.getLinkPaymentType());
                dto.setPayTypeName(linkPayment.getPayTypeName());
                dto.setCancelLimit(linkPayment.getCancelLimit());
                dto.setLaterDateLimit(linkPayment.getLaterDateLimit());

                // 日付関連Helper取得
                DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
                // 支払猶予期限
                int leftDaysToRemind = PropertiesUtil.getSystemPropertiesValueToInt("linkpay.leftdays.expire");

                // 期限切れ日
                if (linkPayment.getLaterDateLimit() != null) {
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