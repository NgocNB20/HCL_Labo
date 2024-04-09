package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.holiday;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.HolidayEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.download.DownloadApiClient;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.RegistUpdateGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.holiday.presentation.api.HolidayApi;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayDeleteRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayDownloadCsvRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayListGetRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayListResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayRegistRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
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
 * 休日検索
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/delivery/holiday")
@Controller
@SessionAttributes(value = "deliveryHolidayModel")
@PreAuthorize("hasAnyAuthority('SETTING:8')")
public class DeliveryHolidayController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryHolidayController.class);

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
    protected static final String MSGCD_ILLEGAL_OPERATION = "AYD000601";

    /**
     * 変更・登録画面から遷移
     */
    public static final String DO_REGIST_UPDATE_PARAM = "doRegistUpdate";

    /**
     * 休日検索ページDxo
     */
    private final DeliveryHolidayHelper deliveryHolidayHelper;

    /**
     * 配送方法API
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 休日API
     */
    private final HolidayApi holidayApi;

    /**
     * ダウンロードAPIクライアント
     */
    private final DownloadApiClient downloadApiClient;

    /**
     * ファイル名
     */
    private String fileName = "holiday";

    /**
     * コンストラクタ
     *
     * @param deliveryHolidayHelper 休日検索ページDxo
     * @param shippingMethodApi     配送方法API
     * @param holidayApi            休日API
     * @param downloadApiClient     ダウンロードAPIクライアント
     */
    @Autowired
    public DeliveryHolidayController(DeliveryHolidayHelper deliveryHolidayHelper,
                                     ShippingMethodApi shippingMethodApi,
                                     HolidayApi holidayApi,
                                     DownloadApiClient downloadApiClient) {
        this.deliveryHolidayHelper = deliveryHolidayHelper;
        this.shippingMethodApi = shippingMethodApi;
        this.holidayApi = holidayApi;
        this.downloadApiClient = downloadApiClient;
    }

    /**
     * 検索<br/>
     *
     * @param deliveryHolidayModel
     * @param model
     */
    protected void search(DeliveryHolidayModel deliveryHolidayModel, BindingResult error, Model model) {
        try {
            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            Integer pageNumber = null;
            if (deliveryHolidayModel.getPageNumber() != null) {
                pageNumber = Integer.parseInt(deliveryHolidayModel.getPageNumber());
            }
            pageInfoHelper.setupPageRequest(pageInfoRequest, pageNumber, deliveryHolidayModel.getLimit(), null, true);

            // 取得
            HolidayListGetRequest holidayListGetRequest = new HolidayListGetRequest();
            holidayListGetRequest.setYear(deliveryHolidayModel.getYear());
            HolidayListResponse holidayListResponse =
                            holidayApi.getByDeliveryMethodSeq(deliveryHolidayModel.getDmcd(), holidayListGetRequest,
                                                              pageInfoRequest
                                                             );
            List<HolidayEntity> list = deliveryHolidayHelper.toHolidayEntityList(holidayListResponse);

            // 画面に反映
            deliveryHolidayHelper.toPageIndex(list, deliveryHolidayModel);

            // ページャーにレスポンス情報をセット
            PageInfoResponse tmp = holidayListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(deliveryHolidayModel, tmp.getPage(), tmp.getLimit(), tmp.getNextPage(),
                                         tmp.getPrevPage(), tmp.getTotal(), tmp.getTotalPages()
                                        );
            // ページャーをセット
            pageInfoHelper.setupViewPager(deliveryHolidayModel.getPageInfo(), deliveryHolidayModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            itemNameAdjust.put("page", "pageNumber");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 初期表示
     *
     * @param dmcdParam
     * @param md
     * @param deliveryHolidayModel
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/holiday/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> yearParam,
                              @RequestParam(required = false) Optional<String> dmcdParam,
                              @RequestParam(required = false) Optional<String> md,
                              @RequestParam(required = false) Optional<String> isUpload,
                              DeliveryHolidayModel deliveryHolidayModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        boolean isAfterRegistUpdate = md.isPresent() && DO_REGIST_UPDATE_PARAM.equals(md.get()) && dmcdParam.isPresent()
                                      && Objects.equals(
                        deliveryHolidayModel.getDmcd(), Integer.parseInt(dmcdParam.get()));
        boolean isNotChangeOfDmcdParam = dmcdParam.isPresent() && Objects.equals(deliveryHolidayModel.getDmcd(),
                                                                                 Integer.parseInt(dmcdParam.get())
                                                                                );
        boolean isNotChangeOfYearParam = yearParam.isPresent() && Objects.equals(deliveryHolidayModel.getYear(),
                                                                                 Integer.parseInt(yearParam.get())
                                                                                );
        boolean isAfterUpload = !isUpload.isEmpty() && Boolean.parseBoolean(isUpload.get());
        if (isAfterRegistUpdate || (isNotChangeOfDmcdParam && isNotChangeOfYearParam) && !isAfterUpload) {
            return "delivery/holiday/index";
        }

        // モデルのクリア処理
        clearModel(DeliveryHolidayModel.class, deliveryHolidayModel, model);

        // 配送方法SEQ取得
        dmcdParam.ifPresent(str -> deliveryHolidayModel.setDmcd(Integer.parseInt(str)));

        // 年が設定されている場合、引き継ぎ用の年にセット
        yearParam.ifPresent(str -> deliveryHolidayModel.setYear(Integer.parseInt(str)));

        // 不正操作チェック
        if (deliveryHolidayModel.getDmcd() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/delivery/";
        }

        try {
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryHolidayModel.getDmcd());
            DeliveryMethodEntity resultEntity = deliveryHolidayHelper.toDeliveryMethodEntity(shippingMethodResponse);

            // 不正操作チェック。配送マスタは物理削除されないので、結果がないのはURLパラメータをいじられた以外ありえない。
            if (resultEntity == null) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/";
            }

            deliveryHolidayHelper.convertToRegistPageForResult(deliveryHolidayModel, resultEntity);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/holiday/index";
        }

        // 設定がない場合、今年を初期選択値として設定
        Calendar cal = Calendar.getInstance();
        if (deliveryHolidayModel.getYear() == null) {
            deliveryHolidayModel.setYear(cal.get(Calendar.YEAR));
        }

        // ページング項目初期化
        deliveryHolidayModel.setPageNumber(DEFAULT_PNUM);
        deliveryHolidayModel.setLimit(DEFAULT_LIMIT);

        // 検索を実行
        search(deliveryHolidayModel, error, model);

        return "delivery/holiday/index";
    }

    /**
     * 検索
     *
     * @param deliveryHolidayModel
     * @param model
     * @return
     */
    @PostMapping(value = "/", params = "doSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/holiday/index")
    public String doSearch(@Validated(SearchGroup.class) DeliveryHolidayModel deliveryHolidayModel,
                           BindingResult error,
                           Model model) {

        if (error.hasErrors()) {
            return "delivery/holiday/index";
        }

        // ページング項目初期化
        deliveryHolidayModel.setPageNumber(DEFAULT_PNUM);
        deliveryHolidayModel.setLimit(DEFAULT_LIMIT);

        search(deliveryHolidayModel, error, model);
        return "delivery/holiday/index";
    }

    /**
     * 全件CSV出力
     *
     * @param deliveryHolidayModel
     * @param error
     * @return
     */
    @PostMapping(value = "/", params = "doCsvDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/holiday/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "delivery/holiday/index")
    public void doCsvDownload(HttpServletResponse response,
                              @Validated(AllDownloadGroup.class) DeliveryHolidayModel deliveryHolidayModel,
                              BindingResult error,
                              Model model) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }

        try {
            String deliveryDownloadPath = "/shippings/methods/" + deliveryHolidayModel.getDmcd() + "/holidays/csv";
            // 検索条件作成
            HolidayDownloadCsvRequest holidayDownloadCsvRequest =
                            deliveryHolidayHelper.toHolidayDownloadCsvRequest(deliveryHolidayModel);
            downloadApiClient.invokeAPI(response, holidayApi.getApiClient().getBasePath(), deliveryDownloadPath,
                                        getFileName(), holidayDownloadCsvRequest
                                       );
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * 休日登録
     *
     * @param deliveryHolidayModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/", params = "doRegistUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/holiday/index")
    public String doRegistUpdate(@Validated(RegistUpdateGroup.class) DeliveryHolidayModel deliveryHolidayModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) throws ParseException {

        if (error.hasErrors()) {
            return "delivery/holiday/index";
        }

        // Pageの値をエンティティへコピー
        HolidayRegistRequest holidayRegistRequest =
                        deliveryHolidayHelper.toHolidayEntityForRegistUpdate(deliveryHolidayModel);

        HolidayResponse holidayResponse = new HolidayResponse();
        // 登録更新結果
        try {
            holidayResponse = holidayApi.regist(deliveryHolidayModel.getDmcd(), holidayRegistRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // エラーメッセージを設定
            addMessage("AYH000103", redirectAttributes, model);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            itemNameAdjust.put("date", "inputDate");
            itemNameAdjust.put("year", "inputDate");
            itemNameAdjust.put("name", "inputName");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/holiday/index";
        }
        HolidayEntity holidayEntity = deliveryHolidayHelper.toHolidayEntity(holidayResponse);
        // 登録した年を検索条件として設定しておく
        deliveryHolidayModel.setYear(holidayEntity.getYear());
        // 再検索を実行

        this.search(deliveryHolidayModel, error, model);
        if (error.hasErrors()) {
            return "delivery/holiday/index";
        }
        // 成功メッセージを設定
        addInfoMessage("AYH000102", null, redirectAttributes, model);

        return "redirect:/delivery/holiday/?dmcdParam=" + deliveryHolidayModel.getDmcd() + "&md=doRegistUpdate";
    }

    /**
     * 休日削除
     *
     * @param deliveryHolidayModel
     * @param redirectAttributes
     * @param model
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/", params = "doDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/holiday/index")
    public String doDelete(DeliveryHolidayModel deliveryHolidayModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) throws ParseException {

        // 削除対象休日取得
        HolidayDeleteRequest holidayDeleteRequest = new HolidayDeleteRequest();
        holidayDeleteRequest.setDeleteDate(deliveryHolidayModel.getDeleteDate());

        // 削除結果
        int result = 1;
        try {
            holidayApi.delete(deliveryHolidayModel.getDmcd(), holidayDeleteRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            result = 0;
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        // 再検索を実行
        this.search(deliveryHolidayModel, error, model);
        if (error.hasErrors()) {
            return "delivery/holiday/index";
        }
        if (result > 0) {
            // 成功メッセージを設定
            addInfoMessage("AYH000104", null, redirectAttributes, model);
        } else {
            // エラーメッセージを設定
            addInfoMessage("AYH000105", null, redirectAttributes, model);
        }

        return "redirect:/delivery/holiday/?yearParam=" + deliveryHolidayModel.getYear() + "&dmcdParam="
               + deliveryHolidayModel.getDmcd();
    }

    /**
     * ソート
     *
     * @param deliveryHolidayModel
     * @param model
     * @return
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/holiday/index")
    public String doDisplayChange(DeliveryHolidayModel deliveryHolidayModel, BindingResult error, Model model) {
        search(deliveryHolidayModel, error, model);
        return "delivery/holiday/index";
    }

    /**
     * アップロード画面へ遷移<br/>
     *
     * @param deliveryHolidayModel
     * @param model
     * @return class
     */
    @PostMapping(value = "/", params = "doUpload")
    public String doUpload(@Validated(UploadGroup.class) DeliveryHolidayModel deliveryHolidayModel,
                           BindingResult error,
                           RedirectAttributes redirectAttrs,
                           Model model) {

        if (error.hasErrors()) {
            return "delivery/holiday/index";
        }

        // アプリケーションが変わるため、選択年、配送方法SEQをリダイレクトスコープで引き渡す
        String yearHoliday =
                        deliveryHolidayModel.getYear() != null ? String.valueOf(deliveryHolidayModel.getYear()) : "";
        String dmcdHoliday =
                        deliveryHolidayModel.getDmcd() != null ? String.valueOf(deliveryHolidayModel.getDmcd()) : "";

        return "redirect:/delivery/holiday/bundledupload/?yearParam=" + yearHoliday + "&dmcdParam=" + dmcdHoliday;
    }

    /**
     * 出力CSVファイル名を設定する
     */
    public String getFileName() {
        if (StringUtils.isEmpty(this.fileName)) {
            this.fileName = "download_file";
        }

        CsvUtility csvUtility = (CsvUtility) ApplicationContextUtility.getBean(CsvUtility.class);
        return csvUtility.getDownLoadCsvFileName(this.fileName);
    }

}