/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * カテゴリーヘルパークラス
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
     * 選択中画像
     * ※サイドメニューを想定
     */
    protected static final String ON = "_o";

    /** TOPのカテゴリーIDを取得するキー */
    protected static final String CATEGORY_ID_TOP = "category.id.top";

    /**
     * カテゴリ画像配置パスの取得
     *
     * @return カテゴリ画像配置パス
     */
    public String getCategoryImageRealPath() {
        return PropertiesUtil.getSystemPropertiesValue("real.images.path.category") + SEPARATOR;
    }

    /**
     * カテゴリ画像配置パスの取得<br/>
     *
     * @return カテゴリ画像配置パス
     */
    public String getCategoryImagePath() {
        return PropertiesUtil.getSystemPropertiesValue("images.path.category");
    }

    /**
     * カテゴリ画像配置パスの取得<br/>
     *
     * @param fileName カテゴリ画像のファイル名
     * @return カテゴリ画像配置パス
     */
    public String getCategoryImagePath(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return null;
        }
        return getCategoryImagePath() + SEPARATOR + fileName;
    }

    /**
     * TOPのカテゴリーIDを取得
     *
     * @return TOPのカテゴリーID
     */
    public String getCategoryIdTop() {
        return PropertiesUtil.getSystemPropertiesValue(CATEGORY_ID_TOP);
    }

    /**
     * 商品公開判定
     * ※現在日時
     *
     * @param openStatus 公開状態
     * @param openStartTime 公開開始日時
     * @param openEndTime 公開終了日時
     * @param targetTime 比較時間
     * @return true=公開、false=公開でない
     */
    public boolean isOpen(HTypeOpenStatus openStatus,
                          Timestamp openStartTime,
                          Timestamp openEndTime,
                          Timestamp targetTime) {

        // 公開
        if (HTypeOpenStatus.OPEN.equals(openStatus)) {
            // 日付関連Helper取得
            DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);
            return dateHelper.isOpen(openStartTime, openEndTime, targetTime);
        }
        return false;
    }

}