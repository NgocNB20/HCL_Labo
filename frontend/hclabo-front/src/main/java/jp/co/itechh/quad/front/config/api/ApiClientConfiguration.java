/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.config.api;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.authentication.presentation.api.AuthenticationApi;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.browsinghistory.presentation.api.BrowsingHistoryApi;
import jp.co.itechh.quad.card.presentation.api.CardApi;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.convenience.presentation.api.ConvenienceApi;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.freearea.presentation.api.FreeareaApi;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.web.HeaderParamsHelper;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquirygroup.presentation.api.InquiryGroupApi;
import jp.co.itechh.quad.inventory.presentation.api.InventoryApi;
import jp.co.itechh.quad.linkpay.presentation.api.LinkPayApi;
import jp.co.itechh.quad.mailcertification.presentation.api.MailCertificationApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.method.presentation.api.PaymentMethodApi;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.multiplecategory.presentation.api.MultipleCategoryApi;
import jp.co.itechh.quad.news.presentation.api.NewsApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.novelty.presentation.api.OrderNoveltyApi;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.pricecalculator.presentation.api.PriceCalculatorApi;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.relation.presentation.api.RelationApi;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shop.presentation.api.ShopApi;
import jp.co.itechh.quad.temp.presentation.api.TempApi;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.user.presentation.api.UsersApi;
import jp.co.itechh.quad.wishlist.presentation.api.WishlistApi;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import java.lang.reflect.Method;

/**
 * APIClientのDI登録クラス<br/>
 *
 * @author kimura
 */
