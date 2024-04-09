package jp.co.itechh.quad.core.config.logging;

import jp.co.itechh.quad.filter.HmLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ロギングフィルタコンフィグレーション
 */
@Configuration
public class LoggingFilterConfiguration {
    @Bean
    public FilterRegistrationBean<HmLoggingFilter> loggingFilter() {
        FilterRegistrationBean<HmLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HmLoggingFilter());
        return registrationBean;
    }
}