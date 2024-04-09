/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 改訂用取引に注文商品を追加 ユースケース
 */
@Service
public class AddOrderItemToTransactionForRevisionUseCase {

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public AddOrderItemToTransactionForRevisionUseCase(IOrderSlipAdapter orderSlipAdapter) {
        this.orderSlipAdapter = orderSlipAdapter;
    }

    /**
     * 改訂用取引に注文商品を追加
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param itemIdList            商品IDリスト
     */
    public void addOrderItemToTransactionForRevision(String transactionRevisionId, List<String> itemIdList) {

        // バリデーションチェック
        ValidateException validateException = new ValidateException();
        if (CollectionUtils.isEmpty(itemIdList)) {
            validateException.addMessage("itemIdList", "VALIDATE-REQUIRED-SELECT", new String[] {"商品"});
        }
        if (validateException.hasMessage()) {
            throw validateException;
        }

        TransactionRevisionId transactionRevisionIdVo = new TransactionRevisionId(transactionRevisionId);

        // 改訂用注文票に注文商品を追加
        for (String itemId : itemIdList) {
            orderSlipAdapter.addOrderItemToOrderSlipForRevision(transactionRevisionIdVo, itemId, 1);
        }
    }

    /**
     * 改訂用取引に注文商品を追加
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param itemId                商品ID
     * @param itemCount             商品数量
     */
    public void addOrderItemToTransactionForRevision(String transactionRevisionId, String itemId, Integer itemCount) {

        // バリデーションチェック
        ValidateException validateException = new ValidateException();
        if (itemId == null || itemCount == null) {
            validateException.addMessage("itemId", "VALIDATE-REQUIRED-SELECT", new String[] {"商品"});
        }
        if (validateException.hasMessage()) {
            throw validateException;
        }

        TransactionRevisionId transactionRevisionIdVo = new TransactionRevisionId(transactionRevisionId);

        // 改訂用注文票に注文商品を追加
        orderSlipAdapter.addOrderItemToOrderSlipForRevision(transactionRevisionIdVo, itemId, itemCount);
    }
}