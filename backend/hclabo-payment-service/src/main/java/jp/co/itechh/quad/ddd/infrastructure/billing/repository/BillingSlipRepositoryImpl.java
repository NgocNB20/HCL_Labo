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
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.IPaymentRequest;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import jp.co.itechh.quad.ddd.domain.card.valueobject.CreditExpirationDate;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.BillingSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.OrderPaymentDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.BillingSlipDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.OrderPaymentDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 請求伝票リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class BillingSlipRepositoryImpl implements IBillingSlipRepository {

    /**
     * 請求伝票Daoクラス
     **/
    private final BillingSlipDao billingSlipDao;

    /**
     * 請求伝票リポジトリHelperクラス
     **/
    private final BillingSlipRepositoryHelper billingSlipRepositoryHelper;

    /**
     * 注文決済Daoクラス
     **/
    private final OrderPaymentDao orderPaymentDao;

    /**
     * 注文決済リポジトリHelperクラス
     **/
    private final OrderPaymentRepositoryHelper orderPaymentRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param billingSlipDao               請求伝票Daoクラス
     * @param billingSlipRepositoryHelper  請求伝票リポジトリHelperクラス
     * @param orderPaymentDao
     * @param orderPaymentRepositoryHelper
     */
    @Autowired
    public BillingSlipRepositoryImpl(BillingSlipDao billingSlipDao,
                                     BillingSlipRepositoryHelper billingSlipRepositoryHelper,
                                     OrderPaymentDao orderPaymentDao,
                                     OrderPaymentRepositoryHelper orderPaymentRepositoryHelper) {
        this.billingSlipDao = billingSlipDao;
        this.billingSlipRepositoryHelper = billingSlipRepositoryHelper;
        this.orderPaymentDao = orderPaymentDao;
        this.orderPaymentRepositoryHelper = orderPaymentRepositoryHelper;
    }

    /**
     * 請求伝票登録
     *
     * @param billingSlipEntity 請求伝票
     */
    @Override
    public void save(BillingSlipEntity billingSlipEntity) {

        // 請求伝票登録
        BillingSlipDbEntity billingSlipDbEntity = billingSlipRepositoryHelper.toBillingSlipDbEntity(billingSlipEntity);
        billingSlipDao.insert(billingSlipDbEntity);

        // 注文決済登録
        OrderPaymentDbEntity orderPaymentDbEntity =
                        orderPaymentRepositoryHelper.toOrderPaymentDbEntity(billingSlipEntity.getOrderPaymentEntity(),
                                                                            billingSlipEntity.getBillingSlipId()
                                                                                             .getValue()
                                                                           );
        orderPaymentDao.insert(orderPaymentDbEntity);
    }

    /**
     * 請求伝票更新
     *
     * @param billingSlipEntity 請求伝票
     * @return 更新件数
     */
    @Override
    public int update(BillingSlipEntity billingSlipEntity) {

        // 請求伝票更新
        BillingSlipDbEntity billingSlipDbEntity = billingSlipRepositoryHelper.toBillingSlipDbEntity(billingSlipEntity);
        int res = billingSlipDao.update(billingSlipDbEntity);

        // 注文決済更新
        OrderPaymentDbEntity orderPaymentDbEntity =
                        orderPaymentRepositoryHelper.toOrderPaymentDbEntity(billingSlipEntity.getOrderPaymentEntity(),
                                                                            billingSlipEntity.getBillingSlipId()
                                                                                             .getValue()
                                                                           );
        orderPaymentDao.update(orderPaymentDbEntity);

        return res;
    }

    /**
     * 取引IDで請求伝票取得
     *
     * @param transactionId 取引ID
     * @return BillingSlipEntity 請求伝票
     */
    @Override
    public BillingSlipEntity getByTransactionId(String transactionId) {

        // 請求伝票DBエンティティ取得
        BillingSlipDbEntity billingSlipDbEntity = billingSlipDao.getByTransactionId(transactionId);
        if (ObjectUtils.isEmpty(billingSlipDbEntity)) {
            return null;
        }

        // 注文決済DBエンティティ取得
        OrderPaymentDbEntity orderPaymentDbEntity =
                        orderPaymentDao.getByBillingSlipId(billingSlipDbEntity.getBillingSlipId());

        // nullチェック
        if (orderPaymentDbEntity == null) {
            return null;
        }

        HTypeSettlementMethodType settlementMethodType = EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                                       orderPaymentDbEntity.getSettlementMethodType()
                                                                                      );

        // 決済依頼
        IPaymentRequest paymentRequest = null;
        if (settlementMethodType != null) {
            if (HTypeSettlementMethodType.CREDIT.equals(settlementMethodType)) {
                paymentRequest = new CreditPayment(orderPaymentDbEntity.getPaymentToken(),
                                                   orderPaymentDbEntity.getMaskedCardNo(),
                                                   new CreditExpirationDate(orderPaymentDbEntity.getExpirationYear(),
                                                                            orderPaymentDbEntity.getExpirationMonth(),
                                                                            null
                                                   ), EnumTypeUtil.getEnumFromValue(HTypePaymentType.class,
                                                                                    orderPaymentDbEntity.getPaymentType()
                                                                                   ),
                                                   EnumTypeUtil.getEnumFromValue(HTypeDividedNumber.class,
                                                                                 orderPaymentDbEntity.getDividedNumber()
                                                                                ),
                                                   orderPaymentDbEntity.isEnable3dSecureFlag(),
                                                   orderPaymentDbEntity.isRegistCardFlag(),
                                                   orderPaymentDbEntity.isUseRegistedCardFlag(),
                                                   orderPaymentDbEntity.getAuthLimitDate(),
                                                   orderPaymentDbEntity.isGmoReleaseFlag()
                );
            } else if (HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType)) {
                paymentRequest = new LinkPayment(EnumTypeUtil.getEnumFromValue(HTypeGmoPaymentCancelStatus.class,
                                                                               orderPaymentDbEntity.getGmoPaymentCancelStatus()
                                                                              ), orderPaymentDbEntity.getPayMethod(),
                                                 orderPaymentDbEntity.getPayType(),
                                                 orderPaymentDbEntity.getPayTypeName(),
                                                 EnumTypeUtil.getEnumFromValue(HTypePaymentLinkType.class,
                                                                               orderPaymentDbEntity.getLinkPaymentType()
                                                                              ), orderPaymentDbEntity.getCancelLimit(),
                                                 orderPaymentDbEntity.getLaterDateLimit()
                );
            }
        }
        // 注文決済
        OrderPaymentEntity orderPaymentEntity =
                        new OrderPaymentEntity(new OrderPaymentId(orderPaymentDbEntity.getOrderPaymentId()),
                                               EnumTypeUtil.getEnum(OrderPaymentStatus.class,
                                                                    orderPaymentDbEntity.getOrderPaymentStatus()
                                                                   ), settlementMethodType, paymentRequest,
                                               orderPaymentDbEntity.getPaymentMethodId(),
                                               orderPaymentDbEntity.getPaymentMethodName(),
                                               orderPaymentDbEntity.getOrderCode()
                        );

        return billingSlipRepositoryHelper.getBillingSlipEntityFromBillingSlipDbEntity(
                        billingSlipDbEntity, orderPaymentEntity);
    }

    /**
     * 注文決済登録
     *
     * @param orderPaymentDbEntity 注文決済DbEntityクラス
     */
    @Override
    public void saveOrderPayment(OrderPaymentDbEntity orderPaymentDbEntity) {

        // 注文決済登録
        orderPaymentDao.update(orderPaymentDbEntity);
    }

    /**
     * 不要確定データを削除する
     *
     * @param transactionId 取引ID
     */
    @Override
    public int deleteUnnecessaryByTransactionId(String transactionId) {

        orderPaymentDao.deleteOrderPayment(transactionId);
        int result = billingSlipDao.deleteBillingSlip(transactionId);

        return result;
    }
}