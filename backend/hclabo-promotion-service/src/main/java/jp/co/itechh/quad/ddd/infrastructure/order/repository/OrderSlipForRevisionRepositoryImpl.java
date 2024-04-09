package jp.co.itechh.quad.ddd.infrastructure.order.repository;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemRevision;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipRevisionId;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderItemOriginRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderItemRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderSlipForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemOriginRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderSlipForRevisionDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 改訂用注文票リポジトリ実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSlipForRevisionRepositoryImpl implements IOrderSlipForRevisionRepository {

    /** 改訂用注文票Daoクラス */
    private final OrderSlipForRevisionDao orderSlipForRevisionDao;

    /** 注文商品Daoクラス */
    private final OrderItemRevisionDao orderItemRevisionDao;

    /** 改訂用注文商品Daoクラス */
    private final OrderItemOriginRevisionDao orderItemOriginRevisionDao;

    /** 改訂用注文票リポジトリHelperクラス */
    private final OrderSlipForRevisionRepositoryHelper orderSlipForRevisionRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param orderSlipForRevisionDao
     * @param orderItemRevisionDao
     * @param orderItemOriginRevisionDao
     * @param orderSlipForRevisionRepositoryHelper
     */
    @Autowired
    public OrderSlipForRevisionRepositoryImpl(OrderSlipForRevisionDao orderSlipForRevisionDao,
                                              OrderItemRevisionDao orderItemRevisionDao,
                                              OrderItemOriginRevisionDao orderItemOriginRevisionDao,
                                              OrderSlipForRevisionRepositoryHelper orderSlipForRevisionRepositoryHelper) {
        this.orderSlipForRevisionDao = orderSlipForRevisionDao;
        this.orderItemRevisionDao = orderItemRevisionDao;
        this.orderItemOriginRevisionDao = orderItemOriginRevisionDao;
        this.orderSlipForRevisionRepositoryHelper = orderSlipForRevisionRepositoryHelper;
    }

    /**
     * 改訂用注文票登録
     *
     * @param orderSlipForRevisionEntity 改訂用注文票
     */
    @Override
    public void save(OrderSlipForRevisionEntity orderSlipForRevisionEntity) {

        // 1. insert table OrderSlipForRevision
        OrderSlipForRevisionDbEntity orderSlipRevisionDb =
                        orderSlipForRevisionRepositoryHelper.toOrderSlipForRevisionDbEntity(orderSlipForRevisionEntity);
        orderSlipForRevisionDao.insert(orderSlipRevisionDb);

        // 2. insert table OrderItemRevision
        List<OrderItemRevisionDbEntity> orderItemRevisionDbEntityList =
                        orderSlipForRevisionRepositoryHelper.toOrderItemRevisionDbEntityList(
                                        orderSlipForRevisionEntity.getOrderItemList(),
                                        orderSlipRevisionDb.getOrderSlipRevisionId()
                                                                                            );
        orderItemRevisionDao.insert(orderItemRevisionDbEntityList);

        // 3. insert table OrderItemOriginRevision
        List<OrderItemOriginRevisionDbEntity> orderItemOriginRevisionDbEntityList =
                        orderSlipForRevisionRepositoryHelper.toOrderItemOriginRevisionDbEntityList(
                                        orderSlipForRevisionEntity.getOrderItemRevisionList(),
                                        orderSlipRevisionDb.getOrderSlipRevisionId()
                                                                                                  );
        orderItemOriginRevisionDao.insert(orderItemOriginRevisionDbEntityList);
    }

    /**
     * 改訂用注文票更新
     *
     * @param orderSlipForRevisionEntity 改訂用注文票
     * @return 更新件数
     */
    @Override
    public int update(OrderSlipForRevisionEntity orderSlipForRevisionEntity) {

        // 1. update table OrderSlipForRevision
        OrderSlipForRevisionDbEntity orderSlipRevisionDb =
                        orderSlipForRevisionRepositoryHelper.toOrderSlipForRevisionDbEntity(orderSlipForRevisionEntity);
        int result = orderSlipForRevisionDao.update(orderSlipRevisionDb);

        // 2. Insert/update/delete table OrderItemRevision
        List<OrderItemRevisionDbEntity> orderItemRevisionDbEntityList =
                        orderSlipForRevisionRepositoryHelper.toOrderItemRevisionDbEntityList(
                                        (List<OrderItem>) (Object) orderSlipForRevisionEntity.getOrderItemRevisionList(),
                                        orderSlipRevisionDb.getOrderSlipRevisionId()
                                                                                            );

        Set<Integer> orderItemSeqSet = new HashSet<>(); // 登録対象注文商品連番set
        for (OrderItemRevisionDbEntity orderItemRevisionDbEntity : orderItemRevisionDbEntityList) {
            orderItemSeqSet.add(orderItemRevisionDbEntity.getOrderItemSeq());
            orderItemRevisionDao.insertOrUpdate(orderItemRevisionDbEntity);
        }

        // DBから取得
        List<OrderItemRevisionDbEntity> orderItemRevisionDbEntityInDbList =
                        orderItemRevisionDao.getByOrderSlipRevisionId(orderSlipRevisionDb.getOrderSlipRevisionId());

        // 登録対象注文商品連番setに含まれていないデータがある場合は削除
        if (!CollectionUtils.isEmpty(orderItemRevisionDbEntityInDbList)) {
            orderItemRevisionDbEntityInDbList.forEach(orderItemInDB -> {
                if (!orderItemSeqSet.contains(orderItemInDB.getOrderItemSeq())) {
                    orderItemRevisionDao.delete(orderItemInDB);
                }
            });
        }

        return result;
    }

    /**
     * 改訂用注文票取得
     *
     * @param orderSlipRevisionId 改訂用注文票ID
     * @return OrderSlipForRevisionEntity 改訂用注文票
     */
    @Override
    public OrderSlipForRevisionEntity get(OrderSlipRevisionId orderSlipRevisionId) {

        String orderSlipRevisionIdValue = orderSlipRevisionId.getValue();

        // 1. Get data in table OrderSlipForRevision
        OrderSlipForRevisionDbEntity orderSlipForRevisionDbEntity =
                        orderSlipForRevisionDao.getByOrderSlipRevisionId(orderSlipRevisionIdValue);

        if (orderSlipForRevisionDbEntity == null) {
            return null;
        }

        // 2. Get data in table OrderItemRevision
        List<OrderItemRevisionDbEntity> orderItemRevisionDbEntityList =
                        orderItemRevisionDao.getByOrderSlipRevisionId(orderSlipRevisionIdValue);
        List<OrderItemRevision> orderItemRevisionList =
                        orderSlipForRevisionRepositoryHelper.toOrderItemRevisionList(orderItemRevisionDbEntityList);

        // 3. Get data in table OrderItemOriginRevision
        List<OrderItemOriginRevisionDbEntity> orderItemOriginRevisionDbEntityList =
                        orderItemOriginRevisionDao.getByOrderSlipRevisionId(orderSlipRevisionIdValue);
        List<OrderItem> orderItemList =
                        orderSlipForRevisionRepositoryHelper.toOrderItemList(orderItemOriginRevisionDbEntityList);

        return new OrderSlipForRevisionEntity(orderSlipForRevisionDbEntity.getOrderSlipRevisionId(),
                                              orderItemRevisionList,
                                              orderSlipForRevisionDbEntity.getTransactionRevisionId(),
                                              orderSlipForRevisionDbEntity.getOrderSlipId(),
                                              orderSlipForRevisionDbEntity.getOrderStatus(), orderItemList,
                                              orderSlipForRevisionDbEntity.getCustomerId(),
                                              orderSlipForRevisionDbEntity.getTransactionId(),
                                              orderSlipForRevisionDbEntity.getRegistDate()
        );
    }

    /**
     * 改訂用取引IDで改訂用注文票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return OrderSlipForRevisionEntity 改訂用注文票
     */
    @Override
    public OrderSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId) {

        // 1. Get data in table OrderSlipForRevision
        OrderSlipForRevisionDbEntity orderSlipForRevisionDbEntity =
                        orderSlipForRevisionDao.getByTransactionRevisionId(transactionRevisionId);

        if (orderSlipForRevisionDbEntity == null) {
            return null;
        }

        // 2. Get data in table OrderItemRevision
        List<OrderItemRevisionDbEntity> orderItemRevisionDbEntityList = orderItemRevisionDao.getByOrderSlipRevisionId(
                        orderSlipForRevisionDbEntity.getOrderSlipRevisionId());
        List<OrderItemRevision> orderItemRevisionList =
                        orderSlipForRevisionRepositoryHelper.toOrderItemRevisionList(orderItemRevisionDbEntityList);

        // 3. Get data in table OrderItemOriginRevision
        List<OrderItemOriginRevisionDbEntity> orderItemOriginRevisionDbEntityList =
                        orderItemOriginRevisionDao.getByOrderSlipRevisionId(
                                        orderSlipForRevisionDbEntity.getOrderSlipRevisionId());
        List<OrderItem> orderItemList =
                        orderSlipForRevisionRepositoryHelper.toOrderItemList(orderItemOriginRevisionDbEntityList);

        return new OrderSlipForRevisionEntity(orderSlipForRevisionDbEntity.getOrderSlipRevisionId(),
                                              orderItemRevisionList,
                                              orderSlipForRevisionDbEntity.getTransactionRevisionId(),
                                              orderSlipForRevisionDbEntity.getOrderSlipId(),
                                              orderSlipForRevisionDbEntity.getOrderStatus(), orderItemList,
                                              orderSlipForRevisionDbEntity.getCustomerId(),
                                              orderSlipForRevisionDbEntity.getTransactionId(),
                                              orderSlipForRevisionDbEntity.getRegistDate()
        );
    }
}