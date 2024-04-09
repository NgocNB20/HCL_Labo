package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.ItemPrice;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 販売アダプターHelperクラス
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

        salesSlip.setCarriage(response.getCarriage());
        salesSlip.setCouponSeq(response.getCouponSeq());
        salesSlip.setCouponPaymentPrice(response.getCouponPaymentPrice());
        salesSlip.setCommission(response.getCommission());
        salesSlip.setBillingAmount(response.getBillingAmount());
        salesSlip.setStandardTaxTargetPrice(response.getStandardTaxTargetPrice());
        salesSlip.setReducedTaxTargetPrice(response.getReducedTaxTargetPrice());
        salesSlip.setStandardTax(response.getStandardTax());
        salesSlip.setReducedTax(response.getReducedTax());
        salesSlip.setCouponVersionNo(response.getCouponVersionNo());
        salesSlip.setCouponUseFlag(response.getCouponUseFlag());

        List<AdjustmentAmount> adjustmentAmountList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(response.getAdjustmentAmountList())) {
            response.getAdjustmentAmountList().forEach(item -> {
                AdjustmentAmount adjustmentAmount = new AdjustmentAmount();
                adjustmentAmount.setAdjustName(item.getAdjustName());

                if (item.getAdjustPrice() != null) {
                    adjustmentAmount.setAdjustPrice(item.getAdjustPrice());
                }

                adjustmentAmountList.add(adjustmentAmount);

            });
        }

        salesSlip.setAdjustmentAmountList(adjustmentAmountList);

        List<ItemPrice> itemPriceList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(response.getItemPriceSubTotalList())) {
            response.getItemPriceSubTotalList().forEach(item -> {
                ItemPrice itemPrice = new ItemPrice();

                itemPrice.setItemId(item.getItemId());
                itemPrice.setItemUnitPrice(item.getItemUnitPrice());
                itemPrice.setItemPriceSubTotal(item.getItemPriceSubTotal());
                itemPrice.setItemCount(item.getItemCount());
                itemPrice.setItemTaxRate(item.getItemTaxRate());
                itemPrice.setSalesItemSeq(item.getSalesItemSeq());

                itemPriceList.add(itemPrice);
            });
        }

        salesSlip.setItemPriceList(itemPriceList);
        salesSlip.setGoodsPriceTotal(response.getItemSalesPriceTotal());
        salesSlip.setCouponName(response.getCouponName());

        return salesSlip;
    }
}
