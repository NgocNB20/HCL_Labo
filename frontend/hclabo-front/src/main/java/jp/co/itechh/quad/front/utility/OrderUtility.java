/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.utility;

import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.util.seasar.StringUtil;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 受注業務ユーティリティクラス
 *
 * @author negishi
 * @author Kaneko (itec) 2012/08/20 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class OrderUtility {

    /** クレジットカードの有効期限（年）を何年分出すかをシステムプロパティから取得するキー */
    protected static final String EXPIRATION_DATE_YEAR = "expiration.date.year";

    /** 無料決済SEQ（設定値） */
    protected static final String FREE_SETTLEMENT_METHOD_SEQ = "free.settlement.method.seq";

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderUtility.class);

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    public OrderUtility(ConversionUtility conversionUtility, DateUtility dateUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * カード入力情報の有効期限（年）プルダウンを作成する
     *
     * @return 有効期限（年）プルダウン
     */
    public Map<String, String> createExpirationDateYearItems() {
        Map<String, String> expirationDateYearMap = new LinkedHashMap<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // システムプロパティから何年分表示するかを取得。
        int years = Integer.parseInt(PropertiesUtil.getSystemPropertiesValue(EXPIRATION_DATE_YEAR));

        for (int i = currentYear; i < currentYear + years; i++) {
            expirationDateYearMap.put(Integer.toString(i).substring(2), Integer.toString(i));
        }

        return expirationDateYearMap;
    }

    /**
     * 無料配送の決済方法 SEQを取得する。
     *
     * @return 無料決済方法SEQ
     */
    public Integer getFreeSettlementMethodSeq() {
        return PropertiesUtil.getSystemPropertiesValueToInt(FREE_SETTLEMENT_METHOD_SEQ);
    }

    /**
     * 配送追跡URLの取得
     *
     * @param deliveryChaseURL              配送追跡URL
     * @param deliveryChaseURLDisplayPeriod 配送追跡URL表示期間
     * @param deliveryCode                  配送伝票
     * @param shipmentDate                  出荷日
     * @return 配送追跡URL
     */
    public String getDeliveryChaseURL(String deliveryChaseURL,
                                      BigDecimal deliveryChaseURLDisplayPeriod,
                                      String deliveryCode,
                                      Timestamp shipmentDate) {

        // 配送追跡URLの設定が無い場合は無し
        if (StringUtil.isEmpty(deliveryChaseURL)) {
            return "";
        }

        // 出荷日が無い場合は無し
        if (shipmentDate == null) {
            return "";
        }

        // 伝票番号が無い場合は無し
        if (StringUtil.isEmpty(deliveryCode)) {
            return "";
        }

        // 配送追跡URLの表示期間が無い場合は無し
        if (deliveryChaseURLDisplayPeriod == null) {
            return "";
        }

        // 出荷日が未来日なら無し
        if (dateUtility.isAfterCurrentTime(shipmentDate)) {
            return "";
        }

        // 配送追跡URLの表示期間が、期間内、または、無期限ならURLを返す
        int period = conversionUtility.toInteger(deliveryChaseURLDisplayPeriod);
        Timestamp targetDay = dateUtility.getAmountDayTimestamp(period, true, shipmentDate);
        if (period == 0 || dateUtility.isAfterCurrentTime(targetDay)) {
            return MessageFormat.format(deliveryChaseURL, deliveryCode);

        }
        return "";
    }
}