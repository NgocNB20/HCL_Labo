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
 * 在庫状況：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeStockStatusType implements EnumType {

    /** 0：非販売 ※ラベル未使用 */
    NO_SALE("", "0"),

    /** 1：販売期間終了 ※ラベル未使用 */
    SOLDOUT("", "1"),

    /** 2：販売前 ※ラベル未使用 */
    BEFORE_SALE("", "2"),

    /** 9：予約受付前 ※ラベル未使用 */
    BEFORE_RESERVATIONS("", "9"),

    /** 3：在庫なし ※ラベル未使用 */
    STOCK_NOSTOCK("", "3"),

    /** 4：在庫あり ※ラベル未使用 */
    STOCK_POSSIBLE_SALES("", "4"),

    /** 10：予約受付中 ※ラベル未使用 */
    ON_RESERVATIONS("", "10"),

    /** 5：残りわずか ※ラベル未使用 */
    STOCK_FEW("", "5"),

    /** 6：公開前 ※ラベル未使用 */
    BEFORE_OPEN("", "6"),

    /** 7：公開期間終了 ※ラベル未使用 */
    OPEN_END("", "7"),

    /** 8：非公開 ※ラベル未使用 */
    NO_OPEN("", "8");

    /**
     * 在庫状況の表示を行うか？
     * <pre>
     * 以下の場合は表示
     * 在庫なし / 販売期間終了 / 販売前 / 在庫あり / 残りわずか / 非販売 / 予約受付前
     * </pre>
     *
     * @return true = 表示
     */
    public boolean isDisplay() {
        if (this == STOCK_NOSTOCK || this == SOLDOUT || this == BEFORE_SALE || this == STOCK_POSSIBLE_SALES
            || this == STOCK_FEW || this == NO_SALE || this == BEFORE_RESERVATIONS) {
            return true;
        }
        return false;
    }

    /**
     * 注文可能か？
     * <pre>
     * 以下の場合は可能
     * 在庫あり / 残りわずか / 予約受付中
     * </pre>
     *
     * @return true = 注文可能
     */
    public boolean isPossibleBuy() {
        if (this == STOCK_POSSIBLE_SALES || this == STOCK_FEW || this == ON_RESERVATIONS) {
            return true;
        }
        return false;
    }

    /** doma用ファクトリメソッド */
    public static HTypeStockStatusType of(String value) {

        HTypeStockStatusType hType = EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}