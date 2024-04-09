/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVRNumberGreater;
import jp.co.itechh.quad.front.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.NumberUtility;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <span class="logicName">【数値比較（対象項目 ＞ 比較項目）】</span>数値の相関バリデータ。<br />
 * <br />
 * 対象数値 ＞ 比較数値でない場合エラー<br />
 *
 * @author kimura
 */
@Data
public class HNumberGreaterValidator implements ConstraintValidator<HVRNumberGreater, Object> {

    /** 対象数値が比較数値以下の場合 */
    public static final String NOT_GREATER_MESSAGE_ID = "{HNumberGreaterValidator.GREATER_detail}";

    /** 相関チェック対象 */
    private String target;

    /** 相関チェック比較対象 */
    private String comparison;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRNumberGreater annotation) {
        target = annotation.target();
        comparison = annotation.comparison();
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

        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

        // 数値の場合
        if (numberUtility.isNumber(targetValue) && numberUtility.isNumber(comparisonValue)) {

            // 比較チェック
            if (!isGreaterThan(targetValue, comparisonValue)) {
                makeContext(context, messageId, target);
                return false;
            }

        } else {
            // @HVNumberでチェックしているので、ここでは未実施
            return true;
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
     * targetValue が comparisonValue より大きいか。
     *
     * @param targetValue     対象の値
     * @param comparisonValue 比較対象の値
     * @return true..OK / false..NG
     */
    protected boolean isGreaterThan(Object targetValue, Object comparisonValue) {

        BigDecimal bigDecimal = BigDecimalConversionUtil.toBigDecimal(targetValue);
        BigDecimal comparisonBigDecimal = BigDecimalConversionUtil.toBigDecimal(comparisonValue);

        return bigDecimal.compareTo(comparisonBigDecimal) > 0;
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