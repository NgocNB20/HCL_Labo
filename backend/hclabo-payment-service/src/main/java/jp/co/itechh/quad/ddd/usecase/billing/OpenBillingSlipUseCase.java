/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodGetLogic;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 請求伝票確定ユースケース
 */
@Service
public class OpenBillingSlipUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 販売企画 アダプター */
    private final ISalesAdapter salesAdapter;

    /** 決済方法取得ロジック */
    private final SettlementMethodGetLogic settlementMethodGetLogic;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public OpenBillingSlipUseCase(IBillingSlipRepository billingSlipRepository,
                                  ISalesAdapter salesAdapter,
                                  SettlementMethodGetLogic settlementMethodGetLogic,
                                  ConversionUtility conversionUtility) {
        this.billingSlipRepository = billingSlipRepository;
        this.salesAdapter = salesAdapter;
        this.settlementMethodGetLogic = settlementMethodGetLogic;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 請求伝票を確定する
     *
     * @param transactionId 取引ID
     * @return OpenBillingSlipUseCaseDto
     */
    public OpenBillingSlipUseCaseDto openBillingSlip(String transactionId) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
        // 請求伝票が存在しない、または下書き状態でない場合は処理をスキップする
        if (billingSlipEntity == null || !billingSlipEntity.isDraft()) {
            return null;
        }
        // 注文決済が確定状態でない場合は、確定処理せずに、処理終了
        if (billingSlipEntity.getOrderPaymentEntity().getOrderPaymentStatus() != OrderPaymentStatus.OPEN) {
            return null;
        }

        // 販売伝票を取得する
        SalesSlip salesSlip = this.salesAdapter.getSalesSlip(transactionId);
        // 販売伝票が存在しない場合はエラー
        if (salesSlip == null) {
            throw new DomainException("PAYMENT_EPAU0001-E", new String[] {transactionId});
        }

        // 請求伝票を確定する
        billingSlipEntity.openSlip(salesSlip.getBillingAmount(), billingSlipEntity.getOrderPaymentEntity());

        // 請求伝票を更新する
        billingSlipRepository.update(billingSlipEntity);

        return toOpenBillingSlipUseCaseDto(billingSlipEntity, billingSlipEntity.getOrderPaymentEntity());
    }

    /**
     * 請求伝票のステータスと注文決済の決済方法マスタ情報をユースケース返却用Dtoへ変換
     *
     * @param billingSlipEntity  請求伝票
     * @param orderPaymentEntity 注文決済
     * @return dto ユースケース返却用Dto
     */
    private OpenBillingSlipUseCaseDto toOpenBillingSlipUseCaseDto(BillingSlipEntity billingSlipEntity,
                                                                  OrderPaymentEntity orderPaymentEntity) {

        // 決済方法情報を取得する
        SettlementMethodEntity settlementMethodDbEntity = settlementMethodGetLogic.execute(
                        conversionUtility.toInteger(orderPaymentEntity.getPaymentMethodId()));

        // 決済方法が存在しない場合はエラー
        if (settlementMethodDbEntity == null) {
            throw new DomainException(
                            "PAYMENT_ODPS0001-E", new String[] {orderPaymentEntity.getOrderPaymentId().getValue()});
        }

        OpenBillingSlipUseCaseDto dto = new OpenBillingSlipUseCaseDto();

        dto.setPaidFlag(false);
        dto.setNotificationFlag(settlementMethodDbEntity.getSettlementMailRequired() == HTypeMailRequired.REQUIRED);
        dto.setPreClaimFlag(billingSlipEntity.getBillingType() == HTypeBillType.PRE_CLAIM);

        return dto;
    }

}