/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.card.valueobject;

import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.IPaymentRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * クレジットカード有効期限 値オブジェクト
 */
@Getter
public class CreditExpirationDate implements IPaymentRequest {

    /** 有効期限(年) ※後ろ2桁 */
    private final String expirationYear;

    /** 有効期限(月) ※2桁 */
    private final String expirationMonth;

    /**
     * コンストラクタ
     *
     * @param expirationYear  有効期限(年) ※4桁で引数に指定され、2桁に変換して設定
     * @param expirationMonth 有効期限(月) ※1桁または2桁で引数に指定され、1桁の場合2桁に変換して設定
     */
    public CreditExpirationDate(String expirationYear, String expirationMonth) {

        // 月が1桁の場合2桁に変換する
        if (expirationMonth.length() == 1) {
            expirationMonth = "0" + expirationMonth;
        }

        // 設定
        if (expirationYear.length() == 4) {
            this.expirationYear = expirationYear.substring(2, 4);
        } else {
            this.expirationYear = expirationYear;
        }

        this.expirationMonth = expirationMonth;
    }

    /**
     * コンストラクタ
     *
     * @param expirationYear  有効期限(年)
     * @param expirationMonth 有効期限(月)
     * @param customParams    案件用引数
     */
    public CreditExpirationDate(String expirationYear, String expirationMonth, Object... customParams) {
        // 設定
        this.expirationYear = expirationYear;
        this.expirationMonth = expirationMonth;
    }

    /**
     * クレジット有効期限を設定せよ。
     *
     * @param expirationYear  有効期限(年)
     * @param expirationMonth 有効期限(月)
     * @return クレジット有効期限 値オブジェクト
     */
    public static CreditExpirationDate createCreditExpirationDate(String expirationYear, String expirationMonth) {

        if (StringUtils.isEmpty(expirationMonth) || StringUtils.isEmpty(expirationYear)) {
            return null;
        }

        return new CreditExpirationDate(expirationYear, expirationMonth);
    }

}
