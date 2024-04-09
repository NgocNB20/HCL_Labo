/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.shop.NewsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.shop.news.NewsListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.news.OpenNewsListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公開ニュースリスト情報取得Service<br/>
 *
 * @author ozaki
 * @version $Revision: 1.3 $
 *
 */
@Service
public class OpenNewsListGetServiceImpl extends AbstractShopService implements OpenNewsListGetService {

    /** ニュース一覧取得Logic */
    private final NewsListGetLogic newsListGetLogic;

    @Autowired
    public OpenNewsListGetServiceImpl(NewsListGetLogic newsListGetLogic) {
        this.newsListGetLogic = newsListGetLogic;
    }

    /**
     *
     * ニュース詳細情報を取得する<br/>
     *
     * @param conditionDto ニュース情報検索条件DTO
     * @return ニュースエンティティリスト
     */
    @Override
    public List<NewsEntity> execute(NewsSearchForDaoConditionDto conditionDto) {

        // (1) パラメータチェック
        // ・ページ情報DTO ： null 又は 未設定の場合 エラーとして処理を終了する
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);

        // (2) 共通情報チェック
        // ・ショップSEQ ： null(or空文字) の場合 エラーとして処理を終了する
        // ・サイト区分 ： null(or空文字) の場合 エラーとして処理を終了する
        Integer shopSeq = 1001;
        HTypeSiteType siteType = HTypeSiteType.FRONT_PC;

        // (3) ニュース情報リストを取得する
        // ‥ショップSEQ =共通情報.ショップSEQ
        // ‥会員SEQ =共通情報.会員SEQ
        // ‥サイト区分 =共通情報.サイト区分
        conditionDto.setShopSeq(shopSeq);
        conditionDto.setSiteType(siteType);

        // ･ニュース情報リスト取得処理を実行
        // ※『logic基本設計書（ニュース情報リスト取得）.xls』参照
        // Logic NewsListGetLogic
        // パラメータ ・ニュース情報Dao用検索条件Dto
        // (公開状態=公開中)
        // 戻り値 ニュースエンティティリスト
        List<NewsEntity> newsEntityList = newsListGetLogic.execute(conditionDto);

        return newsEntityList;

    }
}
