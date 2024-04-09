package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 注文アダプタークラスHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSlipAdapterHelper {

    /**
     * 注文票に変換.
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return 注文票
     */
    public OrderSlip toOrderSlip(OrderSlipResponse orderSlipResponse) {

        if (ObjectUtils.isEmpty(orderSlipResponse)) {
            return null;
        }

        OrderSlip orderSlip = new OrderSlip();

        orderSlip.setOrderSlipId(orderSlipResponse.getOrderSlipId());
        orderSlip.setTransactionId(orderSlipResponse.getTransactionId());
        orderSlip.setUserAgent(orderSlipResponse.getUserAgent());

        if (orderSlipResponse.getTotalItemCount() != null) {
            orderSlip.setTotalItemCount(orderSlipResponse.getTotalItemCount());
        }

        if (orderSlipResponse.getItemList() != null) {
            List<OrderSlipItem> orderSlipItems = new ArrayList<>();
            orderSlipResponse.getItemList().forEach(orderSlipResponseItem -> {

                OrderSlipItem orderSlipItem = new OrderSlipItem();

                if (orderSlipResponseItem.getItemCount() != null) {
                    orderSlipItem.setItemCount(orderSlipResponseItem.getItemCount());
                }

                if (orderSlipResponseItem.getOrderItemSeq() != null) {
                    orderSlipItem.setOrderItemSeq(orderSlipResponseItem.getOrderItemSeq());
                }

                orderSlipItem.setItemId(orderSlipResponseItem.getItemId());
                orderSlipItem.setItemName(orderSlipResponseItem.getItemName());
                orderSlipItem.setUnitValue1(orderSlipResponseItem.getUnitValue1());
                orderSlipItem.setUnitValue2(orderSlipResponseItem.getUnitValue2());
                orderSlipItem.setUnitTitle1(orderSlipResponseItem.getUnitTitle1());
                orderSlipItem.setUnitTitle2(orderSlipResponseItem.getUnitTitle2());
                orderSlipItem.setJanCode(orderSlipResponseItem.getJanCode());
                orderSlipItem.setNoveltyGoodsType(orderSlipResponseItem.getNoveltyGoodsType());
                orderSlipItem.setOrderItemId(orderSlipResponseItem.getOrderItemId());

                orderSlipItems.add(orderSlipItem);
            });
            orderSlip.setItemList(orderSlipItems);
        }
        return orderSlip;
    }
}