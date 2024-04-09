/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.web;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.dto.multipleCategory.ajax.MultipleCategoryGoodsDetailsDto;
import jp.co.itechh.quad.front.thymeleaf.PreConverterViewUtil;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import jp.co.itechh.quad.multiplecategory.presentation.api.MultipleCategoryApi;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.CategoryDataResponse;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.MultipleCategoryGoodsDetailsResponse;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.MultipleCategoryListGetRequest;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.MultipleCategoryListResponse;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.PageInfoRequest;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 複数カテゴリコントローラー
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@RestController
@RequestMapping("/")
public class MultipleCategoryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleCategoryController.class);

    private final MultipleCategoryApi multipleCategoryApi;

    public MultipleCategoryController(MultipleCategoryApi multipleCategoryApi) {
        this.multipleCategoryApi = multipleCategoryApi;
    }

    /**
     * 複数カテゴリを取得する
     *
     * @param categoryId カテゴリコード
     * @param seq ソート条件 normal=標準  new=新着順  price=価格順  salableness=売れ筋順
     * @param limit 取得件数
     * @param priceFrom 価格from
     * @param priceTo 価格to
     * @param stock 在庫指定 有りのみ=true 指定なし=false
     * @param viewType 取得画像 リスト=list サムネイル=thumbnail
     * @return MultipleCategoryDataインスタンス
     */
    @GetMapping("/getMultipleCategoryData")
    @ResponseBody
    public MultipleCategoryData getMultipleCategoryData(
                    @RequestParam(name = "categoryId", required = false) String categoryId,
                    @RequestParam(name = "seq", required = false) String seq,
                    @RequestParam(name = "limit", required = false) String limit,
                    @RequestParam(name = "priceFrom", required = false) String priceFrom,
                    @RequestParam(name = "priceTo", required = false) String priceTo,
                    @RequestParam(name = "stock", required = false) String stock,
                    @RequestParam(name = "viewType", required = false) String viewType) {

        MultipleCategoryListGetRequest request =
                        createMultipleCategoryListGetRequest(categoryId, seq, limit, priceFrom, priceTo, stock,
                                                             viewType
                                                            );
        PageInfoRequest pageInfoRequest = new PageInfoRequest();

        try {
            MultipleCategoryListResponse response = multipleCategoryApi.get(request, pageInfoRequest);
            return convertToResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * Convert To Response
     *
     * @param response MultipleCategoryListResponse
     */
    private MultipleCategoryData convertToResponse(MultipleCategoryListResponse response) {
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        MultipleCategoryData multipleCategoryData = new MultipleCategoryData();
        List<CategoryData> categoryDataList = new ArrayList<>();
        if (response.getCategoryItems() != null) {
            for (CategoryDataResponse categoryDataResponse : response.getCategoryItems()) {
                CategoryData categoryData = new CategoryData();
                categoryData.setCid(categoryDataResponse.getCategoryId());
                categoryData.setCategoryName(categoryDataResponse.getCategoryName());
                categoryData.setStype(categoryDataResponse.getStype());
                categoryDataList.add(categoryData);
            }
        }

        Map<String, List<MultipleCategoryGoodsDetailsDto>> multipleCategoryMap = new LinkedHashMap<>();
        if (response.getMultipleCategoryMap() != null) {
            PreConverterViewUtil pre = new PreConverterViewUtil();
            for (String key : response.getMultipleCategoryMap().keySet()) {
                List<MultipleCategoryGoodsDetailsDto> dtoList = new ArrayList<>();
                List<MultipleCategoryGoodsDetailsResponse> mapList = response.getMultipleCategoryMap().get(key);
                for (MultipleCategoryGoodsDetailsResponse multipleCategoryGoodsDetailsResponse : mapList) {
                    MultipleCategoryGoodsDetailsDto dto = new MultipleCategoryGoodsDetailsDto();
                    dto.setGoodsGroupSeq(multipleCategoryGoodsDetailsResponse.getGoodsGroupSeq());
                    dto.setGgcd(multipleCategoryGoodsDetailsResponse.getGgcd());
                    dto.setHref(multipleCategoryGoodsDetailsResponse.getHref());
                    dto.setGoodsGroupName(multipleCategoryGoodsDetailsResponse.getGoodsGroupName());
                    dto.setGoodsGroupImageThumbnail(multipleCategoryGoodsDetailsResponse.getGoodsGroupImageThumbnail());
                    dto.setGoodsImageItem(multipleCategoryGoodsDetailsResponse.getGoodsImageItem());
                    dto.setGoodsPrice(multipleCategoryGoodsDetailsResponse.getGoodsPrice());
                    dto.setGoodsPriceInTax(multipleCategoryGoodsDetailsResponse.getGoodsPriceInTax());
                    dto.setGoodsNote1(pre.convert(multipleCategoryGoodsDetailsResponse.getGoodsNote1(), false));
                    dto.setTaxRate(multipleCategoryGoodsDetailsResponse.getTaxRate());
                    dto.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                      multipleCategoryGoodsDetailsResponse.getGoodsTaxType()
                                                                     ));
                    if (multipleCategoryGoodsDetailsResponse.getWhatsnewDate() != null) {
                        Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(
                                        Timestamp.valueOf(multipleCategoryGoodsDetailsResponse.getWhatsnewDate()));
                        dto.setWhatsnewDate(whatsnewDate);

                        dto.setNewDate(whatsnewDate.compareTo(dateUtility.getCurrentDate()) >= 0);
                    }
                    dto.setStockStatusPc(multipleCategoryGoodsDetailsResponse.getStockStatus());
                    dto.setGoodsIconItems(convertToMultipleCategoryGoodsDetailsDto(
                                    multipleCategoryGoodsDetailsResponse.getGoodsIconItems()));
                    dto.setIconName(multipleCategoryGoodsDetailsResponse.getIconName());
                    dto.setIconColorCode(multipleCategoryGoodsDetailsResponse.getIconColorCode());
                    if (multipleCategoryGoodsDetailsResponse.getIsStockStatusDisplay() != null) {
                        dto.setStockStatusDisplay(multipleCategoryGoodsDetailsResponse.getIsStockStatusDisplay());
                    }
                    if (multipleCategoryGoodsDetailsResponse.getIsStockNoSaleDisp() != null) {
                        dto.setStockNoSaleDisp(multipleCategoryGoodsDetailsResponse.getIsStockNoSaleDisp());
                    }
                    if (multipleCategoryGoodsDetailsResponse.getIsStockSoldOutIconDisp() != null) {
                        dto.setStockSoldOutIconDisp(multipleCategoryGoodsDetailsResponse.getIsStockSoldOutIconDisp());
                    }
                    if (multipleCategoryGoodsDetailsResponse.getIsStockBeforeSaleIconDisp() != null) {
                        dto.setStockBeforeSaleIconDisp(
                                        multipleCategoryGoodsDetailsResponse.getIsStockBeforeSaleIconDisp());
                    }
                    if (multipleCategoryGoodsDetailsResponse.getIsStockNoStockIconDisp() != null) {
                        dto.setStockNoStockIconDisp(multipleCategoryGoodsDetailsResponse.getIsStockNoStockIconDisp());
                    }
                    if (multipleCategoryGoodsDetailsResponse.getIsStockFewIconDisp() != null) {
                        dto.setStockFewIconDisp(multipleCategoryGoodsDetailsResponse.getIsStockFewIconDisp());
                    }
                    if (multipleCategoryGoodsDetailsResponse.getIsStockPossibleSalesIconDisp() != null) {
                        dto.setStockPossibleSalesIconDisp(
                                        multipleCategoryGoodsDetailsResponse.getIsStockPossibleSalesIconDisp());
                    }
                    if (multipleCategoryGoodsDetailsResponse.getIsGoodsGroupImage() != null) {
                        dto.setGoodsGroupImage(multipleCategoryGoodsDetailsResponse.getIsGoodsGroupImage());
                    }
                    dtoList.add(dto);
                }
                multipleCategoryMap.put(key, dtoList);
            }
        }

        multipleCategoryData.setCategoryItems(categoryDataList);
        multipleCategoryData.setMultipleCategoryMap(multipleCategoryMap);

        return multipleCategoryData;
    }

    /**
     *
     * Create Multiple Category List Get Request
     *
     * @param categoryId categoryId
     * @param seq seq
     * @param limit limit
     * @param priceFrom priceFrom
     * @param priceTo priceTo
     * @param stock stock
     * @param viewType viewType
     */
    private MultipleCategoryListGetRequest createMultipleCategoryListGetRequest(String categoryId,
                                                                                String seq,
                                                                                String limit,
                                                                                String priceFrom,
                                                                                String priceTo,
                                                                                String stock,
                                                                                String viewType) {
        MultipleCategoryListGetRequest request = new MultipleCategoryListGetRequest();
        request.setCategoryId(categoryId);
        request.setSeq(seq);
        request.setLimitItem(limit);
        request.setPriceFrom(priceFrom);
        request.setPriceTo(priceTo);
        request.setStock(stock);
        request.setViewType(viewType);

        return request;
    }

    /**
     *
     * Convert To List MultipleCategoryGoodsDetailsDto
     *
     * @param multipleCategoryGoodsDetailsResponse multipleCategoryGoodsDetailsResponse
     */
    private List<MultipleCategoryGoodsDetailsDto> convertToMultipleCategoryGoodsDetailsDto(List<MultipleCategoryGoodsDetailsResponse> multipleCategoryGoodsDetailsResponse) {
        List<MultipleCategoryGoodsDetailsDto> list = new ArrayList<>();
        multipleCategoryGoodsDetailsResponse.forEach(dto -> {
            MultipleCategoryGoodsDetailsDto multipleCategoryGoodsDetailsDto = new MultipleCategoryGoodsDetailsDto();
            multipleCategoryGoodsDetailsDto.setIconName(dto.getIconName());
            multipleCategoryGoodsDetailsDto.setIconColorCode(dto.getIconColorCode());

            list.add(multipleCategoryGoodsDetailsDto);
        });

        return list;
    }

    /**
     *
     * CategoryData
     *
     */
    @Data
    class CategoryData implements Serializable {
        /** serialVersionUID<br/> */
        private static final long serialVersionUID = 1L;

        /** categoryId */
        public String cid;

        /** category name */
        public String categoryName;

        /** SORT_TYPE */
        public String stype;

    }

    /**
     *
     * Multiple category data
     *
     */
    @Data
    class MultipleCategoryData implements Serializable {
        /** serialVersionUID<br/> */
        private static final long serialVersionUID = 1L;

        /** CategoryData */
        public List<CategoryData> categoryItems;

        /** Multiple category items */
        public Map<String, List<MultipleCategoryGoodsDetailsDto>> multipleCategoryMap;

    }

}