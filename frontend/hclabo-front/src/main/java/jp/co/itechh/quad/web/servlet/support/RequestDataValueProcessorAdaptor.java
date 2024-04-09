/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.servlet.support;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Spring の {@code RequestDataValueProcessor} の実装拡張用クラス
 *
 * @author hk57400
 */
public class RequestDataValueProcessorAdaptor implements RequestDataValueProcessor {

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {
        return action;
    }

    @Override
    public String processFormFieldValue(HttpServletRequest request, @Nullable String name, String value, String type) {
        return value;
    }

    @Override
    @Nullable
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        return null;
    }

    @Override
    public String processUrl(HttpServletRequest request, String url) {
        return url;
    }

}
