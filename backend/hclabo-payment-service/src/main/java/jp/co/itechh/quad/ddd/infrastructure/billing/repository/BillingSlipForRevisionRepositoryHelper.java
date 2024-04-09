/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.billing.repository;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.IBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PendingBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PostClaimBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PreClaimBillingStatus;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.BillingSlipForRevisionDbEntity;
import org.springframework.stereotype.Component;

/**
 * 改訂用請求伝票リポジトリHelper
 *
 * @author kimura
 */
@Component
public class BillingSlipForRevisionRepositoryHelper {

    /**
     * 改訂用請求伝票DbEntityに変換
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票エンティティ
     * @return 改訂用請求伝票DbEntity
     */
    public BillingSlipForRevisionDbEntity toBillingSlipForRevisionDbEntity(BillingSlipForRevisionEntity billingSlipForRevisionEntity) {

        if (billingSlipForRevisionEntity == null) {
            return null;
        }

        BillingSlipForRevisionDbEntity billingSlipForRevisionDbEntity = new BillingSlipForRevisionDbEntity();

        if (billingSlipForRevisionEntity.getBillingSlipRevisionId() != null) {
            billingSlipForRevisionDbEntity.setBillingSlipRevisionId(
                            billingSlipForRevisionEntity.getBillingSlipRevisionId().getValue());
        }

        billingSlipForRevisionDbEntity.setTransactionRevisionId(
                        billingSlipForRevisionEntity.getTransactionRevisionId());

        if (billingSlipForRevisionEntity.getBillingStatus() != null) {
            billingSlipForRevisionDbEntity.setBillingStatus(billingSlipForRevisionEntity.getBillingStatus().toString());
        }

        if (billingSlipForRevisionEntity.getBillingSlipId() != null) {
            billingSlipForRevisionDbEntity.setBillingSlipId(billingSlipForRevisionEntity.getBillingSlipId().getValue());
        }

        billingSlipForRevisionDbEntity.setBilledPrice(billingSlipForRevisionEntity.getBilledPrice());
        billingSlipForRevisionDbEntity.setBillingType(
                        EnumTypeUtil.getValue(billingSlipForRevisionEntity.getBillingType()));
        billingSlipForRevisionDbEntity.setMoneyReceiptTime(billingSlipForRevisionEntity.getMoneyReceiptTime());
        billingSlipForRevisionDbEntity.setMoneyReceiptAmountTotal(
                        billingSlipForRevisionEntity.getMoneyReceiptAmountTotal());
        billingSlipForRevisionDbEntity.setRegistDate(billingSlipForRevisionEntity.getRegistDate());
        billingSlipForRevisionDbEntity.setTransactionId(billingSlipForRevisionEntity.getTransactionId());

        if (billingSlipForRevisionEntity.getBillingAddressId() != null) {
            billingSlipForRevisionDbEntity.setBillingAddressId(
                            billingSlipForRevisionEntity.getBillingAddressId().getValue());
        }

        return billingSlipForRevisionDbEntity;
    }

    /**
     * 改訂用請求伝票に変換
     *
     * @param billingSlipForRevisionDbEntity 改訂用請求伝票DbEntity
     * @param orderPaymentForRevisionEntity
     * @return 改訂用請求伝票エンティティ
     */
    public BillingSlipForRevisionEntity toBillingSlipForRevisionEntity(BillingSlipForRevisionDbEntity billingSlipForRevisionDbEntity,
                                                                       OrderPaymentForRevisionEntity orderPaymentForRevisionEntity) {

        if (billingSlipForRevisionDbEntity == null) {
            return null;
        }

        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        // 請求ステータス
        IBillingStatus billingStatus = null;
        if (billingSlipForRevisionDbEntity.getBillingType() == null) {
            billingStatus = EnumTypeUtil.getEnum(PendingBillingStatus.class,
                                                 billingSlipForRevisionDbEntity.getBillingStatus()
                                                );
        } else if (EnumTypeUtil.getEnumFromValue(HTypeBillType.class, billingSlipForRevisionDbEntity.getBillingType())
                   == HTypeBillType.PRE_CLAIM) {
            billingStatus = EnumTypeUtil.getEnum(PreClaimBillingStatus.class,
                                                 billingSlipForRevisionDbEntity.getBillingStatus()
                                                );
        } else if (EnumTypeUtil.getEnumFromValue(HTypeBillType.class, billingSlipForRevisionDbEntity.getBillingType())
                   == HTypeBillType.POST_CLAIM) {
            billingStatus = EnumTypeUtil.getEnum(PostClaimBillingStatus.class,
                                                 billingSlipForRevisionDbEntity.getBillingStatus()
                                                );
        }

        return new BillingSlipForRevisionEntity(new BillingSlipId(billingSlipForRevisionDbEntity.getBillingSlipId()),
                                                billingStatus, conversionUtility.toInteger(
                        billingSlipForRevisionDbEntity.getBilledPrice()),
                                                EnumTypeUtil.getEnumFromValue(HTypeBillType.class,
                                                                              billingSlipForRevisionDbEntity.getBillingType()
                                                                             ),
                                                billingSlipForRevisionDbEntity.getRegistDate(),
                                                billingSlipForRevisionDbEntity.getTransactionId(), new BillingAddressId(
                        billingSlipForRevisionDbEntity.getBillingAddressId(), null),
                                                billingSlipForRevisionDbEntity.getMoneyReceiptTime(),
                                                conversionUtility.toInteger(
                                                                billingSlipForRevisionDbEntity.getMoneyReceiptAmountTotal()),
                                                null, new BillingSlipRevisionId(
                        billingSlipForRevisionDbEntity.getBillingSlipRevisionId()),
                                                billingSlipForRevisionDbEntity.getTransactionRevisionId(),
                                                orderPaymentForRevisionEntity
        );
    }

}