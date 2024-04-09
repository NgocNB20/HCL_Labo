/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.config.hitmall.log;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * HIT-MALLログ用プロパティ　読み込みクラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Configuration
@PropertySource(value = {"classpath:config/hitmall/log/application_log.properties"}, ignoreResourceNotFound = true,
                encoding = "UTF-8")
public class FrontLogPropertiesConfiguration {

}