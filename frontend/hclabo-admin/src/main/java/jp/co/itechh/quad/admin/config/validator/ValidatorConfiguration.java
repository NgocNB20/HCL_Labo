/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.config.validator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.AggregateResourceBundleLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;

@Configuration
public class ValidatorConfiguration {

    @Bean(name = "csvReaderValidator")
    Validator validator() {
        // バリデーター利用の宣言
        return Validation.byDefaultProvider()
                         .configure()
                         .messageInterpolator(
                                         // デフォルトメッセージの上書きのために設定
                                         new ResourceBundleMessageInterpolator(
                                                         // 単一ファイルの場合は、PlatformResourceBundleLocatorを利用
                                                         // new PlatformResourceBundleLocator("config.hitmall.validationMessages")
                                                         // 複数ファイルの場合は、AggregateResourceBundleLocatorを利用
                                                         new AggregateResourceBundleLocator(Arrays.asList(
                                                                         "config.hitmall.validationMessages",
                                                                         "config.hitmall.coreMessages"
                                                                                                         ))))
                         .buildValidatorFactory()
                         .getValidator();
    }
}