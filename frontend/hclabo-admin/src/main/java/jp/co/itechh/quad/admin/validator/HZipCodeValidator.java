/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRZipCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <span class="logicName">【郵便番号】</span>入力フィールドが分かれている郵便番号用のバリデータクラス。<br />
 * <br />
 * 文字列に数値7桁以外の文字が含まれている場合エラー<br />
 *
 * @author kimura
 *
 */
@Data
public class HZipCodeValidator implements ConstraintValidator<HVRZipCode, Object> {

    /** 郵便番号でない場合 */
    public static final String INVALID_MESSAGE_ID = "{HZipCodeValidator.INVALID_detail}";

    /** 郵便番号の正規表現（数値３桁＋数値４桁） */
    public static final String ZIP_CODE_REGEX = "\\d{3}\\d{4}";

    /** 相関チェック対象 */
    private String targetLeft;

    /** 相関チェック対象 */
    private String targetRight;

    /** メッセージ */
    private String message;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRZipCode annotation) {
        targetLeft = annotation.targetLeft();
        targetRight = annotation.targetRight();
        message = annotation.message();
    }

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

        /** 対象項目 */
        String targetLeftValue = getFieldValue(value, targetLeft);

        /** 比較対象項目 */
        String targetRightValue = getFieldValue(value, targetRight);

        boolean targetLeftInputFlag = StringUtils.isEmpty(targetLeftValue);
        boolean targetRightInputFlag = StringUtils.isEmpty(targetRightValue);

        // 両方入力されているか
        if (targetLeftInputFlag && targetRightInputFlag) {
            return true;
        }

        // いずれかが入力されていない
        if (targetLeftInputFlag || targetRightInputFlag) {

            if (targetLeftInputFlag) {
                makeContext(context, message, targetLeft);
            }

            if (targetRightInputFlag) {
                makeContext(context, message, targetRight);
            }

            return false;
        }

        String zipCode = targetLeftValue + "" + targetRightValue;
        if (!Pattern.matches(ZIP_CODE_REGEX, zipCode)) {
            makeContext(context, message, targetLeft);
            return false;
        }
        return true;
    }

    /**
     * フィールドから値を取得
     *
     * @param value
     * @param field
     *
     * @return フィールドの値
     */
    protected String getFieldValue(Object value, String field) {

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        TypeDescriptor targetValue = beanWrapper.getPropertyTypeDescriptor(field);
        if (targetValue != null) {
            String type = targetValue.getName();

            if ("java.lang.String".equals(type)) {

                Object result = beanWrapper.getPropertyValue(field);

                // nullの場合未変換
                if (result instanceof String) {
                    return result.toString();
                }
            }
        }
        return null;
    }

    /**
     * エラーメッセージを構成<br/>
     * メッセージセット＋エラーメッセージを表示する項目を指定
     */
    protected void makeContext(ConstraintValidatorContext context, String messageId, String errorFiled) {
        // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
        if (context == null) {
            return;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addPropertyNode(errorFiled).addConstraintViolation();
    }
}