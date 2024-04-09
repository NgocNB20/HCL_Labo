/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.common.dto;

import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 登録カテゴリー＆登録商品タグDtoクラス
 *
 **/
@Data
@Component
@Scope("prototype")
public class CategoryAndTagForGoodsRegistUpdateDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 登録カテゴリリスト*/
    private List<CategoryEntity> categoryChosenList;

    /** 登録商品タグリスト*/
    private List<String> tagChosenList;
}
