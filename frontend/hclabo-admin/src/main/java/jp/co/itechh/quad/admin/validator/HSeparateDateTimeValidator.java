/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRSeparateDateTime;
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
 * <span class="logicName">【日時（日付、時刻分割）】</span>日付と時刻が別々の日時用バリデータクラス。<br />
 * <br />
 * 日付が未入力の場合エラー（時刻未入力は可）<br />
 * 日付の文字列が、カレンダー上に存在しない日付の場合や、時刻として妥当でない場合はエラー<br />
 *
 * @author kimura
 *
 */
@Data
public class HSeparateDateTimeValidator implements ConstraintValidator<HVRSeparateDateTime, Object> {

    /** 日付や時間じゃなかった場合 */
    public static final String NOT_DATE_TIME_MESSAGE_ID = "{HSeparateDateTimeValidator.NOT_DATE_TIME_detail}";

    /** 日付が未入力の場合 */
    public static final String NOT_DATE_MESSAGE_ID = "{HSeparateDateTimeValidator.NOT_DATE_detail}";

    /** 日付だけのフォーマットパターン */
    public static final String DATE_PATTERN = "yyyy/MM/dd";

    /** 時刻のフォーマットパターン */
    public static final String DATE_TIME_PATTERN = "HH:mm:ss";

    /** 日付のフォーマットパターン */
    private String datePattern = DATE_PATTERN;

    /** 時刻のフォーマットパターン */
    private String timePattern = DATE_TIME_PATTERN;

    /** フィールド名（日付） */
    String targetDate;

    /** フィールド名（時間） */
    String targetTime;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRSeparateDateTime annotation) {
        targetDate = annotation.targetDate();
        targetTime = annotation.targetTime();
        datePattern = annotation.datePattern();
        timePattern = annotation.timePattern();
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

        String targetDateValue = getFieldValue(value, targetDate);
        String targetTimeValue = getFieldValue(value, targetTime);

        boolean dateInputFlag = StringUtils.isEmpty(targetDateValue);
        boolean timeInputFlag = StringUtils.isEmpty(targetTimeValue);

        if (dateInputFlag && timeInputFlag) {
            // 全ての入力がされていない
            return true;
        } else if (dateInputFlag) {
            // 日付が入力されていない
            makeContext(context, NOT_DATE_MESSAGE_ID, targetDate);
            return false;
        }

        String allowPattern;
        String unitingDateTime;

        if (timeInputFlag) {
            // 時刻が入力されていない
            // 日付のみでチェックする
            allowPattern = getDatePattern();
            unitingDateTime = targetDateValue.toString();
        } else {
            // 日付と時刻が入力されている
            allowPattern = getDatePattern() + " " + getTimePattern();
            unitingDateTime = targetDateValue.toString() + " " + targetTimeValue;
        }

        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        if (!dateUtility.isDate(unitingDateTime, allowPattern)) {
            makeContext(context, messageId, targetTime);
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