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

/**
 * クーポン取消 ユースケース
 */
@Service
public class CancelCouponUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 販売伝票用一括金額計算ドメインサービス */
    private final PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip;

    /** 販売伝票エンティティ ドメインサービス */
    private final SalesSlipEntityService salesSlipEntityService;

    /**
     * コンストラクタ
     *
     * @param salesSlipRepository
     * @param priceCalculationServiceForSalesSlip
     * @param salesSlipEntityService
     */
    @Autowired
    public CancelCouponUseCase(ISalesSlipRepository salesSlipRepository,
                               PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip,
                               SalesSlipEntityService salesSlipEntityService) {
        this.salesSlipRepository = salesSlipRepository;
        this.priceCalculationServiceForSalesSlip = priceCalculationServiceForSalesSlip;
        this.salesSlipEntityService = salesSlipEntityService;
    }

    /**
     * 販売伝票のクーポンを取消する
     *
     * @param transactionId
     */
    public void cancelCoupon(String transactionId) {

        // 取引IDに紐づく販売伝票を取得する
        SalesSlipEntity salesSlipEntity = salesSlipRepository.getByTransactionId(transactionId);
        if (salesSlipEntity == null) {
            throw new DomainException("PRICE-PLANNING-SASE0001-E", new String[] {transactionId});
        }

        /* クーポン取消 */
        salesSlipEntity.cancelCoupon();

        /* 金額再計算 */
        // 一括金額計算
        PriceCalculationForSalesSlipDto bundlePriceCalculationDto =
                        priceCalculationServiceForSalesSlip.bundlePriceCalculation(transactionId, salesSlipEntity);

        // 販売伝票に金額を設定する
        salesSlipEntity.settingPrices(bundlePriceCalculationDto);

        /* 販売チェック */
        salesSlipEntityService.checkSalesSlip(salesSlipEntity);

        // 販売伝票を更新する
        salesSlipRepository.update(salesSlipEntity);
    }
}