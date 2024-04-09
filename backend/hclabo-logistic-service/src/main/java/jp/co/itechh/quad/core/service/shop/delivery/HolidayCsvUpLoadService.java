/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.service.shop.delivery;

import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;

import java.io.File;

/**
 * 休日Csvアップロードサービス<br/>
 *
 * @author Author: ogawa
 */
public interface HolidayCsvUpLoadService {

    /**
     * 休日Csv登録処理<br/>
     *
     * @param uploadedFile アップロードファイル
     * @param validLimit バリデータエラー限界値(行数)
     * @param year 年
     * @param deliveryMethodSeq 配送方法SEQ
     * @return Csvアップロード結果
     */
    CsvUploadResult execute(File uploadedFile, int validLimit, Integer year, Integer deliveryMethodSeq);

}