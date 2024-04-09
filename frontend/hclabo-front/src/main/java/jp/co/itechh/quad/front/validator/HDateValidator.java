/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVDate;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <span class="logicName">【日付】</span>日付チェックバリデータ。<br />
 * <br />
 * 文字列が、カレンダー上に存在しない日付の場合はエラー<br />
 *
 * @author kimura
 */
@Data
public class HDateValidator implements ConstraintValidator<HVDate, Object> {

    /** 日付じゃなかった場合 */
    public static final String NOT_DATE_MESSAGE_ID = "{HDateValidator.NOT_DATE_detail}";

    /** 時間じゃなかった場合 */
    public static final String NOT_DATE_TIME_MESSAGE_ID = "{HDateValidator.NOT_DATE_TIME_detail}";

    /** デフォルトのフォーマットパターン */
    public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";

    /** フォーマットパターン（カンマで区切って複数指定可能） */
    private String pattern = DEFAULT_DATE_PATTERN;

    /**
     * アノテーションの情報設定
     */
    @Override
    public void initialize(HVDate annotation) {
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

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // パターンはカンマで分割
        String[] patterns = this.pattern.split(",");

        if (!dateUtility.isDate(value, patterns)) {
            return false;
        }

        return true;
    }
}