/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.menu;

/**
 * メニュー入力バリデータLogic
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface MenuCheckLogic {

    /**
     *
     * メニュー入力バリデータ
     *
     * @param categoryTreeJson カテゴリーツリーJSON
     */
    void checkForUpdate(String categoryTreeJson);
}