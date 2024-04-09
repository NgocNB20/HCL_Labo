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
 * <span class="logicName">【セキュリティコード】</span>セキュリティコードをチェックするクラスです。<br />
 * <br />
 * 文字列に数値3～4桁以外が含まれている場合エラー<br />
 * <br />
 * <pre>
 * 【チェック内容】
 * ① 数値であるか。
 * ② 3～4桁であるか。
 * </pre>
 * アノテーション利用を想定した実装ではないため、チェック時にはString入力項目が来る想定
 *
 * @author kimura
 */
@Data
public class HSecurityCodeValidator {

    /** セキュリティコードの桁数が誤り */
    public static final String SECURITY_CODENUMBER_LENGTH_MESSAGE_ID = "{HSecurityCodeValidator.LENGTH_detail}";

    /** セキュリティコードが数値以外 */
    public static final String SECURITY_CODENUMBER_NOT_NUMBER_MESSAGE_ID = "{HSecurityCodeValidator.NOT_NUMBER_detail}";

    /** メッセージID */
    private String messageId;

    /** セキュリティコードの正規表現（数値のみかどうか） */
    protected static final String SECURITY_CODENUMBER_REGEX = "[\\d]*$";

    /** デフォルト最小入力文字数 */
    public static final int DEFAULT_MIN_LIMIT = 3;

    /** デフォルト最大入力文字数 */
    public static final int DEFAULT_MAX_LIMIT = 4;

    /** 最小入力文字数 */
    protected int minLimit = DEFAULT_MIN_LIMIT;

    /** 最大入力文字数 */
    protected int maxLimit = DEFAULT_MAX_LIMIT;

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
            messageId = SECURITY_CODENUMBER_NOT_NUMBER_MESSAGE_ID;
            return false;
        }

        // 数値だったけど、桁数が許容範囲か
        if (!isPermittedLength(value)) {
            // メッセージID上書
            messageId = SECURITY_CODENUMBER_LENGTH_MESSAGE_ID;
            return false;
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
        if (!Pattern.matches(SECURITY_CODENUMBER_REGEX, value.toString())) {
            return false;
        }
        return true;
    }

    /**
     * セキュリティコードとして有り得る桁数かどうか
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
}