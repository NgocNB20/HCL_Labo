/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.billing;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipOpenResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipTransactionListResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.CreditResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentLinkResponse;
import jp.co.itechh.quad.ddd.usecase.billing.GetBillingSlipByTransactionIdUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.OpenBillingSlipUseCaseDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 請求伝票エンドポイント Helperクラス
 */
@Component
public class BillingSlipHelper {

    /**
     * 請求伝票エンティティと注文決済エンティティから請求伝票レスポンスに変換
     *
     * @param dto 請求伝票エンティティと注文決済エンティティの情報を格納したDto
     * @return response 請求伝票レスポンス
     */
    public BillingSlipResponse toBillingSlipResponse(GetBillingSlipByTransactionIdUseCaseDto dto) {

        if (dto == null) {
            return null;
        }

        BillingSlipResponse response = new BillingSlipResponse();
        response.setCreditResponse(new CreditResponse());
        response.setBillingSlipId(dto.getBillingSlipId().getValue());
        response.setBillingStatus(dto.getBillingStatus().toString());
        response.setBillingPrice(dto.getBilledPrice());
        response.setBillingAddressId(dto.getBillingAddressId().getValue());
        response.setOrderPaymentId(dto.getOrderPaymentId().getValue());
        response.setPaymentMethodId(dto.getPaymentMethodId());
        response.setPaymentMethodName(dto.getPaymentMethodName());
        response.setMoneyReceiptTime(dto.getMoneyReceiptTime());
        response.getCreditResponse().setPaymentToken(dto.getPaymentToken());
        response.getCreditResponse().setExpirationMonth(dto.getExpirationMonth());
        response.getCreditResponse().setExpirationYear(dto.getExpirationYear());
        response.getCreditResponse().setPaymentType(dto.getPaymentType());
        response.getCreditResponse().setDividedNumber(dto.getDividedNumber());
        response.getCreditResponse().setRegistCardFlag(dto.isRegistCardFlag());
        response.getCreditResponse().setUseRegistedCardFlag(dto.isUseRegistedCardFlag());
        response.getCreditResponse().setCardMaskNo(dto.getCardMaskNo());
        response.getCreditResponse().setAuthLimitDate(dto.getAuthLimitDate());
        response.getCreditResponse().setPaymentAgencyReleaseFlag(dto.isPaymentAgencyReleaseFlag());
        response.setPaymentLinkResponse(new PaymentLinkResponse());
        response.getPaymentLinkResponse().setPaymethod(dto.getPaymethod());
        response.getPaymentLinkResponse().setPayType(dto.getPayType());

        if (dto.getLinkPayType() != null) {
            response.getPaymentLinkResponse().setLinkPayType(dto.getLinkPayType().getValue());
        }

        response.getPaymentLinkResponse().setPayTypeName(dto.getPayTypeName());
        response.getPaymentLinkResponse().setCancelLimit(dto.getCancelLimit());
        response.getPaymentLinkResponse().setLaterDateLimit(dto.getLaterDateLimit());
        response.getPaymentLinkResponse().setCancelExpiredDate(dto.getCancelExpiredDate());

        return response;
    }

    /**
     * 請求伝票確定レスポンスに変換
     *
     * @param dto 請求伝票確定ユースケースDto
     * @return response 請求伝票確定レスポンス
     */
    public BillingSlipOpenResponse toBillingSlipOpenResponse(OpenBillingSlipUseCaseDto dto) {

        if (dto == null) {
            return null;
        }

        BillingSlipOpenResponse response = new BillingSlipOpenResponse();

        response.setNotificationFlag(dto.getNotificationFlag());
        response.setPaidFlag(dto.getPaidFlag());
        response.setPreClaim(dto.getPreClaimFlag());

        return response;
    }

    /**
     * 請求伝票取引リストレスポンスに変換
     *
     * @param transactionIdList 取引IDリスト
     * @return response 請求伝票取引リストレスポンス
     */
    public BillingSlipTransactionListResponse toBillingSlipTransactionListResponse(List<String> transactionIdList) {

        if (CollectionUtils.isEmpty(transactionIdList)) {
            return null;
        }

        BillingSlipTransactionListResponse response = new BillingSlipTransactionListResponse();

        List<String> list = new ArrayList<>();

        for (String transactionId : transactionIdList) {
            list.add(transactionId);
        }

        response.setTransactionIdList(list);

        return response;
    }

    /**
     * 取引に紐づく請求伝票取得レスポンスに変換
     *
     * @param getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto 取引に紐づく請求伝票取得ユースケースDto
     * @return 取引に紐づく請求伝票取得レスポンス
     */
    public BillingSlipForRevisionByTransactionRevisionIdResponse toBillingSlipForRevisionByTransactionRevisionIdResponse(
                    GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto) {

        if (getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto == null) {
            return null;
        }

        BillingSlipForRevisionByTransactionRevisionIdResponse response =
                        new BillingSlipForRevisionByTransactionRevisionIdResponse();

        if (getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getBillingSlipRevisionId() != null) {
            response.setBillingSlipRevisionId(
                            getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getBillingSlipRevisionId()
                                                                                      .getValue());
        }

        if (getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getOrderPaymentRevisionId() != null) {
            response.setOrderPaymentRevisionId(
                            getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getOrderPaymentRevisionId()
                                                                                      .getValue());
        }

        if (getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getBillingSlipId() != null) {
            response.setBillingSlipId(
                            getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getBillingSlipId().getValue());
        }

        if (getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getBillingAddressId() != null) {
            response.setBillingAddressId(
                            getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getBillingAddressId()
                                                                                      .getValue());
        }

        if (getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getOrderPaymentId() != null) {
            response.setOrderPaymentId(
                            getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getOrderPaymentId().getValue());
        }

        response.setPaymentMethodId(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getPaymentMethodId());
        response.setPaymentMethodName(
                        getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getPaymentMethodName());
        response.setPaymentToken(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getPaymentToken());
        response.setExpirationMonth(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getExpirationMonth());
        response.setExpirationYear(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getExpirationYear());
        response.setPaymentType(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getPaymentType());
        response.setDividedNumber(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getDividedNumber());
        response.setRegistCardFlag(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.isRegistCardFlag());
        response.setUseRegistedCardFlag(
                        getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.isUseRegistedCardFlag());
        response.setCardMaskNo(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getCardMaskNo());
        response.setAuthLimitDate(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getAuthLimitDate());
        response.setPaymentAgencyReleaseFlag(
                        getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.isPaymentAgencyReleaseFlag());
        response.setPaymethod(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getPaymethod());
        if (getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getLinkPayType() != null) {
            response.setLinkPayType(
                            getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getLinkPayType().getValue());
        }
        response.setPayTypeName(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getPayTypeName());
        response.setMoneyReceiptTime(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getMoneyReceiptTime());
        response.setMoneyReceiptTime(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getMoneyReceiptTime());
        response.setPayType(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getPayType());
        response.setCancelLimit(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getCancelLimit());
        response.setLaterDateLimit(getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getLaterDateLimit());
        response.setCancelExpiredDate(
                        getBillingSlipForRevisionByTransactionRevisionIdUseCaseDto.getCancelExpiredDate());

        return response;

    }

}