/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.csv.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;
import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.goods.csv.CsvOptionListGetLogic;
import jp.co.itechh.quad.core.service.goods.csv.GoodsCsvOptionListGetService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品の CSVDL オプション リストを取得するクラス
 *
 */
@Service
public class GoodsCsvOptionListGetServiceImpl implements GoodsCsvOptionListGetService {

    /** 商品CSVDLオプション取得リスト */
    private final CsvOptionListGetLogic goodsCsvOptionListGetLogic;

    @Autowired
    public GoodsCsvOptionListGetServiceImpl(CsvOptionListGetLogic goodsCsvOptionListGetLogic) {
        this.goodsCsvOptionListGetLogic = goodsCsvOptionListGetLogic;
    }

    /**
     * 商品のリストを取得 CSVDL オプション DTO
     *
     * @return 商品 CSVDL オプション DTO リスト
     * @throws JsonProcessingException JsonProcessingException
     */
    @Override
    public List<ProductCsvDownloadOptionDto> execute() throws JsonProcessingException {

        List<ProductCsvDownloadOptionDto> dtoList = new ArrayList<>();
        // リスト エンティティを取得する
        List<CsvOptionEntity> csvOptionEntityList = goodsCsvOptionListGetLogic.execute();

        if (CollectionUtils.isNotEmpty(csvOptionEntityList)) {
            // データ転送オブジェクトのリストに変換
            for (CsvOptionEntity csvOptionEntity : csvOptionEntityList) {
                ProductCsvDownloadOptionDto csvDownloadOptionDto = this.readJsonEntity(csvOptionEntity.getOptionInfo());
                dtoList.add(csvDownloadOptionDto);
            }
        }

        return dtoList;
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
