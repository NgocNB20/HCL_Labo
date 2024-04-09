/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.utility;

import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import org.springframework.stereotype.Component;

/**
 * カテゴリーヘルパークラス<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更
 *
 */
@Component
public class CategoryUtility {

    /** ディレクトリセパレータ */
    protected static final String SEPARATOR = "/";

    /**
     * 選択中画像<br/>
     * ※サイドメニューを想定
     */
    protected static final String ON = "_o";

    /** サイドメニューカテゴリ画像パス取得キー */
    protected static final String IMAGES_PATH_LNAV_CATEGORY = "images.path.lnav_category";

    /** サイドメニューカテゴリ画像ファイル命名キー */
    protected static final String LNAV_CATEGORY_NAME = "lnav_category.name";

    /** TOPのカテゴリーIDを取得するキー */
    protected static final String CATEGORY_ID_TOP = "category.id.top";

    /**
     * サイドメニューカテゴリ画像配置パスの取得<br/>
     *
     * @return カテゴリ画像配置パス
     */
    public String getLnavCategoryImageDilectory() {
        return PropertiesUtil.getSystemPropertiesValue(IMAGES_PATH_LNAV_CATEGORY) + SEPARATOR;
    }

    /**
     * サイドメニューカテゴリ画像ファイルを取得<br/>
     *
     * @param categoryId カテゴリID
     * @param status 選択状態 true:選択中 / false：日選択
     * @return カテゴリ画像配置パス
     */
    public String getLnavCategoryImageName(String categoryId, boolean status) {

        // ディレクトリ + カテゴリファイル命名規則
        String fileName = getLnavCategoryImageDilectory() + PropertiesUtil.getSystemPropertiesValue(LNAV_CATEGORY_NAME)
                          + categoryId;
        if (status) {
            // 選択中の場合 「_o」をつける。
            fileName = fileName + ON;
        }

        // 拡張子 svg
        return fileName + ImageUtility.SVG + "#" + categoryId;
    }

}