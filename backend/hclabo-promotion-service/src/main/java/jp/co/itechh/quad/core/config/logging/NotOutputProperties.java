/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.config.logging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Log出力対象外User-Agentの判定で使用する設定値の定義クラス
 *
 * @author mk75401
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = NotOutputProperties.PREFIX)
public class NotOutputProperties {

    /** 設定値のキーの接頭辞 */
    public static final String PREFIX = "quad.logging.not-output";

    /** Log出力対象外User-Agent */
    private List<String> userAgents = Collections.emptyList();

}
