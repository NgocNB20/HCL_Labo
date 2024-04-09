/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.base.util.common;

import jp.co.itechh.quad.core.base.exception.seasar.EmptyRuntimeException;

import java.util.List;

/**
 * 引数チェック用ユーティリティクラス<br/>
 * <pre>
 * 引数チェックのためのみに使用すること。
 * チェックエラーの場合はRuntime系の例外をスローすること。
 * メソッド名はassertを接頭語とすること。
 * </pre>
 *
 * @author negishi
 * @version $Revision: 1.2 $
 */
public final class ArgumentCheckUtil {

    /**
     * デフォルトコンストラクタ<br/>
     * インスタンス化禁止
     */
    private ArgumentCheckUtil() {
    }

    /**
     * 対象オブジェクトが<code>null</code>でないことを表明します。<br/>
     *
     * @param message メッセージ
     * @param obj 対象オブジェクト
     * @throws NullPointerException 保障できない場合
     */
    public static void assertNotNull(String message, Object obj) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 対象値が空あるいは<code>null</code>でないことを表明します。<br/>
     *
     * @param message メッセージ
     * @param value 対象値
     * @throws EmptyRuntimeException 保障できない場合
     */
    public static void assertNotEmpty(String message, String value) throws EmptyRuntimeException {
        if (value == null || value.length() == 0) {
            throw new EmptyRuntimeException(message);
        }
    }

    /**
     * 対象リストが空あるいは<code>null</code>でないことを表明します。<br/>
     *
     * @param message メッセージ
     * @param list 対象リスト
     * @throws EmptyRuntimeException 保障できない場合
     */
    public static void assertNotEmpty(String message, List<?> list) throws EmptyRuntimeException {
        if (list == null || list.isEmpty()) {
            throw new EmptyRuntimeException(message);
        }
    }

    /**
     * 対象値がゼロより大きいことを表明します。<br/>
     *
     * @param message メッセージ
     * @param value 対象値
     * @throws IllegalArgumentException 保障できない場合
     */
    public static void assertGreaterThanZero(String message, Integer value) throws IllegalArgumentException {
        if (value == null || value.intValue() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
