/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;

/**
 * ID で商品の CSVDL オプションを取得する
 *
 */
public interface GoodsCsvOptionGetByIdService {

    /**
     * 商品別 CSVDL オプションで DTO を取得
     *
     * @param optionId オプションID
     * @return 商品CSVDLオプションDTO
     * @throws JsonProcessingException JsonProcessingException
     */
    ProductCsvDownloadOptionDto execute(Integer optionId) throws JsonProcessingException;
}
