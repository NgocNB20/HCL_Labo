/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.order.repository;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderItemDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderSlipDbEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 注文票リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class OrderSlipRepositoryImpl implements IOrderSlipRepository {

    /** 注文票Daoクラス */
    private final OrderSlipDao orderSlipDao;

    /** 注文商品Daoクラス */
    private final OrderItemDao orderItemDao;

    /** 注文票リポジトリHelperクラス */
    private final OrderSlipRepositoryHelper orderSlipRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param orderSlipDao
     * @param orderItemDao
     * @param orderSlipRepositoryHelper
     */
    @Autowired
    public OrderSlipRepositoryImpl(OrderSlipDao orderSlipDao,
                                   OrderItemDao orderItemDao,
                                   OrderSlipRepositoryHelper orderSlipRepositoryHelper) {
        this.orderSlipDao = orderSlipDao;
        this.orderItemDao = orderItemDao;
        this.orderSlipRepositoryHelper = orderSlipRepositoryHelper;
    }

    /**
     * 注文票登録
     *
     * @param orderSlipEntity 注文票エンティティ
     */
    @Override
    public void save(OrderSlipEntity orderSlipEntity) {

        OrderSlipDbEntity orderSlipDbEntity = orderSlipRepositoryHelper.toOrderSlipDbEntity(orderSlipEntity);
        orderSlipDao.insert(orderSlipDbEntity);

        List<OrderItemDbEntity> orderItemDbEntityList =
                        orderSlipRepositoryHelper.toOrderItemDbEntity(orderSlipEntity.getOrderItemList());
        orderItemDbEntityList.forEach(orderItemDbEntity -> {
            orderItemDbEntity.setOrderSlipId(orderSlipEntity.getOrderSlipId().getValue());
            orderItemDao.insert(orderItemDbEntity);
        });
    }

    /**
     * 注文票更新
     *
     * @param orderSlipEntity 注文票エンティティ
     * @return 更新件数
     */
    @Override
    public int update(OrderSlipEntity orderSlipEntity) {

        // 注文票更新
        OrderSlipDbEntity orderSlipDbEntity = orderSlipRepositoryHelper.toOrderSlipDbEntity(orderSlipEntity);
        int result = this.orderSlipDao.update(orderSlipDbEntity);

        // 注文票IDにより分割した注文商品リストを削除する
        this.orderItemDao.deleteByOrderSlipId(orderSlipDbEntity.getOrderSlipId());

        for (OrderItem orderItem : orderSlipEntity.getOrderItemList()) {

            OrderItemDbEntity orderItemDbEntity =
                orderSlipRepositoryHelper.toOrderItemDbEntity(orderItem, orderSlipDbEntity.getOrderSlipId());
            // 注文商品に登録する
            this.orderItemDao.insert(orderItemDbEntity);
        }

        return result;
    }

    /**
     * 注文票更新【確定用】
     *
     * @param orderSlipEntity 注文票エンティティ
     * @param transactionId   取引ID
     * @return 更新件数
     */
    @Override
    public int updateForOpen(OrderSlipEntity orderSlipEntity, String transactionId) {
        // DBEntity生成
        OrderSlipDbEntity orderSlipDbEntity = orderSlipRepositoryHelper.toOrderSlipDbEntity(orderSlipEntity);
        // 注文票確定更新
        // ※第２引数に取引IDを引き渡す
        //   ⇒これにより、下書き伝票取得～注文票確定更新の間（一瞬ではあるが）に
        //     同一ユーザーが取引開始した場合の排他制御を行うことができる
        int result = this.orderSlipDao.updateForOpen(orderSlipDbEntity, transactionId);
        // 更新件数を返却
        return result;
    }

    /**
     * 注文票取得
     *
     * @param orderSlipId 注文票ID
     * @return OrderSlipEntity 注文票エンティティ
     */
    @Override
    public OrderSlipEntity get(OrderSlipId orderSlipId) {

        OrderSlipDbEntity orderSlipDbEntity =
                        orderSlipDao.getByOrderSlipIdAndOrderStatus(orderSlipId.getValue(), OrderStatus.DRAFT.name());
        if (orderSlipDbEntity == null) {
            return null;
        }

        List<OrderItemDbEntity> orderItemDbEntityList = orderItemDao.getByOrderSlipId(orderSlipId.getValue());

        List<OrderItem> orderItemList = orderSlipRepositoryHelper.toOrderItemList(orderItemDbEntityList);

        return new OrderSlipEntity(orderSlipId, OrderStatus.DRAFT, orderItemList, orderSlipDbEntity.getCustomerId(),
                                   orderSlipDbEntity.getTransactionId(), orderSlipDbEntity.getRegistDate(),
                                   orderSlipDbEntity.getUserAgent()
        );
    }

    /**
     * 注文票削除
     *
     * @param orderSlipId 注文票ID
     * @return 更新件数
     */
    @Override
    public int delete(OrderSlipId orderSlipId) {
        int result = orderSlipDao.deleteByOrderSlipId(orderSlipId.getValue());

        if (result > 0) {
            List<OrderItemDbEntity> orderItemDbEntityList = orderItemDao.getByOrderSlipId(orderSlipId.getValue());

            if (!CollectionUtils.isEmpty(orderItemDbEntityList)) {
                orderItemDbEntityList.forEach(orderItemDao::delete);
            }
        }

        return result;
    }

    /**
     * 注文票IDで下書き注文票取得
     *
     * @param orderSlipId 注文票ID
     * @return OrderSlipEntity 下書き注文票エンティティ
     */
    @Override
    public OrderSlipEntity getDraftOrderSlipByOrderSlipId(OrderSlipId orderSlipId) {

        OrderSlipDbEntity orderSlipDbEntity =
                        orderSlipDao.getByOrderSlipIdAndOrderStatus(orderSlipId.getValue(), OrderStatus.DRAFT.name());
        if (orderSlipDbEntity == null) {
            return null;
        }

        List<OrderItemDbEntity> orderItemDbEntityList =
                        orderItemDao.getByOrderSlipId(orderSlipDbEntity.getOrderSlipId());

        List<OrderItem> orderItemList = orderSlipRepositoryHelper.toOrderItemList(orderItemDbEntityList);

        return new OrderSlipEntity(new OrderSlipId(orderSlipDbEntity.getOrderSlipId()), OrderStatus.DRAFT,
                                   orderItemList, orderSlipDbEntity.getCustomerId(),
                                   orderSlipDbEntity.getTransactionId(), orderSlipDbEntity.getRegistDate(),
                                   orderSlipDbEntity.getUserAgent()
        );
    }

    /**
     * 取引IDで下書き注文票取得
     *
     * @param transactionId 取引ID
     * @return OrderSlipEntity 下書き注文票エンティティ
     */
    @Override
    public OrderSlipEntity getDraftOrderSlipByTransactionId(String transactionId) {

        OrderSlipDbEntity orderSlipDbEntity =
                        orderSlipDao.getByTransactionIdAndOrderStatus(transactionId, OrderStatus.DRAFT.name());
        if (orderSlipDbEntity == null) {
            return null;
        }

        List<OrderItemDbEntity> orderItemDbEntityList =
                        orderItemDao.getByOrderSlipId(orderSlipDbEntity.getOrderSlipId());

        List<OrderItem> orderItemList = orderSlipRepositoryHelper.toOrderItemList(orderItemDbEntityList);

        return new OrderSlipEntity(new OrderSlipId(orderSlipDbEntity.getOrderSlipId()), OrderStatus.DRAFT,
                                   orderItemList, orderSlipDbEntity.getCustomerId(),
                                   orderSlipDbEntity.getTransactionId(), orderSlipDbEntity.getRegistDate(),
                                   orderSlipDbEntity.getUserAgent()
        );
    }

    /**
     * 取引IDで注文票取得
     *
     * @param transactionId 取引ID
     * @return OrderSlipEntity 注文票エンティティ
     */
    @Override
    public OrderSlipEntity getOrderSlipByTransactionId(String transactionId) {

        OrderSlipDbEntity orderSlipDbEntity = orderSlipDao.getByTransactionId(transactionId);
        if (orderSlipDbEntity == null) {
            return null;
        }

        List<OrderItemDbEntity> orderItemDbEntityList =
                        orderItemDao.getByOrderSlipId(orderSlipDbEntity.getOrderSlipId());

        List<OrderItem> orderItemList = orderSlipRepositoryHelper.toOrderItemList(orderItemDbEntityList);

        return new OrderSlipEntity(new OrderSlipId(orderSlipDbEntity.getOrderSlipId()),
                                   OrderStatus.valueOf(orderSlipDbEntity.getOrderStatus()), orderItemList,
                                   orderSlipDbEntity.getCustomerId(), orderSlipDbEntity.getTransactionId(),
                                   orderSlipDbEntity.getRegistDate(), orderSlipDbEntity.getUserAgent()
        );
    }

    /**
     * 顧客IDで下書き注文票取得
     *
     * @param customerId 顧客ID
     * @return OrderSlipEntity 下書き注文票エンティティ
     */
    @Override
    public OrderSlipEntity getDraftOrderSlipByCustomerId(String customerId) {

        OrderSlipDbEntity orderSlipDbEntity =
                        orderSlipDao.getByCustomerIdAndOrderStatus(customerId, OrderStatus.DRAFT.name());

        if (orderSlipDbEntity == null) {
            return null;
        }

        List<OrderItemDbEntity> orderItemDbEntityList =
                        orderItemDao.getByOrderSlipId(orderSlipDbEntity.getOrderSlipId());

        List<OrderItem> orderItemList = orderSlipRepositoryHelper.toOrderItemList(orderItemDbEntityList);

        return new OrderSlipEntity(new OrderSlipId(orderSlipDbEntity.getOrderSlipId()), OrderStatus.DRAFT,
                                   orderItemList, orderSlipDbEntity.getCustomerId(),
                                   orderSlipDbEntity.getTransactionId(), orderSlipDbEntity.getRegistDate(),
                                   orderSlipDbEntity.getUserAgent()
        );
    }

    /**
     * 注文商品のデータを削除（下書き状態のみ削除）
     * 注文票のデータを削除（下書き状態のみ削除）
     *
     * @param customerId 顧客ID
     * @return 注文票の削除件数を返却する
     */
    @Override
    public int deleteDraftOrderSlipByCustomerId(String customerId) {

        orderItemDao.deleteDraftOrderItemByCustomerId(customerId);
        int result = orderSlipDao.deleteDraftOrderSlipByCustomerId(customerId);

        return result;
    }

    /**
     * 削除対象の下書き注文票リスト取得
     *
     * @return OrderSlipEntityList 下書き注文票エンティティリスト
     */
    @Override
    public List<OrderSlipEntity> getDraftOrderSlipListForDelete() {

        List<OrderSlipDbEntity> orderSlipDbEntityList = orderSlipDao.getByOrderStatus(OrderStatus.DRAFT.name());
        if (CollectionUtils.isEmpty(orderSlipDbEntityList)) {
            return null;
        }

        List<OrderSlipEntity> orderSlipEntityList = new ArrayList<>();

        orderSlipDbEntityList.forEach(orderSlipDbEntity -> {
            List<OrderItemDbEntity> orderItemDbEntityList =
                            orderItemDao.getByOrderSlipId(orderSlipDbEntity.getOrderSlipId());

            List<OrderItem> orderItemList = orderSlipRepositoryHelper.toOrderItemList(orderItemDbEntityList);

            OrderSlipEntity orderSlipEntity =
                            new OrderSlipEntity(new OrderSlipId(orderSlipDbEntity.getOrderSlipId()), OrderStatus.DRAFT,
                                                orderItemList, orderSlipDbEntity.getCustomerId(),
                                                orderSlipDbEntity.getTransactionId(), orderSlipDbEntity.getRegistDate(),
                                                orderSlipDbEntity.getUserAgent()
                            );

            orderSlipEntityList.add(orderSlipEntity);
        });

        return orderSlipEntityList;
    }

    /**
     * 不要データを削除する
     *
     * @param transactionId 取引ID
     */
    @Override
    public int deleteUnnecessaryByTransactionId(String transactionId) {

        orderItemDao.deleteOrderItem(transactionId);
        int result = orderSlipDao.deleteOrderSlip(transactionId);

        return result;
    }
}