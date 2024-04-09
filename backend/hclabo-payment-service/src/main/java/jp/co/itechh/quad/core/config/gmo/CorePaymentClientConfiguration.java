/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.config.gmo;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.impl.PaymentClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ペイメントクライアント 設定クラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Configuration
public class CorePaymentClientConfiguration {

    /**
     * ペイメントクライアントの定義
     * @return PaymentClient
     */
    @Bean
    PaymentClient paymentClient() {

        PaymentClientImpl paymentClientImpl = new PaymentClientImpl();
        return paymentClientImpl;

    }

}