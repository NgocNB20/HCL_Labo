/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.entity.inquiry;

import jp.co.itechh.quad.admin.constant.type.HTypeInquiryRequestType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 問い合わせ内容エンティティクラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class InquiryDetailEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 問い合わせSEQ（必須） */
    private Integer inquirySeq;

    /** 連番（必須） */
    private Integer inquiryVersionNo;

    /** 発信者種別（必須） */
    private HTypeInquiryRequestType requestType;

    /** 問い合わせ日時（必須） */
    private Timestamp inquiryTime;

    /** 問い合わせ内容 */
    private String inquiryBody;

    /** 部署名 */
    private String divisionName;

    /** 担当者 */
    private String representative;

    /** 連絡先TEL */
    private String contactTel;

    /** 処理担当者 */
    private String operator;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}