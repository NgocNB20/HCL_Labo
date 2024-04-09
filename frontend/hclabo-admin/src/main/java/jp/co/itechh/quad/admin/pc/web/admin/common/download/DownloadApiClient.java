package jp.co.itechh.quad.admin.pc.web.admin.common.download;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * ダウンロードAPIクライアント
 * @author Doan Thang (VJP)
 */
@Component
public class DownloadApiClient {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadApiClient.class);

    private static DateFormat dateFormat;

    public DownloadApiClient() {

        // ダウンロード用日付フォーマット
        dateFormat = new DateFormatForDownload();

        // デフォルトのタイムゾーンとしてUTCを使用します。
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * APIを呼び出す
     *
     * @param response レスポンス
     * @param basePath ベースパス
     * @param uri URI
     * @param fileName ファイル名
     * @param object リクエストオブジェクト
     */
    public void invokeAPI(HttpServletResponse response, String basePath, String uri, String fileName, Object object) {

        // ダウンロードURL先を作成
        try {
            // レスポンスを設定
            response.addHeader("Content-Type", "application/csv");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setStatus(HttpStatus.OK.value());

            final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.putAll(parameterToMultiValueMap(null, "object", new ObjectMapper().configure(
                                                                                                          SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                                                                                          .convertValue(object,
                                                                                                        Map.class
                                                                                                       )));

            URI url = getUri(basePath, uri, Collections.emptyMap(), queryParams);

            // ダウンロードを実施
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.execute(url, HttpMethod.GET, (ClientHttpRequest requestCallback) -> {
            }, responseExtractor -> {
                IOUtils.copy(responseExtractor.getBody(), response.getOutputStream());
                return null;
            });
        } catch (Exception e) {
            LOGGER.error("ベースパス：" + basePath + "、URI：" + uri + "、メッセージ：" + e.getMessage());
        }
    }

    /**
     * コレクションフォーマット
     */
    public enum CollectionFormat {
        CSV(","), TSV("\t"), SSV(" "), PIPES("|"), MULTI(null);

        private final String separator;

        CollectionFormat(String separator) {
            this.separator = separator;
        }

        private String collectionToString(Collection<?> collection) {
            return StringUtils.collectionToDelimitedString(collection, separator);
        }
    }

    /**
     * 文字型に変換
     *
     * @param param 変換するオブジェクト
     * @return 変換した結果
     */
    public String parameterToString(Object param) {
        if (param == null) {
            return "";
        } else if (param instanceof Date) {
            return formatDate((Date) param);
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            for (Object o : (Collection<?>) param) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(String.valueOf(o));
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    /**
     * 日付型から文字型に変換
     *
     * @param date 日付
     * @return 文字型
     */
    public String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * URI取得
     *
     * @param basePath
     * @param uri
     * @param pathParams
     * @param queryParams
     * @return URI
     */
    public URI getUri(String basePath,
                      String uri,
                      Map<String, Object> pathParams,
                      MultiValueMap<String, String> queryParams) {

        Map<String, Object> uriParams = new HashMap<>(pathParams);

        String finalUri = uri;

        if (queryParams != null && !queryParams.isEmpty()) {
            //Include queryParams in uriParams taking into account the paramName
            String queryUri = generateQueryUri(queryParams, uriParams);
            //Append to finalUri the templatized query string like "?param1={param1Value}&.......
            finalUri += "?" + queryUri;
        }
        String expandedPath = this.expandPath(finalUri, uriParams);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path(expandedPath);

        URI uriBuilder;
        try {
            uriBuilder = new URI(builder.build().toUriString());
        } catch (URISyntaxException ex) {
            LOGGER.error("例外処理が発生しました", ex);
            throw new RestClientException("Could not build URL: " + builder.toUriString(), ex);
        }

        return uriBuilder;
    }

    /**
     * クエリパラメータ生成
     *
     * @param queryParams
     * @param uriParams
     * @return 生成されたクエリパラメータ
     */
    public String generateQueryUri(MultiValueMap<String, String> queryParams, Map<String, Object> uriParams) {
        StringBuilder queryBuilder = new StringBuilder();
        queryParams.forEach((name, values) -> {
            try {
                final String encodedName = URLEncoder.encode(name.toString(), "UTF-8");
                if (CollectionUtils.isEmpty(values)) {
                    if (queryBuilder.length() != 0) {
                        queryBuilder.append('&');
                    }
                    queryBuilder.append(encodedName);
                } else {
                    int valueItemCounter = 0;
                    for (Object value : values) {
                        if (queryBuilder.length() != 0) {
                            queryBuilder.append('&');
                        }
                        queryBuilder.append(encodedName);
                        if (value != null) {
                            String templatizedKey = encodedName + valueItemCounter++;
                            final String encodedValue = URLEncoder.encode(value.toString(), "UTF-8");
                            uriParams.put(templatizedKey, encodedValue);
                            queryBuilder.append('=').append("{").append(templatizedKey).append("}");
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("例外処理が発生しました", e);
            }
        });
        return queryBuilder.toString();
    }

    /**
     * パス展開
     *
     * @param pathTemplate
     * @param variables
     * @return 展開されたパス
     */
    public String expandPath(String pathTemplate, Map<String, Object> variables) {
        // disable default URL encoding
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(uriBuilderFactory);

        return restTemplate.getUriTemplateHandler().expand(pathTemplate, variables).toString();
    }

    /**
     * ObjectからMultiValueMapに変換
     *
     * @param collectionFormat
     * @param name
     * @param value
     * @return MultiValueMap
     */
    public MultiValueMap<String, String> parameterToMultiValueMap(CollectionFormat collectionFormat,
                                                                  String name,
                                                                  Object value) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        if (name == null || name.isEmpty() || value == null) {
            return params;
        }

        if (collectionFormat == null) {
            collectionFormat = CollectionFormat.CSV;
        }

        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> valuesMap = (Map<String, Object>) value;
            for (final Map.Entry<String, Object> entry : valuesMap.entrySet()) {
                params.add(entry.getKey(), parameterToString(entry.getValue()));
            }
            return params;
        }

        Collection<?> valueCollection = null;
        if (value instanceof Collection) {
            valueCollection = (Collection<?>) value;
        } else {
            params.add(name, parameterToString(value));
            return params;
        }

        if (valueCollection.isEmpty()) {
            return params;
        }

        if (collectionFormat.equals(CollectionFormat.MULTI)) {
            for (Object item : valueCollection) {
                params.add(name, parameterToString(item));
            }
            return params;
        }

        List<String> values = new ArrayList<String>();
        for (Object o : valueCollection) {
            values.add(parameterToString(o));
        }
        params.add(name, collectionFormat.collectionToString(values));

        return params;
    }
}