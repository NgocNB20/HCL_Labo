/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.multipayment;

import jp.co.itechh.quad.core.constant.type.HTypeProcessedFlag;
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
 * ﾏﾙﾁﾍﾟｲﾒﾝﾄ決済結果
 * @author EntityGenerator
 *
 */
@Entity
@Table(name = "MulPayResult")
@Data
@Component
@Scope("prototype")
public class MulPayResultEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -9190927375006207185L;

    /** ﾏﾙﾁﾍﾟｲﾒﾝﾄ決済結果SEQ */
    @Column(name = "mulPayResultSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "mulPayResultSeq")
    private Integer mulPayResultSeq;

    /** 受信方法 */
    @Column(name = "receiveMode")
    private String receiveMode;

    /** 入金処理済みフラグ */
    @Column(name = "processedFlag")
    private HTypeProcessedFlag processedFlag;

    /** ショップSEQ */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 受注SEQ */
    @Column(name = "orderSeq")
    private Integer orderSeq;

    /** オーダーID */
    @Column(name = "orderId")
    private String orderId;

    /** 現状態 */
    @Column(name = "status")
    private String status;

    /** 処理区分 */
    @Column(name = "jobCd")
    private String jobCd;

    /** 処理日時 */
    @Column(name = "processDate")
    private String processDate;

    /** 商品コード */
    @Column(name = "itemCode")
    private String itemCode;

    /** 利用金額 */
    @Column(name = "amount")
    private BigDecimal amount;

    /** 税送料 */
    @Column(name = "tax")
    private BigDecimal tax;

    /** サイトID */
    @Column(name = "siteId")
    private String siteId;

    /** 会員ID */
    @Column(name = "memberId")
    private String memberId;

    /** カード番号 */
    @Column(name = "cardNo")
    private String cardNo;

    /** カード有効期限 */
    @Column(name = "expire")
    private String expire;

    /** 通貨コード */
    @Column(name = "currency")
    private String currency;

    /** 仕向け先会社コード */
    @Column(name = "forward")
    private String forward;

    /** 支払方法 */
    @Column(name = "method")
    private String method;

    /** 支払回数 */
    @Column(name = "payTimes")
    private Integer payTimes;

    /** トランザクションID */
    @Column(name = "tranId")
    private String tranId;

    /** 承認番号 */
    @Column(name = "approve")
    private String approve;

    /** 処理日付 */
    @Column(name = "tranDate")
    private String tranDate;

    /** エラーコード */
    @Column(name = "errCode")
    private String errCode;

    /** エラー詳細コード */
    @Column(name = "errInfo")
    private String errInfo;

    /** 加盟店自由項目1 */
    @Column(name = "clientField1")
    private String clientField1;

    /** 加盟店自由項目2 */
    @Column(name = "clientField2")
    private String clientField2;

    /** 加盟店自由項目3 */
    @Column(name = "clientField3")
    private String clientField3;

    /** 決済方法 */
    @Column(name = "payType")
    private String payType;

    /** 支払先コンビニコード */
    @Column(name = "cvsCode")
    private String cvsCode;

    /** CVS確認番号 */
    @Column(name = "cvsConfNo")
    private String cvsConfNo;

    /** CVS受付番号 */
    @Column(name = "cvsReceiptNo")
    private String cvsReceiptNo;

    /** お客様番号 */
    @Column(name = "custId")
    private String custId;

    /** 収納機関番号 */
    @Column(name = "bkCode")
    private String bkCode;

    /**確認番号  */
    @Column(name = "confNo")
    private String confNo;

    /** 支払期限日時 */
    @Column(name = "paymentTerm")
    private String paymentTerm;

    /** 暗号化決済番号 */
    @Column(name = "encryptReceiptNo")
    private String encryptReceiptNo;

    /** 入金確定日時 */
    @Column(name = "finishDate")
    private String finishDate;

    /** 受付日時 */
    @Column(name = "receiptDate")
    private String receiptDate;

    /**登録日時  */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}