/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.delivery;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 配送特別料金エリア検索条件Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class DeliverySpecialChargeAreaConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** 郵便番号 */
    private String zipcode;

    /** 送料 */
    private BigDecimal carriage;

    /** 都道府県 */
    private String prefecture;
}