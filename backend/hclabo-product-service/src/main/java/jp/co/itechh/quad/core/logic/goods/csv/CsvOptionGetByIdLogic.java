/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.csv;

import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;

/**
 * 商品CSVDLオプション情報取得クラス(オプションID)
 *
 */
public interface CsvOptionGetByIdLogic {

    /**
     * 商品CSVDLオプション情報取得
     *
     * @param optionId オプションID
     * @return 商品CSVDLオプション
     */
    CsvOptionEntity execute(Integer optionId);
}
