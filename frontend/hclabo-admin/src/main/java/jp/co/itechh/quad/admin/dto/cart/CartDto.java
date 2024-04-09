/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.cart;

import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * カートDtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class CartDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品合計点数 */
    private BigDecimal goodsTotalCount;

    /** 商品合計金額（税抜） */
    private BigDecimal goodsTotalPrice;

    /** 商品合計金額（税込） */
    private BigDecimal goodsTotalPriceInTax;

    /** カート商品Dtoリスト */
    private List<CartGoodsDto> cartGoodsDtoList;

    /** settlement method type */
    private HTypeSettlementMethodType settlementMethodType;
}