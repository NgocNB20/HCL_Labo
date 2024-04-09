package jp.co.itechh.quad.ddd.infrastructure.transaction.repository;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.OrderReceivedDbEntity;
import org.springframework.stereotype.Component;

/**
 * 受注Helperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderReceivedRepositoryHelper {

    /**
     * 受注エンティティに変換.
     *
     * @param orderReceivedDbEntity 受注Dbエンティティ
     * @return 受注エンティティ
     */
    public OrderReceivedEntity toOrderReceivedEntity(OrderReceivedDbEntity orderReceivedDbEntity) {

        if (orderReceivedDbEntity == null) {
            return null;
        }

        return new OrderReceivedEntity(
                        new OrderReceivedId(orderReceivedDbEntity.getOrderReceivedId(), null),
                        new OrderCode(orderReceivedDbEntity.getOrderCode()), orderReceivedDbEntity.getRegistDate(),
                        orderReceivedDbEntity.getOrderReceivedDate(), orderReceivedDbEntity.getCancelDate(),
                        new TransactionId(orderReceivedDbEntity.getLatestTransactionId(), null)
        );
    }

    /**
     * 受注Dbエンティティに変換.
     *
     * @param orderReceivedEntity 受注エンティティ
     * @return 受注Dbエンティティ
     */
    public OrderReceivedDbEntity toOrderReceivedDbEntity(OrderReceivedEntity orderReceivedEntity) {

        OrderReceivedDbEntity orderReceivedDbEntity = new OrderReceivedDbEntity();
        orderReceivedDbEntity.setOrderReceivedId(orderReceivedEntity.getOrderReceivedId().getValue());
        orderReceivedDbEntity.setOrderCode(orderReceivedEntity.getOrderCode().getValue());
        orderReceivedDbEntity.setRegistDate(orderReceivedEntity.getRegistDate());
        orderReceivedDbEntity.setOrderReceivedDate(orderReceivedEntity.getOrderReceivedDate());
        orderReceivedDbEntity.setCancelDate(orderReceivedEntity.getCancelDate());
        if (orderReceivedEntity.getLatestTransactionId() != null) {
            orderReceivedDbEntity.setLatestTransactionId(orderReceivedEntity.getLatestTransactionId().getValue());
        }

        return orderReceivedDbEntity;
    }
}