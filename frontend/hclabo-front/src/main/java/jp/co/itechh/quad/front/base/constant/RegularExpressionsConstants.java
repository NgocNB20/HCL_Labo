/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.base.constant;

/**
 * 正規表現一覧<br/>
 * Bean Validationの@Patternで使用
 *
 * @author kimura
 *
 */
public class RegularExpressionsConstants {

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
}