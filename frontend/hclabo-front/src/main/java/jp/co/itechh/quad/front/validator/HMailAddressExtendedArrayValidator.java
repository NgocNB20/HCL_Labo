/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVMailAddressExtendedArray;
import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <span class="logicName">【メールアドレス（拡張版
 * 複数用）】</span>メールアドレス用拡張バリデータクラス（複数アドレス用）。<br />
 * <br />
 * 文字列が、「"メール表記名"<メールアドレス>,"メール表記名"<メールアドレス>・・・」の形式でない場合エラー（"メール表記名"は未入力可）<br />
 * <br />
 *
 * <pre>
 * メールアドレスの詳細はチェック種別「メールアドレス」を参照。
 * 複数指定が可能なメールアドレス入力項目に対して付与する。
 * ＜使用箇所：抜粋＞
 *  MailTo/CC
 * </pre>
 *
 * @author kimura
 *
 */
@Data
public class HMailAddressExtendedArrayValidator implements ConstraintValidator<HVMailAddressExtendedArray, Object> {

    /** メールアドレスじゃなかった場合 */
    public static final String INVALID_MESSAGE_ID = "{HMailAddressExtendedArrayValidator.INVALID_detail}";

    /** メールアドレスに半角以外が入力されていた場合 */
    public static final String NOT_HANKAKU_MESSAGE_ID = "{HMailAddressExtendedArrayValidator.NOT_HANKAKU_detail}";

    /** 単体拡張メールアドレス */
    protected static final String MATERIAL_REGEX =
                    HMailAddressExtendedValidator.MAIL_ADDRESS_EXTEND_REGEX.replaceAll("(^\\^|\\$$)", "");

    /** メールアドレスの正規表現 */
    public static final String MAIL_ADDRESS_EXTEND_ARRAY_REGEX =
                    "^" + MATERIAL_REGEX + " *(, *" + MATERIAL_REGEX + " *)*$";

    /** 単体拡張メールアドレス（半角チェック用） */
    protected static final String MATERIAL_HANKAKU =
                    HMailAddressExtendedValidator.MAIL_ADDRESS_HANKAKU.replaceAll("(^\\^|\\$$)", "");

    /** メールアドレスの正規表現（半角チェック用） */
    public static final String MAIL_ADDRESS_EXTEND_ARRAY_HANKAKU =
                    "^" + MATERIAL_HANKAKU + " *(, *" + MATERIAL_HANKAKU + " *)*$";

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // null or 空の場合未実施
        if (Objects.equals(value, null) || Objects.equals(value, "")) {
            return true;
        }

        String strValue = value.toString();

        // メールアドレスに半角文字以外が入力されている場合エラー
        if (!Pattern.matches(MAIL_ADDRESS_EXTEND_ARRAY_HANKAKU, strValue)) {
            // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
            if (context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(NOT_HANKAKU_MESSAGE_ID).addConstraintViolation();
            }
            return false;
        }

        // メールアドレスの正規表現にマッチしない場合エラー
        if (!Pattern.matches(MAIL_ADDRESS_EXTEND_ARRAY_REGEX, strValue)) {
            // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
            if (context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(INVALID_MESSAGE_ID).addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}