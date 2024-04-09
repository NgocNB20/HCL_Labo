package jp.co.itechh.quad.admin.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HTypeNoveltyPresentJudgmentStatus implements EnumType {

    /** 未判定 */
    UNJUDGMENT("未判定", "0"),

    /** 判定済 */
    UDGMENT("判定済み", "1");

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
