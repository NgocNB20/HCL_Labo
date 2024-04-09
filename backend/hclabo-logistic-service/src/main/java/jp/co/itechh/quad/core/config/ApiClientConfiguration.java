package jp.co.itechh.quad.core.config;

import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.productnovelty.presentation.api.ProductNoveltyApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * Api Configuration
 *
 * @author Doan Thang (VJP)
 */
@Configuration
public class ApiClientConfiguration {

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-product-service}")
    private String basePathProductService;

    @Value("${base-path-promotion-service}")
    private String basePathPromotionService;

    @Value("${base-path-payment-service}")
    private String basePathPaymentService;

    private RestTemplate restTemplate = ApplicationContextUtility.getBean(RestTemplate.class);

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
    public MailMagazineApi mailMagazineApi() {
        jp.co.itechh.quad.mailmagazine.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.mailmagazine.presentation.ApiClient(restTemplate);
        MailMagazineApi mailMagazineApi = new MailMagazineApi(apiClient);
        mailMagazineApi.getApiClient().setBasePath(basePathUserService);
        return mailMagazineApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public ProductNoveltyApi productsNoveltyApi() {
        jp.co.itechh.quad.productnovelty.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.productnovelty.presentation.ApiClient(restTemplate);
        ProductNoveltyApi productsNoveltyApi = new ProductNoveltyApi(apiClient);
        productsNoveltyApi.getApiClient().setBasePath(basePathProductService);
        return productsNoveltyApi;
    }

}