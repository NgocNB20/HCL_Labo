/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.cart;

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
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * カート商品クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "CartGoods")
@Data
@Component
@Scope("prototype")
public class CartGoodsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カートSEQ（必須） */
    @Column(name = "cartSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "cartSeq")
    private Integer cartSeq;

    /** 端末識別情報（必須） */
    @Column(name = "accessUid")
    private String accessUid;

    /** 会員SEQ (FK)（必須） */
    @Column(name = "memberInfoSeq")
    private Integer memberInfoSeq = 0;

    /** 商品SEQ (FK)（必須） */
    @Column(name = "goodsSeq")
    private Integer goodsSeq;

    /** カート内商品数量（必須） */
    @Column(name = "cartGoodsCount")
    private BigDecimal cartGoodsCount = new BigDecimal(0);

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** ショップSEQ (FK)（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;
}