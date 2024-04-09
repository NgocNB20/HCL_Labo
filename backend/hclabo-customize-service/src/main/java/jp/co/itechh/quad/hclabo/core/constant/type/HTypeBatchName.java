/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.constant.type;

import jp.co.itechh.quad.hclabo.core.constant.BatchName;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
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

    /** 検査キット受領登録バッチ実行 */
    BATCH_EXAMKIT_RECEIVED_ENTRY("検査キット受領登録バッチ実行", BatchName.BATCH_EXAMKIT_RECEIVED_ENTRY),

    BATCH_EXAM_RESULTS_ENTRY("検査結果登録", BatchName.BATCH_EXAM_RESULTS_ENTRY);

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