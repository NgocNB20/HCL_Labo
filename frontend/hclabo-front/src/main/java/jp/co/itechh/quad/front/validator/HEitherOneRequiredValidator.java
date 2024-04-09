/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVREitherOneRequired;
import jp.co.itechh.quad.front.base.constant.ValidatorConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <span class="logicName">【必須】</span>いずれかの項目が入力されているかチェックするバリデータ。<br />
 * <br />
 * 対象の項目が全て未入力の場合エラー<br />
 *
 * @author kimura
 */
@Data
public class HEitherOneRequiredValidator implements ConstraintValidator<HVREitherOneRequired, Object> {

    /** 対象の項目が全て未入力の場合 */
    public static final String REQUIRED_MESSAGE_ID = "{HEitherOneRequiredValidator.INPUT}";

    /** 相関チェック対象 */
    private String[] fields;

    /** エラー表示項目 */
    private String errorField;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVREitherOneRequired annotation) {
        fields = annotation.fields();
        errorField = annotation.errorField();
        messageId = annotation.message();
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

        boolean errorFlag = true;

        for (String target : fields) {
            String targetValue = getFieldValue(value, target);

            if (StringUtils.isNotEmpty(targetValue)) {
                errorFlag = false;
            }
        }

        if (errorFlag) {
            // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
            if (context != null) {
                // メッセージセット＋エラーメッセージを表示する項目を指定
                context.disableDefaultConstraintViolation();
                if (ValidatorConstants.NOTARGET.equals(errorField)) {
                    context.buildConstraintViolationWithTemplate(messageId)
                           .addPropertyNode(fields[0])
                           .addConstraintViolation();
                } else {
                    context.buildConstraintViolationWithTemplate(messageId)
                           .addPropertyNode(errorField)
                           .addConstraintViolation();
                }
            }
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
}