/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.pricecalculator;

import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceSubTotal;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceTotal;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceTotalFactory;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceTotalFactoryParam;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品販売金額計算 ユースケース
 */
@Service
public class CalculationItemSalesPriceUseCase {

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 商品販売金額合計 ファクトリ */
    private final ItemSalesPriceTotalFactory itemSalesPriceTotalFactory;

    /**
     * コンストラクタ
     *
     * @param orderSlipAdapter
     * @param itemSalesPriceTotalFactory
     */
    @Autowired
    public CalculationItemSalesPriceUseCase(IOrderSlipAdapter orderSlipAdapter,
                                            ItemSalesPriceTotalFactory itemSalesPriceTotalFactory) {
        this.orderSlipAdapter = orderSlipAdapter;
        this.itemSalesPriceTotalFactory = itemSalesPriceTotalFactory;
    }

    /**
     * 商品販売金額を計算する
     *
     * @param orderSlipId
     * @return CalculationBillingAmountUseCaseDto
     */
    public CalculationItemSalesPriceUseCaseDto calculationItemSalesPrice(String orderSlipId) {

        // チェック
        AssertChecker.assertNotEmpty("orderSlipId is empty", orderSlipId);

        // 戻り値生成
        CalculationItemSalesPriceUseCaseDto calculationItemSalesPriceUseCaseDto =
                        new CalculationItemSalesPriceUseCaseDto();

        // 下書き注文票を取得する
        OrderSlip orderSlip = orderSlipAdapter.getDraftOrderSlipById(orderSlipId);
        // 存在しない場合、またはカートに商品が存在しない場合は計算せずに終了
        if (orderSlip == null || CollectionUtils.isEmpty(orderSlip.getItemList())) {
            return calculationItemSalesPriceUseCaseDto;
        }

        // 注文票の金額計算を行い、calculationItemSalesPriceUseCaseDtoに設定する
        calculationOrderSlipPrice(orderSlip, calculationItemSalesPriceUseCaseDto);

        return calculationItemSalesPriceUseCaseDto;
    }

    /**
     * 注文票の金額計算
     * ※計算結果をcalculationItemSalesPriceUseCaseDtoに設定する
     *
     * @param orderSlip
     * @param calculationItemSalesPriceUseCaseDto
     */
    private void calculationOrderSlipPrice(OrderSlip orderSlip,
                                           CalculationItemSalesPriceUseCaseDto calculationItemSalesPriceUseCaseDto) {

        // 商品販売金額合計 値オブジェクトを生成
        List<ItemSalesPriceTotalFactoryParam> paramList = new ArrayList<>();

        for (OrderSlipItem orderSlipItem : orderSlip.getItemList()) {
            ItemSalesPriceTotalFactoryParam itemSalesPriceTotalFactoryParam = new ItemSalesPriceTotalFactoryParam();
            itemSalesPriceTotalFactoryParam.setOrderItemId(orderSlipItem.getItemId());
            itemSalesPriceTotalFactoryParam.setOrderItemCount(orderSlipItem.getItemCount());
            paramList.add(itemSalesPriceTotalFactoryParam);
        }

        ItemSalesPriceTotal itemSalesPriceTotal = itemSalesPriceTotalFactory.constructItemSalesPriceTotal(paramList);

        // 商品販売金額 ユースケースDtoリストを生成
        List<ItemSalesPriceUseCaseDto> itemSalesPriceUseCaseDtoList = new ArrayList<>();

        for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceTotal.getItemSalesPriceSubTotalList()) {

            ItemSalesPriceUseCaseDto itemSalesPriceDto = new ItemSalesPriceUseCaseDto();

            itemSalesPriceDto.setItemId(itemSalesPriceSubTotal.getOrderItem().getItemId());
            itemSalesPriceDto.setItemTaxRate(itemSalesPriceSubTotal.getOrderItem().getTaxRate());
            itemSalesPriceDto.setItemUnitPrice(itemSalesPriceSubTotal.getOrderItem().getItemUnitPrice());
            itemSalesPriceDto.setItemUnitPriceInTax(itemSalesPriceSubTotal.getOrderItem().getItemUnitPriceInTax());
            itemSalesPriceDto.setItemSalesPriceSubTotal(itemSalesPriceSubTotal.getItemSalesPriceSubTotal());
            itemSalesPriceDto.setItemSalesPriceSubTotalInTax(itemSalesPriceSubTotal.getItemSalesPriceSubTotalInTax());

            itemSalesPriceUseCaseDtoList.add(itemSalesPriceDto);
        }

        // calculationItemSalesPriceUseCaseDtoに設定する
        calculationItemSalesPriceUseCaseDto.setItemSalesPriceUseCaseDtoList(itemSalesPriceUseCaseDtoList);
        calculationItemSalesPriceUseCaseDto.setItemSalesPriceTotal(itemSalesPriceTotal.getItemPriceTotal());
        calculationItemSalesPriceUseCaseDto.setItemSalesPriceTotalInTax(itemSalesPriceTotal.getItemPriceTotalInTax());
    }
}