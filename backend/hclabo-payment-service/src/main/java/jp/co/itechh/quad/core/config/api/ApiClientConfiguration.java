/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.config.api;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.pricecalculator.presentation.api.PriceCalculatorApi;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
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
 */
@Configuration
public class ApiClientConfiguration {

    @Value("${base-path-logistic-service}")
    private String basePathLogisticService;

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-price-planning-service}")
    private String basePathPricePlanningService;

    @Value("${base-path-order-service}")
    private String basePathOrderService;

    private RestTemplate restTemplate = ApplicationContextUtility.getBean(RestTemplate.class);

    @Bean
    @Primary
    @Scope("prototype")
    public ShippingSlipApi shippingSlipApi() {
        jp.co.itechh.quad.shippingslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.shippingslip.presentation.ApiClient(restTemplate);
        ShippingSlipApi shippingSlipApi = new ShippingSlipApi(apiClient);
        shippingSlipApi.getApiClient().setBasePath(basePathLogisticService);
        return shippingSlipApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public AddressBookApi AddressBookApi() {
        jp.co.itechh.quad.addressbook.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.addressbook.presentation.ApiClient(restTemplate);
        AddressBookApi addressBookApi = new AddressBookApi(apiClient);
        addressBookApi.getApiClient().setBasePath(basePathLogisticService);
        return addressBookApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public ShippingMethodApi ShippingMethodApi() {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        ShippingMethodApi shippingMethodApi = new ShippingMethodApi(apiClient);
        shippingMethodApi.getApiClient().setBasePath(basePathLogisticService);
        return shippingMethodApi;
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
    public SalesSlipApi salesSlipApi() {
        jp.co.itechh.quad.salesslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.salesslip.presentation.ApiClient(restTemplate);
        SalesSlipApi salesSlipApi = new SalesSlipApi(apiClient);
        salesSlipApi.getApiClient().setBasePath(basePathPricePlanningService);
        return salesSlipApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public PriceCalculatorApi priceCalculatorApi() {
        jp.co.itechh.quad.pricecalculator.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.pricecalculator.presentation.ApiClient(restTemplate);
        PriceCalculatorApi priceCalculatorApi = new PriceCalculatorApi(apiClient);
        priceCalculatorApi.getApiClient().setBasePath(basePathPricePlanningService);
        return priceCalculatorApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public OrderReceivedApi orderReceivedApi() {
        jp.co.itechh.quad.orderreceived.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.orderreceived.presentation.ApiClient(restTemplate);
        OrderReceivedApi orderReceivedApi = new OrderReceivedApi(apiClient);
        orderReceivedApi.getApiClient().setBasePath(basePathOrderService);
        return orderReceivedApi;
    }

    @Bean
    @Primary
    public NotificationSubApi notificationSubApi() {
        jp.co.itechh.quad.notificationsub.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.notificationsub.presentation.ApiClient(restTemplate);
        NotificationSubApi notificationSubApi = new NotificationSubApi(apiClient);
        notificationSubApi.getApiClient().setBasePath(basePathUserService);
        return notificationSubApi;
    }
}