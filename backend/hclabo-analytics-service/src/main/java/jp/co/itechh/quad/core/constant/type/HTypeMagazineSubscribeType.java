/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * メールマガジン購読フラグ
 *
 * @author kimura
 */
@Getter
@AllArgsConstructor
public enum HTypeMagazineSubscribeType implements EnumType {

    /**
     * 購読希望する
     */
    SUBSCRIBE("希望する", "0"),

    /**
     * 購読希望しない
     */
    NOT_SUBSCRIBE("希望しない", "1");

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
