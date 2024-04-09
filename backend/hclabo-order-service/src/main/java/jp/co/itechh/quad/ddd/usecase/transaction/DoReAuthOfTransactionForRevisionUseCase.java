/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.transaction.service.OpenTransactionReviseUseCaseExecuter;
import jp.co.itechh.quad.ddd.usecase.transaction.service.StartTransactionReviseUseCaseExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 改訂用取引再オーソリ実行ユースケース
 */
@Service
public class DoReAuthOfTransactionForRevisionUseCase {

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 取引改訂確定ユースケース */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** コンストラクタ */
    @Autowired
    public DoReAuthOfTransactionForRevisionUseCase(ITransactionForRevisionRepository transactionForRevisionRepository,
                                                   IBillingSlipAdapter billingAdapter,
                                                   StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter,
                                                   OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.billingAdapter = billingAdapter;
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
    }

    /**
     * 再オーソリを実行する
     *
     * @param transactionId    取引ID
     * @param administratorSeq 運営者SEQ
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity doReAuth(String transactionId, Integer administratorSeq) {

        // TODO 別UCの呼び出し
        // 改訂用取引を開始
        String targetTransactionRevisionId =
                        startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(transactionId);

        TransactionRevisionId transactionRevisionId = new TransactionRevisionId(targetTransactionRevisionId);

        // 改訂用取引を取得
        TransactionForRevisionEntity transactionForRevisionEntity =
                        transactionForRevisionRepository.get(transactionRevisionId);
        // 改訂用取引が取得できない場合エラー
        if (transactionForRevisionEntity == null) {
            //取引取得失敗
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId.getValue()});
        }

        // 管理メモ設定
        transactionForRevisionEntity.settingReAuthResult();

        // 改訂用取引を更新する
        transactionForRevisionRepository.update(transactionForRevisionEntity);

        // TODO 別UCの呼び出し
        // 改訂用取引を確定
        OrderReceivedEntity orderReceivedEntity = openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(
                        transactionRevisionId.getValue(), administratorSeq, HTypeProcessType.CHANGE, true, true, false);

        // 再オーソリ実行
        billingAdapter.doReAuthBillingSlipForRevision(transactionRevisionId, true);

        return orderReceivedEntity;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcess(OrderReceivedEntity orderReceivedEntity) {

        if (orderReceivedEntity != null) {
            openTransactionReviseUseCaseExecuter.asyncAfterProcessInnerLogic(orderReceivedEntity);
        }
    }
}