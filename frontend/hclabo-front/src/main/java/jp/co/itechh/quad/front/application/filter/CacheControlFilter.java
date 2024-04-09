/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.application.filter;

import org.springframework.http.HttpHeaders;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * htmlのキャッシュの設定
 * 作成日：2021/10/12
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class CacheControlFilter implements Filter {

    /**
     * htmlのキャッシュの設定
     *
     * @param servletRequest servletRequest リクエスト
     * @param servletResponse servletResponse レスポンス
     * @param filterChain filterChain フィルター
     * @throws IOException IOException 例外
     * @throws ServletException ServletException 例外
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                    throws IOException, ServletException {
        // htmlのキャッシュの設定を行う
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");

        // 次の処理
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初期化処理
     *
     * @param arg0  設定
     * @throws ServletException 発生した例外
     *
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // 特になし
    }

    /**
     * 破棄処理
     */
    @Override
    public void destroy() {
        // 特になし
    }

}