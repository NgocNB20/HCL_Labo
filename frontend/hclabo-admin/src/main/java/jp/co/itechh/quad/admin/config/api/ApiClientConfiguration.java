/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.config.api;

import jp.co.itechh.quad.accesskeywords.presentation.api.AccessKeywordsApi;
import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.authentication.presentation.api.AuthenticationApi;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.batchmanagement.presentation.api.BatchManagementApi;
import jp.co.itechh.quad.bill.presentation.api.BillApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.convenience.presentation.api.ConvenienceApi;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.freearea.presentation.api.FreeareaApi;
import jp.co.itechh.quad.holiday.presentation.api.HolidayApi;
import jp.co.itechh.quad.icon.presentation.api.IconApi;
import jp.co.itechh.quad.impossiblearea.presentation.api.ImpossibleAreaApi;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquirygroup.presentation.api.InquiryGroupApi;
import jp.co.itechh.quad.inventory.presentation.api.InventoryApi;
import jp.co.itechh.quad.linkpay.presentation.api.LinkPayApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.menu.presentation.api.MenuApi;
import jp.co.itechh.quad.method.presentation.api.PaymentMethodApi;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.news.presentation.api.NewsApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.novelty.presentation.api.LogisticNoveltyApi;
import jp.co.itechh.quad.novelty.presentation.api.OrderNoveltyApi;
import jp.co.itechh.quad.officezipcodeupdate.presentation.api.OfficezipcodeUpdateApi;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.productnovelty.presentation.api.ProductNoveltyApi;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.ReceiverImpossibleDateApi;
import jp.co.itechh.quad.registasynchronous.presentation.api.RegistAsynchronousApi;
import jp.co.itechh.quad.relation.presentation.api.RelationApi;
import jp.co.itechh.quad.reports.presentation.api.ReportsApi;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shop.presentation.api.ShopApi;
import jp.co.itechh.quad.specialchargearea.presentation.api.SpecialChargeAreaApi;
import jp.co.itechh.quad.stockdisplay.presentation.api.StockDisplayApi;
import jp.co.itechh.quad.tag.presentation.api.TagApi;
import jp.co.itechh.quad.tax.presentation.api.TaxApi;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import jp.co.itechh.quad.zipcodeupdate.presentation.api.ZipcodeUpdateApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

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

    @Value("${base-path-shop-management-service}")
    private String basePathShopManagement;

    @Value("${base-path-customize-service}")
    private String basePathCustomizeService;

    private RestTemplate restTemplate = ApplicationContextUtility.getBean(RestTemplate.class);

    @Bean
    @Primary
    @SessionScope
    public CustomerApi customerApi() {
        jp.co.itechh.quad.customer.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.customer.presentation.ApiClient(restTemplate);
        CustomerApi customerApi = new CustomerApi(apiClient);
        customerApi.getApiClient().setBasePath(basePathUserService);
        return customerApi;
    }

    @Bean
    @Primary
    @SessionScope
    public InquiryApi inquiryApi() {
        jp.co.itechh.quad.inquiry.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.inquiry.presentation.ApiClient(restTemplate);
        InquiryApi inquiryApi = new InquiryApi(apiClient);
        inquiryApi.getApiClient().setBasePath(basePathUserService);
        return inquiryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public InquiryGroupApi inquiryGroupApi() {
        jp.co.itechh.quad.inquirygroup.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.inquirygroup.presentation.ApiClient(restTemplate);
        InquiryGroupApi inquiryGroupApi = new InquiryGroupApi(apiClient);
        inquiryGroupApi.getApiClient().setBasePath(basePathUserService);
        return inquiryGroupApi;
    }

    @Bean
    @Primary
    @SessionScope
    public IconApi iconApi() {
        jp.co.itechh.quad.icon.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.icon.presentation.ApiClient(restTemplate);
        IconApi iconApi = new IconApi(apiClient);
        iconApi.getApiClient().setBasePath(basePathProductService);
        return iconApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ProductApi productApi() {
        jp.co.itechh.quad.product.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.product.presentation.ApiClient(restTemplate);
        ProductApi productApi = new ProductApi(apiClient);
        productApi.getApiClient().setBasePath(basePathProductService);
        return productApi;
    }

    @Bean
    @Primary
    @SessionScope
    public TaxApi taxApi() {
        jp.co.itechh.quad.tax.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.tax.presentation.ApiClient(restTemplate);
        TaxApi taxApi = new TaxApi(apiClient);
        taxApi.getApiClient().setBasePath(basePathProductService);
        return taxApi;
    }

    @Bean
    @Primary
    @SessionScope
    public RelationApi relationApi() {
        jp.co.itechh.quad.relation.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.relation.presentation.ApiClient(restTemplate);
        RelationApi relationApi = new RelationApi(apiClient);
        relationApi.getApiClient().setBasePath(basePathProductService);
        return relationApi;
    }

    @Bean
    @Primary
    @SessionScope
    public CategoryApi categoryApi() {
        jp.co.itechh.quad.category.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.category.presentation.ApiClient(restTemplate);
        CategoryApi categoryApi = new CategoryApi(apiClient);
        categoryApi.getApiClient().setBasePath(basePathProductService);
        return categoryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public InventoryApi inventoryApi() {
        jp.co.itechh.quad.inventory.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.inventory.presentation.ApiClient(restTemplate);
        InventoryApi inventoryApi = new InventoryApi(apiClient);
        inventoryApi.getApiClient().setBasePath(basePathLogisticService);
        return inventoryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public SpecialChargeAreaApi specialChargeAreaApi() {
        jp.co.itechh.quad.specialchargearea.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.specialchargearea.presentation.ApiClient(restTemplate);
        SpecialChargeAreaApi specialChargeAreaApi = new SpecialChargeAreaApi(apiClient);
        specialChargeAreaApi.getApiClient().setBasePath(basePathLogisticService);
        return specialChargeAreaApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ZipcodeApi zipcodeApi() {
        jp.co.itechh.quad.zipcode.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.zipcode.presentation.ApiClient(restTemplate);
        ZipcodeApi zipcodeApi = new ZipcodeApi(apiClient);
        zipcodeApi.getApiClient().setBasePath(basePathLogisticService);
        return zipcodeApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ImpossibleAreaApi impossibleAreaApi() {
        jp.co.itechh.quad.impossiblearea.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.impossiblearea.presentation.ApiClient(restTemplate);
        ImpossibleAreaApi impossibleAreaApi = new ImpossibleAreaApi(apiClient);
        impossibleAreaApi.getApiClient().setBasePath(basePathLogisticService);
        return impossibleAreaApi;
    }

    @Bean
    @Primary
    @SessionScope
    public FreeareaApi freeareaApi() {
        jp.co.itechh.quad.freearea.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.freearea.presentation.ApiClient(restTemplate);
        FreeareaApi freeareaApi = new FreeareaApi(apiClient);
        freeareaApi.getApiClient().setBasePath(basePathPromotionService);
        return freeareaApi;
    }

    @Bean
    @Primary
    @SessionScope
    public MailMagazineApi mailMagazineApi() {
        jp.co.itechh.quad.mailmagazine.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mailmagazine.presentation.ApiClient(restTemplate);
        MailMagazineApi mailMagazineApi = new MailMagazineApi(apiClient);
        mailMagazineApi.getApiClient().setBasePath(basePathUserService);
        return mailMagazineApi;
    }

    @Bean
    @Primary
    @SessionScope
    public NewsApi newsApi() {
        jp.co.itechh.quad.news.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.news.presentation.ApiClient(restTemplate);
        NewsApi newsApi = new NewsApi(apiClient);
        newsApi.getApiClient().setBasePath(basePathShopManagement);
        return newsApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ShopApi shopApi() {
        jp.co.itechh.quad.shop.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.shop.presentation.ApiClient(restTemplate);
        ShopApi shopApi = new ShopApi(apiClient);
        shopApi.getApiClient().setBasePath(basePathShopManagement);
        return shopApi;
    }

    @Bean
    @Primary
    @SessionScope
    public RegistAsynchronousApi registAsynchronousApi() {
        jp.co.itechh.quad.registasynchronous.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.registasynchronous.presentation.ApiClient(restTemplate);
        RegistAsynchronousApi registAsynchronousApi = new RegistAsynchronousApi(apiClient);
        registAsynchronousApi.getApiClient().setBasePath(basePathProductService);
        return registAsynchronousApi;
    }

    @Bean
    @Primary
    @SessionScope
    public SettlementMethodApi settlementMethodApi() {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        SettlementMethodApi settlementMethodApi = new SettlementMethodApi(apiClient);
        settlementMethodApi.getApiClient().setBasePath(basePathPaymentService);
        return settlementMethodApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ConvenienceApi convenienceApi() {
        jp.co.itechh.quad.convenience.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.convenience.presentation.ApiClient(restTemplate);
        ConvenienceApi convenienceApi = new ConvenienceApi(apiClient);
        convenienceApi.getApiClient().setBasePath(basePathPaymentService);
        return convenienceApi;
    }

    @Bean
    @Primary
    @SessionScope
    public HolidayApi holidayApi() {
        jp.co.itechh.quad.holiday.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.holiday.presentation.ApiClient(restTemplate);
        HolidayApi holidayApi = new HolidayApi(apiClient);
        holidayApi.getApiClient().setBasePath(basePathLogisticService);
        return holidayApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ReceiverImpossibleDateApi receiverImpossibleDateApi() {
        jp.co.itechh.quad.receiverimpossibledate.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.receiverimpossibledate.presentation.ApiClient(restTemplate);
        ReceiverImpossibleDateApi receiverImpossibleDateApi = new ReceiverImpossibleDateApi(apiClient);
        receiverImpossibleDateApi.getApiClient().setBasePath(basePathLogisticService);
        return receiverImpossibleDateApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ShippingMethodApi shippingMethodApi() {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        ShippingMethodApi shippingMethodApi = new ShippingMethodApi(apiClient);
        shippingMethodApi.getApiClient().setBasePath(basePathLogisticService);
        return shippingMethodApi;
    }

    @Bean
    @Primary
    @SessionScope
    public LogisticNoveltyApi logisticNoveltyApi() {
        jp.co.itechh.quad.novelty.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.novelty.presentation.ApiClient(restTemplate);
        LogisticNoveltyApi logisticNoveltyApi = new LogisticNoveltyApi(apiClient);
        logisticNoveltyApi.getApiClient().setBasePath(basePathLogisticService);
        return logisticNoveltyApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AddressBookApi addressBookApi() {
        jp.co.itechh.quad.addressbook.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.addressbook.presentation.ApiClient(restTemplate);
        AddressBookApi addressBookApi = new AddressBookApi(apiClient);
        addressBookApi.getApiClient().setBasePath(basePathLogisticService);
        return addressBookApi;
    }

    @Bean
    @Primary
    @SessionScope
    public CouponApi couponApi() {
        jp.co.itechh.quad.coupon.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.coupon.presentation.ApiClient(restTemplate);
        CouponApi couponApi = new CouponApi(apiClient);
        couponApi.getApiClient().setBasePath(basePathPricePlanningService);
        return couponApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AuthorizationApi authorizationApi() {
        jp.co.itechh.quad.authorization.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.authorization.presentation.ApiClient(restTemplate);
        AuthorizationApi authorizationApi = new AuthorizationApi(apiClient);
        authorizationApi.getApiClient().setBasePath(basePathUserService);
        return authorizationApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AdministratorApi administratorApi() {
        jp.co.itechh.quad.administrator.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.administrator.presentation.ApiClient(restTemplate);
        AdministratorApi administratorApi = new AdministratorApi(apiClient);
        administratorApi.getApiClient().setBasePath(basePathUserService);
        return administratorApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AccessKeywordsApi accessKeywordsApi() {
        jp.co.itechh.quad.accesskeywords.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.accesskeywords.presentation.ApiClient(restTemplate);
        AccessKeywordsApi accessKeywordsApi = new AccessKeywordsApi(apiClient);
        accessKeywordsApi.getApiClient().setBasePath(basePathAnalyticsService);
        return accessKeywordsApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ReportsApi reportsApi() {
        jp.co.itechh.quad.reports.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.reports.presentation.ApiClient(restTemplate);
        ReportsApi reportsApi = new ReportsApi(apiClient);
        reportsApi.getApiClient().setBasePath(basePathAnalyticsService);
        return reportsApi;
    }

    @Bean
    @Primary
    @SessionScope
    public BatchManagementApi batchManagementApi() {
        jp.co.itechh.quad.batchmanagement.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.batchmanagement.presentation.ApiClient(restTemplate);
        BatchManagementApi batchManagementApi = new BatchManagementApi(apiClient);
        batchManagementApi.getApiClient().setBasePath(basePathAnalyticsService);
        return batchManagementApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ZipcodeUpdateApi zipcodeUpdateApi() {
        jp.co.itechh.quad.zipcodeupdate.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.zipcodeupdate.presentation.ApiClient(restTemplate);
        ZipcodeUpdateApi zipcodeUpdateApi = new ZipcodeUpdateApi(apiClient);
        zipcodeUpdateApi.getApiClient().setBasePath(basePathLogisticService);
        return zipcodeUpdateApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OfficezipcodeUpdateApi officezipcodeUpdateApi() {
        jp.co.itechh.quad.officezipcodeupdate.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.officezipcodeupdate.presentation.ApiClient(restTemplate);
        OfficezipcodeUpdateApi officezipcodeUpdateApi = new OfficezipcodeUpdateApi(apiClient);
        officezipcodeUpdateApi.getApiClient().setBasePath(basePathLogisticService);
        return officezipcodeUpdateApi;
    }

    @Bean
    @Primary
    @SessionScope
    public StockDisplayApi stockDisplayApi() {
        jp.co.itechh.quad.stockdisplay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.stockdisplay.presentation.ApiClient(restTemplate);
        StockDisplayApi stockDisplayApi = new StockDisplayApi(apiClient);
        stockDisplayApi.getApiClient().setBasePath(basePathProductService);
        return stockDisplayApi;
    }

    @Bean
    @Primary
    @SessionScope
    public NotificationSubApi notificationSubApi() {
        jp.co.itechh.quad.notificationsub.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.notificationsub.presentation.ApiClient(restTemplate);
        NotificationSubApi notificationSubApi = new NotificationSubApi(apiClient);
        notificationSubApi.getApiClient().setBasePath(basePathUserService);
        return notificationSubApi;
    }

    @Bean
    @Primary
    @SessionScope
    public MulpayApi mulpayApi() {
        jp.co.itechh.quad.mulpay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mulpay.presentation.ApiClient(restTemplate);
        MulpayApi mulpayApi = new MulpayApi(apiClient);
        mulpayApi.getApiClient().setBasePath(basePathPaymentService);
        return mulpayApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ShippingSlipApi shippingSlipApi() {
        jp.co.itechh.quad.shippingslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.shippingslip.presentation.ApiClient(restTemplate);
        ShippingSlipApi shippingSlipApi = new ShippingSlipApi(apiClient);
        shippingSlipApi.getApiClient().setBasePath(basePathLogisticService);
        return shippingSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public SalesSlipApi salesSlipApi() {
        jp.co.itechh.quad.salesslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.salesslip.presentation.ApiClient(restTemplate);
        SalesSlipApi salesSlipApi = new SalesSlipApi(apiClient);
        salesSlipApi.getApiClient().setBasePath(basePathPricePlanningService);
        return salesSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public BillingSlipApi billingSlipApi() {
        jp.co.itechh.quad.billingslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.billingslip.presentation.ApiClient(restTemplate);
        BillingSlipApi billingSlipApi = new BillingSlipApi(apiClient);
        billingSlipApi.getApiClient().setBasePath(basePathPaymentService);
        return billingSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OrderSlipApi orderSlipApi() {
        jp.co.itechh.quad.orderslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.orderslip.presentation.ApiClient(restTemplate);
        OrderSlipApi orderSlipApi = new OrderSlipApi(apiClient);
        orderSlipApi.getApiClient().setBasePath(basePathPromotionService);
        return orderSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OrderReceivedApi orderReceivedApi() {
        jp.co.itechh.quad.orderreceived.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.orderreceived.presentation.ApiClient(restTemplate);
        OrderReceivedApi orderReceivedApi = new OrderReceivedApi(apiClient);
        orderReceivedApi.getApiClient().setBasePath(basePathOrderService);
        return orderReceivedApi;
    }

    @Bean
    @Primary
    @SessionScope
    public PaymentMethodApi paymentMethodApi() {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        PaymentMethodApi paymentMethodApi = new PaymentMethodApi(apiClient);
        paymentMethodApi.getApiClient().setBasePath(basePathPaymentService);
        return paymentMethodApi;
    }

    @Bean
    @Primary
    @SessionScope
    public BillApi billApi() {
        jp.co.itechh.quad.bill.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.bill.presentation.ApiClient(restTemplate);
        BillApi billApi = new BillApi(apiClient);
        billApi.getApiClient().setBasePath(basePathPaymentService);
        return billApi;
    }

    @Bean
    @Primary
    @SessionScope
    public TransactionApi transactionApi() {
        jp.co.itechh.quad.transaction.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.transaction.presentation.ApiClient(restTemplate);
        TransactionApi transactionApi = new TransactionApi(apiClient);
        transactionApi.getApiClient().setBasePath(basePathOrderService);
        return transactionApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OrderSearchApi orderSearchApi() {
        jp.co.itechh.quad.ordersearch.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.ordersearch.presentation.ApiClient(restTemplate);
        OrderSearchApi orderSearchApi = new OrderSearchApi(apiClient);
        orderSearchApi.getApiClient().setBasePath(basePathAnalyticsService);
        return orderSearchApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ProductNoveltyApi productsNoveltyApi() {
        jp.co.itechh.quad.productnovelty.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.productnovelty.presentation.ApiClient(restTemplate);
        ProductNoveltyApi productsNoveltyApi = new ProductNoveltyApi(apiClient);
        productsNoveltyApi.getApiClient().setBasePath(basePathProductService);
        return productsNoveltyApi;
    }

    @Bean
    @Primary
    @SessionScope
    public LinkPayApi linkPayApi() {
        jp.co.itechh.quad.linkpay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.linkpay.presentation.ApiClient(restTemplate);
        LinkPayApi linkPayApi = new LinkPayApi(apiClient);
        linkPayApi.getApiClient().setBasePath(basePathPaymentService);
        return linkPayApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AuthenticationApi authenticationApi() {
        jp.co.itechh.quad.authentication.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.authentication.presentation.ApiClient(restTemplate);
        AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);
        authenticationApi.getApiClient().setBasePath(basePathUserService);
        return authenticationApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OrderNoveltyApi orderNoveltyApi() {
        jp.co.itechh.quad.novelty.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.novelty.presentation.ApiClient(restTemplate);
        OrderNoveltyApi orderNoveltyApi = new OrderNoveltyApi(apiClient);
        orderNoveltyApi.getApiClient().setBasePath(basePathOrderService);
        return orderNoveltyApi;
    }

    @Bean
    @Primary
    @SessionScope
    public MenuApi menuApi() {
        jp.co.itechh.quad.menu.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.menu.presentation.ApiClient(restTemplate);
        MenuApi menuApi = new MenuApi(apiClient);
        menuApi.getApiClient().setBasePath(basePathProductService);
        return menuApi;
    }

    @Bean
    @Primary
    @SessionScope
    public TagApi tagApi() {
        jp.co.itechh.quad.tag.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.tag.presentation.ApiClient(restTemplate);
        TagApi tagApi = new TagApi(apiClient);
        tagApi.getApiClient().setBasePath(basePathProductService);
        return tagApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ExaminationApi examinationApi() {
        jp.co.itechh.quad.examination.presentation.ApiClient apiClient =
            new jp.co.itechh.quad.examination.presentation.ApiClient(restTemplate);
        ExaminationApi examinationApi = new ExaminationApi(apiClient);
        examinationApi.getApiClient().setBasePath(basePathCustomizeService);
        return examinationApi;
    }

}