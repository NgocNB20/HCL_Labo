/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.order;

import jp.co.itechh.quad.ddd.domain.order.entity.ChangeOrderItemCountDomainParam;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemRevision;
import jp.co.itechh.quad.ddd.usecase.order.ChangeOrderItemCountForRevisionUseCaseParam;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemCountListUpdateRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.orderslip.presentation.api.param.UpdateOrderSlipForRevisionRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注文票Helperクラス
 *
 * @author kimura
 */
@Component
public class OrderSlipHelper {

    /**
     * 注文票エンティティから注文票レスポンスに変換
     *
     * @param entity 注文票エンティティ
     * @return response 注文票レスポンス
     */
    public OrderSlipResponse toOrderSlipResponse(OrderSlipEntity entity) {

        if (entity == null) {
            return null;
        }

        OrderSlipResponse response = new OrderSlipResponse();

        response.setOrderSlipId(entity.getOrderSlipId().getValue());
        response.setTransactionId(entity.getTransactionId());
        response.setCustomerId(entity.getCustomerId());
        response.setTotalItemCount(entity.countOrderItemCountTotal());
        response.setUserAgent(entity.getUserAgent());
        // 注文商品を詰め替える
        List<OrderSlipResponseItemList> itemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entity.getOrderItemList())) {
            for (int i = 0; i < entity.getOrderItemList().size(); i++) {
                OrderSlipResponseItemList item = new OrderSlipResponseItemList();
                item.setItemId(entity.getOrderItemList().get(i).getItemId());
                item.setOrderItemSeq(entity.getOrderItemList().get(i).getOrderItemSeq().getValue());
                item.setItemCount(entity.getOrderItemList().get(i).getOrderCount().getValue());
                item.setItemName(entity.getOrderItemList().get(i).getItemName());
                item.setUnitTitle1(entity.getOrderItemList().get(i).getUnitTitle1());
                item.setUnitValue1(entity.getOrderItemList().get(i).getUnitValue1());
                item.setUnitTitle2(entity.getOrderItemList().get(i).getUnitTitle2());
                item.setUnitValue2(entity.getOrderItemList().get(i).getUnitValue2());
                item.setJanCode(entity.getOrderItemList().get(i).getJanCode());
                item.setNoveltyGoodsType(entity.getOrderItemList().get(i).getNoveltyGoodsType().getValue());
                item.setOrderItemId(entity.getOrderItemList().get(i).getOrderItemId().getValue());
                itemList.add(item);
            }
        }
        response.setItemList(itemList);

        return response;
    }

    /**
     * 注文商品数量更新ユースケース用の注文商品パラメータリストに変換
     *
     * @param request 注文商品数量更新リクエスト
     * @return paramList 注文商品数量一括変更ユースケース用の注文商品パラメータリスト
     */
    public List<ChangeOrderItemCountDomainParam> toChangeOrderCountUseCaseParam(OrderItemCountListUpdateRequest request) {

        if (request == null) {
            return null;
        }

        List<ChangeOrderItemCountDomainParam> paramList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(request.getItemList())) {
            for (int i = 0; i < request.getItemList().size(); i++) {
                ChangeOrderItemCountDomainParam param = new ChangeOrderItemCountDomainParam();
                param.setItemId(request.getItemList().get(i).getItemId());
                param.setOrderCount(request.getItemList().get(i).getItemCount());
                paramList.add(param);
            }
        }

        return paramList;
    }

    /**
     * 改訂用注文商品数量変更ユースケースパラメータリストに変換
     *
     * @param request 改訂用注文票更新リクエスト
     * @return paramList 改訂用注文商品数量変更ユースケースパラメータリスト
     */
    public List<ChangeOrderItemCountForRevisionUseCaseParam> toChangeOrderCountUseCaseParam(
                    UpdateOrderSlipForRevisionRequest request) {

        if (request == null) {
            return null;
        }

        List<ChangeOrderItemCountForRevisionUseCaseParam> paramList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(request.getChangeItemList())) {
            for (int i = 0; i < request.getChangeItemList().size(); i++) {
                ChangeOrderItemCountForRevisionUseCaseParam param = new ChangeOrderItemCountForRevisionUseCaseParam();
                param.setOrderItemSeq(request.getChangeItemList().get(i).getOrderItemSeq());
                param.setOrderCount(request.getChangeItemList().get(i).getOrderCount());
                paramList.add(param);
            }
        }

        return paramList;
    }

    /**
     * 改訂用注文票レスポンスに変換
     *
     * @param orderSlipForRevisionEntity 改訂用注文票エンティティ
     * @return 改訂用注文票レスポンス
     */
    public OrderSlipForRevisionResponse toOrderSlipForRevisionResponse(OrderSlipForRevisionEntity orderSlipForRevisionEntity) {

        OrderSlipForRevisionResponse orderSlipForRevisionResponse = new OrderSlipForRevisionResponse();

        orderSlipForRevisionResponse.setOrderSlipRevisionId(
                        orderSlipForRevisionEntity.getOrderSlipRevisionId().getValue());
        orderSlipForRevisionResponse.setTransactionRevisionId(orderSlipForRevisionEntity.getTransactionRevisionId());
        orderSlipForRevisionResponse.setOrderSlipId(orderSlipForRevisionEntity.getOrderSlipId().getValue());
        orderSlipForRevisionResponse.setOrderStatus(orderSlipForRevisionEntity.getOrderStatus().name());
        orderSlipForRevisionResponse.setCustomerId(orderSlipForRevisionEntity.getCustomerId());
        orderSlipForRevisionResponse.setTransactionId(orderSlipForRevisionEntity.getTransactionId());
        orderSlipForRevisionResponse.setRegistDate(orderSlipForRevisionEntity.getRegistDate());

        List<OrderItemRevisionResponse> orderItemRevisionResponseList =
                        this.toOrderItemRevisionResponseList(orderSlipForRevisionEntity.getOrderItemRevisionList());
        orderSlipForRevisionResponse.setOrderItemRevisionList(orderItemRevisionResponseList);

        List<OrderItemResponse> orderItemResponseList =
                        this.toOrderItemResponseList(orderSlipForRevisionEntity.getOrderItemList());
        orderSlipForRevisionResponse.setOrderItemList(orderItemResponseList);

        return orderSlipForRevisionResponse;
    }

    /**
     * 改訂用注文商品 値オブジェクトレスポンスリストに変換
     *
     * @param orderItemRevisionList 改訂用注文商品 値オブジェクトリスト
     * @return paramList 改訂用注文商品 値オブジェクトレスポンスリスト
     */
    private List<OrderItemRevisionResponse> toOrderItemRevisionResponseList(List<OrderItemRevision> orderItemRevisionList) {

        return orderItemRevisionList.stream().map(orderItemRevision -> {

            OrderItemRevisionResponse orderItemRevisionResponse = new OrderItemRevisionResponse();

            orderItemRevisionResponse.setItemId(orderItemRevision.getItemId());
            orderItemRevisionResponse.setOrderCount(orderItemRevision.getOrderCount().getValue());
            orderItemRevisionResponse.setOrderItemSeq(orderItemRevision.getOrderItemSeq().getValue());
            orderItemRevisionResponse.setItemName(orderItemRevision.getItemName());
            orderItemRevisionResponse.setUnitTitle1(orderItemRevision.getUnitTitle1());
            orderItemRevisionResponse.setUnitTitle2(orderItemRevision.getUnitTitle2());
            orderItemRevisionResponse.setUnitValue1(orderItemRevision.getUnitValue1());
            orderItemRevisionResponse.setUnitValue2(orderItemRevision.getUnitValue2());
            orderItemRevisionResponse.setJanCode(orderItemRevision.getJanCode());
            orderItemRevisionResponse.setNoveltyGoodsType(orderItemRevision.getNoveltyGoodsType().getValue());
            orderItemRevisionResponse.setOrderItemId(orderItemRevision.getOrderItemId().getValue());

            return orderItemRevisionResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 注文商品値オブジェクトレスポンスリストに変換
     *
     * @param orderItemList 注文商品 値オブジェクトリスト
     * @return paramList 注文商品値オブジェクトレスポンスリスト
     */
    private List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItemList) {

        return orderItemList.stream().map(orderItem -> {

            OrderItemResponse orderItemResponse = new OrderItemResponse();

            orderItemResponse.setItemId(orderItem.getItemId());
            orderItemResponse.setItemName(orderItem.getItemName());
            orderItemResponse.setOrderCount(orderItem.getOrderCount().getValue());
            orderItemResponse.setOrderItemSeq(orderItem.getOrderItemSeq().getValue());
            orderItemResponse.setUnitTitle1(orderItem.getUnitTitle1());
            orderItemResponse.setUnitValue1(orderItem.getUnitValue1());
            orderItemResponse.setUnitTitle2(orderItem.getUnitTitle2());
            orderItemResponse.setUnitValue2(orderItem.getUnitValue2());
            orderItemResponse.setJanCode(orderItem.getJanCode());
            orderItemResponse.setNoveltyGoodsType(orderItem.getNoveltyGoodsType().getValue());
            orderItemResponse.setOrderItemId(orderItem.getOrderItemId().getValue());

            return orderItemResponse;
        }).collect(Collectors.toList());
    }
}