/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVMailAddressExtended;
import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <span class="logicName">【メールアドレス（拡張版）】</span>メールアドレス用拡張バリデータクラス。<br />
 * <br />
 * 文字列が、「"メール表記名"<メールアドレス>」の形式でない場合エラー（"メール表記名"は未入力可）<br />
 * 複数指定されている場合エラー<br />
 * <br />
 *
 * <pre>
 * メールアドレスの詳細はチェック種別「メールアドレス」を参照。
 * 複数指定が不可なメールアドレス入力項目に対して付与する。
 *  ⇒複数指定されていた場合はエラーとなる。
 * ＜使用箇所：抜粋＞
 *  MailFrom
 * </pre>
 *
 * @author kimura
 *
 */
@Data
public class HMailAddressExtendedValidator implements ConstraintValidator<HVMailAddressExtended, Object> {

    /** メールアドレスに半角以外が入力されていた場合 */
    public static final String NOT_HANKAKU_MESSAGE_ID = "{HMailAddressExtendedValidator.NOT_HANKAKU_detail}";

    /** メールアドレスじゃなかった場合 */
    public static final String INVALID_MESSAGE_ID = "{HMailAddressExtendedValidator.INVALID_detail}";

    /** 通常メールアドレス正規表現 */
    protected static final String MATERIAL_REGEX =
                    "^[a-zA-Z0-9!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\|\\}~\\.]+@(([-a-z0-9]+\\.)*[a-z]+|\\[\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\])".replaceAll(
                                    "(^\\^|\\$$)", "");

    /** 半角文字正規表現 */
    protected static final String MATERIAL_HANKAKU = "^[ -~｡-ﾟ\\s]+$".replaceAll("(^\\^|\\$$)", "");

    /** メールアドレスの正規表現 */
    public static final String MAIL_ADDRESS_EXTEND_REGEX =
                    "^(([^,\"]*|\"[^\"]*\") *\\< *" + MATERIAL_REGEX + " *\\>| *" + MATERIAL_REGEX + " *)$";

    /** メールアドレス半角文字正規表現 */
    public static final String MAIL_ADDRESS_HANKAKU =
                    "^(([^,\"]*|\"[^\"]*\") *\\< *" + MATERIAL_HANKAKU + " *\\>| *" + MATERIAL_HANKAKU + " *)$";

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
        if (!Pattern.matches(MAIL_ADDRESS_HANKAKU, strValue)) {
            // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
            if (context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(NOT_HANKAKU_MESSAGE_ID).addConstraintViolation();
            }
            return false;
        }

        // メールアドレスの正規表現にマッチしない場合エラー
        if (!Pattern.matches(MAIL_ADDRESS_EXTEND_REGEX, strValue)) {
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