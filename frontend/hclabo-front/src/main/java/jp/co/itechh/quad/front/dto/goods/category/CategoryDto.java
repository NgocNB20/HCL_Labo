/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.goods.category;

import jp.co.itechh.quad.front.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.front.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.front.entity.goods.category.CategoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * カテゴリDtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.3 $
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Scope("prototype")
public class CategoryDto extends AbstractConditionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリエンティティ */
    private CategoryEntity categoryEntity;

    /** カテゴリ表示エンティティ */
    private CategoryDisplayEntity categoryDisplayEntity;

    /** カテゴリDTOリスト */
    private List<CategoryDto> categoryDtoList;
}
