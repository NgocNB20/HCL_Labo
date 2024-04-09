/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.doublesubmit;

import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ダブルサブミットチェック制御情報のキャッシュクラス
 *
 * @author hk57400
 */
public class DoubleSubmitCheckInfoCache {

    /** 処理したメソッド毎に、チェック有無やネームスペースをキャッシュ */
    private final ConcurrentMap<Method, DoubleSubmitCheckInfo> cacheMap = new ConcurrentHashMap<>();

    /**
     * 該当メソッドのチェック制御情報を取得
     * <pre>
     *   初回実行時はメタデータをスキャンし、
     *   2回目以降はキャッシュから取得する。
     * </pre>
     *
     * @param handlerMethod 実行メソッド
     * @return チェック制御情報
     */
    public DoubleSubmitCheckInfo getInfo(final HandlerMethod handlerMethod) {

        Method method = handlerMethod.getMethod();

        DoubleSubmitCheckInfo info = cacheMap.get(method);
        if (info != null) {
            return info;
        }

        synchronized (cacheMap) {
            info = cacheMap.computeIfAbsent(method, key -> new DoubleSubmitCheckInfo(handlerMethod));
        }

        return info;
    }

}
