/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.exception.ValidateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用取引に調整金額を追加する ユースケース
 */
@Service
public class AddAdjustmentAmountOfTransactionForRevisionUseCase {

    /** 販売伝票アダプター */
    private final ISalesSlipAdapter salesSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public AddAdjustmentAmountOfTransactionForRevisionUseCase(ISalesSlipAdapter salesSlipAdapter) {
        this.salesSlipAdapter = salesSlipAdapter;
    }

    /**
     * 改訂用取引に調整金額を追加する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param adjustName            調整項目
     * @param adjustPrice           調整金額
     */
    public void addAdjustmentAmountOfTransactionForRevision(String transactionRevisionId,
                                                            String adjustName,
                                                            Integer adjustPrice) {

        // バリデーションチェック
        ValidateException validateException = new ValidateException();
        if (StringUtils.isBlank(adjustName)) {
            validateException.addMessage("adjustName", "VALIDATE-REQUIRED-INPUT", new String[] {"調整項目"});
        }
        if (adjustPrice == null) {
            validateException.addMessage("adjustPrice", "VALIDATE-REQUIRED-INPUT", new String[] {"調整金額"});
        }
        if (validateException.hasMessage()) {
            throw validateException;
        }

        TransactionRevisionId transactionRevisionIdVo = new TransactionRevisionId(transactionRevisionId);

        // 改訂用販売伝票に調整金額を追加
        salesSlipAdapter.addAdjustmentAmountOfSalesSlipForRevision(transactionRevisionIdVo, adjustName, adjustPrice);

    }
}