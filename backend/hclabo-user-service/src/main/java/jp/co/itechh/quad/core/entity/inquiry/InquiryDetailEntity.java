/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.entity.inquiry;

import jp.co.itechh.quad.core.constant.type.HTypeInquiryRequestType;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 問い合わせ内容エンティティクラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "InquiryDetail")
@Data
@Component
@Scope("prototype")
public class InquiryDetailEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 問い合わせSEQ（必須） */
    @Column(name = "inquirySeq")
    @Id
    private Integer inquirySeq;

    /** 連番（必須） */
    @Column(name = "inquiryVersionNo")
    private Integer inquiryVersionNo;

    /** 発信者種別（必須） */
    @Column(name = "requestType")
    private HTypeInquiryRequestType requestType;

    /** 問い合わせ日時（必須） */
    @Column(name = "inquiryTime")
    private Timestamp inquiryTime;

    /** 問い合わせ内容 */
    @Column(name = "inquiryBody")
    private String inquiryBody;

    /** 部署名 */
    @Column(name = "divisionName")
    private String divisionName;

    /** 担当者 */
    @Column(name = "representative")
    private String representative;

    /** 連絡先TEL */
    @Column(name = "contactTel")
    private String contactTel;

    /** 処理担当者 */
    @Column(name = "operator")
    private String operator;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}