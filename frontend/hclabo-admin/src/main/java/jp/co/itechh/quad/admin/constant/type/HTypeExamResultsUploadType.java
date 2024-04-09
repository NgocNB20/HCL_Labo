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
 * 検査結果アップロード種別：列挙型
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeExamResultsUploadType implements EnumType {

    /** 追加モード */
    CSV("検査結果CSV", "0"),

    /** 更新モード */
    PDF("検査結果PDF", "1");

    /** doma用ファクトリメソッド */
    public static HTypeExamResultsUploadType of(String value) {

        HTypeExamResultsUploadType hType = EnumTypeUtil.getEnumFromValue(HTypeExamResultsUploadType.class, value);

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