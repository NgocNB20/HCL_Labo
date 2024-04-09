/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.goods.category;

import jp.co.itechh.quad.admin.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
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

    /** フロント表示 */
    private HTypeFrontDisplayStatus frontDisplay;
}
