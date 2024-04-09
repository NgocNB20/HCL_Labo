package jp.co.itechh.quad.ddd.domain.order.adapter.model;

import lombok.Data;

import java.util.Date;

/**
 * 受注
 */
@Data
public class OrderReceived {

    /**
     * 受注番号
     */
    private String orderCode;

    /**
     * 最新取引ID
     */
    private String latestTransactionId;

    /**
     * 受注日時
     */
    private Date orderReceivedDate;

    /**
     * 受注日時
     */
    private Date cancelDate;

    /**
     * 注文ステータス
     */
    private String orderStatus;

    /**
     * 取引ステータス
     */
    private String transactionStatus;

    /**
     * 入金済みフラグ
     */
    private Boolean paidFlag;

    /**
     * 入金状態詳細
     */
    private String paymentStatusDetail;

    /**
     * 出荷済みフラグ
     */
    private Boolean shippedFlag;

    /**
     * 請求決済エラーフラグ
     */
    private Boolean billPaymentErrorFlag;

    /**
     * 入金関連通知実施フラグ
     */
    private Boolean notificationFlag;

    /**
     * 入金督促通知済みフラグ
     */
    private Boolean reminderSentFlag;

    /**
     * 入金期限切れ通知済みフラグ
     */
    private Boolean expiredSentFlag;

    /**
     * 管理メモ
     */
    private String adminMemo;

    /**
     * 顧客ID
     */
    private Integer customerId;

    /**
     * 処理日時
     */
    private Date processTime;

    /**
     * 処理種別
     */
    private String processType;

    /**
     * 処理担当者名
     */
    private String processPersonName;

    /**
     * ノベルティプレゼント判定状態
     */
    private String noveltyPresentJudgmentStatus;

}