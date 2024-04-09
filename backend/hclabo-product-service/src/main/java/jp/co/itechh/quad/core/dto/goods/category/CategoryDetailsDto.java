/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteMapFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * カテゴリ詳細Dtoクラス
 *
 * @author DtoGenerator
 */
@Entity
@Data
@Component
@Scope("prototype")
public class CategoryDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリID */
    private String categoryId;

    /** カテゴリ表示名PC */
    private String categoryNamePC;

    /** カテゴリについての説明文PC */
    private String categoryNotePC;

    /** フリーテキストPC */
    private String freeTextPC;

    /** meta-description */
    private String metaDescription;

    /** meta-keyword */
    private String metaKeyword;

    /** カテゴリ画像PC */
    private String categoryImagePC;

    /** カテゴリSEQ */
    private Integer categorySeq;

    /** ショップSEQ */
    private Integer shopSeq;

    /** カテゴリ名 */
    private String categoryName;

    /** カテゴリ公開状態PC */
    private HTypeOpenStatus categoryOpenStatusPC;

    /** カテゴリ公開開始日時PC */
    private Timestamp categoryOpenStartTimePC;

    /** カテゴリ公開終了日時PC */
    private Timestamp categoryOpenEndTimePC;

    /** カテゴリ種別 */
    private HTypeCategoryType categoryType;

    /** カテゴリSEQパス */
    private String categorySeqPath;

    /** 表示順 */
    private Integer orderDisplay;

    /** ルートカテゴリSEQ */
    private Integer rootCategorySeq;

    /** カテゴリ階層 */
    private Integer categoryLevel;

    /** カテゴリパス */
    private String categoryPath;

    /** 手動表示フラグ */
    private HTypeManualDisplayFlag manualDisplayFlag;

    /** 更新カウンタ */
    private Integer versionNo;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

    /** 条件種別 */
    private String conditionType;

    /** カテゴリ条件詳細 */
    private String conditionDetailList;

    /** カテゴリ商品数 */
    private Integer openGoodsCount;

    /** 商品並び順項目 */
    private String goodsSortColumn;

    /** 商品並び順 */
    private Boolean goodsSortOrder;

    /** 登録日時 */
    private Timestamp displayRegistTime;

    /** 更新日時 */
    private Timestamp displayUpdateTime;

    /** サイトマップ出力フラグ */
    private HTypeSiteMapFlag siteMapFlag;

    /** フロント表示 */
    private HTypeFrontDisplayStatus frontDisplay;
}
