/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * アクセス種別
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeAccessType implements EnumType {

    /** 商品アクセス回数 ※ラベル未使用 */
    GOODS_ACCESS_COUNT("", "G001"),

    /** 商品カート投入回数 ※ラベル未使用 */
    GOODS_CART_ADD_COUNT("", "G002"),

    /** 受注回数 ※ラベル未使用 */
    GOODS_ORDER_COUNT("", "G003"),

    /** 受注個数 ※ラベル未使用 */
    GOODS_ORDER_GOODS_COUNT("", "G004"),

    /** 受注金額 ※ラベル未使用 */
    GOODS_ORDER_PRICE("", "G005"),

    /** 会員入会数 ※ラベル未使用 */
    MEMBER_REGIST_COUNT("", "M001"),

    /** 会員退会数 ※ラベル未使用 */
    MEMBER_REMOVE_COUNT("", "M002"),

    /** メールマガジン登録数 ※ラベル未使用 */
    MAIL_MAGAZINE_REGIST_COUNT("", "A001"),

    /** メールマガジン退会数 ※ラベル未使用 */
    MAIL_MAGAZINE_REMOVE_COUNT("", "A002"),

    /** セッション数 ※ラベル未使用 */
    SESSION_COUNT("", "S001"),

    /** トップPV ※ラベル未使用 */
    TOP_PAGE_PV_COUNT("", "T001");

    /**
     * 商品系種別判定<br/>
     *
     * @return true..商品系 false..それ以外
     */
    public boolean isGoodsType() {
        return this.getValue().startsWith("G");
    }

    /**
     * 会員系種別判定<br/>
     *
     * @return true..会員系 false..それ以外
     */
    public boolean isMemberType() {
        return this.getValue().startsWith("M");
    }

    /**
     * メルマガ系種別判定<br/>
     *
     * @return true..メルマガ系 false..それ以外
     */
    public boolean isMailMagaType() {
        return this.getValue().startsWith("A");
    }

    /**
     * セッション系種別判定<br/>
     *
     * @return true..セッション系 false..それ以外
     */
    public boolean isSesstionType() {
        return this.getValue().startsWith("S");
    }

    /**
     * トップ系種別判定<br/>
     *
     * @return true..トップ系 false..それ以外
     */
    public boolean isTopType() {
        return this.getValue().startsWith("T");
    }

    /** doma用ファクトリメソッド */
    public static HTypeAccessType of(String value) {

        HTypeAccessType hType = EnumTypeUtil.getEnumFromValue(HTypeAccessType.class, value);

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
