package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum HTypeNoveltyPresentJudgmentStatus implements EnumType {

    /** 未判定 */
    UNJUDGMENT("未判定", "0"),

    /** 判定済 */
    UDGMENT("判定済み", "1");

    /** doma用ファクトリメソッド */
    public static HTypeNoveltyPresentJudgmentStatus of(String value) {

        HTypeNoveltyPresentJudgmentStatus hType =
                        EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class, value);

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