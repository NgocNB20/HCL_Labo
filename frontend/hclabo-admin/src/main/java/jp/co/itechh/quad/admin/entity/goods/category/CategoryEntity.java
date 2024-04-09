/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.category;

import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * カテゴリクラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class CategoryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ（必須） */
    private Integer categorySeq;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** カテゴリID（必須） */
    private String categoryId;

    /** カテゴリ名（必須） */
    private String categoryName;

    /** カテゴリ公開状態PC（必須） */
    private HTypeOpenStatus categoryOpenStatusPC = HTypeOpenStatus.NO_OPEN;

    /** カテゴリ公開開始日時PC */
    private Timestamp categoryOpenStartTimePC;

    /** カテゴリ公開終了日時PC */
    private Timestamp categoryOpenEndTimePC;

    /** カテゴリ種別（必須） */
    private HTypeCategoryType categoryType = HTypeCategoryType.NORMAL;

    /** カテゴリSEQパス（必須） */
    private String categorySeqPath;

    /** 手動表示フラグ（必須） */
    private HTypeManualDisplayFlag manualDisplayFlag = HTypeManualDisplayFlag.OFF;

    /** 更新カウンタ（必須） */
    private Integer versionNo = 0;

    /** サイトマップ出力フラグ（必須） */
    private HTypeSiteMapFlag siteMapFlag = HTypeSiteMapFlag.OFF;

    // テーブル項目外追加フィールド
    /** PC公開商品点数 */
    private Integer openGoodsCountPC;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** カテゴリ登録チェック */
    private boolean registCategoryCheck;
}