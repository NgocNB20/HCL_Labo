/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVNumber;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.NumberUtility;
import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <span class="logicName">【数値】</span>数値チェックバリデータ。<br />
 * <br />
 * 文字列に数値以外の文字が含まれている場合エラー<br />
 *
 * @author kimura
 *
 */
@Data
public class HNumberValidator implements ConstraintValidator<HVNumber, Object> {

    /** 数値じゃなかった場合 */
    public static final String NOT_NUMBER_MESSAGE_ID = "{HNumberValidator.NOT_NUMBER_detail}";

    /** マイナスの数値だった場合（マイナス不許可だった場合） */
    public static final String MINUS_NUMBER_MESSAGE_ID = "{HNumberValidator.MINUS_NUMBER_detail}";

    /**
     * マイナスを許可するかどうか<br/>
     * true..許可 / false..不許可
     */
    private boolean minus;

    /** デフォルトはマイナスを許可しない */
    public static final boolean DEFAULT_MINUS_SIGN = false;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVNumber annotation) {
        minus = annotation.minus();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // null or 空の場合未実施
        if (Objects.equals(value, null) || Objects.equals(value, "")) {
            return true;
        }

        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

        // 数値かどうか
        if (!numberUtility.isNumber(value.toString())) {
            return false;
        }

        // 負の数値を許可するかどうか（デフォルトでは許可しない）
        if (!isMinus()) {
            // 負の数値かどうか
            if (value.toString().charAt(0) == '-') {
                // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
                if (context != null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(MINUS_NUMBER_MESSAGE_ID).addConstraintViolation();
                }
                return false;
            }
        }
        return true;
    }

    /**
     * @return the minus
     */
    public boolean isMinus() {
        return minus;
    }
}