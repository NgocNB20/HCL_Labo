/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateHelper;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 商品管理：商品登録更新（関連商品設定検索）ページHelper<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class GoodsRegistUpdateRelationSearchHelper extends AbstractGoodsRegistUpdateHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     */
    @Autowired
    public GoodsRegistUpdateRelationSearchHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param relationsearchModel ページ
     */
    public void toPageForLoad(GoodsRegistUpdateRelationSearchModel relationsearchModel) {
        if (relationsearchModel.getTmpGoodsRelationEntityList() == null
            && relationsearchModel.getRedirectGoodsRelationEntityList() != null) {
            relationsearchModel.setTmpGoodsRelationEntityList(relationsearchModel.getRedirectGoodsRelationEntityList());
        }
    }

    /**
     * 入力情報をページに反映<br/>
     *
     * @param relationsearchModel ページ
     */
    public void toPageForNext(GoodsRegistUpdateRelationSearchModel relationsearchModel) {
        relationsearchModel.setRedirectGoodsRelationEntityList(relationsearchModel.getTmpGoodsRelationEntityList());
    }

    /**
     * 入力情報をページに反映（商品登録更新の他ページへ遷移時）<br/>
     *
     * @param relationsearchModel ページ
     */
    public void toPageForOther(GoodsRegistUpdateRelationSearchModel relationsearchModel) {
        // tmp関連商品エンティティリストをセッションにセット
        relationsearchModel.setGoodsRelationEntityList(relationsearchModel.getTmpGoodsRelationEntityList());
    }

    /**
     * 関連商品情報追加<br/>
     *
     * @param relationsearchModel ページ
     */
    public void toPageForAddRelationGoods(GoodsRegistUpdateRelationSearchModel relationsearchModel) {
        if (relationsearchModel.getGoodsGroupDto() == null
            || relationsearchModel.getGoodsGroupDto().getGoodsGroupEntity() == null
            || relationsearchModel.getTmpGoodsRelationEntityList() == null
            || relationsearchModel.getResultItems() == null) {
            return;
        }

        for (Iterator<GoodsRegistUpdateRelationSearchItem> it =
             relationsearchModel.getResultItems().iterator(); it.hasNext(); ) {
            GoodsRegistUpdateRelationSearchItem item = it.next();
            if (!item.isResultCheck()) {
                continue;
            }

            // 関連商品エンティティを生成してリストにセット
            GoodsRelationEntity goodsRelationEntity = ApplicationContextUtility.getBean(GoodsRelationEntity.class);
            goodsRelationEntity.setGoodsGroupSeq(
                            relationsearchModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq());
            goodsRelationEntity.setGoodsRelationGroupSeq(item.getResultGoodsGroupSeq());
            goodsRelationEntity.setOrderDisplay(relationsearchModel.getTmpGoodsRelationEntityList().size() + 1);
            goodsRelationEntity.setGoodsGroupCode(item.getResultGoodsGroupCode());
            goodsRelationEntity.setGoodsGroupName(item.getResultGoodsGroupName());
            goodsRelationEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   item.getResultGoodsOpenStatusPC()
                                                                                  ));

            // 関連商品DTOをページのtmp関連商品リストに追加
            relationsearchModel.getTmpGoodsRelationEntityList().add(goodsRelationEntity);
        }

        // 関連商品ページへのRedirectScope変数にセット
        relationsearchModel.setRedirectGoodsRelationEntityList(relationsearchModel.getTmpGoodsRelationEntityList());
    }

    /**
     * 検索条件の作成<br/>
     *
     * @param relationsearchModel 関連商品ページ
     * @return 商品グループ検索条件Dto
     */
    public GoodsSearchForBackDaoConditionDto toGoodsGroupSearchForDaoConditionDtoForSearch(
                    GoodsRegistUpdateRelationSearchModel relationsearchModel) {
        GoodsSearchForBackDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(GoodsSearchForBackDaoConditionDto.class);

        /* 画面条件 */
        // キーワード
        if (relationsearchModel.getSearchRelationGoodsKeyword() != null) {
            String[] searchKeywordArray = relationsearchModel.getSearchRelationGoodsKeyword().split("[\\s|　]+");

            for (int i = 0; i < searchKeywordArray.length; i++) {
                switch (i) {
                    case 0:
                        conditionDto.setKeywordLikeCondition1(searchKeywordArray[i].trim());
                        break;
                    case 1:
                        conditionDto.setKeywordLikeCondition2(searchKeywordArray[i].trim());
                        break;
                    case 2:
                        conditionDto.setKeywordLikeCondition3(searchKeywordArray[i].trim());
                        break;
                    case 3:
                        conditionDto.setKeywordLikeCondition4(searchKeywordArray[i].trim());
                        break;
                    case 4:
                        conditionDto.setKeywordLikeCondition5(searchKeywordArray[i].trim());
                        break;
                    case 5:
                        conditionDto.setKeywordLikeCondition6(searchKeywordArray[i].trim());
                        break;
                    case 6:
                        conditionDto.setKeywordLikeCondition7(searchKeywordArray[i].trim());
                        break;
                    case 7:
                        conditionDto.setKeywordLikeCondition8(searchKeywordArray[i].trim());
                        break;
                    case 8:
                        conditionDto.setKeywordLikeCondition9(searchKeywordArray[i].trim());
                        break;
                    case 9:
                        conditionDto.setKeywordLikeCondition10(searchKeywordArray[i].trim());
                        break;
                    default:
                        break;
                }
            }
        }

        // カテゴリIDリストーパス
        if (relationsearchModel.getSearchCategoryIdList() != null) {
            List<String> searchCategoryIdList = new ArrayList<>();
            for (CategoryEntity categoryEntity : relationsearchModel.getSearchCategoryIdList()) {
                searchCategoryIdList.add(categoryEntity.getCategoryId());
            }
            conditionDto.setCategoryIdList(searchCategoryIdList);
        }

        // 商品グループコード
        conditionDto.setGoodsGroupCode(relationsearchModel.getSearchGoodsGroupCode());

        // 商品名
        conditionDto.setGoodsGroupName(relationsearchModel.getSearchGoodsGroupName());

        // サイト種別：PCとモバイル
        conditionDto.setSite("0");

        // ページ情報 limit offset order
        //        conditionDto.setPageInfo(relationsearchModel.getPageInfo());

        conditionDto.setRelationGoodsSearchFlag(true);
        return conditionDto;
    }

    /**
     * 検索結果をページに反映<br/>
     *
     * @param resultDtoList       検索結果リスト
     * @param relationsearchModel ページ
     */
    public void toPageForSearch(List<GoodsSearchResultDto> resultDtoList,
                                GoodsRegistUpdateRelationSearchModel relationsearchModel,
                                GoodsSearchForBackDaoConditionDto conditionDto) {
        // オフセット + 1をNoにセット
        int index = conditionDto.getOffset() + 1;
        List<GoodsRegistUpdateRelationSearchItem> resultItemList = new ArrayList<>();
        for (GoodsSearchResultDto resultDto : resultDtoList) {
            GoodsRegistUpdateRelationSearchItem resultItem =
                            ApplicationContextUtility.getBean(GoodsRegistUpdateRelationSearchItem.class);
            resultItem.setResultDspNo(index++);
            resultItem.setResultGoodsGroupSeq(resultDto.getGoodsGroupSeq());
            resultItem.setResultGoodsGroupCode(resultDto.getGoodsGroupCode());
            resultItem.setResultGoodsGroupName(resultDto.getGoodsGroupName());
            resultItem.setResultGoodsOpenStatusPC(resultDto.getGoodsOpenStatusPC().getValue());
            resultItemList.add(resultItem);
        }
        relationsearchModel.setResultItems(resultItemList);
    }

    /**
     * 検索条件の作成<br/>
     *
     * @return 商品Dao用検索条件Dto
     */
    public ProductItemListGetRequest toProductItemListGetRequest(GoodsSearchForBackDaoConditionDto conditionDto) {

        ProductItemListGetRequest request = new ProductItemListGetRequest();

        request.setCategoryIdList(conditionDto.getCategoryIdList());
        request.setSearchGoodsGroupCode(conditionDto.getGoodsGroupCode());
        request.setSearchGoodsCode(conditionDto.getGoodsCode());
        request.setSearchJanCode(conditionDto.getJanCode());
        request.setSearchMinSalesPossibleStockCount(null);
        request.setSearchMaxSalesPossibleStockCount(null);
        request.setSearchGoodsGroupName(conditionDto.getGoodsGroupName());
        request.setSite(conditionDto.getSite());
        // 公開と非公開のみ設定
        request.setGoodsOpenStatusArray(
                        Arrays.asList(HTypeOpenDeleteStatus.OPEN.getValue(), HTypeOpenDeleteStatus.NO_OPEN.getValue()));
        request.setGoodsSaleStatusArray(conditionDto.getSaleStatusList());

        return request;
    }

    public List<GoodsSearchResultDto> toGoodsSearchResultDto(List<GoodsDetailsResponse> responseList) {
        List<GoodsSearchResultDto> goodsSearchResultDtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(responseList)) {
            responseList.forEach(item -> {
                GoodsSearchResultDto goodsSearchResultDto = new GoodsSearchResultDto();
                goodsSearchResultDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsSearchResultDto.setGoodsSeq(item.getGoodsSeq());
                goodsSearchResultDto.setGoodsGroupCode(item.getGoodsGroupCode());
                goodsSearchResultDto.setGoodsOpenStatusPC(
                                EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class, item.getGoodsOpenStatus()));
                goodsSearchResultDto.setOpenStartTimePC(conversionUtility.toTimestamp(item.getOpenStartTime()));
                goodsSearchResultDto.setOpenEndTimePC(conversionUtility.toTimestamp(item.getOpenStartTime()));
                goodsSearchResultDto.setGoodsGroupName(item.getGoodsGroupName());
                goodsSearchResultDto.setGoodsCode(item.getGoodsCode());
                goodsSearchResultDto.setSaleStatusPC(
                                EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class, item.getSaleStatus()));
                goodsSearchResultDto.setSaleStartTimePC(conversionUtility.toTimestamp(item.getSaleStartTime()));
                goodsSearchResultDto.setSaleEndTimePC(conversionUtility.toTimestamp(item.getSaleEndTime()));
                goodsSearchResultDto.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                         item.getUnitManagementFlag()
                                                                                        ));
                goodsSearchResultDto.setUnitValue1(item.getUnitValue1());
                goodsSearchResultDto.setUnitValue2(item.getUnitValue1());
                goodsSearchResultDto.setGoodsPrice(item.getGoodsPrice());
                goodsSearchResultDto.setStockmanagementflag(
                                EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                              item.getStockManagementFlag()
                                                             ));
                goodsSearchResultDto.setSalesPossibleStock(item.getSalesPossibleStock());
                goodsSearchResultDto.setRealStock(item.getRealStock());
                goodsSearchResultDto.setIndividualDeliveryType(
                                EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                              item.getIndividualDeliveryType()
                                                             ));

                goodsSearchResultDtoList.add(goodsSearchResultDto);
            });
        }

        return goodsSearchResultDtoList;
    }
}