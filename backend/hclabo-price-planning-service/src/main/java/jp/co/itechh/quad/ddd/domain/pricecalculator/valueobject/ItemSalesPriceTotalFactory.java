/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商品販売金額合計 ファクトリ
 */
@Service
public class ItemSalesPriceTotalFactory {

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /**
     * コンストラクタ
     *
     * @param productAdapter 商品アダプター
     */
    public ItemSalesPriceTotalFactory(IProductAdapter productAdapter) {
        this.productAdapter = productAdapter;
    }

    /**
     * 商品販売金額合計 コンストラクト
     *
     * @param paramList 商品販売金額合計ファクトリパラメータリスト
     * @return 商品販売金額合計
     */
    public ItemSalesPriceTotal constructItemSalesPriceTotal(List<ItemSalesPriceTotalFactoryParam> paramList) {

        // チェック
        AssertChecker.assertNotEmpty("ItemSalesPriceTotalParam List is empty", paramList);
        for (ItemSalesPriceTotalFactoryParam param : paramList) {
            AssertChecker.assertNotEmpty("ItemSalesPriceTotalParam itemId is empty", param.getOrderItemId());
            AssertChecker.assertIntegerNotNegative(
                            "ItemSalesPriceTotalParam itemCount is negative", param.getOrderItemCount());
        }

        // DBの商品情報を取得
        List<GoodsDetailsDto> goodsDetailsList = getGoodsDetails(paramList);

        // 商品販売金額小計 値オブジェクトリストを生成
        List<ItemSalesPriceSubTotal> itemSalesPriceSubTotalList = new ArrayList<>();

        for (ItemSalesPriceTotalFactoryParam param : paramList) {

            GoodsDetailsDto goodsDetailsDto = goodsDetailsList.stream()
                                                              .filter(details -> Integer.valueOf(param.getOrderItemId())
                                                                                        .equals(details.getGoodsSeq()))
                                                              .findFirst()
                                                              .orElseThrow();

            // 商品 値オブジェクト作成
            Item item;
            Item existItem = param.getApplyExistItem();
            if (existItem == null) {
                item = new Item(param.getOrderItemId(), goodsDetailsDto.getGoodsPrice().intValue(),
                                goodsDetailsDto.getTaxRate(),
                                goodsDetailsDto.getFreeDeliveryFlag() == HTypeFreeDeliveryFlag.ON,
                                goodsDetailsDto.getGoodsCode()
                );
            } else {
                item = new Item(existItem.getItemId(), existItem.getItemUnitPrice(), existItem.getTaxRate(),
                                existItem.isFreeCarriageItemFlag(), goodsDetailsDto.getGoodsCode()
                );
            }

            // 商品販売金額小計 値オブジェクト生成
            ItemSalesPriceSubTotal itemSalesPriceSubTotal =
                            new ItemSalesPriceSubTotal(param.getOrderItemSeq(), item, param.getOrderItemCount());
            // 商品販売金額小計リスト
            itemSalesPriceSubTotalList.add(itemSalesPriceSubTotal);
        }

        return new ItemSalesPriceTotal(itemSalesPriceSubTotalList);
    }

    /**
     * DBの商品情報取得
     *
     * @param paramList 商品販売金額合計ファクトリパラメータリスト
     * @return 商品詳細リスト
     */
    private List<GoodsDetailsDto> getGoodsDetails(List<ItemSalesPriceTotalFactoryParam> paramList) {

        Set<Integer> goodsSeqSet = new HashSet<>();
        for (ItemSalesPriceTotalFactoryParam param : paramList) {
            goodsSeqSet.add(Integer.valueOf(param.getOrderItemId()));
        }

        return productAdapter.getGoodsDetailsList(new ArrayList<>(goodsSeqSet));
    }

}
