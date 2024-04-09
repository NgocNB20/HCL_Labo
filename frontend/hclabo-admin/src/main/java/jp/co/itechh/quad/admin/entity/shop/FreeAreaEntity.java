/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop;

import jp.co.itechh.quad.admin.constant.type.HTypeFreeAreaOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * フリーエリアクラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class FreeAreaEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** フリーエリアSEQ（必須） */
    private Integer freeAreaSeq;

    /** ショップSEQ (FK)（必須） */
    private Integer shopSeq;

    /** フリーエリアKEY（必須） */
    private String freeAreaKey;

    /** 公開開始日時（必須） */
    private Timestamp openStartTime;

    /** フリーエリアタイトル */
    private String freeAreaTitle;

    /** フリーエリア本文PC */
    private String freeAreaBodyPc;

    /** 対象商品*/
    private String targetGoods;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** 公開状態 ※一時テーブルの時に利用する */
    private HTypeFreeAreaOpenStatus freeAreaOpenStatus = HTypeFreeAreaOpenStatus.OPEN_PAST;

    /** サイトマップ出力フラグ（必須） */
    private HTypeSiteMapFlag siteMapFlag = HTypeSiteMapFlag.OFF;

}