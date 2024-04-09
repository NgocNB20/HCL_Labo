/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.thymeleaf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * <span class="logicName">【数値フォーマット】</span>数値をフォーマットするコンバータ<br />
 * 画面表示用のみが該当
 *
 * @author kimura
 */
public class NumberConverterViewUtil {

    /**
     * 数値をデフォルトフォーマットに変換し出力
     *
     * @param value
     * @return 変換結果
     */
    public String convert(final Object value) {

        return convertToView(value, "#,###");
    }

    /**
     * 数値を指定フォーマットに変換し出力
     *
     * @param value
     * @return 変換結果
     */
    public String convert(final Object value, final String pattern) {

        return convertToView(value, pattern);
    }

    /**
     * viewへの変換処理
     *
     * @param value   値
     * @param pattern 書式
     * @return 変換されたvalue
     */
    private String convertToView(Object value, String pattern) {
        try {
            if (value == null) {
                return null;
            } else {
                value = new BigDecimal(value.toString());
                Locale locale = Locale.getDefault();
                NumberFormat formatter = getNumberFormat(locale, pattern);
                return formatter.format(value);
            }
        } catch (NumberFormatException nfe) {
            // 変換せずに表示
            return value.toString();
        }
    }

    /**
     * 指定されたロケールの数値フォーマッターを返します。
     *
     * @param locale ロケール
     * @return 数値フォーマッター
     */
    private NumberFormat getNumberFormat(Locale locale, String pattern) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat(pattern, symbols);
        int maximumFractionDigits = 0;
        if (pattern.indexOf(".") > 0) {
            maximumFractionDigits = pattern.replaceAll(".*\\.", "").length();
        }

        formatter.setMaximumFractionDigits(maximumFractionDigits);
        formatter.setParseBigDecimal(true);
        formatter.setRoundingMode(RoundingMode.DOWN);// 小数部の桁あふれは切り捨てる

        return formatter;
    }
}