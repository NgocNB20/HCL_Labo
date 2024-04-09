/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.valueobject;

import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 配送状況確認番号 値オブジェクト
 */
@Getter
public class ShipmentStatusConfirmCode {

    /** 値 */
    private final String value;

    /** 配送状況確認番号の正規表現 */
    private static final String REGEX_SHIPMENT_STATUS_CONFIRM_CODE = "[a-zA-Z0-9_-]+";

    /** プロパティに浮かせること */
    public static final int LENGTH_OF_VALUE = 40;

    /**
     * コンストラクタ
     *
     * @param value
     */
    public ShipmentStatusConfirmCode(String value) {

        // チェック
        if (StringUtils.isNotBlank(value)) {
            if (!value.matches(REGEX_SHIPMENT_STATUS_CONFIRM_CODE)) {
                throw new DomainException("LOGISTIC-ESSC0001-E");
            }
            if (value.length() > LENGTH_OF_VALUE) {
                throw new DomainException("LOGISTIC-ESSC0001-E");
            }
        }

        // 設定
        this.value = value;
    }

}
