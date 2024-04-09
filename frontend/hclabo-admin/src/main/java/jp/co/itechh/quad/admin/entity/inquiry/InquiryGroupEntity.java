/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.inquiry;

import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class InquiryGroupEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 問い合わせ分類SEQ（必須） */
    private Integer inquiryGroupSeq;

    /** ショップSEQ (FK)（必須） */
    private Integer shopSeq;

    /** 問い合わせ分類名（必須） */
    private String inquiryGroupName;

    /** 公開状態（必須） */
    private HTypeOpenDeleteStatus openStatus = HTypeOpenDeleteStatus.NO_OPEN;

    /** 表示順（必須） */
    private Integer orderDisplay;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}