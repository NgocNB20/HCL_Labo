/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.util.csvupload;

import jp.co.itechh.quad.admin.validator.Validator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVバリデーションでNGになった項目の情報
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
@NoArgsConstructor
public class InvalidDetail implements Serializable {

    /** シリアライズ */
    private static final long serialVersionUID = 1L;

    /** NG行 */
    private Integer row;

    /** NG列 */
    private Integer column;

    /** NG列名 */
    private String columnName;

    /** NG列ラベル名 */
    private String columnLabel;

    /** 検証対象値 */
    private Object value;

    /** 使用したバリデータ */
    private Validator validator;

    /** バリデータに設定されたメッセージ　*/
    private String message;

    /** 可変CSV のエラーかどうか */
    private boolean variable;

    /**
     * コンストラクタ。
     *
     * @param row           例外発生行
     * @param column        例外発生列
     * @param columnName    例外発生列名
     * @param columnLabel   例外発生列ラベル名
     * @param value         検証対象となった値
     * @param validator     使用したバリデータ
     * @param message       バリデータに設定されているメッセージ
     * @param variable      可変CSVかどうか
     */
    public InvalidDetail(Integer row,
                         Integer column,
                         String columnName,
                         String columnLabel,
                         Object value,
                         Validator validator,
                         String message,
                         boolean variable) {
        this.row = row;
        this.column = column;
        this.columnName = columnName;
        this.columnLabel = columnLabel;
        this.value = value;
        this.validator = validator;
        this.message = message;
        this.variable = variable;
    }

    /**
     * コンストラクタ（シンプル版）。
     *
     * @param row           例外発生行
     * @param columnLabel   例外発生列ラベル名
     * @param message       バリデータに設定されているメッセージ
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
     * 該当行の詳細情報のみ抽出する
     *
     * @param list  InvalidDetailのリスト
     * @param row   対象行
     * @return 抽出されたリスト
     */
    public static List<InvalidDetail> getRowDetails(List<InvalidDetail> list, int row) {

        List<InvalidDetail> returnList = new ArrayList<>();

        for (InvalidDetail detail : list) {
            if (Integer.valueOf(row).equals(detail.row)) {
                returnList.add(detail);
            }
        }

        return returnList;
    }

    /**
     * 該当列の詳細情報のみ抽出する
     *
     * @param list   InvalidDetailのリスト
     * @param column 対象行
     * @return 抽出されたリスト
     */
    public static List<InvalidDetail> getColumnDetails(List<InvalidDetail> list, int column) {

        List<InvalidDetail> returnList = new ArrayList<>();

        for (InvalidDetail detail : list) {
            if (Integer.valueOf(column).equals(detail.column)) {
                returnList.add(detail);
            }
        }

        return returnList;
    }

    /**
     * 名称による該当列の詳細情報のみ抽出する
     *
     * @param list      InvalidDetailのリスト
     * @param columnName 対象行名
     * @return 抽出されたリスト
     */
    public static List<InvalidDetail> getColumnDetailsByName(List<InvalidDetail> list, String columnName) {

        List<InvalidDetail> returnList = new ArrayList<>();

        for (InvalidDetail detail : list) {
            if (columnName.equals(detail.columnName)) {
                returnList.add(detail);
            }
        }

        return returnList;
    }

    /**
     * オブジェクトを文字列化する
     * @return string
     */
    @Override
    public String toString() {
        return "InvalidDetail{row=" + this.row + ",column=" + this.column + ",columnName=" + this.columnName
               + ",columnLabel=" + this.columnLabel + ",value=" + this.value + "validator=" + this.validator
               + ",message=" + this.message + "}";
    }

    /**
     * @return the columnLabel
     */
    public String getColumnLabel() {
        return columnLabel;
    }

}