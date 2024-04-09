package jp.co.itechh.quad.admin.pc.web.admin.totaling.searchkeywordaccording;

import jp.co.itechh.quad.accesskeywords.presentation.api.AccessKeywordsApi;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordListResponse;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsCsvGetRequest;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsGetRequest;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.dto.totaling.AccessSearchKeywordTotalDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.download.DownloadApiClient;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 検索キーワード集計画面用コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/searchkeywordaccording")
@Controller
@SessionAttributes(value = "searchKeywordAccordingModel")
@PreAuthorize("hasAnyAuthority('REPORT:4')")
public class SearchKeywordAccordingController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchKeywordAccordingController.class);

    /**
     * デフォルト：ページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト：最大表示件数
     */
    private static final int DEFAULT_LIMIT = 100;

    private final static String SEARCH_KEY_WORD_ACCORDING_DOWNLOAD = "/reports/access-keywords/csv";

    /**
     * 検索キーワード集計Helper
     */
    private final SearchKeywordAccordingHelper searchKeywordAccordingHelper;

    /**
     * キーワード集計 API
     **/
    private final AccessKeywordsApi accessKeywordsApi;

    /**
     * ダウンロードAPIクライアント
     */
    private final DownloadApiClient downloadApiClient;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    private String fileName = "t_SearchKeyword";

    /**
     * コンストラクター
     *
     * @param searchKeywordAccordingHelper 検索キーワード集計Helper
     * @param conversionUtility            変換ユーティリティクラス
     * @param accessKeywordsApi            キーワード集計 API
     * @param downloadApiClient            ダウンロードAPIクライアント
     */
    @Autowired
    public SearchKeywordAccordingController(SearchKeywordAccordingHelper searchKeywordAccordingHelper,
                                            ConversionUtility conversionUtility,
                                            AccessKeywordsApi accessKeywordsApi,
                                            DownloadApiClient downloadApiClient) {
        this.searchKeywordAccordingHelper = searchKeywordAccordingHelper;
        this.conversionUtility = conversionUtility;
        this.downloadApiClient = downloadApiClient;
        this.accessKeywordsApi = accessKeywordsApi;
    }

    /**
     * 初期表示処理を行います
     *
     * @param model the model
     * @return string
     */
    @GetMapping("/")
    public String doLoadIndex(Model model) {

        SearchKeywordAccordingModel searchModel = new SearchKeywordAccordingModel();

        searchModel.setLimit(DEFAULT_LIMIT);
        searchModel.setPageNumber(DEFAULT_PNUM);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 今月の初日、末日を取得し、集計期間にセット
        Timestamp currentDate = dateUtility.getCurrentDate();
        Timestamp startDate = dateUtility.getStartOfMonth(0, currentDate);
        Timestamp endDate = dateUtility.getEndOfMonth(0, currentDate);

        searchModel.setProcessDateFrom(dateUtility.formatYmdWithSlash(startDate));
        searchModel.setProcessDateTo(dateUtility.formatYmdWithSlash(endDate));

        model.addAttribute("searchKeywordAccordingModel", searchModel);

        return "totaling/searchkeywordaccording/index";
    }

    /**
     * 検索キーワード集計処理を実行します
     *
     * @param searchKeywordAccordingModel 検索キーワード集計ページクラス
     * @param error                       エラー
     * @param redirectAttributes          the redirect attributes
     * @param model                       the model
     * @return Class &lt;IndexPage&gt;
     */
    @PostMapping(value = "/", params = "doReportOutput")
    @HEHandler(exception = AppLevelListException.class, returnView = "totaling/searchkeywordaccording/index")
    public String doReportOutput(@Validated SearchKeywordAccordingModel searchKeywordAccordingModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (error.hasErrors()) {
            return "totaling/searchkeywordaccording/index";
        }

        // PageをDtoに変換
        AccessKeywordsGetRequest accessKeywordsGetRequest =
                        searchKeywordAccordingHelper.toAccessKeywordsGetRequest(searchKeywordAccordingModel);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest,
                                        conversionUtility.toInteger(searchKeywordAccordingModel.getPageNumber()),
                                        searchKeywordAccordingModel.getLimit(), pageInfoRequest.getOrderBy(), null,
                                        DEFAULT_LIMIT
                                       );

        try {
            // 集計処理を実施
            AccessKeywordListResponse accessKeywordListResponse =
                            accessKeywordsApi.get(accessKeywordsGetRequest, pageInfoRequest);

            List<AccessSearchKeywordTotalDto> resultDataItem =
                            searchKeywordAccordingHelper.toAccessSearchKeyWordTotalDtoList(
                                            accessKeywordListResponse.getAccessKeywordList());

            // 検索結果リストを設定
            searchKeywordAccordingModel.setResultDataItems(resultDataItem);
            model.addAttribute("searchKeywordAccordingModel", searchKeywordAccordingModel);

            if (Boolean.TRUE.equals(accessKeywordListResponse.getOverFlag())) {
                // メッセージを設定
                addMessage("ARX000101", new Object[] {pageInfoRequest.getLimit()}, redirectAttributes, model);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("searchKeyword", "keyword");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        return "totaling/searchkeywordaccording/index";
    }

    /**
     * 検索キーワード集計CSV出力処理を実行します
     *
     * @param response
     * @param searchKeywordAccordingModel 検索キーワード集計ページクラス
     * @param error                       エラー
     * @param model                       the model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('REPORT:8')")
    @PostMapping(value = "/", params = "doCsvOutput")
    @HEHandler(exception = AppLevelListException.class, returnView = "totaling/searchkeywordaccording/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "totaling/searchkeywordaccording/index")
    public void doCsvOutput(HttpServletResponse response,
                            @Validated SearchKeywordAccordingModel searchKeywordAccordingModel,
                            BindingResult error,
                            Model model) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }

        // 検索条件作成
        AccessKeywordsCsvGetRequest accessKeywordsCsvGetRequest =
                        searchKeywordAccordingHelper.toAccessKeywordsCsvGetRequest(searchKeywordAccordingModel);

        try {
            downloadApiClient.invokeAPI(response, accessKeywordsApi.getApiClient().getBasePath(),
                                        SEARCH_KEY_WORD_ACCORDING_DOWNLOAD, getFileName(), accessKeywordsCsvGetRequest
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
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        if (StringUtils.isEmpty(this.fileName)) {
            this.fileName = "download_file";
        }

        CsvUtility csvUtility = (CsvUtility) ApplicationContextUtility.getBean(CsvUtility.class);
        return csvUtility.getDownLoadCsvFileName(this.fileName);
    }
}