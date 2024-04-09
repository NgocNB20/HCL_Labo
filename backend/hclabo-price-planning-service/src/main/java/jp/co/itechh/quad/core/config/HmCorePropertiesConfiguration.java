package jp.co.itechh.quad.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:config/hitmall/coreMessages.properties",
                "classpath:config/hitmall/IPAddress/IPAddress.properties"}, ignoreResourceNotFound = true,
                encoding = "UTF-8")
public class HmCorePropertiesConfiguration {
}