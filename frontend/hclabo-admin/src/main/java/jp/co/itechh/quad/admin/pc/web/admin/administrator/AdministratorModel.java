/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator;

import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

/**
 * 運営者検索ページ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AdministratorModel extends AbstractModel {

    /**
     * 運営者情報取得失敗
     */
    public static final String MSGCD_ADMINISTRATOR_NO_DATA = "AYO000201";

    /**
     * ソート項目名
     */
    private String orderField;
    /**
     * ソート昇順フラグ
     */
    private boolean orderAsc;

    /**
     * 結果一覧
     */
    private List<AdministratorModelItem> resultItems;

    /**
     * 会員ID
     */
    private String administratorId;

    /**
     * 氏名
     */
    private String administratorName;

    /**
     * フリガナ
     */
    private String administratorKana;

    /**
     * メールアドレス
     */
    private String administratorMail;

    /**
     * 管理者状態
     */
    private HTypeAdministratorStatus administratorStatus;

    /**
     * 利用開始日
     */
    private Timestamp useStartDate;

    /**
     * 利用終了日
     */
    private Timestamp useEndDate;

    /**
     * 管理者グループ名
     */
    private String administratorGroupName;

    /**
     * Password change time
     */
    private Timestamp passwordChangeTime;

    /**
     * Password expiry date
     */
    private String passwordExpiryDate;

    /**
     * アカウントロック状態
     * true..ロックされている
     */
    private boolean loginFailureAccountLock;

    /**
     * アカウントロック状態
     * true..ロックされている
     */
    private boolean pwdExpiredAccountLock;

    /**
     * アカウントロック日時
     */
    public Timestamp accountLockTime;

    /**
     * Method to decide whether the account is locked or not
     *
     * @return true when account is locked, false when account is not locked
     */
    public boolean isAccountLock() {
        return loginFailureAccountLock || pwdExpiredAccountLock;
    }

    /**
     * ログイン失敗回数
     */

    private Integer loginFailureCount;

    /**
     * パスワード変更要求フラグ
     */
    private HTypePasswordNeedChangeFlag passwordNeedChangeFlag;

    /**
     * 運営者情報エンティティ
     */
    private AdministratorEntity administratorEntity;

    /**
     * 運営者SEQ
     */
    private Integer administratorSeq;

    /**
     * パスワード
     */
    private String administratorPassword;

    /**
     * 氏名（姓）
     */
    private String administratorLastName;

    /**
     * 氏名（名）
     */
    private String administratorFirstName;

    /**
     * フリガナ(姓)
     */
    private String administratorLastKana;

    /**
     * フリガナ(名)
     */
    private String administratorFirstKana;

    /**
     * 管理者グループSEQ
     */
    private String administratorGroupSeq;
}
