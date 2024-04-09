package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderItem;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 注文アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSlipAdapterHelper {

    /**
     * 注文票に変換
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return 注文票
     */
    public OrderSlip toOrderSlipFromOrderSlipResponse(OrderSlipResponse orderSlipResponse) {

        if (orderSlipResponse == null) {
            return null;
        }

        OrderSlip orderSlip = new OrderSlip();

        orderSlip.setOrderSlipId(orderSlipResponse.getOrderSlipId());
        orderSlip.setTransactionId(orderSlipResponse.getTransactionId());
        orderSlip.setCustomerId(orderSlipResponse.getCustomerId());

        // 注文商品を詰め替える
        List<OrderItem> orderItemList = toOrderItemListFromOrderSlipResponseItemList(orderSlipResponse.getItemList());
        orderSlip.setOrderItemList(orderItemList);

        return orderSlip;
    }

    /**
     * 注文商品リストに変換
     *
     * @param itemList 注文伝票回答項目リスト
     * @return 注文商品リスト
     */
    public List<OrderItem> toOrderItemListFromOrderSlipResponseItemList(List<OrderSlipResponseItemList> itemList) {

        if (itemList == null) {
            return null;
        }

        List<OrderItem> orderItemList = new ArrayList<>();

        itemList.forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(item.getItemId());
            orderItem.setOrderItemSeq(item.getOrderItemSeq());
            orderItem.setOrderCount(item.getItemCount());
            orderItem.setItemName(item.getItemName());
            orderItem.setUnitTitle1(item.getUnitTitle1());
            orderItem.setUnitTitle2(item.getUnitTitle2());
            orderItem.setUnitValue1(item.getUnitValue1());
            orderItem.setUnitValue2(item.getUnitValue2());
            HTypeNoveltyGoodsType noveltyGoodsType =
                            EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class, item.getNoveltyGoodsType());
            orderItem.setNoveltyGoodsType(noveltyGoodsType);
            orderItemList.add(orderItem);
        });

        return orderItemList;
    }

    /**
     * 注文票に変換
     *
     * @param orderSlipForRevisionResponse 改訂用注文票レスポンス
     * @return 注文票
     */
    public OrderSlip toOrderSlipFromOrderSlipForRevisionResponse(OrderSlipForRevisionResponse orderSlipForRevisionResponse) {

        if (orderSlipForRevisionResponse == null) {
            return null;
        }

        OrderSlip orderSlip = new OrderSlip();

        orderSlip.setOrderSlipId(orderSlipForRevisionResponse.getOrderSlipId());
        orderSlip.setTransactionId(orderSlipForRevisionResponse.getTransactionId());
        orderSlip.setCustomerId(orderSlipForRevisionResponse.getCustomerId());
        orderSlip.setOrderStatus(orderSlipForRevisionResponse.getOrderStatus());
        orderSlip.setRegistDate(orderSlipForRevisionResponse.getRegistDate());

        // 注文商品
        List<OrderItem> orderItemList =
                        toOrderItemListFromOrderItemResponseItemList(orderSlipForRevisionResponse.getOrderItemList());
        orderSlip.setOrderItemList(orderItemList);

        // 改訂用注文商品
        List<OrderItem> orderItemRevisionList =
                        toOrderItemList(orderSlipForRevisionResponse.getOrderItemRevisionList());
        orderSlip.setRevisionOrderItemList(orderItemRevisionList);

        return orderSlip;
    }

    /**
     * 注文商品リストに変換
     *
     * @param itemList 注文伝票回答項目リスト
     * @return 注文商品リスト
     */
    public List<OrderItem> toOrderItemListFromOrderItemResponseItemList(List<OrderItemResponse> itemList) {

        if (itemList == null) {
            return null;
        }

        List<OrderItem> orderItemList = new ArrayList<>();

        itemList.forEach(item -> {
            OrderItem orderItem = new OrderItem();

            orderItem.setItemId(item.getItemId());
            orderItem.setOrderCount(item.getOrderCount());
            orderItem.setOrderItemSeq(item.getOrderItemSeq());
            orderItem.setItemName(item.getItemName());
            orderItem.setUnitTitle1(item.getUnitTitle1());
            orderItem.setUnitTitle2(item.getUnitTitle2());
            orderItem.setUnitValue1(item.getUnitValue1());
            orderItem.setUnitValue2(item.getUnitValue2());
            HTypeNoveltyGoodsType noveltyGoodsType =
                            EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class, item.getNoveltyGoodsType());
            orderItem.setNoveltyGoodsType(noveltyGoodsType);

            orderItemList.add(orderItem);
        });

        return orderItemList;
    }

    /**
     * 改訂用注文商品リストに変換
     *
     * @param itemRevisionList
     * @return 注文商品リスト
     */
    public List<OrderItem> toOrderItemList(List<OrderItemRevisionResponse> itemRevisionList) {

        if (itemRevisionList == null) {
            return null;
        }

        List<OrderItem> orderItemList = new ArrayList<>();

        itemRevisionList.forEach(item -> {
            OrderItem orderItem = new OrderItem();

            orderItem.setItemId(item.getItemId());
            orderItem.setOrderCount(item.getOrderCount());
            orderItem.setOrderItemSeq(item.getOrderItemSeq());
            orderItem.setItemName(item.getItemName());
            orderItem.setUnitTitle1(item.getUnitTitle1());
            orderItem.setUnitTitle2(item.getUnitTitle2());
            orderItem.setUnitValue1(item.getUnitValue1());
            orderItem.setUnitValue2(item.getUnitValue2());
            HTypeNoveltyGoodsType noveltyGoodsType =
                            EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class, item.getNoveltyGoodsType());
            orderItem.setNoveltyGoodsType(noveltyGoodsType);

            orderItemList.add(orderItem);
        });

        return orderItemList;
    }

}