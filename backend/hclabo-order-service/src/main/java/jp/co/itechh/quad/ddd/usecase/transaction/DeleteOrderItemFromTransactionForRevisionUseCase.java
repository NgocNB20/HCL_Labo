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
 * 改訂用取引から注文商品を削除 ユースケース
 */
@Service
public class DeleteOrderItemFromTransactionForRevisionUseCase {

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public DeleteOrderItemFromTransactionForRevisionUseCase(IOrderSlipAdapter orderSlipAdapter) {
        this.orderSlipAdapter = orderSlipAdapter;
    }

    /**
     * 改訂用取引から注文商品を削除
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param itemSeqList           削除対象商品の注文商品連番リスト
     */
    public void deleteOrderItemToTransactionForRevision(String transactionRevisionId, List<Integer> itemSeqList) {

        // バリデーションチェック
        ValidateException validateException = new ValidateException();
        if (CollectionUtils.isEmpty(itemSeqList)) {
            validateException.addMessage("itemSeqList", "VALIDATE-REQUIRED-SELECT", new String[] {"商品"});
        }
        if (validateException.hasMessage()) {
            throw validateException;
        }

        TransactionRevisionId transactionRevisionIdVo = new TransactionRevisionId(transactionRevisionId);

        // 改訂用注文票から注文商品を削除
        for (Integer itemSeq : itemSeqList) {
            orderSlipAdapter.deleteOrderItemToOrderSlipForRevision(transactionRevisionIdVo, itemSeq);
        }
    }

}