package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 注文の販売状況 (ENUM)
 */
@Getter
@AllArgsConstructor
public enum HTypeOrderSalesStatus implements EnumType {

    /**
     * 受注
     */
    ORDER("受注", "0"),

    /**
     * 売上
     */
    SALES("売上", "1");

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
