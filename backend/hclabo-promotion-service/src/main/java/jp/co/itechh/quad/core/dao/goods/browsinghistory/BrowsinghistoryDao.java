/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.browsinghistory;

import jp.co.itechh.quad.core.dto.goods.browsinghistory.BrowsinghistorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.browsinghistory.BrowsinghistoryEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * あしあと商品Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface BrowsinghistoryDao {

    /**
     * インサート
     *
     * @param browsinghistoryEntity あしあとエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(BrowsinghistoryEntity browsinghistoryEntity);

    /**
     * アップデート
     *
     * @param browsinghistoryEntity あしあとエンティティ
     * @return 処理件数
     */
    @Update
    int update(BrowsinghistoryEntity browsinghistoryEntity);

    /**
     * デリート
     *
     * @param browsinghistoryEntity あしあとエンティティ
     * @return 処理件数
     */
    @Delete
    int delete(BrowsinghistoryEntity browsinghistoryEntity);

    /**
     * エンティティ取得
     *
     * @param accessUid 端末識別情報
     * @param goodsGroupSeq 商品グループSEQ
     * @return あしあとエンティティ
     */
    @Select
    BrowsinghistoryEntity getEntity(String accessUid, Integer goodsGroupSeq);

    /**
     * あしあと商品リスト取得
     *
     * @param conditionDto あしあと検索条件DTO
     * @return あしあとエンティティリスト
     */
    @Select
    List<BrowsinghistoryEntity> getSearchBrowsinghistoryList(BrowsinghistorySearchForDaoConditionDto conditionDto,
                                                             SelectOptions selectOptions);

    /**
     * あしあと削除
     *
     * @param shopSeq ショップSEQ
     * @param accessUid 端末識別情報
     * @return 削除件数
     */
    @Delete(sqlFile = true)
    int deleteBrowsinghistory(Integer shopSeq, String accessUid);
}