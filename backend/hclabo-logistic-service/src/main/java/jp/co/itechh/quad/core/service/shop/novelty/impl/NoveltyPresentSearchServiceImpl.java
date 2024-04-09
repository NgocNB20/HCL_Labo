/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.novelty.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultGoodsDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentConditionListGetLogic;
import jp.co.itechh.quad.core.service.shop.novelty.NoveltyPresentSearchService;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailByGoodCodeGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ノベルティプレゼント条件検索サービス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Service
public class NoveltyPresentSearchServiceImpl implements NoveltyPresentSearchService {

    /** ノベルティプレゼント条件検索Logic */
    public final NoveltyPresentConditionListGetLogic noveltyPresentConditionListGetLogic;

    /**
     * 商品API
     */
    public final ProductApi productApi;

    @Autowired
    public NoveltyPresentSearchServiceImpl(NoveltyPresentConditionListGetLogic noveltyPresentConditionListGetLogic,
                                           ProductApi productApi) {
        this.noveltyPresentConditionListGetLogic = noveltyPresentConditionListGetLogic;
        this.productApi = productApi;
    }

    /**
     * 検索実行
     *
     * @param conditionDto 検索条件DTO
     * @return 検索結果
     */
    @Override
    public List<NoveltyPresentSearchResultDto> execute(NoveltyPresentSearchForDaoConditionDto conditionDto) {

        if (conditionDto != null && conditionDto.getNoveltyPresentGoodsCode() != null) {
            conditionDto.setNoveltyGoodsSeq(getGoodsSeqFromProductApi(conditionDto.getNoveltyPresentGoodsCode()));
        }

        // 検索実行
        List<NoveltyPresentConditionEntity> resultList = noveltyPresentConditionListGetLogic.execute(conditionDto);

        // ノベルティプレゼント条件SEQごとの商品情報格納用
        Map<Integer, List<NoveltyPresentSearchResultGoodsDto>> noveltyGoodsInfoMap = new HashMap<>();

        // ノベルティプレゼント条件SEQのListを作成
        List<Integer> seqList = new ArrayList<Integer>();
        for (NoveltyPresentConditionEntity dto : resultList) {
            seqList.add(dto.getNoveltyPresentConditionSeq());
        }

        if (0 < seqList.size()) {
            // ノベルティプレゼント商品を検索
            List<NoveltyPresentSearchResultGoodsDto> goodsList =
                            noveltyPresentConditionListGetLogic.getGoodsList(seqList);

            // 商品サービスから商品詳細リストを取得
            ProductDetailListResponse productDetailListResponse = getGoodsDetailListFromProductApi(goodsList);

            // ノベルティプレゼント条件SEQごとに商品リストを仕分け（仕分けしたMapを生成する）
            noveltyGoodsInfoMap = createNoveltyGoodsInfoMap(goodsList, productDetailListResponse);
        }

        // 返却用のListを生成
        List<NoveltyPresentSearchResultDto> retList = new ArrayList<NoveltyPresentSearchResultDto>();
        for (NoveltyPresentConditionEntity entity : resultList) {
            // 戻り値Dtoを生成
            NoveltyPresentSearchResultDto resultDto =
                            ApplicationContextUtility.getBean(NoveltyPresentSearchResultDto.class);

            // Entityをセット
            resultDto.setNoveltyPresentConditionEntity(entity);

            // ノベルティ条件にひもづく、ノベルティ商品リストをセット
            Integer seq = entity.getNoveltyPresentConditionSeq();
            if (noveltyGoodsInfoMap.containsKey(seq)) {
                resultDto.setNoveltyGoodsList(noveltyGoodsInfoMap.get(seq));
            }
            retList.add(resultDto);
        }

        return retList;
    }

    /**
     * ノベルティ商品リストのマップを生成<br/>
     * <pre>
     * KEY  ：ノベルティプレゼント条件SEQ
     * VALUE：KEYのノベルティプレゼント条件にひもづく、ノベルティ商品のリスト
     * という構成のマップ。
     *
     * VALUEのノベルティ商品リストは、
     * メソッド第一引数のノベルティ商品リストをベースに、商品名などの詳細情報を第二引数から補填したものが
     * 格納される
     *
     * </pre>
     * @param goodsList ノベルティ商品リスト
     * @param productDetailListResponse 商品詳細リストレスポンス
     */
    private Map<Integer, List<NoveltyPresentSearchResultGoodsDto>> createNoveltyGoodsInfoMap(List<NoveltyPresentSearchResultGoodsDto> goodsList,
                                                                                             ProductDetailListResponse productDetailListResponse) {
        Map<Integer, List<NoveltyPresentSearchResultGoodsDto>> noveltyGoodsInfoMap = new HashMap<>();

        for (NoveltyPresentSearchResultGoodsDto noveltyGoodsDto : goodsList) {
            // ノベルティプレゼント条件を取得
            Integer noveltyPresentConditionSeq = noveltyGoodsDto.getNoveltyPresentConditionSeq();

            // 戻り値用にDtoを新規生成

            // 商品詳細レスポンスを取得
            GoodsDetailsResponse goodsDetailsResponse =
                            getGoodsDetailsResponse(noveltyGoodsDto.getGoodsSeq(), productDetailListResponse);
            if (goodsDetailsResponse != null) {
                // 商品名などの情報をセット
                noveltyGoodsDto.setNoveltyGoodsName(goodsDetailsResponse.getGoodsGroupName());
                noveltyGoodsDto.setUnitValue1(goodsDetailsResponse.getUnitValue1());
                noveltyGoodsDto.setUnitValue2(goodsDetailsResponse.getUnitValue2());

                // MapからListを引当
                List<NoveltyPresentSearchResultGoodsDto> listForMap =
                                noveltyGoodsInfoMap.get(noveltyPresentConditionSeq);
                if (listForMap == null) {
                    listForMap = new ArrayList<>();
                }
                listForMap.add(noveltyGoodsDto);
                noveltyGoodsInfoMap.put(noveltyPresentConditionSeq, listForMap);
            }
        }

        return noveltyGoodsInfoMap;
    }

