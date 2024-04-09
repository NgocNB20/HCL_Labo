/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.csvupload;

import jp.co.itechh.quad.core.base.exception.ValidatorException;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
            tmp = PropertiesUtil.getResourceValue("config.hitmall.coreMessages", ORDINARY_MESSAGE_ID);
        } catch (Exception e) {
            tmp = "CSVファイル{0,number,0}行{1,number,0}列";
        }
        ORDINARY_MESSAGE_FORMAT = tmp;

        //
        // 可変CSV用メッセージ
        //

        try {
            tmp = PropertiesUtil.getResourceValue("config.hitmall.coreMessages", VARIABLE_MESSAGE_ID);
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
}
