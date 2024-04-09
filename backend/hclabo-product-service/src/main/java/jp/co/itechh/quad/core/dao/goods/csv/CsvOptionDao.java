/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

/**
 * 商品索CSVオプション Dao
 *
 * @author
 */
package jp.co.itechh.quad.core.dao.goods.csv;

import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 商品CSVDLオプションDaoクラス
 *
 */
@Dao
@ConfigAutowireable
public interface CsvOptionDao {

    /**
     * アップデート
     *
     * @param csvOptionEntity 商品CSVDLオプション
     * @return 処理件数
     */
    @Update
    int update(CsvOptionEntity csvOptionEntity);

    /**
     * 商品のCSVDLオプションのリストを取得する
     *
     * @return 商品CSVDLオプション一覧
     */
    @Select
    List<CsvOptionEntity> getList();

    /**
     * ID でリスト エンティティを取得する
     *
     * @param optionId オプションID
     * @return 商品CSVDLオプション
     */
    @Select
    CsvOptionEntity getByOptionId(Integer optionId);
}
