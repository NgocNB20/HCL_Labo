package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.did;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryImpossibleDayEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.download.DownloadApiClient;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.RegistUpdateGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.ReceiverImpossibleDateApi;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateDeleteRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateRegistRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesDownloadCsvRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * お届け不可日検索 Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/delivery/did")
@Controller
@SessionAttributes(value = "deliveryDidModel")
@PreAuthorize("hasAnyAuthority('SETTING:8')")
public class DeliveryDidController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryDidController.class);

    /**
     * デフォルトページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト最大表示件数
     */
    private static final int DEFAULT_LIMIT = 100;

    /**
     * メッセージコード：不正操作
     */
    public static final String MSGCD_ILLEGAL_OPERATION = "AYD000601";

    /**
     * メッセージコード：登録成功
     */
    public static final String MSGCD_REGIST_SUCCESS = "HM34-4001-002-A-";

    /**
     * メッセージコード：登録失敗
     */
    public static final String MSGCD_REGIST_FAILURE = "HM34-4001-003-A-";

    /**
     * メッセージコード：削除成功
     */
    public static final String MSGCD_DELETE_SUCCESS = "HM34-4001-004-A-";

    /**
     * メッセージコード：登録失敗
     */
    public static final String MSGCD_DELETE_FAILURE = "HM34-4001-005-A-";

    /**
     * 変更・登録画面から遷移
     */
    public static final String DO_REGIST_UPDATE_PARAM = "doRegistUpdate";

    /**
     * お届け不可日検索 Helper
     */
    private final DeliveryDidHelper deliveryDidHelper;

    /**
     * 配送方法API
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * お届け不可日API
     */
    private final ReceiverImpossibleDateApi receiverImpossibleDateApi;

    /**
     * ダウンロードAPIクライアント
     */
    private final DownloadApiClient downloadApiClient;

    /**
     * ファイル名
     */
    private String fileName = "deliveryImpossibleDay";

    /**
     * コンストラクタ
     *
     * @param deliveryDidHelper         お届け不可日検索 Helper
     * @param shippingMethodApi         配送方法API
     * @param receiverImpossibleDateApi お届け不可日API
     * @param downloadApiClient         ダウンロードAPIクライアント
     */
    @Autowired
    public DeliveryDidController(DeliveryDidHelper deliveryDidHelper,
                                 ShippingMethodApi shippingMethodApi,
                                 ReceiverImpossibleDateApi receiverImpossibleDateApi,
                                 DownloadApiClient downloadApiClient) {
        this.deliveryDidHelper = deliveryDidHelper;
        this.shippingMethodApi = shippingMethodApi;
        this.receiverImpossibleDateApi = receiverImpossibleDateApi;
        this.downloadApiClient = downloadApiClient;
    }

    /**
     * 検索
     *
     * @param deliveryDidModel お届け不可日検索
     * @param model            Model
     */
    protected void search(DeliveryDidModel deliveryDidModel,
                          BindingResult error,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        try {
            // 検索条件作成
            ReceiverImpossibleDateListRequest receiverImpossibleDateListRequest =
                            deliveryDidHelper.toReceiverImpossibleDateListRequest(deliveryDidModel);

            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
            PageInfoRequest pageInfoRequest = new PageInfoRequest();

            Integer pageNumber = null;
            if (deliveryDidModel.getPageNumber() != null) {
                pageNumber = Integer.parseInt(deliveryDidModel.getPageNumber());
            }
            pageInfoHelper.setupPageRequest(pageInfoRequest, pageNumber, deliveryDidModel.getLimit(), null, true);

            // 取得
            ReceiverImpossibleDateListResponse receiverImpossibleDateListResponse =
                            receiverImpossibleDateApi.get(deliveryDidModel.getDmcd(), receiverImpossibleDateListRequest,
                                                          pageInfoRequest
                                                         );
            List<DeliveryImpossibleDayEntity> list =
                            deliveryDidHelper.toListDeliveryImpossibleDayEntity(receiverImpossibleDateListResponse);

            // 画面に反映
            deliveryDidHelper.toPageIndex(list, deliveryDidModel);

            // ページャーにレスポンス情報をセット
            PageInfoResponse tmp = receiverImpossibleDateListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(deliveryDidModel, tmp.getPage(), tmp.getLimit(), tmp.getNextPage(),
                                         tmp.getPrevPage(), tmp.getTotal(), tmp.getTotalPages()
                                        );
            // ページャーをセット
            pageInfoHelper.setupViewPager(deliveryDidModel.getPageInfo(), deliveryDidModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 初期表示
     *
     * @param yearParam          年
     * @param dmcdParam          配送方法SEQ
     * @param md                 Md
     * @param deliveryDidModel   お届け不可日検索
     * @param model              モデル
     * @param redirectAttributes リダイレクトアトリビュート
     * @return 自画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/delivery/did/")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> yearParam,
                              @RequestParam(required = false) Optional<String> dmcdParam,
                              @RequestParam(required = false) Optional<String> md,
                              @RequestParam(required = false) Optional<String> isUpload,
                              DeliveryDidModel deliveryDidModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        boolean isAfterRegistUpdate = md.isPresent() && DO_REGIST_UPDATE_PARAM.equals(md.get()) && dmcdParam.isPresent()
                                      && Objects.equals(deliveryDidModel.getDmcd(), Integer.parseInt(dmcdParam.get()));
        boolean isNotChangeOfDmcdParam = dmcdParam.isPresent() && Objects.equals(deliveryDidModel.getDmcd(),
                                                                                 Integer.parseInt(dmcdParam.get())
                                                                                );
        boolean isNotChangeOfYearParam = yearParam.isPresent() && Objects.equals(deliveryDidModel.getYear(),
                                                                                 Integer.parseInt(yearParam.get())
                                                                                );
        boolean isAfterUpload = !isUpload.isEmpty() && Boolean.parseBoolean(isUpload.get());

        if (isAfterRegistUpdate || (isNotChangeOfDmcdParam && isNotChangeOfYearParam) && !isAfterUpload) {
            return "delivery/did/index";
        }

        // モデルのクリア処理
        clearModel(DeliveryDidModel.class, deliveryDidModel, model);

        // 配送方法SEQ取得
        dmcdParam.ifPresent(str -> deliveryDidModel.setDmcd(Integer.parseInt(str)));

        // 年が設定されている場合、引き継ぎ用の年にセット
        yearParam.ifPresent(str -> deliveryDidModel.setYear(Integer.parseInt(str)));

        // 不正操作チェック
        if (deliveryDidModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        try {
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryDidModel.getDmcd());
            DeliveryMethodEntity resultEntity = deliveryDidHelper.toDeliveryMethodEntity(shippingMethodResponse);

            // 不正操作チェック。配送マスタは物理削除されないので、結果がないのはURLパラメータをいじられた以外ありえない。
            if (resultEntity == null) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/";
            }

            deliveryDidHelper.convertToRegistPageForResult(deliveryDidModel, resultEntity);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "redirect:/delivery/did/";
        }
        // 設定がない場合、今年を初期選択値として設定
        Calendar cal = Calendar.getInstance();
        if (deliveryDidModel.getYear() == null) {
            deliveryDidModel.setYear(cal.get(Calendar.YEAR));
        }

        // ページング項目初期化
        deliveryDidModel.setPageNumber(DEFAULT_PNUM);
        deliveryDidModel.setLimit(DEFAULT_LIMIT);

        // 検索を実行
        search(deliveryDidModel, error, redirectAttributes, model);
        if (error.hasErrors()) {
            return "redirect:/delivery/did/";
        }

        return "delivery/did/index";
    }

    /**
     * 検索
     *
     * @param deliveryDidModel お届け不可日検索
     * @param model            Model
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/did/index")
    public String doSearch(@Validated(SearchGroup.class) DeliveryDidModel deliveryDidModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (error.hasErrors()) {
            return "delivery/did/index";
        }

        // ページング項目初期化
        deliveryDidModel.setPageNumber(DEFAULT_PNUM);
        deliveryDidModel.setLimit(DEFAULT_LIMIT);

        search(deliveryDidModel, error, redirectAttributes, model);
        if (error.hasErrors()) {
            return "delivery/did/index";
        }
        return "delivery/did/index";
    }

    /**
     * 全件CSV出力
     *
     * @param response         HttpServletResponse レスポンス
     * @param deliveryDidModel お届け不可日検索
     * @param error            Error
     * @param model            モデル
     */
    @PostMapping(value = "/", params = "doCsvDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/did/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "delivery/did/index")
    public void doCsvDownload(HttpServletResponse response,
                              @Validated(AllDownloadGroup.class) DeliveryDidModel deliveryDidModel,
                              BindingResult error,
                              Model model) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }

        try {

            ReceiverImpossibleDatesDownloadCsvRequest receiverImpossibleDatesDownloadCsvRequest =
                            new ReceiverImpossibleDatesDownloadCsvRequest();
            receiverImpossibleDatesDownloadCsvRequest.setYear(deliveryDidModel.getYear());
            String deliveryDidDownloadPath =
                            "/shippings/methods/" + deliveryDidModel.getDmcd() + "/receiver-impossible-dates/csv";

            downloadApiClient.invokeAPI(response, receiverImpossibleDateApi.getApiClient().getBasePath(),
                                        deliveryDidDownloadPath, getFileName(),
                                        receiverImpossibleDatesDownloadCsvRequest
                                       );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * お届け不可日登録
     *
     * @param deliveryDidModel   お届け不可日検索
     * @param error              BindingResult
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     * @return 自画面
     * @throws ParseException
     */
    @PostMapping(value = "/", params = "doRegistUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/did/index")
    public String doRegistUpdate(@Validated(RegistUpdateGroup.class) DeliveryDidModel deliveryDidModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) throws ParseException {

        if (error.hasErrors()) {
            return "delivery/did/index";
        }

        // Pageの値をエンティティへコピー
        ReceiverImpossibleDateRegistRequest receiverImpossibleDateRegistRequest =
                        deliveryDidHelper.toDeliveryImpossibleDayEntityForRegistUpdate(deliveryDidModel);

        ReceiverImpossibleDateResponse receiverImpossibleDateResponse = new ReceiverImpossibleDateResponse();
        DeliveryImpossibleDayEntity deliveryImpossibleDayEntity = new DeliveryImpossibleDayEntity();

        // 登録更新結果
        try {
            receiverImpossibleDateResponse = receiverImpossibleDateApi.regist(deliveryDidModel.getDmcd(),
                                                                              receiverImpossibleDateRegistRequest
                                                                             );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // エラーメッセージを設定
            addMessage(MSGCD_REGIST_FAILURE, redirectAttributes, model);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("date", "inputDate");
            itemNameAdjust.put("year", "inputDate");
            itemNameAdjust.put("reason", "inputReason");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/did/index";
        }
        deliveryImpossibleDayEntity = deliveryDidHelper.toDeliveryImpossibleDayEntity(receiverImpossibleDateResponse);
        // 登録した年を検索条件として設定しておく
        deliveryDidModel.setYear(deliveryImpossibleDayEntity.getYear());
        // 再検索を実行
        this.search(deliveryDidModel, error, redirectAttributes, model);
        if (error.hasErrors()) {
            return "delivery/did/index";
        }
        // 成功メッセージを設定
        addMessage(MSGCD_REGIST_SUCCESS, redirectAttributes, model);

        return "redirect:/delivery/did/?dmcdParam=" + deliveryDidModel.getDmcd() + "&md=doRegistUpdate";
    }

    /**
     * お届け不可日削除
     *
     * @param deliveryDidModel   お届け不可日検索
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     * @return 自画面
     * @throws ParseException
     */
    @PostMapping(value = "/", params = "doDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/did/index")
    public String doDelete(DeliveryDidModel deliveryDidModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) throws ParseException {
        // 削除対象お届け不可日取得
        ReceiverImpossibleDateDeleteRequest receiverImpossibleDateDeleteRequest =
                        new ReceiverImpossibleDateDeleteRequest();
        receiverImpossibleDateDeleteRequest.setDeleteDate(deliveryDidModel.getDeleteDate());

        // 削除結果
        int result = 1;
        try {
            receiverImpossibleDateApi.delete(deliveryDidModel.getDmcd(), receiverImpossibleDateDeleteRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            result = 0;
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/did/index";
        }

        // 再検索を実行
        this.search(deliveryDidModel, error, redirectAttributes, model);
        if (error.hasErrors()) {
            return "delivery/did/index";
        }
        if (result > 0) {
            // 成功メッセージを設定
            addInfoMessage(MSGCD_DELETE_SUCCESS, null, redirectAttributes, model);
        } else {
            // エラーメッセージを設定
            addInfoMessage(MSGCD_DELETE_FAILURE, null, redirectAttributes, model);
        }

        return "redirect:/delivery/did/?yearParam=" + deliveryDidModel.getYear() + "&dmcdParam="
               + deliveryDidModel.getDmcd();
    }

    /**
     * ソート<br/>
     *
     * @param deliveryDidModel お届け不可日検索
     * @param model            モデル
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/did/index")
    public String doDisplayChange(DeliveryDidModel deliveryDidModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        search(deliveryDidModel, error, redirectAttributes, model);
        return "delivery/did/index";
    }

    /**
     * アップロード画面へ遷移
     *
     * @param deliveryDidModel お届け不可日検索
     * @param error            BindingResult
     * @param redirectAttrs    リダイレクトアトリビュート
     * @param model            モデル
     * @return class
     */
    @PostMapping(value = "/", params = "doUpload")
    public String doUpload(@Validated(UploadGroup.class) DeliveryDidModel deliveryDidModel,
                           BindingResult error,
                           RedirectAttributes redirectAttrs,
                           Model model) {

        if (error.hasErrors()) {
            return "delivery/did/index";
        }

        // アプリケーションが変わるため、選択年、配送方法SEQをリダイレクトスコープで引き渡す
        String yearDid = deliveryDidModel.getYear() != null ? String.valueOf(deliveryDidModel.getYear()) : "";
        String dmcdDid = deliveryDidModel.getDmcd() != null ? String.valueOf(deliveryDidModel.getDmcd()) : "";

        return "redirect:/delivery/did/bundledupload/?yearParam=" + yearDid + "&dmcdParam=" + dmcdDid;
    }

    /**
     * 出力CSVファイル名を設定する
     */
    private String getFileName() {
        if (StringUtils.isEmpty(this.fileName)) {
            this.fileName = "download_file";
        }

        CsvUtility csvUtility = (CsvUtility) ApplicationContextUtility.getBean(CsvUtility.class);
        return csvUtility.getDownLoadCsvFileName(this.fileName);
    }
}