package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 受注状態(ENUM) 値オブジェクト
 */
@Getter
@AllArgsConstructor
public enum HTypeOrderStatusDdd implements EnumType {

    /**
     * 入金待ち
     */
    PAYMENT_CONFIRMING("入金待ち", "0"),

    /**
     * 商品準備中
     */
    ITEM_PREPARING("商品準備中", "1"),

    /**
     * 出荷完了
     */
    SHIPMENT_COMPLETION("出荷完了", "3"),

    /**
     * キャンセル
     */
    CANCEL("キャンセル", "5"),

    /**
     * 請求決済エラー
     */
    PAYMENT_ERROR("請求決済エラー", "7"),

    /**
     * キャンセル以外
     */
    OTHER("キャンセル以外", "8");

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
