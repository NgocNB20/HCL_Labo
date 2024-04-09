/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.news.presentation.api.param.NewsResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * ニュース詳細 Helper
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
     * 画面表示・再表示<br/>
     *
     * @param newsResponse ニュースレスポンス
     * @param newsModel  ニュースModel
     */
    public void toPageForLoad(NewsResponse newsResponse, NewsModel newsModel) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        Timestamp now = dateUtility.getCurrentTime();

        // 表示対象外のニュースの場合は、何もしない
        if (newsResponse == null || (StringUtils.isNotEmpty(newsResponse.getNewsOpenStatus())
                                     && newsResponse.getNewsOpenStatus().equals(HTypeOpenStatus.NO_OPEN.getValue()))
            || (newsResponse.getNewsOpenStartTime() != null && newsResponse.getNewsOpenStartTime().compareTo(now) > 0)
            || (newsResponse.getNewsOpenEndTime() != null && newsResponse.getNewsOpenEndTime().compareTo(now) < 0)) {

            newsModel.setNewsBodyPC(null);
            newsModel.setNewsNotePC(null);
            newsModel.setTitlePC(null);
            newsModel.setNewsTime(null);

            return;
        }

        newsModel.setNewsBodyPC(newsResponse.getNewsBody());
        newsModel.setNewsNotePC(newsResponse.getNewsNote());
        newsModel.setTitlePC(newsResponse.getTitle());
        newsModel.setNewsTime(conversionUtility.toTimestamp(newsResponse.getNewsTime()));
    }

}