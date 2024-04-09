/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.core.base.util.common;

import java.util.Collection;

/**
 * コレクションユーティリティクラス
 */
public class CollectionUtil {

    /** 隠蔽コンストラクタ */
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
}
