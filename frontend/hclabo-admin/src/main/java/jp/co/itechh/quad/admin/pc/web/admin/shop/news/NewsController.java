/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.news;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.news.presentation.api.NewsApi;
import jp.co.itechh.quad.news.presentation.api.param.NewsListGetRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsListResponse;
import jp.co.itechh.quad.news.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.news.presentation.api.param.PageInfoResponse;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ニュース検索コントローラ
 *
 * @author kimura
 */
@RequestMapping("/news")
@Controller
@SessionAttributes(value = "newsModel")
@PreAuthorize("hasAnyAuthority('SHOP:4')")
public class NewsController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    /**
     * デフォルトページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト：ソート項目
     */
    private static final String DEFAULT_ORDER_FIELD = "newsTime";

    /**
     * デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_ORDER_ASC = false;

    /**
     * ニュース削除成功メッセージコード<br/>
     * <code>MSGCD_NEWS_DELETE_SUCCESS</code>
     */
    public static final String MSGCD_NEWS_DELETE_SUCCESS = "ASN000103I";

    /**
     * ニュース削除失敗メッセージコード<br/>
     * <code>MSGCD_NEWS_DELETE_FAIL</code>
     */
    public static final String MSGCD_NEWS_DELETE_FAIL = "ASN000104W";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * ニュースAPI
     */
    private final NewsApi newsApi;

    /**
     * ニュース検索ページDxo
     */
    private final NewsHelper newsHelper;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    @Autowired
    public NewsController(NewsApi newsApi, NewsHelper newsHelper, ConversionUtility conversionUtility) {
        this.newsApi = newsApi;
        this.newsHelper = newsHelper;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期表示
     *
     * @return 自画面
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "news/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              NewsModel newsModel,
                              BindingResult error,
                              Model model) {

        // 再検索の場合
        if (model.containsAttribute(FLASH_MD) || (md.isPresent())) {
            // 再検索を実行
            search(newsModel, error, model);
        } else {
            clearModel(NewsModel.class, newsModel, model);
        }
        initComponentValue(newsModel);
        return "news/index";
    }

    /**
     * ニュース検索
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doNewsSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "news/index")
    public String doNewsSearch(@Validated NewsModel newsModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (error.hasErrors()) {
            return "news/index";
        }
        // ページング関連項目初期化（limitは画面プルダウンで指定されてくる）
        newsModel.setPageNumber(DEFAULT_PNUM);
        newsModel.setOrderField(DEFAULT_ORDER_FIELD);
        newsModel.setOrderAsc(DEFAULT_ORDER_ASC);

        // 検索
        this.search(newsModel, error, model);
        return "news/index";
    }

    /**
     * 表示順変更
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "news/index")
    public String doDisplayChange(@Validated NewsModel newsModel, BindingResult error, Model model) {
        if (error.hasErrors()) {
            return "news/index";
        }

        // 検索
        search(newsModel, error, model);
        return "news/index";
    }

    /**
     * ニュース削除変更
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doNewsDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "news/index")
    public String doNewsDelete(NewsModel newsModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        try {
            // 削除
            newsApi.delete(newsModel.getDeleteNewsSeq());
            // 削除成功メッセージ登録
            addInfoMessage(MSGCD_NEWS_DELETE_SUCCESS, null, redirectAttributes, model);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 削除失敗メッセージ登録
            addMessage(MSGCD_NEWS_DELETE_FAIL, null, redirectAttributes, model);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("newsSeq", "deleteNewsSeq");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "news/index";
        }

        // 削除後再検索
        search(newsModel, error, model);
        return "news/index";
    }

    /**
     * ニュース登録更新画面へ遷移(登録)
     *
     * @return ニュース登録更新画面
     */
    @PreAuthorize("hasAnyAuthority('SHOP:8')")
    @PostMapping(value = "/", params = "doRegist")
    public String doRegist() {
        return "redirect:/news/registupdate";
    }

    /**
     * 検索
     *
     * @param newsModel
     * @param model
     */
    protected void search(NewsModel newsModel, BindingResult error, Model model) {
        try {
            // 検索条件作成
            NewsListGetRequest request = newsHelper.toSearchForNewsListGetRequest(newsModel);

            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            // リクエスト用のページャーを生成
            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            // リクエスト用のページャー項目をセット
            // リクエスト用のページャー項目をセット
            pageInfoHelper.setupPageRequest(pageInfoRequest, conversionUtility.toInteger(newsModel.getPageNumber()),
                                            newsModel.getLimit(), newsModel.getOrderField(), newsModel.isOrderAsc()
                                           );

            // 検索
            NewsListResponse newsListResponse = newsApi.get(request, pageInfoRequest);

            // ページへ反映
            newsHelper.toPageForSearch(newsListResponse, newsModel, request);

            // ページャーにレスポンス情報をセット
            PageInfoResponse tmp = newsListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(newsModel, tmp.getPage(), tmp.getLimit(), tmp.getNextPage(), tmp.getPrevPage(),
                                         tmp.getTotal(), tmp.getTotalPages()
                                        );
            // ページャーをセット
            pageInfoHelper.setupViewPager(newsModel.getPageInfo(), newsModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("inquiryGroupSeq", "searchInquiryGroupSeq");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    private void initComponentValue(NewsModel newsModel) {
        // プルダウンアイテム情報を取得
        newsModel.setSearchNewsOpenStatusItems(EnumTypeUtil.getEnumMap(HTypeOpenStatus.class));
    }
}