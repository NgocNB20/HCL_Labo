/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.multipayment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * マルチペイメント請求
 * @author EntityGenerator
 *
 */
@Data
@Component
@Scope("prototype")
public class MulPayBillEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1651105988059870491L;

    /** ﾏﾙﾁﾍﾟｲﾒﾝﾄ請求SEQ */
    private Integer mulPayBillSeq;

    /** 決済方法 */
    private String payType;

    /** トランザクション種別 */
    private String tranType;

    /** 受注SEQ */
    private Integer orderSeq;

    /** 受注履歴連番 */
    private Integer orderVersionNo;

    /** オーダーID */
    private String orderId;

    /** 取引ID */
    private String accessId;

    /** 取引パスワード */
    private String accessPass;

    /** 処理区分 */
    private String jobCd;

    /** 支払方法 */
    private String method;

    /** 支払回数 */
    private Integer payTimes;

    /** カード登録連番モード */
    private String seqMode;

    /** カード登録連番 */
    private Integer cardSeq;

    /** 利用金額 */
    private BigDecimal amount;

    /** 税送料 */
    private BigDecimal tax;

    /** 3Dセキュア使用フラグ */
    private String tdFlag;

    /** ACS 呼出判定 */
    private String acs;

    /** 仕向先コード */
    private String forward;

    /** 承認番号 */
    private String approve;

    /** トランザクション ID */
    private String tranId;

    /** 決済日付 */
    private String tranDate;

    /** 支払先コンビニコード */
    private String convenience;

    /** 確認番号 */
    private String confNo;

    /** 受付番 */
    private String receiptNo;

    /** 支払期限日時 */
    private String paymentTerm;

    /** お客様番号 */
    private String custId;

    /** 収納機関番号 */
    private String bkCode;

    /** 暗号化決済番号 */
    private String encryptReceiptNo;

    /** メールアドレス */
    private String mailAddress;

    /** エラーコード */
    private String errCode;

    /** エラー情報 */
    private String errInfo;

    /** ペイメントURL */
    private String paymentURL;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}