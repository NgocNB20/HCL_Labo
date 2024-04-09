package jp.co.itechh.quad.ddd.infrastructure.transaction.repository;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.TransactionForRevisionDbEntity;
import org.springframework.stereotype.Component;

/**
 * 改訂用取引Helperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class TransactionForRevisionRepositoryHelper {

    /**
     * 改訂用取引エンティティに変換
     *
     * @param transactionForRevisionDbEntity 取引DbEntityクラス
     * @return TransactionForRevisionEntity 改訂用取引エンティティ
     */
    public TransactionForRevisionEntity toTransactionForRevisionEntity(TransactionForRevisionDbEntity transactionForRevisionDbEntity) {

        if (transactionForRevisionDbEntity == null) {
            return null;
        }
        TransactionStatus transactionStatus = EnumTypeUtil.getEnum(TransactionStatus.class,
                                                                   transactionForRevisionDbEntity.getTransactionStatus()
                                                                  );

        return new TransactionForRevisionEntity(new TransactionId(transactionForRevisionDbEntity.getTransactionId()),
                                                transactionStatus, transactionForRevisionDbEntity.getPaidFlag(),
                                                new PaymentStatusDetail(
                                                                transactionForRevisionDbEntity.isInsufficientMoneyFlag(),
                                                                transactionForRevisionDbEntity.isOverMoneyFlag()
                                                ), transactionForRevisionDbEntity.getShippedFlag(),
                                                transactionForRevisionDbEntity.getBillPaymentErrorFlag(),
                                                transactionForRevisionDbEntity.getNotificationFlag(),
                                                transactionForRevisionDbEntity.getReminderSentFlag(),
                                                transactionForRevisionDbEntity.getExpiredSentFlag(),
                                                transactionForRevisionDbEntity.getAdminMemo(),
                                                transactionForRevisionDbEntity.getCustomerId(), new OrderReceivedId(
                        transactionForRevisionDbEntity.getOrderReceivedId()),
                                                transactionForRevisionDbEntity.getRegistDate(),
                                                transactionForRevisionDbEntity.getProcessTime(),
                                                EnumTypeUtil.getEnumFromValue(HTypeProcessType.class,
                                                                              transactionForRevisionDbEntity.getProcessType()
                                                                             ),
                                                transactionForRevisionDbEntity.getProcessPersonName(),
                                                new TransactionRevisionId(
                                                                transactionForRevisionDbEntity.getTransactionRevisionId()),
                                                transactionForRevisionDbEntity.isPreClaimFlag(),
                                                EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class,
                                                                              transactionForRevisionDbEntity.getNoveltyPresentJudgmentStatus()
                                                                             )
        );
    }

    /**
     * プロパティコピー
     *
     * @param transactionForRevisionEntity 改訂用取引エンティティ
     * @return 改訂用取引DbEntityクラス
     */
    public TransactionForRevisionDbEntity copyPropertiesFromEntityToDbEntity(TransactionForRevisionEntity transactionForRevisionEntity) {

        TransactionForRevisionDbEntity transactionForRevisionDbEntity = new TransactionForRevisionDbEntity();

        transactionForRevisionDbEntity.setTransactionId(transactionForRevisionEntity.getTransactionId().getValue());
        transactionForRevisionDbEntity.setTransactionStatus(transactionForRevisionEntity.getTransactionStatus().name());
        transactionForRevisionDbEntity.setPaidFlag(transactionForRevisionEntity.isPaidFlag());
        if (transactionForRevisionEntity.getPaymentStatusDetail() != null) {
            transactionForRevisionDbEntity.setInsufficientMoneyFlag(
                            transactionForRevisionEntity.getPaymentStatusDetail().isInsufficientMoneyFlag());
            transactionForRevisionDbEntity.setOverMoneyFlag(
                            transactionForRevisionEntity.getPaymentStatusDetail().isOverMoneyFlag());
        }
        transactionForRevisionDbEntity.setShippedFlag(transactionForRevisionEntity.isShippedFlag());
        transactionForRevisionDbEntity.setNotificationFlag(transactionForRevisionEntity.isNotificationFlag());
        transactionForRevisionDbEntity.setReminderSentFlag(transactionForRevisionEntity.isReminderSentFlag());
        transactionForRevisionDbEntity.setExpiredSentFlag(transactionForRevisionEntity.isExpiredSentFlag());
        transactionForRevisionDbEntity.setBillPaymentErrorFlag(transactionForRevisionEntity.isBillPaymentErrorFlag());
        transactionForRevisionDbEntity.setRegistDate(transactionForRevisionEntity.getRegistDate());
        transactionForRevisionDbEntity.setCustomerId(transactionForRevisionEntity.getCustomerId());
        transactionForRevisionDbEntity.setAdminMemo(transactionForRevisionEntity.getAdminMemo());
        transactionForRevisionDbEntity.setOrderReceivedId(transactionForRevisionEntity.getOrderReceivedId().getValue());
        transactionForRevisionDbEntity.setProcessTime(transactionForRevisionEntity.getProcessTime());
        transactionForRevisionDbEntity.setProcessType(
                        EnumTypeUtil.getValue(transactionForRevisionEntity.getProcessType()));
        transactionForRevisionDbEntity.setProcessPersonName(transactionForRevisionEntity.getProcessPersonName());
        transactionForRevisionDbEntity.setTransactionRevisionId(
                        transactionForRevisionEntity.getTransactionRevisionId().getValue());
        transactionForRevisionDbEntity.setPreClaimFlag(transactionForRevisionEntity.isPreClaimFlag());
        transactionForRevisionDbEntity.setNoveltyPresentJudgmentStatus(
                        EnumTypeUtil.getValue(transactionForRevisionEntity.getNoveltyPresentJudgmentStatus()));

        return transactionForRevisionDbEntity;
    }
}