/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.multipayment;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * マルチペイメント請求
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "MulPayBill")
@Data
@Component
@Scope("prototype")
public class MulPayBillEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1651105988059870491L;

    /** ﾏﾙﾁﾍﾟｲﾒﾝﾄ請求SEQ */
    @Column(name = "mulPayBillSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "mulPayBillSeq")
    private Integer mulPayBillSeq;

    /** 決済方法 */
    @Column(name = "payType")
    private String payType;

    /** トランザクション種別 */
    @Column(name = "tranType")
    private String tranType;

    /** 決済手段識別子 */
    @Column(name = "paymethod")
    private String payMethod;

    /** 注文決済ID */
    @Column(name = "orderPaymentId")
    private String orderPaymentId;

    /** オーダーID */
    @Column(name = "orderId")
    private String orderId;

    /** 取引ID */
    @Column(name = "accessId")
    private String accessId;

    /** 取引パスワード */
    @Column(name = "accessPass")
    private String accessPass;

    /** リンクタイプPlus処理結果 */
    @Column(name = "result")
    private String result;

    /** 処理区分 */
    @Column(name = "jobCd")
    private String jobCd;

    /** 支払方法 */
    @Column(name = "method")
    private String method;

    /** 支払回数 */
    @Column(name = "payTimes")
    private Integer payTimes;

    /** カード登録連番モード */
    @Column(name = "seqMode")
    private String seqMode;

    /** カード登録連番 */
    @Column(name = "cardSeq")
    private Integer cardSeq;

    /** 利用金額 */
    @Column(name = "amount")
    private BigDecimal amount;

    /** 税送料 */
    @Column(name = "tax")
    private BigDecimal tax;

    /** 3Dセキュア使用フラグ */
    @Column(name = "tdFlag")
    private String tdFlag;

    /** ACS 呼出判定 */
    @Column(name = "acs")
    private String acs;

    /** 仕向先コード */
    @Column(name = "forward")
    private String forward;

    /** 承認番号 */
    @Column(name = "approve")
    private String approve;

    /** トランザクション ID */
    @Column(name = "tranId")
    private String tranId;

    /** 決済日付 */
    @Column(name = "tranDate")
    private String tranDate;

    /** 支払先コンビニコード */
    @Column(name = "convenience")
    private String convenience;

    /** 確認番号 */
    @Column(name = "confNo")
    private String confNo;

    /** 受付番 */
    @Column(name = "receiptNo")
    private String receiptNo;

    /** 支払期限日時 */
    @Column(name = "paymentTerm")
    private String paymentTerm;

    /** お客様番号 */
    @Column(name = "custId")
    private String custId;

    /** 収納機関番号 */
    @Column(name = "bkCode")
    private String bkCode;

    /** 暗号化決済番号 */
    @Column(name = "encryptReceiptNo")
    private String encryptReceiptNo;

    /** 取引有効期限 */
    @Column(name = "expireDate")
    private String expireDate;

    /** 振込事由 */
    @Column(name = "tradeReason")
    private String tradeReason;

    /** 振込依頼者氏名 */
    @Column(name = "tradeClientName")
    private String tradeClientName;

    /** 振込依頼者氏名 */
    @Column(name = "tradeClientMailAddress")
    private String tradeClientMailAddress;

    /** 銀行コード */
    @Column(name = "bankCode")
    private String bankCode;

    /** 銀行名 */
    @Column(name = "bankName")
    private String bankName;

    /** 支店コード */
    @Column(name = "branchCode")
    private String branchCode;

    /** 支店名 */
    @Column(name = "branchName")
    private String branchName;

    /** 振込先口座種別 */
    @Column(name = "accountType")
    private String accountType;

    /** 振込先口座番号 */
    @Column(name = "accountNumber")
    private String accountNumber;

    /** エラーコード */
    @Column(name = "errCode")
    private String errCode;

    /** エラー情報 */
    @Column(name = "errInfo")
    private String errInfo;

    /** ペイメントURL */
    @Column(name = "paymentURL")
    private String paymentURL;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}