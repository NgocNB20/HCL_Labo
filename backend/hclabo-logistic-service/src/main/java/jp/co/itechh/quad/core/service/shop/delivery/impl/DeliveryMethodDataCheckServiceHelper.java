package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送方法データチェックサービス実装クラス Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class DeliveryMethodDataCheckServiceHelper {

    /**
     * 変換Utility
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換Utility
     */
    public DeliveryMethodDataCheckServiceHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 決済方法クラスリストに変換
     *
     * @param paymentMethodListResponse 決済方法一覧レスポンス
     * @return 決済方法クラスリスト
     */
    public List<SettlementMethodEntity> toSettlementMethodEntityList(PaymentMethodListResponse paymentMethodListResponse) {
        List<SettlementMethodEntity> settlementMethodEntityList = new ArrayList<>();

        if (paymentMethodListResponse.getPaymentMethodListResponse() != null) {

            for (PaymentMethodResponse paymentMethodResponse : paymentMethodListResponse.getPaymentMethodListResponse()) {
                SettlementMethodEntity settlementMethodEntity = new SettlementMethodEntity();
                settlementMethodEntity.setSettlementMethodSeq(paymentMethodResponse.getSettlementMethodSeq());
                settlementMethodEntity.setSettlementMethodName(paymentMethodResponse.getSettlementMethodName());
                settlementMethodEntity.setSettlementMethodDisplayNamePC(
                                paymentMethodResponse.getSettlementMethodDisplayNamePC());
                settlementMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                     paymentMethodResponse.getOpenStatusPC()
                                                                                    ));
                settlementMethodEntity.setSettlementNotePC(paymentMethodResponse.getSettlementNotePC());
                settlementMethodEntity.setSettlementMethodType(
                                EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                              paymentMethodResponse.getSettlementMethodType()
                                                             ));
                settlementMethodEntity.setSettlementMethodCommissionType(
                                EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                              paymentMethodResponse.getSettlementMethodCommissionType()
                                                             ));
                settlementMethodEntity.setBillType(EnumTypeUtil.getEnumFromValue(HTypeBillType.class,
                                                                                 paymentMethodResponse.getBillType()
                                                                                ));
                settlementMethodEntity.setDeliveryMethodSeq(paymentMethodResponse.getDeliveryMethodSeq());
                settlementMethodEntity.setEqualsCommission(paymentMethodResponse.getEqualsCommission());
                settlementMethodEntity.setSettlementMethodPriceCommissionFlag(
                                EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class,
                                                              paymentMethodResponse.getSettlementMethodPriceCommissionFlag()
                                                             ));
                settlementMethodEntity.setLargeAmountDiscountPrice(paymentMethodResponse.getLargeAmountDiscountPrice());
                settlementMethodEntity.setLargeAmountDiscountCommission(
                                paymentMethodResponse.getLargeAmountDiscountCommission());
                settlementMethodEntity.setOrderDisplay(paymentMethodResponse.getOrderDisplay());
                settlementMethodEntity.setMaxPurchasedPrice(paymentMethodResponse.getMaxPurchasedPrice());
                settlementMethodEntity.setPaymentTimeLimitDayCount(paymentMethodResponse.getPaymentTimeLimitDayCount());
                settlementMethodEntity.setMinPurchasedPrice(paymentMethodResponse.getMinPurchasedPrice());
                settlementMethodEntity.setCancelTimeLimitDayCount(paymentMethodResponse.getCancelTimeLimitDayCount());
                settlementMethodEntity.setSettlementMailRequired(EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                                               paymentMethodResponse.getSettlementMailRequired()
                                                                                              ));
                settlementMethodEntity.setEnableCardNoHolding(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                            paymentMethodResponse.getEnableCardNoHolding()
                                                                                           ));
                settlementMethodEntity.setEnableSecurityCode(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                           paymentMethodResponse.getEnableSecurityCode()
                                                                                          ));
                settlementMethodEntity.setEnable3dSecure(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                       paymentMethodResponse.getEnable3dSecure()
                                                                                      ));
                settlementMethodEntity.setEnableInstallment(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                          paymentMethodResponse.getEnableInstallment()
                                                                                         ));
                settlementMethodEntity.setEnableBonusSinglePayment(
                                EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                              paymentMethodResponse.getEnableBonusSinglePayment()
                                                             ));
                settlementMethodEntity.setEnableBonusInstallment(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                               paymentMethodResponse.getEnableBonusInstallment()
                                                                                              ));
                settlementMethodEntity.setEnableRevolving(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                        paymentMethodResponse.getEnableRevolving()
                                                                                       ));
                settlementMethodEntity.setRegistTime(
                                conversionUtility.toTimestamp(paymentMethodResponse.getRegistTime()));
                settlementMethodEntity.setUpdateTime(
                                conversionUtility.toTimestamp(paymentMethodResponse.getUpdateTime()));

                settlementMethodEntityList.add(settlementMethodEntity);
            }
        }

        return settlementMethodEntityList;
    }
}