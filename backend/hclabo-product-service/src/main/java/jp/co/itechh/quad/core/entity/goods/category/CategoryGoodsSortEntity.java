/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeSortByType;
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
 * カテゴリ登録商品並び順エンティティクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "CategoryGoodsSort")
@Data
@Component
@Scope("prototype")
public class CategoryGoodsSortEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ (FK)（必須） */
    @Column(name = "categorySeq")
    @Id
    private Integer categorySeq;

    /** 商品並び順項目（必須） */
    @Column(name = "goodsSortColumn")
    private HTypeSortByType goodsSortColumn;

    /** 商品並び順（必須） */
    @Column(name = "goodsSortOrder")
    private Boolean goodsSortOrder;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
