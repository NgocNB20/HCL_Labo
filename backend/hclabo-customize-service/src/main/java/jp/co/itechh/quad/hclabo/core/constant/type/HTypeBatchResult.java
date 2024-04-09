package jp.co.itechh.quad.hclabo.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * バッチ処理結果
 *
 * @author Doan Thang (VJP)
 */
@Getter
@AllArgsConstructor
public enum HTypeBatchResult implements EnumType {
    /** 正常 */
    COMPLETED("正常終了", "0"),

    /** 異常 */
    FAILED("異常終了", "1");

    /** ラベル */
    private final String label;

    /** 区分値 */
    private final String value;
}
