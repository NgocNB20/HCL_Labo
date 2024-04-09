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

/**
 * ノベルティ商品フラグ
 *
 * @author Pham Quang Dieu
 */
@Getter
@AllArgsConstructor
public enum HTypeNoveltyGoodsType implements EnumType {

    /**
     * 0:通常商品
     */
    NORMAL_GOODS("通常商品", "0"),

    /**
     * 1:ノベルティ商品
     */
    NOVELTY_GOODS("ノベルティ商品", "1");

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
