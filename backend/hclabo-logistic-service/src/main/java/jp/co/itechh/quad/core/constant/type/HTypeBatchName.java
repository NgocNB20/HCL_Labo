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
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeBatchName implements EnumType {

    /** 郵便番号自動更新 */
    BATCH_ZIP_CODE("郵便番号自動更新", BatchName.BATCH_ZIP_CODE),

    /** 事業所郵便番号自動更新 */
    BATCH_OFFICE_ZIP_CODE("事業所郵便番号自動更新", BatchName.BATCH_OFFICE_ZIP_CODE),

    /** 在庫開放バッチ */
    BATCH_ORDER_RESERVE_STOCK_RELEASE("在庫開放バッチ", BatchName.BATCH_ORDER_RESERVE_STOCK_RELEASE);

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