/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.category;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * カテゴリ条件詳細エンティティクラス
 *
 * @author @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "CategoryConditionDetail")
@Data
@Component
@Scope("prototype")
public class CategoryConditionDetailEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ（必須） */
    @Column(name = "categorySeq")
    @Id
    private Integer categorySeq;

    /** 条件No（必須） */
    @Column(name = "conditionNo")
    private Integer conditionNo;

    /** 条件項目（必須） */
    @Column(name = "conditionColumn")
    private String conditionColumn;

    /** 条件演算子（必須） */
    @Column(name = "conditionOperator")
    private String conditionOperator;

    /** 条件値（必須） */
    @Column(name = "conditionValue")
    private String conditionValue;

    /** 条件値リスト */
    @Transient
    private List<String> conditionValues;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
