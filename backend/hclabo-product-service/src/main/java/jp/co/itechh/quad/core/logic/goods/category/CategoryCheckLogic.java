/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;

/**
 * カテゴリ入力バリデータLogic
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryCheckLogic {

    /**
     *
     * カテゴリ入力バリデータ
     *
     * @param categoryDto カテゴリDtoクラス
     */
    void checkForRegistUpdate(CategoryDto categoryDto);
}