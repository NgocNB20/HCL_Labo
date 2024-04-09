/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntityService;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;

/**
 * 下書き注文票チェックユースケース
 */
@Service
public class CheckDraftOrderSlipUseCase {

    /** 注文票ドメインサービス */
    private final OrderSlipEntityService orderSlipEntityService;

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** コンストラクタ */
    @Autowired
    public CheckDraftOrderSlipUseCase(OrderSlipEntityService orderSlipEntityService,
                                      IOrderSlipRepository orderSlipRepository) {
        this.orderSlipEntityService = orderSlipEntityService;
        this.orderSlipRepository = orderSlipRepository;
    }

    /**
     * 下書き注文票をチェックする
     *
     * @param customerId       顧客ID
     * @param transactionId    取引ID
     * @param customerBirthday 顧客生年月日
     */
    public void checkDraftOrderSlip(String customerId, String transactionId, Date customerBirthday) {

        // パラメータ取引IDが渡されているかどうかを判定
        if (StringUtils.isEmpty(transactionId)) {
            // =============================================
            // 取引IDが渡されていない⇒カートでのチェック
            // =============================================
            checkDraftOrderSlip(customerId, customerBirthday);
        } else {
            // =============================================
            // 取引IDが渡されている⇒注文フロー内でのチェック
            // =============================================
            checkDraftOrderSlipInTransaction(transactionId, customerBirthday);
        }
    }

    /**
     * 下書き注文票をチェックする
     *
     * @param customerId       顧客ID
     * @param customerBirthday 顧客生年月日
     */
    private void checkDraftOrderSlip(String customerId, Date customerBirthday) {
        // 顧客IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerId);

        // 下書き注文票の注文商品が取得できない場合は処理をスキップする
        if (orderSlipEntity == null || CollectionUtils.isEmpty(orderSlipEntity.getOrderItemList())) {
            return;
        }

        // 注文商品をチェック
        this.orderSlipEntityService.checkOrderItem(orderSlipEntity.getOrderItemList(), customerBirthday);
    }

    /**
     * 下書き注文票をチェックする
     *
     * @param transactionId    取引ID
     * @param customerBirthday 顧客生年月日
     */
    private void checkDraftOrderSlipInTransaction(String transactionId, Date customerBirthday) {
        // 取引IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getOrderSlipByTransactionId(transactionId);

        // 下書き注文票の注文商品が取得できない場合は処理をスキップする
        if (orderSlipEntity == null || CollectionUtils.isEmpty(orderSlipEntity.getOrderItemList())) {
            return;
        }

        // 注文商品をチェック（注文フロー用）
        this.orderSlipEntityService.checkOrderItemInTransaction(orderSlipEntity.getOrderItemList(), customerBirthday);
    }
}