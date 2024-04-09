/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.config.doublesubmit;

import jp.co.itechh.quad.web.doublesubmit.interceptor.DoubleSubmitCheckInterceptor;
import jp.co.itechh.quad.web.servlet.support.CompositeRequestDataValueProcessor;
import jp.co.itechh.quad.web.servlet.support.doublesubmit.DoubleSubmitTokenRequestDataValueProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * ダブルサブミットチェック用設定クラス
 * <pre>
 *   特にサーバ環境において、jarの読み込み順のせいか
 *   標準の CsrfRequestDataValueProcessor が勝ってしまい、
 *   拡張した CompositeRequestDataValueProcessor を読み込んでくれないため
 *   通常の Configuration ではなく AutoConfiguration を利用し、
 *   明示的に SecurityAutoConfiguration の後でロードする。
 *   ※中の WebMvcSecurityConfiguration で定義されている。
 * </pre>
 *
 * @author hk57400
 */
@AutoConfiguration
@EnableConfigurationProperties(DoubleSubmitCheckProperties.class)
@ConditionalOnProperty(prefix = DoubleSubmitCheckProperties.PREFIX, name = "enabled", havingValue = "true")
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class DoubleSubmitCheckAutoConfiguration implements WebMvcConfigurer {

    /** 設定値の定義クラス */
    private final DoubleSubmitCheckProperties doubleSubmitCheckProperties;

    /**
     * コンストラクタ
     *
     * @param doubleSubmitCheckProperties 設定値の定義クラス
     */
    @Autowired
    public DoubleSubmitCheckAutoConfiguration(DoubleSubmitCheckProperties doubleSubmitCheckProperties) {
        this.doubleSubmitCheckProperties = doubleSubmitCheckProperties;
    }

    @Bean
    @Primary
    public RequestDataValueProcessor requestDataValueProcessor() {
        return new CompositeRequestDataValueProcessor(
                        new CsrfRequestDataValueProcessor(), new DoubleSubmitTokenRequestDataValueProcessor());
    }

    @Bean
    public DoubleSubmitCheckInterceptor doubleSubmitCheckInterceptor() {
        return new DoubleSubmitCheckInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(doubleSubmitCheckInterceptor())
                .addPathPatterns(doubleSubmitCheckProperties.getPathPatterns())
                .excludePathPatterns(doubleSubmitCheckProperties.getExcludePathPatterns());
    }

}
