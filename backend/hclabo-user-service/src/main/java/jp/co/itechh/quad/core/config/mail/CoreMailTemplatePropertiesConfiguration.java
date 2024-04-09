/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.config.mail;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 新HIT-MALLのメールテンプレートのコンフィグレーション
 * 作成日：2021/03/30
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 *
 */
@Configuration
@PropertySource(value = "classpath:config/hitmall/mail/mail-template.properties", ignoreResourceNotFound = true,
                encoding = "UTF-8")
public class CoreMailTemplatePropertiesConfiguration {

}