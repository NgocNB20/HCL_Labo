package jp.co.itechh.quad.product.presentation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.batch.queue.SyncUpsertGoodsStockDisplayQueueMessage;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryProductBatchRequest;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupImageDao;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsCsvDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultForOrderRegistDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsUnitDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsAllCsvDownloadLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsCsvDownloadLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsDetailsGetByCodeLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsDetailsGetBySeqLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsUnitListGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupListGetLogic;
import jp.co.itechh.quad.core.module.AccessSearchKeywordLoggingModule;
import jp.co.itechh.quad.core.service.goods.category.CategoryGetService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGoodsSortGetService;
import jp.co.itechh.quad.core.service.goods.csv.GoodsCsvOptionGetByIdService;
import jp.co.itechh.quad.core.service.goods.csv.GoodsCsvOptionListGetService;
import jp.co.itechh.quad.core.service.goods.csv.GoodsCsvOptionUpdateService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsDetailsListGetService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsImageZipFileUploadService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsSearchResultListGetService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupCorrelationDataCheckService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupRegistUpdateService;
import jp.co.itechh.quad.core.service.goods.group.OpenGoodsGroupDetailsGetService;
import jp.co.itechh.quad.core.service.goods.group.impl.GoodsGroupGetServiceImpl;
import jp.co.itechh.quad.core.service.order.GoodsSearchResultListForOrderRegistGetService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.AsyncTaskUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.imageupdate.presentation.api.ImageUpdateApi;
import jp.co.itechh.quad.imageupdate.presentation.api.param.ImageUpdateExecuteRequest;
import jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsCodeListRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsCodeListResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsImageItemRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsStockDisplayUpsertRequest;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.product.presentation.api.param.PopularityTotalingBatchRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCorrelationCheckRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvDownloadGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvGetOptionRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDeleteRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailByGoodCodeGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductOrderItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductOrderItemListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductsImagesZipUploadRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductsImagesZipUploadResponse;
import jp.co.itechh.quad.product.presentation.api.param.StandardListRequest;
import jp.co.itechh.quad.product.presentation.api.param.StandardListResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.MapUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class ProductController extends AbstractController implements ProductsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    /** 商品 Helper */
    private final ProductHelper productHelper;

    /**
     * 商品画像更新Api
     */
    private final ImageUpdateApi imageUpdateApi;

    /**
     * カテゴリApi
     */
    private final CategoryApi categoryApi;

    /** 商品検索結果リスト取得Service */
    private final GoodsSearchResultListGetService goodsSearchResultListGetService;

    /** 商品グループDto相関チェックサービス */
    private final GoodsGroupCorrelationDataCheckService goodsGroupCorrelationService;

    /** 商品グループ登録更新サービス */
    private final GoodsGroupRegistUpdateService goodsGroupRegistUpdateService;

    /** 商品詳細情報リスト取得サービス */
    private final GoodsDetailsListGetService goodsDetailsListGetService;

    /*** 商品グループ取得サービス*/
    private final GoodsGroupGetServiceImpl goodsGroupGetService;

    /** 商品検索結果リスト取得サービス */
    private final GoodsSearchResultListForOrderRegistGetService goodsSearchResultListForOrderRegistGetService;

    /** 公開商品グループ詳細情報取得サービス */
    private final OpenGoodsGroupDetailsGetService openGoodsGroupDetailsGetService;

    /** 商品のCSVDLオプションリストを取得する */
    private final GoodsCsvOptionListGetService goodsCsvOptionListGetService;

    /** IDで商品のCSVDLオプションを取得する */
    private final GoodsCsvOptionGetByIdService goodsCsvOptionGetByIdService;

    /** 商品CSVDLオプションの更新 更新 */
    private final GoodsCsvOptionUpdateService goodsCsvOptionUpdateService;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 商品グループリスト取得ロジッククラス */
    private final GoodsGroupListGetLogic goodsGroupListGetLogic;

    private final Integer SHOP_SEQ = 1001;

    /** 商品画像zip一括アップロードサービス<br/> */
    private final GoodsImageZipFileUploadService goodsImageZipFileUploadService;

    /** 商品検索CSV出力ロジッククラス */
    private final GoodsAllCsvDownloadLogic goodsAllCsvDownloadLogic;

    /** 商品検索CSV出力ロジッククラス */
    private final GoodsCsvDownloadLogic goodsCsvDownloadLogic;

    /** 商品詳細情報取得(商品コード) */
    private final GoodsDetailsGetByCodeLogic goodsDetailsGetByCodeLogic;

    /** 商品詳細情報取得(商品SEQ) */
    private final GoodsDetailsGetBySeqLogic goodsDetailsGetBySeqLogic;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 商品DAO */
    private final GoodsDao goodsDao;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** カテゴリ登録商品並び順取得 */
    private final CategoryGoodsSortGetService categoryGoodsSortGetService;

    /** カテゴリ取得サービス */
    private final CategoryGetService categoryGetService;

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    /** 商品グループ画像DAO */
    private final GoodsGroupImageDao goodsGroupImageDao;

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /**
     * 処理件数マップ　商品グループSEQ
     * <code>GOODS_GROUP_SEQ</code>
     */
    public static final String GOODS_GROUP_SEQ = "GoodsGroupSeq";

    /** 商品管理番号 */
    private static final String DEFAULT_GOODS_SEARCH_ORDER_FIELD = "goodsGroupCode";

    /** コンストラクタ */
    public ProductController(ProductHelper productHelper,
                             ImageUpdateApi imageUpdateApi,
                             CategoryApi categoryApi,
                             GoodsSearchResultListGetService goodsSearchResultListGetService,
                             GoodsGroupCorrelationDataCheckService goodsGroupCorrelationService,
                             GoodsGroupRegistUpdateService goodsGroupRegistUpdateService,
                             GoodsDetailsListGetService goodsDetailsListGetService,
                             GoodsGroupGetServiceImpl goodsGroupGetService,
                             GoodsSearchResultListForOrderRegistGetService goodsSearchResultListForOrderRegistGetService,
                             OpenGoodsGroupDetailsGetService openGoodsGroupDetailsGetService,
                             GoodsImageZipFileUploadService goodsImageZipFileUploadService,
                             GoodsAllCsvDownloadLogic goodsAllCsvDownloadLogic,
                             GoodsCsvDownloadLogic goodsCsvDownloadLogic,
                             GoodsDetailsGetByCodeLogic goodsDetailsGetByCodeLogic,
                             HeaderParamsUtility headerParamsUtil,
                             GoodsDao goodsDao,
                             GoodsGroupListGetLogic goodsGroupListGetLogic,
                             DateUtility dateUtility,
                             MessagePublisherService messagePublisherService,
                             GoodsDetailsGetBySeqLogic goodsDetailsGetBySeqLogic,
                             GoodsCsvOptionListGetService goodsCsvOptionListGetService,
                             GoodsCsvOptionGetByIdService goodsCsvOptionGetByIdService,
                             GoodsCsvOptionUpdateService goodsCsvOptionUpdateService,
                             CategoryGoodsSortGetService categoryGoodsSortGetService,
                             AsyncService asyncService,
                             CategoryGetService categoryGetService,
                             GoodsGroupDao goodsGroupDao,
                             GoodsGroupImageDao goodsGroupImageDao) {
        this.productHelper = productHelper;
        this.imageUpdateApi = imageUpdateApi;
        this.categoryApi = categoryApi;
        this.goodsSearchResultListGetService = goodsSearchResultListGetService;
        this.goodsGroupCorrelationService = goodsGroupCorrelationService;
        this.goodsGroupRegistUpdateService = goodsGroupRegistUpdateService;
        this.goodsDetailsListGetService = goodsDetailsListGetService;
        this.goodsGroupGetService = goodsGroupGetService;
        this.goodsSearchResultListForOrderRegistGetService = goodsSearchResultListForOrderRegistGetService;
        this.openGoodsGroupDetailsGetService = openGoodsGroupDetailsGetService;
        this.goodsImageZipFileUploadService = goodsImageZipFileUploadService;
        this.asyncService = asyncService;
        this.goodsAllCsvDownloadLogic = goodsAllCsvDownloadLogic;
        this.goodsCsvDownloadLogic = goodsCsvDownloadLogic;
        this.goodsDetailsGetByCodeLogic = goodsDetailsGetByCodeLogic;
        this.goodsGroupListGetLogic = goodsGroupListGetLogic;
        this.headerParamsUtil = headerParamsUtil;
        this.goodsDao = goodsDao;
        this.dateUtility = dateUtility;
        this.messagePublisherService = messagePublisherService;
        this.goodsDetailsGetBySeqLogic = goodsDetailsGetBySeqLogic;
        this.goodsCsvOptionListGetService = goodsCsvOptionListGetService;
        this.goodsCsvOptionGetByIdService = goodsCsvOptionGetByIdService;
        this.goodsCsvOptionUpdateService = goodsCsvOptionUpdateService;
        this.categoryGoodsSortGetService = categoryGoodsSortGetService;
        this.categoryGetService = categoryGetService;
        this.goodsGroupDao = goodsGroupDao;
        this.goodsGroupImageDao = goodsGroupImageDao;
    }

    /**
     * GET /products/{goodsGroupCode}/items/{goodsCode}/standards2 : 規格２一覧取得
     * 規格２一覧取得
     *
     * @param goodsGroupCode      商品管理番号 (required)
     * @param goodsCode           商品番号 (required)
     * @param standardListRequest 規格2一覧リクエスト
     * @param pageInfoRequest     ページ情報リクエスト（ページネーションのため） (optional) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<StandardListResponse> getItemsStandards2(
                    @ApiParam(value = "商品管理番号", required = true) @PathVariable("goodsGroupCode") String goodsGroupCode,
                    @ApiParam(value = "商品番号", required = true) @PathVariable("goodsCode") String goodsCode,
                    @ApiParam("規格2一覧リクエスト") @Valid StandardListRequest standardListRequest,
                    @ApiParam("ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest) {

        // 返却用Map
        Map<String, String> unit2Map = new LinkedHashMap<>();

        // 判定用List
        List<String> tmpUnitValue2List = new ArrayList<>();

        // 型変換（リクエストパラメータに未設定の場合はnull）
        Timestamp targetTime =
                        this.dateUtility.convertDateToTimestamp(standardListRequest.getFrontDisplayReferenceDate());

        try {
            // 規格値検索サービスのコンポーネントを生成
            GoodsUnitListGetLogic goodsUnitListGetLogic =
                            ApplicationContextUtility.getBean(GoodsUnitListGetLogic.class);

            // 規格値検索サービスを実行し、規格値情報Dtoを取得
            List<GoodsUnitDto> goodsUnit2DtoList = goodsUnitListGetLogic.getUnit2List(goodsGroupCode, goodsCode);

            // Dtoから返却用Mapを作成
            for (GoodsUnitDto goodsUnit2Dto : goodsUnit2DtoList) {

                // 販売、在庫チェック
                boolean isSale = productHelper.isGoodsSale(goodsUnit2Dto, targetTime);
                boolean isStock = productHelper.isGoodsStock(goodsUnit2Dto);

                // 非販売または、在庫切れの場合、処理なし
                if (!isSale) {
                    continue;
                }

                if (tmpUnitValue2List.contains(goodsUnit2Dto.getUnitValue2())) {
                    continue;
                }

                String unitValue2;
                if (isStock) {
                    unitValue2 = goodsUnit2Dto.getUnitValue2();
                } else {
                    unitValue2 = productHelper.addNoStockMessage(goodsUnit2Dto.getUnitValue2());
                }

                unit2Map.put(goodsUnit2Dto.getGoodsCode(), unitValue2);

                // 判定用Listに追加
                tmpUnitValue2List.add(goodsUnit2Dto.getUnitValue2());
            }
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            unit2Map = null;
        }

        StandardListResponse standardListResponse = new StandardListResponse();

        standardListResponse.setStandardValue2Map(unit2Map);

        return new ResponseEntity<>(standardListResponse, HttpStatus.OK);
    }

    /**
     * GET /products/items : 管理検索用商品一覧取得
     * 管理検索用商品一覧取得
     *
     * @param productItemListGetRequest 管理検索用商品一覧取得リクエスト (required)
     * @param pageInfoRequest           ページ情報リクエスト (optional)
     * @return 商品一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ProductItemListResponse> getItems(
                    @NotNull @ApiParam(value = "管理検索用商品一覧取得リクエスト", required = true) @Valid
                                    ProductItemListGetRequest productItemListGetRequest,
                    @ApiParam(value = "ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {
        HTypeSiteType siteType = HTypeSiteType.BACK;

        // 検索条件作成
        GoodsSearchForBackDaoConditionDto conditionDto =
                        productHelper.toGoodsSearchForBackDaoConditionDtoForSearch(productItemListGetRequest);
        conditionDto.setSiteType(siteType);
        conditionDto.setShopSeq(1001);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        // 取得
        List<GoodsSearchResultDto> goodsSearchResultDtoList = goodsSearchResultListGetService.execute(conditionDto);

        try {
            // ページにセット
            ProductItemListResponse productItemListResponse =
                            productHelper.toProductItemListResponse(goodsSearchResultDtoList);

            // ページ情報レスポンスを設定
            PageInfoResponse pageInfoResponse = new PageInfoResponse();
            try {
                pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
            }
            productItemListResponse.setPageInfo(pageInfoResponse);
            return new ResponseEntity<>(productItemListResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /products/regist-update : 商品グループ登録更新
     * 商品グループ登録更新
     *
     * @param productRegistUpdateRequest 商品グループ登録更新リクエスト (required)
     * @return 商品グループレスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ProductResponse> registUpdate(
                    @ApiParam(value = "商品グループ更新リクエスト", required = true) @Valid @RequestBody
                                    ProductRegistUpdateRequest productRegistUpdateRequest) {
        //共通情報チェック
        HTypeSiteType siteType = HTypeSiteType.BACK;

        GoodsGroupDto goodsGroupDto;

        String goodsGroupCode = productRegistUpdateRequest.getGoodsGroup().getGoodsGroupCode();

        try {
            goodsGroupDto = productHelper.toGoodsGroupDto(productRegistUpdateRequest.getGoodsGroup(), SHOP_SEQ);

            List<GoodsRelationEntity> goodsRelationEntityList = productHelper.toGoodRelationEntityList(
                            productRegistUpdateRequest.getGoodsRelationRequestList());

            List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList =
                            productHelper.toGoodsGroupImageUpdateDtoList(
                                            productRegistUpdateRequest.getGoodsGroupImageUpdateRequest());

            // 商品グループDto相関チェック
            goodsGroupCorrelationService.execute(goodsGroupDto, goodsRelationEntityList,
                                                 GoodsGroupCorrelationDataCheckService.PROCESS_TYPE_FROM_SCREEN, null
                                                );

            // 画像に関する登録更新情報をセット
            productHelper.toSetImageInfo(productRegistUpdateRequest);

            // 商品グループ登録更新
            Map<String, Object> retMap;
            retMap = goodsGroupRegistUpdateService.execute(getAdminId(), goodsGroupDto, goodsRelationEntityList,
                                                           goodsGroupImageRegistUpdateDtoList,
                                                           GoodsGroupRegistUpdateService.PROCESS_TYPE_FROM_SCREEN
                                                          );
            List<GoodsImageItemRequest> imageItemsRequest = productRegistUpdateRequest.getGoodsImageItems();

            this.updateImagesPosition(goodsGroupCode, imageItemsRequest, SHOP_SEQ);

            // ワーニングメッセージの設定
            String warningMessage = null;
            if (retMap.get(GoodsGroupRegistUpdateService.WARNING_MESSAGE) != null && !"".equals(
                            retMap.get(GoodsGroupRegistUpdateService.WARNING_MESSAGE))) {
                warningMessage = (String) retMap.get(GoodsGroupRegistUpdateService.WARNING_MESSAGE);
            }
            GoodsGroupDto goodsGroupResponse = goodsGroupGetService.execute(goodsGroupCode, SHOP_SEQ, siteType);

            ProductResponse productResponse = productHelper.setProductResponse(goodsGroupResponse);
            productResponse.setWarningMessage(warningMessage);

            if (!MapUtils.isEmpty(retMap)) {
                try {
                    Integer goodsGroupSeq = (Integer) retMap.get(GOODS_GROUP_SEQ);
                    CategoryProductBatchRequest categoryProductBatchRequest = new CategoryProductBatchRequest();

                    categoryProductBatchRequest.setGoodsGroupSeqList(Arrays.asList(goodsGroupSeq));

                    Object[] objectRequest = new Object[] {categoryProductBatchRequest};
                    Class<?>[] typeClass = new Class<?>[] {CategoryProductBatchRequest.class};

                    // カテゴリ商品更新バッチを呼び出す
                    AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                        asyncService.asyncService(categoryApi, "updateCategoryProductBatch", objectRequest, typeClass);
                    });

                } catch (AmqpException e) {
                    LOGGER.error("カテゴリー登録商品更新" + messageLogParam + e.getMessage());
                }
            }

            return new ResponseEntity<>(productResponse, HttpStatus.OK);

        } catch (AppLevelListException | HttpServerErrorException | HttpClientErrorException ae) {
            LOGGER.error("例外処理が発生しました", ae);
            throw ae;
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /products/correlation-check : 商品グループ相関チェック
     * 商品グループ相関チェック
     *
     * @param productCorrelationCheckRequest 商品グループ相関チェックリクエスト (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> checkCorrelation(
                    @ApiParam(value = "商品グループ相関チェックリクエスト", required = true) @Valid @RequestBody
                                    ProductCorrelationCheckRequest productCorrelationCheckRequest) {
        try {
            GoodsGroupDto goodsGroupDto =
                            productHelper.toGoodsGroupDto(productCorrelationCheckRequest.getGoodsGroup(), SHOP_SEQ);

            List<GoodsRelationEntity> goodsRelationEntityList = productHelper.toGoodRelationEntityList(
                            productCorrelationCheckRequest.getGoodsRelationList());
            // 商品グループDto相関チェック
            goodsGroupCorrelationService.execute(goodsGroupDto, goodsRelationEntityList,
                                                 GoodsGroupCorrelationDataCheckService.PROCESS_TYPE_FROM_SCREEN, null
                                                );

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AppLevelListException ae) {
            LOGGER.error("例外処理が発生しました", ae);
            throw ae;
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /products/{goodsGroupCode} : 商品グループ削除
     * 商品グループ削除
     *
     * @param goodsGroupCode       商品管理番号 (required)
     * @param productDeleteRequest 商品グループ削除リクエスト (optional)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> delete(
                    @ApiParam(value = "商品管理番号", required = true) @PathVariable("goodsGroupCode") String goodsGroupCode,
                    @ApiParam("商品グループ削除リクエスト") @Valid ProductDeleteRequest productDeleteRequest) {

        // 共通情報チェック
        HTypeSiteType siteType = HTypeSiteType.BACK;

        GoodsGroupDto goodsGroupDto = goodsGroupGetService.execute(goodsGroupCode, SHOP_SEQ, siteType);

        productHelper.checkDataForDelete(goodsGroupDto);

        productHelper.setFlagForDelete(goodsGroupDto);
        try {
            goodsGroupRegistUpdateService.execute(
                            getAdminId(), goodsGroupDto, null, null, productDeleteRequest.getProcessType());

        } catch (AppLevelListException ae) {
            LOGGER.error("例外処理が発生しました", ae);
            throw ae;
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /products/details : 商品詳細一覧取得
     * 商品詳細一覧取得
     *
     * @param productDetailListGetRequest 商品詳細一覧取得リクエスト (required)
     * @param pageInfoRequest             ページ情報リクエスト (optional)
     * @return 商品詳細一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ProductDetailListResponse> getDetails(
                    @NotNull @ApiParam(value = "商品詳細一覧取得リクエスト", required = true) @Valid
                                    ProductDetailListGetRequest productDetailListGetRequest,
                    @ApiParam(value = "ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        // TODO SiteTYPE
        HTypeSiteType siteType = HTypeSiteType.FRONT_PC;

        List<GoodsDetailsDto> goodsDetailsList =
                        goodsDetailsListGetService.execute(siteType, productDetailListGetRequest.getGoodsSeqList());

        try {
            ProductDetailListResponse productDetailListResponse =
                            productHelper.toProductDetailListResponse(goodsDetailsList);

            return new ResponseEntity<>(productDetailListResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /products : 商品グループ一覧取得
     * 商品グループ一覧取得
     *
     * @param productListGetRequest 商品グループ一覧取得リクエスト (required)
     * @param pageInfoRequest       ページ情報リクエスト (optional)
     * @return 商品グループ一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ProductListResponse> getList(
                    @NotNull @ApiParam(value = "商品グループ一覧取得リクエスト", required = true) @Valid
                                    ProductListGetRequest productListGetRequest,
                    @ApiParam(value = "ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        // TODO SiteTYPE
        HTypeSiteType siteType = HTypeSiteType.FRONT_PC;

        GoodsGroupSearchForDaoConditionDto conditionDto =
                        productHelper.setConditionDto(SHOP_SEQ, siteType, productListGetRequest);

        // どの項目をソートするか
        String orderBy = pageInfoRequest.getOrderBy();
        // ソート
        Boolean sort = pageInfoRequest.getSort();

        // PageInfoRequestのorderBy（どの項目をソートするか）が未設定の場合、
        if (StringUtils.isEmpty(orderBy)) {
            // CategoryGoodsSortをカテゴリーIDで検索する
            CategoryGoodsSortEntity categoryGoodsSortEntity =
                            categoryGoodsSortGetService.execute(productListGetRequest.getCategoryId());

            // 取得したCategoryGoodsSortの表示順項目と表示順を
            // PageInfoにセット
            if (ObjectUtils.isNotEmpty(categoryGoodsSortEntity)) {
                orderBy = categoryGoodsSortEntity.getGoodsSortColumn().getValue();
                sort = categoryGoodsSortEntity.getGoodsSortOrder();
            } else {
                orderBy = DEFAULT_GOODS_SEARCH_ORDER_FIELD;
                sort = true;
            }
        }

        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(
                        conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(), orderBy, sort);

        // ･商品情報検索処理実行
        // ※『logic基本設計書（商品グループリスト取得（検索））.xls』参照
        // Logic GoodsGroupListGetLogic
        // パラメータ 商品グループDao用検索条件Dto
        // (公開状態=公開中)
        // 戻り値 商品グループDTOリスト
        List<GoodsGroupDto> goodsGroupDtoList = goodsGroupListGetLogic.execute(conditionDto);

        try {
            ProductListResponse productDetailListResponse = productHelper.toProductListResponse(goodsGroupDtoList);
            // ページ情報レスポンスを設定
            PageInfoResponse pageInfoResponse = new PageInfoResponse();
            try {
                pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
            }
            productDetailListResponse.setPageInfo(pageInfoResponse);

            if (ListUtils.isEmpty(productListGetRequest.getGoodsGroupSeqList())) {
                if (!productHelper.keywordLikeConditionIsNull(productListGetRequest)) {
                    this.writeSearchKeywordLog(productListGetRequest, pageInfoResponse);
                }
            }

            return new ResponseEntity<>(productDetailListResponse, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /products/order-items : 受注修正用商品一覧取得
     * 受注修正用商品一覧取得
     *
     * @param productOrderItemListGetRequest 管理検索用商品一覧取得リクエスト (required)
     * @param pageInfoRequest                ページ情報リクエスト (optional)
     * @return 受注用商品一覧レスポンス (status code 200)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<ProductOrderItemListResponse> getOrderItems(
                    @NotNull @Valid ProductOrderItemListGetRequest productOrderItemListGetRequest,
                    @Valid PageInfoRequest pageInfoRequest) {

        // 検索条件作成
        GoodsSearchForBackDaoConditionDto goodsSearchForBackDaoConditionDto =
                        productHelper.toGoodsSearchForBackDaoConditionDtoForGoodsSearch(productOrderItemListGetRequest);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(goodsSearchForBackDaoConditionDto, pageInfoRequest.getPage(),
                                     pageInfoRequest.getLimit(), pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        HTypeSiteType siteType = HTypeSiteType.BACK;
        // 取得
        List<GoodsSearchResultForOrderRegistDto> goodsSearchResultDtoList =
                        goodsSearchResultListForOrderRegistGetService.execute(siteType,
                                                                              goodsSearchForBackDaoConditionDto
                                                                             );

        ProductOrderItemListResponse productOrderItemListResponse =
                        productHelper.toGoodsSearchResultForOrderRegistResponse(goodsSearchResultDtoList);
        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(goodsSearchForBackDaoConditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        productOrderItemListResponse.setPageInfo(pageInfoResponse);
        return new ResponseEntity<>(productOrderItemListResponse, HttpStatus.OK);
    }

    /**
     * GET /products/display : 画面表示用商品グループ取得
     * 画面表示用商品グループ取得
     *
     * @param productDisplayGetRequest 画面表示用商品グループ取得リクエスト (optional)
     * @return 画面表示用商品グループレスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ProductDisplayResponse> getForDisplay(
                    @Valid ProductDisplayGetRequest productDisplayGetRequest) {

        HTypeSiteType siteType =
                        EnumTypeUtil.getEnumFromValue(HTypeSiteType.class, productDisplayGetRequest.getSiteType());
        HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               productDisplayGetRequest.getOpenStatus()
                                                                              );

        try {
            // 商品情報取得
            GoodsGroupDto goodsGroupDto =
                            openGoodsGroupDetailsGetService.execute(productDisplayGetRequest.getGoodsGroupCode(),
                                                                    productDisplayGetRequest.getGoodCode(), siteType,
                                                                    openDeleteStatus,
                                                                    productDisplayGetRequest.getFrontDisplayReferenceDate()
                                                                   );
            ProductDisplayResponse productDisplayResponse = new ProductDisplayResponse();
            if (goodsGroupDto != null) {
                productDisplayResponse = productHelper.setProductDisplayResponse(goodsGroupDto);
            }

            return new ResponseEntity<>(productDisplayResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /products/images/zip : 画像zip一括アップロード
     * 画像zip一括アップロード
     *
     * @param productsImagesZipUploadRequest 商品画像アップロードリクエスト (required)
     * @return 商品画像レスポンス (status code 200)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<ProductsImagesZipUploadResponse> registImageZip(
                    @ApiParam(value = "商品画像アップロードリクエスト", required = true) @Valid @RequestBody
                                    ProductsImagesZipUploadRequest productsImagesZipUploadRequest) {
        String zipImageTarget = productsImagesZipUploadRequest.getZipImageTarget();
        int fileListSize = goodsImageZipFileUploadService.execute(productsImagesZipUploadRequest.getZipImageUrl(),
                                                                  zipImageTarget
                                                                 );

        ProductsImagesZipUploadResponse productsImagesZipUploadResponse = new ProductsImagesZipUploadResponse();
        if (!"1".equals(zipImageTarget)) {
            ImageUpdateExecuteRequest imageUpdateExecuteRequest = new ImageUpdateExecuteRequest();
            imageUpdateExecuteRequest.setStartType(HTypeBatchStartType.MANUAL.getValue());
            jp.co.itechh.quad.imageupdate.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                            imageUpdateApi.execute(imageUpdateExecuteRequest);

            productsImagesZipUploadResponse.setExecuteCode(batchExecuteResponse.getExecuteCode());
            productsImagesZipUploadResponse.setExecuteMessage(batchExecuteResponse.getExecuteMessage());
        }

        productsImagesZipUploadResponse.setFileListSize(fileListSize);
        return new ResponseEntity<>(productsImagesZipUploadResponse, HttpStatus.OK);
    }

    /**
     * GET /products/csv : 商品CSVDL
     * 商品CSVDL
     *
     * @param productCsvDownloadGetRequest 商品CSVDLリクエスト (optional)
     * @return 成功 (status code 200)
     * or その他エラー (status code 200)
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public ResponseEntity<Void> downloadCSV(@NotNull @ApiParam(value = "CSVダウンロードオプションリクエスト", required = true) @Valid
                                                            ProductCsvGetOptionRequest productCsvGetOptionRequest,
                                            @ApiParam(value = "商品CSVDLリクエスト") @Valid
                                                            ProductCsvDownloadGetRequest productCsvDownloadGetRequest) {

        try {
            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(
                            RequestContextHolder.getRequestAttributes())).getResponse();

            assert response != null;
            response.setCharacterEncoding("MS932");

            // Apache Common CSV を特化したCSVフォーマットを準備する
            // 主にHIT-MALL独自のCsvDownloadOptionDtoからCSVFormatに変換する
            CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
            Map<String, OptionContentDto> csvDownloadOption;

            // OptionIdを指定する場合
            if (productCsvGetOptionRequest.getOptionId() != null && StringUtils.isNotEmpty(
                            productCsvGetOptionRequest.getOptionId())) {
                // OptionIdでクエリモデルを取得する
                ProductCsvDownloadOptionDto optionDto = goodsCsvOptionGetByIdService.execute(
                                Integer.valueOf(productCsvGetOptionRequest.getOptionId()));

                // CsvダウンロードオプションDtoを作成する
                csvDownloadOptionDto.setOutputHeader(optionDto.isOutHeader());
                csvDownloadOption = new LinkedHashMap<>();
                for (OptionContentDto optionContentDto : optionDto.getOptionContent()) {
                    if (optionContentDto != null) {
                        csvDownloadOption.put(optionContentDto.getItemName(), optionContentDto);
                    }
                }
                // OptionIdを指定しない場合（デフォルト）
            } else {
                csvDownloadOptionDto.setOutputHeader(true);
                csvDownloadOption = CsvOptionUtil.getDefaultMapOptionContentDto(GoodsCsvDto.class);
            }

            CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(csvDownloadOptionDto);

            // Apache Common CSV を特化したCSVプリンター（設定したCSVフォーマットに沿ってデータを出力）を初期化する
            StringWriter stringWriter = new StringWriter();

            CSVPrinter printer = new CSVPrinter(stringWriter, outputCsvFormat);

            PrintWriter writer = response.getWriter();

            if (csvDownloadOptionDto.isOutputHeader()) {
                printer.printRecord(CsvDownloadHandler.outHeader(GoodsCsvDto.class, csvDownloadOption));
                writer.write(stringWriter.toString());
                stringWriter.getBuffer().setLength(0);
                writer.flush();
            }

            if (ListUtils.isEmpty(productCsvDownloadGetRequest.getGoodsSeqList())) {
                // 検索条件作成
                GoodsSearchForBackDaoConditionDto conditionDto =
                                productHelper.toGoodsSearchForBackDaoConditionDtoForSearch(
                                                productCsvDownloadGetRequest);

                try (Stream<GoodsCsvDto> goodsCsvDtoStream = goodsAllCsvDownloadLogic.execute(conditionDto)) {
                    goodsCsvDtoStream.forEach((goodsCsvDto -> {
                        try {
                            printer.printRecord(CsvDownloadHandler.outCsvRecord(goodsCsvDto, csvDownloadOption));
                            writer.write(stringWriter.toString());
                            stringWriter.getBuffer().setLength(0);
                        } catch (IOException e) {
                            LOGGER.error("例外処理が発生しました", e);
                        }
                    }));
                    writer.flush();
                }
            } else {
                // 出力対象会員SEQ一覧をパラメータから取得する
                List<Integer> goodsSeqList = productCsvDownloadGetRequest.getGoodsSeqList();

                try (Stream<GoodsCsvDto> goodsCsvDtoStream = goodsCsvDownloadLogic.execute(goodsSeqList)) {
                    goodsCsvDtoStream.forEach((goodsCsvDto -> {
                        try {
                            printer.printRecord(CsvDownloadHandler.outCsvRecord(goodsCsvDto, csvDownloadOption));
                            writer.write(stringWriter.toString());
                            stringWriter.getBuffer().setLength(0);
                        } catch (IOException e) {
                            LOGGER.error("例外処理が発生しました", e);
                        }
                    }));
                    writer.flush();
                }
            }
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /products/getListByGcd : 商品情報リスト取得（商品コード）
     * 商品情報リスト取得（商品コード）
     *
     * @param goodsCodeListRequest 商品コードリスト (optional)
     * @return 商品コードリストレスポンスクラス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<GoodsCodeListResponse> getEntityListByGoodsCodeList(
                    @NotNull @ApiParam(value = "商品コードリスト") @Valid GoodsCodeListRequest goodsCodeListRequest) {
        List<String> checkGoodsList = goodsDao.getEntityListByGoodsCodeList(goodsCodeListRequest.getGoodsCodeList());

        GoodsCodeListResponse goodsCodeListResponse = productHelper.toGoodsCodeListResponse(checkGoodsList);

        return new ResponseEntity<>(goodsCodeListResponse, HttpStatus.OK);
    }

    /**
     * GET /products/details/{goodsCode} : 商品詳細一覧取得
     * 商品詳細一覧取得
     *
     * @param goodsCode                         商品コード (required)
     * @param productDetailByGoodCodeGetRequest 商品詳細商品コード別取得リクエスト (required)
     * @return 商品詳細レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<GoodsDetailsResponse> getDetailsByGoodsCode(String goodsCode,
                                                                      @NotNull @Valid
                                                                                      ProductDetailByGoodCodeGetRequest productDetailByGoodCodeGetRequest) {
        HTypeSiteType siteType = HTypeSiteType.FRONT_PC;
        HTypeOpenDeleteStatus openStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                         productDetailByGoodCodeGetRequest.getOpenStatus()
                                                                        );

        GoodsDetailsDto goodsDetailsDto = goodsDetailsGetByCodeLogic.execute(SHOP_SEQ, goodsCode, siteType, openStatus);
        try {
            GoodsDetailsResponse goodsDetailsResponse = productHelper.toGoodsDetailsResponseFromDto(goodsDetailsDto);

            return new ResponseEntity<>(goodsDetailsResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /products/getDetailsBySeq/{goodsSeq} : 商品詳細一覧取得
     * 商品詳細一覧取得
     *
     * @param goodsSeq 商品SEQ (required)
     * @return 商品詳細レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ProductDetailsResponse> getDetailsByGoodsSeq(Integer goodsSeq) {

        GoodsDetailsDto goodsDetailsDto = goodsDetailsGetBySeqLogic.execute(goodsSeq);
        ProductDetailsResponse productDetailsResponse = productHelper.toProductDetailsResponse(goodsDetailsDto);

        return new ResponseEntity<>(productDetailsResponse, HttpStatus.OK);
    }

    /**
     * POST /products/sync/goods-stock-display
     *
     * @param goodsStockDisplayUpsertRequest 商品在庫表示アップサートリクエスト (required)
     * @return バッチ起動結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> syncUpsertGoodsStockDisplay(
                    @Valid GoodsStockDisplayUpsertRequest goodsStockDisplayUpsertRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.syncUpsertGoodsStockDisplay.routing");

        // メッセージ作成
        SyncUpsertGoodsStockDisplayQueueMessage message = new SyncUpsertGoodsStockDisplayQueueMessage();
        message.setGoodsStockDisplayUpsertRequest(goodsStockDisplayUpsertRequest);

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messagePublisherService.publish(routing, message);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("商品在庫表示アップサート" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /products/popularity/totaling-batch : 人気商品ランキング集計バッチ
     * 人気商品ランキング集計バッチ
     *
     * @param popularityTotalingBatchRequest 人気商品ランキング集計バッチリクエスト (required)
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> totalingPopularityBatch(
                    @ApiParam(value = "人気商品ランキング集計バッチリクエスト", required = true) @Valid @RequestBody
                                    PopularityTotalingBatchRequest popularityTotalingBatchRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.productPopularityTotalingBatch.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminId());
        message.setStartType(popularityTotalingBatchRequest.getStartType());

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messagePublisherService.publish(routing, message);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (AmqpException e) {

            LOGGER.error("人気商品ランキング集計バッチ" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /products/csv/option-download : CSVダウンロードオプション
     * CSVダウンロードオプション
     *
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ProductCsvOptionListResponse> getOptionCsv() {

        ProductCsvOptionListResponse productCsvOptionListResponse = new ProductCsvOptionListResponse();
        try {
            productCsvOptionListResponse =
                            productHelper.toProductCsvOptionListResponse(goodsCsvOptionListGetService.execute());
        } catch (JsonProcessingException e) {
            LOGGER.info("JSON コンバーター エラー" + e.getMessage());
        }

        return new ResponseEntity<>(productCsvOptionListResponse, HttpStatus.OK);
    }

    /**
     * PUT /products/csv/option-download : CSVダウンロードオプション更新
     * CSVダウンロードオプション更新
     *
     * @param productCsvOptionUpdateRequest CSV ダウンロードオプションのリクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> updateOption(@Valid ProductCsvOptionUpdateRequest productCsvOptionUpdateRequest) {

        ProductCsvDownloadOptionDto updateDto = productHelper.toProductCSVDLUpdateDto(productCsvOptionUpdateRequest);
        try {
            if (goodsCsvOptionUpdateService.execute(updateDto) == 0)
                throwMessage("PRODUCT-CSVDL0001-E");
        } catch (JsonProcessingException e) {
            LOGGER.info("JSON コンバーター エラー" + e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /products/csv/option-download/default : CSV ダウンロード オプション - デフォルトを取得
     * CSV ダウンロード オプション - デフォルトを取得
     *
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ProductCsvOptionResponse> getDefault() {
        ProductCsvDownloadOptionDto defaultProductCsvOption = productHelper.getDefaultProductCsvOption();
        ProductCsvOptionResponse defaultOptionResponse =
                        productHelper.toProductCsvOptionResponse(defaultProductCsvOption);

        return new ResponseEntity<>(defaultOptionResponse, HttpStatus.OK);
    }

    /**
     * 管理者SEQを取得する
     *
     * @return customerId 顧客ID
     */
    private Integer getAdminId() {
        if (this.headerParamsUtil.getAdministratorSeq() == null) {
            return null;
        } else {
            return Integer.valueOf(this.headerParamsUtil.getAdministratorSeq());
        }
    }

    /**
     * 検索キーワードログ登録
     *
     * @param request 商品グループ検索条件リクエスト
     * @param pageInfoResponse ページ情報レスポンス
     */
    private void writeSearchKeywordLog(ProductListGetRequest request, PageInfoResponse pageInfoResponse) {

        AccessSearchKeywordLoggingModule loggingModule = new AccessSearchKeywordLoggingModule();

        CategoryDto categoryDto = null;

        if (ObjectUtils.isNotEmpty(request.getCategoryId())) {
            categoryDto = this.categoryGetService.execute(request.getCategoryId());
        }

        loggingModule.setAccessTime(new Timestamp(System.currentTimeMillis()));
        loggingModule.setSiteType(HTypeSiteType.FRONT_PC.getLabel());
        loggingModule.setSearchKeyword(productHelper.createSearchKeyword(request));
        loggingModule.setSearchResultCount(pageInfoResponse.getTotal());
        if (categoryDto != null) {
            loggingModule.setSearchCategorySeq(ObjectUtils.isNotEmpty(categoryDto.getCategoryEntity()) ?
                categoryDto.getCategoryEntity().getCategorySeq() :
                null);
        }
        loggingModule.setSearchPriceFrom(request.getMinPrice());
        loggingModule.setSearchPriceTo(request.getMaxPrice());

        try {
            loggingModule.log("AccessSearchKeyword");
        } catch (JsonProcessingException e) {
            LOGGER.error("例外処理が発生しました", e);
            // -------------------
            // アプリケーションログを出力
            // -------------------
            ApplicationLogUtility applicationLogUtility =
                ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            applicationLogUtility.writeExceptionLog(e);
        }
    }

    /**
     * Update images position.
     *
     * @param goodsGroupCode 商品グループコード
     * @param imageItems     画像リスト
     * @param shopSeq        ショップSEQ
     */
    public void updateImagesPosition(String goodsGroupCode, List<GoodsImageItemRequest> imageItems, Integer shopSeq) {

        List<GoodsGroupImageEntity> goodsGroupImageListForUpdate = new ArrayList<>();
        GoodsGroupEntity entity = goodsGroupDao.getGoodsGroupByCode(shopSeq, goodsGroupCode, null, null, null, null);
        List<GoodsGroupImageEntity> masterGoodsGroupImageList =
            goodsGroupImageDao.getGoodsGroupImageListByGoodsGroupSeq(entity.getGoodsGroupSeq());

        for (GoodsGroupImageEntity goodsGroupImageEntity : masterGoodsGroupImageList) {
            for (GoodsImageItemRequest item : imageItems) {
                if (goodsGroupImageEntity.getImageTypeVersionNo().equals(item.getImageNo()) && !StringUtil.isEmpty(
                    item.getImagePath())) {
                    String imagePathDetail3ImagePcGroup = item.getImagePath();
                    String tmpImageDirPath = PropertiesUtil.getSystemPropertiesValue("tmp.path");
                    String goodsGroupImagePath = PropertiesUtil.getSystemPropertiesValue("images.path.goods");
                    String tmpImageFileName;
                    String imageFileName;
                    if (imagePathDetail3ImagePcGroup.contains(tmpImageDirPath)) {
                        tmpImageFileName = imagePathDetail3ImagePcGroup.replaceAll(tmpImageDirPath + "/", "");
                        String tmpImageFileNameDateTime =
                            tmpImageFileName.split("_")[tmpImageFileName.split("_").length - 1].split(
                                "\\.")[0];
                        imageFileName = tmpImageFileName.replaceAll("_" + tmpImageFileNameDateTime, "");
                        String imageFileNameWithGoodsCd = goodsGroupCode + "/" + imageFileName;
                        if (!imageFileNameWithGoodsCd.equals(goodsGroupImageEntity.getImageFileName())) {
                            goodsGroupImageEntity.setImageFileName(imageFileNameWithGoodsCd);
                            goodsGroupImageListForUpdate.add(goodsGroupImageEntity);
                        }
                    } else {
                        imageFileName = imagePathDetail3ImagePcGroup.replaceAll(goodsGroupImagePath + "/", "");
                        if (!imageFileName.equals(goodsGroupImageEntity.getImageFileName())) {
                            goodsGroupImageEntity.setImageFileName(imageFileName);
                            goodsGroupImageListForUpdate.add(goodsGroupImageEntity);
                        }
                    }
                    break;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(goodsGroupImageListForUpdate)) {
            // 画像の順番を更新
            for (GoodsGroupImageEntity updateEntity : goodsGroupImageListForUpdate) {
                goodsGroupImageDao.update(updateEntity);
            }
        }
    }
}