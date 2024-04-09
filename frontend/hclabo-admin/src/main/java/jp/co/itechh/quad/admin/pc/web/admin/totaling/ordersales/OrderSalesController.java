package jp.co.itechh.quad.admin.pc.web.admin.totaling.ordersales;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.OrderSalesDataDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.download.DownloadApiClient;
import jp.co.itechh.quad.admin.pc.web.admin.util.IdenticalDataCheckUtil;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodLinkListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.reports.presentation.api.ReportsApi;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesGetRequest;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesResponse;
import jp.co.itechh.quad.reports.presentation.api.param.PageInfoRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 受注・売上集計コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/ordersales")
@Controller
@SessionAttributes(value = "orderSalesModel")
@PreAuthorize("hasAnyAuthority('REPORT:4')")
public class OrderSalesController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSalesController.class);

    /**
     * 受注・売上集計Helper
     */
    private final OrderSalesHelper orderSalesHelper;

    /**
     * レポート API
     **/
    private final ReportsApi reportsApi;

    /** 決済方法API */
    private final SettlementMethodApi settlementMethodApi;

    /**
     * ダウンロードAPIクライアント
     */
    private final DownloadApiClient downloadApiClient;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * デフォルト：ページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト：最大表示件数
     */
    private static final int DEFAULT_LIMIT = 100;

    private final static String SEARCH_ORDER_SALES_DOWNLOAD = "/reports/orderSales/csv";

    private String fileName = "t_SearchOrderSales";

    /** CSVのファイルパス */
    public static final String ORDER_SALES_FILE_PATH = "orderSalesCsvAsynchronous.file.path";

    /** メッセージ */
    public static final String MESSAGE_VALID_DOWNLOAD_CSV_NOT_EXIST = "DOWNLOADCSV-002-E";

    /** CSVのファイ名 */
    private static final String ORDER_SALES_FILE_NAME_REGEX = "^t_Order\\d{8}_\\d{6}(\\.zip|\\.csv)$";

    /**
     * コンストラクター
     * @param orderSalesHelper      受注・売上集計Helper
     * @param conversionUtility     変換ユーティリティクラス
     * @param reportsApi            レポート API
     * @param settlementMethodApi   決済方法API
     * @param downloadApiClient     ダウンロードAPIクライアント
     */
    public OrderSalesController(OrderSalesHelper orderSalesHelper,
                                ConversionUtility conversionUtility,
                                ReportsApi reportsApi,
                                SettlementMethodApi settlementMethodApi,
                                DownloadApiClient downloadApiClient) {
        this.orderSalesHelper = orderSalesHelper;
        this.conversionUtility = conversionUtility;
        this.reportsApi = reportsApi;
        this.settlementMethodApi = settlementMethodApi;
        this.downloadApiClient = downloadApiClient;
    }

    /**
     * 初期表示処理を行います
     *
     * @param model
     * @return ページクラス
     */
    @GetMapping("/")
    public String doLoadIndex(Model model) {

        OrderSalesModel orderSalesModel = new OrderSalesModel();

        model.addAttribute("orderSalesModel", orderSalesModel);

        clearModel(OrderSalesModel.class, orderSalesModel, model);

        initComponentValue(orderSalesModel);

        return "totaling/ordersales/index";
    }

    /**
     * 受注・売上集計処理を実行します
     *
     * @param orderSalesModel 受注・売上集計ページ
     * @param error                       エラー
     * @param model                       the model
     * @return
     */
    @PostMapping(value = "/", params = "doReportOutput")
    @HEHandler(exception = AppLevelListException.class, returnView = "totaling/ordersales/index")
    public String doReportOutput(@Validated OrderSalesModel orderSalesModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (error.hasErrors()) {
            return "totaling/ordersales/index";
        }

        // 受注・売上集計リクエストに変換
        OrderSalesGetRequest orderSalesGetRequest = orderSalesHelper.toOrderSalesGetRequest(orderSalesModel);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest, conversionUtility.toInteger(orderSalesModel.getPageNumber()),
                                        orderSalesModel.getLimit(), null, null, DEFAULT_LIMIT
                                       );

        try {
            // 受注・売上集計
            OrderSalesResponse orderSalesResponse = reportsApi.getOrderSales(orderSalesGetRequest, pageInfoRequest);

            List<OrderSalesDataDto> resultDataItem = orderSalesHelper.toOrderSalesTotalDtoList(orderSalesResponse);

            // 検索結果リストを設定
            orderSalesModel.setResultOrderSalesDataItems(resultDataItem);

            if (Boolean.TRUE.equals(orderSalesResponse.getOverFlag())) {
                // メッセージを設定
                addMessage("ARX000101", new Object[] {pageInfoRequest.getLimit()}, redirectAttributes, model);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(IdenticalDataCheckUtil.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/totaling/ordersales/";
        }

        return "totaling/ordersales/index";
    }

    /**
     * 受注・売上集計CSV出力処理を実行します
     *
     * @param response
     * @param orderSalesModel 受注・売上集計ページ
     * @param error           エラー
     * @param model           the model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('REPORT:8')")
    @PostMapping(value = "/", params = "doCsvOutput")
    @HEHandler(exception = AppLevelListException.class, returnView = "totaling/ordersales/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "totaling/ordersales/index")
    public String doCsvOutput(HttpServletResponse response,
                              @Validated OrderSalesModel orderSalesModel,
                              BindingResult error,
                              Model model) {

        if (error.hasErrors()) {
            return "totaling/ordersales/index";
        }

        try {
            // 受注・売上集計リクエストに変換
            OrderSalesGetRequest orderSalesGetRequest = orderSalesHelper.toOrderSalesGetRequest(orderSalesModel);

            reportsApi.downloadOrderSales(orderSalesGetRequest, new PageInfoRequest());

            orderSalesModel.setSelectOrderSalesCSVFlag(true);

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        return "totaling/ordersales/index";
    }

    /**
     * CSV非同期処理結果のダウンロード<br/>
     *
     * @param file 対象ファイル名
     * @return ファイル出力先パス
     */
    @PreAuthorize("hasAnyAuthority('REPORT:8')")
    @PostMapping(value = "/", params = "doDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    public ResponseEntity<byte[]> doDownload(@RequestParam(required = false) String file) {

        Path path = null;
        if (StringUtils.isNotEmpty(file)) {

            if (file.contains("/")) {
                throwMessage(MESSAGE_VALID_DOWNLOAD_CSV_NOT_EXIST);
            }

            if (file.matches(ORDER_SALES_FILE_NAME_REGEX)) {
                path = Paths.get(PropertiesUtil.getSystemPropertiesValue(ORDER_SALES_FILE_PATH) + file);
            }
        }

        // ファイルの存在確認
        if (ObjectUtils.isEmpty(path) || !Files.exists(path)) {
            throwMessage(MESSAGE_VALID_DOWNLOAD_CSV_NOT_EXIST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=\"" + file + "\"");

        try {
            return new ResponseEntity<>(Files.readAllBytes(path), headers, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(MESSAGE_VALID_DOWNLOAD_CSV_NOT_EXIST);
            return null;
        }
    }

    /**
     * 出力CSVファイル名を設定する
     *
     */
    public String getFileName() {
        if (StringUtils.isEmpty(this.fileName)) {
            this.fileName = "download_file";
        }
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);
        return csvUtility.getDownLoadCsvFileName(this.fileName);
    }

    /**
     * コンポーネント値初期化
     *
     * @param orderSalesModel
     */
    public void initComponentValue(OrderSalesModel orderSalesModel) {

        orderSalesModel.setLimit(DEFAULT_LIMIT);
        orderSalesModel.setPageNumber(DEFAULT_PNUM);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 今月の初日、末日を取得し、集計期間にセット
        Timestamp currentDate = dateUtility.getCurrentDate();
        Timestamp startDate = dateUtility.getStartOfMonth(0, currentDate);
        Timestamp endDate = dateUtility.getEndOfMonth(0, currentDate);

        orderSalesModel.setProcessDateFrom(dateUtility.formatYmdWithSlash(startDate));
        orderSalesModel.setProcessDateTo(dateUtility.formatYmdWithSlash(endDate));
        orderSalesModel.setOrderDeviceTypeItems(EnumTypeUtil.getEnumMap(HTypeDeviceType.class));

        // すべて リンク決済方法取得一覧
        PaymentMethodListResponse paymentMethodListResponse = settlementMethodApi.get();
        if (orderSalesHelper.toPaymentMethod(paymentMethodListResponse, orderSalesModel)) {
            PaymentMethodLinkListResponse paymentMethodLinkListResponse = settlementMethodApi.getPaymentsMethodLink();
            orderSalesHelper.toLinkPaymentMethod(paymentMethodLinkListResponse, orderSalesModel);
        }

    }
}