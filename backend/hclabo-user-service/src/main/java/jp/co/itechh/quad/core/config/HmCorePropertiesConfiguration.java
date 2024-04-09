package jp.co.itechh.quad.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:config/spring-batch.properties",
                "classpath:config/hitmall/mail/mail-template.properties",
                "classpath:config/hitmall/alert/orderRegistAlert.properties"}, ignoreResourceNotFound = true,
                encoding = "UTF-8")
public class HmCorePropertiesConfiguration {
}