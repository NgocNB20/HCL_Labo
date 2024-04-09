package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 改訂用注文票発行ユースケース
 */
@Service
public class PublishOrderSlipForRevisionUseCase {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public PublishOrderSlipForRevisionUseCase(IOrderSlipRepository orderSlipRepository,
                                              IOrderSlipForRevisionRepository orderSlipForRevisionRepository) {
        this.orderSlipRepository = orderSlipRepository;
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
    }

    /**
     * 改訂用注文票を発行する
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    public void publishOrderSlipForRevision(String transactionId, String transactionRevisionId) {

        // 改訂前の注文票を取得する
        OrderSlipEntity orderSlipEntity = orderSlipRepository.getOrderSlipByTransactionId(transactionId);

        // 改訂前の注文票が取得できない場合はエラー
        if (orderSlipEntity == null) {
            throw new DomainException("PROMOTION-ODER0008-E", new String[] {transactionId});
        }

        // 改訂用注文票発行
        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        new OrderSlipForRevisionEntity(orderSlipEntity, transactionRevisionId, new Date());

        // 改訂用注文票を登録する
        this.orderSlipForRevisionRepository.save(orderSlipForRevisionEntity);
    }
}