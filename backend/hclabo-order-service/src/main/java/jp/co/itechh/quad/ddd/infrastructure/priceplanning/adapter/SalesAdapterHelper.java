package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.ItemPrice;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.salesslip.presentation.api.param.CalcAndCheckSalesSlipForRevisionResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 警告メッセージの変換
     *
     * @param calcAndCheckSalesSlipForRevisionResponse 改訂用販売伝票計算&チェックレスポンス
     * @return transactionWarningMessage 警告メッセージ（取引）
     */
    public Map<String, List<WarningContent>> toTransactionWarningMessageMap(CalcAndCheckSalesSlipForRevisionResponse calcAndCheckSalesSlipForRevisionResponse) {

        if (ObjectUtils.isEmpty(calcAndCheckSalesSlipForRevisionResponse) || MapUtils.isEmpty(
                        calcAndCheckSalesSlipForRevisionResponse.getWarningMessage())) {
            return null;
        }

        Map<String, List<WarningContent>> transactionWarningMessage = new HashMap<>();
        List<WarningContent> warningMessageList = new ArrayList<>();

        calcAndCheckSalesSlipForRevisionResponse.getWarningMessage().forEach((key, values) -> {
            for (jp.co.itechh.quad.salesslip.presentation.api.param.WarningContent warningContent : values) {
                WarningContent content = new WarningContent();
                content.setCode(warningContent.getCode());
                content.setMessage(warningContent.getMessage());

                warningMessageList.add(content);
            }
            transactionWarningMessage.put(key, warningMessageList);
        });

        return transactionWarningMessage;
    }
}