/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.analytics.adapter.IAggregateSalesAdapter;
import jp.co.itechh.quad.ddd.domain.analytics.adapter.IOrderSearchAdapter;
import jp.co.itechh.quad.ddd.domain.customize.adapter.IExaminationAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.OpenResult;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionReflectDepositedRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 取引確定ユースケース
 */
@Service
public class OpenTransactionUseCase {

    /** 通知失敗結果 */
    private final String FAILED = "1";

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** 配送アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 販売アダプター */
    private final ISalesSlipAdapter salesAdapter;

    /** 注文アダプター */
    private final IOrderSlipAdapter orderAdapter;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 受注検索アダプター */
    private final IOrderSearchAdapter orderSearchAdapter;

    /** 集計用販売アダプター */
    private final IAggregateSalesAdapter aggregateSalesAdapter;

    /** 検査アダプター */
    private final IExaminationAdapter examinationAdapter;

    /** 受注を入金済みにする実行クラス */
    private final ConfirmHMPaymentThenSettingPaidStatusUseCase confirmPaymentThenSettingPaidStatusUseCase;

    /** 取引API */
    private final TransactionApi transactionApi;

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenTransactionUseCase.class);

    /** コンストラクタ */
    @Autowired
    public OpenTransactionUseCase(ITransactionRepository iTransactionRepository,
                                  IOrderReceivedRepository orderReceivedRepository,
                                  IBillingSlipAdapter billingAdapter,
                                  IShippingSlipAdapter shippingAdapter,
                                  ISalesSlipAdapter salesAdapter,
                                  IOrderSlipAdapter orderAdapter,
                                  INotificationSubAdapter notificationSubAdapter,
                                  IOrderSearchAdapter orderSearchAdapter,
                                  IAggregateSalesAdapter aggregateSalesAdapter,
                                  IExaminationAdapter examinationAdapter,
                                  ConfirmHMPaymentThenSettingPaidStatusUseCase confirmPaymentThenSettingPaidStatusUseCase,
                                  TransactionApi transactionApi) {
        this.transactionRepository = iTransactionRepository;
        this.orderReceivedRepository = orderReceivedRepository;
        this.billingAdapter = billingAdapter;
        this.shippingAdapter = shippingAdapter;
        this.salesAdapter = salesAdapter;
        this.orderAdapter = orderAdapter;
        this.notificationSubAdapter = notificationSubAdapter;
        this.orderSearchAdapter = orderSearchAdapter;
        this.aggregateSalesAdapter = aggregateSalesAdapter;
        this.examinationAdapter = examinationAdapter;
        this.confirmPaymentThenSettingPaidStatusUseCase = confirmPaymentThenSettingPaidStatusUseCase;
        this.transactionApi = transactionApi;
    }

    /**
     * 取引を確定する
     *
     * @param transactionId 取引ID
     * @return orderReceivedEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity openTransaction(String transactionId) {

        // ***ここから、各サービスの取引処理を下記の順番で実行***
        OpenResult openResult = null;
        // ①：請求伝票を確定し、確定結果を取得する（決済サービス）
        openResult = this.billingAdapter.openBillingSlip(new TransactionId(transactionId));

        // ②：配送伝票を確定（物流サービス）
        this.shippingAdapter.openShippingSlip(new TransactionId(transactionId));

        // ③：販売伝票を確定（販売企画サービス）
        this.salesAdapter.openSalesSlip(new TransactionId(transactionId));

        // ④：注文票を確定（プロモーションサービス）
        this.orderAdapter.openOrderSlip(new TransactionId(transactionId));

        // ***ここまで、各サービスの取引処理***

        // 取引を取得
        TransactionEntity transactionEntity = transactionRepository.get(new TransactionId(transactionId));
        // 取引が存在しない、または下書き状態でない場合は処理をスキップする
        if (transactionEntity == null || transactionEntity.getTransactionStatus() != TransactionStatus.DRAFT) {
            return null;
        }

        // 取引を確定する
        transactionEntity.openTransaction();

        // 受注日時
        Date orderReceivedDate = new Date();

        // 処理履歴用情報を設定する
        transactionEntity.settingProcessHistoryInfo(orderReceivedDate, HTypeProcessType.ORDER, null);

        // 入金関連通知実施フラグがtrueの場合
        if (openResult != null && openResult.isNotificationFlag()) {
            // 入金関連通知を設定する
            transactionEntity.settingNotification(true);
        }

        // 前請求フラグを設定
        if (openResult != null && openResult.isPreClaimFlag()) {
            transactionEntity.settingPreClaim(openResult.isPreClaimFlag());
        }

        // 取引を更新する
        transactionRepository.update(transactionEntity);

        // 受注を取得
        OrderReceivedEntity orderReceivedEntity = orderReceivedRepository.get(transactionEntity.getOrderReceivedId());
        // 受注が存在しない場合はエラー
        if (ObjectUtils.isEmpty(transactionEntity)) {
            throw new DomainException(
                            "ORDER-ODER0002-E", new String[] {transactionEntity.getOrderReceivedId().getValue()});
        }

        // 受注日時を設定
        orderReceivedEntity.settingOrderReceivedDate(orderReceivedDate);

        // 最新取引IDを更新
        orderReceivedEntity.settingLatestTransaction(transactionEntity.getTransactionId());

        // 取引を更新する
        orderReceivedRepository.updateWithTranCheck(orderReceivedEntity, null);

        // 取引IDで注文票を取得する（プロモーションサービス）
        OrderSlip orderSlip = this.orderAdapter.getOrderSlipByTransactionId(new TransactionId(transactionId));

        // 注文商品IDのリストを作成する
        List<String> orderItemIdList = orderSlip.getItemList().stream().map(OrderSlipItem::getOrderItemId)
            .collect(Collectors.toList());

        // 検査キットを新規登録する（兵庫臨床カスタマイズサービス）
        this.examinationAdapter.registExamKit(orderReceivedEntity.getOrderCode().getValue(), orderItemIdList);

        return orderReceivedEntity;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcess(OrderReceivedEntity orderReceivedEntity) {

        if (orderReceivedEntity != null) {
            // ユーザーに通知する
            notificationSubAdapter.orderConfirmation(orderReceivedEntity.getOrderCode());

            // 確定した受注データを受注検索用に分析サービスへ登録・更新する
            orderSearchAdapter.registUpdateOrderSearch(orderReceivedEntity.getOrderReceivedId());

            // 集計用販売データ登録
            // 改訂前取引ID　※フロントサイトからの新規受注の場合未設定
            aggregateSalesAdapter.aggregateSalesData(orderReceivedEntity.getLatestTransactionId().getValue(), null);

            // HM入金結果データを確認して受注を入金済みにする
            try {
                ConfirmHMPaymentThenSettingPaidStatusUseCaseDto useCaseDto =
                                confirmPaymentThenSettingPaidStatusUseCase.reflectDeposited(
                                                orderReceivedEntity.getOrderCode().getValue(), null);
                confirmPaymentThenSettingPaidStatusUseCase.asyncAfterProcess(useCaseDto);
            } catch (Exception e) {

                LOGGER.warn("OpenTransactionUseCase#afterProcess: orderCode [" + orderReceivedEntity.getOrderCode()
                            + "] 注文確定時の入金処理に失敗したため、MQへ非同期入金処理リトライを依頼しました。", e);

                // 失敗した場合、リトライ処理をするため 「HM入金結果データを確認して受注を入金済みにする」をMQへ依頼（MQは同サービスでもAPI呼出し）
                TransactionReflectDepositedRequest transactionReflectDepositedRequest =
                                new TransactionReflectDepositedRequest();
                transactionReflectDepositedRequest.setOrderCode(orderReceivedEntity.getOrderCode().getValue());
                transactionApi.reflectDeposited(transactionReflectDepositedRequest);
            }
        }
    }
}