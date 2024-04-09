/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.config.security;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.stereotype.Component;

/**
 * SameSite設定用　TomcatContextCustomizer
 *
 * @author kaneda
 */
@Component
public class SameSiteCustomizer implements TomcatContextCustomizer {

    /**
     * customize
     * SameSiteCookiesの設定を行う
     *
     * @param context
     */
    @Override
    public void customize(final Context context) {
        final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
        cookieProcessor.setSameSiteCookies("None");
        context.setCookieProcessor(cookieProcessor);
    }
}