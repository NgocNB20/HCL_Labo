/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.thymeleaf;

import jp.co.itechh.quad.front.constant.type.EnumType;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;

/**
 * 区分値ラベル カスタムユーティリティオブジェク用Utility<br/>
 *
 * @author kaneda
 *
 */
public class EnumTypeViewUtil {

    /** パッケージ名 */
    protected static String PACKAGE_NAME = "jp.co.itechh.quad.front.constant.type.";

    /**
     *
     * Enumよりラベル取得<br/>
     *
     * @param targetEnumName Enumクラス名
     * @param value          Enumのvalue値
     * @return Enumのlabel値
     */
    public String getLabel(final String targetEnumName, final String value) throws ClassNotFoundException {

        if (value != null) {
            @SuppressWarnings("unchecked")
            Class<? extends EnumType> enumTypeClass =
                            (Class<? extends EnumType>) Class.forName(PACKAGE_NAME + targetEnumName);
            EnumType targetEnum = EnumTypeUtil.getEnumFromValue(enumTypeClass, value);
            if (targetEnum != null) {
                return targetEnum.getLabel();
            }
        }

        return null;
    }
}