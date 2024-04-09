/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.pricecalculator.service.PriceCalculationForSalesSlipDto;
import jp.co.itechh.quad.ddd.domain.pricecalculator.service.PriceCalculationServiceForSalesSlipService;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 販売伝票発行 ユースケース
 */
@Service
public class PublishSalesSlipUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 販売伝票用一括金額計算ドメインサービス */
    private final PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip;

    /**
     * コンストラクタ
     *
     * @param salesSlipRepository
     * @param priceCalculationServiceForSalesSlip
     */
    @Autowired
    public PublishSalesSlipUseCase(ISalesSlipRepository salesSlipRepository,
                                   PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip) {
        this.salesSlipRepository = salesSlipRepository;
        this.priceCalculationServiceForSalesSlip = priceCalculationServiceForSalesSlip;
    }

    /**
     * 販売伝票を発行する
     *
     * @param transactionId
     * @param customerId
     */
    public void publishSalesSlip(String transactionId, String customerId) {

        // 販売伝票を新規発行する
        SalesSlipEntity salesSlipEntity = new SalesSlipEntity(transactionId, customerId, new Date());

        // 一括金額計算
        PriceCalculationForSalesSlipDto bundlePriceCalculationDto =
                        priceCalculationServiceForSalesSlip.bundlePriceCalculation(transactionId, salesSlipEntity);

        // 販売伝票に金額を設定する
        salesSlipEntity.settingPrices(bundlePriceCalculationDto);

        // 販売伝票をリポジトリに反映する
        salesSlipRepository.save(salesSlipEntity);
    }

}