/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 決済タイプ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeSettlementMethodType implements EnumType {

    /**
     * 全額割引<pre>クーポンで注文金額が0円になった場合に利用する決済区分</pre>
     */
    DISCOUNT("全額割引", "E"),

    /** クレジット **/
    CREDIT("クレジット", "0"),

    /** 現金支払 **/
    // CASH("現金支払", "A"),

    /** 代金引換 **/
    RECEIPT_PAYMENT("代金引換", "B"),

    /** 銀行振込 **/
    BANK_TRANSFER("銀行振込", "C"),

    /** 現金書留 **/
    CASH_REGISTERED_MAIL("現金書留", "D"),

    /** 入金確認 **/
    PRE_PAYMENT("入金確認", "X"),

    /** リンク決済 **/
    LINK_PAYMENT("リンク決済", "5");

    /** doma用ファクトリメソッド */
    public static HTypeSettlementMethodType of(String value) {

        HTypeSettlementMethodType hType = EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
