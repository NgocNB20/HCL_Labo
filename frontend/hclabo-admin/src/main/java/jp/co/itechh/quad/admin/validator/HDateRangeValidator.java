/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRDateRange;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAggregateUnit;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * HDateRangeValidator
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
public class HDateRangeValidator implements ConstraintValidator<HVRDateRange, Object> {

    /** if the search time exceeds the allowed time */
    public static final String RANGE_INVALID_MESSAGE_ID = "{HDateRangeValidator.INVALID_detail}";

    /** 相関チェック対象 */
    private String target;

    /** 相関チェック比較対象 */
    private String comparison;

    /** 単位 */
    private String unit;

    /** 範囲 */
    private int range;

    /** メッセージID */
    private String messageId;

    /** 書式 */
    private String pattern;

    /** 集計単位 */
    private String aggregateUnit;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRDateRange annotation) {
        messageId = annotation.message();
        target = annotation.target();
        comparison = annotation.comparison();
        pattern = annotation.pattern();
        range = annotation.range();
        unit = annotation.unit();
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

        String unitValue = getFieldValue(value, unit);

        // 全ての入力がされていない
        if (StringUtils.isEmpty(targetValue) && StringUtils.isEmpty(comparisonValue)) {
            return true;
        }

        // いずれかが入力されていない
        if (StringUtils.isEmpty(targetValue) || StringUtils.isEmpty(comparisonValue)) {
            return true;
        }

        // 日付ではない、または指定フォーマットを満たしていない
        if (!isDate(targetValue, comparisonValue)) {
            // @HVDateでチェックしているので、ここでは未実施
            return true;
        }

        if (isAfterThan(targetValue, comparisonValue, unitValue, range)) {
            String unit = Objects.requireNonNull(EnumTypeUtil.getEnumFromValue(HTypeAggregateUnit.class, unitValue))
                                 .getLabel();
            makeContext(context, messageId, target, range, unit);
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
     * value が targetValue 以後か。
     *
     * @param value       自コンポーネントの値
     * @param targetValue 対象コンポーネントの値
     * @param unit
     * @param range
     * @return true..OK / false..NG
     */
    protected boolean isAfterThan(String value, String targetValue, String unit, int range) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getPattern());

        LocalDate localDate = LocalDate.parse(value, formatter);
        LocalDate targetLocalDate = LocalDate.parse(targetValue, formatter);

        if (HTypeAggregateUnit.MONTH.getValue().equals(unit)) {
            targetLocalDate = targetLocalDate.plusMonths(range);
        } else if (HTypeAggregateUnit.DAY.getValue().equals(unit)) {
            targetLocalDate = targetLocalDate.plusDays(range);
        }

        return localDate.compareTo(targetLocalDate) >= 0;
    }

    /**
     * value と targetValue が日付かどうか
     *
     * @param value       自コンポーネントの値
     * @param targetValue 対象コンポーネントの値
     * @return true..OK / false..NG
     */
    protected boolean isDate(String value, String targetValue) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // パターンはカンマで分割
        String[] patterns = getPattern().split(",");

        if (dateUtility.isDate(value, patterns) && dateUtility.isDate(targetValue, patterns)) {
            return true;
        }

        return false;
    }

    /**
     * エラーメッセージを構成<br/>
     * メッセージセット＋エラーメッセージを表示する項目を指定
     */
    protected void makeContext(ConstraintValidatorContext context,
                               String messageId,
                               String errorField,
                               Integer range,
                               String unit) {

        // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
        if (context == null) {
            return;
        }

        HibernateConstraintValidatorContext hibernateContext =
                        context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();

        hibernateContext.addMessageParameter("range", range)
                        .addMessageParameter("unit", unit)
                        .buildConstraintViolationWithTemplate(messageId)
                        .addPropertyNode(errorField)
                        .addConstraintViolation();
    }
}