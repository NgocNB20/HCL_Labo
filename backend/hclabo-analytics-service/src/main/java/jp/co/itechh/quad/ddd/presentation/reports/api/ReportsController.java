package jp.co.itechh.quad.ddd.presentation.reports.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.queue.MessagePublisherService;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.core.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesSearchQueryCondition;
import jp.co.itechh.quad.ddd.usecase.reports.GoodsSaleUseCase;
import jp.co.itechh.quad.ddd.usecase.reports.OrderSalesUseCase;
import jp.co.itechh.quad.reports.presentation.api.ReportsApi;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSalesGetRequest;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSalesResponse;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesGetRequest;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesResponse;
import jp.co.itechh.quad.reports.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.reports.presentation.api.param.ReportRegistRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 受注・売上集計エンドポイント Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RestController
public class ReportsController implements ReportsApi {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsController.class);

    /**
     * 商品販売個数集計ユーザケース
     */
    private final GoodsSaleUseCase goodsSaleUseCase;

    /**
     * 受注・売上集計ヘルパー
     */
    private final ReportsHelper reportsHelper;

    /**
     * キューパブリッシャーサービス
     */
    private final MessagePublisherService batchPublisherService;

    /**
     * ヘッダパラメーターユーティル
     */
    private final HeaderParamsUtility headerParamsUtil;

    /**
     * 受注・売上集計ユースケース
     */
    private final OrderSalesUseCase orderSalesUseCase;

    /**
     * 集計用販売データパブリッシャーサービス
     */
    private final MessagePublisherService reportRegistPublisherService;

    /**
     * コンストラクタ
     *
     * @param goodsSaleUseCase      商品販売個数集計ユーザケース
     * @param reportsHelper         受注・売上集計ヘルパー
     * @param batchPublisherService キューパブリッシャーサービス
     * @param headerParamsUtil      ヘッダパラメーターユーティル
     * @param orderSalesUseCase     受注・売上集計ユースケース
     */
    @Autowired
    public ReportsController(GoodsSaleUseCase goodsSaleUseCase,
                             ReportsHelper reportsHelper,
                             MessagePublisherService batchPublisherService,
                             HeaderParamsUtility headerParamsUtil,
                             OrderSalesUseCase orderSalesUseCase,
                             MessagePublisherService reportRegistPublisherService) {
        this.goodsSaleUseCase = goodsSaleUseCase;
        this.reportsHelper = reportsHelper;
        this.batchPublisherService = batchPublisherService;
        this.headerParamsUtil = headerParamsUtil;
        this.orderSalesUseCase = orderSalesUseCase;
        this.reportRegistPublisherService = reportRegistPublisherService;
    }

    /**
     * GET /reports/goods-sales : 商品販売個数集計
     * 商品販売個数集計
     *
     * @param pageInfoRequest      ページ情報リクエスト（ページネーションのため） (required)
     * @param goodsSalesGetRequest 商品販売個数集計リクエスト (optional)
     * @return 商品販売個数集計レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<GoodsSalesResponse> getGoodsSales(
                    @NotNull @ApiParam(value = "ページ情報リクエスト（ページネーションのため）", required = true) @Valid
                    PageInfoRequest pageInfoRequest,
                    @ApiParam(value = "商品販売個数集計リクエスト") @Valid GoodsSalesGetRequest goodsSalesGetRequest) {

        try {
            GoodsSaleQueryCondition queryCondition =
                            reportsHelper.toGoodsSaleQueryCondition(goodsSalesGetRequest, pageInfoRequest);

            List<GoodsSaleQueryModel> goodsSaleQueryModels = goodsSaleUseCase.get(queryCondition);

            GoodsSalesResponse goodsSaleGetListResponse =
                            reportsHelper.toGoodsSaleGetListResponse(goodsSaleQueryModels, pageInfoRequest);

            return new ResponseEntity<>(goodsSaleGetListResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-GOODSALESE0001-E");
        }
    }

    /**
     * GET /reports/order-sales : 受注・売上集計
     *
     * @param orderSalesGetRequest 受注・売上集計リクエスト (optional)
     * @param pageInfoRequest      ページ情報リクエスト（ページネーションのため） (optional)
     * @return 検索キーワード一覧レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<OrderSalesResponse> getOrderSales(@Valid OrderSalesGetRequest orderSalesGetRequest,
                                                            @Valid PageInfoRequest pageInfoRequest) {

        try {
            OrderSalesSearchQueryCondition queryCondition =
                            reportsHelper.toOrderSalesSearchQueryCondition(orderSalesGetRequest, pageInfoRequest);

            List<OrderSalesQueryModel> orderSalesQueryModelList = orderSalesUseCase.get(queryCondition);

            OrderSalesResponse orderSalesResponse =
                            reportsHelper.toOrderSalesSearchQueryModelResponse(orderSalesQueryModelList,
                                                                               queryCondition
                                                                              );

            return new ResponseEntity<>(orderSalesResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-ORSS0001-E");
        }

    }

    /**
     * GET /reports/goods-sales-csv : 商品販売個数集計CSVダウンロード
     * 商品販売個数集計CSVダウンロード
     *
     * @param pageInfoRequest      ページ情報リクエスト（ページネーションのため） (required)
     * @param goodsSalesGetRequest 商品販売個数集計リクエスト (optional)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> downloadGoodsSales(
                    @NotNull @ApiParam(value = "ページ情報リクエスト（ページネーションのため）", required = true) @Valid
                    PageInfoRequest pageInfoRequest,
                    @ApiParam(value = "商品販売個数集計リクエスト") @Valid GoodsSalesGetRequest goodsSalesGetRequest) {

        GoodsSaleQueryCondition queryCondition =
                        reportsHelper.toGoodsSaleQueryConditionDownloadCsv(goodsSalesGetRequest, pageInfoRequest);

        // 受注情報ルーティングキー
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.goodssaledownloadordercsv.routing");

        // メッセージ作成
        QueueMessage message = new QueueMessage();
        message.setGoodsSaleQueryCondition(queryCondition);
        message.setAdminId(getAdminId());
        message.setType("zip");

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
     * GET /reports/order-sales-csv : 受注・売上集計CSVダウンロード
     * 受注・売上集計CSVダウンロード
     *
     * @param orderSalesGetRequest 受注・売上集計リクエスト (optional)
     * @param pageInfoRequest      ページ情報リクエスト（ページネーションのため） (optional)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> downloadOrderSales(
                    @ApiParam(value = "受注・売上集計リクエスト") @Valid OrderSalesGetRequest orderSalesGetRequest,
                    @ApiParam(value = "ページ情報リクエスト（ページネーションのため）") @Valid
                    PageInfoRequest pageInfoRequest) {

        OrderSalesSearchQueryCondition queryCondition =
                        reportsHelper.toOrderSalesSearchQueryCondition(orderSalesGetRequest);

        // 受注情報ルーティングキー
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.ordersalesdownloadcsv.routing");

        // メッセージ作成
        QueueMessage message = new QueueMessage();
        message.setOrderSalesSearchQueryCondition(queryCondition);
        message.setAdminId(getAdminId());
        message.setType("zip");

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

    /**
     * POST /reports/sales : 集計用販売データ登録
     * 集計用販売データ登録
     *
     * @param reportRegistRequest 販売データ登録リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> registReportData(@Valid ReportRegistRequest reportRegistRequest) {

        // 集計用販売データルーティングキー
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.reportsregist.routing");

        // メッセージ作成
        QueueMessage message = new QueueMessage();
        message.setTransactionId(reportRegistRequest.getTransactionId());
        message.setTransactionRevisionId(reportRegistRequest.getTransactionRevisionId());

        try {

            // キューにパブリッシュー
            reportRegistPublisherService.publish(routing, message);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-ORSA0001-E");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}