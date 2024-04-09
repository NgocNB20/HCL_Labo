/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.inquiry;

import jp.co.itechh.quad.core.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.core.constant.type.HTypeInquiryType;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 問い合わせクラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.00 $
 */
@Entity
@Table(name = "Inquiry")
@Data
@Component
@Scope("prototype")
public class InquiryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 問い合わせSEQ（必須） */
    @Column(name = "inquirySeq")
    @Id
    private Integer inquirySeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 問い合わせコード（必須） */
    @Column(name = "inquiryCode")
    private String inquiryCode;

    /** 問い合わせ者氏名(姓)（必須） */
    @Column(name = "inquiryLastName")
    private String inquiryLastName;

    /** 問い合わせ者氏名(名） */
    @Column(name = "inquiryFirstName")
    private String inquiryFirstName;

    /** 問い合わせ者メールアドレス */
    @Column(name = "inquiryMail")
    private String inquiryMail;

    /** 問い合わせタイトル */
    @Column(name = "inquiryTitle")
    private String inquiryTitle;

    /** 問い合わせ内容 */
    @Column(name = "inquiryBody")
    private String inquiryBody;

    /** 問い合わせ日時（必須） */
    @Column(name = "inquiryTime")
    private Timestamp inquiryTime;

    /** 問い合わせ状態（必須） */
    @Column(name = "inquiryStatus")
    private HTypeInquiryStatus inquiryStatus = HTypeInquiryStatus.RECEIVING;

    /** 回答日時 */
    @Column(name = "answerTime")
    private Timestamp answerTime;

    /** 回答タイトル */
    @Column(name = "answerTitle")
    private String answerTitle;

    /** 回答内容 */
    @Column(name = "answerBody")
    private String answerBody;

    /** 回答FROM */
    @Column(name = "answerFrom")
    private String answerFrom;

    /** 回答TO */
    @Column(name = "answerTo")
    private String answerTo;

    /** 回答BCC */
    @Column(name = "answerBcc")
    private String answerBcc;

    /** 問い合わせ分類SEQ (FK)（必須） */
    @Column(name = "inquiryGroupSeq")
    private Integer inquiryGroupSeq;

    /** 問い合わせ者氏名フリガナ(姓) */
    @Column(name = "inquiryLastKana")
    private String inquiryLastKana;

    /** 問い合わせ者氏名フリガナ(名) */
    @Column(name = "inquiryFirstKana")
    private String inquiryFirstKana;

    /** 問い合わせ者住所-郵便番号 */
    @Column(name = "inquiryZipCode")
    private String inquiryZipCode;

    /** 問い合わせ者住所-都道府県 */
    @Column(name = "inquiryPrefecture")
    private String inquiryPrefecture;

    /** 問い合わせ者住所-市区郡 */
    @Column(name = "inquiryAddress1")
    private String inquiryAddress1;

    /** 問い合わせ者住所-町村･番地 */
    @Column(name = "inquiryAddress2")
    private String inquiryAddress2;

    /** 問い合わせ者住所-それ以降の住所 */
    @Column(name = "inquiryAddress3")
    private String inquiryAddress3;

    /** 問い合わせ者TEL */
    @Column(name = "inquiryTel")
    private String inquiryTel;

    /** 問い合わせ者携帯電話番号 */
    @Column(name = "inquiryMobileTel")
    private String inquiryMobileTel;

    /** 処理担当者名 */
    @Column(name = "processPersonName")
    private String processPersonName;

    /** 更新カウンタ（必須） */
    @Version
    @Column(name = "versionNo")
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** 問い合わせ種別（必須） */
    @Column(name = "inquiryType")
    private HTypeInquiryType inquiryType;

    /** 会員SEQ */
    @Column(name = "memberInfoSeq")
    private Integer memberInfoSeq = 0;

    /** 注文番号 */
    @Column(name = "orderCode")
    private String orderCode;

    /** 初回問い合わせ日時（必須） */
    @Column(name = "firstInquiryTime")
    private Timestamp firstInquiryTime;

    /** 最終お客様問い合わせ日時（必須） */
    @Column(name = "lastUserInquiryTime")
    private Timestamp lastUserInquiryTime;

    /** 最終運用者問い合わせ日時（必須） */
    @Column(name = "lastOperatorInquiryTime")
    private Timestamp lastOperatorInquiryTime;

    /** 最終担当者（必須） */
    @Column(name = "lastRepresentative")
    private String lastRepresentative;

    /** 管理メモ */
    @Column(name = "memo")
    private String memo;

    /** 連携メモ */
    @Column(name = "cooperationMemo")
    private String cooperationMemo;

    /** 会員ID(TBL外項目) */
    @Column(insertable = false, updatable = false)
    private String memberInfoId;
}