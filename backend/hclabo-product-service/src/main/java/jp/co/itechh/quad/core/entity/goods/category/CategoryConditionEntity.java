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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * カテゴリ条件エンティティクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "CategoryCondition")
@Data
@Component
@Scope("prototype")
public class CategoryConditionEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ（必須） */
    @Column(name = "categorySeq")
    @Id
    private Integer categorySeq;

    /** 条件種別（必須） */
    @Column(name = "conditionType")
    private String conditionType;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
