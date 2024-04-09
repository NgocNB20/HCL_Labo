/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PostClaimBillingStatus;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlipForRevision;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 改訂用請求伝票決済請求委託ユースケース
 */
@Service
public class EntrustPaymentAgencyForRevisionUseCase {

    /** 改訂用請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 改訂用注文決済サービス */
    private final OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService;

    /** 販売アダプター */
    private final ISalesAdapter saleAdapter;

    /** コンストラクタ */
    @Autowired
    public EntrustPaymentAgencyForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                                  IBillingSlipRepository billingSlipRepository,
                                                  OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService,
                                                  ISalesAdapter saleAdapter) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.billingSlipRepository = billingSlipRepository;
        this.orderPaymentForRevisionEntityService = orderPaymentForRevisionEntityService;
        this.saleAdapter = saleAdapter;
    }

    /**
     * 請求条件の変更を決済代行に連携する<br/>
     *
     * @param administratorId       管理者ID
     * @param transactionRevisionId 改訂用取引ID
     * @return TRUE/金額変更があった場合...FALSE/それ以外
     */
    public boolean entrustPaymentAgencyForRevision(String administratorId, String transactionRevisionId) {

        boolean isGmoExec = false;

        // 改訂用取引IDに紐づく改訂用請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (billingSlipForRevisionEntity == null) {
            throw new DomainException("PAYMENT_EPAR0001-E", new String[] {transactionRevisionId});
        }

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity =
                        billingSlipRepository.getByTransactionId(billingSlipForRevisionEntity.getTransactionId());
        if (billingSlipEntity == null) {
            throw new DomainException(
                            "PAYMENT_EPAR0002-E", new String[] {billingSlipForRevisionEntity.getTransactionId()});
        }

        // 販売企画サービス．改訂用取引にひもづく改訂用販売伝票 を取得する
        SalesSlipForRevision salesSlipForRevision = this.saleAdapter.getSalesSlipForRevision(
                        billingSlipForRevisionEntity.getTransactionRevisionId());
        if (salesSlipForRevision == null) {
            throw new DomainException("PAYMENT_EPAR0003-E",
                                      new String[] {billingSlipForRevisionEntity.getTransactionRevisionId()}
            );
        }
        // 改訂用請求伝票．請求金額設定
        billingSlipForRevisionEntity.settingBilledPrice(salesSlipForRevision.getBillingAmount());

        // リンク決済でない場合は 金額変更依頼
        if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getSettlementMethodType()
            != HTypeSettlementMethodType.LINK_PAYMENT) {
            isGmoExec = orderPaymentForRevisionEntityService.changeOrderPayment(
                            billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity(),
                            billingSlipForRevisionEntity.getBilledPrice(), billingSlipEntity.getBilledPrice()
                                                                               );
            // 金額変更実施された場合
            if (isGmoExec) {
                // 前請求の場合
                if (HTypeBillType.PRE_CLAIM.equals(billingSlipForRevisionEntity.getBillingType())) {
                    billingSlipForRevisionEntity.setMoneyReceipt(
                                    new Date(), billingSlipForRevisionEntity.getBilledPrice());
                }
                // 後請求かつ売上済みの場合
                if (HTypeBillType.POST_CLAIM.equals(billingSlipForRevisionEntity.getBillingType())
                    && PostClaimBillingStatus.SALES.equals(billingSlipForRevisionEntity.getBillingStatus())) {
                    billingSlipForRevisionEntity.setMoneyReceipt(
                                    new Date(), billingSlipForRevisionEntity.getBilledPrice());
                }
            }
        }

        // 改訂用請求伝票を更新
        billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);

        return isGmoExec;
    }
}