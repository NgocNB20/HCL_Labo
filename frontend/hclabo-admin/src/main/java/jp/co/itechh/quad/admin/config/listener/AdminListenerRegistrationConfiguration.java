/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.config.listener;

import jp.co.itechh.quad.admin.application.listener.SessionLogListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Listener 設定クラス
 *
 * @author kimura
 */
@Configuration
public class AdminListenerRegistrationConfiguration {

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
}