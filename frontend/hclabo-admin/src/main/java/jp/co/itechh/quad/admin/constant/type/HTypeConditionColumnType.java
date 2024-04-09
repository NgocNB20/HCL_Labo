/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 条件項目
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeConditionColumnType implements EnumType {

    /** すべての項目 */
    ALL("すべての項目", "0"),

    /** 商品管理番号 */
    GOODS_GROUP_SEQ("商品管理番号", "1"),

    /** 商品名 */
    GOOD_NAME("商品名", "2"),

    /** 商品タグ */
    GOOD_TAG("商品タグ", "3"),

    /** アイコン */
    ICON("アイコン", "4"),

    /** 納期 */
    TYPE("納期", "5"),

    /** 価格（税抜） */
    PRICE("価格（税抜）", "6"),

    /** 新着日付 */
    ARRIVAL_DATE("新着日付", "7"),

    /** 販売可能在庫数 */
    SELLABLE_STOCK("販売可能在庫数", "8"),

    /** 規格１表示名 */
    STANDARD_NAME_1("規格１表示名", "9"),

    /** 規格１設定値 */
    STANDARD_VALUE_1("規格１設定値", "10"),

    /** 規格２表示名 */
    STANDARD_NAME_2("規格２表示名", "11"),

    /** 規格２設定値 */
    STANDARD_VALUE_2("規格２設定値", "12");

    /** doma用ファクトリメソッド */
    public static HTypeConditionColumnType of(String value) {

        HTypeConditionColumnType hType = EnumTypeUtil.getEnumFromValue(HTypeConditionColumnType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /** ラベル */
    private final String label;

    /** 区分値 */
    private final String value;
}