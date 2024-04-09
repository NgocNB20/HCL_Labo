/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.util.common;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import org.springframework.core.env.Environment;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * プロパティファイルUtil
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class PropertiesUtil {

    /**
     * 空のコンストラクタ
     */
    private PropertiesUtil() {
    }

    /**
     * システムプロパティの該当keyのvalueを取得
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static String getSystemPropertiesValue(String key) {
        Environment env = ApplicationContextUtility.getBean(Environment.class);
        return env.getProperty(key);
    }

    /**
     * リソースファイル取得
     *
     * @param resourceName リソースファイル名取得
     * @return リソースファイル
     */
    public static ResourceBundle getResource(String resourceName) {
        return getResource(resourceName, Locale.JAPANESE, PropertiesUtil.class.getClassLoader());
    }

    /**
     * リソースファイル取得
     *
     * @param resourceName リソースファイル名取得
     * @param locale       ロケール
     * @param classLoader  クラスローダー
     * @return リソースファイル
     */
    public static ResourceBundle getResource(String resourceName, Locale locale, ClassLoader classLoader) {
        return ResourceBundle.getBundle(resourceName, locale, classLoader, new PropertiesControl());
    }
}