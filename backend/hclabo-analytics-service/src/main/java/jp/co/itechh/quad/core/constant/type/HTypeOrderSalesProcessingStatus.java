package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 受注データ処理状況 （ENUM）
 */
@Getter
@AllArgsConstructor
public enum HTypeOrderSalesProcessingStatus implements EnumType {

    /**
     * 新規受注
     */
    NEW("新規受注", "0"),

    /**
     * キャンセル
     */
    CANCEL("キャンセル", "1"),

    /**
     * 変更
     */
    UPDATE("変更", "2");

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
