/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.goods.category;

import jp.co.itechh.quad.front.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.front.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.front.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * カテゴリ詳細Dtoクラス
 *
 * @author DtoGenerator
 */
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

    /** 手動表示フラグ */
    private HTypeManualDisplayFlag manualDisplayFlag;

    /** 更新カウンタ */
    private Integer versionNo;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

    /** 登録日時 */
    private Timestamp displayRegistTime;

    /** 更新日時 */
    private Timestamp displayUpdateTime;

    /** 商品並び順項目 */
    private String goodsSortColumn;

    /** 商品並び順 */
    private boolean goodsSortOrder;

    /** フロント表示 */
    private HTypeFrontDisplayStatus frontDisplay;

    /** ワーニングメッセージ */
    private String warningMessage;

}
