/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop;

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
 * フリーエリア表示対象会員クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "FreeAreaViewableMember")
@Data
@Component
@Scope("prototype")
public class FreeAreaViewableMemberEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** フリーエリアSEQ（必須） */
    @Column(name = "freeAreaSeq")
    @Id
    private Integer freeAreaSeq;

    /** 会員SEQ（必須） */
    @Column(name = "memberInfoSeq")
    @Id
    private Integer memberInfoSeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
