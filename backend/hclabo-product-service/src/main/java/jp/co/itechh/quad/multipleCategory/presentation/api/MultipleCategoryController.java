/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.multipleCategory.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.core.dto.multipleCategory.ajax.MultipleCategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.logic.category.MultipleCategoryLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDetailsGetLogic;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.multiplecategory.presentation.api.PromotionsApi;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.CategoryDataResponse;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.MultipleCategoryGoodsDetailsResponse;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.MultipleCategoryListGetRequest;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.MultipleCategoryListResponse;
import jp.co.itechh.quad.multiplecategory.presentation.api.param.PageInfoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * マルチカテゴリ Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class MultipleCategoryController extends AbstractController implements PromotionsApi {

    /*** To fetch special category list */
    private final MultipleCategoryLogic multipleCategoryLogic;

    /*** マルチカテゴリ Helper */
    private final MultipleCategoryHelper multipleCategoryHelper;

    /*** カテゴリ情報DTO取得 */
    private final CategoryDetailsGetLogic categoryDetailsGetLogic;

    /**
     * コンストラクター
     *
     * @param multipleCategoryLogic To fetch special category list
     * @param promotionsHelper      マルチカテゴリ Helper
     */
    public MultipleCategoryController(MultipleCategoryLogic multipleCategoryLogic,
                                      MultipleCategoryHelper promotionsHelper,
                                      CategoryDetailsGetLogic categoryDetailsGetLogic) {
        this.multipleCategoryLogic = multipleCategoryLogic;
        this.multipleCategoryHelper = promotionsHelper;
        this.categoryDetailsGetLogic = categoryDetailsGetLogic;
    }

    /**
     * GET /promotions/multiple-categories : マルチカテゴリ取得
     * マルチカテゴリ取得
     *
     * @param multipleCategoryListGetRequest マルチカテゴリ取得リクエスト (required)
     * @param pageInfoRequest                ページ情報リクエスト (optional)
     * @return マルチカテゴリ一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<MultipleCategoryListResponse> get(
                    @NotNull @ApiParam(value = "マルチカテゴリ取得リクエスト", required = true) @Valid
                                    MultipleCategoryListGetRequest multipleCategoryListGetRequest,
                    @ApiParam(value = "ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        MultipleCategoryListResponse multipleCategoryListResponse = new MultipleCategoryListResponse();

        // siteType設定
        HTypeSiteType siteType = HTypeSiteType.FRONT_PC;

        // Get multiple category map using categoryId, seq and limit with
        // categoryId as key and
        // goods group dto list as value
        Map<String, List<MultipleCategoryGoodsDetailsDto>> multipleCategoryMap =
                        multipleCategoryLogic.getCategoryMap(siteType, multipleCategoryListGetRequest.getCategoryId(),
                                                             multipleCategoryListGetRequest.getSeq(),
                                                             multipleCategoryListGetRequest.getLimitItem(),
                                                             multipleCategoryListGetRequest.getPriceFrom(),
                                                             multipleCategoryListGetRequest.getPriceTo(),
                                                             multipleCategoryListGetRequest.getStock(),
                                                             multipleCategoryListGetRequest.getViewType()
                                                            );

        // Get categoryDetailsDtoList using list of category id
        List<String> categoryIdList = new ArrayList<>(multipleCategoryMap.keySet());
        List<CategoryDetailsDto> categoryDetailsDtoList =
                        categoryDetailsGetLogic.getCategoryDetailsDtoList(categoryIdList, siteType);

        // マルチカテゴリマップレスポンスを設定
        Map<String, List<MultipleCategoryGoodsDetailsResponse>> multipleCategoryMapResponse =
                        multipleCategoryHelper.toMultipleCategoryMapResponse(multipleCategoryMap);
        multipleCategoryListResponse.setMultipleCategoryMap(multipleCategoryMapResponse);

        // カテゴリ詳細レスポンスを設定
        List<CategoryDataResponse> categoryDataResponseList =
                        multipleCategoryHelper.toDataForLoadMultipleCategory(categoryDetailsDtoList,
                                                                             multipleCategoryListGetRequest.getCategoryId(),
                                                                             multipleCategoryListGetRequest.getSeq()
                                                                            );
        multipleCategoryListResponse.setCategoryItems(categoryDataResponseList);

        return new ResponseEntity<>(multipleCategoryListResponse, HttpStatus.OK);
    }
}