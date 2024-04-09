/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRRequiredAllOrNothing;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 複数項目の必須チェックバリデータのアノテーション
 *
 * <pre>
 * 全て未入力、または全て入力の場合はOK
 * そうでない場合はNG
 * </pre>
 *
 * @author kimura
 */
public class HRequiredAllOrNothingValidator implements ConstraintValidator<HVRRequiredAllOrNothing, Object> {

    /** 全て未入力、または全て入力以外の場合 */
    public static final String REQUIRED_ALL_OR_NOTHING_MESSAGE_ID =
                    "{HRequiredAllOrNothingValidator.REQUIRED.SELECT_detail}";

    /** 相関チェック対象 */
    private String[] fields;

    /** エラー表示項目 */
    private String errorField;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRRequiredAllOrNothing annotation) {
        messageId = annotation.message();
        fields = annotation.fields();
        errorField = annotation.errorField();
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

        // 空の数をカウント
        int emptyCnt = 0;

        for (String target : fields) {
            String targetValue = getFieldValue(value, target);

            // 空の場合加算
            if (StringUtils.isEmpty(targetValue)) {
                emptyCnt++;
            }
        }

        // 全て入力、または全て未入力の場合
        if (emptyCnt == 0 || emptyCnt == fields.length) {
            return true;
        }

        // エラー処理
        if (ValidatorConstants.NOTARGET.equals(errorField)) {
            makeContext(context, messageId, fields[0]);
        } else {
            makeContext(context, messageId, errorField);
        }
        return false;
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