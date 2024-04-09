/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.category;

import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * カテゴリ登録商品クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class CategoryGoodsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ (FK)（必須） */
    private Integer categorySeq;

    /** カテゴリ名 */
    private String categoryName;

    /** 商品グループSEQ (FK)（必須） */
    private Integer goodsGroupSeq;

    /** 表示順 */
    private Integer orderDisplay;

    /** カテゴリ種別 */
    private HTypeCategoryType categoryType;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}