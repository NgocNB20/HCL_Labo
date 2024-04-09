package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderItemCountParam;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.orderslip.presentation.api.param.ChangeOrderItemCountForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注文アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderAdapterHelper {

    /**
     * 改訂用注文商品数量変更リクエストに変換.
     *
     * @param orderItemCountParamList 注文商品パラメータ
     * @return 改訂用注文商品数量変更リクエスト
     */
    public List<ChangeOrderItemCountForRevisionRequest> toChangeOrderItemCountForRevisionRequestList(List<OrderItemCountParam> orderItemCountParamList) {
        return orderItemCountParamList.stream().map(orderItemCountParam -> {
            ChangeOrderItemCountForRevisionRequest itemCountForRevisionRequest =
                            new ChangeOrderItemCountForRevisionRequest();
            itemCountForRevisionRequest.setOrderItemSeq(orderItemCountParam.getOrderItemNo());
            itemCountForRevisionRequest.setOrderCount(orderItemCountParam.getOrderCount());
            return itemCountForRevisionRequest;
        }).collect(Collectors.toList());
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

        List<OrderItemResponse> orderItemResponseList = orderSlipForRevisionResponse.getOrderItemList();
        List<OrderItemRevisionResponse> orderItemRevisionList = orderSlipForRevisionResponse.getOrderItemRevisionList();

        if (CollectionUtils.isNotEmpty(orderItemResponseList)) {
            List<OrderSlipItem> orderItemList = orderItemResponseList.stream().map(orderItemResponse ->
                toOrderSlipItem(
                    orderItemResponse.getOrderCount(),
                    orderItemResponse.getOrderItemSeq(),
                    orderItemResponse.getItemId(),
                    orderItemResponse.getItemName(),
                    orderItemResponse.getUnitValue1(),
                    orderItemResponse.getUnitValue2(),
                    orderItemResponse.getUnitTitle1(),
                    orderItemResponse.getUnitTitle2(),
                    orderItemResponse.getJanCode(),
                    orderItemResponse.getNoveltyGoodsType(),
                    orderItemResponse.getOrderItemId())
            ).collect(Collectors.toList());

            orderSlipForRevision.setOrderItemList(orderItemList);
        }

        if (CollectionUtils.isNotEmpty(orderItemRevisionList)) {
            List<OrderSlipItem> revisionOrderItemList = orderItemRevisionList.stream().map(orderItemRevision ->
                toOrderSlipItem(
                    orderItemRevision.getOrderCount(),
                    orderItemRevision.getOrderItemSeq(),
                    orderItemRevision.getItemId(),
                    orderItemRevision.getItemName(),
                    orderItemRevision.getUnitValue1(),
                    orderItemRevision.getUnitValue2(),
                    orderItemRevision.getUnitTitle1(),
                    orderItemRevision.getUnitTitle2(),
                    orderItemRevision.getJanCode(),
                    orderItemRevision.getNoveltyGoodsType(),
                    orderItemRevision.getOrderItemId())
            ).collect(Collectors.toList());

            orderSlipForRevision.setRevisionOrderItemList(revisionOrderItemList);
        }

        return orderSlipForRevision;
    }

    /**
     * 注文商品に変換.
     *
     * @param orderCount       注文数量
     * @param orderItemSeq     注文商品連番
     * @param itemId           商品ID
     * @param itemName         商品名
     * @param unitValue1       規格値1
     * @param unitValue2       規格値2
     * @param unitTitle1       規格タイトル1
     * @param unitTitle2       規格タイトル2
     * @param janCode          JANコード
     * @param noveltyGoodsType ノベルティ商品フラグ
     * @param orderItemId      注文商品ID
     * @return 注文商品
     */
    public OrderSlipItem toOrderSlipItem(Integer orderCount, Integer orderItemSeq, String itemId, String itemName,
                                          String unitValue1, String unitValue2, String unitTitle1, String unitTitle2,
                                          String janCode, String noveltyGoodsType, String orderItemId) {
        OrderSlipItem orderSlipItem = new OrderSlipItem();

        orderSlipItem.setItemCount(orderCount);
        orderSlipItem.setOrderItemSeq(orderItemSeq);
        orderSlipItem.setItemId(itemId);
        orderSlipItem.setItemName(itemName);
        orderSlipItem.setUnitValue1(unitValue1);
        orderSlipItem.setUnitValue2(unitValue2);
        orderSlipItem.setUnitTitle1(unitTitle1);
        orderSlipItem.setUnitTitle2(unitTitle2);
        orderSlipItem.setJanCode(janCode);
        orderSlipItem.setNoveltyGoodsType(noveltyGoodsType);
        orderSlipItem.setOrderItemId(orderItemId);

        return orderSlipItem;
    }
}