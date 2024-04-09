package jp.co.itechh.quad.core.dto.csv;

import lombok.Data;

/**
 * オプションコンテンツDTO
 */
@Data
public class OptionContentDto {
    /**
     * アイテム名
     */
    private String itemName;

    /**
     * デフォルトの出力順序
     */
    private int defaultOrder;

    /**
     * 出力順序
     */
    private int order;

    /**
     * デフォルカラム名称
     */
    private String defaultColumnLabel;

    /**
     * カラム名称
     */
    private String columnLabel;

    /**
     * 出力フラグ
     */
    private boolean outFlag;
}
