/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用請求伝票チェック　ユースケース
 */
@Service
public class CheckBillingSlipForRevisionUseCase {

    /** 改訂用請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 請求伝票ドメインサービス */
    private final BillingSlipForRevisionEntityService billingSlipForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public CheckBillingSlipForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                              IBillingSlipRepository billingSlipRepository,
                                              BillingSlipForRevisionEntityService billingSlipForRevisionEntityService) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.billingSlipRepository = billingSlipRepository;
        this.billingSlipForRevisionEntityService = billingSlipForRevisionEntityService;
    }

    /**
     * 改訂用請求伝票をチェックする
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    public void checkBillingSlipForRevision(String transactionRevisionId) {

        // 改訂用取引IDに紐づく改訂用請求伝票を取得する
        // ※取得できなければ処理スキップ
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (billingSlipForRevisionEntity == null) {
            return;
        }

        // 取引IDで請求伝票を取得する
        // ※取得できなければ処理スキップ
        BillingSlipEntity billingSlipEntity =
                        billingSlipRepository.getByTransactionId(billingSlipForRevisionEntity.getTransactionId());
        if (billingSlipEntity == null) {
            return;
        }

        // 改訂注文決済チェック
        this.billingSlipForRevisionEntityService.checkBillingSlipForRevision(
                        billingSlipForRevisionEntity, billingSlipEntity);
    }
}