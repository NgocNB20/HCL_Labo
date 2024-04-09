/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 改訂用請求伝票入金ユースケース
 */
@Service
public class DepositedBillingSlipForRevisionUseCase {

    /** 改訂用請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /* マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** コンストラクタ */
    @Autowired
    public DepositedBillingSlipForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                                  MulPayProxyService mulPayProxyService) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.mulPayProxyService = mulPayProxyService;
    }

    /**
     * 改訂用請求伝票を入金済みにする
     *
     * @param transactionRevisionId
     */
    public DepositedBillingSlipForRevisionUseCaseDto depositedBillingSlipForRevision(String transactionRevisionId,
                                                                                     Integer mulPayResultSeq) {

        // 改訂用請求伝票リポジトリー
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 取得結果無し→処理終了（正常終了）
        if (billingSlipForRevisionEntity == null) {
            return null;
        }

        // マルチペイメント決済結果取得
        MulPayResultEntity mulPayResultEntity = mulPayProxyService.getEntityByMulPayResultSeq(mulPayResultSeq);
        if (mulPayResultEntity == null) {
            return null;
        }

        int moneyReceiptAmountTotal = 0;
        // バーチャル口座あおぞら
        if ("36".equals(mulPayResultEntity.getPayType())) {
            moneyReceiptAmountTotal = mulPayResultEntity.getGanbTotalTransferAmount().intValue();
        } else {
            moneyReceiptAmountTotal = mulPayResultEntity.getAmount().intValue();
        }

        // 伝票入金
        billingSlipForRevisionEntity.depositSlip(new Date(), moneyReceiptAmountTotal);

        // 改訂用請求伝票更新
        billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);

        DepositedBillingSlipForRevisionUseCaseDto dto = new DepositedBillingSlipForRevisionUseCaseDto();
        dto.setInsufficientMoneyFlag(billingSlipForRevisionEntity.isInsufficientMoney());
        dto.setOverMoneyFlag(billingSlipForRevisionEntity.isOverMoney());

        return dto;
    }
}