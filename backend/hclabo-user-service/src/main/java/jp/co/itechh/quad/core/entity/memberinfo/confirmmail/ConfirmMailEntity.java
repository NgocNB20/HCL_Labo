/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.memberinfo.confirmmail;

import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
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
 * 確認メールクラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "ConfirmMail")
@Data
@Component
@Scope("prototype")
public class ConfirmMailEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 確認メールSEQ（必須） */
    @Column(name = "confirmMailSeq")
    @Id
    private Integer confirmMailSeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 会員SEQ */
    @Column(name = "memberInfoSeq")
    private Integer memberInfoSeq = 0;

    /** 受注SEQ */
    @Column(name = "orderSeq")
    private Integer orderSeq = 0;

    /** 確認メールアドレス */
    @Column(name = "confirmMail")
    private String confirmMail;

    /** 確認メール種別（必須） */
    @Column(name = "confirmMailType")
    private HTypeConfirmMailType confirmMailType;

    /** 確認メールパスワード（必須） */
    @Column(name = "confirmMailPassword")
    private String confirmMailPassword;

    /** 有効期限（必須） */
    @Column(name = "effectiveTime")
    private Timestamp effectiveTime;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}