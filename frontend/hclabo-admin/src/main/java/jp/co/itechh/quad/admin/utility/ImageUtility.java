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

/**
 * 画像操作ヘルパークラス
 *
 * @author shibuya
 * @author Iwama (Itec) 2010/08/25 チケット #2202 対応
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class ImageUtility {

    /** 区切り文字（スラッシュ） */
    protected static final String SEPARATE = "_";

    /** 画像拡張子 */
    public static final String GIF = ".gif";

    /** 画像拡張子 */
    public static final String SVG = ".svg";

    /**
     * コンストラクタ<br/>
     */
    public ImageUtility() {
        // nop
    }

    /**
     * 一時ファイル名がユニークとなるよう、年～ミリ秒までを追加した文字列を返します
     *
     * @param  fileName ファイル名(拡張子なし)
     * @return 新ファイル名
     */
    public String createTempImageFileName(String fileName) {

        if (fileName != null) {

            // 日付関連Helper取得
            DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);

            return fileName + SEPARATE + dateHelper.format(dateHelper.getCurrentTime(), "yyyyMMddHHmmssSSS");
        }

        return null;
    }

    /**
     * 一時ファイル名がユニークとなるよう、年～ミリ秒までを追加した文字列を返します
     * newFileName + 年～ミリ秒 + fileNameの拡張子 を返します。
     *
     * @param  fileName ファイル名(拡張子あり)
     * @param  newFileName ファイル名(拡張子なし)
     * @return 新ファイル名
     */
    public String createTempImageFileNameExtension(String fileName, String newFileName) {

        String extension = getExtension(fileName);

        if (newFileName != null) {
            return createTempImageFileName(newFileName) + extension;
        }
        return null;
    }

    /**
     * ファイル名置換処理<br />
     * newFileName + fileNameの拡張子 を返します。
     *
     * @param  fileName ファイル名(拡張子あり)
     * @param  newFileName ファイル名(拡張子なし)
     * @return 新ファイル名
     */
    public String createImageFileNameExtension(String fileName, String newFileName) {

        String extension = getExtension(fileName);

        if (newFileName != null) {
            return newFileName + extension;
        }
        return null;
    }

    /**
     * 拡張子取得
     *
     * @param fileName ファイル名
     * @return 拡張子
     */
    public String getExtension(String fileName) {

        String extension = "";
        // ファイル名が有効な場合
        if (fileName != null) {
            int beginIndex = fileName.lastIndexOf(".");
            if (beginIndex >= 0) {
                extension = (fileName.substring(beginIndex)).toLowerCase();
            }
        }

        return extension;
    }

    /**
     * 画像テンプパスを取得
     *
     * @return テンプパス
     */
    public String getTempPath() {
        return PropertiesUtil.getSystemPropertiesValue("server.servlet.context-path")
               + PropertiesUtil.getSystemPropertiesValue("tmp.path");
    }

    /**
     * 画像パスを取得
     *
     * @return 画像保存先パス
     */
    public String getRealTempPath() {
        return PropertiesUtil.getSystemPropertiesValue("real.tmp.path");
    }
}