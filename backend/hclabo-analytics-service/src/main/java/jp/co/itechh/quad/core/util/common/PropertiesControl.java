/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * UTF8で記述されたプロパティファイルを ResourceBundle で読み込むためのリソースバンドルコントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class PropertiesControl extends ResourceBundle.Control {

    /**
     * リソースバンドルの有効期限
     */
    private long timeToLive = ResourceBundle.Control.TTL_NO_EXPIRATION_CONTROL;

    /**
     * コンストラクタ
     */
    public PropertiesControl() {
    }

    /**
     * コントロールの再実装。
     * <p>
     * 文字コード（UTF-8）対応。
     *
     * @param baseName baseName
     * @param locale   locale
     * @param format   format
     * @param loader   loader
     * @param reload   reload
     * @return 生成したResourceBundle
     * @throws IllegalAccessException IllegalAccessException
     * @throws InstantiationException InstantiationException
     * @throws IOException            IOException
     */
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                    throws IllegalAccessException, InstantiationException, IOException {
        String bundleName = toBundleName(baseName, locale);
        ResourceBundle bundle = null;
        if (format.equals("java.class")) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends ResourceBundle> bundleClass =
                                (Class<? extends ResourceBundle>) loader.loadClass(bundleName);

                // If the class isn't a ResourceBundle subclass, throw a
                // ClassCastException.
                if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
                    bundle = bundleClass.newInstance();
                } else {
                    throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
                }
            } catch (ClassNotFoundException e) {
            }
        } else if (format.equals("java.properties")) {
            final String resourceName = toResourceName(bundleName, "properties");
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream = null;
            try {
                stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
                    @Override
                    public InputStream run() throws IOException {
                        InputStream is = null;
                        if (reloadFlag) {
                            URL url = classLoader.getResource(resourceName);
                            if (url != null) {
                                URLConnection connection = url.openConnection();
                                if (connection != null) {
                                    // Disable caches to get fresh data for
                                    // reloading.
                                    connection.setUseCaches(false);
                                    is = connection.getInputStream();
                                }
                            }
                        } else {
                            is = classLoader.getResourceAsStream(resourceName);
                        }
                        return is;
                    }
                });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
        } else {
            throw new IllegalArgumentException("unknown format: " + format);
        }
        return bundle;
    }

    /**
     * リソースバンドルの有効期間を返却する。
     *
     * @param baseName 使用しない
     * @param locale   使用しない
     * @return 設定されているキャッシュ有効期限(単位はミリ秒)
     */
    @Override
    public long getTimeToLive(String baseName, Locale locale) {
        return timeToLive;
    }
}