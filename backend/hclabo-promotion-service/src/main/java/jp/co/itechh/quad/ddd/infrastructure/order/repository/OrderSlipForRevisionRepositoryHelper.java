package jp.co.itechh.quad.ddd.infrastructure.order.repository;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderCount;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemRevision;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeq;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemOriginRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderSlipForRevisionDbEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 改訂用注文票リポジトリHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSlipForRevisionRepositoryHelper {

    /**
     * 改訂用注文票Dbエンティティに変換
     *
     * @param orderSlipForRevisionEntity 改訂用注文票エンティティ
     * @return 改訂用注文票Dbエンティティ
     */
    public OrderSlipForRevisionDbEntity toOrderSlipForRevisionDbEntity(OrderSlipForRevisionEntity orderSlipForRevisionEntity) {

        if (orderSlipForRevisionEntity == null) {
            return null;
        }

        OrderSlipForRevisionDbEntity orderSlipForRevisionDbEntity = new OrderSlipForRevisionDbEntity();

        orderSlipForRevisionDbEntity.setOrderSlipRevisionId(
                        orderSlipForRevisionEntity.getOrderSlipRevisionId().getValue());
        orderSlipForRevisionDbEntity.setTransactionRevisionId(orderSlipForRevisionEntity.getTransactionRevisionId());
        orderSlipForRevisionDbEntity.setOrderSlipId(orderSlipForRevisionEntity.getOrderSlipId().getValue());
        orderSlipForRevisionDbEntity.setOrderStatus(orderSlipForRevisionEntity.getOrderStatus().name());
        orderSlipForRevisionDbEntity.setCustomerId(orderSlipForRevisionEntity.getCustomerId());
        orderSlipForRevisionDbEntity.setTransactionId(orderSlipForRevisionEntity.getTransactionId());
        orderSlipForRevisionDbEntity.setRegistDate(orderSlipForRevisionEntity.getRegistDate());

        return orderSlipForRevisionDbEntity;
    }

    /**
     * 改訂用注文商品DbEntityリストに変換
     *
     * @param orderItemRevisionList
     * @param orderSlipRevisionId
     * @return 改訂用注文商品DbEntityリスト
     */
    public List<OrderItemOriginRevisionDbEntity> toOrderItemOriginRevisionDbEntityList(List<OrderItemRevision> orderItemRevisionList,
                                                                                       String orderSlipRevisionId) {

        if (CollectionUtils.isEmpty(orderItemRevisionList)) {
            return null;
        }

        return orderItemRevisionList.stream().map(orderItemRevision -> {
            OrderItemOriginRevisionDbEntity originRevisionDb = new OrderItemOriginRevisionDbEntity();

            originRevisionDb.setItemId(orderItemRevision.getItemId());
            originRevisionDb.setOrderItemSeq(orderItemRevision.getOrderItemSeq().getValue());
            originRevisionDb.setOrderCount(orderItemRevision.getOrderCount().getValue());
            originRevisionDb.setOrderSlipRevisionId(orderSlipRevisionId);
            originRevisionDb.setItemName(orderItemRevision.getItemName());
            originRevisionDb.setUnitTitle1(orderItemRevision.getUnitTitle1());
            originRevisionDb.setUnitValue1(orderItemRevision.getUnitValue1());
            originRevisionDb.setUnitTitle2(orderItemRevision.getUnitTitle2());
            originRevisionDb.setUnitValue2(orderItemRevision.getUnitValue2());
            originRevisionDb.setJanCode(orderItemRevision.getJanCode());
            originRevisionDb.setNoveltyGoodsType(EnumTypeUtil.getValue(orderItemRevision.getNoveltyGoodsType()));
            originRevisionDb.setOrderItemId(orderItemRevision.getOrderItemId().getValue());

            return originRevisionDb;
        }).collect(Collectors.toList());
    }

    /**
     * 注文商品DbEntityクラスリストに変換
     *
     * @param orderItemList
     * @param orderSlipRevisionId
     * @return 注文商品DbEntityクラスリスト
     */
    public List<OrderItemRevisionDbEntity> toOrderItemRevisionDbEntityList(List<OrderItem> orderItemList,
                                                                           String orderSlipRevisionId) {

        if (CollectionUtils.isEmpty(orderItemList)) {
            return null;
        }

        return orderItemList.stream().map(orderItem -> {
            OrderItemRevisionDbEntity originRevisionDb = new OrderItemRevisionDbEntity();

            originRevisionDb.setItemId(orderItem.getItemId());
            originRevisionDb.setOrderItemSeq(orderItem.getOrderItemSeq().getValue());
            originRevisionDb.setOrderCount(orderItem.getOrderCount().getValue());
            originRevisionDb.setItemName(orderItem.getItemName());
            originRevisionDb.setUnitTitle1(orderItem.getUnitTitle1());
            originRevisionDb.setUnitValue1(orderItem.getUnitValue1());
            originRevisionDb.setUnitTitle2(orderItem.getUnitTitle2());
            originRevisionDb.setUnitValue2(orderItem.getUnitValue2());
            originRevisionDb.setJanCode(orderItem.getJanCode());
            originRevisionDb.setOrderSlipRevisionId(orderSlipRevisionId);
            originRevisionDb.setNoveltyGoodsType(EnumTypeUtil.getValue(orderItem.getNoveltyGoodsType()));
            originRevisionDb.setOrderItemId(orderItem.getOrderItemId().getValue());

            return originRevisionDb;
        }).collect(Collectors.toList());
    }

    /**
     * 改訂用注文商品リストに変換
     *
     * @param orderItemRevisionDbEntityList
     * @return 改訂用注文商品リスト
     */
    public List<OrderItemRevision> toOrderItemRevisionList(List<OrderItemRevisionDbEntity> orderItemRevisionDbEntityList) {

        if (CollectionUtils.isEmpty(orderItemRevisionDbEntityList)) {
            return null;
        }

        return orderItemRevisionDbEntityList.stream().map(orderItemRevisionDb -> {
            OrderCount orderCount = new OrderCount(orderItemRevisionDb.getOrderCount());
            OrderItemSeq orderItemSeq = new OrderItemSeq(orderItemRevisionDb.getOrderItemSeq());
            return new OrderItemRevision(orderItemRevisionDb.getItemId(), orderItemSeq, orderCount,
                                         orderItemRevisionDb.getItemName(), orderItemRevisionDb.getUnitTitle1(),
                                         orderItemRevisionDb.getUnitValue1(), orderItemRevisionDb.getUnitTitle2(),
                                         orderItemRevisionDb.getUnitValue2(), orderItemRevisionDb.getJanCode(),
                                         EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                                       orderItemRevisionDb.getNoveltyGoodsType()),
                                         new OrderItemId(orderItemRevisionDb.getOrderItemId())
            );
        }).collect(Collectors.toList());
    }

    /**
     * 注文商品リストに変換
     *
     * @param orderItemOriginRevisionDbEntityList
     * @return 注文商品リスト
     */
    public List<OrderItem> toOrderItemList(List<OrderItemOriginRevisionDbEntity> orderItemOriginRevisionDbEntityList) {
        if (CollectionUtils.isEmpty(orderItemOriginRevisionDbEntityList)) {
            return null;
        }

        return orderItemOriginRevisionDbEntityList.stream().map(orderItemOriginRevisionDb -> {
            OrderCount orderCount = new OrderCount(orderItemOriginRevisionDb.getOrderCount());
            OrderItemSeq orderItemSeq = new OrderItemSeq(orderItemOriginRevisionDb.getOrderItemSeq());
            return new OrderItem(orderItemOriginRevisionDb.getItemId(), orderItemSeq, orderCount,
                                 orderItemOriginRevisionDb.getItemName(), orderItemOriginRevisionDb.getUnitTitle1(),
                                 orderItemOriginRevisionDb.getUnitValue1(), orderItemOriginRevisionDb.getUnitTitle2(),
                                 orderItemOriginRevisionDb.getUnitValue2(), orderItemOriginRevisionDb.getJanCode(),
                                 EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                               orderItemOriginRevisionDb.getNoveltyGoodsType()),
                                 new OrderItemId(orderItemOriginRevisionDb.getOrderItemId())
            );
        }).collect(Collectors.toList());
    }
}