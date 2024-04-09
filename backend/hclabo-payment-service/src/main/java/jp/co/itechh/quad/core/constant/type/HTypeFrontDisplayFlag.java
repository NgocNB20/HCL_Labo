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
 * Front表示フラグ
 * <pre>
 * 本カラムはカードブランドの一覧を取得(CardBrandDao_getCardBrandList.sql)のみで使用
 * PKGの製造ルール上、EnumTypeクラスを作成しているが制御する想定はしていないため@Deprecatedを指定している
 * </pre>
 * @author kaneda
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeFrontDisplayFlag implements EnumType {

    /** 表示する ※ラベル未使用 */
    @Deprecated ON("", "1"),

    /**表示しない ※ラベル未使用 */
    @Deprecated OFF("", "0");

    /** doma用ファクトリメソッド */
    public static HTypeFrontDisplayFlag of(String value) {

        HTypeFrontDisplayFlag hType = EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayFlag.class, value);

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