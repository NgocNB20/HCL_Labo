package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingItem;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingAdapterHelper {

    /**
     * 配送伝票に変換
     *
     * @param shippingSlipResponse 配送伝票レスポンス
     * @return 配送伝票
     */
    public ShippingSlip toShippingSlip(ShippingSlipResponse shippingSlipResponse) {

        if (ObjectUtils.isEmpty(shippingSlipResponse)) {
            return null;
        }

        ShippingSlip shippingSlip = new ShippingSlip();
        shippingSlip.setShippingAddressId(shippingSlipResponse.getShippingAddressId());

        if (StringUtils.isNotEmpty(shippingSlipResponse.getShippingMethodId())) {

            shippingSlip.setShippingMethodId(Integer.parseInt(shippingSlipResponse.getShippingMethodId()));
        }

        shippingSlip.setShippingMethodName(shippingSlipResponse.getShippingMethodName());
        shippingSlip.setShipmentStatusConfirmCode(shippingSlipResponse.getShipmentStatusConfirmCode());
        shippingSlip.setCompleteShipmentDate(shippingSlipResponse.getCompleteShipmentDate());
        shippingSlip.setReceiverTimeZone(shippingSlipResponse.getReceiverTimeZone());
        shippingSlip.setReceiverDate(shippingSlipResponse.getReceiverDate());

        if (shippingSlipResponse.getInvoiceNecessaryFlag() != null) {
            shippingSlip.setInvoiceNecessaryFlag(shippingSlipResponse.getInvoiceNecessaryFlag());
        }

        List<ShippingItem> shippingItemList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(shippingSlipResponse.getShippingItemList())) {
            shippingSlipResponse.getShippingItemList().forEach(item -> {
                ShippingItem shippingItem = new ShippingItem();

                shippingItem.setItemId(item.getItemId());
                shippingItem.setItemName(item.getItemName());
                shippingItem.setUnit1(item.getUnitValue1());
                shippingItem.setUnit2(item.getUnitValue2());

                if (item.getShippingCount() != null) {
                    shippingItem.setShippingCount(item.getShippingCount());
                }

                shippingItemList.add(shippingItem);
            });
        }
        shippingSlip.setShippingItemList(shippingItemList);

        return shippingSlip;
    }

}
