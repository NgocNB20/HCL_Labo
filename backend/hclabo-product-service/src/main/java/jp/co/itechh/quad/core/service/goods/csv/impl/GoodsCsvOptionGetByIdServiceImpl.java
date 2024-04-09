/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.csv.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;
import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.goods.csv.CsvOptionGetByIdLogic;
import jp.co.itechh.quad.core.service.goods.csv.GoodsCsvOptionGetByIdService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IDで商品のCSVDLオプションを取得する
 *
 */
@Service
public class GoodsCsvOptionGetByIdServiceImpl implements GoodsCsvOptionGetByIdService {

    /** 商品CSVDLオプション情報取得クラス */
    private final CsvOptionGetByIdLogic csvOptionGetByIdLogic;

    @Autowired
    public GoodsCsvOptionGetByIdServiceImpl(CsvOptionGetByIdLogic csvOptionGetByIdLogic) {
        this.csvOptionGetByIdLogic = csvOptionGetByIdLogic;
    }

    /**
     * IDで商品のCSVDLオプションを取得する
     *
     * @param optionId オプションID
     * @return 商品CSVDLオプションDTO
     * @throws JsonProcessingException JsonProcessingException
     */
    @Override
    public ProductCsvDownloadOptionDto execute(Integer optionId) throws JsonProcessingException {

        ProductCsvDownloadOptionDto csvDownloadOptionDto = new ProductCsvDownloadOptionDto();
        // ID でオプションを取得する
        CsvOptionEntity csvOptionEntity = csvOptionGetByIdLogic.execute(optionId);

        if (ObjectUtils.isNotEmpty(csvOptionEntity)) {
            // データ転送オブジェクトに変換
            csvDownloadOptionDto = this.readJsonEntity(csvOptionEntity.getOptionInfo());
        }

        return csvDownloadOptionDto;
    }

    /**
     * JSONをオプション情報に変換
     *
     * @param jsonEntity
     * @return 商品CSVDLオプションDTO
     * @throws JsonProcessingException JsonProcessingException
     */
    private ProductCsvDownloadOptionDto readJsonEntity(String jsonEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonEntity, ProductCsvDownloadOptionDto.class);
    }
}
