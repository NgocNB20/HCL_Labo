package jp.co.itechh.quad.ddd.infrastructure.transaction.query;

import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.OrderReceivedDao;
import jp.co.itechh.quad.ddd.usecase.transaction.query.IGetTransactionForExpiredSessionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GMOからのOrderCodeを使用して取引IDを取得するクエリ実装
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class GetTransactionForExpiredSessionQueryImpl implements IGetTransactionForExpiredSessionQuery {

    /** 受注Daoクラス */
    private final OrderReceivedDao orderReceivedDao;

    /**
     * @param orderReceivedDao 受注Daoクラス
     */
    @Autowired
    public GetTransactionForExpiredSessionQueryImpl(OrderReceivedDao orderReceivedDao) {
        this.orderReceivedDao = orderReceivedDao;
    }

    /**
     *  GMOからのOrderCodeを使用して取引IDを取得する
     *
     * @param orderCode 受注番号
     * @return transactionId 取引ID
     */
    @Override
    public String getTransactionIdByOrderCode(String orderCode) {
        return orderReceivedDao.getTransactionIdByOrderCodeExpiredSession(orderCode);
    }
}
