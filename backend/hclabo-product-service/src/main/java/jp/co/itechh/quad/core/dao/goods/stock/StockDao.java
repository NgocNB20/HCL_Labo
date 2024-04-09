/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.stock;

import jp.co.itechh.quad.core.dto.goods.stock.StockDownloadCsvDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockSearchForDaoConditionDto;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.stream.Stream;

/**
 * 在庫情報Dao
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface StockDao {

    /**
     * 在庫検索結果一括CSVダウンロード
     *
     * @param conditionDto 在庫検索条件DTO
     * @return ダウンロード取得件数
     */
    @Select
    Stream<StockDownloadCsvDto> exportCsvByStockSearchForDaoConditionDto(StockSearchForDaoConditionDto conditionDto);
}