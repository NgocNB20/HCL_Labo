/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order.delivery;

import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.admin.entity.order.delivery.OrderDeliveryEntity;
import jp.co.itechh.quad.admin.entity.order.goods.OrderGoodsEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 受注配送Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderDeliveryDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カートポップアップで各お届け先に対して選んだ商品のリスト ※画面用の一時情報 */
    private List<OrderGoodsEntity> tmpOrderGoodsEntityList;

    /** 受注商品Dtoリスト */
    private List<OrderGoodsEntity> orderGoodsEntityList;

    /** 受注配送エンティティ */
    private OrderDeliveryEntity orderDeliveryEntity;

    /** 配送方法エンティティ */
    private DeliveryMethodEntity deliveryMethodEntity;

    /** 「このお届け先を住所に登録する」フラグ */
    private boolean receiverRegistFlg;

    /** 配送DTOリスト */
    private List<DeliveryDto> deliveryDtoList;

    /**
     * 前回送料<br/>
     */
    private BigDecimal originalCarriage;

    /** 初回に選択した商品エンティティリスト */
    private List<OrderGoodsEntity> firstSelectedOrderGoodsEntityList;
}