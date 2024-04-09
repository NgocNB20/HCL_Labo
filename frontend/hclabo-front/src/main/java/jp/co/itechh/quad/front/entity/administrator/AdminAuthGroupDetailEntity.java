/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.entity.administrator;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 運営者権限グループ詳細エンティティ
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class AdminAuthGroupDetailEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** 運営者権限グループSEQ */
    private Integer adminAuthGroupSeq;

    /** 権限種別 */
    private String authTypeCode;

    /** 権限レベル */
    private Integer authLevel;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

}