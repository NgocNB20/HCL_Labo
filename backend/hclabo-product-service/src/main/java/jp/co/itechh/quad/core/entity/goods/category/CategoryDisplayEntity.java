/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.category;

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
 * カテゴリ表示クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "CategoryDisplay")
@Data
@Component
@Scope("prototype")
public class CategoryDisplayEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ (FK)（必須） */
    @Column(name = "categorySeq")
    @Id
    private Integer categorySeq;

    /** カテゴリ表示名PC */
    @Column(name = "categoryNamePC")
    private String categoryNamePC;

    /** カテゴリについての説明文PC */
    @Column(name = "categoryNotePC")
    private String categoryNotePC;

    /** フリーテキストPC */
    @Column(name = "freeTextPC")
    private String freeTextPC;

    /** meta-description */
    @Column(name = "metaDescription")
    private String metaDescription;

    /** カテゴリ画像PC */
    @Column(name = "categoryImagePC")
    private String categoryImagePC;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}