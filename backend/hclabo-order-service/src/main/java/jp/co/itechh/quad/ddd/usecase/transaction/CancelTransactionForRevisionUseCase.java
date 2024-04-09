/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.usecase.transaction.service.CancelTransactionForRevisionUseCaseExecuter;
import jp.co.itechh.quad.ddd.usecase.transaction.service.StartTransactionReviseUseCaseExecuter;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 改訂用取引取消ユースケース
 */
@Service
public class CancelTransactionForRevisionUseCase {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CancelTransactionForRevisionUseCase.class);

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 改訂用取引取消ユースケース内部ロジック */
    private final CancelTransactionForRevisionUseCaseExecuter cancelTransactionForRevisionUseCaseExecuter;

    /** 改訂用取引のクーポンを無効化する ユースケース */
    private final DisableCouponOfTransactionForRevisionUseCase disableCouponOfTransactionForRevisionUseCase;

    /** 改訂用取引のクーポンを有効化する ユースケース */
    private final EnableCouponOfTransactionForRevisionUseCase enableCouponOfTransactionForRevisionUseCase;

    /** コンストラクタ */
    @Autowired
    public CancelTransactionForRevisionUseCase(StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter,
                                               CancelTransactionForRevisionUseCaseExecuter cancelTransactionForRevisionUseCaseExecuter,
                                               DisableCouponOfTransactionForRevisionUseCase disableCouponOfTransactionForRevisionUseCase,
                                               EnableCouponOfTransactionForRevisionUseCase enableCouponOfTransactionForRevisionUseCase) {
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
        this.cancelTransactionForRevisionUseCaseExecuter = cancelTransactionForRevisionUseCaseExecuter;
        this.disableCouponOfTransactionForRevisionUseCase = disableCouponOfTransactionForRevisionUseCase;
        this.enableCouponOfTransactionForRevisionUseCase = enableCouponOfTransactionForRevisionUseCase;
    }

    /**
     * 改訂用取引を取消する
     *
     * @param transactionId 改訂取引ID
     * @param adminMemo     管理メモ
     * @param couponDisableFlag    クーポン無効化フラグ
     * @return OrderReceivedEntity order received entity
     * @param transactionId                        改訂取引ID
     * @param adminMemo                            管理メモ
     * @param messageMapForResponse                警告メッセージ
     * @return OrderReceivedEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity cancelTransactionForRevision(String transactionId,
                                                            String adminMemo,
                                                            Boolean couponDisableFlag,
                                                            Map<String, List<WarningContent>> messageMapForResponse) {

        // 取引改訂を開始する
        String transactionRevisionId =
                        startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(transactionId);

        // クーポンが有効の場合(クーポンが表示されていて、割引されている)場合
        if (couponDisableFlag != null) {
            if (couponDisableFlag) {
                // 無効化の場合
                disableCouponOfTransactionForRevisionUseCase.disableCouponOfTransactionForRevision(
                                transactionRevisionId);
            } else {
                // 有効化の場合
                enableCouponOfTransactionForRevisionUseCase.enableCouponOfTransactionForRevision(transactionRevisionId);
            }
        }
        // 改訂用取引を取消する（内部で取引確定実施）
        OrderReceivedEntity orderReceivedEntity =
                        cancelTransactionForRevisionUseCaseExecuter.cancelTransactionForRevisionInnerLogic(
                                        transactionRevisionId, adminMemo, messageMapForResponse);

        return orderReceivedEntity;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcess(OrderReceivedEntity orderReceivedEntity) {
        cancelTransactionForRevisionUseCaseExecuter.asyncAfterProcessInnerLogic(orderReceivedEntity);
    }
}