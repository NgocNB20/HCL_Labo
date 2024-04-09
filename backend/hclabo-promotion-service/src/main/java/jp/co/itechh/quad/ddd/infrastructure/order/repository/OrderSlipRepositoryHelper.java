package jp.co.itechh.quad.ddd.infrastructure.order.repository;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderCount;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeq;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderSlipDbEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注文票リポジトリHelperクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSlipRepositoryHelper {

    /**
     * 注文票Dbエンティティに変換
     *
     * @param orderSlipEntity 注文票エンティティ
     * @return 注文票Dbエンティティ
     */
    public OrderSlipDbEntity toOrderSlipDbEntity(OrderSlipEntity orderSlipEntity) {

        if (orderSlipEntity == null) {
            return null;
        }

        OrderSlipDbEntity orderSlipDbEntity = new OrderSlipDbEntity();

        orderSlipDbEntity.setOrderSlipId(orderSlipEntity.getOrderSlipId().getValue());
        orderSlipDbEntity.setOrderStatus(orderSlipEntity.getOrderStatus().name());
        orderSlipDbEntity.setCustomerId(orderSlipEntity.getCustomerId());
        orderSlipDbEntity.setTransactionId(orderSlipEntity.getTransactionId());
        orderSlipDbEntity.setRegistDate(orderSlipEntity.getRegistDate());
        orderSlipDbEntity.setUserAgent(orderSlipEntity.getUserAgent());

        return orderSlipDbEntity;
    }

    /**
     * 注文商品リストに変換
     *
     * @param orderItemDbEntityList 注文商品DbEntityリスト
     * @return 注文商品リスト
     */
    public List<OrderItem> toOrderItemList(List<OrderItemDbEntity> orderItemDbEntityList) {

        if (CollectionUtils.isEmpty(orderItemDbEntityList)) {
            return null;
        }

        return orderItemDbEntityList.stream().map(orderItemDbEntity -> {
            OrderCount orderCount = new OrderCount(orderItemDbEntity.getOrderCount());
            OrderItemSeq orderItemSeq = new OrderItemSeq(orderItemDbEntity.getOrderItemSeq());
            return new OrderItem(orderItemDbEntity.getItemId(), orderItemSeq, orderCount,
                                 orderItemDbEntity.getItemName(), orderItemDbEntity.getUnitTitle1(),
                                 orderItemDbEntity.getUnitValue1(), orderItemDbEntity.getUnitTitle2(),
                                 orderItemDbEntity.getUnitValue2(), orderItemDbEntity.getJanCode(),
                                 EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                               orderItemDbEntity.getNoveltyGoodsType()),
                                 new OrderItemId(orderItemDbEntity.getOrderItemId())
            );
        }).collect(Collectors.toList());
    }

    /**
     * 注文商品DbEntityリストに変換
     *
     * @param orderItemList 注文商品リスト
     * @return 注文商品DbEntityリスト
     */
    public List<OrderItemDbEntity> toOrderItemDbEntity(List<OrderItem> orderItemList) {

        if (CollectionUtils.isEmpty(orderItemList)) {
            return new ArrayList<>();
        }

        return orderItemList.stream().map(orderItem -> {
            OrderItemDbEntity orderItemDbEntity = new OrderItemDbEntity();
            orderItemDbEntity.setItemId(orderItem.getItemId());
            orderItemDbEntity.setOrderItemSeq(orderItem.getOrderItemSeq().getValue());
            orderItemDbEntity.setOrderCount(orderItem.getOrderCount().getValue());
            orderItemDbEntity.setItemName(orderItem.getItemName());
            orderItemDbEntity.setUnitTitle1(orderItem.getUnitTitle1());
            orderItemDbEntity.setUnitValue1(orderItem.getUnitValue1());
            orderItemDbEntity.setUnitTitle2(orderItem.getUnitTitle2());
            orderItemDbEntity.setUnitValue2(orderItem.getUnitValue2());
            orderItemDbEntity.setJanCode(orderItem.getJanCode());
            orderItemDbEntity.setNoveltyGoodsType(EnumTypeUtil.getValue(orderItem.getNoveltyGoodsType()));
            orderItemDbEntity.setOrderItemId(orderItem.getOrderItemId().getValue());
            return orderItemDbEntity;
        }).collect(Collectors.toList());
    }

    /**
     * 注文商品DbEntityに変換
     *
     * @param orderItem   注文商品
     * @param orderSlipId 注文票ID
     * @return 注文商品DbEntity
     */
    public OrderItemDbEntity toOrderItemDbEntity(OrderItem orderItem, String orderSlipId) {

        if (orderItem == null) {
            return null;
        }

        OrderItemDbEntity orderItemDbEntity = new OrderItemDbEntity();

        orderItemDbEntity.setItemId(orderItem.getItemId());
        orderItemDbEntity.setOrderItemSeq(orderItem.getOrderItemSeq().getValue());
        orderItemDbEntity.setOrderCount(orderItem.getOrderCount().getValue());
        orderItemDbEntity.setItemName(orderItem.getItemName());
        orderItemDbEntity.setUnitTitle1(orderItem.getUnitTitle1());
        orderItemDbEntity.setUnitValue1(orderItem.getUnitValue1());
        orderItemDbEntity.setUnitTitle2(orderItem.getUnitTitle2());
        orderItemDbEntity.setUnitValue2(orderItem.getUnitValue2());
        orderItemDbEntity.setJanCode(orderItem.getJanCode());
        orderItemDbEntity.setOrderSlipId(orderSlipId);
        orderItemDbEntity.setNoveltyGoodsType(EnumTypeUtil.getValue(orderItem.getNoveltyGoodsType()));
        orderItemDbEntity.setOrderItemId(orderItem.getOrderItemId().getValue());

        return orderItemDbEntity;
    }
}