/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.csv;

import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;

/**
 * 商品のCSVDLオプションの更新クラス
 */
public interface CsvOptionUpdateLogic {

    /**
     * 商品 CSVDL オプションの更新
     *
     * @param csvOptionEntity 更新対象のエンティティ
     * @return 処理件数
     */
    Integer execute(CsvOptionEntity csvOptionEntity);
}
