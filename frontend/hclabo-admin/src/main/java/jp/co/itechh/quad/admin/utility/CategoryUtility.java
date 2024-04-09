/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.utility;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
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

    /** カテゴリーSEQの文字数 */
    public static final Integer CATEGORY_SEQ_LENGTH = 8;

    /** カテゴリーパスの文字数 */
    public static final Integer CATEGORY_PATH_LENGTH = 4;

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
     * カテゴリ画像配置パスの取得<br/>
     *
     * @return カテゴリ画像配置パス
     */
    public String getCategoryImageRealPath() {
        return PropertiesUtil.getSystemPropertiesValue("real.images.path.category") + SEPARATOR;
    }

    /**
     * カテゴリ画像の一時配置パスの取得<br/>
     *
     * @param fileName カテゴリ画像のファイル名
     * @return カテゴリ画像の一時配置パス
     */
    public String getCategoryImageTempPath(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return null;
        }
        ImageUtility imageUtility = ApplicationContextUtility.getBean(ImageUtility.class);
        return imageUtility.getTempPath() + SEPARATOR + fileName;
    }

    /**
     * TOPのカテゴリーIDを取得<br/>
     *
     * @return TOPのカテゴリーID
     */
    public String getCategoryIdTop() {
        return PropertiesUtil.getSystemPropertiesValue(CATEGORY_ID_TOP);
    }

}