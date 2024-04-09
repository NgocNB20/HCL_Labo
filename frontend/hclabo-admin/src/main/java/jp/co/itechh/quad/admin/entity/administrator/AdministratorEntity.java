/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.administrator;

import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordSHA256EncryptedFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 管理者クラスエンティティ
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class AdministratorEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 管理者SEQ（必須） */
    private Integer administratorSeq;

    /** ショップSEQ (FK)（必須） */
    private Integer shopSeq;

    /** 管理者状態（必須） */
    private HTypeAdministratorStatus administratorStatus = HTypeAdministratorStatus.USE;

    /** 管理者ID（必須） */
    private String administratorId;

    /** 管理者パスワード（必須） */
    private String administratorPassword;

    /** 管理者メールアドレス */
    private String mail;

    /** 管理者氏名(性)（必須） */
    private String administratorLastName;

    /** 管理者氏名(名） */
    private String administratorFirstName;

    /** 管理者フリガナ(性)（必須） */
    private String administratorLastKana;

    /** 管理者フリガナ(名) */
    private String administratorFirstKana;

    /** 利用開始日 */
    private Timestamp useStartDate;

    /** 利用終了日 */
    private Timestamp useEndDate;

    /** 管理者権限グループSEQ (FK)（必須） */
    private Integer adminAuthGroupSeq;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** パスワード変更日時*/
    private Timestamp passwordChangeTime;

    /** パスワード期限切れ日 */
    private Timestamp passwordExpiryDate;

    /** ログイン失敗回数 */
    private Integer loginFailureCount;

    /** アカウントロック日時 */
    private Timestamp accountLockTime;

    /** パスワード変更要求フラグ */
    private HTypePasswordNeedChangeFlag passwordNeedChangeFlag;

    /** パスワードSHA256暗号化済みフラグ */
    private HTypePasswordSHA256EncryptedFlag passwordSHA256EncryptedFlag;

    /** 運営者に紐付けられた権限グループ */
    private AdminAuthGroupEntity adminAuthGroup;

}