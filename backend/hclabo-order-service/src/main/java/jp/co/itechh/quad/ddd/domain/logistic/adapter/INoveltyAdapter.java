/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.NoveltyPresentMemberRegistParam;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.NoveltyPresentParam;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;

import java.util.List;

/**
 * ノベルティアダプター<br/>
 * ※物流マイクロサービス
 */
public interface INoveltyAdapter {

    /**
     * ノベルティ商品追加一覧取得
     *
     * @param transactionEntity 取引エンティティ
     * @param itemSalesPriceTotal 商品販売金額合計
     *
     * @return ノベルティ商品追加一覧
     */
    List<NoveltyPresentParam> noveltyPresentConditionJudgment(TransactionEntity transactionEntity,
                                                              Integer itemSalesPriceTotal);

    /**
     * ノベルティプレゼント対象会員登録
     *
     * @param noveltyPresentMemberRegistParamList ノベルティプレゼント対象会員登録パラメーター一覧
     */
    void noveltyPresentMemberRegist(List<NoveltyPresentMemberRegistParam> noveltyPresentMemberRegistParamList);
}