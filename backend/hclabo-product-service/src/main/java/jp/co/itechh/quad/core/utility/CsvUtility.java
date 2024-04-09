/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CSVヘルパークラス
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更
 *
 */
@Component
public class CsvUtility {

    /** バッチ処理対象ファイルを配置するパスを取得するためにキー */
    protected static final String BATCH_FILE_PATH_KEY = "batch.file.path";

    /**
     * コンストラクタ
     */
    public CsvUtility() {
        // nop
    }

    /**
     * バッチへ渡す
     *
     * @param batchName バッチ名 … SHIPMENT_REGSIT 等
     * @return パスを含むファイル名
     */
    public String getBatchTargetFileName(String batchName) {
        final String stamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        final int randomValue = (int) (Math.abs(Math.random()) * 10000);
        final String path = PropertiesUtil.getSystemPropertiesValue(BATCH_FILE_PATH_KEY);
        final String fileName = path + File.separator + batchName + "@" + stamp + "_" + randomValue + ".csv";
        return fileName;
    }

}