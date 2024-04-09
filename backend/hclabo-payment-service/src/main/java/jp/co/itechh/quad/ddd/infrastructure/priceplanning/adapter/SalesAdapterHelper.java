package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.ItemPrice;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlipForRevision;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 販売アダプター実装Helperクラス
 */
@Component
public class SalesAdapterHelper {

    /**
     * 販売伝票に変換
     *
     * @param response 販売伝票レスポンス
     * @return 販売伝票
     */
    public SalesSlip toSalesSlip(SalesSlipResponse response) {

        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        SalesSlip salesSlip = new SalesSlip();

        List<ItemPrice> itemPriceList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(response.getItemPriceSubTotalList())) {
            response.getItemPriceSubTotalList().forEach(item -> {
                ItemPrice itemPrice = new ItemPrice();

                itemPrice.setItemId(item.getItemId());
                itemPrice.setItemUnitPrice(item.getItemUnitPrice());
                itemPrice.setItemPriceSubTotal(item.getItemPriceSubTotal());

                itemPriceList.add(itemPrice);
            });
        }

        salesSlip.setItemPriceList(itemPriceList);
        salesSlip.setItemSalesPriceTotal(response.getItemSalesPriceTotal());
        salesSlip.setCarriage(response.getCarriage());
        salesSlip.setCouponPaymentPrice(response.getCouponPaymentPrice());
        salesSlip.setCommission(response.getCommission());
        salesSlip.setBillingAmount(response.getBillingAmount());
        salesSlip.setStandardTaxTargetPrice(response.getStandardTaxTargetPrice());
        salesSlip.setReducedTaxTargetPrice(response.getReducedTaxTargetPrice());
        salesSlip.setStandardTax(response.getStandardTax());
        salesSlip.setReducedTax(response.getReducedTax());

        return salesSlip;
    }

    /**
     * 改訂用販売伝票に変換
     *
     * @param response 改訂用取引IDに紐づく改訂用販売伝票取得レスポンス
     * @return 改訂用販売伝票
     */
    public SalesSlipForRevision toSalesSlipForRevision(GetSalesSlipForRevisionByTransactionRevisionIdResponse response) {

        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        SalesSlipForRevision salesSlipForRevision = new SalesSlipForRevision();

        List<ItemPrice> itemPriceList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(response.getItemPriceSubTotal())) {

            response.getItemPriceSubTotal().forEach(item -> {
                ItemPrice itemPrice = new ItemPrice();

                itemPrice.setItemId(item.getItemId());
                itemPrice.setItemUnitPrice(item.getItemUnitPrice());
                itemPrice.setItemPriceSubTotal(item.getItemPriceSubTotal());

                itemPriceList.add(itemPrice);
            });

        }

        salesSlipForRevision.setSalesSlipRevisionId(response.getSalesSlipRevisionId());
        salesSlipForRevision.setSalesSlipId(response.getSalesSlipId());
        salesSlipForRevision.setTransactionId(response.getTransactionId());
        salesSlipForRevision.setItemPriceList(itemPriceList);
        salesSlipForRevision.setItemSalesPriceTotal(response.getItemPurchasePriceTotal());
        salesSlipForRevision.setCarriage(response.getCarriage());

        if (!ObjectUtils.isEmpty(response.getApplyCouponResponse())) {
            salesSlipForRevision.setCouponPaymentPrice(response.getApplyCouponResponse().getCouponPaymentPrice());
        }

        salesSlipForRevision.setCommission(response.getCommission());
        salesSlipForRevision.setBillingAmount(response.getBillingAmount());

        if (!ObjectUtils.isEmpty(response.getSalesPriceConsumptionTaxResponse())) {

            salesSlipForRevision.setStandardTaxTargetPrice(
                            response.getSalesPriceConsumptionTaxResponse().getStandardTaxTargetPrice());
            salesSlipForRevision.setReducedTaxTargetPrice(
                            response.getSalesPriceConsumptionTaxResponse().getReducedTax());
            salesSlipForRevision.setStandardTax(response.getSalesPriceConsumptionTaxResponse().getStandardTax());
            salesSlipForRevision.setReducedTax(response.getSalesPriceConsumptionTaxResponse().getReducedTax());
        }

        return salesSlipForRevision;
    }
}