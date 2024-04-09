/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.cart;

import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.entity.cart.CartGoodsEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * カート商品Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class CartGoodsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カート商品エンティティ */
    private CartGoodsEntity cartGoodsEntity;

    /** 商品詳細DTO */
    private GoodsDetailsDto goodsDetailsDto;

    /** 商品アイコン情報DTOリスト */
    private List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList;

    /** 金額(税抜き） */
    private BigDecimal goodsPriceSubtotal;

    /** 金額（税込み） */
    private BigDecimal goodsPriceInTaxSubtotal;

    /** 商品単価変更フラグ */
    private Boolean goodsPriceChanged;
}