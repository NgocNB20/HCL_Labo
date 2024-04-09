package jp.co.itechh.quad.ddd.presentation.ordersearch.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.queue.MessagePublisherService;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.core.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.order.OrderSearchCsvOptionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.OrderSearchUseCase;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchCsvOptionQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryModel;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.ordersearch.presentation.api.param.CountOrderProductRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.CountOrderProductResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.DownloadFileTypeRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvGetOptionRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvGetRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionUpdateRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryModelListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchRegistUpdateRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.PageInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 受注検索 Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RestController
public class OrderSearchController implements OrderSearchApi {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSearchController.class);

    /**
     * キューパブリッシャーサービス
     */
    private final MessagePublisherService batchPublisherService;

    /**
     * 受注検索ユースケース
     */
    private final OrderSearchUseCase orderSearchUseCase;

    /**
     * 受注検索CSVオプションユースケース
     */
    private final OrderSearchCsvOptionUseCase orderSearchCsvOptionUseCase;

    /**
     * 受注検索Helperクラス
     */
    private final OrderSearchHelper orderSearchHelper;

    /**
     * ヘッダパラメーターユーティル
     */
    private final HeaderParamsUtility headerParamsUtil;

    /**
     * 変換Helper
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param batchPublisherService       キューパブリッシャーサービス
     * @param orderSearchUseCase          orderSearchUseCase
     * @param orderSearchCsvOptionUseCase
     * @param orderSearchHelper           orderSearchHelper
     * @param headerParamsUtil
     * @param conversionUtility
     */
    @Autowired
    public OrderSearchController(MessagePublisherService batchPublisherService,
                                 OrderSearchUseCase orderSearchUseCase,
                                 OrderSearchCsvOptionUseCase orderSearchCsvOptionUseCase,
                                 OrderSearchHelper orderSearchHelper,
                                 HeaderParamsUtility headerParamsUtil,
                                 ConversionUtility conversionUtility) {
        this.batchPublisherService = batchPublisherService;
        this.orderSearchUseCase = orderSearchUseCase;
        this.orderSearchCsvOptionUseCase = orderSearchCsvOptionUseCase;
        this.orderSearchHelper = orderSearchHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
    }

    /**
     * POST /order-search : 受注情報登録・更新
     * 受注情報を登録・更新する
     *
     * @param orderSearchRegistUpdateRequest 受注情報登録更新リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> registUpdate(
                    @ApiParam(value = "受注情報登録更新リクエスト", required = true) @Valid @RequestBody
                    OrderSearchRegistUpdateRequest orderSearchRegistUpdateRequest) {

        // 受注情報ルーティングキー
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.ordersearchregistupdate.routing");

        // メッセージ作成
        QueueMessage message = new QueueMessage();
        message.setOrderReceivedId(orderSearchRegistUpdateRequest.getOrderReceivedId());

        try {

            // キューにパブリッシュー
            batchPublisherService.publish(routing, message);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-OSRU0001-E");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /order-search : 受注検索
     * 受注検索
     *
     * @param orderSearchQueryRequest 受注検索条件 (required)
     * @param pageInfoRequest         ページ情報リクエスト（ページネーションのため） (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<OrderSearchQueryModelListResponse> get(
                    @NotNull @ApiParam(value = "受注検索条件", required = true) @Valid
                    OrderSearchQueryRequest orderSearchQueryRequest,
                    @NotNull @ApiParam(value = "ページ情報リクエスト（ページネーションのため）", required = true) @Valid
                    PageInfoRequest pageInfoRequest) {

        try {
            OrderSearchQueryCondition queryCondition =
                            orderSearchHelper.toOrderSearchQueryCondition(orderSearchQueryRequest, pageInfoRequest);

            List<OrderSearchQueryModel> orderSearchQueryModelList = orderSearchUseCase.get(queryCondition);
            int totalRecord = orderSearchUseCase.count(queryCondition);

            OrderSearchQueryModelListResponse response =
                            orderSearchHelper.toOrderSearchQueryModelResponse(orderSearchQueryModelList,
                                                                              pageInfoRequest, totalRecord
                                                                             );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-ORSE0001-E");
        }

    }

    /**
     * GET /order-search/csv : 受注CSVダウンロード
     * 受注CSVダウンロード
     *
     * @param orderSearchCsvGetRequest       受注検索CSVDLリクエスト (required)
     * @param orderSearchCsvGetOptionRequest CSVダウンロードオプションリクエスト (required)
     * @param pageInfoRequest                ページ情報リクエスト（ページネーションのため） (required)
     * @param downloadFileTypeRequest        ダウンロードファイルタイプ
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> downloadOrderCsv(
                    @NotNull @ApiParam(value = "受注検索CSVDLリクエスト", required = true) @Valid
                    OrderSearchCsvGetRequest orderSearchCsvGetRequest,
                    @NotNull @ApiParam(value = "CSVダウンロードオプションリクエスト", required = true) @Valid
                    OrderSearchCsvGetOptionRequest orderSearchCsvGetOptionRequest,
                    @NotNull @ApiParam(value = "ページ情報リクエスト（ページネーションのため）", required = true) @Valid
                    PageInfoRequest pageInfoRequest,
                    @ApiParam("ダウンロードファイルタイプ") @Valid DownloadFileTypeRequest downloadFileTypeRequest) {

        OrderSearchQueryCondition queryCondition =
                        orderSearchHelper.toDownloadQueryCondition(orderSearchCsvGetRequest, pageInfoRequest);

        // 受注情報ルーティングキー
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.ordersearchdownloadordercsv.routing");

        // メッセージ作成
        QueueMessage message = new QueueMessage();
        message.setOrderSearchQueryCondition(queryCondition);
        message.setAdminId(getAdminId());
        message.setType(downloadFileTypeRequest.getFileType());
        if (orderSearchCsvGetOptionRequest != null) {
            message.setOption(orderSearchCsvGetOptionRequest);
        }

        try {
            // キューにパブリッシュー
            batchPublisherService.publish(routing, message);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-OSCD0001-E");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /order-search/shipments-csv : 出荷検索CSVダウンロード
     * 出荷検索CSVダウンロード
     *
     * @param orderSearchCsvGetRequest       受注検索CSVDLリクエスト (required)
     * @param orderSearchCsvGetOptionRequest CSVダウンロードオプションリクエスト (required)
     * @param pageInfoRequest                ページ情報リクエスト（ページネーションのため） (required)
     * @param downloadFileTypeRequest        ダウンロードファイルタイプ
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> downloadShipmentCsv(
                    @NotNull @ApiParam(value = "受注検索CSVDLリクエスト", required = true) @Valid
                    OrderSearchCsvGetRequest orderSearchCsvGetRequest,
                    @NotNull @ApiParam(value = "CSVダウンロードオプションリクエスト", required = true) @Valid
                    OrderSearchCsvGetOptionRequest orderSearchCsvGetOptionRequest,
                    @NotNull @ApiParam(value = "ページ情報リクエスト（ページネーションのため）", required = true) @Valid
                    PageInfoRequest pageInfoRequest,
                    @ApiParam("ダウンロードファイルタイプ") @Valid DownloadFileTypeRequest downloadFileTypeRequest) {

        OrderSearchQueryCondition queryCondition =
                        orderSearchHelper.toDownloadQueryCondition(orderSearchCsvGetRequest, pageInfoRequest);

        // 受注情報ルーティングキー
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.ordersearchdownloadshipmentcsv.routing");

        // メッセージ作成
        QueueMessage message = new QueueMessage();
        message.setOrderSearchQueryCondition(queryCondition);
        message.setAdminId(getAdminId());
        message.setType(downloadFileTypeRequest.getFileType());
        if (orderSearchCsvGetOptionRequest != null) {
            message.setOption(orderSearchCsvGetOptionRequest);
        }

        try {
            // キューにパブリッシュー
            batchPublisherService.publish(routing, message);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-OSCD0001-E");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /order-search/order-product/count : 受注商品件数カウント
     * 受注商品件数カウント
     *
     * @param countOrderGoodsRequest 受注商品件数カウントリクエスト (required)
     * @return 成功 (status code 200)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CountOrderProductResponse> countOrderProduct(
                    @NotNull @ApiParam(value = "受注商品件数カウントリクエスト", required = true) @Valid
                    CountOrderProductRequest countOrderGoodsRequest) {

        int orderProductCount = orderSearchUseCase.countOrderProduct(countOrderGoodsRequest.getGoodsGroupCode(),
                                                                     conversionUtility.toTimestamp(
                                                                                     countOrderGoodsRequest.getTimeFrom())
                                                                    );
        CountOrderProductResponse countOrderProductResponse = new CountOrderProductResponse();
        countOrderProductResponse.setOrderGoodsCount(orderProductCount);

        return new ResponseEntity<>(countOrderProductResponse, HttpStatus.OK);
    }

    /**
     * GET /order-search/csv/option-download : CSVダウンロードオプション取得
     * CSVダウンロードオプション取得
     *
     * @return 成功 (status code 200)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderSearchCsvOptionListResponse> getOptionCsv() {
        try {
            OrderSearchCsvOptionListResponse responseList = orderSearchHelper.toOrderSearchCsvOptionListResponse(
                            orderSearchCsvOptionUseCase.getOrderSearchCsvOption());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-OSCD0002-E");
        }
    }

    /**
     * PUT /order-search/csv/option-download : CSVダウンロードオプション更新
     * CSVダウンロードオプション更新
     *
     * @return 成功 (status code 200)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> updateOption(
                    @Valid OrderSearchCsvOptionUpdateRequest orderSearchCsvOptionUpdateRequest) {
        try {
            orderSearchCsvOptionUseCase.updateOrderSearchCsvOption(orderSearchCsvOptionUpdateRequest);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-OSCD0002-E");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /order-search/csv/option-download/default : CSV ダウンロード オプション - デフォルトを取得
     * CSV ダウンロード オプション - デフォルトを取得
     *
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderSearchCsvOptionResponse> getDefault() {
        try {
            OrderSearchCsvOptionQueryModel queryModel = orderSearchHelper.getDefaultOrderSearchCsvOption();
            OrderSearchCsvOptionResponse response = orderSearchHelper.toOrderSearchCsvOptionResponse(queryModel);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-OSCD0002-E");
        }
    }

    /**
     * 管理者SEQを取得するcd
     *
     * @return Integer 管理者ID
     */
    private Integer getAdminId() {
        if (this.headerParamsUtil.getAdministratorSeq() == null) {
            return null;
        } else {
            return Integer.valueOf(this.headerParamsUtil.getAdministratorSeq());
        }
    }
}