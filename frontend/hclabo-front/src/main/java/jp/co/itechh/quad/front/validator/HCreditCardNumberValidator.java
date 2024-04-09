/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * <span class="logicName">【クレジットカード番号】</span>クレジットカード番号をチェックするクラスです。<br />
 * <br />
 * 文字列に10～16桁の数値以外が含まれている、またはカード番号として妥当でない場合エラー<br />
 * <br />
 * <pre>
 * 【チェック内容】
 * ① 数値であるか。
 * ② 10～16桁であるか。
 * ③ クレジットカード番号として妥当であるか。
 *
 * 【③の処理内容】
 *    1. 下から２桁ごとの数値を2倍にした数値にする。
 *       ex）1234567890 ⇒ 2264106148180（1,3,5,7,9 を2倍にしている）
 *           123456789  ⇒ 14385127169（2,4,6,8 を2倍にしている）
 *    2. 1で作成した数値を1桁ずつ合計した数値が10で割り切れれば妥当である。
 * </pre>
 *
 * アノテーション利用を想定した実装ではないため、チェック時にはString入力項目が来る想定
 *
 * @author kimura
 */
@Data
public class HCreditCardNumberValidator {

    /** クレジットカード番号じゃなかった */
    public static final String CREDIT_CARD_NUMBER_INVALID_MESSAGE_ID = "{HCreditCardNumberValidator.INVALID_detail}";

    /** クレジットカード番号の桁数が誤り */
    public static final String CREDIT_CARD_NUMBER_LENGTH_MESSAGE_ID = "{HCreditCardNumberValidator.LENGTH_detail}";

    /** クレジットカード番号が数値以外 */
    public static final String CREDIT_CARD_NOT_NUMBER_MESSAGE_ID = "{HCreditCardNumberValidator.NOT_NUMBER_detail}";

    /** メッセージID */
    private String messageId;

    /** クレジットカードの正規表現（数値のみかどうか） */
    protected static final String CREDIT_CARD_NUMBER_REGEX = "[\\d]*$";

    /** デフォルト最小入力文字数 */
    public static final int DEFAULT_MIN_LIMIT = 10;

    /** デフォルト最大入力文字数 */
    public static final int DEFAULT_MAX_LIMIT = 16;

    /** デフォルトクレジットカード妥当性チェック（実行しない場合は false） */
    public static final boolean DEFAULT_CHECK_VALID = false;

    /** 最小入力文字数 */
    protected int minLimit = DEFAULT_MIN_LIMIT;

    /** 最大入力文字数 */
    protected int maxLimit = DEFAULT_MAX_LIMIT;

    /** クレジットカード妥当性チェック */
    protected boolean doCheckCreditCardNumber = DEFAULT_CHECK_VALID;

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    public boolean isValid(String value) {

        // null or 空の場合未実施
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 数値かどうか
        if (!isNumber(value)) {
            // メッセージID上書
            messageId = CREDIT_CARD_NOT_NUMBER_MESSAGE_ID;
            return false;
        }

        // 数値だったけど、桁数が許容範囲か
        if (!isPermittedLength(value)) {
            // メッセージID上書
            messageId = CREDIT_CARD_NUMBER_LENGTH_MESSAGE_ID;
            return false;
        }

        // 数値で桁数も許容範囲だけどクレジットカード番号として妥当か
        if (doCheckCreditCardNumber) {
            if (!isCreditCardNumber(value)) {
                // メッセージID上書
                messageId = CREDIT_CARD_NUMBER_INVALID_MESSAGE_ID;
                return false;
            }
        }
        return true;
    }

    /**
     * 数値かどうか
     *
     * @param value 入力値
     * @return true..OK / false..NG
     */
    protected boolean isNumber(Object value) {
        // 数値かどうか
        if (!Pattern.matches(CREDIT_CARD_NUMBER_REGEX, value.toString())) {
            return false;
        }
        return true;
    }

    /**
     * クレジットカード番号として有り得る桁数かどうか
     *
     * @param value 入力値
     * @return true..OK / false..NG
     */
    protected boolean isPermittedLength(Object value) {
        int length = value.toString().length();
        if (length < minLimit || length > maxLimit) {
            return false;
        }
        return true;
    }

    /**
     * クレジットカード番号として妥当か<br/>
     *
     * @param value 入力値
     * @return true..OK / false..NG
     */
    protected boolean isCreditCardNumber(Object value) {
        // ハイフンを除去
        String cardNumber = value.toString().replaceAll("-", "");

        int cardNumberLength = cardNumber.length();
        int remainder = cardNumberLength % 2 == 0 ? 0 : 1;
        String doubleValue = "";
        int result = 0;

        for (int i = 0; i < cardNumberLength; i++) {
            char digit = cardNumber.charAt(i);

            // 入力値の文字数が偶数の場合は、インデックスが偶数の箇所を2倍にする。
            // 入力値の文字数が奇数の場合は、インデックスが奇数の箇所を2倍にする。
            if (i % 2 == remainder) {
                doubleValue = Integer.toString(charToInt(digit) * 2);

                // 2倍にした結果が2桁だった場合、1桁ずつに分割する。
                if (doubleValue.length() == 2) {
                    result += Integer.parseInt(doubleValue.substring(0, 1));
                    result += Integer.parseInt(doubleValue.substring(1));
                } else {
                    result += charToInt(digit) * 2;
                }
            } else {
                result += charToInt(digit);
            }
        }
        // 10の倍数で無かったらエラー。
        if (result % 10 > 0) {
            return false;
        }
        return true;
    }

    /**
     * char を int に変換します。
     * @param c 文字
     * @return int
     */
    protected int charToInt(char c) {
        return Character.getNumericValue(c);
    }
}