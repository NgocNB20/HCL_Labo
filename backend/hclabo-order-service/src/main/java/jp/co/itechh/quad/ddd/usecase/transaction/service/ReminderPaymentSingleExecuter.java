package jp.co.itechh.quad.ddd.usecase.transaction.service;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.payment.adapter.ILinkPayAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.IAdministratorAdapter;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.GetByDraftStatusResultDto;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementReminderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 支払督促1件処理 クラス
 */
@Service
public class ReminderPaymentSingleExecuter {

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 取引改訂を確定するユースケース */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** 通知アダプター */
    private final IAdministratorAdapter administratorAdapter;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** リンク支払いアダプター */
    private final ILinkPayAdapter linkPayAdapter;

    /** 受注リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public ReminderPaymentSingleExecuter(StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter,
                                         OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter,
                                         IAdministratorAdapter administratorAdapter,
                                         ILinkPayAdapter linkPayAdapter,
                                         INotificationSubAdapter notificationSubAdapter,
                                         ITransactionForRevisionRepository transactionForRevisionRepository) {
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
        this.administratorAdapter = administratorAdapter;
        this.linkPayAdapter = linkPayAdapter;
        this.notificationSubAdapter = notificationSubAdapter;
        this.transactionForRevisionRepository = transactionForRevisionRepository;
    }

    /**
     * 支払督促1件処理（コミット）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity execute(GetByDraftStatusResultDto reminderPaymentDto) {

        // 戻り値
        OrderReceivedEntity orderReceivedEntity = null;

        // リンク決済(後日払い)の支払期限切れ間近を判定する
        Boolean dueDateFlag = linkPayAdapter.checkLaterDatePaymentReminder(reminderPaymentDto.getTransactionId());

        // 支払間近の場合
        if (Boolean.TRUE.equals(dueDateFlag)) {

            // 改訂用取引を開始
            String targetTransactionRevisionId = startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(
                            reminderPaymentDto.getTransactionId());

            // 支払督促メール送信リクエストを生成
            SettlementReminderRequest settlementReminderRequest = new SettlementReminderRequest();
            // パラメータ設定
            List<String> orderCodeList = new ArrayList<>();
            orderCodeList.add(reminderPaymentDto.getOrderCode());
            settlementReminderRequest.setOrderCodeList(orderCodeList);

            // 改訂用取引に入金督促通知済みを設定
            TransactionRevisionId transactionRevisionId = new TransactionRevisionId(targetTransactionRevisionId);
            TransactionForRevisionEntity transactionForRevisionEntity =
                            transactionForRevisionRepository.get(transactionRevisionId);
            transactionForRevisionEntity.settingReminderSent();
            transactionForRevisionRepository.update(transactionForRevisionEntity);

            // 改訂用取引を確定
            orderReceivedEntity = openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(
                            targetTransactionRevisionId, null, HTypeProcessType.REMINDER_SEND_MAIL, true, false, false);

            // 改訂処理楽観ロックエラーを考慮し、ここで処理
            // 支払督促メール送信
            notificationSubAdapter.reminderPayment(settlementReminderRequest);
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
            openTransactionReviseUseCaseExecuter.asyncAfterProcessInnerLogic(orderReceivedEntity);
        }
    }
}