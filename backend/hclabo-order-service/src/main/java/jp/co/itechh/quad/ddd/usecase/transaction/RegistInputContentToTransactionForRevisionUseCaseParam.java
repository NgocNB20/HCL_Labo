/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import lombok.Data;

import java.util.List;

/**
 * 入力内容を改訂用取引へ反映するユースケースパラメータ
 */
@Data
public class RegistInputContentToTransactionForRevisionUseCaseParam {

    /** 改訂用取引ID */
    private String transactionRevisionId;

    /** 管理メモ */
    private String adminMemo;

    /** 顧客ID */
    private String customerId;

    /** 入金関連通知実施フラグ */
    private boolean notificationFlag;

    /** 注文商品リスト */
    private List<OrderItemCountUseCaseParam> orderItemCountParamList;

    /** お届け先 */
    private ReceiverUseCaseParam receiverParam;

    /** 請求 */
    private BillingUseCaseParam billingUseCaseParam;

    /** ノベルティプレゼント判定状態 */
    private String noveltyPresentJudgmentStatus;

}
