package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 注文票アダプタークラスHelperクラス
 */
@Component
public class OrderSlipAdapterHelper {

    /**
     * OrderSlipのドラフトへ
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return 下書き注文票
     */
    public OrderSlip toDraftOrderSlip(OrderSlipResponse orderSlipResponse) {

        if (ObjectUtils.isEmpty(orderSlipResponse)) {
            return null;
        }

        OrderSlip draftOrderSlip = new OrderSlip();

        draftOrderSlip.setOrderSlipId(orderSlipResponse.getOrderSlipId());
        draftOrderSlip.setOrderStatus("DRAFT");

        if (orderSlipResponse.getTotalItemCount() != null) {
            draftOrderSlip.setTotalItemCount(orderSlipResponse.getTotalItemCount());
        }

        List<OrderSlipResponseItemList> orderSlipResponseItemLists = orderSlipResponse.getItemList();

        if (orderSlipResponseItemLists != null) {

            List<OrderSlipItem> orderSlipItems = orderSlipResponseItemLists.stream().map(listItem -> {
                OrderSlipItem orderSlipItem = new OrderSlipItem();
                orderSlipItem.setItemId(listItem.getItemId());
                orderSlipItem.setItemCount(listItem.getItemCount() != null ? listItem.getItemCount() : 0);
                orderSlipItem.setOrderItemSeq(listItem.getOrderItemSeq());
                return orderSlipItem;
            }).collect(Collectors.toList());

            draftOrderSlip.setItemList(orderSlipItems);
        }

        draftOrderSlip.setTransactionId(orderSlipResponse.getTransactionId());

        return draftOrderSlip;
    }

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

        if (orderSlipResponse.getTotalItemCount() != null) {
            orderSlip.setTotalItemCount(orderSlipResponse.getTotalItemCount());
        }

        if (orderSlipResponse.getItemList() != null) {
            List<OrderSlipItem> orderSlipItems = new ArrayList<>();
            orderSlipResponse.getItemList().forEach(OrderSlipResponseItem -> {

                OrderSlipItem orderSlipItem = new OrderSlipItem();

                if (OrderSlipResponseItem.getItemCount() != null) {
                    orderSlipItem.setItemCount(OrderSlipResponseItem.getItemCount());
                }
                orderSlipItem.setItemId(OrderSlipResponseItem.getItemId());

                orderSlipItems.add(orderSlipItem);
            });
            orderSlip.setItemList(orderSlipItems);
        }
        return orderSlip;
    }

    /**
     * 改訂用注文票に変換.
     *
     * @param orderSlipForRevisionResponse 改訂用注文票レスポンス
     * @return 改訂用注文票
     */
    public OrderSlipForRevision toOrderSlipForRevision(OrderSlipForRevisionResponse orderSlipForRevisionResponse) {

        if (ObjectUtils.isEmpty(orderSlipForRevisionResponse)) {
            return null;
        }

        OrderSlipForRevision orderSlipForRevision = new OrderSlipForRevision();

        orderSlipForRevision.setOrderSlipRevisionId(orderSlipForRevisionResponse.getOrderSlipRevisionId());
        orderSlipForRevision.setTransactionRevisionId(orderSlipForRevisionResponse.getTransactionRevisionId());
        orderSlipForRevision.setOrderSlipId(orderSlipForRevisionResponse.getOrderSlipId());
        orderSlipForRevision.setOrderStatus(orderSlipForRevisionResponse.getOrderStatus());

        // 改訂元注文商品
        List<OrderSlipItem> orderSlipItemList = new ArrayList<>();

        if (orderSlipForRevisionResponse != null && !CollectionUtils.isEmpty(
                        orderSlipForRevisionResponse.getOrderItemList())) {
            for (OrderItemResponse item : orderSlipForRevisionResponse.getOrderItemList()) {
                OrderSlipItem orderSlipItem = new OrderSlipItem();
                orderSlipItem.setItemId(item.getItemId());
                orderSlipItem.setItemCount(Objects.requireNonNull(item.getOrderCount()));
                orderSlipItem.setOrderItemSeq(Objects.requireNonNull(item.getOrderItemSeq()));

                orderSlipItemList.add(orderSlipItem);
            }
            orderSlipForRevision.setItemList(orderSlipItemList);
        }

        // 改訂用注文商品
        List<OrderSlipItem> orderSlipRevisionItemList = new ArrayList<>();

        if (orderSlipForRevisionResponse != null && !CollectionUtils.isEmpty(
                        orderSlipForRevisionResponse.getOrderItemRevisionList())) {
            for (OrderItemRevisionResponse item : orderSlipForRevisionResponse.getOrderItemRevisionList()) {
                OrderSlipItem orderSlipItem = new OrderSlipItem();
                orderSlipItem.setItemId(item.getItemId());
                orderSlipItem.setItemCount(Objects.requireNonNull(item.getOrderCount()));
                orderSlipItem.setOrderItemSeq(Objects.requireNonNull(item.getOrderItemSeq()));

                orderSlipRevisionItemList.add(orderSlipItem);
            }
            orderSlipForRevision.setOrderItemRevisionList(orderSlipRevisionItemList);
        }

        orderSlipForRevision.setCustomerId(orderSlipForRevisionResponse.getCustomerId());
        orderSlipForRevision.setTransactionId(orderSlipForRevision.getTransactionId());
        orderSlipForRevision.setRegistDate(orderSlipForRevisionResponse.getRegistDate());
        return orderSlipForRevision;
    }
}