/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.api;

import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.card.presentation.api.CardApi;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.method.presentation.api.PaymentMethodApi;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
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

    @Value("${base-path-order-service}")
    private String basePathOrderService;

    @Value("${base-path-logistic-service}")
    private String basePathLogisticService;

    @Value("${base-path-payment-service}")
    private String basePathPaymentService;

    @Value("${base-path-price-planning-service}")
    private String basePathPricePlanningService;

    @Value("${base-path-user-service}")
    private String basePathUserService;

    private RestTemplate restTemplate = ApplicationContextUtility.getBean(RestTemplate.class);

    @Bean
    @Primary
    @Scope("prototype")
    public TransactionApi transactionApi() {
        jp.co.itechh.quad.transaction.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.transaction.presentation.ApiClient(restTemplate);
        TransactionApi transactionApi = new TransactionApi(apiClient);
        transactionApi.getApiClient().setBasePath(basePathOrderService);
        return transactionApi;
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
    public SettlementMethodApi settlementMethodApi() {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        SettlementMethodApi settlementMethodApi = new SettlementMethodApi(apiClient);
        settlementMethodApi.getApiClient().setBasePath(basePathPaymentService);
        return settlementMethodApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public MulpayApi mulpayApi() {
        jp.co.itechh.quad.mulpay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mulpay.presentation.ApiClient(restTemplate);
        MulpayApi mulpayApi = new MulpayApi(apiClient);
        mulpayApi.getApiClient().setBasePath(basePathPaymentService);
        return mulpayApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public CardApi cardApi() {
        jp.co.itechh.quad.card.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.card.presentation.ApiClient(restTemplate);
        CardApi cardApi = new CardApi(apiClient);
        cardApi.getApiClient().setBasePath(basePathPaymentService);
        return cardApi;
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
    public NotificationSubApi notificationSubApi() {
        jp.co.itechh.quad.notificationsub.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.notificationsub.presentation.ApiClient(restTemplate);
        NotificationSubApi notificationSubApi = new NotificationSubApi(apiClient);
        notificationSubApi.getApiClient().setBasePath(basePathUserService);
        return notificationSubApi;
    }
}