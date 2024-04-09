package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

/**
 * ノベルティプレゼントパラメーター
 */
@Data
public class NoveltyPresentParam {

    /** ノベルティプレゼント条件Id */
    private Integer noveltyPresentConditionId;

    /** アイテムId */
    private String itemId;

    /** アイテム数量 */
    private Integer itemCount;
}
