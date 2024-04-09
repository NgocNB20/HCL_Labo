package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

/**
 * ノベルティプレゼント対象会員登録パラメーター
 */
@Data
public class NoveltyPresentMemberRegistParam {

    /** ノベルティプレゼント条件Id */
    private Integer noveltyPresentConditionId;

    /** 受注ID */
    private String orderreceivedId;

    /** アイテムId */
    private String itemId;

    /** 会員Id */
    private Integer memberInfoSeq;
}
