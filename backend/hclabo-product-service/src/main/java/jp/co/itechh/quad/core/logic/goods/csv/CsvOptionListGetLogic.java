/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.csv;

import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;

import java.util.List;

/**
 * 商品CSVDLオプション取得リストクラス
 *
 */
public interface CsvOptionListGetLogic {

    /**
     * 商品のCSVDLオプションのリストを取得する
     *
     * @return 商品CSVDLオプション一覧
     */
    List<CsvOptionEntity> execute();
}
