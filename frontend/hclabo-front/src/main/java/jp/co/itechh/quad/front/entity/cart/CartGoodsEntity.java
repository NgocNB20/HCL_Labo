/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.entity.cart;

import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class CartGoodsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カートSEQ（必須） */
    private Integer cartSeq;

    /** 端末識別情報（必須） */
    private String accessUid;

    /** 会員SEQ (FK)（必須） */
    private Integer memberInfoSeq = 0;

    /** 商品SEQ (FK)（必須） */
    private Integer goodsSeq;

    /** カート内商品数量（必須） */
    private BigDecimal cartGoodsCount = new BigDecimal(0);

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** ショップSEQ (FK)（必須） */
    private Integer shopSeq;
}