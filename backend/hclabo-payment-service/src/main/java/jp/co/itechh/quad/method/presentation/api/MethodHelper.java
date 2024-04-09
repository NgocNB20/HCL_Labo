/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.method.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;
import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.usecase.method.GetSelectablePaymentMethodListUseCaseDto;
import jp.co.itechh.quad.method.presentation.api.param.PaymentLinkResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodCheckRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodLinkListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodPriceCommissionResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodRegistRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentRegistUpdateModelItemRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethod;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethodListResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 決済方法　Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class MethodHelper {

    /**
     * 決済方法一覧レスポンスに変換
     *
     * @param resultList 決済方法リスト
     * @return 決済方法一覧レスポンス
     */
    public PaymentMethodListResponse toPaymentMethodListResponse(List<SettlementMethodEntity> resultList) {

        PaymentMethodListResponse paymentMethodListResponse = new PaymentMethodListResponse();

        for (SettlementMethodEntity settlementMethod : resultList) {
            PaymentMethodResponse paymentMethodResponse = new PaymentMethodResponse();

            paymentMethodResponse.setSettlementMethodSeq(settlementMethod.getSettlementMethodSeq());
            paymentMethodResponse.setSettlementMethodName(settlementMethod.getSettlementMethodName());
            paymentMethodResponse.setSettlementMethodDisplayNamePC(settlementMethod.getSettlementMethodDisplayNamePC());
            paymentMethodResponse.setOpenStatusPC(EnumTypeUtil.getValue(settlementMethod.getOpenStatusPC()));
            paymentMethodResponse.setSettlementNotePC(settlementMethod.getSettlementNotePC());
            paymentMethodResponse.setSettlementMethodType(
                            EnumTypeUtil.getValue(settlementMethod.getSettlementMethodType()));
            paymentMethodResponse.setSettlementMethodCommissionType(
                            EnumTypeUtil.getValue(settlementMethod.getSettlementMethodCommissionType()));
            paymentMethodResponse.setBillType(EnumTypeUtil.getValue(settlementMethod.getBillType()));
            paymentMethodResponse.setDeliveryMethodSeq(settlementMethod.getDeliveryMethodSeq());
            paymentMethodResponse.setEqualsCommission(settlementMethod.getEqualsCommission());
            paymentMethodResponse.setSettlementMethodPriceCommissionFlag(
                            EnumTypeUtil.getValue(settlementMethod.getSettlementMethodPriceCommissionFlag()));
            paymentMethodResponse.setLargeAmountDiscountPrice(settlementMethod.getLargeAmountDiscountPrice());
            paymentMethodResponse.setLargeAmountDiscountCommission(settlementMethod.getLargeAmountDiscountCommission());
            paymentMethodResponse.setOrderDisplay(settlementMethod.getOrderDisplay());
            paymentMethodResponse.setMaxPurchasedPrice(settlementMethod.getMaxPurchasedPrice());
            paymentMethodResponse.setMinPurchasedPrice(settlementMethod.getMinPurchasedPrice());
            paymentMethodResponse.setSettlementMailRequired(
                            EnumTypeUtil.getValue(settlementMethod.getSettlementMailRequired()));
            paymentMethodResponse.setEnableCardNoHolding(
                            EnumTypeUtil.getValue(settlementMethod.getEnableCardNoHolding()));
            paymentMethodResponse.setEnableSecurityCode(
                            EnumTypeUtil.getValue(settlementMethod.getEnableSecurityCode()));
            paymentMethodResponse.setEnable3dSecure(EnumTypeUtil.getValue(settlementMethod.getEnable3dSecure()));
            paymentMethodResponse.setEnableInstallment(EnumTypeUtil.getValue(settlementMethod.getEnableInstallment()));
            paymentMethodResponse.setEnableBonusSinglePayment(
                            EnumTypeUtil.getValue(settlementMethod.getEnableBonusSinglePayment()));
            paymentMethodResponse.setEnableBonusInstallment(
                            EnumTypeUtil.getValue(settlementMethod.getEnableBonusInstallment()));
            paymentMethodResponse.setEnableRevolving(EnumTypeUtil.getValue(settlementMethod.getEnableRevolving()));
            paymentMethodResponse.setRegistTime(settlementMethod.getRegistTime());
            paymentMethodResponse.setUpdateTime(settlementMethod.getUpdateTime());

            if (paymentMethodListResponse.getPaymentMethodListResponse() == null) {
                paymentMethodListResponse.setPaymentMethodListResponse(new ArrayList<>());
            }
            paymentMethodListResponse.getPaymentMethodListResponse().add(paymentMethodResponse);
        }

        return paymentMethodListResponse;
    }

    /**
     * 決済方法DTO作成<br/>
     *
     * @param paymentMethodCheckRequest 決済方法詳細設定：確認ページ
     * @return 決済方法DTO
     */
    public SettlementMethodDto toSettlementMethodDtoCheck(PaymentMethodCheckRequest paymentMethodCheckRequest) {

        PaymentMethodRequest paymentMethodRequest = paymentMethodCheckRequest.getPaymentMethodRequest();

        changeCommissionFlag(paymentMethodRequest, paymentMethodCheckRequest);

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        SettlementMethodDto settlementMethodDto = ApplicationContextUtility.getBean(SettlementMethodDto.class);

        SettlementMethodEntity settlementMethod = ApplicationContextUtility.getBean(SettlementMethodEntity.class);

        // 決済方法SEQ
        settlementMethod.setSettlementMethodSeq(paymentMethodCheckRequest.getSettlementMethodSeq());
        // 決済方法名
        settlementMethod.setSettlementMethodName(paymentMethodRequest.getSettlementMethodName());
        // 決済方法表示名PC
        settlementMethod.setSettlementMethodDisplayNamePC(paymentMethodRequest.getSettlementMethodDisplayNamePC());
        // 公開状態PC
        HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               paymentMethodRequest.getOpenStatusPC()
                                                                              );
        settlementMethod.setOpenStatusPC(openDeleteStatus);
        // 決済方法説明文PC
        settlementMethod.setSettlementNotePC(paymentMethodRequest.getSettlementNotePC());
        // 決済方法種別
        HTypeSettlementMethodType settlementMethodType = EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                                       paymentMethodRequest.getSettlementMethodType()
                                                                                      );
        settlementMethod.setSettlementMethodType(settlementMethodType);
        // 決済方法手数料種別
        HTypeSettlementMethodCommissionType settlementMethodCommissionType =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                      paymentMethodRequest.getSettlementMethodCommissionType()
                                                     );
        settlementMethod.setSettlementMethodCommissionType(settlementMethodCommissionType);
        // 請求種別
        HTypeBillType billType = EnumTypeUtil.getEnumFromValue(HTypeBillType.class, paymentMethodRequest.getBillType());
        settlementMethod.setBillType(billType);
        // 配送方法SEQ
        settlementMethod.setDeliveryMethodSeq(paymentMethodRequest.getDeliveryMethodSeq());
        // 一律手数料
        settlementMethod.setEqualsCommission(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag commissionFlag =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class,
                                                      paymentMethodRequest.getSettlementMethodPriceCommissionFlag()
                                                     );
        settlementMethod.setSettlementMethodPriceCommissionFlag(commissionFlag);
        // 高額割引下限金額
        settlementMethod.setLargeAmountDiscountPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        settlementMethod.setLargeAmountDiscountCommission(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getLargeAmountDiscountCommission()));
        // 最大購入金額
        settlementMethod.setMaxPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getMaxPurchasedPrice()));
        // 最小購入金額
        settlementMethod.setMinPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        HTypeMailRequired mailRequired = EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                       paymentMethodRequest.getSettlementMailRequired()
                                                                      );
        settlementMethod.setSettlementMailRequired(mailRequired);
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        settlementMethod.setEnableInstallment(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        settlementMethod.setEnableRevolving(HTypeEffectiveFlag.INVALID);
        // 決済方法種別がクレジットの場合は画面の入力値を設定する
        if (HTypeSettlementMethodType.CREDIT.equals(settlementMethod.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnable3dSecure()) {
                settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableCardNoHolding()) {
                settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸ
            if (paymentMethodRequest.getEnableInstallment()) {
                settlementMethod.setEnableInstallment(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableRevolving()) {
                settlementMethod.setEnableRevolving(HTypeEffectiveFlag.VALID);
            }
        }

        settlementMethodDto.setSettlementMethodEntity(settlementMethod);

        HTypeSettlementMethodPriceCommissionFlag settlementMethodPriceCommissionFlag =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class,
                                                      paymentMethodRequest.getSettlementMethodPriceCommissionFlag()
                                                     );
        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != settlementMethodPriceCommissionFlag) {
            return settlementMethodDto;
        }

        // 金額別手数料設定
        List<PaymentRegistUpdateModelItemRequest> priceCommissonItemList =
                        paymentMethodRequest.getPriceCommissionItemList();
        List<SettlementMethodPriceCommissionEntity> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        for (PaymentRegistUpdateModelItemRequest item : priceCommissonItemList) {
            SettlementMethodPriceCommissionEntity priceCommission =
                            ApplicationContextUtility.getBean(SettlementMethodPriceCommissionEntity.class);
            BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
            BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
            if (maxPrice != null && commission != null) {
                // 上限金額
                priceCommission.setMaxPrice(conversionUtility.toBigDecimal(item.getMaxPrice()));
                // 手数料
                priceCommission.setCommission(conversionUtility.toBigDecimal(item.getCommission()));
                priceCommissonList.add(priceCommission);
                if (maxPurchasedPrice.compareTo(priceCommission.getMaxPrice()) < 0) {
                    maxPurchasedPrice = priceCommission.getMaxPrice();
                }
            }
        }
        settlementMethodDto.setSettlementMethodPriceCommissionEntityList(priceCommissonList);
        settlementMethod.setMaxPurchasedPrice(paymentMethodRequest.getMaxPurchasedPrice());

        return settlementMethodDto;
    }

    /**
     * 決済方法DTO作成<br/>
     *
     * @param settlementMethodSeq              決済方法SEQ
     * @param paymentMethodRegistUpdateRequest 決済方法更新リクエスト
     * @return 決済方法DTO
     */
    public SettlementMethodDto toSettlementMethodDtoUpdate(Integer settlementMethodSeq,
                                                           PaymentMethodUpdateRequest paymentMethodRegistUpdateRequest) {

        SettlementMethodDto settlementMethodDto = new SettlementMethodDto();
        PaymentMethodRequest paymentMethodRequest = paymentMethodRegistUpdateRequest.getPaymentMethodRequest();
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag priceCommissionFlag =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class,
                                                      paymentMethodRequest.getSettlementMethodPriceCommissionFlag()
                                                     );

        SettlementMethodEntity settlementMethod = ApplicationContextUtility.getBean(SettlementMethodEntity.class);
        settlementMethod.setShopSeq(1001);
        // 決済方法SEQ
        settlementMethod.setSettlementMethodSeq(settlementMethodSeq);
        // 表示順
        settlementMethod.setOrderDisplay(paymentMethodRequest.getOrderDisplay());

        // 決済方法名
        settlementMethod.setSettlementMethodName(paymentMethodRequest.getSettlementMethodName());
        // 決済方法表示名PC
        settlementMethod.setSettlementMethodDisplayNamePC(paymentMethodRequest.getSettlementMethodDisplayNamePC());
        // 公開状態PC
        HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               paymentMethodRequest.getOpenStatusPC()
                                                                              );
        settlementMethod.setOpenStatusPC(openDeleteStatus);
        // 決済方法説明文PC
        settlementMethod.setSettlementNotePC(paymentMethodRequest.getSettlementNotePC());

        // 決済方法手数料種別
        HTypeSettlementMethodCommissionType settlementMethodCommissionType =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                      paymentMethodRequest.getSettlementMethodCommissionType()
                                                     );
        settlementMethod.setSettlementMethodCommissionType(settlementMethodCommissionType);
        // 一律手数料
        settlementMethod.setEqualsCommission(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        settlementMethod.setSettlementMethodPriceCommissionFlag(priceCommissionFlag);
        // 高額割引下限金額
        settlementMethod.setLargeAmountDiscountPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        settlementMethod.setLargeAmountDiscountCommission(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getLargeAmountDiscountCommission()));
        // 最大購入金額
        settlementMethod.setMaxPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getMaxPurchasedPrice()));
        // 最小購入金額
        settlementMethod.setMinPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        HTypeMailRequired mailRequired = EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                       paymentMethodRequest.getSettlementMailRequired()
                                                                      );
        settlementMethod.setSettlementMailRequired(mailRequired);
        // ｸﾚｼﾞｯﾄｾｷｭﾘﾃｨｺｰﾄﾞ有効化ﾌﾗｸﾞ
        settlementMethod.setEnableSecurityCode(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        settlementMethod.setEnableInstallment(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        settlementMethod.setEnableRevolving(HTypeEffectiveFlag.INVALID);
        if (HTypeSettlementMethodType.CREDIT.getValue().equals(paymentMethodRequest.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableCardNoHolding()) {
                settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnable3dSecure()) {
                settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableInstallment()) {
                settlementMethod.setEnableInstallment(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableRevolving()) {
                settlementMethod.setEnableRevolving(HTypeEffectiveFlag.VALID);
            }
        }
        settlementMethodDto.setSettlementMethodEntity(settlementMethod);
        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != priceCommissionFlag) {
            return settlementMethodDto;
        }

        // 金額別手数料設定
        List<PaymentRegistUpdateModelItemRequest> priceCommissonItemList =
                        paymentMethodRequest.getPriceCommissionItemList();
        List<SettlementMethodPriceCommissionEntity> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        if (priceCommissonItemList != null) {
            for (PaymentRegistUpdateModelItemRequest item : priceCommissonItemList) {
                SettlementMethodPriceCommissionEntity priceCommission =
                                ApplicationContextUtility.getBean(SettlementMethodPriceCommissionEntity.class);
                BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
                BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
                if (maxPrice != null && commission != null) {
                    // 上限金額
                    priceCommission.setMaxPrice(conversionUtility.toBigDecimal(item.getMaxPrice()));
                    // 手数料
                    priceCommission.setCommission(conversionUtility.toBigDecimal(item.getCommission()));
                    priceCommissonList.add(priceCommission);
                    if (maxPurchasedPrice.compareTo(priceCommission.getMaxPrice()) < 0) {
                        maxPurchasedPrice = priceCommission.getMaxPrice();
                    }
                }
            }
        }
        settlementMethodDto.setSettlementMethodEntity(settlementMethod);
        settlementMethodDto.setSettlementMethodPriceCommissionEntityList(priceCommissonList);
        settlementMethod.setMaxPurchasedPrice(maxPurchasedPrice);

        return settlementMethodDto;
    }

    /**
     * 決済方法DTO作成<br/>
     *
     * @param paymentMethodRegistRequest 決済方法登録リクエスト
     * @return 決済方法DTO
     */
    public SettlementMethodDto toSettlementMethodDtoRegist(PaymentMethodRegistRequest paymentMethodRegistRequest) {

        SettlementMethodDto settlementMethodDto = new SettlementMethodDto();
        PaymentMethodRequest paymentMethodRequest = paymentMethodRegistRequest.getPaymentMethodRequest();
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag priceCommissionFlag =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class,
                                                      paymentMethodRequest.getSettlementMethodPriceCommissionFlag()
                                                     );

        SettlementMethodEntity settlementMethod = ApplicationContextUtility.getBean(SettlementMethodEntity.class);
        settlementMethod.setShopSeq(1001);
        // 表示順
        settlementMethod.setOrderDisplay(paymentMethodRequest.getOrderDisplay());

        // 決済方法名
        settlementMethod.setSettlementMethodName(paymentMethodRequest.getSettlementMethodName());
        // 決済方法表示名PC
        settlementMethod.setSettlementMethodDisplayNamePC(paymentMethodRequest.getSettlementMethodDisplayNamePC());
        // 公開状態PC
        HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               paymentMethodRequest.getOpenStatusPC()
                                                                              );
        settlementMethod.setOpenStatusPC(openDeleteStatus);
        // 決済方法説明文PC
        settlementMethod.setSettlementNotePC(paymentMethodRequest.getSettlementNotePC());
        // 決済方法種別
        HTypeSettlementMethodType settlementMethodType = EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                                       paymentMethodRequest.getSettlementMethodType()
                                                                                      );
        settlementMethod.setSettlementMethodType(settlementMethodType);
        // 請求種別
        HTypeBillType billType = EnumTypeUtil.getEnumFromValue(HTypeBillType.class, paymentMethodRequest.getBillType());
        settlementMethod.setBillType(billType);
        // 配送方法SEQ
        settlementMethod.setDeliveryMethodSeq(paymentMethodRequest.getDeliveryMethodSeq());

        // 決済方法手数料種別
        HTypeSettlementMethodCommissionType settlementMethodCommissionType =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                      paymentMethodRequest.getSettlementMethodCommissionType()
                                                     );
        settlementMethod.setSettlementMethodCommissionType(settlementMethodCommissionType);
        // 一律手数料
        settlementMethod.setEqualsCommission(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        settlementMethod.setSettlementMethodPriceCommissionFlag(priceCommissionFlag);
        // 高額割引下限金額
        settlementMethod.setLargeAmountDiscountPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        settlementMethod.setLargeAmountDiscountCommission(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getLargeAmountDiscountCommission()));
        // 最大購入金額
        settlementMethod.setMaxPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getMaxPurchasedPrice()));
        // 最小購入金額
        settlementMethod.setMinPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodRequest.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        HTypeMailRequired mailRequired = EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                       paymentMethodRequest.getSettlementMailRequired()
                                                                      );
        settlementMethod.setSettlementMailRequired(mailRequired);
        // ｸﾚｼﾞｯﾄｾｷｭﾘﾃｨｺｰﾄﾞ有効化ﾌﾗｸﾞ
        settlementMethod.setEnableSecurityCode(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        settlementMethod.setEnableInstallment(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        settlementMethod.setEnableRevolving(HTypeEffectiveFlag.INVALID);
        if (HTypeSettlementMethodType.CREDIT.getValue().equals(paymentMethodRequest.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableCardNoHolding()) {
                settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnable3dSecure()) {
                settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableInstallment()) {
                settlementMethod.setEnableInstallment(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (paymentMethodRequest.getEnableRevolving()) {
                settlementMethod.setEnableRevolving(HTypeEffectiveFlag.VALID);
            }
        }

        settlementMethodDto.setSettlementMethodEntity(settlementMethod);
        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != priceCommissionFlag) {
            return settlementMethodDto;
        }

        // 金額別手数料設定
        List<PaymentRegistUpdateModelItemRequest> priceCommissonItemList =
                        paymentMethodRequest.getPriceCommissionItemList();
        List<SettlementMethodPriceCommissionEntity> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        if (priceCommissonItemList != null) {
            for (PaymentRegistUpdateModelItemRequest item : priceCommissonItemList) {
                SettlementMethodPriceCommissionEntity priceCommission =
                                ApplicationContextUtility.getBean(SettlementMethodPriceCommissionEntity.class);
                BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
                BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
                if (maxPrice != null && commission != null) {
                    // 上限金額
                    priceCommission.setMaxPrice(conversionUtility.toBigDecimal(item.getMaxPrice()));
                    // 手数料
                    priceCommission.setCommission(conversionUtility.toBigDecimal(item.getCommission()));
                    priceCommissonList.add(priceCommission);
                    if (maxPurchasedPrice.compareTo(priceCommission.getMaxPrice()) < 0) {
                        maxPurchasedPrice = priceCommission.getMaxPrice();
                    }
                }
            }
        }
        settlementMethodDto.setSettlementMethodEntity(settlementMethod);
        settlementMethodDto.setSettlementMethodPriceCommissionEntityList(priceCommissonList);
        settlementMethod.setMaxPurchasedPrice(maxPurchasedPrice);

        return settlementMethodDto;
    }

    /**
     * レスポンスに変換<br/>
     *
     * @param settlementMethodDto 決済方法DTO
     * @return 決済方法レスポンス
     */
    public PaymentMethodResponse toPaymentMethodResponse(SettlementMethodDto settlementMethodDto) {

        PaymentMethodResponse paymentMethodResponse = new PaymentMethodResponse();

        if (!ObjectUtils.isEmpty(settlementMethodDto.getSettlementMethodEntity())) {
            SettlementMethodEntity settlementMethodEntity = settlementMethodDto.getSettlementMethodEntity();
            List<PaymentMethodPriceCommissionResponse> paymentMethodPriceCommissionList = new ArrayList<>();

            paymentMethodResponse.setSettlementMethodSeq(settlementMethodEntity.getSettlementMethodSeq());
            paymentMethodResponse.setSettlementMethodName(settlementMethodEntity.getSettlementMethodName());
            paymentMethodResponse.setSettlementMethodDisplayNamePC(
                            settlementMethodEntity.getSettlementMethodDisplayNamePC());
            paymentMethodResponse.setOpenStatusPC(EnumTypeUtil.getValue(settlementMethodEntity.getOpenStatusPC()));
            paymentMethodResponse.setSettlementNotePC(settlementMethodEntity.getSettlementNotePC());
            paymentMethodResponse.setSettlementMethodType(
                            EnumTypeUtil.getValue(settlementMethodEntity.getSettlementMethodType()));
            paymentMethodResponse.setSettlementMethodCommissionType(
                            EnumTypeUtil.getValue(settlementMethodEntity.getSettlementMethodCommissionType()));
            paymentMethodResponse.setBillType(EnumTypeUtil.getValue(settlementMethodEntity.getBillType()));
            paymentMethodResponse.setDeliveryMethodSeq(settlementMethodEntity.getDeliveryMethodSeq());
            paymentMethodResponse.setEqualsCommission(settlementMethodEntity.getEqualsCommission());
            paymentMethodResponse.setSettlementMethodPriceCommissionFlag(
                            EnumTypeUtil.getValue(settlementMethodEntity.getSettlementMethodPriceCommissionFlag()));
            paymentMethodResponse.setLargeAmountDiscountPrice(settlementMethodEntity.getLargeAmountDiscountPrice());
            paymentMethodResponse.setLargeAmountDiscountCommission(
                            settlementMethodEntity.getLargeAmountDiscountCommission());
            paymentMethodResponse.setOrderDisplay(settlementMethodEntity.getOrderDisplay());
            paymentMethodResponse.setMaxPurchasedPrice(settlementMethodEntity.getMaxPurchasedPrice());
            paymentMethodResponse.setMinPurchasedPrice(settlementMethodEntity.getMinPurchasedPrice());
            paymentMethodResponse.setSettlementMailRequired(
                            EnumTypeUtil.getValue(settlementMethodEntity.getSettlementMailRequired()));
            paymentMethodResponse.setEnableCardNoHolding(
                            EnumTypeUtil.getValue(settlementMethodEntity.getEnableCardNoHolding()));
            paymentMethodResponse.setEnableSecurityCode(
                            EnumTypeUtil.getValue(settlementMethodEntity.getEnableSecurityCode()));
            paymentMethodResponse.setEnable3dSecure(EnumTypeUtil.getValue(settlementMethodEntity.getEnable3dSecure()));
            paymentMethodResponse.setEnableInstallment(
                            EnumTypeUtil.getValue(settlementMethodEntity.getEnableInstallment()));
            paymentMethodResponse.setEnableBonusSinglePayment(
                            EnumTypeUtil.getValue(settlementMethodEntity.getEnableBonusSinglePayment()));
            paymentMethodResponse.setEnableBonusInstallment(
                            EnumTypeUtil.getValue(settlementMethodEntity.getEnableBonusInstallment()));
            paymentMethodResponse.setEnableRevolving(
                            EnumTypeUtil.getValue(settlementMethodEntity.getEnableRevolving()));
            paymentMethodResponse.setRegistTime(settlementMethodEntity.getRegistTime());
            paymentMethodResponse.setUpdateTime(settlementMethodEntity.getUpdateTime());

            List<SettlementMethodPriceCommissionEntity> settlementMethodPriceCommissionEntityList =
                            settlementMethodDto.getSettlementMethodPriceCommissionEntityList();
            for (SettlementMethodPriceCommissionEntity settlementMethodPriceCommissionEntity : settlementMethodPriceCommissionEntityList) {
                PaymentMethodPriceCommissionResponse paymentMethodPriceCommissionResponse =
                                new PaymentMethodPriceCommissionResponse();

                paymentMethodPriceCommissionResponse.setCommission(
                                settlementMethodPriceCommissionEntity.getCommission());
                paymentMethodPriceCommissionResponse.setMaxPrice(settlementMethodPriceCommissionEntity.getMaxPrice());
                paymentMethodPriceCommissionResponse.setSettlementMethodSeq(
                                settlementMethodPriceCommissionEntity.getSettlementMethodSeq());
                paymentMethodPriceCommissionResponse.setRegistTime(
                                settlementMethodPriceCommissionEntity.getRegistTime());
                paymentMethodPriceCommissionResponse.setUpdateTime(
                                settlementMethodPriceCommissionEntity.getUpdateTime());

                paymentMethodPriceCommissionList.add(paymentMethodPriceCommissionResponse);
            }
            paymentMethodResponse.setDeliveryMethodName(settlementMethodDto.getDeliveryMethodName());
            paymentMethodResponse.setPaymentMethodPriceCommissionList(paymentMethodPriceCommissionList);
        }

        return paymentMethodResponse;
    }

    /**
     * 決済方法リスト取得<br />
     *
     * @param paymentMethodListUpdateRequest 決済方法一覧更新一覧レスポンス
     * @return 決済方法リスト
     */
    public List<SettlementMethodEntity> toSettlementMethodEntityList(PaymentMethodListUpdateRequest paymentMethodListUpdateRequest) {

        List<SettlementMethodEntity> settlementMethodList = new ArrayList<>();

        // 決済方法アイテムリスト
        List<PaymentUpdateRequest> resultItems = paymentMethodListUpdateRequest.getPaymentMethodListUpdate();

        for (PaymentUpdateRequest resultItem : resultItems) {
            SettlementMethodEntity settlementMethodEntity =
                            ApplicationContextUtility.getBean(SettlementMethodEntity.class);
            settlementMethodEntity.setOrderDisplay(resultItem.getOrderDisplayValue());
            settlementMethodEntity.setSettlementMethodSeq(resultItem.getSettlementMethodSeq());
            settlementMethodList.add(settlementMethodEntity);
        }

        return settlementMethodList;
    }

    /**
     * 決済方法金額別手数料フラグ変更<br/>
     *
     * @param paymentMethodRequest      決済方法リクエスト
     * @param paymentMethodCheckRequest 決済方法チェックリクエスト
     */
    private void changeCommissionFlag(PaymentMethodRequest paymentMethodRequest,
                                      PaymentMethodCheckRequest paymentMethodCheckRequest) {

        if (paymentMethodRequest.getSettlementMethodCommissionType() == null) {
            paymentMethodRequest.setSettlementMethodPriceCommissionFlag(null);
            return;
        }
        paymentMethodCheckRequest.setCommissionType(paymentMethodRequest.getSettlementMethodCommissionType());

        if (HTypeSettlementMethodCommissionType.EACH_AMOUNT_YEN.getValue()
                                                               .equals(paymentMethodRequest.getSettlementMethodCommissionType())) {
            paymentMethodRequest.setSettlementMethodPriceCommissionFlag(
                            HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT.getValue());
            if (paymentMethodRequest.getPriceCommissionItemList() != null) {
                return;
            }
            paymentMethodRequest.setPriceCommissionItemList(makePriceCommissionComponent());
        } else {
            paymentMethodRequest.setSettlementMethodPriceCommissionFlag(
                            HTypeSettlementMethodPriceCommissionFlag.FLAT.getValue());
        }
    }

    /**
     * 決済方法金額別手数料コンポーネント作成<br/>
     *
     * @return 決済方法金額別手数料リスト
     */
    private List<PaymentRegistUpdateModelItemRequest> makePriceCommissionComponent() {

        List<PaymentRegistUpdateModelItemRequest> items = new ArrayList<>();
        for (int count = 0; count < 10; count++) {
            PaymentRegistUpdateModelItemRequest item = new PaymentRegistUpdateModelItemRequest();
            items.add(item);
        }

        return items;
    }

    /** ここからDDD設計範囲 */

    /**
     * 選択可能決済方法一覧から選択可能決済方法一覧レスポンスに変換
     *
     * @param selectAbleList 選択可能決済方法一覧
     * @return response 選択可能決済方法一覧レスポンス
     */
    public SelectablePaymentMethodListResponse toSelectablePaymentMethodListResponse(List<GetSelectablePaymentMethodListUseCaseDto> selectAbleList) {

        if (CollectionUtils.isEmpty(selectAbleList)) {
            return null;
        }

        SelectablePaymentMethodListResponse response = new SelectablePaymentMethodListResponse();

        List<SelectablePaymentMethod> methodList = new ArrayList<>();
        for (int i = 0; i < selectAbleList.size(); i++) {
            SelectablePaymentMethod method = new SelectablePaymentMethod();
            method.setPaymentMethodId(selectAbleList.get(i).getPaymentMethodId());
            method.setPaymentMethodName(selectAbleList.get(i).getPaymentMethodName());
            method.setBillingType(selectAbleList.get(i).getBillingType());
            method.setPaymentMethodNote(selectAbleList.get(i).getPaymentMethodNote());
            method.setSettlementMethodType(selectAbleList.get(i).getSettlementMethodType());
            method.setEnableRevolvingFlag(selectAbleList.get(i).isEnableRevolvingFlag());
            method.setEnableInstallmentFlag(selectAbleList.get(i).isEnableInstallmentFlag());
            method.setInstallmentCounts(selectAbleList.get(i).getInstallmentCounts());
            methodList.add(method);
        }

        response.setSelectablePaymentMethodList(methodList);

        return response;
    }

    /**
     * 決済方法一覧レスポンスに変換
     *
     * @param resultList 決済方法リスト
     * @return 決済方法一覧レスポンス
     */
    public PaymentMethodLinkListResponse toPaymentMethodLinkListResponse(List<SettlementMethodLinkEntity> resultList) {

        PaymentMethodLinkListResponse paymentMethodLinkListResponse = new PaymentMethodLinkListResponse();
        List<PaymentLinkResponse> paymentMethodLinkList = new ArrayList<>();

        for (SettlementMethodLinkEntity entity : resultList) {
            PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();
            paymentLinkResponse.setPaymethod(entity.getPayMethod());
            paymentLinkResponse.setPayTypeName(entity.getPayTypeName());
            paymentLinkResponse.setPayType(entity.getPayType());

            paymentMethodLinkList.add(paymentLinkResponse);
        }

        paymentMethodLinkListResponse.setPaymentMethodLinkList(paymentMethodLinkList);

        return paymentMethodLinkListResponse;
    }

    /** ここまでDDD設計範囲 */

}
