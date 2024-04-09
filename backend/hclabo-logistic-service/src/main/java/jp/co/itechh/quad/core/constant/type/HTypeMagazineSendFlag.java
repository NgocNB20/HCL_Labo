/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * メールマガジン配信条件フラグ<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeMagazineSendFlag implements EnumType {

    /** 条件なし */
    OFF("条件なし", "0"),

    /** 配信中を条件とする */
    ON("配信中を条件とする", "1");

    /** doma用ファクトリメソッド */
    public static HTypeMagazineSendFlag of(String value) {

        HTypeMagazineSendFlag hType = EnumTypeUtil.getEnumFromValue(HTypeMagazineSendFlag.class, value);

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