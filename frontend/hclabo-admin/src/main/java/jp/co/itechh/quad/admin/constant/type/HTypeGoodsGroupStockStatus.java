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
 * 在庫状態表示：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeGoodsGroupStockStatus implements EnumType {

    /** 非販売 */
    NO_SALE("非販売", "0"),

    /** 販売期間終了 */
    SOLDOUT("販売期間終了", "1"),

    /** 販売前 */
    BEFORE_SALE("販売前", "2"),

    /** 在庫なし */
    STOCK_NOSTOCK("在庫なし", "3"),

    /** 在庫あり */
    STOCK_POSSIBLE_SALES("在庫あり", "4"),

    /** 残りわずか */
    STOCK_FEW("残りわずか", "5");

    /** doma用ファクトリメソッド */
    public static HTypeGoodsGroupStockStatus of(String value) {

        HTypeGoodsGroupStockStatus hType = EnumTypeUtil.getEnumFromValue(HTypeGoodsGroupStockStatus.class, value);

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