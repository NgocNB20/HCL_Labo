package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.front.dto.shop.NewsSearchForDaoConditionDto;
import jp.co.itechh.quad.front.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.news.presentation.api.NewsApi;
import jp.co.itechh.quad.news.presentation.api.param.NewsListGetRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsListResponse;
import jp.co.itechh.quad.news.presentation.api.param.PageInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * トップ画面 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 * @version $Revision: 1.0 $
 *
 */
@RequestMapping("/")
@Controller
public class IndexController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    /** １ページ当たりのニュースデフォルト最大表示件数 */
    private static final String NEWS_DEFAULT_LIMIT = "10";

    /** トップ画面のHelper */
    private IndexHelper indexHelper;

    /** ニュースAPI */
    private final NewsApi newsApi;

    /** トップ画面 : ソート項目 */
    private static final String DEFAULT_INDEX_ORDER_FIELD = "newstime";

    /**
     * コンストラクタ
     *
     * @param indexHelper トップ画面 Helper
     * @param newsApi ニュースAPI
     */
    @Autowired
    public IndexController(IndexHelper indexHelper, NewsApi newsApi) {
        this.indexHelper = indexHelper;
        this.newsApi = newsApi;
    }

    /**
     * トップ画面：初期処理
     *
     * @param indexModel
     * @param model
     * @return トップ画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "index")
    protected String doLoadIndex(IndexModel indexModel,
                                 BindingResult error,
                                 @RequestParam(required = false, defaultValue = NEWS_DEFAULT_LIMIT) int limit,
                                 Model model) {

        // ニュースを取得
        indexHelper.toPageForLoadNews(getNewsList(limit, error), indexModel);

        if (error.hasErrors()) {
            return "index";
        }

        return "index";

    }

    /**
     * ニュース一覧取得<br/>
     *
     * @param limit 制限
     * @return ニュースエンティティ
     */
    protected List<NewsEntity> getNewsList(int limit, BindingResult error) {

        NewsSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(NewsSearchForDaoConditionDto.class);
        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        pageInfoModule.setupPageRequest(pageInfoRequest, null, limit, DEFAULT_INDEX_ORDER_FIELD, false);

        NewsListGetRequest newsListGetRequest = new NewsListGetRequest();
        newsListGetRequest.setOpenStatus(HTypeOpenStatus.OPEN.getValue());
        newsListGetRequest.setIsTopScreen(true);

        NewsListResponse newsListResponse = null;
        try {
            newsListResponse = newsApi.get(newsListGetRequest, pageInfoRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            return null;
        }

        List<NewsEntity> newsEntityList = indexHelper.toListNewsEntity(newsListResponse);

        return newsEntityList;
    }

}