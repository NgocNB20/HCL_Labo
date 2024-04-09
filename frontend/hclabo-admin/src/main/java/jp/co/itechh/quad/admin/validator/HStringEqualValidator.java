/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRStringEqual;
import lombok.Data;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <span class="logicName">【文字列一致】</span>文字列の相関バリデータ。<br />
 * <br />
 * 対象項目 と 比較項目の文字列が異なる場合エラー<br />
 *
 * @author kimura
 */
@Data
public class HStringEqualValidator implements ConstraintValidator<HVRStringEqual, Object> {

    /** 異なる場合 */
    public static final String NOT_EQUAL_MESSAGE_ID = "{HStringEqualValidator.NOT_EQUAL_detail}";

    /** 相関チェック対象 */
    private String target;

    /** 相関チェック比較対象 */
    private String comparison;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRStringEqual annotation) {
        messageId = annotation.message();
        target = annotation.target();
        comparison = annotation.comparison();
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
        String targetValue = getFieldValue(value, target);

        /** 比較対象項目 */
        String comparisonValue = getFieldValue(value, comparison);

        if (!isEquals(targetValue, comparisonValue)) {
            // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
            if (context != null) {
                // メッセージセット＋エラーメッセージを表示する項目を指定
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(messageId)
                       .addPropertyNode(target)
                       .addConstraintViolation();
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

    /**
     * 文字列が同じかどうかチェック
     *
     * @return true..同じ / false..異なる
     */
    protected boolean isEquals(String targetValue, String comparisonValue) {

        // 対象項目のみnullの場合
        if (targetValue == null && comparisonValue != null) {
            return false;
        }
        // 両項目がnullの場合
        if (targetValue == null && comparisonValue == null) {
            return true;
        }
        return targetValue.equals(comparisonValue);
    }
}