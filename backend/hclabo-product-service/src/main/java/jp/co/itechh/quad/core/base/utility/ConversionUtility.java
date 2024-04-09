/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.core.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.TimestampConversionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 変換ユーティリティクラス
 * <p>
 * Dxoでデータの型変換や分割・結合時に使用する。
 *
 * @author ueshima
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更 SanitizeUtilを統合
 */
@Component
public class ConversionUtility {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionUtility.class);

    /**
     * 隠蔽コンストラクタ
     */
    public ConversionUtility() {
    }

    /**
     * String変換
     *
     * @param value 変換元の値
     * @return 変換後の値
     */
    public String toString(Object value) {
        return StringConversionUtil.toString(value);
    }

    /**
     * Integer変換
     *
     * @param value 変換元文字列
     * @return 変換後の値
     */
    public Integer toInteger(Object value) {
        return IntegerConversionUtil.toInteger(value);
    }

    /**
     * 年月日⇒TimeStamp変換
     *
     * @param ymd 年月日（yyyy/MM/dd）
     * @return 変換後の値
     */
    public Timestamp toTimeStamp(String ymd) {
        return TimestampConversionUtil.toTimestamp(ymd, "yyyy/MM/dd");
    }

    /**
     * TimeStamp⇒Date変換<br/>
     *
     * @param timestamp 日時
     * @return Date
     */
    public Date toDate(Timestamp timestamp) {
        if (timestamp != null) {
            return new Date(timestamp.getTime());
        }
        return null;
    }

    /**
     * Date⇒TimeStamp変換
     *
     * @param date 日時
     * @return Timestamp
     */
    public Timestamp toTimeStamp(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * Stringレスポンスボディからオブジェクトクラスに変換
     *
     * @param body  レスポンスボディ
     * @param clazz クラス
     * @return <T>
     */
    public <T> T toObject(String body, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(body, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * String変換
     *
     * @param obj JSON
     * @return 変換後の値
     */
    public String toJson(Object obj) {
        String jsonStr;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        return jsonStr;
    }

    /**
     * サイニタイジング対象文字を置き換える為のMAP
     */
    protected static final Map<Object, Object> SANITIZE_MAP = new HashMap<>();

    static {
        synchronized (SANITIZE_MAP) {
            if (SANITIZE_MAP.isEmpty()) {
                SANITIZE_MAP.put("&", "&amp;");
                SANITIZE_MAP.put("<", "&lt;");
                SANITIZE_MAP.put(">", "&gt;");
                SANITIZE_MAP.put("\"", "&quot;");
                SANITIZE_MAP.put("'", "&#39;");
            }
        }
    }

}