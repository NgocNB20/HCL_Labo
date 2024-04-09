/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRItems;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Objects;

/**
 * HTypeに値が含まれるかの入力チェックバリデータ<br />
 *
 * @author kimura
 */
@Data
public class HItemsRValidator implements ConstraintValidator<HVRItems, Object> {

    /** 含まれていない場合 */
    public static final String INVALID_MESSAGE_ID = "{HItemsValidator.INVALID_detail}";

    /** 相関チェック対象 */
    private String target;

    /** 相関チェック比較対象 */
    private String comparison;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRItems annotation) {
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
        String[] targetValue = getFieldValue(value, target);

        /** 比較対象項目 */
        Map<String, String> comparisonValue = getFieldValueMapList(value, comparison);

        if (StringUtils.isAllEmpty(targetValue)) {
            // 必須チェックは除外
            return true;
        }

        if (Objects.isNull(comparisonValue)) {
            // session切れなどが原因でcomparisonValueがnullとなった場合もここではチェックしない
            return true;
        }

        for (String curTargetValue : targetValue) {
            if (!comparisonValue.containsKey(curTargetValue)) {
                // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
                if (context != null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(messageId)
                           .addPropertyNode(target)
                           .addConstraintViolation();
                }
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
    protected String[] getFieldValue(Object value, String field) {

        // valueがnullの場合処理しない
        if (value == null) {
            return null;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        TypeDescriptor targetValue = beanWrapper.getPropertyTypeDescriptor(field);
        if (targetValue != null) {
            String type = targetValue.getName();

            if ("java.lang.String".equals(type)) {
                return new String[] {(String) beanWrapper.getPropertyValue(field)};
            } else if ("java.lang.String[]".equals(type)) {
                return (String[]) beanWrapper.getPropertyValue(field);
            }
        }

        return null;
    }

    /**
     * フィールドからMap値を取得
     *
     * @param value
     * @param field
     *
     * @return フィールドのMap値のList
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> getFieldValueMapList(Object value, String field) {

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        TypeDescriptor targetValue = beanWrapper.getPropertyTypeDescriptor(field);
        if (targetValue != null) {

            String type = targetValue.getName();

            if ("java.util.Map".equals(type)) {

                Map<?, ?> tmpMap = (Map<?, ?>) beanWrapper.getPropertyValue(field);
                if (tmpMap == null) {
                    return null;
                }

                boolean errorFlag = false;

                // Mapの型が<Stirng, String>かどうか
                for (Map.Entry<?, ?> entry : tmpMap.entrySet()) {
                    if (entry.getKey() instanceof String && entry.getValue() instanceof String)
                        ;
                    else {
                        errorFlag = true;
                    }
                }

                if (!errorFlag) {
                    return (Map<String, String>) beanWrapper.getPropertyValue(field);
                }
            }
        }
        return null;
    }
}