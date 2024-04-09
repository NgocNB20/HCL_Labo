package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeq;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用注文商品削除ユースケース
 */
@Service
public class DeleteOrderItemForRevisionUseCase {

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** 改訂用注文票ドメインサービス */
    private final OrderSlipForRevisionEntityService orderSlipForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public DeleteOrderItemForRevisionUseCase(IOrderSlipForRevisionRepository orderSlipForRevisionRepository,
                                             OrderSlipForRevisionEntityService orderSlipForRevisionEntityService) {
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
        this.orderSlipForRevisionEntityService = orderSlipForRevisionEntityService;
    }

    /**
     * 注文商品を削除する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param orderItemSeq          注文商品連番
     */
    public void deleteOrderItemForRevision(String transactionRevisionId, int orderItemSeq) {

        // 改訂用取引IDで改訂用注文票を取得する
        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        orderSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);

        // 改訂用注文票が取得できない場合はエラー
        if (orderSlipForRevisionEntity == null) {
            throw new DomainException("PROMOTION-ODER0007-E", new String[] {transactionRevisionId});
        }

        // 改訂用注文票の注文商品削除
        orderSlipForRevisionEntityService.deleteOrderItemForRevision(
                        new OrderItemSeq(orderItemSeq), orderSlipForRevisionEntity);
    }
}