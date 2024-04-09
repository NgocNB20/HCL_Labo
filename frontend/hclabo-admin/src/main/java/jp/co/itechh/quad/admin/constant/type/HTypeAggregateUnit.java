package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 集計単位
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Getter
@AllArgsConstructor
public enum HTypeAggregateUnit implements EnumType {

    /** 月別 */
    MONTH("月別", "0"),

    /** 日別 */
    DAY("日別", "1");

    /** doma用ファクトリメソッド */
    public static HTypeAggregateUnit of(String value) {

        HTypeAggregateUnit hType = EnumTypeUtil.getEnumFromValue(HTypeAggregateUnit.class, value);

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
