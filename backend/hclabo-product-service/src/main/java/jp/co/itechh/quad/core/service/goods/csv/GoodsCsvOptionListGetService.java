/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;

import java.util.List;

/**
 * 商品の CSVDL オプション リストを取得するクラス
 *
 */
public interface GoodsCsvOptionListGetService {

    /**
     * 商品のリストを取得 CSVDL オプション DTO
     *
     * @return 商品 CSVDL オプション DTO リスト
     * @throws JsonProcessingException JsonProcessingException
     */
    List<ProductCsvDownloadOptionDto> execute() throws JsonProcessingException;
}
