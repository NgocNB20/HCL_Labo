/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

/**
 * キャンセル理由 値オブジェクト(enum)
 */
public enum CancelReason {

    /** 注文誤り */
    MISTAKE,

    /** 在庫なし */
    NOSTOCK,

    /** 支払い無し*/
    NOPAYMENT,

    /** その他 */
    OTHERS;

}
