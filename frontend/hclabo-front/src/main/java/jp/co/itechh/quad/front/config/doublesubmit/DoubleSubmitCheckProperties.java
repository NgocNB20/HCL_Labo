/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.config.doublesubmit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

import java.util.Collections;
import java.util.List;

/**
 * ダブルサブミットチェックで使用する設定値の定義クラス
 *
 * @author hk57400
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DoubleSubmitCheckProperties.PREFIX)
public class DoubleSubmitCheckProperties {

    /** 設定値のキーの接頭辞 */
    public static final String PREFIX = "quad.double-submit.check";

    /** チェックを行うかどうか */
    private boolean enabled;

    /**
     * チェックを行うInterceptorを挟み込むパスのリスト
     *
     * @see InterceptorRegistration#addPathPatterns(String...)
     */
    private List<String> pathPatterns = Collections.emptyList();

    /**
     * チェックを行うInterceptorの挟み込みを行わないパスのリスト
     *
     * @see InterceptorRegistration#excludePathPatterns(String...)
     */
    private List<String> excludePathPatterns = Collections.emptyList();

    /** チェック用トークンのデフォルトネームスペースを決定するための正規表現パターン */
    private String nameSpacePattern;

}
