/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.common;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import org.springframework.core.env.Environment;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * プロパティファイルUtil
 *
 * @author natume
 * @author tomo(itec) 2012/09/13 HM3.3 UTF8のリソースをResourceBundleで扱うための対応
 */
public class PropertiesUtil {

    /**
     * 空のコンストラクタ
     */
    private PropertiesUtil() {
    }

    /**
     * 指定プロパティからキーの値を取得
     *
     * @param resourceName リソース名
     * @param key          プロパティのkey
     * @return プロパティのvalue
     */
    public static String getResourceValue(String resourceName, String key) {
        String value = null;
        ResourceBundle bundle = getResource(resourceName);
        if (bundle.containsKey(key)) {
            value = bundle.getString(key);
        }
        return value;
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
     * システムプロパティの該当keyのvalueを取得
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static boolean getSystemPropertiesValueToBool(String key) {
        String value = getSystemPropertiesValue(key);
        return Boolean.valueOf(value);
    }

    /**
     * システムプロパティの該当keyのvalueを取得
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static int getSystemPropertiesValueToInt(String key) {
        String value = getSystemPropertiesValue(key);
        return Integer.parseInt(value);
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
