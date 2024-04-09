/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.INoveltyAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.NoveltyPresentMemberRegistParam;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.NoveltyPresentParam;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.novelty.presentation.api.LogisticNoveltyApi;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionJudgmentRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentMemberRegist;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentMemberRegistRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAdd;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAddJudgmentListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ノベルティアダプター実装クラス
 *
 * @author Doan Thang (VJP)
 */
@Component
public class NoveltyAdapterImpl implements INoveltyAdapter {

    /** ノベルティAPI */
    private final LogisticNoveltyApi logisticNoveltyApi;

    /** コンストラクタ */
    @Autowired
    public NoveltyAdapterImpl(LogisticNoveltyApi logisticNoveltyApi, HeaderParamsUtility headerParamsUtil) {
        this.logisticNoveltyApi = logisticNoveltyApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.logisticNoveltyApi.getApiClient());
    }

    /**
     * ノベルティ商品追加一覧取得
     *
     * @param transactionEntity   取引エンティティ
     * @param itemSalesPriceTotal 商品販売金額合計
     * @return ノベルティ商品追加一覧
     */
    @Override
    public List<NoveltyPresentParam> noveltyPresentConditionJudgment(TransactionEntity transactionEntity,
                                                                     Integer itemSalesPriceTotal) {

        NoveltyPresentConditionJudgmentRequest request = new NoveltyPresentConditionJudgmentRequest();
        request.setTransactionId(transactionEntity.getTransactionId().getValue());
        request.setRegistTime(transactionEntity.getRegistDate());
        request.setItemSalesPriceTotal(itemSalesPriceTotal);
        request.setMemberInfoSeq(Integer.valueOf(transactionEntity.getCustomerId()));

        NoveltyProductAddJudgmentListResponse response = logisticNoveltyApi.noveltyPresentConditionJudgment(request);
        if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getNoveltyProductAddList())) {
            return null;
        } else {
            List<NoveltyPresentParam> noveltyPresentParamList = new ArrayList<>();
            for (NoveltyProductAdd noveltyProductAdd : response.getNoveltyProductAddList()) {
                NoveltyPresentParam noveltyPresentParam = new NoveltyPresentParam();
                noveltyPresentParam.setNoveltyPresentConditionId(noveltyProductAdd.getNoveltyPresentConditionId());
                noveltyPresentParam.setItemId(noveltyProductAdd.getItemId());
                noveltyPresentParam.setItemCount(noveltyProductAdd.getItemCount());
                noveltyPresentParamList.add(noveltyPresentParam);
            }
            return noveltyPresentParamList;
        }
    }

    /**
     * ノベルティプレゼント対象会員登録
     *
     * @param noveltyPresentMemberRegistParamList ノベルティプレゼント対象会員登録パラメーター一覧
     */
    @Override
    public void noveltyPresentMemberRegist(List<NoveltyPresentMemberRegistParam> noveltyPresentMemberRegistParamList) {
        NoveltyPresentMemberRegistRequest request = new NoveltyPresentMemberRegistRequest();
        List<NoveltyPresentMemberRegist> registList = new ArrayList<>();
        for (NoveltyPresentMemberRegistParam param : noveltyPresentMemberRegistParamList) {
            NoveltyPresentMemberRegist regist = new NoveltyPresentMemberRegist();
            regist.setNoveltyPresentConditionId(param.getNoveltyPresentConditionId());
            regist.setOrderreceivedId(param.getOrderreceivedId());
            regist.setItemId(param.getItemId());
            regist.setMemberInfoSeq(param.getMemberInfoSeq());
            registList.add(regist);
        }
        request.setNoveltyPresentMemberRegistList(registList);

        logisticNoveltyApi.noveltyPresentMemberRegist(request);
    }
}