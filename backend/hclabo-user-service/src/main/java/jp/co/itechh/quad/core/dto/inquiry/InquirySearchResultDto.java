/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.inquiry;

import jp.co.itechh.quad.core.constant.type.HTypeInquiryStatus;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 問い合わせDao用検索結果Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class InquirySearchResultDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 問い合わせSEQ */
    private Integer inquirySeq;

    /** 問い合わせ状態 */
    private HTypeInquiryStatus inquiryStatus;

    /** 問い合わせコード */
    private String inquiryCode;

    /** 問い合わせ分類 */
    private String inquiryGroupSeq;

    /** 問い合わせ分類名 */
    private String inquiryGroupName;

    /** 問い合わせ者氏名(姓) */
    private String inquiryLastName;

    /** 問い合わせ者氏名(名） */
    private String inquiryFirstName;

    /** 問い合わせ日時 */
    private Timestamp inquiryTime;

    /** 回答日時 */
    private Timestamp answerTime;

    /**問い合わせ種別 */
    private String inquiryType;

    /** 注文番号 */
    private String orderCode;

    /** 電話番号 */
    private String inquiryTel;

    /** 初回お客様問い合わせ日時*/
    private Timestamp firstInquiryTime;

    /** 最終お客様問い合わせ日時*/
    private Timestamp lastUserInquiryTime;

    /** 担当者（最終担当者 )*/
    private String lastRepresentative;

    /** 連携メモ */
    private String cooperationMemo;

    /** 会員ID(メールアドレス) */
    private String memberInfoMail;
}