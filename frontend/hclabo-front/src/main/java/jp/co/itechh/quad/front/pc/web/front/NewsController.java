/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.news.presentation.api.NewsApi;
import jp.co.itechh.quad.news.presentation.api.param.NewsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * ニュース詳細 Controller
 *
 * @author kimura
 */
@RequestMapping("/")
@Controller
public class NewsController extends AbstractController {

    /** ニュースAPI */
    private final NewsApi newsApi;

    /** ニュース画面のHelperクラス */
    private final NewsHelper newsHelper;

    /** アプリケーションログ出力Utility */
    private final ApplicationLogUtility applicationLogUtility;

    /** ログ */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    /**
     * コンストラクタ
     *
     * @param newsApi
     * @param newsHelper
     * @param applicationLogUtility
     */
    @Autowired
    public NewsController(NewsApi newsApi, NewsHelper newsHelper, ApplicationLogUtility applicationLogUtility) {
        this.newsApi = newsApi;
        this.newsHelper = newsHelper;
        this.applicationLogUtility = applicationLogUtility;
    }

    /**
     * ニュース詳細画面：初期処理
     *
     * @param newsModel
     * @param model
     * @return ニュース詳細画面
     */
    @GetMapping(value = "/news")
    @HEHandler(exception = AppLevelListException.class, returnView = "news")
    protected String doLoadIndex(NewsModel newsModel, BindingResult error, Model model) {

        NewsResponse newsResponse = null;

        Integer nSeq = 0;
        try {
            nSeq = Integer.parseInt(newsModel.getNseq());
        } catch (NumberFormatException e) {
            // 不正なnSeqの場合、後続でエラーハンドリングが行われるので、ここではチェックしない
            LOGGER.error("例外処理が発生しました", e);
        }

        if (nSeq.intValue() > 0) {
            try {
                newsResponse = newsApi.getByNewsSeq(nSeq);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }

            if (error.hasErrors()) {
                return "news";
            }

        }

        newsHelper.toPageForLoad(newsResponse, newsModel);

        if (newsModel.getNewsTime() == null) {
            // ニュースが非公開または存在しない
            LOGGER.debug("存在しない、もしくは公開されていないニュースが選択されました");
            return "redirect:/error";
        }

        return "news";
    }
}