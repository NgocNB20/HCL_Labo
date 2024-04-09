/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.billing.repository;

import jp.co.itechh.quad.core.constant.type.HTypeDividedNumber;
import jp.co.itechh.quad.core.constant.type.HTypeGmoPaymentCancelStatus;
import jp.co.itechh.quad.core.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.core.constant.type.HTypePaymentType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentRevisionId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.IPaymentRequest;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import jp.co.itechh.quad.ddd.domain.card.valueobject.CreditExpirationDate;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.BillingSlipForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.OrderPaymentForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.BillingSlipForRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.OrderPaymentForRevisionDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 改訂用請求伝票リポジトリ
 *
 * @author kimura
 */
@Component
public class BillingSlipForRevisionRepositoryImpl implements IBillingSlipForRevisionRepository {

    /** 改訂用請求伝票Dao */
    private final BillingSlipForRevisionDao billingSlipForRevisionDao;

    /** 改訂用請求伝票リポジトリHelper */
    private final BillingSlipForRevisionRepositoryHelper billingSlipForRevisionRepositoryHelper;

    /** 改訂用注文決済リポジトリHelperクラス */
    private final OrderPaymentForRevisionRepositoryHelper orderPaymentForRevisionRepositoryHelper;

    /** 改訂用注文決済エンティティDaoクラス */
    private final OrderPaymentForRevisionDao orderPaymentForRevisionDao;

    /** コンストラクタ */
    @Autowired
    public BillingSlipForRevisionRepositoryImpl(BillingSlipForRevisionDao billingSlipForRevisionDao,
                                                BillingSlipForRevisionRepositoryHelper billingSlipForRevisionRepositoryHelper,
                                                OrderPaymentForRevisionRepositoryHelper orderPaymentForRevisionRepositoryHelper,
                                                OrderPaymentForRevisionDao orderPaymentForRevisionDao) {
        this.billingSlipForRevisionDao = billingSlipForRevisionDao;
        this.billingSlipForRevisionRepositoryHelper = billingSlipForRevisionRepositoryHelper;
        this.orderPaymentForRevisionRepositoryHelper = orderPaymentForRevisionRepositoryHelper;
        this.orderPaymentForRevisionDao = orderPaymentForRevisionDao;
    }

    /**
     * 改訂用請求伝票登録
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     */
    @Override
    public void save(BillingSlipForRevisionEntity billingSlipForRevisionEntity) {

        // 改訂用請求伝票登録
        BillingSlipForRevisionDbEntity billingSlipForRevisionDbEntity =
                        billingSlipForRevisionRepositoryHelper.toBillingSlipForRevisionDbEntity(
                                        billingSlipForRevisionEntity);
        billingSlipForRevisionDao.insert(billingSlipForRevisionDbEntity);

        // 改訂用注文決済登録
        OrderPaymentForRevisionDbEntity orderPaymentForRevisionDbEntity =
                        orderPaymentForRevisionRepositoryHelper.toOrderPaymentForRevisionDbEntity(
                                        billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity(),
                                        billingSlipForRevisionEntity.getBillingSlipRevisionId().getValue()
                                                                                                 );
        orderPaymentForRevisionDao.insert(orderPaymentForRevisionDbEntity);

    }

    /**
     * 改訂用請求伝票更新
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     * @return 更新件数
     */
    @Override
    public int update(BillingSlipForRevisionEntity billingSlipForRevisionEntity) {

        // 改訂用請求伝票更新
        BillingSlipForRevisionDbEntity billingSlipForRevisionDbEntity =
                        billingSlipForRevisionRepositoryHelper.toBillingSlipForRevisionDbEntity(
                                        billingSlipForRevisionEntity);
        int result = billingSlipForRevisionDao.update(billingSlipForRevisionDbEntity);

        // 改訂用注文決済更新
        OrderPaymentForRevisionDbEntity orderPaymentForRevisionDbEntity =
                        orderPaymentForRevisionRepositoryHelper.toOrderPaymentForRevisionDbEntity(
                                        billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity(),
                                        billingSlipForRevisionEntity.getBillingSlipRevisionId().getValue()
                                                                                                 );
        orderPaymentForRevisionDao.update(orderPaymentForRevisionDbEntity);

        return result;
    }