@Configuration
public class ApiClientConfiguration {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientConfiguration.class);

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
    public InquiryApi inquiryApi() throws Exception {
        jp.co.itechh.quad.inquiry.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.inquiry.presentation.ApiClient(restTemplate);
        InquiryApi inquiryApi = new InquiryApi(apiClient);
        inquiryApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(inquiryApi.getApiClient());
        return inquiryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public InventoryApi inventoryApi() throws Exception {
        jp.co.itechh.quad.inventory.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.inventory.presentation.ApiClient(restTemplate);
        InventoryApi inventoryApi = new InventoryApi(apiClient);
        inventoryApi.getApiClient().setBasePath(basePathLogisticService);
        setMemberSeqToApiHeader(inventoryApi.getApiClient());
        return inventoryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ShippingMethodApi shippingMethodApi() throws Exception {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        ShippingMethodApi shippingMethodApi = new ShippingMethodApi(apiClient);
        shippingMethodApi.getApiClient().setBasePath(basePathLogisticService);
        setMemberSeqToApiHeader(shippingMethodApi.getApiClient());
        return shippingMethodApi;
    }

    @Bean
    @Primary
    @SessionScope
    public WishlistApi wishlistApi() throws Exception {
        jp.co.itechh.quad.wishlist.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.wishlist.presentation.ApiClient(restTemplate);
        WishlistApi wishlistApi = new WishlistApi(apiClient);
        wishlistApi.getApiClient().setBasePath(basePathPromotionService);
        setMemberSeqToApiHeader(wishlistApi.getApiClient());
        return wishlistApi;
    }

    @Bean
    @Primary
    @SessionScope
    public InquiryGroupApi inquiryGroupApi() throws Exception {
        jp.co.itechh.quad.inquirygroup.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.inquirygroup.presentation.ApiClient(restTemplate);
        InquiryGroupApi inquiryGroupApi = new InquiryGroupApi(apiClient);
        inquiryGroupApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(inquiryGroupApi.getApiClient());
        return inquiryGroupApi;
    }

    @Bean
    @Primary
    @SessionScope
    public CustomerApi customerApi() throws Exception {
        jp.co.itechh.quad.customer.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.customer.presentation.ApiClient(restTemplate);
        CustomerApi customerApi = new CustomerApi(apiClient);
        customerApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(customerApi.getApiClient());
        return customerApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AuthorizationApi AuthorizationApi() throws Exception {
        AuthorizationApi authorizationApi = new AuthorizationApi();
        authorizationApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(authorizationApi.getApiClient());
        return authorizationApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ProductApi productApi() throws Exception {
        jp.co.itechh.quad.product.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.product.presentation.ApiClient(restTemplate);
        ProductApi productApi = new ProductApi(apiClient);
        productApi.getApiClient().setBasePath(basePathProductService);
        setMemberSeqToApiHeader(productApi.getApiClient());
        return productApi;
    }

    @Bean
    @Primary
    @SessionScope
    public NewsApi newsApi() throws Exception {
        jp.co.itechh.quad.news.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.news.presentation.ApiClient(restTemplate);
        NewsApi newsApi = new NewsApi(apiClient);
        newsApi.getApiClient().setBasePath(basePathShopManagement);
        setMemberSeqToApiHeader(newsApi.getApiClient());
        return newsApi;
    }

    @Bean
    @Primary
    @SessionScope
    public CategoryApi categoryApi() throws Exception {
        jp.co.itechh.quad.category.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.category.presentation.ApiClient(restTemplate);
        CategoryApi categoryApi = new CategoryApi(apiClient);
        categoryApi.getApiClient().setBasePath(basePathProductService);
        setMemberSeqToApiHeader(categoryApi.getApiClient());
        return categoryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public FreeareaApi freeareaApi() throws Exception {
        jp.co.itechh.quad.freearea.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.freearea.presentation.ApiClient(restTemplate);
        FreeareaApi freeareaApi = new FreeareaApi(apiClient);
        freeareaApi.getApiClient().setBasePath(basePathPromotionService);
        setMemberSeqToApiHeader(freeareaApi.getApiClient());
        return freeareaApi;
    }

    @Bean
    @Primary
    @SessionScope
    public MultipleCategoryApi multipleCategoryApi() throws Exception {
        jp.co.itechh.quad.multiplecategory.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.multiplecategory.presentation.ApiClient(restTemplate);
        MultipleCategoryApi multipleCategoryApi = new MultipleCategoryApi(apiClient);
        multipleCategoryApi.getApiClient().setBasePath(basePathProductService);
        setMemberSeqToApiHeader(multipleCategoryApi.getApiClient());
        return multipleCategoryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public MailCertificationApi mailCertificationApi() throws Exception {
        jp.co.itechh.quad.mailcertification.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mailcertification.presentation.ApiClient(restTemplate);
        MailCertificationApi mailCertificationApi = new MailCertificationApi(apiClient);
        mailCertificationApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(mailCertificationApi.getApiClient());
        return mailCertificationApi;
    }

    @Bean
    @Primary
    @SessionScope
    public MailMagazineApi mailMagazineApi() throws Exception {
        jp.co.itechh.quad.mailmagazine.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mailmagazine.presentation.ApiClient(restTemplate);
        MailMagazineApi mailMagazineApi = new MailMagazineApi(apiClient);
        mailMagazineApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(mailMagazineApi.getApiClient());
        return mailMagazineApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ZipcodeApi zipcodeApi() throws Exception {
        jp.co.itechh.quad.zipcode.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.zipcode.presentation.ApiClient(restTemplate);
        ZipcodeApi zipcodeApi = new ZipcodeApi(apiClient);
        zipcodeApi.getApiClient().setBasePath(basePathLogisticService);
        setMemberSeqToApiHeader(zipcodeApi.getApiClient());
        return zipcodeApi;
    }

    @Bean
    @Primary
    @SessionScope
    public BrowsingHistoryApi browsinghistoryApi() throws Exception {
        jp.co.itechh.quad.browsinghistory.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.browsinghistory.presentation.ApiClient(restTemplate);
        BrowsingHistoryApi browsinghistoryApi = new BrowsingHistoryApi(apiClient);
        browsinghistoryApi.getApiClient().setBasePath(basePathPromotionService);
        setMemberSeqToApiHeader(browsinghistoryApi.getApiClient());
        return browsinghistoryApi;
    }

    @Bean
    @Primary
    @SessionScope
    public RelationApi relationApi() throws Exception {
        jp.co.itechh.quad.relation.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.relation.presentation.ApiClient(restTemplate);
        RelationApi relationApi = new RelationApi(apiClient);
        relationApi.getApiClient().setBasePath(basePathProductService);
        setMemberSeqToApiHeader(relationApi.getApiClient());
        return relationApi;
    }

    @Bean
    @Primary
    @SessionScope
    public UsersApi usersApi() throws Exception {
        jp.co.itechh.quad.user.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.user.presentation.ApiClient(restTemplate);
        UsersApi usersApi = new UsersApi(apiClient);
        usersApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(usersApi.getApiClient());
        return usersApi;
    }

    @Bean
    @Primary
    @SessionScope
    public TempApi tempApi() throws Exception {
        jp.co.itechh.quad.temp.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.temp.presentation.ApiClient(restTemplate);
        TempApi tempApi = new TempApi(apiClient);
        tempApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(tempApi.getApiClient());
        return tempApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ShopApi shopApi() throws Exception {
        jp.co.itechh.quad.shop.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.shop.presentation.ApiClient(restTemplate);
        ShopApi shopApi = new ShopApi(apiClient);
        shopApi.getApiClient().setBasePath(basePathShopManagement);
        setMemberSeqToApiHeader(shopApi.getApiClient());
        return shopApi;
    }

    @Bean
    @Primary
    @SessionScope
    public MulpayApi mulpayApi() throws Exception {
        jp.co.itechh.quad.mulpay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mulpay.presentation.ApiClient(restTemplate);
        MulpayApi mulpayApi = new MulpayApi(apiClient);
        mulpayApi.getApiClient().setBasePath(basePathPaymentService);
        setMemberSeqToApiHeader(mulpayApi.getApiClient());
        return mulpayApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AddressBookApi addressBookApi() throws Exception {
        jp.co.itechh.quad.addressbook.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.addressbook.presentation.ApiClient(restTemplate);
        AddressBookApi addressBookApi = new AddressBookApi(apiClient);
        addressBookApi.getApiClient().setBasePath(basePathLogisticService);
        setMemberSeqToApiHeader(addressBookApi.getApiClient());
        return addressBookApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ShippingSlipApi shippingSlipApi() throws Exception {
        jp.co.itechh.quad.shippingslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.shippingslip.presentation.ApiClient(restTemplate);
        ShippingSlipApi shippingSlipApi = new ShippingSlipApi(apiClient);
        shippingSlipApi.getApiClient().setBasePath(basePathLogisticService);
        setMemberSeqToApiHeader(shippingSlipApi.getApiClient());
        return shippingSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public SalesSlipApi salesSlipApi() throws Exception {
        jp.co.itechh.quad.salesslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.salesslip.presentation.ApiClient(restTemplate);
        SalesSlipApi salesSlipApi = new SalesSlipApi(apiClient);
        salesSlipApi.getApiClient().setBasePath(basePathPricePlanningService);
        setMemberSeqToApiHeader(salesSlipApi.getApiClient());
        return salesSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public CouponApi couponApi() throws Exception {
        jp.co.itechh.quad.coupon.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.coupon.presentation.ApiClient(restTemplate);
        CouponApi couponApi = new CouponApi(apiClient);
        couponApi.getApiClient().setBasePath(basePathPricePlanningService);
        setMemberSeqToApiHeader(couponApi.getApiClient());
        return couponApi;
    }

    @Bean
    @Primary
    @SessionScope
    public BillingSlipApi billingSlipApi() throws Exception {
        jp.co.itechh.quad.billingslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.billingslip.presentation.ApiClient(restTemplate);
        BillingSlipApi billingSlipApi = new BillingSlipApi(apiClient);
        billingSlipApi.getApiClient().setBasePath(basePathPaymentService);
        setMemberSeqToApiHeader(billingSlipApi.getApiClient());
        return billingSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OrderSlipApi orderSlipApi() throws Exception {
        jp.co.itechh.quad.orderslip.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.orderslip.presentation.ApiClient(restTemplate);
        OrderSlipApi orderSlipApi = new OrderSlipApi(apiClient);
        orderSlipApi.getApiClient().setBasePath(basePathPromotionService);
        setMemberSeqToApiHeader(orderSlipApi.getApiClient());
        return orderSlipApi;
    }

    @Bean
    @Primary
    @SessionScope
    public PriceCalculatorApi priceCalculatorApi() throws Exception {
        jp.co.itechh.quad.pricecalculator.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.pricecalculator.presentation.ApiClient(restTemplate);
        PriceCalculatorApi priceCalculatorApi = new PriceCalculatorApi(apiClient);
        priceCalculatorApi.getApiClient().setBasePath(basePathPricePlanningService);
        setMemberSeqToApiHeader(priceCalculatorApi.getApiClient());
        return priceCalculatorApi;
    }

    @Bean
    @Primary
    @SessionScope
    public TransactionApi transactionApi() throws Exception {
        jp.co.itechh.quad.transaction.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.transaction.presentation.ApiClient(restTemplate);
        TransactionApi transactionApi = new TransactionApi(apiClient);
        transactionApi.getApiClient().setBasePath(basePathOrderService);
        setMemberSeqToApiHeader(transactionApi.getApiClient());
        return transactionApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OrderReceivedApi orderReceivedApi() throws Exception {
        jp.co.itechh.quad.orderreceived.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.orderreceived.presentation.ApiClient(restTemplate);
        OrderReceivedApi orderReceivedApi = new OrderReceivedApi(apiClient);
        orderReceivedApi.getApiClient().setBasePath(basePathOrderService);
        setMemberSeqToApiHeader(orderReceivedApi.getApiClient());
        return orderReceivedApi;
    }

    @Bean
    @Primary
    @SessionScope
    public PaymentMethodApi paymentMethodApi() throws Exception {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        PaymentMethodApi paymentMethodApi = new PaymentMethodApi(apiClient);
        paymentMethodApi.getApiClient().setBasePath(basePathPaymentService);
        setMemberSeqToApiHeader(paymentMethodApi.getApiClient());
        return paymentMethodApi;
    }

    @Bean
    @Primary
    @SessionScope
    public CardApi cardApi() throws Exception {
        jp.co.itechh.quad.card.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.card.presentation.ApiClient(restTemplate);
        CardApi cardApi = new CardApi(apiClient);
        cardApi.getApiClient().setBasePath(basePathPaymentService);
        setMemberSeqToApiHeader(cardApi.getApiClient());
        return cardApi;
    }

    @Bean
    @Primary
    @SessionScope
    public ConvenienceApi convenienceApi() throws Exception {
        jp.co.itechh.quad.convenience.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.convenience.presentation.ApiClient(restTemplate);
        ConvenienceApi convenienceApi = new ConvenienceApi(apiClient);
        convenienceApi.getApiClient().setBasePath(basePathPaymentService);
        setMemberSeqToApiHeader(convenienceApi.getApiClient());
        return convenienceApi;
    }

    @Bean
    @Primary
    @SessionScope
    public SettlementMethodApi settlementMethodApi() throws Exception {
        jp.co.itechh.quad.method.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.method.presentation.ApiClient(restTemplate);
        SettlementMethodApi settlementMethodApi = new SettlementMethodApi(apiClient);
        settlementMethodApi.getApiClient().setBasePath(basePathPaymentService);
        setMemberSeqToApiHeader(settlementMethodApi.getApiClient());
        return settlementMethodApi;
    }

    @Bean
    @Primary
    @SessionScope
    public LinkPayApi linkPayApi() throws Exception {
        jp.co.itechh.quad.linkpay.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.linkpay.presentation.ApiClient(restTemplate);
        LinkPayApi linkPayApi = new LinkPayApi(apiClient);
        linkPayApi.getApiClient().setBasePath(basePathPaymentService);
        setMemberSeqToApiHeader(linkPayApi.getApiClient());
        return linkPayApi;
    }

    @Bean
    @Primary
    @SessionScope
    public AuthenticationApi authenticationApi() throws Exception {
        jp.co.itechh.quad.authentication.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.authentication.presentation.ApiClient(restTemplate);
        AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);
        authenticationApi.getApiClient().setBasePath(basePathUserService);
        setMemberSeqToApiHeader(authenticationApi.getApiClient());
        return authenticationApi;
    }

    @Bean
    @Primary
    @SessionScope
    public OrderNoveltyApi orderNoveltyApi() throws Exception {
        jp.co.itechh.quad.novelty.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.novelty.presentation.ApiClient(restTemplate);
        OrderNoveltyApi orderNoveltyApi = new OrderNoveltyApi(apiClient);
        orderNoveltyApi.getApiClient().setBasePath(basePathOrderService);
        setMemberSeqToApiHeader(orderNoveltyApi.getApiClient());
        return orderNoveltyApi;
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
    public ExaminationApi examinationApi() {
        jp.co.itechh.quad.examination.presentation.ApiClient apiClient =
            new jp.co.itechh.quad.examination.presentation.ApiClient(restTemplate);
        ExaminationApi examinationApi = new ExaminationApi(apiClient);
        examinationApi.getApiClient().setBasePath(basePathCustomizeService);
        return examinationApi;
    }

    /**
     * API認証ヘッダーへ顧客IDを設定
     * ※セッション切れを考慮
     *
     * @param apiClient
     */
    private void setMemberSeqToApiHeader(Object apiClient) throws Exception {

        // ヘッダーの顧客ID設定（セッション切れ等）
        String customerId = HmFrontUserDetailsServiceImpl.getCustomerIdFromCookie();
        if (!StringUtils.isBlank(customerId)) {
            try {
                // 認証取得
                Method getAuthentication = apiClient.getClass()
                                                    .getDeclaredMethod(HeaderParamsHelper.METHOD_GET_AUTHENTICATION,
                                                                       String.class
                                                                      );
                Object apiKeyAuth = getAuthentication.invoke(apiClient, HeaderParamsHelper.API_KEY_AUTHENTICATION);

                // APIキー設定
                Method setApiKey = apiKeyAuth.getClass()
                                             .getDeclaredMethod(HeaderParamsHelper.METHOD_SET_API_KEY, String.class);
                setApiKey.invoke(apiKeyAuth, customerId);

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new Exception(e.getMessage(), e);
            }
        }
    }
}