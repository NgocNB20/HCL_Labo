/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IMulPayAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.DepositedResult;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.infrastructure.user.adapter.NotificationSubAdapterImpl;
import jp.co.itechh.quad.ddd.usecase.transaction.service.OpenTransactionReviseUseCaseExecuter;
import jp.co.itechh.quad.ddd.usecase.transaction.service.StartTransactionReviseUseCaseExecuter;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmDepositedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * HIT-MALL入金結果データを確認して受注を入金済みにする
 */
@Service
public class ConfirmHMPaymentThenSettingPaidStatusUseCase {

    /** 改訂取引アダプター */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** マルチペイメントエンドポイントアダプター */
    private final IMulPayAdapter mulPayAdapter;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** 請求アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 取引改訂確定ユースケース内部ロジック */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** コンストラクタ */
    @Autowired
    public ConfirmHMPaymentThenSettingPaidStatusUseCase(ITransactionForRevisionRepository transactionForRevisionRepository,
                                                        IMulPayAdapter mulPayAdapter,
                                                        IBillingSlipAdapter billingAdapter,
                                                        NotificationSubAdapterImpl notificationSubAdapter,
                                                        StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter,
                                                        OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter,
                                                        IOrderReceivedRepository orderReceivedRepository,
                                                        ITransactionRepository transactionRepository) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.mulPayAdapter = mulPayAdapter;
        this.billingAdapter = billingAdapter;
        this.notificationSubAdapter = notificationSubAdapter;
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
        this.orderReceivedRepository = orderReceivedRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * HM入金結果データを確認して受注を入金済みにする
     *
     * @param orderCode
     * @param administratorSeq
     * @return orderReceived
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ConfirmHMPaymentThenSettingPaidStatusUseCaseDto reflectDeposited(String orderCode,
                                                                            Integer administratorSeq) {

        // 受注より最新取引IDを取得
        OrderReceivedEntity orderReceivedEntity = orderReceivedRepository.getByOrderCode(new OrderCode(orderCode));
        // 対象受注が存在しない場合はエラー
        if (orderReceivedEntity == null) {
            throw new DomainException("ORDER-ODER0002-E", new String[] {orderCode});
        }

        // マルチペイメント決済結果が要入金反映処理か確認する
        MulPayResultConfirmDepositedResponse mulPayResultConfirmDepositedResponse =
                        mulPayAdapter.doConfirmDeposited(orderCode);
        if (mulPayResultConfirmDepositedResponse == null || !Boolean.TRUE.equals(
                        mulPayResultConfirmDepositedResponse.getRequiredReflectionProcessingFlag())) {
            return null;
        }

        // 取引を取得
        TransactionEntity transactionEntity = transactionRepository.get(orderReceivedEntity.getLatestTransactionId());
        // 取引が存在しない場合はエラー
        if (transactionEntity == null) {
            throw new DomainException(
                            "ORDER-TRAN0007-E", new String[] {orderReceivedEntity.getLatestTransactionId().getValue()});
        }
        // 入金済の場合は後続処理をスキップ
        if (transactionEntity.isPaidFlag()) {
            // 念のためマルチペイメント決済結果の入金登録済みフラグを更新しておく
            mulPayAdapter.updateDeposited(mulPayResultConfirmDepositedResponse.getMulPayResultSeq());
            return null;
        }

        /* 入金処理 */
        // 取引改訂を開始する
        String transactionRevisionId = startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(
                        orderReceivedEntity.getLatestTransactionId().getValue());

        // 改訂用請求伝票を入金済みにする
        DepositedResult depositedResult = billingAdapter.depositedForRevision(transactionRevisionId,
                                                                              mulPayResultConfirmDepositedResponse.getMulPayResultSeq()
                                                                             );
        // 改訂用請求伝票のレスポンスがない場合
        if (depositedResult == null) {
            throw new DomainException("ORDER-BILS0001-E", new String[] {transactionRevisionId});
        }

        // 改訂用取引を取得
        TransactionForRevisionEntity transactionForRevisionEntity =
                        transactionForRevisionRepository.get(new TransactionRevisionId(transactionRevisionId));
        // 改訂用取引が取得できない場合エラー
        if (transactionForRevisionEntity == null) {
            //取引取得失敗
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
        }

        // 改訂用取引の入金確定
        PaymentStatusDetail paymentStatusDetail = new PaymentStatusDetail(depositedResult.isInsufficientMoneyFlag(),
                                                                          depositedResult.isOverMoneyFlag()
        );
        transactionForRevisionEntity.openPayment(paymentStatusDetail);

        transactionForRevisionRepository.update(transactionForRevisionEntity);

        // 取引改訂を確定する（DB楽観ロックチェックあり）
        OrderReceivedEntity orderReceived =
                        openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(transactionRevisionId,
                                                                                             administratorSeq,
                                                                                             HTypeProcessType.PAYMENT,
                                                                                             true, false, false
                                                                                            );

        // マルペイ決済結果へ入金処理済みを反映（上記楽観チェックNGのロールバックを考慮してここで実施）
        mulPayAdapter.updateDeposited(mulPayResultConfirmDepositedResponse.getMulPayResultSeq());

        ConfirmHMPaymentThenSettingPaidStatusUseCaseDto resDto = new ConfirmHMPaymentThenSettingPaidStatusUseCaseDto();
        resDto.setOrderReceived(orderReceived);
        resDto.setPaymentStatusDetail(paymentStatusDetail);
        return resDto;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param useCaseDto
     */
    public void asyncAfterProcess(ConfirmHMPaymentThenSettingPaidStatusUseCaseDto useCaseDto) {

        if (useCaseDto != null) {

            // 取引改訂確定ユースケースの処理実行後非同期処理
            openTransactionReviseUseCaseExecuter.asyncAfterProcessInnerLogic(useCaseDto.getOrderReceived());

            // 取引にひもづく請求伝票を取得する
            BillingSlip billingSlip = billingAdapter.getBillingSlip(
                            useCaseDto.getOrderReceived().getLatestTransactionId().getValue());

            // リンク決済後日払いの場合
            if (billingSlip != null & "1".equals(billingSlip.getLinkPayType())) {

                // 入金完了メール送信
                if (useCaseDto.getPaymentStatusDetail() != null && !useCaseDto.getPaymentStatusDetail()
                                                                              .isInsufficientMoneyFlag()) {
                    List<String> orderCodeList = new ArrayList<>();
                    orderCodeList.add(useCaseDto.getOrderReceived().getOrderCode().getValue());
                    notificationSubAdapter.paymentDeposited(
                                    orderCodeList, useCaseDto.getPaymentStatusDetail().isOverMoneyFlag());
                }

                // 管理者へのアラートメール送信
                if (useCaseDto.getPaymentStatusDetail() != null && (
                                useCaseDto.getPaymentStatusDetail().isOverMoneyFlag()
                                || useCaseDto.getPaymentStatusDetail().isInsufficientMoneyFlag())) {
                    List<String> orderCodeAlertList = new ArrayList<>();
                    orderCodeAlertList.add(useCaseDto.getOrderReceived().getOrderCode().getValue());

                    boolean overFlag = useCaseDto.getPaymentStatusDetail().isOverMoneyFlag() ? true : false;
                    notificationSubAdapter.paymentExcessAlert(orderCodeAlertList, overFlag);
                }
            }
        }
    }
}