/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.multipayment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * マルチペイメント決済結果通知受付サーブレットDto<br/>
 * ※フェーズ2のボトムアップ定義用に新規作成。MulPayNotificationReceiverLogic#creatMulPayResultEntityでの詰め替え項目を定義
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class MulPayNotificationReceiverDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップID */
    private String shopId;

    /** 取引ID(AccessID) ※マルチペイメント側発行 */
    private String accessId;

    /** オーダーID */
    private String orderId;

    /** 現状態 */
    private String status;

    /** 処理区分 */
    private String jobCd;

    /** 利用金額 */
    private BigDecimal amount;

    /** 税送料 */
    private BigDecimal tax;

    /** 通貨コード */
    private String currency;

    /** 仕向け先会社コード */
    private String forward;

    /** 支払方法 */
    private String method;

    /** 支払回数 */
    private Integer payTimes;

    /** トランザクションID */
    private String tranId;

    /** 承認番号 */
    private String approve;

    /** 処理日付 */
    private String tranDate;

    /** エラーコード */
    private String errCode;

    /** エラー詳細コード */
    private String errInfo;

    /** 決済方法 */
    private String payType;

    /** 支払先コンビニコード */
    private String cvsCode;

    /** CVS確認番号 */
    private String cvsConfNo;

    /** CVS受付番号 */
    private String cvsReceiptNo;

    /** お客様番号 */
    private String custId;

    /** 収納機関番号 */
    private String bkCode;

    /** 確認番号 */
    private String confNo;

    /** 支払期限日時 */
    private String paymentTerm;

    /** 暗号化決済番号 */
    private String encryptReceiptNo;

    /** 入金確定日時 */
    private String finishDate;

    /** 受付日時 */
    private String receiptDate;

    /** キャンセル金額 */
    private BigDecimal cancelAmount;

    /** キャンセル税送料金額 */
    private BigDecimal cancelTax;

    /** バーチャル口座あおぞら 累計入金額 */
    private Integer ganbTotalTransferAmount;
}
