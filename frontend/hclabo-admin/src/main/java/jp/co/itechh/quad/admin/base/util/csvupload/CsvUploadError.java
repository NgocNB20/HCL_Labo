/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.util.csvupload;

import lombok.Data;

import java.io.Serializable;

/**
 * Csvアップロードエラー格納Dto<br/>
 *
 * @author natume
 * @version $Revision: 1.1 $
 */
@Data
public class CsvUploadError implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 行番号<br/>
     */
    private int row;

    /**
     * メッセージコード<br/>
     */
    private String messageCode;

    /**
     * 引数<br/>
     */
    private Object[] args;

    /**
     * メッセージ<br/>
     */
    private String message;

}
