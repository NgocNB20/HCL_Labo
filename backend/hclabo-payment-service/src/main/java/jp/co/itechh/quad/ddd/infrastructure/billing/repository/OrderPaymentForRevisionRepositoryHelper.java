package jp.co.itechh.quad.ddd.infrastructure.billing.repository;

import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.OrderPaymentForRevisionDbEntity;
import org.springframework.stereotype.Component;

/**
 * 改訂用注文決済リポジトリHelperクラス
 */
@Component
public class OrderPaymentForRevisionRepositoryHelper {

    /**
     * 改訂用注文決済エンティティDbEntityクラスに変換
     *
     * @param entity                改訂用注文決済エンティティ
     * @param billingSlipRevisionId
     * @return 改訂用注文決済エンティティDbEntityクラス
     */
    public OrderPaymentForRevisionDbEntity toOrderPaymentForRevisionDbEntity(OrderPaymentForRevisionEntity entity,
                                                                             String billingSlipRevisionId) {
        OrderPaymentForRevisionDbEntity orderPaymentForRevisionDbEntity = new OrderPaymentForRevisionDbEntity();

        orderPaymentForRevisionDbEntity.setOrderPaymentRevisionId(entity.getOrderPaymentRevisionId().getValue());
        orderPaymentForRevisionDbEntity.setOrderPaymentId(entity.getOrderPaymentId().getValue());
        if (entity.getOrderPaymentStatus() != null) {
            orderPaymentForRevisionDbEntity.setOrderPaymentStatus(entity.getOrderPaymentStatus().name());
        }
        if (entity.getSettlementMethodType() != null) {
            orderPaymentForRevisionDbEntity.setSettlementMethodType(
                            EnumTypeUtil.getValue(entity.getSettlementMethodType()));

            if (HTypeSettlementMethodType.CREDIT.equals(entity.getSettlementMethodType())) {
                CreditPayment creditPayment = (CreditPayment) entity.getPaymentRequest();

                orderPaymentForRevisionDbEntity.setPaymentToken(creditPayment.getPaymentToken());
                orderPaymentForRevisionDbEntity.setMaskedCardNo(creditPayment.getMaskedCardNo());
                orderPaymentForRevisionDbEntity.setExpirationMonth(
                                creditPayment.getExpirationDate().getExpirationMonth());
                orderPaymentForRevisionDbEntity.setExpirationYear(
                                creditPayment.getExpirationDate().getExpirationYear());
                orderPaymentForRevisionDbEntity.setPaymentType(EnumTypeUtil.getValue(creditPayment.getPaymentType()));
                orderPaymentForRevisionDbEntity.setDividedNumber(
                                EnumTypeUtil.getValue(creditPayment.getDividedNumber()));
                orderPaymentForRevisionDbEntity.setEnable3dSecureFlag(creditPayment.isEnable3dSecureFlag());
                orderPaymentForRevisionDbEntity.setRegistCardFlag(creditPayment.isRegistCardFlag());
                orderPaymentForRevisionDbEntity.setUseRegistedCardFlag(creditPayment.isUseRegistedCardFlag());
                orderPaymentForRevisionDbEntity.setAuthLimitDate(creditPayment.getAuthLimitDate());
                orderPaymentForRevisionDbEntity.setGmoReleaseFlag(creditPayment.isGmoReleaseFlag());

            } else if (HTypeSettlementMethodType.LINK_PAYMENT.equals(entity.getSettlementMethodType())) {
                LinkPayment linkPayment = (LinkPayment) entity.getPaymentRequest();
                if (linkPayment.getGmoPaymentCancelStatus() != null) {
                    orderPaymentForRevisionDbEntity.setGmoPaymentCancelStatus(
                                    linkPayment.getGmoPaymentCancelStatus().getValue());
                }
                orderPaymentForRevisionDbEntity.setPayMethod(linkPayment.getPayMethod());
                orderPaymentForRevisionDbEntity.setPayType(linkPayment.getPayType());
                orderPaymentForRevisionDbEntity.setPayTypeName(linkPayment.getPayTypeName());
                if (linkPayment.getLinkPaymentType() != null) {
                    orderPaymentForRevisionDbEntity.setLinkPaymentType(linkPayment.getLinkPaymentType().getValue());
                }
                orderPaymentForRevisionDbEntity.setCancelLimit(linkPayment.getCancelLimit());
                orderPaymentForRevisionDbEntity.setLaterDateLimit(linkPayment.getLaterDateLimit());

            }
        }

        orderPaymentForRevisionDbEntity.setPaymentMethodId(entity.getPaymentMethodId());
        orderPaymentForRevisionDbEntity.setPaymentMethodName(entity.getPaymentMethodName());
        orderPaymentForRevisionDbEntity.setOrderCode(entity.getOrderCode());
        // RDB親キー項目
        orderPaymentForRevisionDbEntity.setBillingSlipRevisionId(billingSlipRevisionId);

        return orderPaymentForRevisionDbEntity;
    }
}