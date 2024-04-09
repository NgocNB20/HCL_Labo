/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.csvupload;

import lombok.Data;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * CSVバリデーションでNGになった項目の情報
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
public class InvalidDetail implements Serializable {

    /**
     * シリアライズ
     */
    private static final long serialVersionUID = 1L;

    /**
     * NG行
     */
    private Integer row;

    /**
     * NG列
     */
    private Integer column;

    /**
     * NG列名
     */
    private String columnName;

    /**
     * NG列ラベル名
     */
    private String columnLabel;

    /**
     * 検証対象値
     */
    private Object value;

    /**
     * バリデータに設定されたメッセージ
     */
    private String message;

    /**
     * 可変CSV のエラーかどうか
     */
    private boolean variable;

    /**
     * コンストラクタ。
     *
     * @param row         例外発生行
     * @param column      例外発生列
     * @param columnName  例外発生列名
     * @param columnLabel 例外発生列ラベル名
     * @param value       検証対象となった値
     * @param message     バリデータに設定されているメッセージ
     * @param variable    可変CSVかどうか
     */
    public InvalidDetail(Integer row,
                         Integer column,
                         String columnName,
                         String columnLabel,
                         Object value,
                         String message,
                         boolean variable) {
        this.row = row;
        this.column = column;
        this.columnName = columnName;
        this.columnLabel = columnLabel;
        this.value = value;
        this.message = message;
        this.variable = variable;
    }

    /**
     * コンストラクタ（シンプル版）。
     *
     * @param row         例外発生行
     * @param columnLabel 例外発生列ラベル名
     * @param message     バリデータに設定されているメッセージ
     */
    public InvalidDetail(Integer row, String columnLabel, String message) {
        this.row = row;
        this.columnLabel = columnLabel;
        this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        if (this.variable) {
            return message.replace(MessageFormat.format(CsvValidationResult.VARIABLE_MESSAGE_FORMAT, this.row,
                                                        this.columnName
                                                       ), "{0}");
        }

        return message.replace(MessageFormat.format(CsvValidationResult.ORDINARY_MESSAGE_FORMAT, this.row, this.column),
                               "{0}"
                              );
    }

    /**
     * オブジェクトを文字列化する
     *
     * @return string
     */
    @Override
    public String toString() {
        return "InvalidDetail{row=" + this.row + ",column=" + this.column + ",columnName=" + this.columnName
               + ",columnLabel=" + this.columnLabel + ",value=" + this.value + ",message=" + this.message + "}";
    }

    /**
     * @return the columnLabel
     */
    public String getColumnLabel() {
        return columnLabel;
    }

}