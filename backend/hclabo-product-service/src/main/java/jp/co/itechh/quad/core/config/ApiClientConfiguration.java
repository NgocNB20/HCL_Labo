package jp.co.itechh.quad.core.config;

import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.imageupdate.presentation.api.ImageUpdateApi;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import jp.co.itechh.quad.tag.presentation.api.TagApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * Api Client Configuration
 *
 * @author Doan Thang (VJP)
 */
@Configuration
public class ApiClientConfiguration {

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-logistic-service}")
    private String basePathLogisticService;

    @Value("${base-path-analytics-service}")
    private String basePathAnalyticsService;

    @Value("${base-path-product-service}")
    private String basePathProductService;

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
    public StockApi stockApi() {
        jp.co.itechh.quad.stock.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.stock.presentation.ApiClient(restTemplate);
        StockApi stockApi = new StockApi(apiClient);
        stockApi.getApiClient().setBasePath(basePathLogisticService);
        return stockApi;
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
    public ImageUpdateApi imageUpdateApi() {
        jp.co.itechh.quad.imageupdate.presentation.ApiClient apiClient =
                        new jp.co.itechh.quad.imageupdate.presentation.ApiClient(restTemplate);
        ImageUpdateApi imageUpdateApi = new ImageUpdateApi(apiClient);
        imageUpdateApi.getApiClient().setBasePath(basePathProductService);
        return imageUpdateApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public TagApi tagApi() {
        TagApi tagApi = new TagApi();
        tagApi.getApiClient().setBasePath(basePathProductService);
        return tagApi;
    }

    @Bean
    @Primary
    @Scope("prototype")
    public CategoryApi categoryApi() {
        CategoryApi categoryApi = new CategoryApi();
        categoryApi.getApiClient().setBasePath(basePathProductService);
        return categoryApi;
    }
}