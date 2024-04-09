package jp.co.itechh.quad.core.config;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.method.presentation.api.PaymentMethodApi;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
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
 * Api Configuration
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Configuration
public class ApiClientConfiguration {

    @Value("${base-path-order-service}")
    private String basePathOrderService;

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-logistic-service}")
    private String basePathLogisticService;

    @Value("${base-path-payment-service}")
    private String basePathPaymentService;

    @Value("${base-path-price-planning-service}")
    private String basePathPricePlanningService;

    @Value("${base-path-product-service}")
    private String basePathProductService;

    @Value("${base-path-promotion-service}")
    private String basePathPromotionService;

    @Value("${base-path-customize-service}")
    private String basePathCustomizeService;

    private RestTemplate restTemplate = ApplicationContextUtility.getBean(RestTemplate.class);

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
    public CategoryApi categoryApi() {
        jp.co.itechh.quad.category.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.category.presentation.ApiClient(restTemplate);
        CategoryApi categoryApi = new CategoryApi(apiClient);
        categoryApi.getApiClient().setBasePath(basePathProductService);
        return categoryApi;
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
    public CouponApi couponApi() {
        jp.co.itechh.quad.coupon.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.coupon.presentation.ApiClient(restTemplate);
        CouponApi couponApi = new CouponApi(apiClient);
        couponApi.getApiClient().setBasePath(basePathPricePlanningService);
        return couponApi;
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

    @Bean
    @Primary
    @Scope("prototype")
    public MailMagazineApi mailmagazineApi() {
        jp.co.itechh.quad.mailmagazine.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mailmagazine.presentation.ApiClient(restTemplate);
        MailMagazineApi mailMagazineApi = new MailMagazineApi(apiClient);
        mailMagazineApi.getApiClient().setBasePath(basePathUserService);
        return mailMagazineApi;
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
    public ExaminationApi examinationApi() {
        jp.co.itechh.quad.examination.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.examination.presentation.ApiClient(restTemplate);
        ExaminationApi examinationApi = new ExaminationApi(apiClient);
        examinationApi.getApiClient().setBasePath(basePathCustomizeService);
        return examinationApi;
    }

}