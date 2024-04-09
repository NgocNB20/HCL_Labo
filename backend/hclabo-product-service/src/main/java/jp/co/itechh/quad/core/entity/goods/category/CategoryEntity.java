/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
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
@Entity
@Table(name = "Category")
@Data
@Component
@Scope("prototype")
public class CategoryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ（必須） */
    @Column(name = "categorySeq")
    @Id
    private Integer categorySeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** カテゴリID（必須） */
    @Column(name = "categoryId")
    private String categoryId;

    /** カテゴリ名（必須） */
    @Column(name = "categoryName")
    private String categoryName;

    /** カテゴリ公開状態PC（必須） */
    @Column(name = "categoryOpenStatusPC")
    private HTypeOpenStatus categoryOpenStatusPC = HTypeOpenStatus.NO_OPEN;

    /** カテゴリ公開開始日時PC */
    @Column(name = "categoryOpenStartTimePC")
    private Timestamp categoryOpenStartTimePC;

    /** カテゴリ公開終了日時PC */
    @Column(name = "categoryOpenEndTimePC")
    private Timestamp categoryOpenEndTimePC;

    /** カテゴリ種別（必須） */
    @Column(name = "categoryType")
    private HTypeCategoryType categoryType = HTypeCategoryType.NORMAL;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}