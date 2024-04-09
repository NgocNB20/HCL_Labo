package jp.co.itechh.quad.admin.pc.web.admin.order;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeDate;
import jp.co.itechh.quad.admin.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderOutData;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSite;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderType;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePerson;
import jp.co.itechh.quad.admin.constant.type.HTypeSelectMapOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSelectionOrderOutData;
import jp.co.itechh.quad.admin.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.admin.dto.common.CheckMessageDto;
import jp.co.itechh.quad.admin.dto.order.OrderSearchConditionDto;
import jp.co.itechh.quad.admin.dto.order.OrderSearchOrderResultDto;
import jp.co.itechh.quad.admin.dto.order.ShipmentRegistDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.OptionDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.CouponValidator;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.OrderSearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.OutputGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.SelectShipmentRegistGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.SelectionOutput1Group;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.ShipmentSearchGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.ConfigInfoResponse;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.ordersearch.presentation.api.param.DownloadFileTypeRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvGetOptionRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvGetRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionUpdateRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryModelListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.MessageContent;
import jp.co.itechh.quad.transaction.presentation.api.param.MessageResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionShipmentInfo;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionShipmentRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.ListUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 受注検索アクション<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@RequestMapping("/order")
@Controller
@SessionAttributes(value = "orderModel")
@PreAuthorize("hasAnyAuthority('ORDER:4')")
public class OrderController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    /** フロントチェック：不正操作エラー */
    private static final String MSGCD_PARAM_ERROR = "AOX002201";

    /** 受注検索：NONE_LIMIT */
    private static final int NONE_LIMIT = -1;

    /** 受注検索：デフォルトページ番号 */
    private static final String DEFAULT_ORDERSEARCH_PNUM = "1";

    /** 受注検索：デフォルト：ソート項目 */
    private static final String DEFAULT_ORDERSEARCH_ORDER_FIELD = "orderTime";

    /** 受注検索：デフォルト：ソート条件(昇順/降順) */
    private static final boolean DEFAULT_ORDERSEARCH_ORDER_ASC = false;

    /** 受注検索：デフォルト：最大表示件数 */
    private static final int DEFAULT_ORDERSEARCH_LIMIT = 100;

    /** 表示モード(md):list 検索画面の再検索実行 */
    public static final String MODE_LIST = "list";

    /** ダウンロードファイル：ZIP形式 */
    public static final String DOWNLOAD_ZIP = "zip";

    /** CSVのファイルパス */
    public static final String ORDERCSV_FILE_PATH = "orderCsvAsynchronous.file.path";
    public static final String SHIPMENT_FILE_PATH = "shipmentCsvAsynchronous.file.path";

    public static final String MESSAGE_VALID_DOWNLOAD_CSV = "DOWNLOADCSV-001-E";

    public static final String MESSAGE_VALID_DOWNLOAD_CSV_NOT_EXIST = "DOWNLOADCSV-002-E";

    /** 受注検索: デフォルトページ番号 */
    private static final Integer DEFAULT_PNUM = 1;

    /** 受注検索helper */
    private final OrderHelper orderHelper;

    /** 決済方法用動的バリデータ */
    private final CouponValidator couponValidator;

    /** 取引API（注文フロー） */
    private final TransactionApi transactionApi;

    /** 受注検索 */
    private final OrderSearchApi orderSearchApi;

    /** 受注検索Helperクラス */
    private final OrderSearchHelper orderSearchHelper;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 配送方法Api */
    private final ShippingMethodApi shippingMethodApi;

    /** 決済方法API */
    private final SettlementMethodApi settlementMethodApi;

    /** 検査API */
    private final ExaminationApi examinationApi;

    /** CSVのファイ名 */
    private static final String ORDER_FILE_NAME_REGEX = "^order\\d{8}_\\d{6}(\\.zip|\\.csv)$";

    /** CSVのファイ名 */
    private static final String SHIPMENT_FILE_NAME_REGEX = "^shipment\\d{8}_\\d{6}(\\.zip|\\.csv)$";

    /** コンストラクタ */
    @Autowired
    public OrderController(OrderHelper orderHelper,
                           CouponValidator couponValidator,
                           TransactionApi transactionApi,
                           OrderSearchApi orderSearchApi,
                           OrderSearchHelper orderSearchHelper,
                           ConversionUtility conversionUtility,
                           ShippingMethodApi shippingMethodApi,
                           SettlementMethodApi settlementMethodApi,
                           ExaminationApi examinationApi) {
        this.orderHelper = orderHelper;
        this.couponValidator = couponValidator;
        this.transactionApi = transactionApi;
        this.orderSearchApi = orderSearchApi;
        this.orderSearchHelper = orderSearchHelper;
        this.conversionUtility = conversionUtility;
        this.shippingMethodApi = shippingMethodApi;
        this.settlementMethodApi = settlementMethodApi;
        this.examinationApi = examinationApi;
    }

    @InitBinder("orderModel")
    public void initBinder(WebDataBinder error) {
        // 検索条件クーポンの動的バリデータをセット
        error.addValidators(couponValidator);
    }

    /**
     * アクション実行前に処理結果表示をクリア
     * @param orderModel 受注検索モデル
     */
    public void preDoAction(OrderModel orderModel) {
        orderModel.setCheckMessageItems(null);
        if (orderModel.getPageNumber() == null) {
            orderModel.setPageNumber(DEFAULT_ORDERSEARCH_PNUM);
        }
    }

    /**
     *
     * 初期処理<br/>
     *
     * @return 自画面
     */
    @GetMapping(value = "/")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              @RequestParam(required = false) Optional<Integer> memberInfoSeq,
                              OrderModel orderModel,
                              BindingResult error,
                              RedirectAttributes redirectAttrs,
                              Model model) {

        // 会員詳細画面からの遷移の場合、パラメータで取得した会員SEQを条件に検索を行う
        if (md.isPresent() && MODE_LIST.equals(md.get())) {
            // 受注詳細 ⇒ 戻る の流れで
            // 実施する際に、pageLimitが未設定のケースがあったため、こちらのロジックを追加する
            if (orderModel.getLimit() == 0) {
                orderModel.setLimit(orderModel.getPageDefaultLimitModel());
            }
            orderHelper.toOrderSearchWhenMemberInfoDetails(orderModel);
            doOrderSearch(orderModel, error, model);
        } else if (memberInfoSeq.isPresent()) {
            // 会員詳細 の流れで
            clearModel(OrderModel.class, orderModel, model);
            orderModel.setMemberInfoSeq(String.valueOf(memberInfoSeq.get()));
        } else {
            clearModel(OrderModel.class, orderModel, model);
        }

        // システムは環境設定情報
        ConfigInfoResponse configInfoResponse = examinationApi.getConfigInfo();

        // プルダウンアイテム情報を取得
        initComponentValue(orderModel, configInfoResponse);

        // 受注商品単位で絞り込む
        orderModel.setFilterOrderedProductFlag(true);

        // 実行前処理
        preDoAction(orderModel);

        return "order/index";
    }

    /**
     *
     * 画面表示切替処理<br/>
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/index")
    public String doDisplayChange(@Validated(DisplayChangeGroup.class) OrderModel orderModel,
                                  BindingResult error,
                                  Model model) {

        // 実行前処理
        preDoAction(orderModel);

        if (error.hasErrors()) {
            return "order/index";
        }

        // 検索結果チェック
        resultListCheck(orderModel);
        // 受注検索受注一覧取得処理実行
        searchOrder(true, orderModel, model);

        return "order/index";
    }

    /* Search Action */

    /**
     *
     * 受注番号別一覧表示処理<br/>
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doOrderSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/index")
    public String doOrderSearch(@Validated(OrderSearchGroup.class) OrderModel orderModel,
                                BindingResult error,
                                Model model) {

        // 実行前処理
        preDoAction(orderModel);

        if (error.hasErrors()) {
            return "order/index";
        }

        orderModel.onOrderSearch();

        // 受注検索受注一覧取得処理実行
        searchOrder(false, orderModel, model);

        return "order/index";
    }

    /**
     *
     * 出荷登録用一覧表示処理<br/>
     *
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doShipmentSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/index")
    public String doShipmentSearch(@Validated(ShipmentSearchGroup.class) OrderModel orderModel,
                                   BindingResult error,
                                   Model model) {

        // 実行前処理
        preDoAction(orderModel);

        if (error.hasErrors()) {
            return "order/index";
        }

        orderModel.onShipmentRegister();

        // 受注検索受注一覧取得処理実行
        searchOrder(false, orderModel, model);

        return "order/index";
    }

    /**
     * 出荷登録処理<br />
     *
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doSelectShipmentRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/index")
    public String doSelectShipmentRegist(@Validated(SelectShipmentRegistGroup.class) OrderModel orderModel,
                                         BindingResult error,
                                         RedirectAttributes redirectAttrs,
                                         Model model) {

        // 実行前処理
        preDoAction(orderModel);

        if (error.hasErrors()) {
            return "order/index";
        }

        // 検索結果チェック
        resultListCheck(orderModel);

        // 出荷登録パラメータ作成
        List<ShipmentRegistDto> shipmentRegistDtoList = orderHelper.toShipmentRegistDtoForRegist(orderModel);

        if (ListUtils.isEmpty(shipmentRegistDtoList)) {
            orderHelper.setFinishPageItem("AOX000501W", null, orderModel);
            return "order/index";
        }

        MessageResponse messageResponse = null;
        try {
            List<TransactionShipmentInfo> transactionShipmentInfoList =
                            orderHelper.toTransactionShipmentRequestList(shipmentRegistDtoList);
            TransactionShipmentRequest transactionShipmentRequest = new TransactionShipmentRequest();
            transactionShipmentRequest.setTransactionShipmentInfoList(transactionShipmentInfoList);
            messageResponse = transactionApi.shipment(transactionShipmentRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // API呼び出し側でエラ―ハンドリングで実装しているので、ここに到達するケースはないため、エラー画面に強制遷移させる
            addMessage(MSGCD_PARAM_ERROR, redirectAttrs, model);
            return "redirect:/error";
        }

        // メッセージを生成
        List<CheckMessageDto> messageDtoList = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(messageResponse) && ObjectUtils.isNotEmpty(messageResponse.getMessages())) {
            messageResponse.getMessages().forEach((fieldName, messageContentList) -> {
                for (MessageContent messageContent : messageContentList) {
                    CheckMessageDto checkMessageDto = ApplicationContextUtility.getBean(CheckMessageDto.class);
                    // メッセージコードからメッセージレベルを切り出す
                    if (StringUtils.isNotBlank(messageContent.getMessageCode())) {
                        checkMessageDto.setMessageLevel(messageContent.getMessageCode()
                                                                      .substring(messageContent.getMessageCode()
                                                                                               .length() - 1));
                    } else {
                        checkMessageDto.setMessageLevel("E");
                    }
                    checkMessageDto.setMessage(messageContent.getMessage());
                    messageDtoList.add(checkMessageDto);
                }
            });
        }

        orderModel.setCheckMessageItems(messageDtoList);

        // 同ページを同条件で再検索
        searchOrder(true, orderModel, model);

        return "order/index";
    }

    /**
     *
     * 出荷データアップロード画面遷移<br/>
     *
     * @return 出荷データアップロード画面
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doShipmentUpload")
    public String doShipmentUpload(OrderModel orderModel, Model model) {
        // 実行前処理
        preDoAction(orderModel);
        return "redirect:/order/shipmentUpload/";
    }

    /**
     *
     * 検査キット受領データアップロード画面遷移<br/>
     *
     * @return 出荷データアップロード画面
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doExamkitReceivedUpload")
    public String doExamkitReceivedUpload(OrderModel orderModel, Model model) {
        // 実行前処理
        preDoAction(orderModel);
        return "redirect:/order/examkitReceivedUpload/";
    }

    /**
     *
     * 検査結果登録データアップロード画面遷移<br/>
     *
     * @return 検査結果登録データアップロード画面
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doExamResultsUpload")
    public String doExamResultsUpload(OrderModel orderModel, Model model) {
        // 実行前処理
        preDoAction(orderModel);
        return "redirect:/order/examResultsUpload/";
    }

    /**
     * 全件CSV出力
     *
     * @param orderModel
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doOutput")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @HEHandler(exception = AppLevelListException.class, returnView = "order/index")
    public String doOutput(@Validated(OutputGroup.class) OrderModel orderModel, BindingResult error) {
        // 実行前処理
        preDoAction(orderModel);

        if (error.hasErrors()) {
            return "order/index";
        }

        // 検索条件作成
        OrderSearchConditionDto orderSearchConditionDto = orderHelper.toOrderSearchListConditionDto(orderModel);
        orderSearchConditionDto.setShopSeq(this.getCommonInfo().getCommonInfoBase().getShopSeq());

        // 検索条件の最新化 エラーがある場合は、終了
        if (orderModel.getMsgCodeList() != null && !orderModel.getMsgCodeList().isEmpty()) {
            List<String> msgCodeList = orderModel.getMsgCodeList();
            for (String messageCode : msgCodeList) {
                // メッセージ引数マップに一致するメッセージコードがある場合は引数として設定する
                if (orderModel.getMsgArgMap().containsKey(messageCode)) {
                    throwMessage(messageCode, orderModel.getMsgArgMap().get(messageCode));
                } else {
                    throwMessage(messageCode);
                }
            }
        }

        List<String> orderIds = orderSearchHelper.toOrderIds(orderModel.getConditionOrderCodeList());

        for (String orderId : orderIds) {
            // 受注番号桁数チェック
            if (orderId.length() > 14) {
                orderModel.getMsgCodeList().add("AOX000114E");
                orderModel.getMsgArgMap().put("AOX000114E", new String[] {"受注番号(複数検索用)", "14"});
                break;
            }
            // 受注番号数チェック(最大番号数はプロパティから取得)
            if (orderIds.size() > OrderModel.CONDITION_ORDER_CODE_LIST_LIMIT) {
                orderModel.getMsgCodeList().add("AOX000115E");
                orderModel.getMsgArgMap()
                          .put("AOX000115E", new String[] {Integer.toString(OrderModel.CONDITION_ORDER_CODE_LIST_LIMIT),
                                          "受注番号(複数検索用)"});
            }
        }

        OrderSearchCsvGetRequest orderSearchCsvGetRequest =
                        orderSearchHelper.toOrderSearchCsvGetRequest(orderSearchConditionDto);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();

        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

        // リクエスト用のページャー項目をセット
        String orderField = orderModel.getOrderField();
        if (StringUtils.isEmpty(orderField)) {
            orderField = DEFAULT_ORDERSEARCH_ORDER_FIELD;
        }
        pageInfoHelper.setupPageRequest(
                        pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE, orderField, orderModel.isOrderAsc());

        // CSVダウンロードオプションリクエスト設定
        OrderSearchCsvGetOptionRequest csvDownloadOptionRequest = new OrderSearchCsvGetOptionRequest();
        if (orderModel.getCsvDownloadOptionDto() != null) {
            csvDownloadOptionRequest.setOptionId(orderModel.getCsvDownloadOptionDto().getOptionId());
        }

        // ダウンロードファイルタイプリクエスト
        DownloadFileTypeRequest downloadFileTypeRequest = new DownloadFileTypeRequest();
        downloadFileTypeRequest.setFileType(DOWNLOAD_ZIP);

        if (HTypeSelectionOrderOutData.SHIPMENT_CSV.getValue().equals(orderModel.getOrderOutData())) {
            orderSearchApi.downloadShipmentCsv(orderSearchCsvGetRequest, csvDownloadOptionRequest, pageInfoRequest,
                                               downloadFileTypeRequest
                                              );
            orderModel.setSelectShipmentCSVFlag(true);
        } else if (HTypeSelectionOrderOutData.ORDER_CSV.getValue().equals(orderModel.getOrderOutData())) {
            orderSearchApi.downloadOrderCsv(orderSearchCsvGetRequest, csvDownloadOptionRequest, pageInfoRequest,
                                            downloadFileTypeRequest
                                           );
            orderModel.setSelectOrderCSVFlag(true);
        }

        return "order/index";
    }

    /**
     * 受注CSVダウンロード（一覧上部ボタン）<br/>
     *
     * @param orderModel
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doSelectionOutput1")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @HEHandler(exception = AppLevelListException.class, returnView = "order/index")
    public String doSelectionOutput1(@Validated(SelectionOutput1Group.class) OrderModel orderModel,
                                     BindingResult error) {
        // 実行前処理
        preDoAction(orderModel);

        if (error.hasErrors()) {
            return "order/index";
        }
        // CSV選択ダウンロード
        downloadCheckedCsv(orderModel);

        return "order/index";
    }

    /**
     * CSV非同期処理結果のダウンロード<br/>
     *
     * @param file 対象ファイル名
     * @return ファイル出力先パス
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    public ResponseEntity<byte[]> doDownload(@RequestParam(required = false) String file) {

        Path path = null;
        if (StringUtils.isNotEmpty(file)) {

            if (file.contains("/")) {
                throwMessage(MESSAGE_VALID_DOWNLOAD_CSV_NOT_EXIST);
            }

            if (file.matches(ORDER_FILE_NAME_REGEX)) {
                path = Paths.get(PropertiesUtil.getSystemPropertiesValue(ORDERCSV_FILE_PATH) + file);
            } else if (file.matches(SHIPMENT_FILE_NAME_REGEX)) {
                path = Paths.get(PropertiesUtil.getSystemPropertiesValue(SHIPMENT_FILE_PATH) + file);
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
     * デフォルトの CSV ダウンロード オプションを取得 AJAX<br/>
     *
     * @return orderSearchCsvOptionResponse 受注検索CSVDLオプションリストレスポンス
     */
    @GetMapping(value = "/getDefaultOptionDownloadAjax")
    @ResponseBody
    public ResponseEntity<OrderSearchCsvOptionResponse> getDefaultOptionDownloadAjax() {
        return ResponseEntity.ok(orderSearchApi.getDefault());
    }

    /**
     * CSVテンプレートの一覧を取得 AJAX<br/>
     *
     * @return orderSearchCsvOptionListResponse 受注検索CSVDLオプションリストレスポンス
     */
    @GetMapping(value = "/getCsvTemplateListAjax")
    @ResponseBody
    public ResponseEntity<OrderSearchCsvOptionListResponse> getCsvTemplateListAjax() {
        return ResponseEntity.ok(orderSearchApi.getOptionCsv());
    }

    /**
     * CSV オプションの更新テンプレート AJAX <br/>
     *
     * @param csvDownloadOptionDto
     * @param error
     * @param orderModel
     * @return
     */
    @PostMapping(value = "/doUpdateTemplateAjax")
    @ResponseBody
    public ResponseEntity<?> doUpdateTemplateAjax(
                    @RequestBody @Validated(OptionDownloadGroup.class) CsvDownloadOptionDto csvDownloadOptionDto,
                    BindingResult error,
                    OrderModel orderModel) {

        // ユーザーが注文画面でテンプレートを選択しなかった場合
        if (csvDownloadOptionDto.getResetFlg()) {
            orderModel.setCsvDownloadOptionDto(null);
        }

        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }

        // ユーザーが注文画面でテンプレートのみを選択した場合
        if (csvDownloadOptionDto.getUpdateFlg()) {
            OrderSearchCsvOptionUpdateRequest orderSearchCsvOptionUpdateRequest =
                            orderHelper.toOrderSearchCsvOptionUpdateRequest(csvDownloadOptionDto);

            orderSearchApi.updateOption(orderSearchCsvOptionUpdateRequest);
            orderModel.getCsvDownloadOptionDtoList().stream().forEach(dto -> {
                if (dto.getOptionId().equals(csvDownloadOptionDto.getOptionId())) {
                    dto.setOptionName(csvDownloadOptionDto.getOptionName());
                }
            });
        }

        orderModel.setCsvDownloadOptionDto(csvDownloadOptionDto);

        return ResponseEntity.ok("ok");
    }

    /**
     * 受注番号別一覧・選択出力
     *
     * @param orderModel
     * @return
     */
    protected void downloadCheckedCsv(OrderModel orderModel) {

        // 検索結果チェック
        resultListCheck(orderModel);

        List<String> orderCodeList = orderHelper.convertToListForSearch(orderModel);

        // チェック選択なし
        if (orderCodeList.isEmpty()) {
            throwMessage("AOX000109");
        }

        // 検索条件作成
        OrderSearchConditionDto orderSearchConditionDto = orderHelper.toOrderSearchListConditionDto(orderModel);
        orderSearchConditionDto.setShopSeq(this.getCommonInfo().getCommonInfoBase().getShopSeq());

        OrderSearchCsvGetRequest orderSearchCsvGetRequest =
                        orderSearchHelper.toOrderSearchCsvGetRequest(orderSearchConditionDto);

        orderSearchCsvGetRequest.setOrderIDs(orderCodeList);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();

        // リクエスト用のページャー項目をセット
        pageInfoRequest.setSort(orderModel.isOrderAsc());
        pageInfoRequest.setOrderBy(orderModel.getOrderField());

        // CSVダウンロードオプションリクエスト設定
        OrderSearchCsvGetOptionRequest csvDownloadOptionRequest = new OrderSearchCsvGetOptionRequest();

        if (orderModel.getOptionTemplateIndexResult() != null) {
            boolean errorFlag = true;
            for (CsvDownloadOptionDto csvDownloadOptionDto : orderModel.getCsvDownloadOptionDtoList()) {
                if (csvDownloadOptionDto.getOptionId().equals(orderModel.getOptionTemplateIndexResult())) {
                    errorFlag = false;
                    break;
                }
            }
            if (errorFlag) {
                orderModel.setOptionTemplateIndexResult(null);
            }
        }
        csvDownloadOptionRequest.setOptionId(orderModel.getOptionTemplateIndexResult());

        // ダウンロードファイルタイプリクエスト
        DownloadFileTypeRequest downloadFileTypeRequest = new DownloadFileTypeRequest();
        downloadFileTypeRequest.setFileType(DOWNLOAD_ZIP);

        if (HTypeSelectionOrderOutData.SHIPMENT_CSV.getValue().equals(orderModel.getOrderOutData1())) {
            orderSearchApi.downloadShipmentCsv(orderSearchCsvGetRequest, csvDownloadOptionRequest, pageInfoRequest,
                                               downloadFileTypeRequest
                                              );
            orderModel.setSelectShipmentCSVFlag(true);
        } else if (HTypeSelectionOrderOutData.ORDER_CSV.getValue().equals(orderModel.getOrderOutData1())) {
            orderSearchApi.downloadOrderCsv(orderSearchCsvGetRequest, csvDownloadOptionRequest, pageInfoRequest,
                                            downloadFileTypeRequest
                                           );
            orderModel.setSelectOrderCSVFlag(true);
        }
    }

    /**
     *
     * 受注検索受注一覧取得処理実行
     *
     * @param isDisplayChange 表示変更フラグ
     */
    private void searchOrder(boolean isDisplayChange, OrderModel orderModel, Model model) {

        toOrderSearchListConditionDto(isDisplayChange, orderModel);

        // 検索条件作成
        OrderSearchConditionDto orderSearchConditionDto = orderModel.getOrderSearchConditionDto();

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();

        // リクエスト用のページャー項目をセット
        pageInfoRequest.setPage(conversionUtility.toInteger(orderModel.getPageNumber()));
        pageInfoRequest.setLimit(orderModel.getLimit());
        pageInfoRequest.setSort(orderModel.isOrderAsc());
        pageInfoRequest.setOrderBy(orderModel.getOrderField());

        // 受注検索受注一覧取得サービスを実行
        OrderSearchQueryRequest orderSearchQueryRequest =
                        orderSearchHelper.toOrderSearchQueryRequest(orderModel.getOrderSearchConditionDto());

        OrderSearchQueryModelListResponse responseList = orderSearchApi.get(orderSearchQueryRequest, pageInfoRequest);

        List<OrderSearchOrderResultDto> resultList = orderSearchHelper.toOrderSearchOrderResultDtoList(responseList);

        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

        PageInfoResponse pageInfoResponse = responseList.getPageInfo();

        pageInfoHelper.setupPageInfo(orderModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                     pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                     pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages()
                                    );

        // 検索結果をindexPageに反映
        orderHelper.toPageForSearch(resultList, orderModel, orderSearchConditionDto);

        // 検索アクションで種別を保存
        String searchActionType = null;
        if (orderModel.isShipmentRegister()) {
            searchActionType = OrderModel.SEARCH_ACTION_TYPE_SHIPMENT;

        } else {
            searchActionType = OrderModel.SEARCH_ACTION_TYPE_ORDER;
        }

        orderModel.getOrderSearchConditionDto().setSearchActionType(searchActionType);

        // ページャーセットアップ
        pageInfoHelper.setupViewPager(orderModel.getPageInfo(), orderModel);

    }

    /**
     * 受注検索条件設定
     *
     * @param isDisplayChange 表示変更フラグ
     * @param orderModel
     *                        受注検索モデル
     */
    protected void toOrderSearchListConditionDto(boolean isDisplayChange, OrderModel orderModel) {

        if (!isDisplayChange) {
            orderModel.setPageNumber(DEFAULT_ORDERSEARCH_PNUM);
            // ソート条件設定
            setDefaultSort(orderModel);
            // 検索条件作成
            orderHelper.toOrderSearchListConditionDtoForPage(orderModel);
        } else {
            // 検索対象のページ情報を設定
            orderHelper.toOrderSearchListConditionDtoDisplayChange(orderModel);
        }

        if (orderModel.getMsgCodeList() != null && !orderModel.getMsgCodeList().isEmpty()) {
            List<String> msgCodeList = orderModel.getMsgCodeList();
            for (String messageCode : msgCodeList) {
                // メッセージ引数マップに一致するメッセージコードがある場合は引数として設定する
                if (orderModel.getMsgArgMap().containsKey(messageCode)) {
                    throwMessage(messageCode, orderModel.getMsgArgMap().get(messageCode));
                } else {
                    throwMessage(messageCode);
                }
            }
        }
    }

    /**
     * デフォルトソート条件を設定<br/>
     */
    protected void setDefaultSort(OrderModel orderModel) {
        orderModel.setOrderField(DEFAULT_ORDERSEARCH_ORDER_FIELD);
        orderModel.setOrderAsc(DEFAULT_ORDERSEARCH_ORDER_ASC);
    }

    /**
     * 受注検索結果リストが空でないことをチェックする<br/>
     * (ブラウザバック後の選択出力などでの不具合防止のため)<br/>
     */
    protected void resultListCheck(OrderModel orderModel) {
        if (!orderModel.isResult()) {
            return;
        }
        OrderModelItem item = orderModel.getResultItems().get(0);
        if (StringUtil.isEmpty(item.getOrderCode())) {
            orderModel.setResultItems(null);
            this.throwMessage("AOX000112");
        }
    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param orderModel 受注検索モデル
     * @param configInfoResponse 受注検索モデル
     */
    private void initComponentValue(OrderModel orderModel, ConfigInfoResponse configInfoResponse) {

        orderModel.setOrderSiteTypeArrayItems(EnumTypeUtil.getEnumMap(HTypeOrderSite.class));
        orderModel.setOrderTypeArrayItems(EnumTypeUtil.getEnumMap(HTypeOrderType.class));
        orderModel.setSettlememntItems(orderHelper.toSettlementMapList(settlementMethodApi.get()));
        orderModel.setBillTypeItems(EnumTypeUtil.getEnumMap(HTypeBillType.class));
        orderModel.setBillStatusItems(EnumTypeUtil.getEnumMap(HTypeBillStatus.class));
        orderModel.setOrderStatusItems(EnumTypeUtil.getEnumMap(HTypeSelectMapOrderStatus.class));
        orderModel.setTimeTypeItems(EnumTypeUtil.getEnumMap(HTypeDate.class));
        orderModel.setOrderPersonItems(EnumTypeUtil.getEnumMap(HTypePerson.class));
        orderModel.setDeliveryItems(orderHelper.toDeliveryMapList(shippingMethodApi.get()));
        orderModel.setShipmentStatusItems(EnumTypeUtil.getEnumMap(HTypeShipmentStatus.class));
        orderModel.setPaymentStatusItems(EnumTypeUtil.getEnumMap(HTypePaymentStatus.class));
        orderModel.setExamStatusItems(EnumTypeUtil.getEnumMap(HTypeExamStatus.class));

        Map<String, String> tmpOrderOutDataSelectItems = EnumTypeUtil.getEnumMap(HTypeSelectionOrderOutData.class);
        orderModel.setOrderOutData1Items(tmpOrderOutDataSelectItems);

        Map<String, String> tmpOrderOutDataItems = EnumTypeUtil.getEnumMap(HTypeOrderOutData.class);
        orderModel.setOrderOutDataItems(tmpOrderOutDataItems);
        orderModel.setCsvDownloadOptionDtoList(orderHelper.toCsvDownloadOptionDtoList(orderSearchApi.getOptionCsv()));

        orderModel.setExamKitCodeDivChar(configInfoResponse.getExamKitCodeListDivchar());
        orderModel.setExamKitCodeListLength(configInfoResponse.getExamKitCodeListLength());
    }

}
