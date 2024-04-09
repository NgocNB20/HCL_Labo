package jp.co.itechh.quad.admin.pc.web.admin.totaling.goodssales;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderDevice;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CategoryAndTagForGoodsRegistUpdateDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.reports.presentation.api.ReportsApi;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSaleResponse;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSalesGetRequest;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSalesResponse;
import jp.co.itechh.quad.reports.presentation.api.param.PageInfoRequest;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品別販売価格別集計Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Controller
@RequestMapping("/goodssales/")
@SessionAttributes(value = "goodsSalesModel")
@PreAuthorize("hasAnyAuthority('REPORT:4')")
public class GoodsSalesController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsSalesController.class);

    /** レポート API */
    private final ReportsApi reportsApi;

    /** カテゴリAPI */
    private final CategoryApi categoryApi;

    /** 商品別販売価格別集計ヘルパー */
    private final GoodsSalesHelper goodsSalesHelper;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** デフォルト最大表示件数 */
    private static final int DEFAULT_LIMIT = 100;

    /** デフォルトページ番号 */
    public static final String DEFAULT_PNUM = "1";

    /** CSVのファイルパス */
    public static final String SOLDGOODS_FILE_PATH = "soldGoodsCsvAsynchronous.file.path";

    /** メッセージ */
    public static final String MESSAGE_VALID_DOWNLOAD_CSV_NOT_EXIST = "DOWNLOADCSV-002-E";

    /** CSVのファイ名 */
    private static final String SOLDGOODS_FILE_NAME_REGEX = "^t_SoldGoods\\d{8}_\\d{6}(\\.zip|\\.csv)$";

    /**
     * コンストラクター
     *
     * @param goodsSalesHelper  商品別販売価格別集計ヘルパー
     * @param conversionUtility 変換ユーティリティクラス
     * @param reportsApi        レポート API
     * @param categoryApi       カテゴリAPI
     */
    @Autowired
    public GoodsSalesController(GoodsSalesHelper goodsSalesHelper,
                                ConversionUtility conversionUtility,
                                ReportsApi reportsApi,
                                CategoryApi categoryApi) {
        this.goodsSalesHelper = goodsSalesHelper;
        this.reportsApi = reportsApi;
        this.categoryApi = categoryApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面初期化
     *
     * @param goodsSalesModel 商品別販売価格別集計モデル
     * @param error
     * @param redirectAttrs
     * @param model
     * @return ページクラス
     *
     * @throws ParseException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @GetMapping(value = "/")
    public String doLoadIndex(GoodsSalesModel goodsSalesModel,
                              BindingResult error,
                              RedirectAttributes redirectAttrs,
                              Model model) throws ParseException, InvocationTargetException, IllegalAccessException {
        clearModel(GoodsSalesModel.class, goodsSalesModel, model);

        initComponentValue(goodsSalesModel);

        return "totaling/goodssales/index";
    }

    /**
     * 表示切替
     *
     * @param goodsSalesModel 商品別販売価格別集計モデル
     * @param error
     * @param redirectAttrs
     * @param model
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "totaling/goodssales/index")
    protected String doDisplayChange(@Validated GoodsSalesModel goodsSalesModel,
                                     BindingResult error,
                                     RedirectAttributes redirectAttrs,
                                     Model model) {

        if (error.hasErrors()) {
            return "totaling/goodssales/index";
        }

        goodsSalesModel.setRangeDateStr(goodsSalesHelper.buildRangeSearchTime(goodsSalesModel.getAggregateTimeFrom(),
                                                                              goodsSalesModel.getAggregateTimeTo(),
                                                                              goodsSalesModel.getAggregateUnit()
                                                                             ));

        // 検索処理を行う
        search(goodsSalesModel, error, redirectAttrs, model);
        return "totaling/goodssales/index";
    }

    /**
     * 検索
     *
     * @param goodsSalesModel 商品別販売価格別集計モデル
     * @param error
     * @param redirectAttrs
     * @param model
     * @return 遷移先ページ
     */
    @PostMapping(value = "/", params = "doSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "totaling/goodssales/index")
    protected String doSearch(@Validated GoodsSalesModel goodsSalesModel,
                              BindingResult error,
                              RedirectAttributes redirectAttrs,
                              Model model) {

        if (error.hasErrors()) {
            return "totaling/goodssales/index";
        }

        goodsSalesModel.setRangeDateStr(goodsSalesHelper.buildRangeSearchTime(goodsSalesModel.getAggregateTimeFrom(),
                                                                              goodsSalesModel.getAggregateTimeTo(),
                                                                              goodsSalesModel.getAggregateUnit()
                                                                             ));

        // 検索処理を行う
        search(goodsSalesModel, error, redirectAttrs, model);
        return "totaling/goodssales/index";

    }

    /**
     * 商品別販売価格別集計CSV出力処理を実行します
     *
     * @param response
     * @param goodsSalesModel 商品別販売価格別集計モデル
     * @param error
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('REPORT:8')")
    @PostMapping(value = "/", params = "doCsvOutput")
    @HEHandler(exception = AppLevelListException.class, returnView = "totaling/goodssales/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "totaling/goodssales/index")
    public String doCsvOutput(HttpServletResponse response,
                              @Validated GoodsSalesModel goodsSalesModel,
                              BindingResult error,
                              Model model) {

        if (error.hasErrors()) {
            return "totaling/goodssales/index";
        }

        try {

            GoodsSalesGetRequest goodsSalesGetRequest = goodsSalesHelper.toGoodsSalesGetRequest(goodsSalesModel);
            reportsApi.downloadGoodsSales(new PageInfoRequest(), goodsSalesGetRequest);
            goodsSalesModel.setSelectGoodsSalesCSVFlag(true);

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        return "totaling/goodssales/index";
    }

    /**
     * カテゴリーリスト取得
     *
     * @param searchKeywords
     * @param limit
     * @return カテゴリ一覧レスポンス
     */
    @GetMapping(value = "/category/ajax")
    @ResponseBody
    public ResponseEntity<CategoryListResponse> getCategoryList(
                    @RequestParam(value = "searchKeywords", required = false) String searchKeywords,
                    @RequestParam("limit") Integer limit) {

        // カテゴリ一覧取得リクエスト
        CategoryListGetRequest categoryListGetRequest = new CategoryListGetRequest();
        categoryListGetRequest.setCategorySearchKeyword(searchKeywords);

        jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest pageInfoRequest =
                        new jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット(並び順項目＝カテゴリー名,並び順＝true（昇順）,limit＝20件
        pageInfoHelper.setupPageRequest(pageInfoRequest, 1, limit, "categoryId", true);

        // カテゴリ一覧取得レスポンス
        CategoryListResponse categoryListResponse = new CategoryListResponse();
        try {
            // 画面表示用商品グループ取得
            categoryListResponse = categoryApi.get(categoryListGetRequest, pageInfoRequest);

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        } catch (HttpClientErrorException ce) {
            LOGGER.error("カテゴリ一覧取得", ce);
        }

        return ResponseEntity.ok(categoryListResponse);
    }

    /**
     * 選択されたカテゴリをモデルに更新 （Ajax）<br/>
     *
     * @return カテゴリーリスト
     */
    @PostMapping(value = "/category/update/ajax")
    @ResponseBody
    public ResponseEntity<?> updateSelectedCategoryList(
                    @RequestBody CategoryAndTagForGoodsRegistUpdateDto categoryChosenDto,
                    GoodsSalesModel goodsSalesModel) {

        if (CollectionUtil.isNotEmpty(categoryChosenDto.getCategoryChosenList())) {
            goodsSalesModel.setCategoryList(categoryChosenDto.getCategoryChosenList());
        } else {
            goodsSalesModel.setCategoryList(new ArrayList<>());
        }

        return ResponseEntity.ok("Success");
    }

    /**
     * 検索処理
     *
     * @param goodsSalesModel 商品別販売価格別集計モデル
     * @param error
     * @param redirectAttrs
     * @param model
     */
    private void search(GoodsSalesModel goodsSalesModel,
                        BindingResult error,
                        RedirectAttributes redirectAttrs,
                        Model model) {

        try {
            // リクエスト用のページャーを生成
            PageInfoRequest pageInfoRequest = new PageInfoRequest();

            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
            // リクエスト用のページャー項目をセット
            pageInfoHelper.setupPageRequest(pageInfoRequest,
                                            conversionUtility.toInteger(goodsSalesModel.getPageNumber()),
                                            goodsSalesModel.getLimit(), goodsSalesModel.getOrderField(),
                                            goodsSalesModel.isOrderAsc(), DEFAULT_LIMIT
                                           );

            GoodsSalesGetRequest goodsSalesGetRequest = goodsSalesHelper.toGoodsSalesGetRequest(goodsSalesModel);
            GoodsSalesResponse goodsSalesResponse = reportsApi.getGoodsSales(pageInfoRequest, goodsSalesGetRequest);

            List<GoodsSaleResponse> goodsSaleResponseList = goodsSalesResponse.getGoodsSalesList();
            goodsSalesHelper.buildGoodsSalesItemList(goodsSaleResponseList, goodsSalesModel);

            if (Boolean.TRUE.equals(goodsSalesResponse.getOverFlag())) {
                // メッセージを設定
                addMessage("ARX000101", new Object[] {pageInfoRequest.getLimit()}, redirectAttrs, model);
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        } catch (ParseException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.debug("日時のパースに失敗しています。バリデーションは行いません。");
        }
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

            if (file.matches(SOLDGOODS_FILE_NAME_REGEX)) {
                path = Paths.get(PropertiesUtil.getSystemPropertiesValue(SOLDGOODS_FILE_PATH) + file);
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
     * 初期化
     *
     * @param goodsSalesModel 商品別販売価格別集計モデル
     */
    private void initComponentValue(GoodsSalesModel goodsSalesModel) {

        goodsSalesModel.setLimit(DEFAULT_LIMIT);
        goodsSalesModel.setPageNumber(DEFAULT_PNUM);

        if (StringUtils.isEmpty(goodsSalesModel.getAggregateTimeFrom()) || StringUtils.isEmpty(
                        goodsSalesModel.getAggregateTimeTo())) {
            goodsSalesModel.setAggregateTimeFrom(goodsSalesHelper.getSearchTimeFrom());
            goodsSalesModel.setAggregateTimeTo(goodsSalesHelper.getSearchTimeTo());
        }

        goodsSalesModel.setDeviceItems(EnumTypeUtil.getEnumMap(HTypeOrderDevice.class));
    }

}