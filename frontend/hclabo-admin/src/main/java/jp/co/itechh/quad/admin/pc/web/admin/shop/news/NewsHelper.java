/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.news;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.news.presentation.api.param.NewsListGetRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsListResponse;
import jp.co.itechh.quad.news.presentation.api.param.NewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ニュース検索Helper
 *
 * @author kimura
 */
@Component
public class NewsHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    @Autowired
    public NewsHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面入力値から検索条件Dtoを作成する
     *
     * @param newsModel 検索画面のページ情報
     * @return ニュース検索条件リクエスト(管理者用)
     */
    public NewsListGetRequest toSearchForNewsListGetRequest(NewsModel newsModel) {

        NewsListGetRequest request = new NewsListGetRequest();

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // ニュース日時From
        request.setNewsTimeFrom(
                        conversionUtility.toDate(conversionUtility.toTimeStamp(newsModel.getSearchNewsTimeFrom())));

        // ニュース日時To
        request.setNewsTimeTo(conversionUtility.toDate(
                        dateUtility.getEndOfDate(conversionUtility.toTimeStamp(newsModel.getSearchNewsTimeTo()))));

        // 公開状態
        HTypeOpenStatus status =
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, newsModel.getSearchNewsOpenStatus());
        if (status != null) {
            request.setOpenStatus(status.getValue());
        } else {
            request.setOpenStatus(null);
        }

        // 公開開始日時From
        request.setOpenStartTimeFrom(conversionUtility.toDate(newsModel.getSearchNewsOpenStartTimeFrom()));

        // 公開開始日時To
        request.setOpenStartTimeTo(conversionUtility.toDate(dateUtility.getEndOfDate(
                        conversionUtility.toTimeStamp(newsModel.getSearchNewsOpenStartTimeTo()))));

        // 公開終了日時From
        request.setOpenEndTimeFrom(conversionUtility.toDate(newsModel.getSearchNewsOpenEndTimeFrom()));

        // 公開終了日時To
        request.setOpenEndTimeTo(conversionUtility.toDate(dateUtility.getEndOfDate(
                        conversionUtility.toTimeStamp(newsModel.getSearchNewsOpenEndTimeTo()))));

        // タイトル
        request.setTitle(newsModel.getSearchTitle());

        // URL
        request.setUrl(newsModel.getSearchURL());

        // 本文・詳細
        request.setBodyNote(newsModel.getSearchBodyNote());

        return request;
    }

    /**
     * 検索結果をモデルに反映<br/>
     *
     * @param newsListResponse ニュースリストレスポンス
     * @param newsModel        ニュースモデル
     * @param request          検索リクエスト
     */
    public void toPageForSearch(NewsListResponse newsListResponse, NewsModel newsModel, NewsListGetRequest request) {

        int index = ((newsListResponse.getPageInfo().getPage() - 1) * newsListResponse.getPageInfo().getLimit()) + 1;
        List<NewsModelItem> resultItemList = new ArrayList<>();

        // 現在時刻取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();

        for (NewsResponse newsResponse : newsListResponse.getNewsList()) {
            NewsModelItem newsModelItem = ApplicationContextUtility.getBean(NewsModelItem.class);
            newsModelItem.setResultNo(index++);
            newsModelItem.setNewsSeq(newsResponse.getNewsSeq());
            newsModelItem.setNewsTime(conversionUtility.toTimestamp(newsResponse.getNewsTime()));
            newsModelItem.setTitlePC(newsResponse.getTitle());
            newsModelItem.setNewsOpenStatusPC(newsResponse.getNewsOpenStatus());
            newsModelItem.setNewsNotePc(newsResponse.getNewsNote());
            // ニュース表示ステータス設定
            String newsDisplayStatus = getNewsDisplayStatus(newsResponse, currentTime);
            newsModelItem.setNewsDisplayStatusPC(newsDisplayStatus);
            if (NewsModel.NEWS_DISPLAY_STATUS_DISPLAY.equals(newsDisplayStatus)) {
                newsModelItem.setNewsDisplayStatus(NewsModel.NEWS_DISPLAY_STATUS_DISPLAY);
            } else {
                newsModelItem.setNewsDisplayStatus(NewsModel.NEWS_DISPLAY_STATUS_HIDE);
            }

            resultItemList.add(newsModelItem);
        }

        newsModel.setResultItems(resultItemList);
    }

    /**
     * ニュース表示ステータス取得処理<br/>
     * ・ニュース公開ステータスが非公開の場合、「非表示：０」
     * ・ニュース公開ステータスが公開中で、公開開始日が未来の場合、「非表示：０」
     * ・ニュース公開ステータスが公開中で、公開終了日が過去の場合、「非表示：０」
     * ・上記以外の場合、「表示中：１」
     *
     * @param newsResponse ニュースレスポンス
     * @param currentTime  現在時刻
     * @param customParams 案件用引数
     * @return ニュース表示ステータス 「非表示：０」、「表示中：１」
     */
    protected String getNewsDisplayStatus(NewsResponse newsResponse, Timestamp currentTime, Object... customParams) {

        // ニュース公開ステータス取得
        HTypeOpenStatus newsOpenStatus =
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, newsResponse.getNewsOpenStatus());
        // ニュース公開開始時間取得
        Date newsOpenStartTime = newsResponse.getNewsOpenStartTime();
        // ニュース公開終了時間取得
        Date newsOpenEndTime = newsResponse.getNewsOpenEndTime();

        return getNewsDisplayStatus(currentTime, newsOpenStatus, newsOpenStartTime, newsOpenEndTime);
    }

    /**
     * ニュース表示ステータス取得処理<br/>
     * ・ニュース公開ステータスが非公開の場合、「非表示：０」
     * ・ニュース公開ステータスが公開中で、公開開始日が未来の場合、「非表示：０」
     * ・ニュース公開ステータスが公開中で、公開終了日が過去の場合、「非表示：０」
     * ・上記以外の場合、「表示中：１」
     *
     * @param currentTime       現在時刻
     * @param newsOpenStatus    ニュース公開ステータス
     * @param newsOpenStartTime ニュース公開開始日
     * @param newsOpenEndTime   ニュース公開終了日
     * @param customParams      案件用引数
     * @return ニュース表示ステータス 「非表示：０」、「表示中：１」
     */
    protected String getNewsDisplayStatus(Timestamp currentTime,
                                          HTypeOpenStatus newsOpenStatus,
                                          Date newsOpenStartTime,
                                          Date newsOpenEndTime,
                                          Object... customParams) {
        if (HTypeOpenStatus.NO_OPEN.equals(newsOpenStatus)) {
            // 公開ステータスが非公開の場合、「非表示：０」
            return NewsModel.NEWS_DISPLAY_STATUS_HIDE;
        } else {
            // 公開ステータスが公開の場合、期間判定
            if (newsOpenStartTime != null && currentTime.before(newsOpenStartTime)) {
                // 公開開始日が未来の場合、「非表示：０」
                return NewsModel.NEWS_DISPLAY_STATUS_HIDE;
            } else if (newsOpenEndTime != null && currentTime.after(newsOpenEndTime)) {
                // 公開終了日が過去の場合、「非表示：０」
                return NewsModel.NEWS_DISPLAY_STATUS_HIDE;
            } else {
                // 公開期間の指定なし、または公開期間中の場合、「表示中：１」
                return NewsModel.NEWS_DISPLAY_STATUS_DISPLAY;
            }
        }
    }
}