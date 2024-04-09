/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 変換ユーティリティクラス
 * <p>
 * Dxoでデータの型変換や分割・結合時に使用する。
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ConversionUtility {

    /**
     * 隠蔽コンストラクタ
     */
    public ConversionUtility() {
    }

    /**
     * Date⇒TimeStamp変換
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

    /**
     * TimeStamp⇒年月日変換<br/>
     *
     * @param date 変換前の値
     * @return 変換後の値（yyyy/MM/dd）
     */
    public String toYmd(Date date) {

        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        formatter.setLenient(false);

        return formatter.format(date);
    }

    public String toYmdHms(Date date) {

        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        formatter.setLenient(false);

        return formatter.format(date);
    }

    /**
     * LocalDateTime⇒Date変換
     *
     * @param localDateTime localDateTime
     * @return Date変換
     */
    public Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
