/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.news.registupdate;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.news.presentation.api.param.NewsRegistRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsResponse;
import jp.co.itechh.quad.news.presentation.api.param.NewsUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ニュース登録更新Helper
 *
 * @author kimura
 */
@Component
public class NewsRegistUpdateHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    @Autowired
    public NewsRegistUpdateHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * レスポンス結果をEntityに詰め替える<br/>
     * ※画面ModelにEntityやDtoが宣言されている場合に変換<br/>
     * ※主な用途は、確認画面のDiffUtilで利用
     *
     * @param newsResponse ニュースレスポンス
     */
    public NewsEntity toEntityForResponse(NewsResponse newsResponse) {

        NewsEntity newsEntity = ApplicationContextUtility.getBean(NewsEntity.class);

        // レスポンス情報がある場合
        if (newsResponse != null) {

            // 反映
            newsEntity.setNewsSeq(newsResponse.getNewsSeq());
            newsEntity.setNewsTime(conversionUtility.toTimestamp(newsResponse.getNewsTime()));
            newsEntity.setTitlePC(newsResponse.getTitle());
            newsEntity.setNewsBodyPC(newsResponse.getNewsBody());
            newsEntity.setNewsNotePC(newsResponse.getNewsNote());
            newsEntity.setNewsUrlPC(newsResponse.getNewsUrl());
            newsEntity.setNewsOpenStatusPC(
                            EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, newsResponse.getNewsOpenStatus()));
            newsEntity.setNewsOpenStartTimePC(conversionUtility.toTimestamp(newsResponse.getNewsOpenStartTime()));
            newsEntity.setNewsOpenEndTimePC(conversionUtility.toTimestamp(newsResponse.getNewsOpenEndTime()));
        }
        return newsEntity;
    }

    /**
     * 初期処理時の画面反映
     *
     * @param newsRegistUpdateModel ニュース登録更新モデル
     * @param newsEntity            ニュースエンティティ
     */
    public void toPageForLoad(NewsRegistUpdateModel newsRegistUpdateModel, NewsEntity newsEntity) {

        // 画面へ反映する情報が指定されている場合
        if (newsEntity != null) {

            // 反映
            newsRegistUpdateModel.setNewsSeq(newsEntity.getNewsSeq());

            newsRegistUpdateModel.setNewsDate(conversionUtility.toYmd(newsEntity.getNewsTime()));
            newsRegistUpdateModel.setNewsTime(conversionUtility.toHms(newsEntity.getNewsTime()));

            newsRegistUpdateModel.setTitlePC(newsEntity.getTitlePC());
            newsRegistUpdateModel.setNewsBodyPC(newsEntity.getNewsBodyPC());
            newsRegistUpdateModel.setNewsNotePC(newsEntity.getNewsNotePC());
            newsRegistUpdateModel.setNewsUrlPC(newsEntity.getNewsUrlPC());
            newsRegistUpdateModel.setNewsOpenStatusPC(newsEntity.getNewsOpenStatusPC().getValue());
            newsRegistUpdateModel.setNewsOpenStartDatePC(conversionUtility.toYmd(newsEntity.getNewsOpenStartTimePC()));
            newsRegistUpdateModel.setNewsOpenStartTimePC(conversionUtility.toHms(newsEntity.getNewsOpenStartTimePC()));
            newsRegistUpdateModel.setNewsOpenEndDatePC(conversionUtility.toYmd(newsEntity.getNewsOpenEndTimePC()));
            newsRegistUpdateModel.setNewsOpenEndTimePC(conversionUtility.toHms(newsEntity.getNewsOpenEndTimePC()));

            newsRegistUpdateModel.setNewsEntity(newsEntity);
        }
        newsRegistUpdateModel.setNormality(true);
    }

    /**
     * 確認画面へ遷移前の画面反映
     *
     * @param newsRegistUpdateModel ニュース登録更新画面
     */
    public void toPageForConfirm(NewsRegistUpdateModel newsRegistUpdateModel) {

        // 時刻反映
        newsRegistUpdateModel.setNewsTime(conversionUtility.toDefaultHms(newsRegistUpdateModel.getNewsDate(),
                                                                         newsRegistUpdateModel.getNewsTime(),
                                                                         ConversionUtility.DEFAULT_START_TIME
                                                                        ));
        newsRegistUpdateModel.setNewsOpenStartTimePC(
                        conversionUtility.toDefaultHms(newsRegistUpdateModel.getNewsOpenStartDatePC(),
                                                       newsRegistUpdateModel.getNewsOpenStartTimePC(),
                                                       ConversionUtility.DEFAULT_START_TIME
                                                      ));
        newsRegistUpdateModel.setNewsOpenEndTimePC(
                        conversionUtility.toDefaultHms(newsRegistUpdateModel.getNewsOpenEndDatePC(),
                                                       newsRegistUpdateModel.getNewsOpenEndTimePC(),
                                                       ConversionUtility.DEFAULT_END_TIME
                                                      ));
    }

    /**
     * ニュース登録時の処理
     *
     * @param newsRegistUpdateModel ページ
     * @return ニュース情報
     */
    public NewsRegistRequest toNewsRequestForNewsRegist(NewsRegistUpdateModel newsRegistUpdateModel) {

        NewsRegistRequest request = new NewsRegistRequest();

        request.setNewsTime(this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsDate(),
                                                   newsRegistUpdateModel.getNewsTime()
                                                  ));
        request.setTitle(newsRegistUpdateModel.getTitlePC());
        request.setNewsBody(newsRegistUpdateModel.getNewsBodyPC());
        request.setNewsNote(newsRegistUpdateModel.getNewsNotePC());
        request.setNewsUrl(newsRegistUpdateModel.getNewsUrlPC());
        request.setNewsOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class,
                                                                newsRegistUpdateModel.getNewsOpenStatusPC()
                                                               ).getValue());
        request.setNewsOpenStartTime(conversionUtility.toDate(
                        this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsOpenStartDatePC(),
                                               newsRegistUpdateModel.getNewsOpenStartTimePC()
                                              )));
        request.setNewsOpenEndTime(conversionUtility.toDate(
                        this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsOpenEndDatePC(),
                                               newsRegistUpdateModel.getNewsOpenEndTimePC()
                                              )));

        return request;
    }

    /**
     * ニュース更新時の処理
     *
     * @param newsRegistUpdateModel ページ
     * @return ニュース情報
     */
    public NewsUpdateRequest toNewsRequestForNewsUpdate(NewsRegistUpdateModel newsRegistUpdateModel) {

        NewsUpdateRequest request = new NewsUpdateRequest();

        request.setNewsTime(new Date(this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsDate(),
                                                            newsRegistUpdateModel.getNewsTime()
                                                           ).getTime()));
        request.setTitle(newsRegistUpdateModel.getTitlePC());
        request.setNewsBody(newsRegistUpdateModel.getNewsBodyPC());
        request.setNewsNote(newsRegistUpdateModel.getNewsNotePC());
        request.setNewsUrl(newsRegistUpdateModel.getNewsUrlPC());
        request.setNewsOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class,
                                                                newsRegistUpdateModel.getNewsOpenStatusPC()
                                                               ).getValue());
        request.setNewsOpenStartTime(conversionUtility.toDate(
                        this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsOpenStartDatePC(),
                                               newsRegistUpdateModel.getNewsOpenStartTimePC()
                                              )));
        request.setNewsOpenEndTime(conversionUtility.toDate(
                        this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsOpenEndDatePC(),
                                               newsRegistUpdateModel.getNewsOpenEndTimePC()
                                              )));

        return request;
    }

    /**
     * 画面入力値をEntityに詰め替え
     *
     * @param newsRegistUpdateModel ページ
     * @return ニュース情報
     */
    public NewsEntity toNewsEntityForNewsModel(NewsRegistUpdateModel newsRegistUpdateModel) {

        NewsEntity newsEntity = newsRegistUpdateModel.getNewsEntity();
        newsEntity.setNewsTime(this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsDate(),
                                                      newsRegistUpdateModel.getNewsTime()
                                                     ));

        // PC
        newsEntity.setTitlePC(newsRegistUpdateModel.getTitlePC());
        newsEntity.setNewsBodyPC(newsRegistUpdateModel.getNewsBodyPC());
        newsEntity.setNewsNotePC(newsRegistUpdateModel.getNewsNotePC());
        newsEntity.setNewsUrlPC(newsRegistUpdateModel.getNewsUrlPC());
        newsEntity.setNewsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class,
                                                                     newsRegistUpdateModel.getNewsOpenStatusPC()
                                                                    ));
        newsEntity.setNewsOpenStartTimePC(this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsOpenStartDatePC(),
                                                                 newsRegistUpdateModel.getNewsOpenStartTimePC()
                                                                ));
        newsEntity.setNewsOpenEndTimePC(this.ymdhmsToTimestamp(newsRegistUpdateModel.getNewsOpenEndDatePC(),
                                                               newsRegistUpdateModel.getNewsOpenEndTimePC()
                                                              ));

        return newsEntity;
    }

    /**
     * 年月日・時分秒→タイムスタンプ
     *
     * @param ymd 日付項目値
     * @param hms 時刻項目値
     * @return 引数から取得されるタイムスタンプ
     */
    protected Timestamp ymdhmsToTimestamp(String ymd, String hms) {

        Timestamp ret = null;
        if (ymd != null && hms != null) {

            ret = conversionUtility.toTimeStamp(ymd, hms);
        }
        return ret;

    }

}