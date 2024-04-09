package jp.co.itechh.quad.admin.pc.web.admin.other.batchmanagement;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.admin.base.application.FacesMessage;
import jp.co.itechh.quad.admin.base.application.HmMessages;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.batch.core.dto.BatchManagementDetailDto;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchName;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeManualBatch;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.other.batchmanagement.validator.group.ExecuteGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.batchmanagement.presentation.api.BatchManagementApi;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementGetRequest;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementListResponse;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.linkpay.presentation.api.LinkPayApi;
import jp.co.itechh.quad.officezipcodeupdate.presentation.api.OfficezipcodeUpdateApi;
import jp.co.itechh.quad.officezipcodeupdate.presentation.api.param.OfficeZipCodeUpdateExecuteRequest;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PopularityTotalingBatchRequest;
import jp.co.itechh.quad.stockdisplay.presentation.api.StockDisplayApi;
import jp.co.itechh.quad.stockdisplay.presentation.api.param.StockDisplayExecuteRequest;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.zipcodeupdate.presentation.api.ZipcodeUpdateApi;
import jp.co.itechh.quad.zipcodeupdate.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.zipcodeupdate.presentation.api.param.ZipCodeUpdateExecuteRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * バッチ管理画面コントローラー。<br />
 *
 * <pre>
 * 検索条件を元に検索結果を一覧表示する。
 * </pre>
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@RequestMapping("/batchmanagement")
@Controller
@SessionAttributes(value = "batchManagementModel")
@PreAuthorize("hasAnyAuthority('BATCH:4')")
public class BatchManagementController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchManagementController.class);

    /**
     * バッチ検索：デフォルトページ番号
     */
    private static final String DEFAULT_BATCHSEARCH_PNUM = "1";

    /**
     * バッチ検索：デフォルトソート
     */
    private static final boolean DEFAULT_BATCHSEARCH_ORDER_DESC = false;

    /**
     * バッチ検索：デフォソート項目
     */
    private static final String DEFAULT_BATCHSEARCH_ORDER_FIELD = "startTime";

    /**
     * バッチ管理Helper
     */
    private final BatchManagementHelper helper;

    /**
     * バッチ管理画面 API
     **/
    private final BatchManagementApi batchManagementApi;

    /**
     * 日付のフォーマット
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    /**
     * 郵便番号自動更新 API
     **/
    private final ZipcodeUpdateApi zipcodeUpdateApi;

    /**
     * 事業所の個別郵便番号住所クラス API
     **/
    private final OfficezipcodeUpdateApi officezipcodeUpdateApi;

    /**
     * 商品グループ在庫状態更新 API
     **/
    private final StockDisplayApi stockDisplayApi;

    /**
     * 商品 API
     **/
    private final ProductApi productApi;

    /** 取引API */
    private final TransactionApi transactionApi;

    /** リンク決済API */
    private final LinkPayApi linkPayApi;

    /**
     * コンストラクター
     */
    @Autowired
    public BatchManagementController(BatchManagementHelper helper,
                                     BatchManagementApi batchManagementApi,
                                     ZipcodeUpdateApi zipcodeUpdateApi,
                                     OfficezipcodeUpdateApi officezipcodeUpdateApi,
                                     StockDisplayApi stockDisplayApi,
                                     ProductApi productApi,
                                     TransactionApi transactionApi,
                                     LinkPayApi linkPayApi) {
        this.helper = helper;
        this.batchManagementApi = batchManagementApi;
        this.zipcodeUpdateApi = zipcodeUpdateApi;
        this.officezipcodeUpdateApi = officezipcodeUpdateApi;
        this.stockDisplayApi = stockDisplayApi;
        this.productApi = productApi;
        this.transactionApi = transactionApi;
        this.linkPayApi = linkPayApi;
    }

    /**
     * 初期処理
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @param model                Model
     * @return 検索一覧画面 string
     */
    @GetMapping("/")
    public String doLoad(BatchManagementModel batchManagementModel, Model model) {

        clearModel(BatchManagementModel.class, batchManagementModel, model);

        batchManagementModel.setBatchtypesItems(EnumTypeUtil.getEnumMap(HTypeBatchName.class));
        batchManagementModel.setTaskstatusesItems(EnumTypeUtil.getEnumMap(HTypeBatchStatus.class));
        batchManagementModel.setManualBatchItems(EnumTypeUtil.getEnumMap(HTypeManualBatch.class));

        return "other/batchmanagement/index";
    }

    /**
     * バッチタスク情報検索処理を実行します
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @param error                BindingResult
     * @param redirectAttributes   RedirectAttributes
     * @param model                Model
     * @return 検索一覧画面
     */
    @PostMapping(value = "/", params = "doSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "other/batchmanagement/index")
    public String doSearch(@Validated(SearchGroup.class) BatchManagementModel batchManagementModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (error.hasErrors()) {
            return "other/batchmanagement/index";
        }

        // ページ番号を初期設定
        batchManagementModel.setPageNumber(DEFAULT_BATCHSEARCH_PNUM);

        search(batchManagementModel, error, model);

        return "other/batchmanagement/index";
    }

    /**
     * ページング処理を行います
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @param error                BindingResult
     * @param model                Model
     * @return 検索一覧画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    public String doDisplayChange(@Validated(DisplayChangeGroup.class) BatchManagementModel batchManagementModel,
                                  BindingResult error,
                                  Model model) {
        if (error.hasErrors()) {
            return "other/batchmanagement/index";
        }

        search(batchManagementModel, error, model);

        return "other/batchmanagement/index";
    }

    /**
     * バッチタスク詳細情報を検索します
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @param model                Model
     * @return レポート
     */
    @PostMapping(value = "/", params = "doReport")
    public String doReport(BatchManagementModel batchManagementModel, Model model) {
        BatchManagementReportItem batchManagementReportItem = helper.getBatchManagementReportItem(batchManagementModel);

        model.addAttribute("batchManagementReportItem", batchManagementReportItem);
        return "other/batchmanagement/report";
    }

    /**
     * バッチタスク詳細情報を検索します
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @param model
     * @return レポート
     */
    @PostMapping(value = "/doReportAjax")
    @ResponseBody
    public ResponseEntity<?> doReportAjax(BatchManagementModel batchManagementModel, Model model) {
        BatchManagementReportItem batchManagementReportItem = helper.getBatchManagementReportItem(batchManagementModel);

        batchManagementReportItem.setAccepttimeStr(convertTime(batchManagementReportItem.getAccepttime()));
        batchManagementReportItem.setTerminatetimeStr(convertTime(batchManagementReportItem.getTerminatetime()));

        return ResponseEntity.ok(batchManagementReportItem);
    }

    /**
     * バッチの手動起動処理を実行します
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @param error                エラー
     * @param redirectAttributes   the redirect attributes
     * @param model                the model
     * @return string
     */
    @PreAuthorize("hasAnyAuthority('BATCH:8')")
    @PostMapping(value = "/", params = "doExecute")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @HEHandler(exception = AppLevelListException.class, returnView = "other/batchmanagement/index")
    public String doExecute(@Validated(ExecuteGroup.class) BatchManagementModel batchManagementModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (error.hasErrors()) {
            return "other/batchmanagement/index";
        }

        if (batchManagementModel.getManualBatch() == null) {
            throwMessage("AGS000105", new Object[] {"手動起動バッチ名"});
        } else {
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

            // バッチ名からバッチのジョッブを取得する
            switch (batchManagementModel.getManualBatch()) {
                case "BATCH_PRODUCT_POPULARITY_TOTALING":
                    try {
                        PopularityTotalingBatchRequest popularityTotalingBatchRequest =
                                        new PopularityTotalingBatchRequest();
                        popularityTotalingBatchRequest.setStartType(HTypeBatchStartType.MANUAL.getValue());
                        jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        productApi.totalingPopularityBatch(popularityTotalingBatchRequest);
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                   jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse.class
                                                                  );
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                case "BATCH_ZIP_CODE":
                    try {
                        ZipCodeUpdateExecuteRequest zipCodeUpdateExecuteRequest = new ZipCodeUpdateExecuteRequest();

                        zipCodeUpdateExecuteRequest.setStartType(HTypeBatchStartType.MANUAL.getValue());
                        zipCodeUpdateExecuteRequest.allFlag(false);
                        BatchExecuteResponse zipcodeUpdateResponse =
                                        zipcodeUpdateApi.execute(zipCodeUpdateExecuteRequest);
                        setBatchExecuteResult(zipcodeUpdateResponse.getExecuteCode(),
                                              zipcodeUpdateResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        BatchExecuteResponse zipcodeUpdateResponse =
                                        conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                   BatchExecuteResponse.class
                                                                  );
                        setBatchExecuteResult(zipcodeUpdateResponse.getExecuteCode(),
                                              zipcodeUpdateResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                case "BATCH_OFFICE_ZIP_CODE":
                    try {
                        OfficeZipCodeUpdateExecuteRequest officeZipCodeUpdateExecuteRequest =
                                        new OfficeZipCodeUpdateExecuteRequest();

                        officeZipCodeUpdateExecuteRequest.setStartType(HTypeBatchStartType.MANUAL.getValue());
                        officeZipCodeUpdateExecuteRequest.setAllFlag(false);
                        jp.co.itechh.quad.officezipcodeupdate.presentation.api.param.BatchExecuteResponse
                                        officezipcodeUpdateResponse =
                                        officezipcodeUpdateApi.execute(officeZipCodeUpdateExecuteRequest);
                        setBatchExecuteResult(officezipcodeUpdateResponse.getExecuteCode(),
                                              officezipcodeUpdateResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        jp.co.itechh.quad.officezipcodeupdate.presentation.api.param.BatchExecuteResponse
                                        officezipcodeUpdateResponse =
                                        conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                   jp.co.itechh.quad.officezipcodeupdate.presentation.api.param.BatchExecuteResponse.class
                                                                  );
                        setBatchExecuteResult(officezipcodeUpdateResponse.getExecuteCode(),
                                              officezipcodeUpdateResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                case "BATCH_STOCKSTATUSDISPLAY_UPDATE":
                    try {
                        StockDisplayExecuteRequest stockDisplayExecuteRequest = new StockDisplayExecuteRequest();

                        stockDisplayExecuteRequest.setStartType(HTypeBatchStartType.MANUAL.getValue());
                        jp.co.itechh.quad.stockdisplay.presentation.api.param.BatchExecuteResponse
                                        stockDisplayResponse = stockDisplayApi.execute(stockDisplayExecuteRequest);
                        setBatchExecuteResult(stockDisplayResponse.getExecuteCode(),
                                              stockDisplayResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        jp.co.itechh.quad.stockdisplay.presentation.api.param.BatchExecuteResponse
                                        stockDisplayResponse = conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                                          jp.co.itechh.quad.stockdisplay.presentation.api.param.BatchExecuteResponse.class
                                                                                         );
                        setBatchExecuteResult(stockDisplayResponse.getExecuteCode(),
                                              stockDisplayResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                case "BATCH_MULPAY_NOTIFICATION_RECOVERY":
                    try {
                        jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        transactionApi.mulPayNotificationRecoveryBatch();
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                   jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse.class
                                                                  );
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                case "BATCH_REMINDER_PAYMENT":
                    try {
                        jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        transactionApi.paymentReminderBatch();
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                   jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse.class
                                                                  );
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                case "BATCH_EXPIRED_PAYMENT":
                    try {
                        jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        transactionApi.paymentExpiredBatch();
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                   jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse.class
                                                                  );
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                case "BATCH_LINK_PAY_CANCEL_REMINDER":
                    try {
                        jp.co.itechh.quad.linkpay.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        linkPayApi.cancelForgetReminderBatch();
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    } catch (HttpServerErrorException | HttpClientErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        jp.co.itechh.quad.linkpay.presentation.api.param.BatchExecuteResponse batchExecuteResponse =
                                        conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                   jp.co.itechh.quad.linkpay.presentation.api.param.BatchExecuteResponse.class
                                                                  );
                        setBatchExecuteResult(batchExecuteResponse.getExecuteCode(),
                                              batchExecuteResponse.getExecuteMessage(), model
                                             );
                    }
                    if (error.hasErrors()) {
                        return "other/batchmanagement/index";
                    }
                    break;
                default:
                    break;
            }
        }

        // 検索結果再初期化
        batchManagementModel.setResultItems(null);

        return "other/batchmanagement/index";
    }

    /**
     * 検索処理
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @param model                Model
     */
    private void search(BatchManagementModel batchManagementModel, BindingResult error, Model model) {
        try {
            BatchManagementGetRequest batchManagementGetRequest =
                            helper.toBatchManagementGetRequest(batchManagementModel);

            PageInfoRequest pageInfoRequest = new PageInfoRequest();

            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageRequest(pageInfoRequest,
                                            IntegerConversionUtil.toInteger(batchManagementModel.getPageNumber()),
                                            batchManagementModel.getLimit(), DEFAULT_BATCHSEARCH_ORDER_FIELD,
                                            DEFAULT_BATCHSEARCH_ORDER_DESC
                                           );

            BatchManagementListResponse batchManagementListResponse =
                            batchManagementApi.batchManagement(batchManagementGetRequest, pageInfoRequest);
            List<BatchManagementDetailDto> dtoList =
                            helper.toBatchManagementDetailDtoList(batchManagementListResponse.getBatchManagementList());

            List<BatchManagementReportItem> resultItems = new ArrayList<>();
            Integer index = 1;
            for (BatchManagementDetailDto detailDto : dtoList) {
                BatchManagementReportItem reportItem = helper.convertToBatchManagementReportItem(detailDto, index);
                resultItems.add(reportItem);
                index++;
            }

            batchManagementModel.setResultItems(resultItems);

            // ページャーにレスポンス情報をセット
            PageInfoResponse pageInfoResponse = batchManagementListResponse.getPageInfo();

            if (ObjectUtils.isNotEmpty(pageInfoResponse)) {
                pageInfoModule.setupPageInfo(batchManagementModel, pageInfoResponse.getPage(),
                                             pageInfoResponse.getLimit(), pageInfoResponse.getNextPage(),
                                             pageInfoResponse.getPrevPage(), pageInfoResponse.getTotal(),
                                             pageInfoResponse.getTotalPages()
                                            );
            }
            // ページャーセットアップ
            pageInfoModule.setupViewPager(batchManagementModel.getPageInfo(), batchManagementModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("batchName", "batchtypes");
            itemNameAdjust.put("status", "taskstatuses");
            itemNameAdjust.put("createTime", "accepttimeFrom");
            itemNameAdjust.put("endTime", "accepttimeTo");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 日付のフォーマット
     *
     * @param ts
     * @return String formattedDate
     */
    private String convertTime(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        return ts.toLocalDateTime().format(FORMATTER);
    }

    /**
     * バッチ起動結果表示
     *
     * @param code    コード
     * @param message メッセージ
     * @param model   Model
     */
    private void setBatchExecuteResult(String code, String message, Model model) {
        // HIT-MALLメッセージ
        HmMessages hmMessages = new HmMessages();

        // グローバルエラー追加
        AppLevelFacesMessage appLevelFacesMessage;
        if (HTypeBatchResult.COMPLETED.getValue().equals(code)) {
            appLevelFacesMessage = new AppLevelFacesMessage(new FacesMessage(message));
        } else {
            appLevelFacesMessage = new AppLevelFacesMessage(message);
        }
        hmMessages.add(appLevelFacesMessage);

        // グローバルエラー設定
        model.addAttribute("allMessages", hmMessages);
    }
}