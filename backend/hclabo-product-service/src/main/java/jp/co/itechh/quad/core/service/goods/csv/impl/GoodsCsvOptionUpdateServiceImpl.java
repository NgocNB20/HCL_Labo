/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.service.goods.csv.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;
import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.goods.csv.CsvOptionUpdateLogic;
import jp.co.itechh.quad.core.service.goods.csv.GoodsCsvOptionUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品 CSVDL オプション 更新クラス
 */
@Service
public class GoodsCsvOptionUpdateServiceImpl implements GoodsCsvOptionUpdateService {

    /** 商品のCSVDLオプションの更新クラス */
    private final CsvOptionUpdateLogic csvOptionUpdateLogic;

    @Autowired
    public GoodsCsvOptionUpdateServiceImpl(CsvOptionUpdateLogic csvOptionUpdateLogic) {
        this.csvOptionUpdateLogic = csvOptionUpdateLogic;
    }

    /**
     * 商品の CSVDL オプション テンプレートを更新する
     *
     * @param csvDownloadOptionDto 商品CSVDLオプションDTOクラス
     * @return 処理件数
     * @throws JsonProcessingException JsonProcessingException
     */
    @Override
    public int execute(ProductCsvDownloadOptionDto csvDownloadOptionDto) throws JsonProcessingException {

        CsvOptionEntity csvOptionEntity = new CsvOptionEntity();
        csvOptionEntity.setOptionId(csvDownloadOptionDto.getOptionId());
        csvOptionEntity.setOptionInfo(this.convertToJsonEntity(csvDownloadOptionDto));

        int updateCount = csvOptionUpdateLogic.execute(csvOptionEntity);

        return updateCount;
    }

    /**
     * 商品 CSVDL オプション エンティティに変換
     *
     * @param csvDownloadOptionDto 商品CSVDLオプションDTO
     * @return JSON 文字列
     * @throws JsonProcessingException JsonProcessingException
     */
    private String convertToJsonEntity(ProductCsvDownloadOptionDto csvDownloadOptionDto)
                    throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(csvDownloadOptionDto);
    }
}