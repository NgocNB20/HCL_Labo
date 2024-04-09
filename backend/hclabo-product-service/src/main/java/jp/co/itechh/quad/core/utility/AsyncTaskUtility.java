/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 非同期処理のユーティリティクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class AsyncTaskUtility {

    /**
     * トランザクションがコミットされたタイミングで非同期処理を実行させる
     *
     * @param runnable
     */
    public static void executeAfterTransactionCommit(Runnable runnable) {
        // 同期処理がトランザクション内に動いているかないかのチェック
        // あった場合：afterCommitメソッドを上書きし、同期処理の終了後に非同期処理を開始させる
        // なかった場合：非同期処理をすぐに開始させる
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

}