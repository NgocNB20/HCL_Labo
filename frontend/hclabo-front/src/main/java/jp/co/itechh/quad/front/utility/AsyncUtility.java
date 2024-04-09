/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 非同期用共通Utility<br/>
 * AsyncUtilを経由して呼び出し、このUtilityを直接利用しないこと<br/>
 * @Async付与メソッドは、publicのみ機能する
 *
 * @author kimura
 */
@Component
public class AsyncUtility {
    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUtility.class);

    /**
     * 非同期処理実行<br/>
     * AsyncUtil#asyncService() を経由して呼び出すこと
     *
     * @param execInstance 実行するインスタンス
     * @param methodName 実行するメソッド名
     * @param args メソッドの引数
     * @param argsClass メソッドの引数のクラス
     */
    @Async
    public void asyncExecute(Object execInstance, String methodName, Object[] args, Class<?>[] argsClass) {

        LOGGER.info(String.format("AsyncUtility#asyncExecute : %s#%s", execInstance.getClass().getSimpleName(), methodName));

        Method methodExecute;
        try {
            // Executeメソッド取得
            methodExecute = execInstance.getClass().getMethod(methodName, argsClass);

            try {
                // Executeメソッド実行
                methodExecute.invoke(execInstance, args);
            } catch (Exception e) {
                LOGGER.error("次のサービス" + methodExecute + "の非同期処理実行中に例外発生", e);
            }

        } catch (Exception e) {
            LOGGER.error("非同期処理サービス取得で例外発生", e);
        }
    }
}