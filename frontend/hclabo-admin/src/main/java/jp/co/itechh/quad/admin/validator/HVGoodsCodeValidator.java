/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVGoodsCode;
import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 製品コード チェック バリデーター。
 *
 */
@Data
public class HVGoodsCodeValidator implements ConstraintValidator<HVGoodsCode, Object> {

    public static final String MSGCD_GOODS_CODE_MAX_LENGTH = "{ITC-B000119}";

    private static final int GOODS_CODE_LINE_MAX_LENGTH = 21;

    private static final String LINE_SEPARATOR_SYMBOL = "\n";

    /**
     * バリデーション実行
     *
     * @return バリデーション実行
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // null or 空の場合未実施
        if (Objects.equals(value, null) || Objects.equals(value, "")) {
            return true;
        }

        String[] goodsCodeList = String.valueOf(value).split(LINE_SEPARATOR_SYMBOL);
        for (String line : goodsCodeList) {
            if (line.length() > GOODS_CODE_LINE_MAX_LENGTH) {
                makeContext(context, MSGCD_GOODS_CODE_MAX_LENGTH);
                return false;
            }
        }

        return true;
    }

    /**
     * エラーメッセージを構成<br/>
     * メッセージセット＋エラーメッセージを表示する項目を指定
     */
    protected void makeContext(ConstraintValidatorContext context, String messageId) {
        // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
        if (context == null) {
            return;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addConstraintViolation();
    }
}