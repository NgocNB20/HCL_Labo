/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRequiredDiscountType;
import jp.co.itechh.quad.admin.constant.type.HTypeDiscountType;
import lombok.Data;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Data
public class HVRequiredDiscountTypeValidator implements ConstraintValidator<HVRequiredDiscountType, Object> {

    /** 空でないメッセージ */
    private static final String NOT_NULL_MESSAGE_ID = "{javax.validation.constraints.NotEmpty.message}";

    /** DISCOUNT_PRICE_CANNOT_ENTER */
    private static final String DISCOUNT_PRICE_CANNOT_ENTER =
                    "{HVRequiredDiscountTypeValidator.NOT_ENTER_discount_price}";

    /** DISCOUNT_RATE_CANNOT_ENTER */
    private static final String DISCOUNT_RATE_CANNOT_ENTER =
                    "{HVRequiredDiscountTypeValidator.NOT_ENTER_discount_rate}";

    /** 文字列型 */
    private static final String STRING_TYPE = "java.lang.String";

    /** 割引率 */
    private String discountRate;

    /** 割引金額 */
    private String discountPrice;

    /** 割引種別 */
    private String discountType;

    /**
     * アノテーションの情報設定
     */
    @Override
    public void initialize(HVRequiredDiscountType annotation) {
        this.discountRate = annotation.discountRate();
        this.discountPrice = annotation.discountPrice();
        this.discountType = annotation.discountType();
    }

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String dcntRate = getFieldValue(value, this.discountRate);
        String dcntPrice = getFieldValue(value, this.discountPrice);
        String dcntType = getFieldValue(value, this.discountType);

        if (dcntType.equals(HTypeDiscountType.PERCENT.getValue())) {
            if (dcntRate == null) {
                makeContext(context, NOT_NULL_MESSAGE_ID, discountRate);
                return false;
            } else if (dcntPrice != null) {
                makeContext(context, DISCOUNT_PRICE_CANNOT_ENTER, discountPrice);
                return false;
            }
        } else if (dcntType.equals(HTypeDiscountType.AMOUNT.getValue())) {
            if (dcntPrice == null) {
                makeContext(context, NOT_NULL_MESSAGE_ID, discountPrice);
                return false;
            } else if (dcntRate != null) {
                makeContext(context, DISCOUNT_RATE_CANNOT_ENTER, discountRate);
                return false;
            }
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

            if (STRING_TYPE.equals(type)) {

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