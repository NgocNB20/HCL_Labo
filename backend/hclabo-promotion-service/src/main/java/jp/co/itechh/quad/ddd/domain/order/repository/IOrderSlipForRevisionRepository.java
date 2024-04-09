package jp.co.itechh.quad.ddd.domain.order.repository;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipRevisionId;

/**
 * 改訂用注文票リポジトリ
 */
public interface IOrderSlipForRevisionRepository {

    /**
     * 改訂用注文票登録
     *
     * @param orderSlipForRevisionEntity 改訂用注文票
     */
    void save(OrderSlipForRevisionEntity orderSlipForRevisionEntity);

    /**
     * 改訂用注文票更新
     *
     * @param orderSlipForRevisionEntity 改訂用注文票
     * @return 更新件数
     */
    int update(OrderSlipForRevisionEntity orderSlipForRevisionEntity);

    /**
     * 改訂用注文票取得
     *
     * @param orderSlipRevisionId 改訂用注文票ID
     * @return OrderSlipForRevisionEntity 改訂用注文票
     */
    OrderSlipForRevisionEntity get(OrderSlipRevisionId orderSlipRevisionId);

    /**
     * 改訂用取引IDで改訂用注文票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return OrderSlipForRevisionEntity 改訂用注文票
     */
    OrderSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId);
}
