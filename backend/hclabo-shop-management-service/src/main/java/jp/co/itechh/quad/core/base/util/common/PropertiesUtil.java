/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.util.common;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import org.springframework.core.env.Environment;

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
}