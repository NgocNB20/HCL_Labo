/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.entity;

import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.Date;

/**
 * 受注エンティティ
 */
@Getter
public class OrderReceivedEntity {

    /** 受注ID */
    private OrderReceivedId orderReceivedId;

    /** 受注番号 */
    private OrderCode orderCode;

    /** 登録日時 */
    private Date registDate;

    /** 受注日時 */
    private Date orderReceivedDate;

    /** 取消日時 */
    private Date cancelDate;

    /** 最新取引ID */
    private TransactionId latestTransactionId;

    /**
     * コンストラクタ<br/>
     * 受注生成ファクトリ用
     *
     * @param registDate 登録日時
     * @param orderCode 受注番号
     */
    public OrderReceivedEntity(Date registDate, OrderCode orderCode) {

        // アサートチェック
        AssertChecker.assertNotNull("registDate is null", registDate);
        AssertChecker.assertNotNull("orderCode is null", orderCode);

        this.orderReceivedId = new OrderReceivedId();
        this.registDate = registDate;
        this.orderCode = orderCode;

    }

    /**
     * 受注日時設定
     *
     * @param orderReceivedDate 受注日時
     */
    public void settingOrderReceivedDate(Date orderReceivedDate) {

        // アサートチェック
        AssertChecker.assertNotNull("orderReceivedDate is null", orderReceivedDate);

        //設定
        this.orderReceivedDate = orderReceivedDate;
    }

    /**
     * 取消日時設定
     */
    public void settingCancelDate() {

        //設定
        this.cancelDate = new Date();
    }

    /**
     * 最新取引ID設定
     *
     * @param transactionId 取引ID
     */
    public void settingLatestTransaction(TransactionId transactionId) {

        // アサートチェック
        AssertChecker.assertNotNull("transactionId is null", transactionId);

        //設定
        this.latestTransactionId = transactionId;
    }

    /**
     * 受注番号再設定
     *
     * @param orderCode 受注番号
     */
    public void reSettingOrderCode(OrderCode orderCode) {

        // アサートチェック
        AssertChecker.assertNotNull("orderCode is null", orderCode);

        //設定
        this.orderCode = orderCode;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     **/
    public OrderReceivedEntity(OrderReceivedId orderReceivedId,
                               OrderCode orderCode,
                               Date registDate,
                               Date orderReceivedDate,
                               Date cancelDate,
                               TransactionId latestTransactionId) {
        this.orderReceivedId = orderReceivedId;
        this.orderCode = orderCode;
        this.registDate = registDate;
        this.orderReceivedDate = orderReceivedDate;
        this.cancelDate = cancelDate;
        this.latestTransactionId = latestTransactionId;
    }
}