/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * アサートチェッカー<br/>
 * @author yt23807
 */
public class AssertChecker {

    /**
     * プライベートコンストラクタ<br/>
     */
    private AssertChecker() {
    }

    /**
     * <code>null</code>でないことを表明します。
     *
     * @param message
     * @param obj
     * @throws AssertException
     *             <code>null</code>の場合。
     */
    public static void assertNotNull(String message, Object obj) throws AssertException {
        if (obj == null) {
            throw new AssertException(message);
        }
    }

    /**
     * 文字列が空あるいは<code>null</code>でないことを表明します。
     *
     * @param message
     * @param s
     * @throws AssertException
     *             文字列が空あるいは<code>null</code>の場合。
     */
    public static void assertNotEmpty(String message, String s) throws AssertException {
        if (StringUtils.isEmpty(s)) {
            throw new AssertException(message);
        }
    }

    /**
     * コレクションが空あるいは<code>null</code>でないことを表明します。
     *
     * @param message
     * @param c
     * @throws AssertException
     *             コレクションが空あるいは<code>null</code>の場合。
     */
    public static void assertNotEmpty(String message, Collection<?> c) throws AssertException {
        if (CollectionUtils.isEmpty(c)) {
            throw new AssertException(message);
        }
    }

    /**
     * <code>int</code>が負でないことを表明します。
     *
     * @param message
     * @param num
     * @throws AssertException
     *             <code>int</code>が負の場合。
     */
    public static void assertIntegerNotNegative(String message, int num) throws AssertException {
        if (num < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * <code>int</code>が負でないことを表明します。
     *
     * @param message
     * @param num
     * @throws AssertException
     *             <code>int</code>が負の場合。
     */
    public static void assertIntegerNotNegatives(String message, int num) throws AssertException {
        if (num <= 0) {
            throw new IllegalArgumentException(message);
        }
    }
}