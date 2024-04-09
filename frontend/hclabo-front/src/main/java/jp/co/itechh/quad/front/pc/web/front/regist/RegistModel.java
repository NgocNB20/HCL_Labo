/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.regist;

import jp.co.itechh.quad.front.annotation.converter.HCDate;
import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.front.annotation.validator.HVItems;
import jp.co.itechh.quad.front.annotation.validator.HVRItems;
import jp.co.itechh.quad.front.annotation.validator.HVRSeparateDate;
import jp.co.itechh.quad.front.annotation.validator.HVRZipCode;
import jp.co.itechh.quad.front.annotation.validator.HVWindows31J;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * 本会員登録 Model
 *
 * @author kimura
 */
@Data
@HVRSeparateDate(targetYear = "memberInfoBirthdayYear", targetMonth = "memberInfoBirthdayMonth",
                 targetDate = "memberInfoBirthdayDay")
@HVRZipCode(targetLeft = "memberInfoZipCode1", targetRight = "memberInfoZipCode2")
@HVRItems(target = "memberInfoPrefecture", comparison = "memberInfoPrefectureItems")
public class RegistModel extends AbstractModel {

    /** メッセージコード：正規表現エラー(制限なし) */
    public static final String MSGCD_REGULAR_EXPRESSION_ERR = "PKG34-3552-302-A-";
    /** メッセージコード：正規表現エラー(全角のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_EM_SIZE_ERR = "PKG34-3552-303-A-";
    /** メッセージコード：正規表現エラー(半角英数のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_AN_CHAR_ERR = "PKG34-3552-304-A-";
    /** メッセージコード：正規表現エラー(半角英字のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_A_CHAR_ERR = "PKG34-3552-305-A-";
    /** メッセージコード：正規表現エラー(半角数字のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_N_CHAR_ERR = "PKG34-3552-306-A-";

    /**
     * セッション切れ<br/>
     * ※Controllerからの連番
     */
    protected static final String MSGCD_SESSION_DESTROY = "AMR000404";

    /**
     * 初期値セット<br/>
     */
    public RegistModel() {
        memberInfoSex = HTypeSexUnnecessaryAnswer.UNKNOWN.getValue();
        mailMagazine = true;
        errorUrl = true;
    }

    /**
     * 仮会員ID(URLパラメータ)<br/>
     */
    private String mid;

    /**
     * メールアドレス<br/>
     */
    private String memberInfoMail;

    /**
     * 氏名(姓)<br/>
     */
    @NotEmpty
    @HVWindows31J(checkJISX0208 = true)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 16)
    @HCZenkaku
    private String memberInfoLastName;

    /**
     * 氏名(名)<br/>
     */
    @NotEmpty
    @HVWindows31J(checkJISX0208 = true)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 16)
    @HCZenkaku
    private String memberInfoFirstName;

    /**
     * フリガナ(セイ)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 40)
    @HCZenkakuKana
    private String memberInfoLastKana;

    /**
     * フリガナ(メイ)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 40)
    @HCZenkakuKana
    private String memberInfoFirstKana;

    /**
     * 性別<br/>
     */
    @HVItems(target = HTypeSexUnnecessaryAnswer.class)
    @NotEmpty
    private String memberInfoSex;

    /**
     * 性別選択肢<br/>
     */
    private Map<String, String> memberInfoSexItems;

    /**
     * 生年月日(年)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 4)
    @HCDate(pattern = "yyyy")
    private String memberInfoBirthdayYear;

    /**
     * 生年月日(月)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 2)
    @HCDate(pattern = "MM")
    private String memberInfoBirthdayMonth;

    /**
     * 生年月日(日)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 2)
    @HCDate(pattern = "dd")
    private String memberInfoBirthdayDay;

    /**
     * 電話番号<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}")
    @Length(min = 1, max = 11)
    @HCHankaku
    private String memberInfoTel;

    /**
     * 郵便番号(上3桁)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 3)
    @HCHankaku
    private String memberInfoZipCode1;

    /**
     * 郵便番号(下4桁)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 4)
    @HCHankaku
    private String memberInfoZipCode2;

    /**
     * 都道府県<br/>
     */
    @NotEmpty
    private String memberInfoPrefecture;

    /**
     * 都道府県プルダウン用リスト
     **/
    private Map<String, String> memberInfoPrefectureItems;

    /**
     * 住所(市区郡)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 50)
    @HCZenkaku
    private String memberInfoAddress1;

    /**
     * 住所(町村)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 100)
    @HCZenkaku
    private String memberInfoAddress2;

    /**
     * 住所(番地・ビル名)<br/>
     */
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 0, max = 200)
    @HCZenkaku
    private String memberInfoAddress3;

    /**
     * パスワード<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.PASSWORD_NUMBER_REGEX,
             message = "{HPasswordValidator.INVALID_detail}")
    private String memberInfoPassWord;

    /**
     * メルマガ希望<br/>
     */
    private boolean mailMagazine;

    /**
     * 変更前メールマガジン購読希望<br/>
     */
    private boolean preMailMagazine;

    /**
     * 無効なURLからの遷移かどうか<br/>
     * ※デフォルトtrue。Controllerで正常処理実行時のみfalseが設定
     */
    private boolean errorUrl;

    /**
     * ショップSEQ
     */
    private Integer shopSeq;

    /**
     * 不正なメールからの処理の場合<br/>
     *
     * @return true=不正メール, false=正常終了
     */
    public boolean isErrorMemberInfoMail() {
        if (errorUrl) {
            return true;
        }
        return false;
    }
}
