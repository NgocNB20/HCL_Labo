/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.administrator;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 運営者権限グループエンティティ
 * @author EntityGenerator
 */
@Entity
@Table(name = "AdminAuthGroup")
@Data
@Component
@Scope("prototype")
public class AdminAuthGroupEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -4233091931416476732L;

    /** 運営者権限グループSEQ */
    @Column(name = "adminAuthGroupSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "adminAuthGroupSeq")
    private Integer adminAuthGroupSeq;

    /** ショップSEQ */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** グループ表示名 */
    @Column(name = "authGroupDisplayName")
    private String authGroupDisplayName;

    /** 登録日時 */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    // テーブル項目外追加フィールド

    /** 運営者権限グループに紐付けられた権限グループ詳細情報 */
    @Column(insertable = false, updatable = false)
    @Transient
    private List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList;

    /** 権限グループが編集不能なグループかどうか */
    @Column(insertable = false, updatable = false)
    private Boolean unmodifiableGroup;

}