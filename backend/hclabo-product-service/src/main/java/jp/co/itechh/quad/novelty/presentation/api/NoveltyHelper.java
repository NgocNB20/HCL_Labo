/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.novelty.presentation.api;

import jp.co.itechh.quad.core.dto.shop.noveltypresent.NoveltyGoodsCountConditionDto;
import jp.co.itechh.quad.productnovelty.presentation.api.param.NoveltyPresentConditionTargetGoodsCountGetRequest;
import org.springframework.stereotype.Component;

/**
 *
 * ノベルティプレゼント条件Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class NoveltyHelper {

    /**
     * ノベルティプレゼント商品数確認用検索条件に変換<br/>
     *
     * @param noveltyPresentConditionTargetGoodsCountGetRequest ノベルティプレゼント条件の商品数確認リクエスト
     * @return ノベルティプレゼント商品数確認用検索条件
     */
    public NoveltyGoodsCountConditionDto toNoveltyPresentConditionTargetGoodsCountResponse(
                    NoveltyPresentConditionTargetGoodsCountGetRequest noveltyPresentConditionTargetGoodsCountGetRequest) {

        NoveltyGoodsCountConditionDto noveltyGoodsCountConditionDto = new NoveltyGoodsCountConditionDto();
        noveltyGoodsCountConditionDto.setGoodsGroupCodeList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getGoodsGroupCodeList());
        noveltyGoodsCountConditionDto.setGoodsCodeList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getGoodsCodeList());
        noveltyGoodsCountConditionDto.setCategoryIdList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getCategoryIdList());
        noveltyGoodsCountConditionDto.setCategorySeqList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getCategorySeqList());
        noveltyGoodsCountConditionDto.setIconSeqList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getIconSeqList());
        noveltyGoodsCountConditionDto.setGoodsNameList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getGoodsNameList());
        noveltyGoodsCountConditionDto.setSearchKeywordList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getSearchKeywordList());

        return noveltyGoodsCountConditionDto;
    }

}