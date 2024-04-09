package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * デバイス種別（ENUM）
 */
@Getter
@AllArgsConstructor
public enum HTypeDeviceType implements EnumType {
    /**
     * PC
     */
    PC("PC", "0"),

    /**
     * SP
     */
    SP("SP", "1"),

    /**
     * タブレット
     */
    TAB("タブレット", "2");

    /**
     * ラベル
     */
    private final String label;

    /**
     * 区分値
     */
    private final String value;
}
