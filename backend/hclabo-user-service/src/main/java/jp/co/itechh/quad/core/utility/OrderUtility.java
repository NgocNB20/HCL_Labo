/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;

/**
 * 受注業務ユーティリティクラス
 *
 * @author negishi
 * @author Kaneko (itec) 2012/08/20 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class OrderUtility {

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    public OrderUtility(ConversionUtility conversionUtility, DateUtility dateUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 注文エラーメール送信用パラメータ保持クラス<br/>
     *
     * @author sm21603
     */
    @Data
    protected static class OrderRegistAlertMailProperties {
        /** メールサーバ */
        private String mailServer;

        /** メールのFROM */
        private String mailFrom;

        /** アラートメール受信者 */
        private String[] recipients;

        /** アラートメール対象外エラーコード */
        private List<String> excludeErrorCode;

        /** メールに記載するシステム名 */
        private String mailSystem;
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