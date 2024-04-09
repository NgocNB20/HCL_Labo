/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.goods.category;

import jp.co.itechh.quad.front.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.front.entity.goods.category.CategoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * カテゴリー木構造Dtoクラス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Scope("prototype")
public class CategoryTreeDto extends AbstractConditionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 通番（同一階層内の通番） */
    private String serialNumber;

    /** カテゴリID */
    private String categoryId;

    /** 表示名 */
    private String displayName;

    /** 階層付き通番 */
    private String hierarchicalSerialNumber;

    /** カテゴリエンティティ */
    private CategoryEntity categoryEntity;

    /** カテゴリDTOリスト */
    private List<CategoryTreeDto> categoryTreeDtoList;
}
