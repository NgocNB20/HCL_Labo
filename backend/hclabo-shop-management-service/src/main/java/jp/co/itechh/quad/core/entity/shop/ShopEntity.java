/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop;

import jp.co.itechh.quad.core.constant.type.HTypeAutoApprovalFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ショップクラス
 *
 */
@Entity
@Table(name = "Shop")
@Data
@Component
@Scope("prototype")
public class ShopEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "shopSeq")
    private Integer shopSeq;

    /** ショップID（必須） */
    @Column(name = "shopId")
    private String shopId;

    /** ショップ名pc（必須） */
    @Column(name = "shopNamePC")
    private String shopNamePC;

    /** ショップurl-pc */
    @Column(name = "urlPC")
    private String urlPC;

    /** meta-description */
    @Column(name = "metaDescription")
    private String metaDescription;

    /** meta-keyword */
    @Column(name = "metaKeyword")
    private String metaKeyword;

    /** 更新カウンタ */
    @Version
    @Column(name = "versionNo")
    private Integer versionNo;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** 自動承認フラグ */
    @Column(name = "autoApprovalFlag")
    private HTypeAutoApprovalFlag autoApprovalFlag;

}
