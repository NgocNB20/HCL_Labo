/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;

/**
 * 配送方法 アダプター
 */
public interface IShippingMethodAdapter {
    /**
     * 配送方法情報取得
     * ※引数パラメータを基に適切な送料を含む配送方法情報取得するAPI呼出し
     *
     * @param settlementMethodSeq
     * @param zipcode
     * @param itemPriceTotal
     * @return DeliveryDto
     */
    DeliveryDto getDeliveryDto(Integer settlementMethodSeq, String zipcode, int itemPriceTotal);
}
