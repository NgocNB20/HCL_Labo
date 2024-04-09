/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.administrator;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 運営者権限グループエンティティ
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class AdminAuthGroupEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -4233091931416476732L;

    /** 運営者権限グループSEQ */
    private Integer adminAuthGroupSeq;

    /** ショップSEQ */
    private Integer shopSeq;

    /** グループ表示名 */
    private String authGroupDisplayName;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

    // テーブル項目外追加フィールド

    /** 運営者権限グループに紐付けられた権限グループ詳細情報 */
    private List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList;

    /** 権限グループが編集不能なグループかどうか */
    private Boolean unmodifiableGroup;

}