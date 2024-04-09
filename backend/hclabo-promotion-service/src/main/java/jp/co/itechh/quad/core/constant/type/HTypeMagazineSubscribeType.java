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
 * メールマガジン購読フラグ
 *
 * @author kimura
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeMagazineSubscribeType implements EnumType {

    /** 購読希望する */
    SUBSCRIBE("希望する", "0"),

    /** 購読希望しない */
    NOT_SUBSCRIBE("希望しない", "1");

    /**
     *
     * コンストラクタ
     *
     * @param ordinal Integer
     * @param name String
     * @param value String
     */

    /** doma用ファクトリメソッド */
    public static HTypeMagazineSubscribeType of(String value) {

        HTypeMagazineSubscribeType hType = EnumTypeUtil.getEnumFromValue(HTypeMagazineSubscribeType.class, value);

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