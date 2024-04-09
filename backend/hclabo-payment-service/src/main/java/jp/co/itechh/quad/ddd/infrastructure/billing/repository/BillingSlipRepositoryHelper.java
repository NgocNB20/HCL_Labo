package jp.co.itechh.quad.ddd.infrastructure.billing.repository;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.IBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PendingBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PostClaimBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PreClaimBillingStatus;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.BillingSlipDbEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 請求伝票Helperクラス
 */
@Component
public class BillingSlipRepositoryHelper {

    /**
     * 請求伝票DbEntityクラスに変換
     *
     * @param entity 請求伝票エンティティ
     * @return 請求伝票DbEntityクラス
     */
    public BillingSlipDbEntity toBillingSlipDbEntity(BillingSlipEntity entity) {
        BillingSlipDbEntity billingSlipDbEntity = new BillingSlipDbEntity();

        if (entity.getBillingSlipId() != null) {
            billingSlipDbEntity.setBillingSlipId(entity.getBillingSlipId().getValue());
        }
        if (entity.getBillingStatus() != null) {
            billingSlipDbEntity.setBillingStatus(entity.getBillingStatus().toString());
        }
        billingSlipDbEntity.setBilledPrice(entity.getBilledPrice());
        billingSlipDbEntity.setBillingType(EnumTypeUtil.getValue(entity.getBillingType()));
        billingSlipDbEntity.setRegistDate(entity.getRegistDate());
        billingSlipDbEntity.setMoneyReceiptTime(entity.getMoneyReceiptTime());
        billingSlipDbEntity.setMoneyReceiptAmountTotal(entity.getMoneyReceiptAmountTotal());
        billingSlipDbEntity.setTransactionId(entity.getTransactionId());
        if (entity.getBillingAddressId() != null) {
            billingSlipDbEntity.setBillingAddressId(entity.getBillingAddressId().getValue());
        }

        return billingSlipDbEntity;
    }

    /**
     * 請求伝票エンティティに変換
     *
     * @param billingSlipDbEntity 請求伝票DbEntityクラス
     * @param orderPaymentEntity
     * @return 請求伝票エンティティ
     */
    public BillingSlipEntity getBillingSlipEntityFromBillingSlipDbEntity(BillingSlipDbEntity billingSlipDbEntity,
                                                                         OrderPaymentEntity orderPaymentEntity) {

        if (ObjectUtils.isEmpty(billingSlipDbEntity)) {
            return null;
        }

        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        // 請求ステータス
        IBillingStatus billingStatus = null;
        if (billingSlipDbEntity.getBillingType() == null) {
            billingStatus = EnumTypeUtil.getEnum(PendingBillingStatus.class, billingSlipDbEntity.getBillingStatus());
        } else if (EnumTypeUtil.getEnumFromValue(HTypeBillType.class, billingSlipDbEntity.getBillingType())
                   == HTypeBillType.PRE_CLAIM) {
            billingStatus = EnumTypeUtil.getEnum(PreClaimBillingStatus.class, billingSlipDbEntity.getBillingStatus());
        } else if (EnumTypeUtil.getEnumFromValue(HTypeBillType.class, billingSlipDbEntity.getBillingType())
                   == HTypeBillType.POST_CLAIM) {
            billingStatus = EnumTypeUtil.getEnum(PostClaimBillingStatus.class, billingSlipDbEntity.getBillingStatus());
        }

        return new BillingSlipEntity(new BillingSlipId(billingSlipDbEntity.getBillingSlipId()), billingStatus,
                                     conversionUtility.toInteger(billingSlipDbEntity.getBilledPrice()),
                                     EnumTypeUtil.getEnumFromValue(HTypeBillType.class,
                                                                   billingSlipDbEntity.getBillingType()
                                                                  ), billingSlipDbEntity.getRegistDate(),
                                     billingSlipDbEntity.getTransactionId(),
                                     new BillingAddressId(billingSlipDbEntity.getBillingAddressId(), null),
                                     billingSlipDbEntity.getMoneyReceiptTime(),
                                     billingSlipDbEntity.getMoneyReceiptAmountTotal(), orderPaymentEntity
        );
    }

}