package jp.co.itechh.quad.ddd.infrastructure.transaction.repository;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.OrderReceivedDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.OrderReceivedDbEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 受注リポジトリ
 */
@Component
public class OrderReceivedRepositoryImpl implements IOrderReceivedRepository {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderReceivedRepositoryImpl.class);

    /** 受注Daoクラス */
    private final OrderReceivedDao orderReceivedDao;

    /** 受注Helperクラス */
    private final OrderReceivedRepositoryHelper orderReceivedRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param orderReceivedDao              受注Daoクラス
     * @param orderReceivedRepositoryHelper 受注Helperクラス
     */
    @Autowired
    public OrderReceivedRepositoryImpl(OrderReceivedDao orderReceivedDao,
                                       OrderReceivedRepositoryHelper orderReceivedRepositoryHelper) {
        this.orderReceivedDao = orderReceivedDao;
        this.orderReceivedRepositoryHelper = orderReceivedRepositoryHelper;
    }

    /**
     * 受注番号連用番取得
     *
     * @return 受注番号用連番
     */
    @Override
    public Integer getOrderCodeSeq() {
        return orderReceivedDao.getOrderCodeSeq();
    }

    /**
     * 受注取得
     *
     * @param orderReceivedId
     * @return OrderReceivedEntity
     */
    @Override
    public OrderReceivedEntity get(OrderReceivedId orderReceivedId) {

        OrderReceivedDbEntity orderReceivedDbEntity = orderReceivedDao.getByOrderReceivedId(orderReceivedId.getValue());

        return orderReceivedRepositoryHelper.toOrderReceivedEntity(orderReceivedDbEntity);
    }

    /**
     * 受注番号に紐づく受注取得
     *
     * @param latestTransactionId
     * @return OrderReceivedEntity
     */
    @Override
    public OrderReceivedEntity getByLatestTransactionId(TransactionId latestTransactionId) {
        OrderReceivedDbEntity orderReceivedDbEntity =
                        orderReceivedDao.getByLatestTransactionId(latestTransactionId.getValue());
        return orderReceivedRepositoryHelper.toOrderReceivedEntity(orderReceivedDbEntity);
    }

    /**
     * 受注番号に紐づく受注取得
     *
     * @param orderCode
     * @return OrderReceivedEntity
     */
    @Override
    public OrderReceivedEntity getByOrderCode(OrderCode orderCode) {
        OrderReceivedDbEntity orderReceivedDbEntity = orderReceivedDao.getByOrderCode(orderCode.getValue());

        return orderReceivedRepositoryHelper.toOrderReceivedEntity(orderReceivedDbEntity);
    }

    /**
     * 受注登録
     *
     * @param orderReceivedEntity
     */
    public void save(OrderReceivedEntity orderReceivedEntity) {

        OrderReceivedDbEntity orderReceivedDbEntity =
                        orderReceivedRepositoryHelper.toOrderReceivedDbEntity(orderReceivedEntity);

        orderReceivedDao.insert(orderReceivedDbEntity);
    }

    /**
     * 受注更新（改訂元取引IDチェック）
     * ※originTransactionIdを指定した場合は、latestTransactionIdに一致したものを更新
     * ※対象なければRuntimeException
     *
     * @param orderReceivedEntity
     * @param originTransactionId
     * @return 更新件数
     */
    @Override
    public int updateWithTranCheck(OrderReceivedEntity orderReceivedEntity, String originTransactionId) {

        OrderReceivedDbEntity orderReceivedDbEntity =
                        orderReceivedRepositoryHelper.toOrderReceivedDbEntity(orderReceivedEntity);

        int cnt = orderReceivedDao.updateWithDbTranCheck(orderReceivedDbEntity, originTransactionId);
        if (cnt <= 0) {
            LOGGER.error("OrderReceivedRepositoryImpl#updateWithTranCheck：originTransactionId is not latest [originTransactionId:"
                         + originTransactionId + "]");
            throw new DomainException("ORDER-ODER0001-E");
        }

        return cnt;
    }

    /**
     * 顧客ごとの受注件数取得
     *
     * @param customerId 顧客ID
     * @return 顧客ごとの受注件数
     */
    @Override
    public int getOrderReceivedCountByCustomerId(String customerId) {
        return orderReceivedDao.getOrderReceivedCountByCustomerId(customerId);
    }
}