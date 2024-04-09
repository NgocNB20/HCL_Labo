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
 * クーポン適用 ユースケース
 */
@Service
public class ApplyCouponUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 販売伝票用一括金額計算ドメインサービス */
    private final PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlipService;

    /** 販売伝票Entityドメインサービス */
    private final SalesSlipEntityService salesSlipEntityService;

    /**
     * コンストラクタ
     *
     * @param salesSlipRepository 販売伝票集約リポジトリ
     * @param priceCalculationServiceForSalesSlipService 販売伝票用一括金額計算ドメインサービス
     * @param salesSlipEntityService 販売伝票Entityドメインサービス
     */
    @Autowired
    public ApplyCouponUseCase(ISalesSlipRepository salesSlipRepository,
                              PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlipService,
                              SalesSlipEntityService salesSlipEntityService) {
        this.salesSlipRepository = salesSlipRepository;
        this.priceCalculationServiceForSalesSlipService = priceCalculationServiceForSalesSlipService;
        this.salesSlipEntityService = salesSlipEntityService;
    }

    /**
     * 販売伝票にクーポンを適用する
     *
     * @param transactionId 取引ID
     * @param couponCode クーポンコード
     */
    public void applyCoupon(String transactionId, String couponCode) {

        // 取引IDに紐づく販売伝票を取得する
        SalesSlipEntity salesSlipEntity = salesSlipRepository.getByTransactionId(transactionId);
        if (salesSlipEntity == null) {
            throw new DomainException("PRICE-PLANNING-SASE0001-E", new String[] {transactionId});
        }

        /* クーポン適用 */
        // やっていることメモ：
        //  クーポンコードから最新のクーポンを取得し、販売伝票にセット
        //  クーポン支払い額の計算は、次の一括金額計算ドメインサービスが実施
        salesSlipEntityService.applyCoupon(couponCode, salesSlipEntity);

        /* 金額再計算 */
        // 一括金額計算
        PriceCalculationForSalesSlipDto bundlePriceCalculationDto =
                        priceCalculationServiceForSalesSlipService.bundlePriceCalculation(transactionId,
                                                                                          salesSlipEntity
                                                                                         );

        // 販売伝票に金額を設定する
        salesSlipEntity.settingPrices(bundlePriceCalculationDto);

        /* 販売チェック */
        salesSlipEntityService.checkSalesSlip(salesSlipEntity);

        // 販売伝票を更新する
        salesSlipRepository.update(salesSlipEntity);
    }

}
