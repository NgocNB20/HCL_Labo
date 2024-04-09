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
 * @author EntityGenerator
 *
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

    /** 受注SEQ */
    @Column(name = "orderSeq")
    private Integer orderSeq;

    /** 受注履歴連番 */
    @Column(name = "orderVersionNo")
    private Integer orderVersionNo;

    /** オーダーID */
    @Column(name = "orderId")
    private String orderId;

    /** 取引ID */
    @Column(name = "accessId")
    private String accessId;

    /** 取引パスワード */
    @Column(name = "accessPass")
    private String accessPass;

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

    /** メールアドレス */
    @Column(name = "mailAddress")
    private String mailAddress;

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
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}