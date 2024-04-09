/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.application.listener;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * アプリケーションログを出力するためのセッションリスナ
 *
 * @author kimura
 */
public class SessionLogListener implements HttpSessionListener, HttpSessionIdListener {

    /**
     * セッション生成時ログを出力する
     *
     * @param event イベント
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        applicationLogUtility.writeSessionCreatedLog(event);
    }

    /**
     * セッション破棄時ログを出力する
     *
     * @param event イベント
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        applicationLogUtility.writeSessionDestroyedLog(event);
    }

    /**
     * セッションID変更時ログを出力する
     *
     * @param event イベント
     * @param oldSessionId
     */
    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        applicationLogUtility.writeSessionCreatedLog(event);
    }
}