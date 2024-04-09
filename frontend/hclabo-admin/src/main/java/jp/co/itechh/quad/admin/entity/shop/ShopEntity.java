/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop;

import jp.co.itechh.quad.admin.constant.type.HTypeAutoApprovalFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ショップクラス
 *
 */
@Data
@Component
@Scope("prototype")
public class ShopEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** ショップID（必須） */
    private String shopId;

    /** ショップ名pc（必須） */
    private String shopNamePC;

    /** ショップurl-pc */
    private String urlPC;

    /** meta-description */
    private String metaDescription;

    /** meta-keyword */
    private String metaKeyword;

    /** 更新カウンタ */
    private Integer versionNo;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** 自動承認フラグ */
    private HTypeAutoApprovalFlag autoApprovalFlag;

}