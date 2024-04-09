/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.base.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.hclabo.core.base.util.seasar.DateConversionUtil;
import jp.co.itechh.quad.hclabo.core.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.hclabo.core.base.util.seasar.StringConversionUtil;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 変換ユーティリティクラス
 * <P>
 * Dxoでデータの型変換や分割・結合時に使用する。
 *
 */
@Component
public class ConversionUtility {

    /** 隠蔽コンストラクタ  */
    public ConversionUtility() {
    }

    /**
     * Integer変換<br/>
     *
     * @param value 変換元文字列
     * @return 変換後の値
     */
    public Integer toInteger(Object value) {
        return IntegerConversionUtil.toInteger(value);
    }

    /**
     * String変換<br/>
     *
     * @param value 変換元の値
     * @return 変換後の値
     */
    public String toString(Object value) {
        return StringConversionUtil.toString(value);
    }

    /**
     * 年月日⇒Date変換<br/>
     *
     * @param ymd 年月日（yyyy/MM/dd）
     * @return 変換後の値
     */
    public Date toDate(String ymd) {
        return DateConversionUtil.toDate(ymd, "yyyy/MM/dd");
    }

    /**
     * Date⇒TimeStamp変換<br/>
     *
     * @param date 日時
     * @return Timestamp
     */
    public Timestamp toTimestamp(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /** 以下、SanitizeUtilより統合 */

    /** サイニタイジング対象文字を置き換える為のMAP */
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
            return null;
        }
    }
}
