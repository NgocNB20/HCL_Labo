/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.csv.impl;

import jp.co.itechh.quad.core.dao.goods.csv.CsvOptionDao;
import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.goods.csv.CsvOptionGetByIdLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品CSVDLオプション情報取得クラス(オプションID)
 *
 */
@Component
public class CsvOptionGetByIdLogicImpl implements CsvOptionGetByIdLogic {

    /** 商品CSVDLオプションDao */
    private final CsvOptionDao csvOptionDao;

    @Autowired
    public CsvOptionGetByIdLogicImpl(CsvOptionDao csvOptionDao) {
        this.csvOptionDao = csvOptionDao;
    }

    /**
     * 商品CSVDLオプション情報取得
     *
     * @param optionId オプションID
     * @return 商品CSVDLオプション
     */
    @Override
    public CsvOptionEntity execute(Integer optionId) {
        return csvOptionDao.getByOptionId(optionId);
    }
}
