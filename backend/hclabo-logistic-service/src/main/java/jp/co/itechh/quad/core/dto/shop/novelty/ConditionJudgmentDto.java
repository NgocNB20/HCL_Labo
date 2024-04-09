/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dto.shop.novelty;

import lombok.Data;

import java.util.Date;

/**
 * 条件判定DTO
 *
 * @author PHAM QUANG DIEU (VJP)
 */

@Data
public class ConditionJudgmentDto {

    /** 取引ID */
    private String transactionId;

    /** 登録日付 */
    private Date registTime;

    /** 商品販売金額合計 */
    private Integer itemSalesPriceTotal;

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** 会員ID */
    private String memberInfoId;
}
