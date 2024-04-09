package jp.co.itechh.quad.ddd.presentation.ordersearch.api.processor;

import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.ddd.usecase.order.OrderRegistUpdateUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 受注検索 Processor
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class OrderSearchRegistUpdateProcessor {

    /**
     * 注文登録更新用 ユースケース
     */
    private final OrderRegistUpdateUseCase orderRegistUpdateUsecase;

    /**
     * コンストラクタ
     *
     * @param orderRegistUpdateUsecase 注文登録更新用 ユースケース
     */
    @Autowired
    public OrderSearchRegistUpdateProcessor(OrderRegistUpdateUseCase orderRegistUpdateUsecase) {
        this.orderRegistUpdateUsecase = orderRegistUpdateUsecase;
    }

    /**
     * 受注検索クエリーモデル取得
     *
     * @param message メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(QueueMessage message) throws Exception {

        orderRegistUpdateUsecase.orderRegistUpdate(message.getOrderReceivedId());

    }

}
