/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.entity.mailmagazine;

import jp.co.itechh.quad.front.constant.type.HTypeSendStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * メルマガ購読者クラス
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class MailMagazineMemberEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ （必須） */
    private Integer shopSeq;

    /** 会員SEQ (FK)（必須） */
    private Integer memberinfoSeq;

    /** 一意制約用メールアドレス（必須） */
    private String uniqueMail;

    /** メールアドレス（必須） */
    private String mail;

    /** 配信状態（必須） */
    private HTypeSendStatus sendStatus = HTypeSendStatus.SENDING;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}