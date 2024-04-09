/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.base.util.common;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * プロパティファイルUtil
 *
 * @author natume
 * @author tomo(itec) 2012/09/13 HM3.3 UTF8のリソースをResourceBundleで扱うための対応
 *
 */
public class PropertiesUtil {

    /**
     * 空のコンストラクタ<br/>
     */
    private PropertiesUtil() {
    }

    /** プロパティコンポーネント名 systemProperties*/
    protected static final String SYSTEM_PROPERTIES = "systemProperties";

    /** プロパティコンポーネント名 htypelabel */
    protected static final String HTYPELABEL_PROPERTIES = "htypeLabelProperties";

    /** プロパティコンポーネント名 systemlabel */
    protected static final String LABEL_PROPERTIES = "systemLabelProperties";

    /** プロパティファイル pageaccess */
    protected static final String ACCESS_PROPERTIES = "pageaccess";

    /** 区切り文字：ドット */
    protected static final String SEPARATOR_PACHAGE = ".";

    /** 区切り文字：カンマ */
    protected static final String SEPARATOR_CAMMA = ",";

    /** 区切り文字：スラッシュ */
    protected static final String SEPARATOR_SLASH = "/";

    /** 空文字 */
    protected static final String BLANK_STRING = "";

    /** 文字:view */
    private static final String VIEW_STRING = "view";

    /** 本人認証カテゴリkey */
    protected static final String ATTESTATION_KEY = "attestation";

    /** 管理者ログインカテゴリkey */
    protected static final String ADMIN_LOGIN_KEY = "adminlogin";

    /** 管理者ログインエラーパスkey */
    protected static final String ADMIN_LOGIN_PATH_KEY = ADMIN_LOGIN_KEY + SEPARATOR_PACHAGE + "pagepath";

    /**
     * 本人認証チェックMap取得<br/>
     *
     * @return Map 本人認証のkeyのMap
     */
    public static Map<String, String> getAttestationKeyMap() {

        // プロパティ取得
        ResourceBundle resourceBundle = getResource(ACCESS_PROPERTIES);

        // 指定カテゴリのkeyのMapを作成
        Map<String, String> attestationMap = new LinkedHashMap<>();
        for (String key : resourceBundle.keySet()) {
            if (key.startsWith(ATTESTATION_KEY)) {
                attestationMap.put(
                                key.replace(ATTESTATION_KEY + SEPARATOR_PACHAGE, BLANK_STRING),
                                resourceBundle.getString(key)
                                  );
            }
        }
        return attestationMap;
    }

    /**
     * 管理者ログインチェックMap取得<br/>
     *
     * @return Map 管理者ログインのkeyのMap
     */
    public static Map<String, String> getAdminLoginsKeyMap() {

        // プロパティ取得
        ResourceBundle resourceBundle = getResource(ACCESS_PROPERTIES);

        // 指定カテゴリのkeyのMapを作成
        Map<String, String> attestationMap = new LinkedHashMap<>();
        for (String key : resourceBundle.keySet()) {
            if (key.startsWith(ADMIN_LOGIN_KEY)) {
                attestationMap.put(
                                key.replace(ADMIN_LOGIN_KEY + SEPARATOR_PACHAGE, BLANK_STRING),
                                resourceBundle.getString(key)
                                  );
            }
        }
        return attestationMap;
    }

    /**
     * 管理者ログインエラーパス<br/>
     *
     * @return 管理者ログインエラーパス
     */
    public static String getAdminLoginPath() {
        return getAccessPagePropertiesValue(ADMIN_LOGIN_PATH_KEY);
    }

    /**
     * 指定プロパティからキーの値を取得
     *
     * @param resourceName リソース名
     * @param key プロパティのkey
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
     * 指定プロパティからキーの値のMapを作成
     *
     * @param resourceName リソースファイル名
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static Map<String, String> getResourceValueMap(String resourceName, String key) {

        String value = getResourceValue(resourceName, key);

        if (value == null || BLANK_STRING.equals(value)) {
            return null;
        }

        Map<String, String> map = new HashMap<>();
        for (String val : value.split(SEPARATOR_CAMMA)) {
            map.put(val, val);
        }
        return map;
    }

    /**
     * urlから画面アクセスキーの取得
     *
     * @param url URL
     * @return view以下のURL「/」は、「.」に置換
     */
    public static String getAccessPageKey(String url) {

        if (url == null) {
            return null;
        }

        String accessUrl = url.substring(url.indexOf(SEPARATOR_SLASH + VIEW_STRING + SEPARATOR_SLASH) + 1);
        accessUrl = accessUrl.replaceAll(SEPARATOR_SLASH, SEPARATOR_PACHAGE);
        return accessUrl;
    }

    /**
     * アクセスページプロパティの該当keyのvalueを取得<br/>
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static String getAccessPagePropertiesValue(String key) {
        return getResourceValue(ACCESS_PROPERTIES, key);
    }

    /**
     * システムプロパティの該当keyのvalueを取得<br/>
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static String getSystemPropertiesValue(String key) {
        Environment env = ApplicationContextUtility.getBean(Environment.class);
        return env.getProperty(key);
    }

    /**
     * システムプロパティの該当keyのvalueを取得<br/>
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static boolean getSystemPropertiesValueToBool(String key) {
        String value = getSystemPropertiesValue(key);
        return Boolean.valueOf(value);
    }

    /**
     * システムプロパティの該当keyのvalueを取得<br/>
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static int getSystemPropertiesValueToInt(String key) {
        String value = getSystemPropertiesValue(key);
        return Integer.parseInt(value);
    }

    /**
     * ラベルプロパティの該当keyのvalueを取得<br/>
     *
     * @param key プロパティkey
     * @return プロパティvalue
     */
    public static String getLabelPropertiesValue(String key) {
        Environment env = ApplicationContextUtility.getBean(Environment.class);
        return env.getProperty(key);
    }

    /**
     * リソースファイル取得<br/>
     *
     * @param resourceName リソースファイル名取得
     * @return リソースファイル
     */
    public static ResourceBundle getResource(String resourceName) {
        return getResource(resourceName, Locale.JAPANESE, PropertiesUtil.class.getClassLoader());
    }

    /**
     * リソースファイル取得<br/>
     *
     * @param resourceName リソースファイル名取得
     * @param locale ロケール
     * @return リソースファイル
     */
    public static ResourceBundle getResource(String resourceName, Locale locale) {
        return getResource(resourceName, locale, PropertiesUtil.class.getClassLoader());
    }

    /**
     * リソースファイル取得<br/>
     *
     * @param resourceName リソースファイル名取得
     * @param locale ロケール
     * @param classLoader クラスローダー
     * @return リソースファイル
     */
    public static ResourceBundle getResource(String resourceName, Locale locale, ClassLoader classLoader) {
        return ResourceBundle.getBundle(resourceName, locale, classLoader, new PropertiesControl());
    }
}