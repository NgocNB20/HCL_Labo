/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.util.common;

import jp.co.itechh.quad.core.constant.type.EnumType;

/**
 * 区分値ユーティル
 *
 * @author kaneda
 */
public class EnumTypeUtil {

    /**
     * 隠蔽コンストラクタ
     */
    private EnumTypeUtil() {
    }

    /**
     * 区分値から列挙型を返す
     * 区分値に一致しない場合は、nullを返す
     *
     * @param <T>           列挙型クラス
     * @param enumTypeClass HEnumType実装クラス
     * @param value         区分値
     * @return 列挙型クラス
     */
    public static <T extends EnumType> T getEnumFromValue(Class<T> enumTypeClass, String value) {

        T[] enumTypeArray = enumTypeClass.getEnumConstants();

        if (enumTypeArray == null) {
            return null;
        }

        for (T enumType : enumTypeArray) {
            if (enumType.getValue().equals(value)) {
                return enumType;
            }
        }

        return null;
    }

    /**
     * 区分ラベルから列挙型を返す
     * 区分ラベルに一致しない場合は、nullを返す
     *
     * @param <T>           列挙型クラス
     * @param enumTypeClass HEnumType実装クラス
     * @param label         ラベル
     * @return 列挙型クラス
     */
    public static <T extends EnumType> T getEnumFromLabel(Class<T> enumTypeClass, String label) {
        T[] enumTypeArray = enumTypeClass.getEnumConstants();

        if (enumTypeArray == null) {
            return null;
        }

        for (T enumType : enumTypeArray) {
            if (enumType.getLabel().equals(label)) {
                return enumType;
            }
        }
        return null;
    }

    /**
     * 区分名称から列挙型を返す
     * 区分名称に一致しない場合は、nullを返す
     *
     * @param <T>           列挙型クラス
     * @param enumTypeClass HEnumType実装クラス
     * @param name          名称
     * @return 列挙型クラス
     */
    public static <T extends EnumType> T getEnumFromName(Class<T> enumTypeClass, String name) {
        T[] enumTypeArray = enumTypeClass.getEnumConstants();

        if (enumTypeArray == null) {
            return null;
        }

        for (T enumType : enumTypeArray) {
            if (enumType.toString().equals(name)) {
                return enumType;
            }
        }
        return null;
    }

    /**
     * 値の取得
     * nullの場合nullを返す
     *
     * @param enumType HEnumType実装クラス
     * @return 値
     */
    public static String getValue(EnumType enumType) {

        if (enumType == null) {
            return null;
        }
        return enumType.getValue();
    }
}
