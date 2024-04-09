/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategoryExclusiveDto;

/**
 * カテゴリ排他を取得
 *
 * @author chinhmc (ALX)  2019/11/28 チケット #4119 対応  排他チェック
 * @author chinhmc (ALX)  2019/11/29 チケット #4119 対応  カテゴリー削除の排他制御
 *
 */
public interface CategoryGetExclusiveLogic {

    /**
     * カテゴリ排他を取得
     *
     * @return カテゴリ排他Dtoクラス
     */
    CategoryExclusiveDto execute();

}
