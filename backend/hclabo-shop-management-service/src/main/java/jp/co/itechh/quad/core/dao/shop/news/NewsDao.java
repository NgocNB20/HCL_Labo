/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.news;

import jp.co.itechh.quad.core.dto.shop.NewsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.NewsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * ニュースDaoクラス<br/>
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface NewsDao {

    /**
     * インサート<br/>
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(NewsEntity newsEntity);

    /**
     * アップデート
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    @Update
    int update(NewsEntity newsEntity);

    /**
     * デリート
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    @Delete
    int delete(NewsEntity newsEntity);

    /**
     * エンティティ取得
     * @param shopSeq ショップSEQ
     * @param newsSeq ニュースSEQ
     * @return ニュースエンティティ
     */
    @Select
    NewsEntity getEntityByShopSeq(Integer shopSeq, Integer newsSeq);

    /**
     * エンティティリスト取得<br/>
     *
     * @param conditionDto 検索条件DTO
     * @param selectOptions 検索オプション
     * @return ニュースエンティティリスト
     */
    @Select
    List<NewsEntity> getSearchNewsList(NewsSearchForDaoConditionDto conditionDto, SelectOptions selectOptions);

    /**
     * エンティティリスト取得
     *
     * @param conditionDto 管理者画面用検索条件Dto
     * @param selectOptions 検索オプション
     * @return ニュースエンティティリスト
     */
    @Select
    List<NewsEntity> getSearchNewsForBackList(NewsSearchForBackDaoConditionDto conditionDto,
                                              SelectOptions selectOptions);
}