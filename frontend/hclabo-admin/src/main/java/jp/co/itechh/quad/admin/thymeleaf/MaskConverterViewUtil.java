/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.thymeleaf;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import org.apache.commons.lang3.StringUtils;

/**
 * マスクコンバータ カスタムユーティリティオブジェク用Utility<br/>
 *
 * HM3ではアノテーションのため、マスク範囲が指定可能だが<br/>
 * こちらはマスク範囲指定不可<br/>
 *
 * @author kimura
 */
public class MaskConverterViewUtil {

    /**
     * 値をマスク<br/>
     *
     * @param value
     * @return マスクされた値
     */
    public String convert(final String value) {

        if (StringUtils.isEmpty(value)) {
            return "";
        }

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        return conversionUtility.toMaskString(value, '*');
    }
}