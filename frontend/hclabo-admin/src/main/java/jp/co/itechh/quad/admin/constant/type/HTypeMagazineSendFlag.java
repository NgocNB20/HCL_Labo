/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * メールマガジン配信条件フラグ<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeMagazineSendFlag implements EnumType {

    /** 0:条件なし */
    OFF("条件なし", "0"),

    /** 1:配信中を条件とする */
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