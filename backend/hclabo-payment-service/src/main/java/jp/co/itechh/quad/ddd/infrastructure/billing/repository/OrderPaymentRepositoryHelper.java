package jp.co.itechh.quad.ddd.infrastructure.billing.repository;

import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.OrderPaymentDbEntity;
import org.springframework.stereotype.Component;

/**
 * 注文決済リポジトリHelperクラス
 */
@Component
public class OrderPaymentRepositoryHelper {

    /**
     * 注文決済DbEntityクラスに変換
     *
     * @param entity        注文決済エンティティ
     * @param billingSlipId
     * @return 注文決済DbEntityクラス
     */
    public OrderPaymentDbEntity toOrderPaymentDbEntity(OrderPaymentEntity entity, String billingSlipId) {

        OrderPaymentDbEntity orderPaymentDbEntity = new OrderPaymentDbEntity();

        orderPaymentDbEntity.setOrderPaymentId(entity.getOrderPaymentId().getValue());
        if (entity.getOrderPaymentStatus() != null) {
            orderPaymentDbEntity.setOrderPaymentStatus(entity.getOrderPaymentStatus().name());
        }
        if (entity.getSettlementMethodType() != null) {
            orderPaymentDbEntity.setSettlementMethodType(EnumTypeUtil.getValue(entity.getSettlementMethodType()));

            if (HTypeSettlementMethodType.CREDIT.equals(entity.getSettlementMethodType())) {
                CreditPayment creditPayment = (CreditPayment) entity.getPaymentRequest();

                orderPaymentDbEntity.setPaymentToken(creditPayment.getPaymentToken());
                orderPaymentDbEntity.setMaskedCardNo(creditPayment.getMaskedCardNo());
                orderPaymentDbEntity.setExpirationMonth(creditPayment.getExpirationDate().getExpirationMonth());
                orderPaymentDbEntity.setExpirationYear(creditPayment.getExpirationDate().getExpirationYear());
                orderPaymentDbEntity.setPaymentType(EnumTypeUtil.getValue(creditPayment.getPaymentType()));
                orderPaymentDbEntity.setDividedNumber(EnumTypeUtil.getValue(creditPayment.getDividedNumber()));
                orderPaymentDbEntity.setEnable3dSecureFlag(creditPayment.isEnable3dSecureFlag());
                orderPaymentDbEntity.setRegistCardFlag(creditPayment.isRegistCardFlag());
                orderPaymentDbEntity.setUseRegistedCardFlag(creditPayment.isUseRegistedCardFlag());
                orderPaymentDbEntity.setAuthLimitDate(creditPayment.getAuthLimitDate());
                orderPaymentDbEntity.setGmoReleaseFlag(creditPayment.isGmoReleaseFlag());

            } else if (HTypeSettlementMethodType.LINK_PAYMENT.equals(entity.getSettlementMethodType())) {
                LinkPayment linkPayment = (LinkPayment) entity.getPaymentRequest();
                if (linkPayment.getGmoPaymentCancelStatus() != null) {
                    orderPaymentDbEntity.setGmoPaymentCancelStatus(linkPayment.getGmoPaymentCancelStatus().getValue());
                }
                orderPaymentDbEntity.setPayMethod(linkPayment.getPayMethod());
                orderPaymentDbEntity.setPayType(linkPayment.getPayType());
                orderPaymentDbEntity.setPayTypeName(linkPayment.getPayTypeName());
                if (linkPayment.getLinkPaymentType() != null) {
                    orderPaymentDbEntity.setLinkPaymentType(linkPayment.getLinkPaymentType().getValue());
                }
                orderPaymentDbEntity.setCancelLimit(linkPayment.getCancelLimit());
                orderPaymentDbEntity.setLaterDateLimit(linkPayment.getLaterDateLimit());
            }
        }

        orderPaymentDbEntity.setPaymentMethodId(entity.getPaymentMethodId());
        orderPaymentDbEntity.setPaymentMethodName(entity.getPaymentMethodName());
        orderPaymentDbEntity.setOrderCode(entity.getOrderCode());
        // RDB親キー項目
        orderPaymentDbEntity.setBillingSlipId(billingSlipId);

        return orderPaymentDbEntity;
    }
}