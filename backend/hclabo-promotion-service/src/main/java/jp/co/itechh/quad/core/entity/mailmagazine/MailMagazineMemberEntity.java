/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.mailmagazine;

import jp.co.itechh.quad.core.constant.type.HTypeSendStatus;
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
 * メルマガ購読者クラス
 *
 * @author kimura
 */
@Entity
@Table(name = "MailMagazineMember")
@Data
@Component
@Scope("prototype")
public class MailMagazineMemberEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ （必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 会員SEQ (FK)（必須） */
    @Column(name = "memberinfoSeq")
    @Id
    private Integer memberinfoSeq;

    /** 一意制約用メールアドレス（必須） */
    @Column(name = "uniqueMail")
    private String uniqueMail;

    /** メールアドレス（必須） */
    @Column(name = "mail")
    private String mail;

    /** 配信状態（必須） */
    @Column(name = "sendStatus")
    private HTypeSendStatus sendStatus = HTypeSendStatus.SENDING;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}