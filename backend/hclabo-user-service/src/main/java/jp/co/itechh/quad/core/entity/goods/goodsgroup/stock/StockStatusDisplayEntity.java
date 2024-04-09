/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.goodsgroup.stock;

import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品グループ在庫表示クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "StockStatusDisplay")
@Data
@Component
@Scope("prototype")
public class StockStatusDisplayEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ（必須） */
    @Column(name = "goodsGroupSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "goodsGroupSeq")
    private Integer goodsGroupSeq;

    /** 在庫状態PC（必須） */
    @Column(name = "stockStatusPc")
    private HTypeStockStatusType stockStatusPc;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}