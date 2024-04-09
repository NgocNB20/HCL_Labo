/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 期間（受注検索）：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeDate implements EnumType {

    /** 受注日時 */
    ORDER_DATE("受注日時", "1"),

    /** 出荷登録日時 */
    REGIST_SHIPMENT_DATE("出荷登録日時", "2"),

    /** 入金日時*/
    PAYMENT_DATE("入金日時", "3"),

    /** 更新日時 */
    UPDATE_DATE("更新日時", "5"),

    /** 支払期限日 */
    DEADLINE_DATE("支払期限日", "6"),

    /** お届け希望日 */
    RECEIVER_DATE("お届け希望日", "7"),

    /** キャンセル日時 */
    CANCEL_TIME("キャンセル日時", "8"),

    /** 受付日 */
    RECEPTION_DATE("受付日", "9");

    /** doma用ファクトリメソッド */
    public static HTypeDate of(String value) {

        HTypeDate hType = EnumTypeUtil.getEnumFromValue(HTypeDate.class, value);

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