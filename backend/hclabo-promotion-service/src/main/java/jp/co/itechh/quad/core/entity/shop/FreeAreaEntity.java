/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop;

import jp.co.itechh.quad.core.constant.type.HTypeFreeAreaOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteMapFlag;
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
 * フリーエリアクラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "FreeArea")
@Data
@Component
@Scope("prototype")
public class FreeAreaEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** フリーエリアSEQ（必須） */
    @Column(name = "freeAreaSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "freeAreaSeq")
    private Integer freeAreaSeq;

    /** ショップSEQ (FK)（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** フリーエリアKEY（必須） */
    @Column(name = "freeAreaKey")
    private String freeAreaKey;

    /** 公開開始日時（必須） */
    @Column(name = "openStartTime")
    private Timestamp openStartTime;

    /** フリーエリアタイトル */
    @Column(name = "freeAreaTitle")
    private String freeAreaTitle;

    /** フリーエリア本文PC */
    @Column(name = "freeAreaBodyPc")
    private String freeAreaBodyPc;

    /** 対象商品*/
    @Column(name = "targetGoods")
    private String targetGoods;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** 公開状態 ※一時テーブルの時に利用する */
    @Column(insertable = false, updatable = false)
    private HTypeFreeAreaOpenStatus freeAreaOpenStatus = HTypeFreeAreaOpenStatus.OPEN_PAST;

    /** サイトマップ出力フラグ（必須） */
    @Column(name = "siteMapFlag")
    private HTypeSiteMapFlag siteMapFlag = HTypeSiteMapFlag.OFF;

}