    /**
     * 改訂用取引IDで改訂用請求伝票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return BillingSlipForRevisionEntity 改訂用請求伝票
     */
    @Override
    public BillingSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId) {

        // 改訂用請求伝票DBエンティティ
        BillingSlipForRevisionDbEntity billingSlipForRevisionDbEntity =
                        billingSlipForRevisionDao.getByTransactionRevisionId(transactionRevisionId);

        // nullチェック
        if (billingSlipForRevisionDbEntity == null) {
            return null;
        }

        // 改訂用注文決済DBエンティティ取得
        OrderPaymentForRevisionDbEntity orderPaymentForRevisionDbEntity =
                        orderPaymentForRevisionDao.getByBillingSlipRevisionId(
                                        billingSlipForRevisionDbEntity.getBillingSlipRevisionId());

        // nullチェック
        if (orderPaymentForRevisionDbEntity == null) {
            return null;
        }

        // 決済種別を取得
        HTypeSettlementMethodType settlementMethodTypeForRevision =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                      orderPaymentForRevisionDbEntity.getSettlementMethodType()
                                                     );

        // 決済依頼を生成
        IPaymentRequest paymentRequestForRevision = null;
        if (settlementMethodTypeForRevision != null) {
            if (HTypeSettlementMethodType.CREDIT.equals(settlementMethodTypeForRevision)) {
                paymentRequestForRevision = new CreditPayment(orderPaymentForRevisionDbEntity.getPaymentToken(),
                                                              orderPaymentForRevisionDbEntity.getMaskedCardNo(),
                                                              new CreditExpirationDate(
                                                                              orderPaymentForRevisionDbEntity.getExpirationYear(),
                                                                              orderPaymentForRevisionDbEntity.getExpirationMonth(),
                                                                              null
                                                              ), EnumTypeUtil.getEnumFromValue(HTypePaymentType.class,
                                                                                               orderPaymentForRevisionDbEntity.getPaymentType()
                                                                                              ),
                                                              EnumTypeUtil.getEnumFromValue(HTypeDividedNumber.class,
                                                                                            orderPaymentForRevisionDbEntity.getDividedNumber()
                                                                                           ),
                                                              orderPaymentForRevisionDbEntity.isEnable3dSecureFlag(),
                                                              orderPaymentForRevisionDbEntity.isRegistCardFlag(),
                                                              orderPaymentForRevisionDbEntity.isUseRegistedCardFlag(),
                                                              orderPaymentForRevisionDbEntity.getAuthLimitDate(),
                                                              orderPaymentForRevisionDbEntity.isGmoReleaseFlag()
                );
            } else if (HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodTypeForRevision)) {
                paymentRequestForRevision =
                                new LinkPayment(EnumTypeUtil.getEnumFromValue(HTypeGmoPaymentCancelStatus.class,
                                                                              orderPaymentForRevisionDbEntity.getGmoPaymentCancelStatus()
                                                                             ),
                                                orderPaymentForRevisionDbEntity.getPayMethod(),
                                                orderPaymentForRevisionDbEntity.getPayType(),
                                                orderPaymentForRevisionDbEntity.getPayTypeName(),
                                                EnumTypeUtil.getEnumFromValue(HTypePaymentLinkType.class,
                                                                              orderPaymentForRevisionDbEntity.getLinkPaymentType()
                                                                             ),
                                                orderPaymentForRevisionDbEntity.getCancelLimit(),
                                                orderPaymentForRevisionDbEntity.getLaterDateLimit()
                                );
            }
        }
        // 注文決済を生成
        OrderPaymentForRevisionEntity orderPaymentForRevisionEntity = new OrderPaymentForRevisionEntity(
                        new OrderPaymentId(orderPaymentForRevisionDbEntity.getOrderPaymentId()),
                        EnumTypeUtil.getEnum(OrderPaymentStatus.class,
                                             orderPaymentForRevisionDbEntity.getOrderPaymentStatus()
                                            ), settlementMethodTypeForRevision, paymentRequestForRevision,
                        orderPaymentForRevisionDbEntity.getPaymentMethodId(),
                        orderPaymentForRevisionDbEntity.getPaymentMethodName(),
                        orderPaymentForRevisionDbEntity.getOrderCode(),
                        new OrderPaymentRevisionId(orderPaymentForRevisionDbEntity.getOrderPaymentRevisionId())
        );

        return billingSlipForRevisionRepositoryHelper.toBillingSlipForRevisionEntity(
                        billingSlipForRevisionDbEntity, orderPaymentForRevisionEntity);
    }
}