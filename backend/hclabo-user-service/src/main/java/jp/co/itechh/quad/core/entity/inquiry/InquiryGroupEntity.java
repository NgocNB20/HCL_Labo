/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.inquiry;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
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
import java.sql.Timestamp;

/**
 * 問い合わせ分類クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "InquiryGroup")
@Data
@Component
@Scope("prototype")
public class InquiryGroupEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 問い合わせ分類SEQ（必須） */
    @Column(name = "inquiryGroupSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "inquiryGroupSeq")
    private Integer inquiryGroupSeq;

    /** ショップSEQ (FK)（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 問い合わせ分類名（必須） */
    @Column(name = "inquiryGroupName")
    private String inquiryGroupName;

    /** 公開状態（必須） */
    @Column(name = "openStatus")
    private HTypeOpenDeleteStatus openStatus = HTypeOpenDeleteStatus.NO_OPEN;

    /** 表示順（必須） */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}