/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.constant.BatchName;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * バッチ処理名：列挙型
 *
 * @author kaneda
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeBatchName implements EnumType {

    /** オーソリ期限切れ間近注文警告 */
    BATCH_AUTHTIMELIMITORDER_NOTIFICATION("オーソリ期限切れ間近注文警告", BatchName.BATCH_AUTHTIMELIMITORDER_NOTIFICATION),

    /** 与信枠解放バッチ */
    BATCH_ORDER_CREDITLINE_REPORT("与信枠解放バッチ", BatchName.BATCH_ORDER_CREDITLINE_REPORT),

    /** GMO決済キャンセル漏れ検知バッチ */
    BATCH_LINK_PAY_CANCEL_REMINDER("GMO決済キャンセル漏れ検知バッチ", BatchName.BATCH_LINK_PAY_CANCEL_REMINDER);

    /** doma用ファクトリメソッド */
    public static HTypeBatchName of(String value) {

        HTypeBatchName hType = EnumTypeUtil.getEnumFromValue(HTypeBatchName.class, value);

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
