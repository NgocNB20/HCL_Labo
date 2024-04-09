/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.util.common;

import jp.co.itechh.quad.core.constant.type.EnumType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 区分値ユーティル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class EnumTypeUtil {

    /**
     * 隠蔽コンストラクタ
     */
    private EnumTypeUtil() {
    }

    /**
     * EnumTypeから区分値とラベルのMapを作成
     *
     * @param <T>           列挙型クラス
     * @param enumTypeClass HEnumType実装クラス
     * @return EnumValueLabelMap(key = HEnumType # getValue (), value=HEnumType#getLabel())
     */
    public static <T extends EnumType> Map<String, String> getEnumMap(Class<T> enumTypeClass) {

        return getEnumMap(enumTypeClass, false);

    }

    /**
     * EnumTypeから区分値とラベルのMapを作成
     *
     * @param <T>           列挙型クラス
     * @param enumTypeClass HEnumType実装クラス
     * @param labelKeyFlag  HEnumType実装クラスのラベルをMapのキーとするか
     * @return EnumValueLabelMap(key = HEnumType # getValue (), value=HEnumType#getLabel())
     */
    public static <T extends EnumType> Map<String, String> getEnumMap(Class<T> enumTypeClass, boolean labelKeyFlag) {

        // label,valueを持つ列挙型からMapを作成
        Map<String, String> enumMap = new LinkedHashMap<>();
        EnumType[] enumTypeArray = enumTypeClass.getEnumConstants();

        if (enumTypeArray == null) {
            return null;
        }
        for (EnumType h : enumTypeArray) {
            if (labelKeyFlag) {
                enumMap.put(h.getLabel(), h.getLabel());
            } else {
                enumMap.put(h.getValue(), h.getLabel());
            }
        }
        return enumMap;
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
     * 対象列挙型が存在するか判定する
     * 存在する場合はtrueを返却する
     *
     * @param <T>
     * @param <T>           列挙型クラス
     * @param enumTypeClass HEnumType実装クラス
     * @param value         区分値
     * @return boolean
     */
    public static <T extends EnumType> boolean isExist(Class<T> enumTypeClass, String value) {

        if (getEnumFromValue(enumTypeClass, value) == null) {
            return false;
        }
        return true;
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
     * ラベル取得
     * nullの場合nullを返す
     *
     * @param enumType HEnumType実装クラス
     * @return ラベル値
     */
    public static String getLabelValue(EnumType enumType) {

        if (enumType == null) {
            return null;
        }
        return enumType.getLabel();
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