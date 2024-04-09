/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * 非同期用共通サービス
 * 非同期処理はこの共通サービスを経由してcall
 * @Async付与メソッドは、publicのみ機能する
 *
 * @author kimura
 */
@Service
public class AsyncService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);

    /**
     * 非同期処理を実行
     * @param component サービスクラス
     * @param args 実行サービス用パラメータ
     * @param argsClass 実行サービス用パラメータの型
     */
    @Async
    public void asyncService(Object component, String methodName, Object[] args, Class<?>[] argsClass) {

        Method methodExecute;
        try {
            // Executeメソッド取得
            methodExecute = component.getClass().getMethod(methodName, argsClass);

            try {
                // Executeメソッド実行
                methodExecute.invoke(component, args);
            } catch (Exception e) {
                LOGGER.error("次のサービス" + methodExecute + "の非同期処理実行中に例外発生", e);
            }

        } catch (Exception e) {
            LOGGER.error("非同期処理サービス取得で例外発生", e);
        }
    }
}