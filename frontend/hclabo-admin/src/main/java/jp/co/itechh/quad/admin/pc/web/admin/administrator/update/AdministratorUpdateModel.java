/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.update;

import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 運営者変更ページ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AdministratorUpdateModel extends AbstractModel {

    /**
     * 選択した運営者状態が不正
     */
    public static final String MSGCD_ADMINISTRATOR_STATUS_ERROR = "AYO000301";

    public static final String MSGCD_ADMINISTRATOR_GROUP_SEQ_ERROR = "AYO000600";

    /**
     * 運営者情報が取得できない
     */
    public static final String MSGCD_ADMINISTRATOR_NO_DATA = "AYO000302";

    /**
     * コンストラクタ
     * 初期値の設定
     *
     */
    public AdministratorUpdateModel() {
        super();
    }

    /**
     * 確認画面からの遷移フラグ
     */
    private boolean editFlag;

    /**
     * 運営者SEQ
     */
    private Integer administratorSeq;

    /**
     * 修正対象となる運営者詳細DTO
     */
    private AdministratorEntity modifiedEntity;

    /**
     * 運営者情報エンティティ
     * 修正箇所の比較時に使用
     */
    private AdministratorEntity originalEntity;

    /**
     * 会員ID
     */
    @NotEmpty
    @Length(min = 1, max = 20)
    @HCHankaku
    private String administratorId;

    /**
     * パスワード
     */
    @Pattern(regexp = RegularExpressionsConstants.PASSWORD_NUMBER_REGEX,
             message = "{HPasswordValidator.INVALID_detail}")
    @Length(min = 5, max = 20)
    private String administratorPassword;

    /**
     * 氏名（姓）
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 15)
    @HCZenkaku
    private String administratorLastName;

    /**
     * 氏名（名）
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 15)
    @HCZenkaku
    private String administratorFirstName;

    /**
     * フリガナ(姓)
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 15)
    @HCZenkakuKana
    private String administratorLastKana;

    /**
     * フリガナ(名)
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 15)
    @HCZenkakuKana
    private String administratorFirstKana;

    /**
     * メールアドレス
     */
    @NotEmpty
    @Email
    @Pattern(regexp = RegularExpressionsConstants.HANKAKU_REGEX, message = "{HMailAddressValidator.NOT_HANKAKU_detail}")
    @Length(min = 1, max = 160)
    @HCHankaku
    private String administratorMail;

    /**
     * 管理者状態
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}")
    private String administratorStatus;

    /**
     * 管理者状態リスト
     */
    private Map<String, String> administratorStatusItems;

    /**
     * 管理者グループSEQ
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}")
    private String administratorGroupSeq;

    /**
     * 管理者グループSEQ
     */
    private Integer administratorGroupSeqConfirm;

    /**
     * 管理者グループ名
     */
    private String administratorGroupName;

    /**
     * 管理者グループリスト
     */
    private Map<String, String> administratorGroupSeqItems;

    /**
     * 利用開始日
     */
    private Timestamp useStartDate;

    /**
     * 利用終了日
     */
    private Timestamp useEndDate;

    /**
     * 管理者状態(変更前）
     */
    private String oldAdministratorStatus;

    /**
     * 利用開始日(変更前）
     */
    private Timestamp oldUseStartDate;

    /**
     * 利用終了日(変更前）
     */
    private Timestamp oldUseEndDate;

    /** Password change time */
    private Timestamp passwordChangeTime;

    /** Password expiry date */
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

    /** アカウントロック日時 */
    private Timestamp accountLockTime;

    /**
     * Method to decide whether the account is locked or not
     *
     * @return true when account is locked, false when account is not locked
     */
    public boolean isAccountLock() {
        return loginFailureAccountLock || pwdExpiredAccountLock;
    }

    /** ログイン失敗回数 */
    private Integer loginFailureCount;

    /**
     * パスワード変更要求フラグ
     */
    private HTypePasswordNeedChangeFlag passwordNeedChangeFlag;

    /**
     * パスワード変更要求
     */
    private boolean passwordNeedChange;

    /** 変更箇所リスト */
    private List<String> modifiedList;

    /**
     * 氏名
     */
    private String administratorName;

    /**
     * フリガナ
     */
    private String administratorKana;

    /**
     * 変更後のアカウントロック状態
     * true..ロックされている
     */
    public boolean modifyLoginFailureAccountLock;

    /**
     * 変更後のアカウントロック状態
     * true..ロックされている
     */
    public boolean modifyPwdExpiredAccountLock;

    /**
     * アカウントロック状態フラグの差分チェック
     *
     * @return true:差分がある false:差分がない
     */
    protected boolean isAccountLockModified() {
        // 二つのフラグのいずれかに差分があればtrue、そうでなければfalseを返す
        if (loginFailureAccountLock == modifyLoginFailureAccountLock
            && pwdExpiredAccountLock == modifyPwdExpiredAccountLock) {
            return false;
        }
        return true;
    }

    /** 差分スタイルクラス */
    private String DIFF_STYLE_CLASS = "diff";

    /** デフォルトスタイルクラス */
    private String DEFAULT_STYLE_CLASS = "";

    /**
     * ダイナミックプロパティ
     * アカウントロック状態が変更されてたら修正用のスタイルシート名を返します。
     * 状態の項目はEntityには無いのでアカウントロックおよび期限切れの状態変更があったかで判定する
     *
     * @return スタイルシート名
     */
    public String getDiffAccountLockStyleClass() {
        if (isAccountLockModified()) {
            return DIFF_STYLE_CLASS;
        }
        return DEFAULT_STYLE_CLASS;
    }
}