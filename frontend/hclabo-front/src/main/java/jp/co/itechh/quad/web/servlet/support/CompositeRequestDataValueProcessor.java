/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.servlet.support;

import org.apache.commons.collections.MapUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring の {@code RequestDataValueProcessor} を拡張したクラス
 * <pre>
 *   Wサブミット・ブラウザバック対策のトークン対応用に作成。
 *   Spring標準だと、Spring SecurityのCSRF対策のために実装されている
 *     CsrfRequestDataValueProcessor
 *   がDIされてしまい、
 *   他の RequestDataValueProcessor を利用することができない。
 *   複数のProcessorを有効にできるように、TERASOLUNA の実装を参考に作成。
 * </pre>
 *
 * @author hk57400
 */
public class CompositeRequestDataValueProcessor implements RequestDataValueProcessor {

    /** 適用するProcessorのリスト */
    private final List<RequestDataValueProcessor> processors;

    /** 適用するProcessorのリスト ※逆順にして保持（output系の処理時は、逆順で適用） */
    private final List<RequestDataValueProcessor> reversedProcessors;

    /**
     * コンストラクタ
     *
     * @param processors 適用するProcessorのリスト
     */
    public CompositeRequestDataValueProcessor(RequestDataValueProcessor... processors) {

        this.processors = List.of(processors);

        List<RequestDataValueProcessor> reverseList = Arrays.asList(processors);
        Collections.reverse(reverseList);
        this.reversedProcessors = Collections.unmodifiableList(reverseList);
    }

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {

        for (RequestDataValueProcessor processor : processors) {
            action = processor.processAction(request, action, httpMethod);
        }

        return action;
    }

    @Override
    public String processFormFieldValue(HttpServletRequest request, @Nullable String name, String value, String type) {

        for (RequestDataValueProcessor processor : processors) {
            value = processor.processFormFieldValue(request, name, value, type);
        }

        return value;
    }

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {

        Map<String, String> result = new LinkedHashMap<>();
        for (RequestDataValueProcessor processor : reversedProcessors) {
            Map<String, String> map = processor.getExtraHiddenFields(request);
            if (MapUtils.isNotEmpty(map)) {
                result.putAll(map);
            }
        }

        return result;
    }

    @Override
    public String processUrl(HttpServletRequest request, String url) {

        for (RequestDataValueProcessor processor : processors) {
            url = processor.processUrl(request, url);
        }

        return url;
    }

}
