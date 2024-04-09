/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.usecase.transaction.service.OpenTransactionReviseUseCaseExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取引改訂確定ユースケース
 */
@Service
public class OpenTransactionReviseUseCase {

    /** 取引改訂確定ユースケース内部ロジック */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** コンストラクタ */
    @Autowired
    public OpenTransactionReviseUseCase(OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter) {
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
    }

    /**
     * 取引改訂を確定する</br>
     * ※プレゼン層からの呼び出し用（新規トランザクションのため呼出し元とは別トランになるので注意） </br>
     * ※呼出し元と同トランザクションで処理したい場合は、openTransactionReviseUseCaseService/asyncAfterProcessInnerLogicを直接呼ぶこと
     *
     * @param transactionRevisionId       改訂用取引ID
     * @param administratorSeq            運営者SEQ
     * @param processType                 HIT-MALLの処理種別
     * @param inventorySettlementSkipFlag 在庫決済スキップフラグ
     * @return OrderReceivedEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity openTransactionRevise(String transactionRevisionId,
                                                     Integer administratorSeq,
                                                     HTypeProcessType processType,
                                                     Boolean inventorySettlementSkipFlag) {

        return openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(transactionRevisionId,
                                                                                    administratorSeq, processType,
                                                                                    inventorySettlementSkipFlag, false,
                                                                                    false
                                                                                   );
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcess(OrderReceivedEntity orderReceivedEntity) {
        openTransactionReviseUseCaseExecuter.asyncAfterProcessInnerLogic(orderReceivedEntity);
    }
}