    /**
     * ノベルティ商品かどうか判定
     * @param goodsDetailsResponse 商品詳細レスポンス
     * @return true...ノベルティ商品である
     */
    private boolean isNoveltyGoods(GoodsDetailsResponse goodsDetailsResponse) {
        // ノベルティ商品の前提条件に該当するか確認

        // ノベルティ商品フラグ=ON　でなければ、ノベルティではない
        if (!StringUtils.equals(
                        HTypeNoveltyGoodsType.NOVELTY_GOODS.getValue(), goodsDetailsResponse.getNoveltyGoodsType())) {
            return false;
        }
        // 非公開　でなければ、ノベルティではない
        if (!StringUtils.equals(HTypeOpenDeleteStatus.NO_OPEN.getValue(), goodsDetailsResponse.getGoodsOpenStatus())) {
            return false;
        }
        // 非販売　でなければ、ノベルティではない
        if (!StringUtils.equals(HTypeGoodsSaleStatus.NO_SALE.getValue(), goodsDetailsResponse.getSaleStatus())) {
            return false;
        }

        // 該当条件にあてはまらない場合はノベルティである（trueを返却）
        return true;
    }

    /**
     * 商品詳細リストレスポンスから商品詳細を取得
     * @param goodsSeq 商品SEQ
     * @param productDetailListResponse 商品詳細リストレスポンス
     * @return 商品詳細レスポンス
     */
    private GoodsDetailsResponse getGoodsDetailsResponse(Integer goodsSeq,
                                                         ProductDetailListResponse productDetailListResponse) {
        // 商品詳細リストの件数分ループ
        for (GoodsDetailsResponse goodsDetailsResponse : productDetailListResponse.getGoodsDetailsList()) {
            // ノベルティ商品にあてはまる条件でなければ、後続処理を実施しない
            if (!isNoveltyGoods(goodsDetailsResponse)) {
                continue;
            }
            // 商品SEQの一致する商品詳細レスポンスを返却
            if (goodsDetailsResponse.getGoodsSeq() != null && goodsDetailsResponse.getGoodsSeq().equals(goodsSeq)) {
                return goodsDetailsResponse;
            }
        }
        return null;
    }

    /**
     * 商品サービスに商品コードで問合せ、商品SEQを取得する
     *
     * @param goodsCode ノベルティ商品番号
     * @return 商品SEQ
     */
    private Integer getGoodsSeqFromProductApi(String goodsCode) {
        // APIパラメータ生成
        ProductDetailByGoodCodeGetRequest productDetailByGoodCodeGetRequest = new ProductDetailByGoodCodeGetRequest();
        productDetailByGoodCodeGetRequest.setGoodsCode(goodsCode);

        // 商品サービスに問合せ
        GoodsDetailsResponse goodsDetailsResponse =
                        productApi.getDetailsByGoodsCode(goodsCode, productDetailByGoodCodeGetRequest);

        // APIレスポンスの有無を判定
        if (!ObjectUtils.isEmpty(goodsDetailsResponse) && goodsDetailsResponse.getGoodsSeq() != null) {
            // レスポンスがあれば商品SEQを返却
            return goodsDetailsResponse.getGoodsSeq();
        }

        return null;
    }

    /**
     * 商品サービスから商品詳細リストを取得
     *
     * @param goodsList ノベルティ商品リスト
     * @return 商品詳細リスト
     */
    private ProductDetailListResponse getGoodsDetailListFromProductApi(List<NoveltyPresentSearchResultGoodsDto> goodsList) {
        // 商品SEQリスト組み立て
        List<Integer> goodsSeqList = new ArrayList<Integer>();
        for (NoveltyPresentSearchResultGoodsDto dto : goodsList) {
            if (!goodsSeqList.contains(dto.getGoodsSeq())) {
                goodsSeqList.add(dto.getGoodsSeq());
            }
        }

        // APIリクエストに商品SEQをセット
        ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
        productDetailListGetRequest.setGoodsSeqList(goodsSeqList);

        // ページングパラメータについては、特に何も指定なし
        PageInfoRequest pageInfoRequest = new PageInfoRequest();

        // 商品サービスから商品詳細リストを取得
        return productApi.getDetails(productDetailListGetRequest, pageInfoRequest);
    }
}