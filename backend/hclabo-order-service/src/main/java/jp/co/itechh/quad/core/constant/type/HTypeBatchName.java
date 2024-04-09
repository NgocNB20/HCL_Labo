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
 * バッチ処理名：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeBatchName implements EnumType {

    /** 出荷アップロード */
    BATCH_SHIPMENT_REGIST("出荷アップロード", "BATCH_SHIPMENT_REGIST"),

    /** 決済代行入金結果受取予備処理バッチ */
    BATCH_MULPAY_NOTIFICATION_RECOVERY("決済代行入金結果受取予備処理", "BATCH_MULPAY_NOTIFICATION_RECOVERY"),

    /** 支払督促メール送信 */
    BATCH_REMINDER_PAYMENT("支払督促", "BATCH_REMINDER_PAYMENT"),

    /** 支払期限切れメール送信 */
    BATCH_EXPIRED_PAYMENT("支払期限切れ", "BATCH_EXPIRED_PAYMENT"),

    /** クリア */
    BATCH_CLEAR("クリア", "BATCH_CLEAR"),

    /** ノベルティ */
    BATCH_NOVELTY("ノベルティ", "BATCH_NOVELTY");

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