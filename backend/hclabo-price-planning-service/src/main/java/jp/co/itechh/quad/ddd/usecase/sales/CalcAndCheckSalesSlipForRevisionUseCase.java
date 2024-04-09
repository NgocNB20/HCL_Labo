/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.pricecalculator.service.PriceCalculationForSalesSlipDto;
import jp.co.itechh.quad.ddd.domain.pricecalculator.service.PriceCalculationServiceForSalesSlipService;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.salesslip.presentation.api.param.WarningContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 改訂用販売伝票計算&チェックユースケース
 */
@Service
public class CalcAndCheckSalesSlipForRevisionUseCase {

    /** 改訂用販売伝票集約リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 販売伝票用一括金額計算ドメインサービス */
    private final PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip;

    /** 改訂用販売伝票エンティティ ドメインサービス */
    private final SalesSlipForRevisionEntityService salesSlipForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public CalcAndCheckSalesSlipForRevisionUseCase(ISalesSlipForRevisionRepository salesSlipForRevisionRepository,
                                                   ISalesSlipRepository salesSlipRepository,
                                                   PriceCalculationServiceForSalesSlipService priceCalculationServiceForSalesSlip,
                                                   SalesSlipForRevisionEntityService salesSlipForRevisionEntityService) {
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
        this.salesSlipRepository = salesSlipRepository;
        this.priceCalculationServiceForSalesSlip = priceCalculationServiceForSalesSlip;
        this.salesSlipForRevisionEntityService = salesSlipForRevisionEntityService;
    }

    /**
     * 改訂用販売伝票を計算&チェックする
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param contractConfirmFlag   契約確定フラグ
     * @return 警告メッセージマップ
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = DomainException.class,
                   rollbackFor = Exception.class)
    public Map<String, List<WarningContent>> calcAndCheckSalesSlip(String transactionRevisionId,
                                                                   boolean contractConfirmFlag) {

        // 改訂用取引IDに紐づく改訂用販売伝票を取得する
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        salesSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (salesSlipForRevisionEntity == null) {
            return null;
        }

        // 改訂元販売伝票を取得する
        SalesSlipEntity salesSlipEntity =
                        salesSlipRepository.getByTransactionId(salesSlipForRevisionEntity.getTransactionId());
        if (salesSlipEntity == null) {
            return null;
        }

        if (!contractConfirmFlag) {
            /* 金額再計算 */
            // 一括金額計算
            PriceCalculationForSalesSlipDto bundlePriceCalculationDto =
                            priceCalculationServiceForSalesSlip.bundlePriceCalculationForRevision(transactionRevisionId,
                                                                                                  salesSlipForRevisionEntity,
                                                                                                  salesSlipEntity
                                                                                                 );

            // 改訂用販売伝票に金額を設定する
            salesSlipForRevisionEntity.settingPrices(bundlePriceCalculationDto);

            // 改訂用販売伝票を更新する
            salesSlipForRevisionRepository.update(salesSlipForRevisionEntity);
        }

        /* 改訂用販売伝票チェック */
        return salesSlipForRevisionEntityService.checkSalesSlipForRevision(salesSlipForRevisionEntity);
    }

}