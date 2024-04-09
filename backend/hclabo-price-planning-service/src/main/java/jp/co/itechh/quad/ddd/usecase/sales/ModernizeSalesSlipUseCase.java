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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 販売伝票計算&チェックユースケース
 */
@Service
public class ModernizeSalesSlipUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 販売伝票用一括金額計算ドメインサービス */
    private final PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip;

    /** 販売伝票エンティティ ドメインサービス */
    private final SalesSlipEntityService salesSlipEntityService;

    /** コンストラクタ */
    @Autowired
    public ModernizeSalesSlipUseCase(ISalesSlipRepository salesSlipRepository,
                                     PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip,
                                     SalesSlipEntityService salesSlipEntityService) {
        this.salesSlipRepository = salesSlipRepository;
        this.priceCalculationServiceForSalesSlip = priceCalculationServiceForSalesSlip;
        this.salesSlipEntityService = salesSlipEntityService;
    }

    /**
     * 販売伝票を最新化する
     *
     * @param transactionId 取引ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = DomainException.class,
                   rollbackFor = Exception.class)
    public void modernizeSalesSlip(String transactionId) {

        // 取引IDに紐づく販売伝票を取得する
        SalesSlipEntity salesSlipEntity = salesSlipRepository.getByTransactionId(transactionId);
        if (salesSlipEntity == null) {
            return;
        }
        // 販売伝票が存在しない場合は処理をスキップする
        if (ObjectUtils.isEmpty(salesSlipEntity)) {
            return;
        }
        // 適用クーポン または適用クーポンにクーポンコードが存在しない場合は処理をスキップする
        if (ObjectUtils.isEmpty(salesSlipEntity.getApplyCoupon()) || StringUtils.isBlank(
                        salesSlipEntity.getApplyCoupon().getCouponCode())) {
            return;
        }

        // クーポン情報を最新化
        // やっていることメモ：
        //  クーポンコードから最新のクーポンを取得し、販売伝票にセット
        //  クーポン支払い額の計算は、次の一括金額計算ドメインサービスが実施
        salesSlipEntityService.applyCoupon(salesSlipEntity.getApplyCoupon().getCouponCode(), salesSlipEntity);

        /* 金額再計算 */
        // 一括金額計算
        PriceCalculationForSalesSlipDto bundlePriceCalculationDto =
                        priceCalculationServiceForSalesSlip.bundlePriceCalculation(transactionId, salesSlipEntity);

        // 販売伝票に金額を設定する
        salesSlipEntity.settingPrices(bundlePriceCalculationDto);

        // 本UseCaseでは、チェックは行わない（伝票の最新化のみ）

        // 販売伝票を更新
        salesSlipRepository.update(salesSlipEntity);
    }

}
