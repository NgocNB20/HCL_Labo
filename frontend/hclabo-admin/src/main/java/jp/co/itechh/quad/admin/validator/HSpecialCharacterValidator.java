/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <span class="defaultValidator">★デフォルトバリデータ</span><br />
 * <span class="logicName">【特殊文字】</span>画面コンポーネント用特殊文字チェックバリデータ。<br />
 * <br />
 * アノテーション初期値の場合のValidatorの挙動：文字列に制御文字（NULLバイトなど）・タブ・改行・半角記号が存在する場合エラー<br />
 * デフォルトValidatorの挙動：文字列に制御文字（NULLバイトなど）・タブ・改行が存在する場合エラー<br />
 * バリデータを指定すれば、タブ・改行・半角記号のうち任意の文字が入力可能となる<br />
 * <br />
 *
 * <pre>
 * 以下の文字が入力されている場合、入力エラーとする
 * 0x00-0x9Fまでの文字のうち
 * ・制御文字・タブ・改行
 *  0x00-0x1F, 0x7F-0x9F
 * ・半角記号
 *  !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
 *
 * タブ(0x09), LF(0x0A), CR(0x0D), 半角記号については、allowCharactersに指定することで入力を許可することが可能
 * </pre>
 *
 * @author kimura
 */
@Data
public class HSpecialCharacterValidator implements ConstraintValidator<HVSpecialCharacter, Object> {

    /** 入力されていない場合 */
    public static final String MESSAGE_ID = "{HSpecialCharacterValidator.INVALID_detail}";

    /** 制御文字 0x80 - 0x9F \p{Cntrl}には含まれない為独自に定義する */
    public static final String CONTROL_CHARACTER_0X80_0X9F = "\\x80\\x81\\x82\\x83\\x84\\x85\\x86"
                                                             + "\\x87\\x88\\x89\\x8a\\x8b\\x8c\\x8d\\x8e\\x8f\\x90\\x91\\x92\\x93\\x94"
                                                             + "\\x95\\x96\\x97\\x98\\x99\\x9a\\x9b\\x9c\\x9d\\x9e\\x9f";

    /** 特殊文字が含まれない正規表現 */
    // \p{Cntrl}:制御文字・タブ・改行[\x00-\x1F\x7F]
    // \p{Punct}:半角記号[!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~] ※33(x21)~47(x2f),58(x3a)~64(x40),91(x5b)~96(x60),123(x7b)~126(x7e)
    public static final String NO_SPECIAL_CHARACTER_REGEX =
                    "[^\\p{Cntrl}\\p{Punct}" + CONTROL_CHARACTER_0X80_0X9F + "]*";

    /** 制御文字・タブ・改行が含まれない正規表現 */
    // \p{Cntrl}:制御文字・タブ・改行[\x00-\x1F\x7F]
    public static final String NO_CONTROL_CHARACTER_REGEX = "[^\\p{Cntrl}" + CONTROL_CHARACTER_0X80_0X9F + "]*";

    /** 常に拒否する文字列 */
    // 制御文字（タブ・改行を除く）
    protected static final String DENY_CHARACTERS = "\\x00\\x01\\x02\\x03\\x04\\x05\\x06\\x07\\x08"
                                                    + "\\x0b\\x0c\\x0e\\x0f\\x10\\x11\\x12\\x13\\x14\\x15\\x16\\x17\\x18\\x19\\x1a\\x1b\\x1c\\x1d\\x1e\\x1f\\x7f"
                                                    + CONTROL_CHARACTER_0X80_0X9F;

    /** 指定された場合に許可する文字とエスケープ後の値のマップ */
    protected static final Map<Character, String> ALLOWABLE_CHARACTERS_MAP;

    /** @see HVSpecialCharacter#allowPunctuation() */
    protected boolean allowPunctuation;

    /** @see HVSpecialCharacter#allowSymbol() */
    protected boolean allowSymbol;

    /** @see HVSpecialCharacter#allowCharacters() */
    protected char[] allowCharacters;

    static {
        /*
         * デフォルトでは拒否するが設定によって許可可能な文字（タブ・改行・半角記号）
         */
        ALLOWABLE_CHARACTERS_MAP = new LinkedHashMap<>();

        // \t, \r, \nは許可可能
        ALLOWABLE_CHARACTERS_MAP.put((char) 9, "\\x0" + Integer.toHexString(9));
        ALLOWABLE_CHARACTERS_MAP.put((char) 10, "\\x0" + Integer.toHexString(10));
        ALLOWABLE_CHARACTERS_MAP.put((char) 13, "\\x0" + Integer.toHexString(13));

        for (int i = 33; i <= 47; i++) {
            ALLOWABLE_CHARACTERS_MAP.put((char) i, "\\x" + Integer.toHexString(i));
        }

        for (int i = 58; i <= 64; i++) {
            ALLOWABLE_CHARACTERS_MAP.put((char) i, "\\x" + Integer.toHexString(i));
        }

        for (int i = 91; i <= 96; i++) {
            ALLOWABLE_CHARACTERS_MAP.put((char) i, "\\x" + Integer.toHexString(i));
        }

        for (int i = 123; i <= 126; i++) {
            ALLOWABLE_CHARACTERS_MAP.put((char) i, "\\x" + Integer.toHexString(i));
        }
    }

    @Override
    public void initialize(HVSpecialCharacter annotation) {
        allowPunctuation = annotation.allowPunctuation();
        allowSymbol = annotation.allowSymbol();
        allowCharacters = annotation.allowCharacters();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // null or 空の場合未実施
        if (Objects.equals(value, null) || Objects.equals(value, "")) {
            return true;
        }

        String strValue = value.toString();
        return Pattern.matches(createPattern(), strValue);
    }

    /**
     * チェック用正規表現を作成
     *
     * @return 正規表現
     */
    protected String createPattern() {

        // 判定優先度①：制御文字以外すべてを許可するフラグ
        if (allowPunctuation) {
            // 制御文字を除く、タブ・改行・半角記号をすべて許可
            return "[^" + DENY_CHARACTERS + "]*";
        }

        // 判定優先度②：半角記号を許可するフラグ
        // ★デフォルトバリデータでは、こちらのフラグが適用
        if (allowSymbol) {
            // 制御文字・タブ・改行を除く、半角記号を許可
            return NO_CONTROL_CHARACTER_REGEX;
        }

        // 判定優先度③：許可する文字を個別指定
        if (allowCharacters != null && allowCharacters.length != 0) {

            // 【ここの仕様】制御文字・タブ・改行・半角記号を一旦すべて禁止し、allowCharactersだけ許可
            List<Character> denyCharacterList = new ArrayList<>(ALLOWABLE_CHARACTERS_MAP.keySet());
            for (Character allow : allowCharacters) {
                denyCharacterList.remove(allow);
            }

            // パターンを作成
            StringBuilder customizePattern = new StringBuilder();
            customizePattern.append("[^").append(DENY_CHARACTERS);

            for (Character deny : denyCharacterList) {
                customizePattern.append(ALLOWABLE_CHARACTERS_MAP.get(deny));
            }

            customizePattern.append("]*");

            return customizePattern.toString();
        }

        // 制御文字・タブ・改行・半角記号をすべて禁止
        return NO_SPECIAL_CHARACTER_REGEX;
    }

}
