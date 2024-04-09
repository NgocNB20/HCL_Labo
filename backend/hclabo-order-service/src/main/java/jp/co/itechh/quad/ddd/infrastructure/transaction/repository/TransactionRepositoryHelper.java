package jp.co.itechh.quad.ddd.infrastructure.transaction.repository;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.TransactionDbEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 取引リポジトリHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class TransactionRepositoryHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRepositoryHelper.class);

    /**
     * 取引DbEntityに変換.
     *
     * @param transactionEntity 取引エンティティ
     * @return 取引DbEntityクラス
     */
    public TransactionDbEntity toTransactionDbEntity(TransactionEntity transactionEntity) {

        if (transactionEntity == null) {
            return null;
        }

        TransactionDbEntity transactionDbEntity = new TransactionDbEntity();

        try {
            BeanUtils.copyProperties(transactionDbEntity, transactionEntity);
            if (transactionEntity.getPaymentStatusDetail() != null) {
                transactionDbEntity.setInsufficientMoneyFlag(
                                transactionEntity.getPaymentStatusDetail().isInsufficientMoneyFlag());
                transactionDbEntity.setOverMoneyFlag(transactionEntity.getPaymentStatusDetail().isOverMoneyFlag());
            }
            transactionDbEntity.setTransactionId(transactionEntity.getTransactionId().getValue());
            transactionDbEntity.setTransactionStatus(transactionEntity.getTransactionStatus().name());
            transactionDbEntity.setProcessType(EnumTypeUtil.getValue(transactionEntity.getProcessType()));
            transactionDbEntity.setOrderReceivedId(transactionEntity.getOrderReceivedId().getValue());
            transactionDbEntity.setPreClaimFlag(transactionEntity.isPreClaimFlag());
            transactionDbEntity.setNoveltyPresentJudgmentStatus(
                            EnumTypeUtil.getValue(transactionEntity.getNoveltyPresentJudgmentStatus()));

        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            return null;
        }

        return transactionDbEntity;
    }

    /**
     * 取引に変換.
     *
     * @param transactionDbEntity 取引DbEntityクラス
     * @return 取引
     */
    public TransactionEntity toTransactionEntity(TransactionDbEntity transactionDbEntity) {

        if (transactionDbEntity == null) {
            return null;
        }

        TransactionStatus transactionStatus =
                        EnumTypeUtil.getEnum(TransactionStatus.class, transactionDbEntity.getTransactionStatus());

        return new TransactionEntity(new TransactionId(transactionDbEntity.getTransactionId()), transactionStatus,
                                     transactionDbEntity.getPaidFlag(),
                                     new PaymentStatusDetail(transactionDbEntity.isInsufficientMoneyFlag(),
                                                             transactionDbEntity.isOverMoneyFlag()
                                     ), transactionDbEntity.getShippedFlag(),
                                     transactionDbEntity.getBillPaymentErrorFlag(),
                                     transactionDbEntity.getNotificationFlag(),
                                     transactionDbEntity.getReminderSentFlag(),
                                     transactionDbEntity.getExpiredSentFlag(), transactionDbEntity.getAdminMemo(),
                                     transactionDbEntity.getCustomerId(),
                                     new OrderReceivedId(transactionDbEntity.getOrderReceivedId(), null),
                                     transactionDbEntity.getRegistDate(), transactionDbEntity.getProcessTime(),
                                     EnumTypeUtil.getEnumFromValue(HTypeProcessType.class,
                                                                   transactionDbEntity.getProcessType()
                                                                  ), transactionDbEntity.getProcessPersonName(),
                                     transactionDbEntity.isPreClaimFlag(),
                                     EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class,
                                                                   transactionDbEntity.getNoveltyPresentJudgmentStatus()
                                                                  )
        );
    }

    /**
     * 取引一覧に変換する.
     *
     * @param transactionDbEntityList トランザクション DbEntity リスト
     * @return 取引一覧
     */
    public List<TransactionEntity> toTransactionEntityList(List<TransactionDbEntity> transactionDbEntityList) {

        if (CollectionUtils.isEmpty(transactionDbEntityList)) {
            return new ArrayList<>();
        }

        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        for (TransactionDbEntity transactionDbEntity : transactionDbEntityList) {
            transactionEntityList.add(this.toTransactionEntity(transactionDbEntity));
        }

        return transactionEntityList;
    }

}