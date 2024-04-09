package jp.co.itechh.quad.front.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:config/hitmall/validationMessages.properties"}, ignoreResourceNotFound = true,
                encoding = "UTF-8")
public class HmCorePropertiesConfiguration {
}