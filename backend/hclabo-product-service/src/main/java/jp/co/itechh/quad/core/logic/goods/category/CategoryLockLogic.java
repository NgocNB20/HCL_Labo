/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import java.util.List;

/**
 *
 * カテゴリレコードロック取得ロジック
 *
 * @author hirata
 * @version $Revision: 1.2 $
 *
 */
public interface CategoryLockLogic {
    // LGC0009

    /**
     * カテゴリ排他取得エラー
     * <code>MSGCD_CATEGORY_SELECT_FOR_UPDATE_FAIL</code>
     */
    public static final String MSGCD_CATEGORY_SELECT_FOR_UPDATE_FAIL = "LGC000901";

    /**
     * カテゴリレコードロック
     * カテゴリテーブルのレコードを排他取得する。
     *
     * @param categorySeqList カテゴリSEQリスト
     */
    void execute(List<Integer> categorySeqList);

}