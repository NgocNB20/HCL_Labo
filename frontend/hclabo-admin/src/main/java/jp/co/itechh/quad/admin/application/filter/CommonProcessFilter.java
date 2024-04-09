/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.application.filter;

import jp.co.itechh.quad.admin.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 共通処理Filter<br/>
 * フロントではSpringSecurityのFilter前に起動<br/>
 * バックではSpringSecurityのFilter後に起動
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/08 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
public class CommonProcessFilter implements Filter {

    /**
     * 共通処理
     * <ul>
     *  <li>HotDeploy対応</li>
     *  <li>共通処理サービスの実行</li>
     * </ul>
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

        // CommonInfoを取得
        // ※初回アクセス時は、このタイミングで取得と同時に、Session登録を行う
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);

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