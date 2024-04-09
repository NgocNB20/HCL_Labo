/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.administrator;

import jp.co.itechh.quad.core.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.core.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.core.constant.type.HTypePasswordSHA256EncryptedFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 管理者クラスエンティティ
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "Administrator")
@Data
@Component
@Scope("prototype")
public class AdministratorEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 管理者SEQ（必須） */
    @Column(name = "administratorSeq")
    @Id
    private Integer administratorSeq;

    /** ショップSEQ (FK)（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 管理者状態（必須） */
    @Column(name = "administratorStatus")
    private HTypeAdministratorStatus administratorStatus = HTypeAdministratorStatus.USE;

    /** 管理者ID（必須） */
    @Column(name = "administratorId")
    private String administratorId;

    /** 管理者パスワード（必須） */
    @Column(name = "administratorPassword")
    private String administratorPassword;

    /** 管理者メールアドレス */
    @Column(name = "mail")
    private String mail;

    /** 管理者氏名(性)（必須） */
    @Column(name = "administratorLastName")
    private String administratorLastName;

    /** 管理者氏名(名） */
    @Column(name = "administratorFirstName")
    private String administratorFirstName;

    /** 管理者フリガナ(性)（必須） */
    @Column(name = "administratorLastKana")
    private String administratorLastKana;

    /** 管理者フリガナ(名) */
    @Column(name = "administratorFirstKana")
    private String administratorFirstKana;

    /** 利用開始日 */
    @Column(name = "useStartDate")
    private Timestamp useStartDate;

    /** 利用終了日 */
    @Column(name = "useEndDate")
    private Timestamp useEndDate;

    /** 管理者権限グループSEQ (FK)（必須） */
    @Column(name = "adminAuthGroupSeq")
    private Integer adminAuthGroupSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** パスワード変更日時*/
    @Column(name = "passwordChangeTime")
    private Timestamp passwordChangeTime;

    /** パスワード期限切れ日 */
    @Column(name = "passwordExpiryDate")
    private Timestamp passwordExpiryDate;

    /** ログイン失敗回数 */
    @Column(name = "loginFailureCount")
    private Integer loginFailureCount;

    /** アカウントロック日時 */
    @Column(name = "accountLockTime")
    private Timestamp accountLockTime;

    /** パスワード変更要求フラグ */
    @Column(name = "passwordNeedChangeFlag")
    private HTypePasswordNeedChangeFlag passwordNeedChangeFlag;

    /** パスワードSHA256暗号化済みフラグ */
    @Column(name = "passwordSHA256EncryptedFlag")
    private HTypePasswordSHA256EncryptedFlag passwordSHA256EncryptedFlag;

    /** 運営者に紐付けられた権限グループ */
    @Column(insertable = false, updatable = false)
    @Transient
    private AdminAuthGroupEntity adminAuthGroup;

}