/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.util.csvupload;

import jp.co.itechh.quad.admin.base.exception.ValidatorException;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.validator.Validator;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * CSV Validation 時に発生した ValidatorException を保持するためのクラス。
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
public class CsvValidationResult implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1509283979233530705L;

    /** CSVバリデーションエラー情報を出力する際に使用するメッセージID */
    public static final String ORDINARY_MESSAGE_ID = CsvValidationResult.class.getName() + ".ORDINARY_TYPE";

    /** CSVバリデーションエラー情報を出力する際に使用するメッセージID */
    public static final String VARIABLE_MESSAGE_ID = CsvValidationResult.class.getName() + ".VARIABLE_TYPE";

    /** メッセージフォーマット */
    public static final String ORDINARY_MESSAGE_FORMAT;

    /** メッセージフォーマット */
    public static final String VARIABLE_MESSAGE_FORMAT;

    static {

        String tmp = null;

        //
        // 通常CSV用メッセージ
        //

        try {
            tmp = PropertiesUtil.getResourceValue("config.hitmall.adminMessages", ORDINARY_MESSAGE_ID);
        } catch (Exception e) {
            tmp = "CSVファイル{0,number,0}行{1,number,0}列";
        }
        ORDINARY_MESSAGE_FORMAT = tmp;

        //
        // 可変CSV用メッセージ
        //

        try {
            tmp = PropertiesUtil.getResourceValue("config.hitmall.adminMessages", VARIABLE_MESSAGE_ID);
        } catch (Exception e) {
            tmp = "CSVデータ{0,number,0}行目の{1}列";
        }
        VARIABLE_MESSAGE_FORMAT = tmp;
    }

    /** カラムごとの ValidatorException 情報 */
    private Map<String, List<ValidatorException>> exceptionMap;

    /** 行ごとの ValidatorException 情報 */
    private Map<Integer, List<ValidatorException>> exceptionLineMap;

    /** 検証NG詳細リスト */
    private List<InvalidDetail> detailList;

    /** Validation 終了後にオブジェクト内容を変更できないようにするためのフラグ。 */
    private boolean sealed;

    /**
     * コンストラクタ
     */
    public CsvValidationResult() {
        this.exceptionMap = new LinkedHashMap<>();
        this.exceptionLineMap = new LinkedHashMap<>();
        this.detailList = new ArrayList<>();
        this.sealed = false;
    }

    /**
     * ValidatorException を登録する
     *
     * @param row                   例外発生行
     * @param column                例外発生列
     * @param columnName            例外発生列名
     * @param columnLabel           例外発生列ラベル
     * @param value                 検証対象となった値
     * @param validator             使用したバリデータ
     * @param validationException   発生した例外
     * @param variable              可変CSVかどうか
     */
    public void addException(final Integer row,
                             final Integer column,
                             final String columnName,
                             final String columnLabel,
                             final Object value,
                             final Validator validator,
                             final ValidatorException validationException,
                             final boolean variable) {

        // 例外の登録処理が既に完了していた場合
        if (this.sealed) {
            throw new IllegalStateException("This ValidationResult has been sealed. Nothing could be added.");
        }

        //
        // カラム別例外情報
        //

        List<ValidatorException> colExcep = this.exceptionMap.get(columnName);

        // 初めて例外を登録する場合はリストオブジェクトを生成する
        if (colExcep == null) {
            colExcep = new LinkedList<>();
            this.exceptionMap.put(columnName, colExcep);
        }

        colExcep.add(validationException);

        //
        // 行別例外情報
        //

        List<ValidatorException> lineExcep = this.exceptionLineMap.get(row);

        // 初めて例外を登録する場合はリストオブジェクトを生成する
        if (lineExcep == null) {
            lineExcep = new LinkedList<>();
            this.exceptionLineMap.put(row, lineExcep);
        }

        lineExcep.add(validationException);

        //
        // 詳細情報
        //

        this.detailList.add(new InvalidDetail(row, column, columnName, columnLabel, value, validator,
                                              validationException.getMessage(), variable
        ));
    }

    /**
     * ValidationException の登録を終了する。
     */
    public void seal() {
        this.sealed = true;
    }

    /**
     * ValidationException が発生していなかったことを確認する。
     *
     * @return true - 妥当性に問題なし。<br />
     *         false - 妥当性に問題あり。
     */
    public boolean isValid() {

        boolean foundException = false;

        for (final List<ValidatorException> list : this.exceptionMap.values()) {
            if (list != null && !list.isEmpty()) {
                foundException = true;
                break;
            }
        }

        if (!this.detailList.isEmpty()) {
            foundException = true;
        }

        return !foundException;
    }

    /**
     * 妥当性に問題があったカラム名称のリストを取得する。
     *
     * @return 妥当性に問題があったカラム名称のリスト
     */
    public List<String> getInvalidColumnNames() {

        final List<String> colNames = new LinkedList<>();

        for (final String key : this.exceptionMap.keySet()) {
            final List<ValidatorException> list = this.exceptionMap.get(key);
            if (list != null && !list.isEmpty()) {
                colNames.add(key);
            }
        }

        return Collections.unmodifiableList(colNames);
    }

    /**
     * 妥当性に問題があった行のリストを取得する
     *
     * @return 妥当性に問題があった行番号のリスト
     */
    public List<Integer> getInvalidLines() {
        return new ArrayList<>(this.exceptionLineMap.keySet());
    }

    /**
     * 指定カラムで発生した ValidatorException の一覧を返す。
     *
     * @param columnName
     *            カラム名称
     * @return ValidatorException の一覧
     */
    public List<ValidatorException> getValidatorExceptionList(final String columnName) {
        return Collections.unmodifiableList(this.exceptionMap.get(columnName));
    }

    /**
     * 指定行で発生した ValidatorException の一覧を返す。
     *
     * @param lineNo    行番号
     * @return ValidatorException の一覧
     */
    public List<ValidatorException> getValidatorExceptionList(final Integer lineNo) {
        return Collections.unmodifiableList(this.exceptionLineMap.get(lineNo));
    }

    /**
     * 検証NG詳細リストを返す。
     *
     * @return 検証NG詳細リスト
     */
    public List<InvalidDetail> getInvalidDetailList() {
        return Collections.unmodifiableList(this.detailList);
    }

    /**
     * デバッグ用
     * @return メッセージ一覧
     */
    public String dump() {
        final StringBuilder dump = new StringBuilder();
        for (final String columnName : this.exceptionMap.keySet()) {
            List<ValidatorException> vList = this.exceptionMap.get(columnName);
            for (final ValidatorException ve : vList) {
                dump.append(columnName + " : " + ve.getMessage() + "\n");
            }
        }
        return dump.toString();
    }

}