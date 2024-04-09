/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.category.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.batch.queue.SyncUpdateCategoryProductQueueMessage;
import jp.co.itechh.quad.category.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryExclusiveResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryItemListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryItemListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryProductBatchRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryRegistRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategorySeqListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategorySeqListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.category.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathListResponse;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryExclusiveDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryCheckLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDetailsListGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGetExclusiveLogic;
import jp.co.itechh.quad.core.logic.goods.category.NewCategorySeqGetLogic;
import jp.co.itechh.quad.core.service.goods.category.CategoryGetGoodsListService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGetService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGoodsSortGetService;
import jp.co.itechh.quad.core.service.goods.category.CategoryListGetService;
import jp.co.itechh.quad.core.service.goods.category.CategoryModifyService;
import jp.co.itechh.quad.core.service.goods.category.CategoryRegistService;
import jp.co.itechh.quad.core.service.goods.category.CategoryRemoveService;
import jp.co.itechh.quad.core.service.goods.category.CategoryTreeNodeGetService;
import jp.co.itechh.quad.core.service.goods.category.OpenCategoryGetService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.AsyncTaskUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

/**
 * カテゴリ Controller
 *
 *  @author PHAM QUANG DIEU (VJP)
 *
 */

@RestController
public class CategoryController extends AbstractController implements ProductsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間無し） */
    public static final String MSGCD_OPENSTATUS_NO_OPEN = "PREVIEW-STATUS-001-";

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間有り） */
    public static final String MSGCD_OPENSTATUS_OUT_OF_TERM = "PREVIEW-STATUS-002-";

    /** カテゴリー階層最大 */
    private static final Integer CATEGORY_HIERARCHY_MAX = 10;

    /** カテゴリ修正 */
    private final CategoryModifyService categoryModifyService;

    /** カテゴリ削除サービス */
    private final CategoryRemoveService categoryRemoveService;

    /** カテゴリ取得サービス */
    private final CategoryGetService categoryGetService;

    /** カテゴリ登録サービス */
    private final CategoryRegistService categoryRegistService;

    /** カテゴリリスト取得サービス */
    private final CategoryListGetService categoryListGetService;

    /** 公開商品グループ情報検索 */
    private final CategoryTreeNodeGetService categoryTreeNodeGetService;

    /** カテゴリIDに紐づく公開商品数取得サービス */
    private final CategoryGetGoodsListService categoryGetGoodsListService;

    /** カテゴリ排他を取得 */
    private final CategoryGetExclusiveLogic categoryGetExclusiveLogic;

    /** カテゴリ情報リスト取得ロジッククラス */
    private final CategoryDetailsListGetLogic categoryDetailsListGetLogic;

    /** 公開中カテゴリ取得 */
    private final OpenCategoryGetService openCategoryGetService;

    /** カテゴリ登録商品並び順取得 */
    private final CategoryGoodsSortGetService categoryGoodsSortGetService;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** カテゴリ Helper */
    private final CategoryHelper categoryHelper;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** カテゴリSEQ採番 */
    private final NewCategorySeqGetLogic newCategorySeqGetLogic;

    /** カテゴリ入力バリデータLogic */
    private final CategoryCheckLogic categoryCheckLogic;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /** カテゴリApi */
    private final CategoryApi categoryApi;

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** コンストラクタ */
    public CategoryController(CategoryListGetService categoryListGetService,
                              CategoryHelper categoryHelper,
                              CategoryApi categoryApi,
                              CategoryModifyService categoryModifyService,
                              CategoryRemoveService categoryRemoveService,
                              CategoryGetService categoryGetService,
                              CategoryRegistService categoryRegistService,
                              CategoryTreeNodeGetService categoryTreeNodeGetService,
                              CategoryGetExclusiveLogic categoryGetExclusiveLogic,
                              CategoryGetGoodsListService categoryGetGoodsListService,
                              CategoryDetailsListGetLogic categoryDetailsListGetLogic,
                              OpenCategoryGetService openCategoryGetService,
                              CategoryGoodsSortGetService categoryGoodsSortGetService,
                              MessagePublisherService messagePublisherService,
                              AsyncService asyncService,
                              NewCategorySeqGetLogic newCategorySeqGetLogic,
                              CategoryCheckLogic categoryCheckLogic,
                              DateUtility dateUtility) {
        this.categoryListGetService = categoryListGetService;
        this.categoryTreeNodeGetService = categoryTreeNodeGetService;
        this.categoryGetGoodsListService = categoryGetGoodsListService;
        this.categoryHelper = categoryHelper;
        this.categoryApi = categoryApi;
        this.categoryModifyService = categoryModifyService;
        this.categoryRemoveService = categoryRemoveService;
        this.categoryGetService = categoryGetService;
        this.categoryRegistService = categoryRegistService;
        this.categoryGetExclusiveLogic = categoryGetExclusiveLogic;
        this.categoryDetailsListGetLogic = categoryDetailsListGetLogic;
        this.openCategoryGetService = openCategoryGetService;
        this.categoryGoodsSortGetService = categoryGoodsSortGetService;
        this.messagePublisherService = messagePublisherService;
        this.asyncService = asyncService;
        this.newCategorySeqGetLogic = newCategorySeqGetLogic;
        this.categoryCheckLogic = categoryCheckLogic;
        this.dateUtility = dateUtility;
    }

    /**
     * GET /products/categories/{categoryId} : カテゴリ取得
     * カテゴリ取得
     *
     * @param categoryId         カテゴリID (required)
     * @param categoryGetRequest カテゴリー取得リクエスト
     * @return カテゴリレスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CategoryResponse> getByCategoryId(
                    @ApiParam(value = "カテゴリーID", required = true) @PathVariable("categoryId") String categoryId,
                    @ApiParam("カテゴリー取得リクエスト") @Valid CategoryGetRequest categoryGetRequest) {

        CategoryResponse categoryResponse = null;

        Timestamp frontDisplayReferenceDate =
                        this.dateUtility.convertDateToTimestamp(categoryGetRequest.getFrontDisplayReferenceDate());

        CategoryDto categoryDto = categoryGetService.execute(categoryId, null, frontDisplayReferenceDate);

        if (categoryDto != null) {
            categoryResponse = categoryHelper.toCategoryResponse(categoryDto);
        }

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    /**
     * POST /products/categories : カテゴリ登録
     * カテゴリ登録
     *
     * @param categoryRegistRequest カテゴリ登録リクエスト (required)
     * @return カテゴリレスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CategoryResponse> regist(@ApiParam(value = "カテゴリ登録リクエスト", required = true) @Valid @RequestBody
                                                                   CategoryRegistRequest categoryRegistRequest) {

        // 登録可能チェックを行う
        categoryCheckLogic.checkForRegistUpdate(categoryHelper.toCategoryDto(categoryRegistRequest,
                                                                             categoryRegistRequest.getCategoryRequest()
                                                                                                  .getCategorySeq()
                                                                            ));

        CategoryDto categoryDtoGet =
                        categoryGetService.execute(categoryRegistRequest.getCategoryRequest().getCategoryId());

        // カテゴリSEQ採番
        Integer categorySeq = newCategorySeqGetLogic.execute();

        CategoryResponse categoryResponse = new CategoryResponse();

        CategoryDto categoryDto = categoryHelper.toCategoryDto(categoryRegistRequest, categorySeq);
        if (categoryDtoGet != null && categoryDtoGet.getCategoryEntity() != null) {
            throwMessage("AGC000004");
        }

        if (categoryRegistService.execute(categoryDto) != 1) {
            throwMessage("AGC000001");
        }

        CategoryDto categoryDtoResponse =
                        categoryGetService.execute(categoryRegistRequest.getCategoryRequest().getCategoryId());
        categoryHelper.toCategoryResponse(categoryResponse, categoryDtoResponse);

        // カテゴリー種別が「自動」の場合のみ
        if (HTypeCategoryType.AUTO.equals(EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class,
                                                                        categoryRegistRequest.getCategoryRequest()
                                                                                             .getCategoryType()
                                                                       ))) {

            try {
                CategoryProductBatchRequest categoryProductBatchRequest = new CategoryProductBatchRequest();

                categoryProductBatchRequest.setCategorySeq(categorySeq);

                Object[] objectRequest = new Object[] {categoryProductBatchRequest};
                Class<?>[] typeClass = new Class<?>[] {CategoryProductBatchRequest.class};

                // カテゴリー登録商品更新バッチを呼び出す（MQ）　引数：カテゴリーSEQ
                AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                    asyncService.asyncService(categoryApi, "updateCategoryProductBatch", objectRequest, typeClass);
                });

            } catch (AmqpException e) {
                LOGGER.error("カテゴリー登録商品更新" + messageLogParam + e.getMessage());
            }
        }
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    /**
     * GET /products/categories/{categoryId}/items : カテゴリ内商品一覧取得
     * カテゴリ内商品一覧取得
     *
     * @param categoryId 親カテゴリID (required)
     * @param pageInfoRequest ページ情報リクエスト (optional)
     * @return カテゴリ内商品一覧レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CategoryItemListResponse> getCategoryItems(
                    @ApiParam(value = "親カテゴリID", required = true) @PathVariable("categoryId") String categoryId,
                    @ApiParam("カテゴリ内商品一覧取得リクエスト") @Valid CategoryItemListGetRequest categoryItemListGetRequest,
                    @ApiParam("ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        CategoryGoodsSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(CategoryGoodsSearchForDaoConditionDto.class);
        conditionDto.setCategoryId(categoryId);

        conditionDto.setFrontDisplayReferenceDate(this.dateUtility.convertDateToTimestamp(
                        categoryItemListGetRequest.getFrontDisplayReferenceDate()));

        // どの項目をソートするか
        String orderBy = pageInfoRequest.getOrderBy();
        // ソート
        Boolean sort = pageInfoRequest.getSort();

        // PageInfoRequestのorderBy（どの項目をソートするか）が未設定の場合、
        if (ObjectUtils.isEmpty(orderBy)) {
            // CategoryGoodsSortをカテゴリーIDで検索する
            CategoryGoodsSortEntity categoryGoodsSortEntity = categoryGoodsSortGetService.execute(categoryId);

            // 取得したCategoryGoodsSortの表示順項目と表示順を
            // PageInfoにセット
            if (!ObjectUtils.isEmpty(categoryGoodsSortEntity)) {
                orderBy = categoryGoodsSortEntity.getGoodsSortColumn().getValue();
                sort = categoryGoodsSortEntity.getGoodsSortOrder();
            }
        }

        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(
                        conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(), orderBy, sort);

        List<CategoryGoodsDetailsDto> list = categoryGetGoodsListService.execute(conditionDto);
        CategoryItemListResponse categoryItemListResponses = categoryHelper.toCategoryItemListResponses(list);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        categoryItemListResponses.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(categoryItemListResponses, HttpStatus.OK);
    }

    /**
     * GET /products/categories : カテゴリ一覧取得
     * カテゴリ一覧取得
     *
     * @param categoryListGetRequest カテゴリ一覧取得リクエスト (required)
     * @param pageInfoRequest ページ情報リクエスト (optional)
     * @return カテゴリ一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CategoryListResponse> get(@NotNull @ApiParam(value = "カテゴリ一覧取得リクエスト", required = true)
                                                    @Valid CategoryListGetRequest categoryListGetRequest,
                                                    @ApiParam("ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        // 検索条件
        CategorySearchForDaoConditionDto conditionDto =
                        categoryHelper.toCategorySearchForDaoConditionDto(categoryListGetRequest);

        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<CategoryDetailsDto> categoryDetailsDtoList = categoryDetailsListGetLogic.execute(conditionDto);

        // 結果設定
        CategoryListResponse categoryListResponse = categoryHelper.toCategoryListResponse(categoryDetailsDtoList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        categoryListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(categoryListResponse, HttpStatus.OK);
    }

    /**
     * GET /products/categories/tree-nodes : カテゴリ木構造取得
     * カテゴリ木構造取得
     *
     * @param categoryTreeGetRequest カテゴリ木構造取得リクエスト (optional)
     * @return カテゴリ木構造レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CategoryTreeResponse> getTreeNodes(
                    @ApiParam("カテゴリ木構造取得リクエスト") @Valid CategoryTreeGetRequest categoryTreeGetRequest) {

        // リクエストを元にCategorySearchForDaoConditionDtoを作る
        // 検索条件の作成
        CategorySearchForDaoConditionDto conditionDto =
                        categoryHelper.toCategorySearchForDaoConditionDto(categoryTreeGetRequest);

        // カテゴリー木構造Dtoクラス取得
        CategoryTreeDto categoryTreeDto = categoryTreeNodeGetService.execute(conditionDto);

        CategoryTreeResponse categoryTreeResponse = new CategoryTreeResponse();
        // カテゴリ木構造クラスに変換
        categoryHelper.toCategoryTreeResponse(categoryTreeDto, categoryTreeResponse);

        // return　レスポンス
        return new ResponseEntity<>(categoryTreeResponse, HttpStatus.OK);
    }

    /**
     * PUT /products/categories/{categoryId} : カテゴリ更新
     * カテゴリ更新
     *
     * @param categoryId カテゴリID (required)
     * @param categoryUpdateRequest カテゴリ更新リクエスト (optional)
     * @return カテゴリレスポンス (status code 200)
     *         or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CategoryResponse> updateCategories(String categoryId,
                                                             @Valid CategoryUpdateRequest categoryUpdateRequest) {
        CategoryResponse categoryResponse = new CategoryResponse();
        boolean checkChangeCategoryConditionDetailList = false;

        CategoryDto originalCategoryDto = categoryGetService.execute(categoryId);
        CategoryDto modifyCategoryDto = categoryHelper.toCategoryUpdate(categoryUpdateRequest, originalCategoryDto);

        // 登録可能チェックを行う
        categoryCheckLogic.checkForRegistUpdate(modifyCategoryDto);

        if (originalCategoryDto.getCategoryConditionDetailEntityList() != null) {
            checkChangeCategoryConditionDetailList = categoryModifyService.checkChangeCategoryConditionDetailEntityList(
                            originalCategoryDto.getCategoryConditionDetailEntityList(),
                            modifyCategoryDto.getCategoryConditionDetailEntityList()
                                                                                                                       );
        }

        // カテゴリー更新
        if (categoryModifyService.execute(originalCategoryDto, modifyCategoryDto) != 1) {
            throwMessage("AGC000015");
        }

        modifyCategoryDto = categoryGetService.execute(categoryId);
        categoryHelper.toCategoryResponse(categoryResponse, modifyCategoryDto);

        //カテゴリー種別が「自動」　且つ　商品検索条件に修正差分があった場合のみ、
        if (HTypeCategoryType.AUTO.equals(originalCategoryDto.getCategoryEntity().getCategoryType())) {

            String orginalConditionType = originalCategoryDto.getCategoryConditionEntity().getConditionType();
            String modifyConditionType = modifyCategoryDto.getCategoryConditionEntity().getConditionType();
            if (checkChangeCategoryConditionDetailList || !orginalConditionType.equals(modifyConditionType)) {
                try {
                    CategoryProductBatchRequest categoryProductBatchRequest = new CategoryProductBatchRequest();

                    categoryProductBatchRequest.setCategorySeq(
                                    originalCategoryDto.getCategoryEntity().getCategorySeq());

                    Object[] objectRequest = new Object[] {categoryProductBatchRequest};
                    Class<?>[] typeClass = new Class<?>[] {CategoryProductBatchRequest.class};

                    // カテゴリー登録商品更新バッチを呼び出す（MQ）　引数：カテゴリーSEQ
                    AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                        asyncService.asyncService(categoryApi, "updateCategoryProductBatch", objectRequest, typeClass);
                    });

                } catch (AmqpException e) {
                    LOGGER.error("カテゴリー登録商品更新" + messageLogParam + e.getMessage());
                }
            }

        }

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    /**
     * GET /products/categories/{categoryId}/open : 公開中カテゴリ取得
     * 公開中カテゴリ取得
     *
     * @param categoryId         親カテゴリID (required)
     * @param categoryGetRequest カテゴリー取得リクエスト
     * @return カテゴリレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CategoryResponse> getCategoryOpen(
                    @ApiParam(value = "親カテゴリID", required = true) @PathVariable("categoryId") String categoryId,
                    @ApiParam("カテゴリー取得リクエスト") @Valid CategoryGetRequest categoryGetRequest) {

        Timestamp frontDisplayReferenceDate =
                        this.dateUtility.convertDateToTimestamp(categoryGetRequest.getFrontDisplayReferenceDate());

        // カテゴリSEQリストを元にカテゴリDTOのリストを取得する
        CategoryDto categoryDto = openCategoryGetService.execute(categoryId, frontDisplayReferenceDate);

        // カテゴリレスポンスに変換
        CategoryResponse categoryResponse = categoryHelper.toCategoryResponse(categoryDto);

        return new ResponseEntity(categoryResponse, HttpStatus.OK);
    }

    /**
     * DELETE /products/categories/{categoryId} : カテゴリ削除
     * カテゴリ削除
     *
     * @param categoryId カテゴリID (required)
     * @return 成功 (status code 200)
     *         or その他エラー (status code 500)
     */

    public ResponseEntity<Void> deleteCategories(
                    @ApiParam(value = "カテゴリID", required = true) @PathVariable("categoryId") String categoryId) {
        // 削除処理
        if (categoryRemoveService.execute(categoryId) < 1) {
            throwMessage("AGC000007");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /products/categories/exclusive : カテゴリ排他を取得
     * カテゴリ排他を取得
     *
     * @return 成功 (status code 200)
     *         or その他エラー (status code 500)
     */

    public ResponseEntity<CategoryExclusiveResponse> getExclusive() {
        CategoryExclusiveResponse categoryExclusiveResponse;
        CategoryExclusiveDto categoryExclusiveDto = categoryGetExclusiveLogic.execute();
        categoryExclusiveResponse = categoryHelper.toCategoryExclusiveResponse(categoryExclusiveDto);

        return new ResponseEntity<>(categoryExclusiveResponse, HttpStatus.OK);
    }

    /**
     * GET /products/categories/seqlist/{goodsGroupCode} : 商品コードでリスト カテゴリSEQを取得
     * 商品コードでリスト カテゴリSEQを取得
     *
     * @param goodsGroupCode 商品グループコード (required)
     * @return カテゴリ排他を取得 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<CategorySeqListResponse> getSeqListByGoodsGroupCode(String goodsGroupCode) {

        List<CategoryEntity> categoryEntityList = categoryListGetService.getEntityListByGoodsGroupCode(goodsGroupCode);
        CategorySeqListResponse categorySeqListResponse = categoryHelper.toCategorySeqListResponse(categoryEntityList);

        return new ResponseEntity<>(categorySeqListResponse, HttpStatus.OK);
    }

    /**
     * GET /products/categories/seqlist : リストカテゴリIDでSEQリストを取得する
     * リストカテゴリIDでSEQリストを取得する
     *
     * @param categorySeqListGetRequest カテゴリSEQリスト取得リクエスト (optional)
     * @return カテゴリ排他を取得 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<CategorySeqListResponse> getSeqListByIdList(@Valid CategorySeqListGetRequest categorySeqListGetRequest) {

        List<CategoryEntity> categoryEntityList =
                        categoryListGetService.getEntityListByIdList(categorySeqListGetRequest.getCategoryIdList());
        CategorySeqListResponse categorySeqListResponse = categoryHelper.toCategorySeqListResponse(categoryEntityList);

        return new ResponseEntity<>(categorySeqListResponse, HttpStatus.OK);
    }

    /**
     * GET /products/categories/{categoryId}/topicPath : パンくずリスト情報取得
     *
     * @param categoryId 親カテゴリID (required)
     * @param topicPathGetRequest パンくずリスト取得リクエスト (required)
     * @return パンくずリストレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<TopicPathListResponse> getCategoryTopicPath(String categoryId,
                                                                      @NotNull @Valid TopicPathGetRequest topicPathGetRequest) {

        CategoryDto categoryDto = categoryGetService.execute(HTypeOpenStatus.OPEN, categoryId,
                                                             this.dateUtility.convertDateToTimestamp(
                                                                             topicPathGetRequest.getFrontDisplayReferenceDate())
                                                            );
        if (ObjectUtils.isEmpty(categoryDto)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!ObjectUtils.isEmpty(topicPathGetRequest.getFrontDisplayReferenceDate())
            && HTypeFrontDisplayStatus.NO_OPEN.equals(categoryDto.getFrontDisplay())) {
            // プレビュー日時で公開期間外のためそのまま返却
            return null;
        }

        CategorySearchForDaoConditionDto conditionDto = new CategorySearchForDaoConditionDto();
        conditionDto.setMaxHierarchical(CATEGORY_HIERARCHY_MAX);
        conditionDto.setOpenStatus(HTypeOpenStatus.OPEN);
        conditionDto.setFrontDisplayReferenceDate(
                        this.dateUtility.convertDateToTimestamp(topicPathGetRequest.getFrontDisplayReferenceDate()));

        CategoryTreeDto categoryTreeDto = categoryTreeNodeGetService.execute(conditionDto);

        // CategoryTreeDtoからTopicPathを割り出して
        TopicPathListResponse topicPathListResponse =
                        categoryHelper.toTopicPathListResponse(categoryId, categoryDto, categoryTreeDto,
                                                               topicPathGetRequest
                                                              );

        return new ResponseEntity<>(topicPathListResponse, HttpStatus.OK);
    }

    /**
     * POST /products/categories/update-category-product-batch : カテゴリ商品更新バッチを
     * カテゴリ商品更新バッチを
     *
     * @param categoryProductBatchRequest カテゴリ商品更新バッチをリクエスト (required)
     * @return 結果レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> updateCategoryProductBatch(
                    @ApiParam(value = "カテゴリ商品更新バッチをリクエスト", required = true) @Valid @RequestBody
                                    CategoryProductBatchRequest categoryProductBatchRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.syncUpdateCategoryProductBatch.routing");

        SyncUpdateCategoryProductQueueMessage syncUpdateCategoryProductQueueMessage =
                        new SyncUpdateCategoryProductQueueMessage();

        syncUpdateCategoryProductQueueMessage.setCategorySeq(categoryProductBatchRequest.getCategorySeq());
        syncUpdateCategoryProductQueueMessage.setGoodsGroupSeqList(categoryProductBatchRequest.getGoodsGroupSeqList());

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messagePublisherService.publish(routing, syncUpdateCategoryProductQueueMessage);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (AmqpException e) {

            LOGGER.error("カテゴリー登録商品更新" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}