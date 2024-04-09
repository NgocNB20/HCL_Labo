/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.pricecalculator.service.PriceCalculationForSalesSlipDto;
import jp.co.itechh.quad.ddd.domain.pricecalculator.service.PriceCalculationServiceForSalesSlipService;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntityService;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 販売伝票計算&チェックユースケース
 */
@Service
public class CalcAndCheckSalesSlipUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 販売伝票用一括金額計算ドメインサービス */
    private final PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip;

    /** 販売伝票エンティティ ドメインサービス */
    private final SalesSlipEntityService salesSlipEntityService;

    /** コンストラクタ */
    @Autowired
    public CalcAndCheckSalesSlipUseCase(ISalesSlipRepository salesSlipRepository,
                                        PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip,
                                        SalesSlipEntityService salesSlipEntityService) {
        this.salesSlipRepository = salesSlipRepository;
        this.priceCalculationServiceForSalesSlip = priceCalculationServiceForSalesSlip;
        this.salesSlipEntityService = salesSlipEntityService;
    }

    /**
     * 販売伝票を計算&チェックする
     *
     * @param transactionId
     * @param contractConfirmFlag 契約確定フラグ
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = DomainException.class,
                   rollbackFor = Exception.class)
    public void calcAndCheckSalesSlip(String transactionId, boolean contractConfirmFlag) {

        // 取引IDに紐づく販売伝票を取得する
        SalesSlipEntity salesSlipEntity = salesSlipRepository.getByTransactionId(transactionId);
        if (salesSlipEntity == null) {
            return;
        }

        // 契約確定フラグがONの場合、料金計算をスキップ
        if (!contractConfirmFlag) {
            // 一括金額計算
            PriceCalculationForSalesSlipDto bundlePriceCalculationDto =
                            priceCalculationServiceForSalesSlip.bundlePriceCalculation(transactionId, salesSlipEntity);

            // 販売伝票に金額を設定する
            salesSlipEntity.settingPrices(bundlePriceCalculationDto);

            // 販売伝票を更新する
            salesSlipRepository.update(salesSlipEntity);
        }

        // 販売チェック
        salesSlipEntityService.checkSalesSlip(salesSlipEntity);

    }
}