/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.front.base.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * コレクションユーティリティクラス
 *
 * @author k.kizaki
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

    /**
     * Collection に値があるかのチェック
     *
     * @param value 処理対象
     * @return true..要素を持っている
     */
    public static boolean isNotEmpty(Collection<?> value) {
        return !isEmpty(value);
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

    /**
     * Collection の要素に指定の要素があるか？
     *
     * @param value 処理対象
     * @param target 指定の要素
     * @return true ある
     */
    public static boolean isContains(Collection<?> value, Object target) {
        if (isEmpty(value)) {
            return false;
        }
        return value.contains(target);
    }

    /**
     * Collection の要素に指定の要素がないか？
     *
     * @param value 処理対象
     * @param target 指定の要素
     * @return true ない
     */
    public static boolean isNotContains(Collection<?> value, Object target) {
        return !isContains(value, target);
    }

    /**
     * リストデータの絞り込み<br />
     * リスト1, リスト2, ... に共通する項目リストを作成する際に使用します。<br />
     * List&lt;T&gt; commonList に共通リストを指定します。<br />
     * list をList1, List2, ...<br />
     * と繰り返し呼び出して使用します。<br />
     *
     * @param <T> オブジェクト
     * @param commonList 共通リスト
     * @param list データリスト
     * @return 共通リスト
     */
    public static <T> List<T> narrowingList(List<T> commonList, List<T> list) {
        List<T> resList = null;
        if (commonList == null) {
            resList = list;
        } else {
            resList = new ArrayList<>(commonList);
            for (int index = 0; index < resList.size() && list != null; index++) {
                if (!list.contains(commonList.get(index))) {
                    resList.remove(index);
                    index--;
                }
            }
        }
        return resList;
    }
}
