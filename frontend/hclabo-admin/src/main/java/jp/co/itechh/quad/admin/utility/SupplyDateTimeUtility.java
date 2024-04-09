/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.utility;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * <span class="logicName">【日付変換】</span>時分秒補完コンバータクラス。<br />
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Component
public class SupplyDateTimeUtility {

    /**
     * 補完種別 開始日時<br />
     */
    public static final String TYPE_START = "start";

    /**
     * 補完種別 終了日時<br />
     */
    public static final String TYPE_END = "end";

    /**
     * yyyy/MM/dd HH:mm:ss正規表現パターン
     */
    private static final String DATE_1_REG_PATTERN =
                    "[0-9]{4}/([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|[12][0-9]|3[01]) ([0-9]+):([0-9]+):([0-9]+)";

    /**
     * yyyy/MM/dd HH:mm正規表現パターン
     */
    private static final String DATE_2_REG_PATTERN =
                    "[0-9]{4}/([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|[12][0-9]|3[01]) ([0-9]+):([0-9]+)";

    /**
     * yyyy/MM/dd HH正規表現パターン
     */
    private static final String DATE_3_REG_PATTERN =
                    "[0-9]{4}/([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|[12][0-9]|3[01]) ([0-9]+)";

    /**
     * yyyy/MM/dd正規表現パターン
     */
    private static final String DATE_4_REG_PATTERN = "[0-9]{4}/([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|[12][0-9]|3[01])";

    /**
     * 時分秒補完を補完した後、日付変換します。<br />
     * 年月日時分秒フォーマットの時分秒を補完して変換します。<br />
     * 以下のフォーマットをサポート（サポート対象外はnullを返す）<br />
     * yyyy/MM/dd HH:mm:ss → 補完しない<br />
     * yyyy/MM/dd HH:mm → 秒を補完。開始日時の場合00秒。終了日時の場合59秒。<br />
     * yyyy/MM/dd HH → 分秒を補完。開始日時の場合00分00秒。終了日時の場合59分59秒。<br />
     * yyyy/MM/dd → 時分秒を補完。開始日時の場合00時00分00秒。終了日時の場合59時59分59秒。<br />
     *
     * @param value
     * @param supplyDateType
     * @return
     */
    public String supplyDateTime(String value, String supplyDateType) {

        // 空文字の場合はnullを返す
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        // 正規表現チェックを行い時分秒を補完
        String timeSupplyDate = "";
        if (Pattern.matches(DATE_1_REG_PATTERN, value)) {
            timeSupplyDate = value;
        } else if (Pattern.matches(DATE_2_REG_PATTERN, value)) {
            if (TYPE_START.equals(supplyDateType)) {
                timeSupplyDate = value + ":00";
            } else if (TYPE_END.equals(supplyDateType)) {
                timeSupplyDate = value + ":59";
            }
        } else if (Pattern.matches(DATE_3_REG_PATTERN, value)) {
            if (TYPE_START.equals(supplyDateType)) {
                timeSupplyDate = value + ":00:00";
            } else if (TYPE_END.equals(supplyDateType)) {
                timeSupplyDate = value + ":59:59";
            }
        } else if (Pattern.matches(DATE_4_REG_PATTERN, value)) {
            if (TYPE_START.equals(supplyDateType)) {
                timeSupplyDate = value + " 00:00:00";
            } else if (TYPE_END.equals(supplyDateType)) {
                timeSupplyDate = value + " 23:59:59";
            }
        }
        if (StringUtils.isEmpty(timeSupplyDate)) {
            return value;
        }

        return timeSupplyDate;
    }

}