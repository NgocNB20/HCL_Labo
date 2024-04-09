/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

/**
 * 取引ステータス 値オブジェクト(enum)
 */
public enum TransactionStatus {

    /** 下書き */
    DRAFT,

    /** 確定 */
    OPEN,

    /** 終了 */
    CLOSE,

    /** 取消 */
    CANCEL;

}
