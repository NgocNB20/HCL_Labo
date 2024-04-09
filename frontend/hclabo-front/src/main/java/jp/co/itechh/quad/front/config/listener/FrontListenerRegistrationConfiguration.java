/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.config.listener;

import jp.co.itechh.quad.front.application.listener.AuthenticationEventListeners;
import jp.co.itechh.quad.front.application.listener.SessionLogListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Listener 設定クラス
 *
 * @author kimura
 */
@Configuration
public class FrontListenerRegistrationConfiguration {

    /**
     * SessionLogListenerを登録
     * @return SessionLogListener
     */
    @Bean
    public SessionLogListener sessionLogListener() {
        // SessionLogListenerを登録
        SessionLogListener bean = new SessionLogListener();
        return bean;
    }

    /**
     * AuthenticationEventListenersを登録
     * @return AuthenticationEventListeners
     */
    @Bean
    public AuthenticationEventListeners authenticationEventListeners() {
        // AuthenticationEventListenersを登録
        AuthenticationEventListeners bean = new AuthenticationEventListeners();
        return bean;
    }
}