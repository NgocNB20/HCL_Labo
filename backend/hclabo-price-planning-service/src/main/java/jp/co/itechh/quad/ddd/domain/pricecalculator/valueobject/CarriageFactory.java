/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingMethodAdapter;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 送料 ファクトリ
 */
@Service
public class CarriageFactory {

    /** 送料 アダプター */
    private final IShippingMethodAdapter shippingAdapter;

    /**
     * コンストラクタ
     *
     * @param shippingAdapter
     */
    @Autowired
    public CarriageFactory(IShippingMethodAdapter shippingAdapter) {
        this.shippingAdapter = shippingAdapter;
    }

    /**
     * 送料 算出コンストラクト
     *
     * @param shippingMethodId
     * @param zipcode
     * @param itemSalesPriceTotal
     * @return 送料
     */
    public Carriage constructCarriage(String shippingMethodId,
                                      String zipcode,
                                      ItemSalesPriceTotal itemSalesPriceTotal) {

        // チェック
        AssertChecker.assertNotEmpty("shippingMethodId is empty", shippingMethodId);
        AssertChecker.assertNotEmpty("zipcode is empty", zipcode);
        AssertChecker.assertNotNull("itemSalesPriceTotal is null", itemSalesPriceTotal);

        // 送料込み商品が含まれている場合は送料なし
        for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceTotal.getItemSalesPriceSubTotalList()) {
            if (itemSalesPriceSubTotal.getOrderItem().isFreeCarriageItemFlag()
                && itemSalesPriceSubTotal.getOrderItemCount() > 0) {
                return new Carriage(0);
            }
        }

        // 配送方法情報取得
        DeliveryDto deliveryDto = shippingAdapter.getDeliveryDto(Integer.valueOf(shippingMethodId), zipcode,
                                                                 itemSalesPriceTotal.getItemPriceTotal()
                                                                );
        if (deliveryDto == null) {
            throw new DomainException("PRICE-PLANNING-DELV0001-E");
        }
        if (deliveryDto.getCarriage() == null) {
            return new Carriage(0);
        }

        return new Carriage(deliveryDto.getCarriage().intValue());
    }

}