package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用注文票取消ユースケース
 */
@Service
public class CancelOrderSlipForRevisionUseCase {

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public CancelOrderSlipForRevisionUseCase(IOrderSlipForRevisionRepository orderSlipForRevisionRepository) {
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
    }

    /**
     * 改訂用注文票を取消する
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    public void cancelOrderSlipForRevision(String transactionRevisionId) {

        // 改訂用取引IDで改訂用注文票を取得する
        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        orderSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);

        // 改訂用注文票が取得できない場合はエラー
        if (orderSlipForRevisionEntity == null) {
            throw new DomainException("PROMOTION-ODER0007-E", new String[] {transactionRevisionId});
        }

        // 改訂用注文票を取消
        orderSlipForRevisionEntity.cancelOrderSlipRevision();

        // 改訂用注文票を更新する
        orderSlipForRevisionRepository.update(orderSlipForRevisionEntity);
    }
}