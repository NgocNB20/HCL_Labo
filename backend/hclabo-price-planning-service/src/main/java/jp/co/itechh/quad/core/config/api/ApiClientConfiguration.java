/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.config.api;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.method.presentation.api.PaymentMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
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

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-product-service}")
    private String basePathProductService;

    @Value("${base-path-order-service}")
    private String basePathOrderService;

    @Value("${base-path-promotion-service}")
    private String basePathPromotionService;

    @Value("${base-path-logistic-service}")
    private String basePathLogisticService;

    @Value("${base-path-payment-service}")
    private String basePathPaymentService;

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
    public OrderReceivedApi orderReceivedApi() {
        jp.co.itechh.quad.orderreceived.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.orderreceived.presentation.ApiClient(restTemplate);
        OrderReceivedApi orderReceivedApi = new OrderReceivedApi(apiClient);
        orderReceivedApi.getApiClient().setBasePath(basePathOrderService);
        return orderReceivedApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public OrderSlipApi orderSlipApi() {
        jp.co.itechh.quad.orderslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.orderslip.presentation.ApiClient(restTemplate);
        OrderSlipApi orderSlipApi = new OrderSlipApi(apiClient);
        orderSlipApi.getApiClient().setBasePath(basePathPromotionService);
        return orderSlipApi;
    }

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
    public AddressBookApi addressBookApi() {
        jp.co.itechh.quad.addressbook.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.addressbook.presentation.ApiClient(restTemplate);
        AddressBookApi addressBookApi = new AddressBookApi(apiClient);
        addressBookApi.getApiClient().setBasePath(basePathLogisticService);
        return addressBookApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public BillingSlipApi billingSlipApi() {
        jp.co.itechh.quad.billingslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.billingslip.presentation.ApiClient(restTemplate);
        BillingSlipApi billingSlipApi = new BillingSlipApi(apiClient);
        billingSlipApi.getApiClient().setBasePath(basePathPaymentService);
        return billingSlipApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public PaymentMethodApi paymentMethodApi() {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        PaymentMethodApi paymentMethodApi = new PaymentMethodApi(apiClient);
        paymentMethodApi.getApiClient().setBasePath(basePathPaymentService);
        return paymentMethodApi;
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
    public ZipcodeApi zipCodeApi() {
        jp.co.itechh.quad.zipcode.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.zipcode.presentation.ApiClient(restTemplate);
        ZipcodeApi zipCodeApi = new ZipcodeApi(apiClient);
        zipCodeApi.getApiClient().setBasePath(basePathLogisticService);
        return zipCodeApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public ShippingMethodApi shippingMethodApi() {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        ShippingMethodApi shippingMethodApi = new ShippingMethodApi(apiClient);
        shippingMethodApi.getApiClient().setBasePath(basePathLogisticService);
        return shippingMethodApi;
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