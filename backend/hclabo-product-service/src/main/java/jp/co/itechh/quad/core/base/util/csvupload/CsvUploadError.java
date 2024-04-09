/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.csvupload;

import lombok.Data;

import java.io.Serializable;

/**
 * Csvアップロードエラー格納Dto
 *
 * @author natume
 * @version $Revision: 1.1 $
 */
@Data
public class CsvUploadError implements Serializable {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 行番号
     */
    private int row;

    /**
     * 行範囲（rowに設定された行番号を含めた同一主キーの行数をカウント）
     */
    private int rowSpan;

    /**
     * メッセージコード
     */
    private String messageCode;

    /**
     * 引数
     */
    private Object[] args;

    /**
     * メッセージ
     */
    private String message;
}
