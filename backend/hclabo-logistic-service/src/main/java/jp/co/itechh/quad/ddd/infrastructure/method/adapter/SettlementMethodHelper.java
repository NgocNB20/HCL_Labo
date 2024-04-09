package jp.co.itechh.quad.ddd.infrastructure.method.adapter;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 決済方法 アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class SettlementMethodHelper {

    public List<SettlementMethodEntity> toSettlementMethodEntityList(PaymentMethodListResponse paymentMethodListResponse) {

        List<SettlementMethodEntity> settlementMethodEntityList = new ArrayList<>();

        if (paymentMethodListResponse == null) {
            return null;
        }

        List<PaymentMethodResponse> paymentMethodResponseList =
                        paymentMethodListResponse.getPaymentMethodListResponse();

        if (CollectionUtil.isEmpty(paymentMethodResponseList)) {
            return settlementMethodEntityList;
        }

        paymentMethodResponseList.forEach(item -> {
            SettlementMethodEntity settlementMethodEntity = new SettlementMethodEntity();
            settlementMethodEntity.setSettlementMethodSeq(item.getSettlementMethodSeq());
            settlementMethodEntity.setSettlementMethodName(item.getSettlementMethodName());
            settlementMethodEntity.setSettlementMethodDisplayNamePC(item.getSettlementMethodDisplayNamePC());
            settlementMethodEntity.setOpenStatusPC(HTypeOpenDeleteStatus.valueOf(item.getOpenStatusPC()));
            settlementMethodEntity.setSettlementNotePC(item.getSettlementNotePC());
            settlementMethodEntity.setSettlementMethodType(
                            HTypeSettlementMethodType.valueOf(item.getSettlementMethodType()));
            settlementMethodEntity.setSettlementMethodCommissionType(
                            HTypeSettlementMethodCommissionType.valueOf(item.getSettlementMethodCommissionType()));
            settlementMethodEntity.setBillType(HTypeBillType.valueOf(item.getBillType()));
            settlementMethodEntity.setDeliveryMethodSeq(item.getDeliveryMethodSeq());
            settlementMethodEntity.setEqualsCommission(item.getEqualsCommission());
            settlementMethodEntity.setSettlementMethodPriceCommissionFlag(
                            HTypeSettlementMethodPriceCommissionFlag.valueOf(
                                            item.getSettlementMethodPriceCommissionFlag()));
            settlementMethodEntity.setLargeAmountDiscountPrice(item.getLargeAmountDiscountPrice());
            settlementMethodEntity.setLargeAmountDiscountCommission(item.getLargeAmountDiscountCommission());
            settlementMethodEntity.setOrderDisplay(item.getOrderDisplay());
            settlementMethodEntity.setMaxPurchasedPrice(item.getMaxPurchasedPrice());
            settlementMethodEntity.setPaymentTimeLimitDayCount(item.getPaymentTimeLimitDayCount());
            settlementMethodEntity.setMinPurchasedPrice(item.getMinPurchasedPrice());
            settlementMethodEntity.setCancelTimeLimitDayCount(item.getCancelTimeLimitDayCount());
            settlementMethodEntity.setSettlementMailRequired(
                            HTypeMailRequired.valueOf(item.getSettlementMailRequired()));
            settlementMethodEntity.setEnableCardNoHolding(HTypeEffectiveFlag.valueOf(item.getEnableCardNoHolding()));
            settlementMethodEntity.setEnableSecurityCode(HTypeEffectiveFlag.valueOf(item.getEnableSecurityCode()));
            settlementMethodEntity.setEnable3dSecure(HTypeEffectiveFlag.valueOf(item.getEnable3dSecure()));
            settlementMethodEntity.setEnableInstallment(HTypeEffectiveFlag.valueOf(item.getEnableInstallment()));
            settlementMethodEntity.setEnableBonusSinglePayment(
                            HTypeEffectiveFlag.valueOf(item.getEnableBonusSinglePayment()));
            settlementMethodEntity.setEnableBonusInstallment(
                            HTypeEffectiveFlag.valueOf(item.getEnableBonusInstallment()));
            settlementMethodEntity.setEnableRevolving(HTypeEffectiveFlag.valueOf(item.getEnableRevolving()));
            if (item.getRegistTime() != null) {
                settlementMethodEntity.setRegistTime(new Timestamp(item.getRegistTime().getTime()));
            }
            if (item.getUpdateTime() != null) {
                settlementMethodEntity.setUpdateTime(new Timestamp(item.getUpdateTime().getTime()));
            }
            settlementMethodEntityList.add(settlementMethodEntity);
        });
        return settlementMethodEntityList;
    }
}