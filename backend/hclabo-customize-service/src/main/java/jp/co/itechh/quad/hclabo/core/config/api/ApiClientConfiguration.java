package jp.co.itechh.quad.hclabo.core.config.api;

import jp.co.itechh.quad.hclabo.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
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

    @Value("${base-path-user-service}")
    private String basePathUserService;

    @Value("${base-path-order-service}")
    private String basePathOrderService;

    @Value("${base-path-analytics-service}")
    private String basePathAnalyticsService;

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
    public NotificationSubApi notificationSubApi() {
        jp.co.itechh.quad.notificationsub.presentation.ApiClient apiClient =
            new jp.co.itechh.quad.notificationsub.presentation.ApiClient(restTemplate);
        NotificationSubApi notificationSubApi = new NotificationSubApi(apiClient);
        notificationSubApi.getApiClient().setBasePath(basePathUserService);
        return notificationSubApi;
    }

}