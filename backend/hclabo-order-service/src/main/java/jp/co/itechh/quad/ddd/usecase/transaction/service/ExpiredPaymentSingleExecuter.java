package jp.co.itechh.quad.ddd.usecase.transaction.service;

import jp.co.itechh.quad.ddd.domain.payment.adapter.ILinkPayAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.GetByDraftStatusResultDto;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementExpirationNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 支払期限切れ1件処理 クラス
 */
@Service
public class ExpiredPaymentSingleExecuter {

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 改訂用取引取消ユースケース内部ロジック */
    private final CancelTransactionForRevisionUseCaseExecuter cancelTransactionForRevisionUseCaseExecuter;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** リンク支払いアダプター */
    private final ILinkPayAdapter linkPayAdapter;

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public ExpiredPaymentSingleExecuter(StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter,
                                        CancelTransactionForRevisionUseCaseExecuter cancelTransactionForRevisionUseCaseExecuter,
                                        INotificationSubAdapter notificationSubAdapter,
                                        ILinkPayAdapter linkPayAdapter,
                                        ITransactionForRevisionRepository transactionForRevisionRepository) {
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
        this.cancelTransactionForRevisionUseCaseExecuter = cancelTransactionForRevisionUseCaseExecuter;
        this.notificationSubAdapter = notificationSubAdapter;
        this.linkPayAdapter = linkPayAdapter;
        this.transactionForRevisionRepository = transactionForRevisionRepository;
    }

    /**
     * 支払期限切れ処理（コミット）
     *
     * @param expiredPaymentDto
     * @return OrderReceivedEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity execute(GetByDraftStatusResultDto expiredPaymentDto) {

        // 戻り値
        OrderReceivedEntity orderReceivedEntity = null;

        // リンク決済(後日払い)の支払期限切れ判定する
        Boolean expiredFlag = linkPayAdapter.checkLaterDatePaymentExpired(expiredPaymentDto.getTransactionId());

        // 支払期限切れの場合
        if (Boolean.TRUE.equals(expiredFlag)) {

            // 改訂用取引を開始
            String targetTransactionRevisionId = startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(
                            expiredPaymentDto.getTransactionId());

            // 改訂用取引リポジトリー 改訂用取引取得
            TransactionForRevisionEntity transactionForRevisionEntity = transactionForRevisionRepository.get(
                            new TransactionRevisionId(targetTransactionRevisionId));
            if (transactionForRevisionEntity == null) {
                // 取引取得失敗
                throw new DomainException("ORDER-TREV0009-E", new String[] {targetTransactionRevisionId});
            }

            // 入金期限切れ通知が必要な場合
            boolean needExpiredSent = transactionForRevisionEntity.isNecessaryExpiredSent();
            if (needExpiredSent) {
                // 入金督促通知済み設定
                transactionForRevisionEntity.settingExpiredSent();
                transactionForRevisionRepository.update(transactionForRevisionEntity);
            }

            // 改訂用取引を 取消して取引改訂確定
            orderReceivedEntity = cancelTransactionForRevisionUseCaseExecuter.cancelTransactionForRevisionInnerLogic(
                            targetTransactionRevisionId, null, null);

            // 改訂処理楽観ロックエラーを考慮し、ここで処理
            // 入金期限切れ通知が必要な場合
            if (needExpiredSent) {

                // 支払期限切れメール送信リクエストパラメータ生成
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(expiredPaymentDto.getOrderCode());
                SettlementExpirationNotificationRequest settlementExpirationNotificationRequest =
                                new SettlementExpirationNotificationRequest();
                settlementExpirationNotificationRequest.setOrderCodeList(orderCodeList);

                // 支払期限切れメール送信
                notificationSubAdapter.expiredPayment(settlementExpirationNotificationRequest);
            }
        }

        return orderReceivedEntity;
    }

    /**
     * 処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcess(OrderReceivedEntity orderReceivedEntity) {

        if (orderReceivedEntity != null) {
            // 改訂取引を確定 非同期処理
            cancelTransactionForRevisionUseCaseExecuter.asyncAfterProcessInnerLogic(orderReceivedEntity);
        }
    }
}