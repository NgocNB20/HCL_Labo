/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.config.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * APIClientのDI登録クラス<br/>
 *
 * @author kimura
 *
 */
@Configuration
public class ApiClientConfiguration {

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-product-service}")
    private String basePathProductService;

    @Value("${base-path-logistic-service}")
    private String basePathLogisticService;

    private RestTemplate restTemplate = ApplicationContextUtility.getBean(RestTemplate.class);

    @Bean
    @Primary
    @Scope("prototype")
    public ProductApi productApi() {
        jp.co.itechh.quad.product.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.product.presentation.ApiClient(restTemplate);
        ProductApi productApi = new ProductApi(apiClient);
        productApi.getApiClient().setBasePath(basePathProductService);
        return productApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public StockApi stockApi() {
        StockApi stockApi = new StockApi();
        stockApi.getApiClient().setBasePath(basePathLogisticService);
        return stockApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public CustomerApi customerApi() {
        jp.co.itechh.quad.customer.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.customer.presentation.ApiClient(restTemplate);
        CustomerApi customerApi = new CustomerApi(apiClient);
        customerApi.getApiClient().setBasePath(basePathUserService);
        return customerApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public NotificationSubApi notificationSubApi() {
        jp.co.itechh.quad.notificationsub.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.notificationsub.presentation.ApiClient(restTemplate);
        NotificationSubApi notificationSubApi = new NotificationSubApi(apiClient);
        notificationSubApi.getApiClient().setBasePath(basePathUserService);
        return notificationSubApi;
    }
}