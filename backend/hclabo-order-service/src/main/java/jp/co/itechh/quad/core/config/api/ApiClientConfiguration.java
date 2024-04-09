/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.config.api;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.clearlogistic.presentation.api.LogisticClearApi;
import jp.co.itechh.quad.clearpayment.presentation.api.PaymentClearApi;
import jp.co.itechh.quad.clearpriceplanning.presentation.api.PricePlanningClearApi;
import jp.co.itechh.quad.clearpromotion.presentation.api.PromotionClearApi;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.linkpay.presentation.api.LinkPayApi;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.novelty.presentation.api.LogisticNoveltyApi;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.reports.presentation.api.ReportsApi;
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

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-price-planning-service}")
    private String basePathPricePlanningService;

    @Value("${base-path-payment-service}")
    private String basePathPaymentService;

    @Value("${base-path-logistic-service}")
    private String basePathLogisticService;

    @Value("${base-path-promotion-service}")
    private String basePathPromotionService;

    @Value("${base-path-analytics-service}")
    private String basePathAnalyticsService;

    @Value("${base-path-order-service}")
    private String basePathOrderService;

    @Value("${base-path-customize-service}")
    private String basePathCustomizeService;

    private RestTemplate restTemplate = ApplicationContextUtility.getBean(RestTemplate.class);

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
    public MulpayApi mulPayApi() {
        jp.co.itechh.quad.mulpay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mulpay.presentation.ApiClient(restTemplate);
        MulpayApi mulPayApi = new MulpayApi(apiClient);
        mulPayApi.getApiClient().setBasePath(basePathPaymentService);
        return mulPayApi;
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
    public NotificationSubApi notificationSubApi() {
        jp.co.itechh.quad.notificationsub.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.notificationsub.presentation.ApiClient(restTemplate);
        NotificationSubApi notificationSubApi = new NotificationSubApi(apiClient);
        notificationSubApi.getApiClient().setBasePath(basePathUserService);
        return notificationSubApi;
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
    public OrderSearchApi orderSearchApi() {
        jp.co.itechh.quad.ordersearch.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.ordersearch.presentation.ApiClient(restTemplate);
        OrderSearchApi orderSearchApi = new OrderSearchApi(apiClient);
        orderSearchApi.getApiClient().setBasePath(basePathAnalyticsService);
        return orderSearchApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public ReportsApi reportsApi() {
        jp.co.itechh.quad.reports.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.reports.presentation.ApiClient(restTemplate);
        ReportsApi reportsApi = new ReportsApi(apiClient);
        reportsApi.getApiClient().setBasePath(basePathAnalyticsService);
        return reportsApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public LinkPayApi linkPayApi() {
        jp.co.itechh.quad.linkpay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.linkpay.presentation.ApiClient(restTemplate);
        LinkPayApi linkPayApi = new LinkPayApi(apiClient);
        linkPayApi.getApiClient().setBasePath(basePathPaymentService);
        return linkPayApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public AdministratorApi administratorApi() {
        jp.co.itechh.quad.administrator.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.administrator.presentation.ApiClient(restTemplate);
        AdministratorApi administratorApi = new AdministratorApi(apiClient);
        administratorApi.getApiClient().setBasePath(basePathUserService);
        return administratorApi;
    }

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
    public LogisticNoveltyApi logisticNoveltyApi() {
        jp.co.itechh.quad.novelty.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.novelty.presentation.ApiClient(restTemplate);
        LogisticNoveltyApi logisticNoveltyApi = new LogisticNoveltyApi(apiClient);
        logisticNoveltyApi.getApiClient().setBasePath(basePathLogisticService);
        return logisticNoveltyApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public LogisticClearApi logisticClearApi() {
        jp.co.itechh.quad.clearlogistic.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.clearlogistic.presentation.ApiClient(restTemplate);
        LogisticClearApi logisticClearApi = new LogisticClearApi(apiClient);
        logisticClearApi.getApiClient().setBasePath(basePathLogisticService);
        return logisticClearApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public PaymentClearApi paymentClearApi() {
        jp.co.itechh.quad.clearpayment.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.clearpayment.presentation.ApiClient(restTemplate);
        PaymentClearApi paymentClearApi = new PaymentClearApi(apiClient);
        paymentClearApi.getApiClient().setBasePath(basePathPaymentService);
        return paymentClearApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public PricePlanningClearApi pricePlanningClearApi() {
        jp.co.itechh.quad.clearpriceplanning.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.clearpriceplanning.presentation.ApiClient(restTemplate);
        PricePlanningClearApi pricePlanningClearApi = new PricePlanningClearApi(apiClient);
        pricePlanningClearApi.getApiClient().setBasePath(basePathPricePlanningService);
        return pricePlanningClearApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public PromotionClearApi promotionClearApi() {
        jp.co.itechh.quad.clearpromotion.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.clearpromotion.presentation.ApiClient(restTemplate);
        PromotionClearApi promotionClearApi = new PromotionClearApi(apiClient);
        promotionClearApi.getApiClient().setBasePath(basePathPromotionService);
        return promotionClearApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public ExaminationApi examinationApi() {
        jp.co.itechh.quad.examination.presentation.ApiClient apiClient =
            new jp.co.itechh.quad.examination.presentation.ApiClient(restTemplate);
        ExaminationApi examinationApi = new ExaminationApi(apiClient);
        examinationApi.getApiClient().setBasePath(basePathCustomizeService);
        return examinationApi;
    }

}