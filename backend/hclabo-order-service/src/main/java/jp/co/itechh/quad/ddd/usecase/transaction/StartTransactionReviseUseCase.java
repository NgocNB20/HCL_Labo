/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.usecase.transaction.service.StartTransactionReviseUseCaseExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 取引改訂開始ユースケース
 */
@Service
public class StartTransactionReviseUseCase {

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** コンストラクタ */
    @Autowired
    public StartTransactionReviseUseCase(StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter) {
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
    }

    /**
     * 取引改訂を開始する
     *
     * @param transactionId 取引ID
     */
    public String startTransactionRevise(String transactionId) {

        return startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(transactionId);
    }
}
