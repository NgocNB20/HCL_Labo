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
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 運営者権限グループ詳細エンティティ
 * @author EntityGenerator
 */
@Entity
@Table(name = "AdminAuthGroupDetail")
@Data
@Component
@Scope("prototype")
public class AdminAuthGroupDetailEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** 運営者権限グループSEQ */
    @Column(name = "adminAuthGroupSeq")
    @Id
    private Integer adminAuthGroupSeq;

    /** 権限種別 */
    @Column(name = "authTypeCode")
    private String authTypeCode;

    /** 権限レベル */
    @Column(name = "authLevel")
    private Integer authLevel;

    /** 登録日時 */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}