package jp.co.itechh.quad.core.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * アクセス検索キーワーログ出力
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccessSearchKeywordLoggingModule {
    /** ロガー */
    private static final Logger ACCESS_SEARCH_KEYWORD_LOG = LoggerFactory.getLogger("ACCESS-SEARCH-KEYWORD");

    /** ログ内容 */
    private final Map<String, Object> logObj;

    /**
     * コンストラクタ
     */
    public AccessSearchKeywordLoggingModule() {
        this.logObj = new LinkedHashMap<>();
    }

    /**
     * アクセス日時（必須）設定
     *
     * @param accessTime アクセス日時（必須）
     */
    public void setAccessTime(Timestamp accessTime) {
        this.logObj.put("accessTime", accessTime.getTime());
    }

    /**
     * サイト種別（必須）設定
     *
     * @param siteType サイト種別（必須）
     */
    public void setSiteType(String siteType) {
        this.logObj.put("siteType", siteType);
    }

    /**
     * 検索キー（必須）設定
     *
     * @param searchKeyword 検索キー（必須）
     */
    public void setSearchKeyword(String searchKeyword) {
        this.logObj.put("searchKeyword", searchKeyword);
    }

    /**
     * 検索結果件数（必須）設定
     *
     * @param searchResultCount 検索結果件数（必須）
     */
    public void setSearchResultCount(Integer searchResultCount) {
        this.logObj.put("searchResultCount", Objects.requireNonNullElse(searchResultCount, ""));
    }

    /**
     * 検索カテゴリSEQ設定
     *
     * @param searchCategorySeq 検索カテゴリSEQ
     */
    public void setSearchCategorySeq(Integer searchCategorySeq) {
        this.logObj.put("searchCategorySeq", Objects.requireNonNullElse(searchCategorySeq, ""));
    }

    /**
     * 検索価格from設定
     *
     * @param searchPriceFrom 検索価格from
     */
    public void setSearchPriceFrom(BigDecimal searchPriceFrom) {
        this.logObj.put("searchPriceFrom", Objects.requireNonNullElse(searchPriceFrom, ""));
    }

    /**
     * 検索価格to設定
     *
     * @param searchPriceTo 検索価格to
     */
    public void setSearchPriceTo(BigDecimal searchPriceTo) {
        this.logObj.put("searchPriceTo", Objects.requireNonNullElse(searchPriceTo, ""));
    }

    /**
     * ログ出力
     *
     * @param key ログ出力キー
     * @throws JsonProcessingException JsonProcessingException
     */
    public void log(String key) throws JsonProcessingException {
        String raw = convertObjectToJson(logObj);
        ACCESS_SEARCH_KEYWORD_LOG.info(net.logstash.logback.marker.Markers.appendRaw(key, raw), null);
    }

    /**
     * ObjectからJsonに変換<br/>
     *
     * @param object Object
     * @return String
     * @throws JsonProcessingException JsonProcessingException
     */
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer();
        return ow.writeValueAsString(object);
    }
}