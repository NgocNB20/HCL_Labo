/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.inquiry;

import jp.co.itechh.quad.admin.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryType;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class InquiryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 問い合わせSEQ（必須） */
    private Integer inquirySeq;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 問い合わせコード（必須） */
    private String inquiryCode;

    /** 問い合わせ者氏名(姓)（必須） */
    private String inquiryLastName;

    /** 問い合わせ者氏名(名） */
    private String inquiryFirstName;

    /** 問い合わせ者メールアドレス */
    private String inquiryMail;

    /** 問い合わせタイトル */
    private String inquiryTitle;

    /** 問い合わせ内容 */
    private String inquiryBody;

    /** 問い合わせ日時（必須） */
    private Timestamp inquiryTime;

    /** 問い合わせ状態（必須） */
    private HTypeInquiryStatus inquiryStatus = HTypeInquiryStatus.RECEIVING;

    /** 回答日時 */
    private Timestamp answerTime;

    /** 回答タイトル */
    private String answerTitle;

    /** 回答内容 */
    private String answerBody;

    /** 回答FROM */
    private String answerFrom;

    /** 回答TO */
    private String answerTo;

    /** 回答BCC */
    private String answerBcc;

    /** 問い合わせ分類SEQ (FK)（必須） */
    private Integer inquiryGroupSeq;

    /** 問い合わせ者氏名フリガナ(姓) */
    private String inquiryLastKana;

    /** 問い合わせ者氏名フリガナ(名) */
    private String inquiryFirstKana;

    /** 問い合わせ者住所-郵便番号 */
    private String inquiryZipCode;

    /** 問い合わせ者住所-都道府県 */
    private String inquiryPrefecture;

    /** 問い合わせ者住所-市区郡 */
    private String inquiryAddress1;

    /** 問い合わせ者住所-町村･番地 */
    private String inquiryAddress2;

    /** 問い合わせ者住所-それ以降の住所 */
    private String inquiryAddress3;

    /** 問い合わせ者TEL */
    private String inquiryTel;

    /** 問い合わせ者携帯電話番号 */
    private String inquiryMobileTel;

    /** 処理担当者名 */
    private String processPersonName;

    /** 更新カウンタ（必須） */
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** 問い合わせ種別（必須） */
    private HTypeInquiryType inquiryType;

    /** 会員SEQ */
    private Integer memberInfoSeq = 0;

    /** 注文番号 */
    private String orderCode;

    /** 初回問い合わせ日時（必須） */
    private Timestamp firstInquiryTime;

    /** 最終お客様問い合わせ日時（必須） */
    private Timestamp lastUserInquiryTime;

    /** 最終運用者問い合わせ日時（必須） */
    private Timestamp lastOperatorInquiryTime;

    /** 最終担当者（必須） */
    private String lastRepresentative;

    /** 管理メモ */
    private String memo;

    /** 連携メモ */
    private String cooperationMemo;

    /** 会員ID(TBL外項目) */
    private String memberInfoId;
}