/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.multipleCategory.presentation.api;

import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.core.dto.multipleCategory.ajax.MultipleCategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.logic.category.MultipleCategoryLogic;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.CategoryDataResponse;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.MultipleCategoryGoodsDetailsResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * マルチカテゴリ Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class MultipleCategoryHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleCategoryHelper.class);

    /**
     * To multiple category map response.
     *
     * @param multipleCategoryMap Map as categoryId as key and list of GoodsGroupDto as values
     * @return マルチカテゴリ一覧レスポンス
     */
    public Map<String, List<MultipleCategoryGoodsDetailsResponse>> toMultipleCategoryMapResponse(Map<String, List<MultipleCategoryGoodsDetailsDto>> multipleCategoryMap) {

        Map<String, List<MultipleCategoryGoodsDetailsResponse>> multipleCategoryList = new HashMap<>();
        List<String> categoryIdList = new ArrayList<>(multipleCategoryMap.keySet());

        categoryIdList.forEach(id -> {
            List<MultipleCategoryGoodsDetailsDto> multipleCategoryGoodsDetailsDtos = multipleCategoryMap.get(id);
            List<MultipleCategoryGoodsDetailsResponse> multipleCategoryGoodsDetailsResponses = new ArrayList<>();

            for (MultipleCategoryGoodsDetailsDto i : multipleCategoryGoodsDetailsDtos) {

                MultipleCategoryGoodsDetailsResponse multipleCategoryGoodsDetailsResponse =
                                new MultipleCategoryGoodsDetailsResponse();

                multipleCategoryGoodsDetailsResponse.setGoodsGroupSeq(i.getGoodsGroupSeq());
                multipleCategoryGoodsDetailsResponse.setGgcd(i.getGgcd());
                multipleCategoryGoodsDetailsResponse.setHref(i.getHref());
                multipleCategoryGoodsDetailsResponse.setGoodsGroupName(i.getGoodsGroupName());
                multipleCategoryGoodsDetailsResponse.setGoodsGroupImageThumbnail(i.getGoodsGroupImageThumbnail());
                multipleCategoryGoodsDetailsResponse.setGoodsImageItem(i.getGoodsImageItem());
                multipleCategoryGoodsDetailsResponse.setGoodsPrice(i.getGoodsPrice());
                multipleCategoryGoodsDetailsResponse.setGoodsPriceInTax(i.getGoodsPriceInTax());
                multipleCategoryGoodsDetailsResponse.setTaxRate(i.getTaxRate());
                multipleCategoryGoodsDetailsResponse.setGoodsNote1(i.getGoodsNote1());
                multipleCategoryGoodsDetailsResponse.setGoodsTaxType(EnumTypeUtil.getValue(i.getGoodsTaxType()));
                multipleCategoryGoodsDetailsResponse.setWhatsnewDate(i.getWhatsnewDate().toString());
                multipleCategoryGoodsDetailsResponse.setStockStatus(i.getStockStatusPc());
                multipleCategoryGoodsDetailsResponse.setGoodsIconItems(
                                toListMultipleCategoryGoodsDetailsResponse(i.getGoodsIconItems()));
                multipleCategoryGoodsDetailsResponse.setIconName(i.getIconName());
                multipleCategoryGoodsDetailsResponse.setIconColorCode(i.getIconColorCode());
                multipleCategoryGoodsDetailsResponse.setIsStockStatusDisplay(i.isStockStatusDisplay());
                multipleCategoryGoodsDetailsResponse.setIsStockNoSaleDisp(i.isStockNoSaleDisp());
                multipleCategoryGoodsDetailsResponse.setIsStockSoldOutIconDisp(i.isStockSoldOutIconDisp());
                multipleCategoryGoodsDetailsResponse.setIsStockBeforeSaleIconDisp(i.isStockBeforeSaleIconDisp());
                multipleCategoryGoodsDetailsResponse.setIsStockNoStockIconDisp(i.isStockNoStockIconDisp());
                multipleCategoryGoodsDetailsResponse.setIsGoodsGroupImage(i.isGoodsGroupImage());
                multipleCategoryGoodsDetailsResponses.add(multipleCategoryGoodsDetailsResponse);
            }

            multipleCategoryList.put(id, multipleCategoryGoodsDetailsResponses);
        });

        return multipleCategoryList;
    }

    /**
     * To list multiple category goods details response list.
     *
     * @param goodsIconItems 商品アイコンリスト
     * @return マルチカテゴリ一覧レスポンスリスト
     */
    public List<MultipleCategoryGoodsDetailsResponse> toListMultipleCategoryGoodsDetailsResponse(List<MultipleCategoryGoodsDetailsDto> goodsIconItems) {

        List<MultipleCategoryGoodsDetailsResponse> multipleCategoryGoodsDetailsResponses = new ArrayList<>();

        for (MultipleCategoryGoodsDetailsDto i : goodsIconItems) {
            MultipleCategoryGoodsDetailsResponse multipleCategoryGoodsDetailsResponse =
                            new MultipleCategoryGoodsDetailsResponse();
            multipleCategoryGoodsDetailsResponse.setIconName(i.getIconName());
            multipleCategoryGoodsDetailsResponse.setIconColorCode(i.getIconColorCode());
            multipleCategoryGoodsDetailsResponses.add(multipleCategoryGoodsDetailsResponse);
        }

        return multipleCategoryGoodsDetailsResponses;
    }

    /**
     * Method to set MultipleCategory data
     *
     * @param categoryDetailsDtoList categoryDetailsDtoList instance
     * @param categoryString         categoryString
     * @param seqString              seqString
     * @return カテゴリ詳細レスポンス
     */
    protected List<CategoryDataResponse> toDataForLoadMultipleCategory(List<CategoryDetailsDto> categoryDetailsDtoList,
                                                                       String categoryString,
                                                                       String seqString) {
        List<CategoryDataResponse> categoryItemsList = new ArrayList<>();

        Map<String, String> seqMap = new LinkedHashMap<>();
        if (StringUtils.isEmpty(categoryString)) {
            categoryString = "";
        }
        if (StringUtils.isNotEmpty(seqString)) {
            String[] categoryArray = categoryString.split(",");
            String[] seqArray = seqString.split(",");

            int index = 0;
            for (String cc : categoryArray) {
                String seqStr = getStringAtIndex(seqArray, index);
                seqMap.put(cc, seqStr);
                index++;
            }
        }

        if (MultipleCategoryLogic.SORT_TYPE_REGISTDATE_KEY.equals(seqMap.get(""))
            || MultipleCategoryLogic.SORT_TYPE_SALECOUNT_KEY.equals(seqMap.get(""))) {
            CategoryDataResponse categoryItems = new CategoryDataResponse();
            categoryItems.setCategoryId("");
            categoryItems.setStype(seqMap.get(""));
            categoryItemsList.add(categoryItems);
            return categoryItemsList;
        }

        for (CategoryDetailsDto categoryDetailsDto : categoryDetailsDtoList) {
            String categoryId = categoryDetailsDto.getCategoryId();

            CategoryDataResponse categoryItems = new CategoryDataResponse();
            categoryItems.setCategoryId(categoryId);
            categoryItems.setCategoryName(categoryDetailsDto.getCategoryNamePC());
            categoryItems.setStype(seqMap.get(categoryId));
            categoryItemsList.add(categoryItems);
        }

        return categoryItemsList;
    }

    /**
     * This method will accept String array and index, and returns the value from the array at the given index<br/>
     * It also handles ArrayIndexOutOfBoundsException and return blank string<br/>
     *
     * @param stringArray string array
     * @param index       index to get the value
     * @return value at given index from given array
     */
    protected String getStringAtIndex(String[] stringArray, int index) {
        String value = StringUtils.EMPTY;
        try {
            if (StringUtils.isNotEmpty(stringArray[index])) {
                value = stringArray[index];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // 取得失敗は無視
            LOGGER.error("例外処理が発生しました", e);
        }
        return value;
    }
}