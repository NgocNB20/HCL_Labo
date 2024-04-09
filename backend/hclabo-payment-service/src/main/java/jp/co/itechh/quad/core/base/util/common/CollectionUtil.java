/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.core.base.util.common;

import java.util.Collection;

/**
 * コレクションユーティリティクラス
 *
 * @author k.kizaki
 */
public class CollectionUtil {

    /**
     * 隠蔽コンストラクタ
     */
    private CollectionUtil() {
    }

    /**
     * Collection に値があるかのチェック
     *
     * @param value 処理対象
     * @return true..null か 要素ゼロ
     */
    public static boolean isEmpty(Collection<?> value) {
        return value == null || value.isEmpty();
    }

    /**
     * Collection の要素数を返却
     * <pre>
     * null の場合は -1 を返却
     * </pre>
     *
     * @param value 処理対象
     * @return 要素数
     */
    public static int getSize(Collection<?> value) {
        if (value == null) {
            return -1;
        }
        return value.size();
    }

}
