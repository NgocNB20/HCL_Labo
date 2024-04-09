/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;

/**
 * カテゴリ登録
 *
 * @author kimura
 * @version $Revision: 1.3 $
 */
public interface CategoryRegistService {

    /**
     * カテゴリの登録
     *
     * @param categoryDto カテゴリ情報DTO
     *                    (当サービスでは、List<CategoryDto>を処理対象としない)
     * @return 件数
     */
    int execute(CategoryDto categoryDto);

}
