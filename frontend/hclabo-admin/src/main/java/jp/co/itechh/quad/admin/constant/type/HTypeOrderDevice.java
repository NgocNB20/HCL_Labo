package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HTypeOrderDevice implements EnumType {

    /** PC */
    PC("PC", "0"),

    /** SP */
    SP("SP", "1"),

    /** タブレット */
    TABLET("タブレット", "2");

    /** doma用ファクトリメソッド */
    public static HTypeOrderDevice of(String value) {

        HTypeOrderDevice hType = EnumTypeUtil.getEnumFromValue(HTypeOrderDevice.class, value);

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
