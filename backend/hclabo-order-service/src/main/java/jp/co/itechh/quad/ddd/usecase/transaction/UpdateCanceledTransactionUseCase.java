/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
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
 * 取消済み取引更新ユースケース
 */
@Service
public class UpdateCanceledTransactionUseCase {

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 取引改訂確定ユースケース 内部ロジック */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** コンストラクタ */
    @Autowired
    public UpdateCanceledTransactionUseCase(ITransactionForRevisionRepository transactionForRevisionRepository,
                                            StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter,
                                            OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
    }

    /**
     * 取消済み取引を更新する
     *
     * @param transactionId    取引ID
     * @param administratorSeq 運営者SEQ
     * @param adminMemo        管理メモ
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity updateCanceledTransaction(String transactionId,
                                                         Integer administratorSeq,
                                                         String adminMemo) {

        // TODO 別UCの呼び出し
        // 改訂用取引を開始
        String transactionRevisionId =
                        this.startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(transactionId);

        // 改訂用取引を取得
        TransactionForRevisionEntity transactionForRevisionEntity =
                        this.transactionForRevisionRepository.get(new TransactionRevisionId(transactionRevisionId));
        // 改訂用取引が取得できない場合エラー
        if (transactionForRevisionEntity == null) {
            //取引取得失敗
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
        }

        // 管理メモ設定
        transactionForRevisionEntity.settingAdminMemo(adminMemo);

        // 改訂用取引を更新する
        this.transactionForRevisionRepository.update(transactionForRevisionEntity);

        // TODO 別UCの呼び出し
        // 改訂用取引を確定
        OrderReceivedEntity orderReceivedEntity =
                        openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(transactionRevisionId,
                                                                                             administratorSeq,
                                                                                             HTypeProcessType.CHANGE,
                                                                                             true, false, false
                                                                                            );

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