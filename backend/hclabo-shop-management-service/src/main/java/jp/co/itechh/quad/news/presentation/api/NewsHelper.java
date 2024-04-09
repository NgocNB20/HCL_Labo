/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.news.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.dto.shop.NewsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.news.presentation.api.param.NewsListGetRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsRegistRequest;
import jp.co.itechh.quad.news.presentation.api.param.NewsResponse;
import jp.co.itechh.quad.news.presentation.api.param.NewsUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ニュースエンドポイント Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class NewsHelper {

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /**
     * コンストラクター
     *
     * @param dateUtility
     */
    public NewsHelper(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    /**
     * レスポンスに変換
     *
     * @param newsEntity ニュースエンティティ
     * @return ニュースレスポンス
     */
    public NewsResponse toNewsResponse(NewsEntity newsEntity) {

        NewsResponse newsResponse = new NewsResponse();

        newsResponse.setTitle(newsEntity.getTitlePC());
        newsResponse.setNewsBody(newsEntity.getNewsBodyPC());
        newsResponse.setNewsUrl(newsEntity.getNewsUrlPC());
        newsResponse.setNewsOpenStatus(EnumTypeUtil.getValue(newsEntity.getNewsOpenStatusPC()));
        newsResponse.setNewsOpenStartTime(newsEntity.getNewsOpenStartTimePC());
        newsResponse.setNewsOpenEndTime(newsEntity.getNewsOpenEndTimePC());
        newsResponse.setNewsTime(newsEntity.getNewsTime());
        newsResponse.setNewsNote(newsEntity.getNewsNotePC());
        newsResponse.setNewsSeq(newsEntity.getNewsSeq());
        newsResponse.setRegistTime(newsEntity.getRegistTime());
        newsResponse.setUpdateTime(newsEntity.getUpdateTime());

        return newsResponse;
    }

    /**
     *
     * 検索条件Dtoを作成する
     *
     * @param newsListGetRequest 検索画面のリクエスト情報
     * @return ニュース検索条件Dto
     */
    public NewsSearchForBackDaoConditionDto toNewsSearchForBackDaoConditionDtoForSearch(NewsListGetRequest newsListGetRequest) {

        NewsSearchForBackDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(NewsSearchForBackDaoConditionDto.class);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // ニュース日時From
        conditionDto.setNewsTimeFrom(dateUtility.convertDateToTimestamp(newsListGetRequest.getNewsTimeFrom()));

        // ニュース日時To
        conditionDto.setNewsTimeTo(dateUtility.getEndOfDate(
                        dateUtility.convertDateToTimestamp(newsListGetRequest.getNewsTimeTo())));

        // 公開状態
        conditionDto.setOpenStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, newsListGetRequest.getOpenStatus()));

        // 公開開始日時From
        conditionDto.setOpenStartTimeFrom(
                        dateUtility.convertDateToTimestamp(newsListGetRequest.getOpenStartTimeFrom()));

        // 公開開始日時To
        conditionDto.setOpenStartTimeTo(dateUtility.getEndOfDate(
                        dateUtility.convertDateToTimestamp(newsListGetRequest.getOpenStartTimeTo())));

        // 公開終了日時From
        conditionDto.setOpenEndTimeFrom(dateUtility.convertDateToTimestamp(newsListGetRequest.getOpenEndTimeFrom()));

        // 公開終了日時To
        conditionDto.setOpenEndTimeTo(dateUtility.getEndOfDate(
                        dateUtility.convertDateToTimestamp(newsListGetRequest.getOpenEndTimeTo())));

        // タイトル
        conditionDto.setTitle(newsListGetRequest.getTitle());

        // URL
        conditionDto.setUrl(newsListGetRequest.getUrl());

        // 本文・詳細
        conditionDto.setBodyNote(newsListGetRequest.getBodyNote());

        return conditionDto;
    }

    /**
     * ニュースレスポンス一覧に反映
     *
     * @param newsEntityList 検索結果リスト
     * @return ニュースレスポンス一覧
     */
    public List<NewsResponse> toNewsListResponse(List<NewsEntity> newsEntityList) {

        List<NewsResponse> newsList = new ArrayList<>();

        for (NewsEntity newsEntity : newsEntityList) {

            NewsResponse newsResponse = new NewsResponse();

            newsResponse.setNewsSeq(newsEntity.getNewsSeq());
            newsResponse.setTitle(newsEntity.getTitlePC());
            newsResponse.setNewsBody(newsEntity.getNewsBodyPC());
            newsResponse.setNewsUrl(newsEntity.getNewsUrlPC());
            newsResponse.setNewsOpenStatus(EnumTypeUtil.getValue(newsEntity.getNewsOpenStatusPC()));
            newsResponse.setNewsOpenStartTime(newsEntity.getNewsOpenStartTimePC());
            newsResponse.setNewsOpenEndTime(newsEntity.getNewsOpenEndTimePC());
            newsResponse.setNewsTime(newsEntity.getNewsTime());
            newsResponse.setNewsNote(newsEntity.getNewsNotePC());
            newsResponse.setRegistTime(newsEntity.getRegistTime());
            newsResponse.setUpdateTime(newsEntity.getUpdateTime());

            newsList.add(newsResponse);
        }

        return newsList;
    }

    /**
     * ニュース登録時の処理
     *
     * @param newsRegistRequest ニュース登録リクエスト
     * @return NewsEntity ニュースエンティティ
     */
    public NewsEntity toNewsEntity(NewsRegistRequest newsRegistRequest) {

        NewsEntity newsEntity = ApplicationContextUtility.getBean(NewsEntity.class);
        newsEntity.setNewsTime(dateUtility.convertDateToTimestamp(newsRegistRequest.getNewsTime()));
        newsEntity.setTitlePC(newsRegistRequest.getTitle());
        newsEntity.setNewsBodyPC(newsRegistRequest.getNewsBody());
        newsEntity.setNewsNotePC(newsRegistRequest.getNewsNote());
        newsEntity.setNewsUrlPC(newsRegistRequest.getNewsUrl());
        newsEntity.setNewsOpenStatusPC(
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, newsRegistRequest.getNewsOpenStatus()));
        newsEntity.setNewsOpenStartTimePC(dateUtility.convertDateToTimestamp(newsRegistRequest.getNewsOpenStartTime()));
        newsEntity.setNewsOpenEndTimePC(dateUtility.convertDateToTimestamp(newsRegistRequest.getNewsOpenEndTime()));

        return newsEntity;
    }

    /**
     * ニュース更新時の処理
     *
     * @param newsSeq ニュースSeq
     * @param newsUpdateRequest ニュース更新リクエスト
     * @return NewsEntity ニュースエンティティ
     */
    public NewsEntity toNewsEntity(Integer newsSeq, NewsUpdateRequest newsUpdateRequest) {

        NewsEntity newsEntity = ApplicationContextUtility.getBean(NewsEntity.class);

        newsEntity.setShopSeq(1001);
        newsEntity.setNewsSeq(newsSeq);
        newsEntity.setNewsTime(dateUtility.convertDateToTimestamp(newsUpdateRequest.getNewsTime()));
        newsEntity.setTitlePC(newsUpdateRequest.getTitle());
        newsEntity.setNewsBodyPC(newsUpdateRequest.getNewsBody());
        newsEntity.setNewsNotePC(newsUpdateRequest.getNewsNote());
        newsEntity.setNewsUrlPC(newsUpdateRequest.getNewsUrl());
        newsEntity.setNewsOpenStatusPC(
                        EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, newsUpdateRequest.getNewsOpenStatus()));
        newsEntity.setNewsOpenStartTimePC(dateUtility.convertDateToTimestamp(newsUpdateRequest.getNewsOpenStartTime()));
        newsEntity.setNewsOpenEndTimePC(dateUtility.convertDateToTimestamp(newsUpdateRequest.getNewsOpenEndTime()));

        return newsEntity;
    }

}