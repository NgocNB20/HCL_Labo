/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;

/**
 * 商品CSVDLオプション 更新クラス
 */
public interface GoodsCsvOptionUpdateService {

    /**
     * 商品の CSVDL オプション テンプレートを更新する
     *
     * @param csvDownloadOptionDto 商品CSVDLオプションDTOクラス
     * @return 処理件数
     * @throws JsonProcessingException JsonProcessingException
     */
    int execute(ProductCsvDownloadOptionDto csvDownloadOptionDto) throws JsonProcessingException;
}
