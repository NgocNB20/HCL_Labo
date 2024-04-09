/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.category;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * カテゴリ情報Dao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Scope("prototype")
public class CategorySearchForDaoConditionDto extends AbstractConditionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリテーブル項目（カテゴリパス） */
    public static final String CATEGORY_FIELD_CATEGORY_PATH = "categorypath";

    /** ショップSEQ */
    private Integer shopSeq;

    /** カテゴリキーワード集計 */
    private String categorySearchKeyword;

    /** カテゴリ条件 */
    private CategoryConditionDto categoryCondition1;

    /** カテゴリ条件 */
    private CategoryConditionDto categoryCondition2;

    /** カテゴリ条件 */
    private CategoryConditionDto categoryCondition3;

    /** カテゴリ条件 */
    private CategoryConditionDto categoryCondition4;

    /** カテゴリ条件 */
    private CategoryConditionDto categoryCondition5;

    /** 公開開始日時From */
    private Timestamp openStartTimeFrom;

    /** 公開開始日時To */
    private Timestamp openStartTimeTo;

    /** 公開終了日時From */
    private Timestamp openEndTimeFrom;

    /** 公開終了日時To */
    private Timestamp openEndTimeTo;

    /** カテゴリID */
    private String categoryId;

    /** カテゴリSEQリスト */
    private List<Integer> categorySeqList;

    /** 最大表示階層数 */
    private Integer maxHierarchical;

    /** サイト区分 */
    private HTypeSiteType siteType;

    /** 公開状態 */
    private HTypeOpenStatus openStatus;

    /** 除外カテゴリIDリスト */
    private List<String> notInCategoryIdList;

    /** カテゴリ種類 */
    private List<String> categoryTypeList;

    // ※カテゴリの検索ではページング制御不要のため、PageInfoは使わない（AbstractConditionDtoの継承はしない）
    /** 並替項目 */
    private String orderField;

    /** 並替昇順フラグ */
    private boolean orderAsc;

    /** フロント表示状態リスト */
    private List<String> frontDisplayList;

    /** フロント表示基準日時 */
    private Timestamp frontDisplayReferenceDate;
}