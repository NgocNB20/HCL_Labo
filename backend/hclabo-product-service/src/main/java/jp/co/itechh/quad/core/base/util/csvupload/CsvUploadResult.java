/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.csvupload;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Csvアップロード結果Dto
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
public class CsvUploadResult implements Serializable {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * バリデータエラー限界値(行数)
     */
    public static int CSV_UPLOAD_VALID_LIMIT = 10;

    /**
     * Csvバリデータ結果
     */
    private CsvValidationResult csvValidationResult;

    /**
     * Csvアップロードエラーリスト
     */
    private List<CsvUploadError> csvUploadErrorlList;

    /**
     * Csvデータ件数
     */
    private int recordCount;

    /**
     * 成功回数
     */
    private int successCount;

    /**
     * エラー件数
     */
    private int errorCount;

    /**
     * バリデートエラーを中断したかのフラグ
     */
    private boolean validLimitFlg;

    /**
     * csvデータ行数(ヘッダー行を除く)
     */
    public int csvRowCount;

    /**
     * 登録データ行数
     */
    public int mergeRowCount;

    /**
     * バリデーションチェックまたはデータチェックでエラーが出ているか判定
     *
     * @return true=エラー、false=エラーなし
     */
    public boolean isCsvUploadError() {
        return isInValid() || isError();
    }

    /**
     * エラー判定
     *
     * @return true=エラー、false=エラーなし
     */
    public boolean isError() {
        return csvUploadErrorlList != null && !csvUploadErrorlList.isEmpty();
    }

    /**
     * バリデータエラー判定
     *
     * @return true=エラー、false=エラーなし
     */
    public boolean isInValid() {
        return csvValidationResult != null && !csvValidationResult.isValid();
    }

    /**
     * 登録更新件数をインクリメントする
     */
    public void addMergeCount() {
        mergeRowCount++;
    }

}
