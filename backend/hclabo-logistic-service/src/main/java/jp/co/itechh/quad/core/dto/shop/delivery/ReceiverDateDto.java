/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.delivery;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeReceiverDateDesignationFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * お届け希望日Dto<br/>
 *
 * @author DtoGenerator
 *
 */
@Data
@Component
@Scope("prototype")
public class ReceiverDateDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * お届け希望日選択肢-指定なし<br/>
     * お届け希望日の選択肢に「指定なし」を付加する場合に使用する<br/>
     */
    public static final String NON_SELECT_KEY = "00000000";

    /**
     * お届け希望日選択肢-指定なし<br/>
     * お届け希望日の選択肢に「指定なし」を付加する場合に使用する<br/>
     */
    public static final String NON_SELECT_VALUE = "指定なし";

    /**
     * 日付Map(key=YYYYMMDD,value=YYYY/MM/DD)<br/>
     */
    private Map<String, String> dateMap;

    /**
     * お届け希望日指定フラグ<br/>
     * 日付Mapが空(配送方法＃選択可能日数=0)の場合にtrue<br/>
     * true..指定可能<br/>
     */
    private HTypeReceiverDateDesignationFlag receiverDateDesignationFlag;

    /** 最短お届け日_日付登録用 */
    private Timestamp shortestDeliveryDateToRegist;

    /**
     * 月日フォーマットに曜日を付加して取得する<br/>
     *
     * @param timestamp 対象の日付
     * @return 曜日
     */
    public static String getFormatMdWithWeek(Timestamp timestamp) {

        StringBuilder sb = new StringBuilder();

        if (timestamp != null) {
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            String dateMd = dateUtility.format(timestamp, "M/d");
            String week = getDayOfTheWeek(timestamp);

            sb.append(dateMd);
            sb.append(week);
        }

        return sb.toString();
    }

    /**
     * 日付の曜日を取得する<br/>
     *
     * @param timestamp 対象の日付
     * @return 曜日
     */
    public static String getDayOfTheWeek(Timestamp timestamp) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        String weekName = null;

        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                weekName = "(日)";
                break;
            case Calendar.MONDAY:
                weekName = "(月)";
                break;
            case Calendar.TUESDAY:
                weekName = "(火)";
                break;
            case Calendar.WEDNESDAY:
                weekName = "(水)";
                break;
            case Calendar.THURSDAY:
                weekName = "(木)";
                break;
            case Calendar.FRIDAY:
                weekName = "(金)";
                break;
            case Calendar.SATURDAY:
                weekName = "(土)";
                break;
            default:
        }
        return weekName;
    }
}