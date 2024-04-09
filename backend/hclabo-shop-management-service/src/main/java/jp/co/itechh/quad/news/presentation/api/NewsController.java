/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.news.presentation.api;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.dto.shop.NewsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.NewsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.shop.news.NewsDeleteLogic;
import jp.co.itechh.quad.core.logic.shop.news.NewsGetLogic;
import jp.co.itechh.quad.core.service.shop.news.NewsDetailsGetService;
import jp.co.itechh.quad.core.service.shop.news.NewsForBackListGetService;
import jp.co.itechh.quad.core.service.shop.news.NewsRegistService;
import jp.co.itechh.quad.core.service.shop.news.NewsUpdateService;
import jp.co.itechh.quad.core.service.shop.news.OpenNewsListGetService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.news.presentation.api.param.NewsListGetRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsListResponse;
import jp.co.itechh.quad.news.presentation.api.param.NewsRegistRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsResponse;
import jp.co.itechh.quad.news.presentation.api.param.NewsUpdateRequest;
import jp.co.itechh.quad.news.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.news.presentation.api.param.PageInfoResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * ニュースエンドポイント　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@RestController
public class NewsController extends AbstractController implements ContentManagementApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    /** ニュース画面のHelperクラス */
    private final NewsHelper newsHelper;

    /** ニュース詳細取得サービス */
    private final NewsDetailsGetService newsDetailsGetService;

    /** ニュース管理機能用リスト取得 */
    private final NewsForBackListGetService newsForBackListGetService;

    /** ニュース詳細情報取得Logic */
    private final NewsGetLogic newsGetLogic;

    /** ニュース登録サービス */
    private final NewsRegistService newsRegistService;

    /** ニュース更新サービス */
    private final NewsUpdateService newsUpdateService;

    /** ニュース削除ロジック */
    private final NewsDeleteLogic newsDeleteLogic;

    /** 公開ニュースリスト情報取得サービスクラス */
    private final OpenNewsListGetService openNewsListGetService;

    /** メッセージコード：更新中データ削除 */
    protected static final String MSGCD_DATA_NOT_EXIST = "ASN000105";

    /** ニュース削除失敗メッセージコード */
    public static final String MSGCD_NEWS_DELETE_FAIL = "ASN000104W";

    /**
     * コンストラクター
     * @param newsHelper ニュース画面のHelperクラス
     * @param newsDetailsGetService ニュース詳細取得サービス
     * @param newsForBackListGetService ニュース管理機能用リスト取得
     * @param newsGetLogic ニュース詳細情報取得Logic
     * @param newsRegistService ニュース登録サービス
     * @param newsUpdateService ニュース更新サービス
     * @param newsDeleteLogic ニュース削除ロジック
     * @param openNewsListGetService 公開ニュースリスト情報取得サービスクラス
     */
    public NewsController(NewsHelper newsHelper,
                          NewsDetailsGetService newsDetailsGetService,
                          NewsForBackListGetService newsForBackListGetService,
                          NewsGetLogic newsGetLogic,
                          NewsRegistService newsRegistService,
                          NewsUpdateService newsUpdateService,
                          NewsDeleteLogic newsDeleteLogic,
                          OpenNewsListGetService openNewsListGetService) {
        this.newsHelper = newsHelper;
        this.newsDetailsGetService = newsDetailsGetService;
        this.newsForBackListGetService = newsForBackListGetService;
        this.newsGetLogic = newsGetLogic;
        this.newsRegistService = newsRegistService;
        this.newsUpdateService = newsUpdateService;
        this.newsDeleteLogic = newsDeleteLogic;
        this.openNewsListGetService = openNewsListGetService;
    }

    /**
     * GET /content-management/news/{newsSeq} : ニュース取得
     * ニュース取得
     *
     * @param newsSeq ニュースSEQ (required)
     * @return ニュースレスポンス (status code 200)
     *         or システムエラー (status code 400)
     */
    @Override
    public ResponseEntity<NewsResponse> getByNewsSeq(Integer newsSeq) {

        NewsEntity newsEntity = newsDetailsGetService.execute(newsSeq);

        NewsResponse newsResponse = newsHelper.toNewsResponse(newsEntity);

        return new ResponseEntity<>(newsResponse, HttpStatus.OK);
    }

    /**
     * GET /content-management/news : ニュース一覧取得
     * ニュース一覧取得
     *
     * @param newsListGetRequest ニュース一覧取得リクエスト (optional)
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return ニュース一覧レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NewsListResponse> get(NewsListGetRequest newsListGetRequest,
                                                PageInfoRequest pageInfoRequest) {
        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();

        // 検索
        List<NewsEntity> newsEntityList = new ArrayList<>();
        if (newsListGetRequest.getIsTopScreen() == null || !newsListGetRequest.getIsTopScreen()) {

            // 検索条件作成
            NewsSearchForBackDaoConditionDto conditionDto =
                            newsHelper.toNewsSearchForBackDaoConditionDtoForSearch(newsListGetRequest);

            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                         pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                        );

            newsEntityList = newsForBackListGetService.execute(conditionDto);

            try {
                pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
            }

        } else {
            NewsSearchForDaoConditionDto conditionDto =
                            ApplicationContextUtility.getBean(NewsSearchForDaoConditionDto.class);
            conditionDto.setOpenStatus(HTypeOpenStatus.OPEN);
            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                         pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                        );

            newsEntityList = openNewsListGetService.execute(conditionDto);

            try {
                pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
            }
        }

        // レスポンス
        NewsListResponse newsListResponse = new NewsListResponse();

        // ニュースレスポンス一覧を設定
        List<NewsResponse> newsList = newsHelper.toNewsListResponse(newsEntityList);
        newsListResponse.setNewsList(newsList);

        newsListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(newsListResponse, HttpStatus.OK);
    }

    /**
     * POST /content-management/news : ニュース登録
     * ニュース登録
     *
     * @param newsRegistRequest ニュース登録リクエスト (required)
     * @return ニュースレスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NewsResponse> regist(NewsRegistRequest newsRegistRequest) {

        // 登録
        NewsEntity newsEntity = newsHelper.toNewsEntity(newsRegistRequest);
        newsRegistService.execute(newsEntity);

        // 取得
        NewsEntity newsEntityRes = newsDetailsGetService.execute(newsEntity.getNewsSeq());
        NewsResponse newsResponse = newsHelper.toNewsResponse(newsEntityRes);

        return new ResponseEntity<>(newsResponse, HttpStatus.OK);
    }

    /**
     * PUT /content-management/news/{newsSeq} : ニュース更新
     * ニュース更新
     *
     * @param newsSeq ニュースSEQ (required)
     * @param newsUpdateRequest ニュース更新リクエスト (required)
     * @return ニュースレスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<NewsResponse> update(Integer newsSeq, NewsUpdateRequest newsUpdateRequest) {

        // 編集中ニュース取得
        Integer shopSeq = 1001;
        NewsEntity newsEntity = newsGetLogic.execute(shopSeq, newsSeq);
        if (ObjectUtils.isEmpty(newsEntity)) {
            // 編集中ニュースが削除されている場合、エラー
            String appComplementUrl = PropertiesUtil.getSystemPropertiesValue("app.complement.url");
            throwMessage(MSGCD_DATA_NOT_EXIST, new Object[] {appComplementUrl});
        }

        NewsEntity newsUpdateEntity = newsHelper.toNewsEntity(newsSeq, newsUpdateRequest);
        newsUpdateService.execute(newsUpdateEntity);

        //ニュース取得
        NewsEntity newsEntityRes = newsDetailsGetService.execute(newsEntity.getNewsSeq());
        NewsResponse newsResponse = newsHelper.toNewsResponse(newsEntityRes);

        return new ResponseEntity<>(newsResponse, HttpStatus.OK);
    }

    /**
     * DELETE /content-management/news/{newsSeq} : ニュース削除
     * ニュース削除
     *
     * @param newsSeq ニュースSEQ (required)
     * @return 成功 (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(Integer newsSeq) {

        // 共通情報の取得
        Integer shopSeq = 1001;

        // 削除対象ニュース情報取得
        NewsEntity entity = newsGetLogic.execute(shopSeq, newsSeq);

        int result = 0;
        if (entity != null) {
            result = newsDeleteLogic.execute(entity);
        }

        // 削除失敗（削除済み）
        if (result == 0) {
            // 削除失敗メッセージ登録
            throwMessage(MSGCD_NEWS_DELETE_FAIL);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}