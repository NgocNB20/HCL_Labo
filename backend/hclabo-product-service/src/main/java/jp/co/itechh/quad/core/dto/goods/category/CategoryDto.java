/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.category;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
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

    /** カテゴリ登録商品並び順エンティティ */
    private CategoryGoodsSortEntity categoryGoodsSortEntity;

    /** カテゴリ条件エンティティ */
    private CategoryConditionEntity categoryConditionEntity;

    /** カテゴリ条件詳細エンティティリスト */
    private List<CategoryConditionDetailEntity> categoryConditionDetailEntityList;

    /** カテゴリ登録商品エンティティリスト */
    private List<CategoryGoodsEntity> categoryGoodsEntityList;

    /** フロント表示 */
    private HTypeFrontDisplayStatus frontDisplay;

    /** ワーニングメッセージ */
    private String warningMessage;
}
