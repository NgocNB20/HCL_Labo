/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSearchResultForOrderRegistResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductOrderItemListGetRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 「受注修正：商品検索」画面helper
 */
@Component
public class GoodsSearchHelper {

    /**
     * ProductOrderItemListGetRequestへの変換処理<br />
     *
     * @param goodsSearchModel 自画面Model
     * @return ProductOrderItemListGetRequest
     */
    public ProductOrderItemListGetRequest toGoodsSearchForBackDaoConditionDtoForGoodsSearchAjax(GoodsSearchModel goodsSearchModel) {

        ProductOrderItemListGetRequest productOrderItemListGetRequest = new ProductOrderItemListGetRequest();

        /* 画面条件 */
        // サイト
        productOrderItemListGetRequest.setSite(goodsSearchModel.getSite());

        // キーワード
        if (goodsSearchModel.getSearchKeyword() != null) {
            String[] searchKeywordArray = goodsSearchModel.getSearchKeyword().split("[\\s|　]+");

            for (int i = 0; i < searchKeywordArray.length; i++) {
                switch (i) {
                    case 0:
                        productOrderItemListGetRequest.setKeywordLikeCondition1(searchKeywordArray[i].trim());
                        break;
                    case 1:
                        productOrderItemListGetRequest.setKeywordLikeCondition2(searchKeywordArray[i].trim());
                        break;
                    case 2:
                        productOrderItemListGetRequest.setKeywordLikeCondition3(searchKeywordArray[i].trim());
                        break;
                    case 3:
                        productOrderItemListGetRequest.setKeywordLikeCondition4(searchKeywordArray[i].trim());
                        break;
                    case 4:
                        productOrderItemListGetRequest.setKeywordLikeCondition5(searchKeywordArray[i].trim());
                        break;
                    case 5:
                        productOrderItemListGetRequest.setKeywordLikeCondition6(searchKeywordArray[i].trim());
                        break;
                    case 6:
                        productOrderItemListGetRequest.setKeywordLikeCondition7(searchKeywordArray[i].trim());
                        break;
                    case 7:
                        productOrderItemListGetRequest.setKeywordLikeCondition8(searchKeywordArray[i].trim());
                        break;
                    case 8:
                        productOrderItemListGetRequest.setKeywordLikeCondition9(searchKeywordArray[i].trim());
                        break;
                    case 9:
                        productOrderItemListGetRequest.setKeywordLikeCondition10(searchKeywordArray[i].trim());
                        break;
                    default:
                        // この処理は到達しない
                }
            }
        }

        // 商品グループコード
        productOrderItemListGetRequest.setGoodsGroupCode(goodsSearchModel.getSearchGoodsGroupCode());
        // 商品コード
        productOrderItemListGetRequest.setGoodsCode(goodsSearchModel.getSearchGoodsCode());
        // JAN/カタログコード
        productOrderItemListGetRequest.setJanCode(goodsSearchModel.getSearchJanCode());
        // 商品名
        productOrderItemListGetRequest.setGoodsGroupName(goodsSearchModel.getSearchGoodsGroupName());

        return productOrderItemListGetRequest;
    }

    /**
     * Pageへの変換処理。<br />
     * 商品検索結果Dtoのリスト ⇒ Pageの検索結果リスト<br />
     *
     * @param goodsSearchResultDtoList 検索結果リスト
     * @param startIndexNo
     * @return List<GoodsSearchModelItem>
     */
    public List<GoodsSearchModelItem> toPageForSearch(List<GoodsSearchResultForOrderRegistResponse> goodsSearchResultDtoList,
                                                      int startIndexNo) {

        List<GoodsSearchModelItem> resultItemList = new ArrayList<>();

        // オフセット + 1をNoにセット
        for (GoodsSearchResultForOrderRegistResponse goodsSearchResultForOrderRegistResponse : goodsSearchResultDtoList) {

            GoodsSearchModelItem goodssearchPageItem = ApplicationContextUtility.getBean(GoodsSearchModelItem.class);

            goodssearchPageItem.setResultGoodsSearchResultResponse(goodsSearchResultForOrderRegistResponse);
            goodssearchPageItem.setResultNo(startIndexNo);
            goodssearchPageItem.setGoodsGroupCode(goodsSearchResultForOrderRegistResponse.getGoodsGroupCode());
            goodssearchPageItem.setResultGoodsCode(goodsSearchResultForOrderRegistResponse.getGoodsCode());
            goodssearchPageItem.setResultJanCode(goodsSearchResultForOrderRegistResponse.getJanCode());
            goodssearchPageItem.setResultGoodsGroupName(goodsSearchResultForOrderRegistResponse.getGoodsGroupName());
            goodssearchPageItem.setResultUnitValue1(goodsSearchResultForOrderRegistResponse.getUnitValue1());
            goodssearchPageItem.setResultUnitValue2(goodsSearchResultForOrderRegistResponse.getUnitValue2());
            goodssearchPageItem.setResultGoodsPrice(goodsSearchResultForOrderRegistResponse.getGoodsPrice());
            goodssearchPageItem.setResultIndividualDeliveryType(
                            goodsSearchResultForOrderRegistResponse.getIndividualDeliveryType());
            goodssearchPageItem.setResultStockManagementFlag(
                            goodsSearchResultForOrderRegistResponse.getStockManagementFlag());
            goodssearchPageItem.setResultSalesPossibleStock(
                            goodsSearchResultForOrderRegistResponse.getSalesPossibleStock());
            // 商品SEQ
            goodssearchPageItem.setGoodsSeq(goodsSearchResultForOrderRegistResponse.getGoodsSeq());

            resultItemList.add(goodssearchPageItem);
            startIndexNo++;
        }

        return resultItemList;
    }

    /**
     * 注文商品IDリストへの変換処理。
     *
     * @param pageItems 商品検索アイテムリスト
     * @return 注文商品Idリスト
     */
    public List<String> toOrderItemSeqList(List<GoodsSearchModelItem> pageItems) {

        // 戻り値
        List<String> orderItemIdList = new ArrayList<>();

        // 検索一覧分ループ
        if (CollectionUtils.isNotEmpty(pageItems)) {
            for (GoodsSearchModelItem goodsSearchModelItem : pageItems) {
                // チェック有の場合
                if (goodsSearchModelItem.isResultGoodsCheck()) {
                    // リストに追加
                    orderItemIdList.add(String.valueOf(goodsSearchModelItem.getGoodsSeq()));
                }
            }
        }

        // リストを返す
        return orderItemIdList;
    }
}