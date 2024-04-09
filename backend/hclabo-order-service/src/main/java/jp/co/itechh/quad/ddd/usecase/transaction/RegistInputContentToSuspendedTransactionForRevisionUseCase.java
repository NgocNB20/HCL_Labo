/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 入力内容を改訂用一時停止取引へ反映するユースケース
 */
@Service
public class RegistInputContentToSuspendedTransactionForRevisionUseCase {

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 請求伝票アダプター */
    private final IBillingSlipAdapter billingSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public RegistInputContentToSuspendedTransactionForRevisionUseCase(ITransactionForRevisionRepository transactionForRevisionRepository,
                                                                      IBillingSlipAdapter billingAdapter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.billingSlipAdapter = billingAdapter;
    }

    /**
     * 改訂用一時停止取引に入力内容を反映する
     *
     * @param transactionRevisionId    改訂用取引ID
     * @param adminMemo                管理メモ
     * @param paymentAgencyReleaseFlag 決済代行連携解除フラグ
     */
    public void registInputContentToSuspendedTransactionForRevision(String transactionRevisionId,
                                                                    String adminMemo,
                                                                    boolean paymentAgencyReleaseFlag) {

        // アサートチェック
        AssertChecker.assertNotNull("transactionRevisionId is null", transactionRevisionId);

        // 改訂用請求伝票の決済代行連携解除を設定する
        this.billingSlipAdapter.settingReleasePaymentAgencyBillingSlipForRevision(
                        new TransactionRevisionId(transactionRevisionId), paymentAgencyReleaseFlag);

        // 改訂用取引を取得
        TransactionForRevisionEntity transactionForRevisionEntity =
                        this.transactionForRevisionRepository.get(new TransactionRevisionId(transactionRevisionId));
        // 取引が取得できない場合エラー
        if (transactionForRevisionEntity == null) {
            // 取引取得失敗
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
        }

        // 管理メモ設定
        transactionForRevisionEntity.settingAdminMemo(adminMemo);

        // 改訂用取引を更新する
        this.transactionForRevisionRepository.update(transactionForRevisionEntity);
    }

}