/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.aop.cache;

import jp.co.itechh.quad.admin.annotation.cache.DisableCache;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * キャッシュ設定のインターセプタ
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class CacheInterceptor extends WebContentInterceptor {

    /**
     * ヘッダにてキャッシュ設定を行う
     *
     * @param request HttpServletRequest リクエスト
     * @param response HttpServletResponse レスポンス
     * @param handler Object
     * @param modelAndView ModelAndView
     * @throws Exception Exception 例外
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (handlerMethod.hasMethodAnnotation(DisableCache.class)) {
            disableCacheHeaders(response);
        }
    }

    /**
     * ヘッダにてキャッシュ設定を行う
     *
     * @param response HttpServletResponse レスポンス
     */
    private void disableCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "private, no-store, no-cache, max-age=0, must-revalidate");
    }

}