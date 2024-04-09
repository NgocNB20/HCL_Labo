/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * アンケート回答形式種別：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeReplyType implements EnumType {

    /** テキストボックス */
    TEXTBOX("テキストボックス", "0"),

    /** テキストエリア */
    TEXTAREA("テキストエリア", "1"),

    /** ラジオボタン */
    RADIOBUTTON("ラジオボタン", "2"),

    /** プルダウン */
    PULLDOWN("プルダウン", "3"),

    /** チェックボックス */
    CHECKBOX("チェックボックス", "4");

    /**
     * 形式（replyType）が「テキストボックス/テキストエリア」であるかチェック<br />
     *
     * @return 「テキストボックス/テキストエリア」の場合にtrueを返す
     */
    public boolean isTextType() {
        return HTypeReplyType.TEXTBOX.equals(this) || HTypeReplyType.TEXTAREA.equals(this);
    }

    /**
     * 形式（replyType）が「ラジオボタン/プルダウン/チェックボックス」であるかチェック<br />
     *
     * @return 「ラジオボタン/プルダウン/チェックボックス」の場合にtrueを返す
     */
    public boolean isSelectType() {
        return HTypeReplyType.RADIOBUTTON.equals(this) || HTypeReplyType.PULLDOWN.equals(this)
               || HTypeReplyType.CHECKBOX.equals(this);
    }

    /** doma用ファクトリメソッド */
    public static HTypeReplyType of(String value) {

        HTypeReplyType hType = EnumTypeUtil.getEnumFromValue(HTypeReplyType.class, value);

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