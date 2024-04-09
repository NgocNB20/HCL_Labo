/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.inquiry;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeInquiryStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 問い合わせDao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class InquirySearchDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 問い合わせ分類SEQ */
    private Integer inquiryGroupSeq;

    /** 問い合わせ状態 */
    private HTypeInquiryStatus inquiryStatus;

    /** 問い合わせコード */
    private String inquiryCode;

    /** 名前 */
    private String inquiryName;

    /** E-Mail */
    private String inquiryMail;

    /** 問い合わせ日時From */
    private Timestamp inquiryTimeFrom;

    /** 問い合わせ日時To */
    private Timestamp inquiryTimeTo;

    /**問い合わせ種別 */
    private String inquiryType;

    /** 注文番号 */
    private String orderCode;

    /** 電話番号 */
    private String inquiryTel;

    /** 担当者（最終担当者 )*/
    private String lastRepresentative;

    /** 連携メモ */
    private String cooperationMemo;

    /** 会員ID(メールアドレス) */
    private String memberInfoMail;

    /** 会員Seq */
    private Integer memberInfoSeq;
}