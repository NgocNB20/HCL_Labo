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
 * 郵便番号タイプ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeZipCodeType implements EnumType {

    /** 住所の郵便番号 ※ラベル未使用 */
    NORMAL("", "0"),

    /** 事業所の大口郵便番号 ※ラベル未使用 */
    OFFICE("", "0");

    /**
     * バッチ識別情報を取得する<br/>
     * バッチタスクテーブルのバッチ識別情報に格納するバッチ名を返却する。<br/>
     *
     * @return 区分値に応じたバッチ識別情報
     */
    public String getBatchType() {

        if (NORMAL.equals(this)) {
            return "ZIP_CODE";
        } else if (OFFICE.equals(this)) {
            return "OFFICE_ZIP_CODE";
        }
        return null;
    }

    /** doma用ファクトリメソッド */
    public static HTypeZipCodeType of(String value) {

        HTypeZipCodeType hType = EnumTypeUtil.getEnumFromValue(HTypeZipCodeType.class, value);

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
