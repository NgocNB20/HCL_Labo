package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeAreaOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.validation.TargetDateTimeValidator;
import jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.validation.group.FreeAreaSearchGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.freearea.presentation.api.FreeareaApi;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListGetRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.PageInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * フリーエリア検索コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/freearea")
@Controller
@SessionAttributes(value = "freeareaModel")
@PreAuthorize("hasAnyAuthority('SHOP:4')")
public class FreeareaController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeareaController.class);

    /**
     * デフォルトページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト：ソート項目
     */
    private static final String DEFAULT_ORDER_FIELD = "freeAreaKey";

    /**
     * デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_ORDER_ASC = true;

    /**
     * フリーエリア削除成功メッセージコード<br/>
     * <code>MSGCD_FREEAREA_DELETE_SUCCESS</code>
     */
    public static final String MSGCD_FREEAREA_DELETE_SUCCESS = "ASF000102I";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * フリーエリア削除失敗メッセージコード<br/>
     * <code>MSGCD_FREEAREA_DELETE_FAIL</code>
     */
    public static final String MSGCD_FREEAREA_DELETE_FAIL = "ASF000103W";

    /**
     * ニュースAPI
     */
    private final FreeareaApi freeareaApi;
    /**
     * ニュースAPI
     */
    private final FreeareaHelper freeareaHelper;
    /**
     * 表示状態-日付（日、時間）
     */
    private final TargetDateTimeValidator targetDateTimeValidator;

    /**
     * コンストラクタ
     */
    @Autowired
    public FreeareaController(FreeareaApi freeareaApi,
                              FreeareaHelper freeareaHelper,
                              TargetDateTimeValidator targetDateTimeValidator) {
        this.freeareaApi = freeareaApi;
        this.freeareaHelper = freeareaHelper;
        this.targetDateTimeValidator = targetDateTimeValidator;
    }

    @InitBinder(value = "freeareaModel")
    public void initBinder(WebDataBinder error) {
        // 登録更新の動的バリデータをセット
        error.addValidators(targetDateTimeValidator);
    }

    /**
     *
     *
     *
     * @return 自画面
     */
    /**
     * リスト画面：初期表示
     *
     * @param freeareaModel フリーエリア検索モデル
     * @param model         Model
     * @return 自画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              FreeareaModel freeareaModel,
                              BindingResult error,
                              Model model) throws ParseException {

        // 再検索の場合
        if (model.containsAttribute(FLASH_MD) || (md.isPresent())) {
            // 再検索を実行
            search(freeareaModel, error, model);
            if (error.hasErrors()) {
                return "freearea/index";
            }
        } else {
            clearModel(FreeareaModel.class, freeareaModel, model);
        }
        // プルダウンアイテム情報を取得
        initComponentValue(freeareaModel);
        return "freearea/index";
    }

    /**
     * 表示順変更
     *
     * @param freeareaModel フリーエリア検索モデル
     * @param error
     * @param model         Model
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/index")
    public String doDisplayChange(@Validated(DisplayChangeGroup.class) FreeareaModel freeareaModel,
                                  BindingResult error,
                                  Model model) throws ParseException {

        if (error.hasErrors()) {
            return "freearea/index";
        }

        // 検索
        this.search(freeareaModel, error, model);
        return "freearea/index";
    }

    /**
     * フリーエリア検索
     *
     * @param freeareaModel      フリーエリア検索モデル
     * @param error
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping(value = "/", params = "doFreeAreaSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/index")
    public String doFreeAreaSearch(@Validated(FreeAreaSearchGroup.class) FreeareaModel freeareaModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   Model model) throws ParseException {

        if (error.hasErrors()) {
            return "freearea/index";
        }

        // ページング関連項目初期化（limitは画面プルダウンで指定されてくる）
        freeareaModel.setPageNumber(DEFAULT_PNUM);
        freeareaModel.setOrderField(DEFAULT_ORDER_FIELD);
        freeareaModel.setOrderAsc(DEFAULT_ORDER_ASC);

        // 検索
        search(freeareaModel, error, model);
        return "freearea/index";
    }

    /**
     * フリーエリア削除
     *
     * @param freeareaModel フリーエリア検索モデル
     * @param redirectAttrs
     * @param model
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doFreeAreaDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "freearea/index")
    public String doFreeAreaDelete(FreeareaModel freeareaModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttrs,
                                   Model model) throws ParseException {
        try {
            freeareaApi.deleteWithHttpInfo(freeareaModel.getDeleteFreeAreaSeq());
            FreeareaResultItem freeareaResultItem = freeareaModel.getResultItems()
                                                                 .stream()
                                                                 .filter(resultItem -> resultItem.getFreeAreaSeq()
                                                                                       != null
                                                                                       && resultItem.getFreeAreaSeq()
                                                                                                    .equals(freeareaModel.getDeleteFreeAreaSeq()))
                                                                 .findFirst()
                                                                 .orElse(new FreeareaResultItem());
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            // 公開開始日を画面表示用に変換
            String openStartTime = dateUtility.format(freeareaResultItem.getOpenStartTime(), DateUtility.YMD_SLASH_HMS);
            //削除成功メッセージ登録
            addInfoMessage(MSGCD_FREEAREA_DELETE_SUCCESS,
                           new Object[] {freeareaResultItem.getFreeAreaKey(), openStartTime}, redirectAttrs, model
                          );
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 削除失敗メッセージ登録
            addMessage(MSGCD_FREEAREA_DELETE_FAIL, null, redirectAttrs, model);
            e.printStackTrace();
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "freearea/index";
        }
        // 削除後再検索
        this.search(freeareaModel, error, model);
        return "freearea/index";
    }

    /**
     * フリーエリア登録画面へ遷移
     *
     * @param freeareaModel
     * @param model
     * @return フリーエリア登録画面
     */
    @PreAuthorize("hasAnyAuthority('SHOP:8')")
    @PostMapping(value = "/", params = "doRegist")
    public String doRegist(FreeareaModel freeareaModel, Model model) {

        return "redirect:/freearea/registupdate";
    }

    /**
     * 検索処理
     *
     * @param freeareaModel
     * @param model
     */
    private void search(FreeareaModel freeareaModel, BindingResult error, Model model) throws ParseException {

        try {
            // 条件取得
            FreeAreaListGetRequest freeAreaListGetRequest =
                            freeareaHelper.toFreeAreaListGetRequestForSearch(freeareaModel);
            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            pageInfoHelper.setupPageRequest(pageInfoRequest, Integer.parseInt(freeareaModel.getPageNumber()),
                                            freeareaModel.getLimit(), freeareaModel.getOrderField(),
                                            freeareaModel.isOrderAsc()
                                           );
            // 検索
            FreeAreaListResponse freeAreaEntityList = freeareaApi.get(freeAreaListGetRequest, pageInfoRequest);

            // ページへ反映
            freeareaHelper.toPageForSearch(freeAreaEntityList, freeareaModel, freeAreaListGetRequest);
            // ページャーにレスポンス情報をセット
            PageInfoResponse pageInfoResponse = freeAreaEntityList.getPageInfo();
            pageInfoHelper.setupPageInfo(freeareaModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages()
                                        );
            // ページャーセットアップ
            pageInfoHelper.setupViewPager(freeareaModel.getPageInfo(), freeareaModel);

            // 件数セット
            freeareaModel.setTotalCount(Objects.requireNonNull(freeAreaEntityList.getFreeareaList()).size());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("freeAreaKey", "searchFreeAreaKey");
            itemNameAdjust.put("freeAreaTitle", "searchFreeAreaTitle");
            itemNameAdjust.put("openStartTimeFrom", "searchOpenStartTimeFrom");
            itemNameAdjust.put("openStartTimeTo", "searchOpenStartTimeTo");
            itemNameAdjust.put("dateType", "searchDateType");
            itemNameAdjust.put("targetDate", "searchTargetDate");
            itemNameAdjust.put("targetTime", "searchTargetTime");
            itemNameAdjust.put("siteMapFlag", "searchSiteMapFlag");
            itemNameAdjust.put("openStatusList", "searchOpenStatusList");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param freeareaModel フリーエリア検索モデル
     */
    private void initComponentValue(FreeareaModel freeareaModel) {
        // プルダウンアイテム情報を取得
        freeareaModel.setSearchOpenStateArrayItems(EnumTypeUtil.getEnumMap(HTypeFreeAreaOpenStatus.class));
        freeareaModel.setSearchSiteMapFlagItems(EnumTypeUtil.getEnumMap(HTypeSiteMapFlag.class));
        freeareaModel.setSearchDateType("0");
    }
}