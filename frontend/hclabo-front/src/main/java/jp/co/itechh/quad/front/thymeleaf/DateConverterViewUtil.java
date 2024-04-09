/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.thymeleaf;

import java.text.SimpleDateFormat;

/**
 * <span class="logicName">【日付変換】</span>日付コンバータクラス<br />
 * 画面表示用のみが該当
 *
 * @author kimura
 */
public class DateConverterViewUtil {

    /**
     * 日付をデフォルトフォーマットに変換し出力
     *
     * @param value
     * @return 変換結果
     */
    public String convert(final Object value) {

        return convertToView(value, "yyyy/MM/dd");
    }

    /**
     * 日付を指定フォーマットに変換し出力
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
                return new SimpleDateFormat(pattern).format(value);
            }
        } catch (IllegalArgumentException iae) {
            // 変換せずに表示
            return value.toString();
        }
    }
}