package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

/**
 * クーポン 値オブジェクト
 */
@Data
public class AdjustmentAmount {

    /**
     * 調整項目名
     */
    private String adjustName;

    /**
     * 調整金額
     */
    private int adjustPrice;
}
