/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * サイトマップタイプ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeUrlType implements EnumType {

    /** 0:静的 ※ラベル未使用 */
    UNCHANGED("", "0"),

    /** 1:商品一覧 ※ラベル未使用 */
    ITEM_LIST("", "1"),

    /** 2:商品詳細 ※ラベル未使用 */
    GOODS_DETAIL("", "2"),

    /** 4:ニュース詳細 ※ラベル未使用 */
    NEWS_DETAIL("", "4"),

    /** 5:特集ページ ※ラベル未使用 */
    SPECIAL_PAGE("", "5"),

    /** 6:ランディングページ ※ラベル未使用 */
    LP_PAGE("", "6"),

    /** 8:アンケート ※ラベル未使用 */
    QUESTIONNAIRE("", "8");

    /**
     * 隠蔽コンストラクタ<br/>
     * 区分値の設定
     *
     * @param ordinal Integer
     * @param name String
     * @param value String
     */

    /** doma用ファクトリメソッド */
    public static HTypeUrlType of(String value) {

        HTypeUrlType hType = EnumTypeUtil.getEnumFromValue(HTypeUrlType.class, value);

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