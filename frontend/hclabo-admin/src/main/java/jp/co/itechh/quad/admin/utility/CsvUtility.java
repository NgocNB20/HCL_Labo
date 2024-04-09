/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.utility;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static jp.co.itechh.quad.admin.base.utility.DateUtility.HM;
import static jp.co.itechh.quad.admin.base.utility.DateUtility.HMS;
import static jp.co.itechh.quad.admin.base.utility.DateUtility.YMD_SLASH;

/**
 * CSVヘルパークラス<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更
 *
 */
@Component
public class CsvUtility {

    /**
     * 時間フォーマット(アップロード用)<br/>
     */
    protected static final String FORMAT_UP = "yyyyMMdd_HHmmssSSS";

    /**
     * 時間フォーマット(ダウンロード用)<br/>
     */
    protected static final String FORMAT_DOWN = "yyyyMMdd_HHmmss";

    /**
     * アップロード時に許可する日時のフォーマット<br/>
     * "yyyy/MM/dd, yyyy/MM/dd HH:mm:ss, yyyy/MM/dd HH:mm"
     */
    public static final String UPLOAD_TIME_FORMAT =
                    YMD_SLASH + "," + YMD_SLASH + " " + HMS + "," + YMD_SLASH + " " + HM;

    /**
     * Csvアップロード接頭語<br/>
     */
    protected static final String PREFIX_UP = "up_";

    /**
     * Csvアップロード接尾語<br/>
     */
    protected static final String SUFFIX_CSV = ".csv";

    /**
     * セパレータ<br/>
     */
    protected static final String SEPARATOR = "/";

    /**
     * テンプファイルディレクトリパス<br/>
     */
    protected static final String SYSTEM_REAL_TMP_KEY = "real.tmp.path";

    /** バッチ処理対象ファイルを配置するパスを取得するためにキー */
    protected static final String BATCH_FILE_PATH_KEY = "batch.file.path";

    /** 商品画像アップロード先絶対パス(ZIP画像ファイルアップロードの場合に利用) */
    public static final String UPLOAD_PATH_GOODS = "goodsimage.input.directory";

    /** デザイン画像アップロード先絶対パス(ZIP画像ファイルアップロードの場合に利用) */
    public static final String UPLOAD_PATH_DESIGN = "goodsimage.d_images.directory";

    /**
     * コンストラクタ<br/>
     */
    public CsvUtility() {
        // nop
    }

    /**
     * Csvアップロードテンプファイル名の取得<br/>
     * 「up_ファイル名yyyyMMdd_HHmmssSSS.csv」のフォーマットで作成する
     *
     * @param fileName ファイル名
     * @return テンプファイル名
     */
    public String getUploadCsvTmpFileName(String fileName) {
        return getTmpPath() + SEPARATOR + PREFIX_UP + fileName + getDate(FORMAT_UP) + SUFFIX_CSV;
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

    /**
     * バッチへ渡す
     *
     * @param zipImageTarget アップロード先判定
     * @return パスを含むファイル名
     */
    public String getBatchTargetImageName(String zipImageTarget) throws IOException {
        // 展開先のパスを取得
        String uploadPath = UPLOAD_PATH_GOODS;
        if (zipImageTarget != null) {
            if (zipImageTarget.equals("1")) {
                uploadPath = UPLOAD_PATH_DESIGN;
            }
        }
        String imageFilePath = PropertiesUtil.getSystemPropertiesValue(uploadPath);
        String separator = System.getProperty("file.separator");
        if (imageFilePath.lastIndexOf(separator) != (imageFilePath.length() - 1)) {
            imageFilePath = imageFilePath + separator;
        }
        return imageFilePath;
    }

    /**
     * PDF一時ファイルパスの取得
     *
     * @param pdfFileName PDFのファイル名
     * @return PDF一時ファイルパス
     */
    public String getPdfTargetFileName(String pdfFileName) {
        final String path = PropertiesUtil.getSystemPropertiesValue(BATCH_FILE_PATH_KEY);
        return path + File.separator + pdfFileName;
    }


    /**
     * Csvダウンロードファイル名の取得<br/>
     * 「ファイル名yyyyMMdd_HHmmss.csv」<br/>
     *
     * @param fileName ファイル名
     * @return ダウンロードファイル名
     */
    public String getDownLoadCsvFileName(String fileName) {
        return fileName + getDate(FORMAT_DOWN) + SUFFIX_CSV;
    }

    /**
     * 指定フォーマットの日時を取得<br/>
     *
     * @param format フォーマット
     * @return 日時
     */
    protected String getDate(String format) {

        // 日付関連Helper取得
        DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);

        return dateHelper.format(dateHelper.getCurrentTime(), format);
    }

    /**
     * テンプパス取得<br/>
     *
     * @return テンプパス
     */
    protected String getTmpPath() {
        return PropertiesUtil.getSystemPropertiesValue(SYSTEM_REAL_TMP_KEY);
    }

}