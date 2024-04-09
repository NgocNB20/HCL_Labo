/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.RegistAddressParam;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.UpdateShippingConditionParam;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderItemCountParam;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.UpdateOrderSlipForRevisionParam;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 入力内容を改訂用取引へ反映するユースケース
 */
@Service
public class RegistInputContentToTransactionForRevisionUseCase {

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送伝票アダプター */
    private final IShippingSlipAdapter shippingSlipAdapter;

    /** 請求伝票アダプター */
    private final IBillingSlipAdapter billingSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public RegistInputContentToTransactionForRevisionUseCase(ITransactionForRevisionRepository transactionForRevisionRepository,
                                                             IOrderSlipAdapter orderAdapter,
                                                             IShippingSlipAdapter shippingAdapter,
                                                             IBillingSlipAdapter billingAdapter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.orderSlipAdapter = orderAdapter;
        this.shippingSlipAdapter = shippingAdapter;
        this.billingSlipAdapter = billingAdapter;
    }

    /**
     * 改訂用取引に入力内容を反映する
     *
     * @param useCaseParam 入力内容反映param
     */
    public void registInputContentToTransactionForRevision(RegistInputContentToTransactionForRevisionUseCaseParam useCaseParam) {

        // アサートチェック
        AssertChecker.assertNotNull("useCaseParam is null", useCaseParam);
        AssertChecker.assertNotEmpty(
                        "useCaseParam.orderItemCountParamList is empty", useCaseParam.getOrderItemCountParamList());

        TransactionRevisionId transactionRevisionId =
                        new TransactionRevisionId(useCaseParam.getTransactionRevisionId());

        // 改訂用注文票を更新する
        orderSlipAdapter.updateOrderSlipForRevision(toUpdateOrderSlipForRevisionParam(useCaseParam));

        // 改訂用配送伝票の配送条件更新
        shippingSlipAdapter.updateShippingConditionOfShippingSlipForRevision(
                        toUpdateShippingConditionParam(useCaseParam));

        // 改訂用配送伝票の配送先住所を更新する
        shippingSlipAdapter.updateShippingAddressOfShippingSlipForRevision(
                        transactionRevisionId, useCaseParam.getReceiverParam()
                                                           .getShippingAddressId());

        // 改訂用請求伝票の請求先住所を更新する
        billingSlipAdapter.updateBillingAddressOfBillingSlipForRevision(
                        transactionRevisionId, useCaseParam.getBillingUseCaseParam()
                                                           .getBillingAddressId());

        // 改訂用取引を取得
        TransactionForRevisionEntity transactionForRevisionEntity = transactionForRevisionRepository.get(
                        new TransactionRevisionId(useCaseParam.getTransactionRevisionId()));
        // 取引が取得できない場合エラー
        if (transactionForRevisionEntity == null) {
            // 取引取得失敗
            throw new DomainException("ORDER-TREV0009-E", new String[] {useCaseParam.getTransactionRevisionId()});
        }

        // 管理メモ設定
        transactionForRevisionEntity.settingAdminMemo(useCaseParam.getAdminMemo());

        // 入金通知実施設定
        transactionForRevisionEntity.settingNotification(useCaseParam.isNotificationFlag());

        // ノベルティプレゼント判定状態
        transactionForRevisionEntity.settingNoveltyPresentJudgmentStatus(
                        useCaseParam.getNoveltyPresentJudgmentStatus());

        // 改訂用取引を更新する
        transactionForRevisionRepository.update(transactionForRevisionEntity);

    }

    /**
     * 改訂用注文票更新 アダプターパラメータ
     *
     * @param useCaseParam
     * @return
     */
    private UpdateOrderSlipForRevisionParam toUpdateOrderSlipForRevisionParam(
                    RegistInputContentToTransactionForRevisionUseCaseParam useCaseParam) {

        // 戻り値生成
        UpdateOrderSlipForRevisionParam orderSlipForRevisionParam = new UpdateOrderSlipForRevisionParam();

        // 改訂用取引ID
        orderSlipForRevisionParam.setTransactionRevisionId(
                        new TransactionRevisionId(useCaseParam.getTransactionRevisionId()));

        // 注文商品
        List<OrderItemCountParam> orderItemCountInfraParamList = new ArrayList<>();

        for (OrderItemCountUseCaseParam orderItemCountUseCaseParam : useCaseParam.getOrderItemCountParamList()) {

            OrderItemCountParam orderItemCountInfraParam = new OrderItemCountParam();
            orderItemCountInfraParam.setOrderCount(orderItemCountUseCaseParam.getOrderCount());
            orderItemCountInfraParam.setOrderItemNo(orderItemCountUseCaseParam.getOrderItemSeq());

            orderItemCountInfraParamList.add(orderItemCountInfraParam);
        }
        orderSlipForRevisionParam.setOrderItemCountParamList(orderItemCountInfraParamList);

        return orderSlipForRevisionParam;
    }

    /**
     * 改訂用配送伝票の配送条件更新 アダプターパラメータ
     *
     * @param useCaseParam
     * @return
     */
    private UpdateShippingConditionParam toUpdateShippingConditionParam(
                    RegistInputContentToTransactionForRevisionUseCaseParam useCaseParam) {

        // 戻り値生成
        UpdateShippingConditionParam updateShippingConditionParam = new UpdateShippingConditionParam();

        // 改訂用取引ID
        updateShippingConditionParam.setTransactionRevisionId(
                        new TransactionRevisionId(useCaseParam.getTransactionRevisionId()));
        // 配送方法ID
        updateShippingConditionParam.setShippingMethodId(useCaseParam.getReceiverParam().getShippingMethodId());
        // お届け希望日
        updateShippingConditionParam.setReceiverDate(useCaseParam.getReceiverParam().getReceiverDate());
        // お届け希望時間帯
        updateShippingConditionParam.setReceiverTimeZone(useCaseParam.getReceiverParam().getReceiverTimeZone());
        // 納品書要否フラグ
        updateShippingConditionParam.setInvoiceNecessaryFlag(useCaseParam.getReceiverParam().isInvoiceNecessaryFlag());
        // 配送状況確認番号
        updateShippingConditionParam.setShipmentStatusConfirmCode(
                        useCaseParam.getReceiverParam().getShipmentStatusConfirmCode());

        return updateShippingConditionParam;
    }

    /**
     * 配送先住所登録パラメータへ変換
     *
     * @param useCaseParam
     * @return
     */
    private RegistAddressParam toRegistShippingAddressBookParam(RegistInputContentToTransactionForRevisionUseCaseParam useCaseParam) {

        // 戻り値生成
        RegistAddressParam registAddressBookParam = new RegistAddressParam();

        // 顧客ID
        registAddressBookParam.setCustomerId(useCaseParam.getCustomerId());
        // 住所ID
        registAddressBookParam.setAddressId(useCaseParam.getReceiverParam().getShippingAddressId());

        return registAddressBookParam;
    }

}