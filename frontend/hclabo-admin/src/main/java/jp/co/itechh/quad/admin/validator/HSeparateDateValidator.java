/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRSeparateDate;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <span class="logicName">【日付（年月日分割）】</span>年、月、日の入力フィールドが別々の日付用バリデータクラス。<br />
 * <br />
 * 年、月、日の文字列が、カレンダー上に存在しない日付の場合はエラー<br />
 *
 * @author kimura
 */
@Data
public class HSeparateDateValidator implements ConstraintValidator<HVRSeparateDate, Object> {

    /** 日付じゃなかった場合 */
    public static final String NOT_DATE_MESSAGE_ID = "{HSeparateDateValidator.NOT_DATE_detail}";

    /** 日付のフォーマットパターン */
    public static final String DATE_PATTERN = "yyyy/MM/dd";

    /** フォーマットパターン */
    private String pattern = DATE_PATTERN;

    /** フィールド名（年） */
    String targetYear;

    /** フィールド名（月） */
    String targetMonth;

    /** フィールド名（日） */
    String targetDate;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRSeparateDate annotation) {
        messageId = annotation.message();
        targetYear = annotation.targetYear();
        targetMonth = annotation.targetMonth();
        targetDate = annotation.targetDate();
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

        String targetYearValue = getFieldValue(value, targetYear);
        String targetMonthValue = getFieldValue(value, targetMonth);
        String targetDateValue = getFieldValue(value, targetDate);

        boolean yearInputFlag = StringUtils.isEmpty(targetYearValue);
        boolean monthInputFlag = StringUtils.isEmpty(targetMonthValue);
        boolean dateInputFlag = StringUtils.isEmpty(targetDateValue);

        // 全ての入力がされていない
        if (yearInputFlag && monthInputFlag && dateInputFlag) {
            return true;
        }

        // いずれかが入力されていない
        else if (yearInputFlag || monthInputFlag || dateInputFlag) {

            // 日 ＞ 月 ＞ 年 の順でエラーメッセージを表示する
            if (dateInputFlag) {
                makeContext(context, messageId, targetDate);
                return false;
            }

            if (monthInputFlag) {
                makeContext(context, messageId, targetMonth);
                return false;
            }

            if (yearInputFlag) {
                makeContext(context, messageId, targetYear);
                return false;
            }
        }

        String unitingDate = targetYearValue + "/" + targetMonthValue + "/" + targetDateValue;

        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        if (!dateUtility.isDate(unitingDate, getPattern())) {
            makeContext(context, messageId, targetDate);
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