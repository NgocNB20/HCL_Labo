/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;

/**
 * マルチペイメント決済通信の JOB_CD
 *
 * @author kaneda
 *
 */
@AllArgsConstructor
public enum HTypeJobCode implements EnumType {

    /** 未決済 ※ラベル未使用 */
    UNPROCESSED("", "UNPROCESSED"),

    /** 未決済(3D 登録済) ※ラベル未使用 */
    AUTHENTICATED("", "AUTHENTICATED"),

    /** 有効性チェック ※ラベル未使用 */
    CHECK("", "CHECK"),

    /** 即時売上 ※ラベル未使用 */
    CAPTURE("", "CAPTURE"),

    /** 仮売上 ※ラベル未使用 */
    AUTH("", "AUTH"),

    /** 実売上 ※ラベル未使用 */
    SALES("", "SALES"),

    /** 取消 ※ラベル未使用 */
    VOID("", "VOID"),

    /** 返品 ※ラベル未使用 */
    RETURN("", "RETURN"),

    /** 月跨り返品 ※ラベル未使用 */
    RETURNX("", "RETURNX"),

    /** 値なし ※ラベル未使用 */
    NULL("", ""),

    /** キャンセル ※ラベル未使用 */
    CANCEL("", "CANCEL");

    /** doma用ファクトリメソッド */
    public static HTypeJobCode of(String value) {

        HTypeJobCode hType = EnumTypeUtil.getEnumFromValue(HTypeJobCode.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル<br/>
     */
    @SuppressWarnings("unused")
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;

    /**
     * ラベルを返す<br/>
     *
     * @return ラベル
     */
    @Override
    public String getLabel() {
        return getValue();
    }

    @Override
    public String getValue() {
        return this.value;
    }

}