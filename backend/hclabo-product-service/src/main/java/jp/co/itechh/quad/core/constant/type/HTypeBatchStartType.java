package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * バッチ起動種別
 *
 * @author Doan Thang (VJP)
 */
@Getter
@AllArgsConstructor
public enum HTypeBatchStartType implements EnumType {
    /** 手動起動 */
    MANUAL("手動", "0"),

    /** スケジューラー起動 */
    SCHEDULER("スケジューラー", "1");

    /** ラベル */
    private final String label;

    /** 区分値 */
    private final String value;
}
