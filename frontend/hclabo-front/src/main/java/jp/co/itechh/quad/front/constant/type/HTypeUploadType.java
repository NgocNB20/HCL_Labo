/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * アップロード種別：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeUploadType implements EnumType {

    /** 追加モード */
    REGIST("追加モード", "0"),

    /** 更新モード */
    MODIFY("更新モード", "1");

    /** doma用ファクトリメソッド */
    public static HTypeUploadType of(String value) {

        HTypeUploadType hType = EnumTypeUtil.getEnumFromValue(HTypeUploadType.class, value);

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