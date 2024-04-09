/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.front.base.util.seasar.TimestampConversionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <span class="logicName">【日付比較（対象項目 ≧ 比較項目）】</span>日付の相関バリデータ。<br />
 * <br />
 * 対象日付 ≧ 比較日付でない場合エラー<br />
 *
 * @author kimura
 *
 */
@Data
public class HDateGreaterEqualValidator implements ConstraintValidator<HVRDateGreaterEqual, Object> {

    /** 対象日付が比較日付より小さい場合 */
    public static final String NOT_GREATER_EQUAL_MESSAGE_ID = "{HDateGreaterEqualValidator.GREATER_EQUAL_detail}";

    /** 相関チェック対象 */
    private String target;

    /** 相関チェック比較対象 */
    private String comparison;

    /** メッセージID */
    private String messageId;

    /** 書式 */
    private String pattern;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRDateGreaterEqual annotation) {
        messageId = annotation.message();
        target = annotation.target();
        comparison = annotation.comparison();
        pattern = annotation.pattern();
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

        if (!isAfterThan(targetValue, comparisonValue)) {
            makeContext(context, messageId, target);
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
     * @return true..OK / false..NG
     */
    protected boolean isAfterThan(String value, String targetValue) {

        Timestamp timestamp = TimestampConversionUtil.toTimestamp(value, getPattern());
        Timestamp targetTimestamp = TimestampConversionUtil.toTimestamp(targetValue, getPattern());

        return timestamp.compareTo(targetTimestamp) >= 0;
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
    protected void makeContext(ConstraintValidatorContext context, String messageId, String errorFiled) {
        // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
        if (context == null) {
            return;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addPropertyNode(errorFiled).addConstraintViolation();
    }
}