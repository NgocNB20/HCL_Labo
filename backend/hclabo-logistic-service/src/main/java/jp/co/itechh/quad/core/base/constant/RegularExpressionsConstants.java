/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.base.constant;

/**
 * 正規表現一覧<br/>
 * Bean Validationの@Patternで使用
 *
 * @author kimura
 */
public class RegularExpressionsConstants {

    /** 半角数値チェックの正規表現 */
    public static final String HANKAKU_NUMBER_REGEX = "[0-9]+";

    /** 半角チェックの正規表現 */
    public static final String HANKAKU_REGEX = "^[ -~｡-ﾟ\\s]+$";

    /** 全角チェックの正規表現 */
    public static final String ZENKAKU_REGEX = "^[^\\x00-\\x1F\\x7F-\\x9F -~｡-ﾟ\\s]+$";

    /** 全角カナチェックの正規表現 */
    public static final String ZENKAKU_KANA_REGEX = "^[ァ-ヶー－]+$";

    /** パスワードの正規表現（半角英数記号7～20桁） */
    // Alphanumeric password required.Special characters not mandatory.
    public static final String PASSWORD_NUMBER_REGEX =
                    "(^(?=.*[a-zA-Z])(?=.*\\d)|^(?=.*[a-zA-Z])(?=.*[!\"#\\$%&'\\(\\)\\*\\+,\\-\\./\\:\\;<=>\\?@\\[\\\\\\]\\^_`\\{\\|\\}~])|^(?=.*\\d)(?=.*[!\"#\\$%&'\\(\\)\\*\\+,\\-\\./\\:\\;<=>\\?@\\[\\\\\\]\\^_`\\{\\|\\}~]))[!-~]{7,20}";

    /** 電話番号の正規表現（数値とハイフンの組み合わせかどうか） */
    public static final String TELEPHONE_NUMBER_REGEX = "[\\d]*$";

    /** 郵便番号の正規表現（数値３桁＋数値４桁） */
    public static final String ZIP_CODE_REGEX = "\\d{3}\\d{4}";

    /** 特殊文字チェックの正規表現　ここから */

    /** 制御文字 0x80 - 0x9F \p{Cntrl}には含まれない為独自に定義する */
    public static final String CONTROL_CHARCTER_0X80_0X9F = "\\x80\\x81\\x82\\x83\\x84\\x85\\x86"
                                                            + "\\x87\\x88\\x89\\x8a\\x8b\\x8c\\x8d\\x8e\\x8f\\x90\\x91\\x92\\x93\\x94"
                                                            + "\\x95\\x96\\x97\\x98\\x99\\x9a\\x9b\\x9c\\x9d\\x9e\\x9f";

    /** 特殊文字が含まれない正規表現 */
    public static final String NO_SPECIAL_CHARACTER_REGEX =
                    "[^\\p{Cntrl}\\p{Punct}" + CONTROL_CHARCTER_0X80_0X9F + "]*";

    /** 常に拒否する文字列 */
    public static final String DENY_CHARACTERS = "\\x00\\x01\\x02\\x03\\x04\\x05\\x06\\x07\\x08"
                                                 + "\\x0b\\x0c\\x0e\\x0f\\x10\\x11\\x12\\x13\\x14\\x15\\x16\\x17\\x18\\x19\\x1a\\x1b\\x1c\\x1d\\x1e\\x1f\\x7f"
                                                 + CONTROL_CHARCTER_0X80_0X9F;

    /** 「句読文字とtab, 改行」を許可する場合に利用 ※左記パターンのチェック用正規表現の定義をフェーズ１流用しつつ、新規作成 */
    public static final String ALLOW_PUNCTUATION_REGEX = "[^" + DENY_CHARACTERS + "]*";
    /** 特殊文字チェックの正規表現　ここまで */
}