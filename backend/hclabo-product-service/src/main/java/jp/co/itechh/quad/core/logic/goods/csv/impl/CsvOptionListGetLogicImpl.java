/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.csv.impl;

import jp.co.itechh.quad.core.dao.goods.csv.CsvOptionDao;
import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.goods.csv.CsvOptionListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品CSVDLオプション取得リストクラス
 *
 */
@Component
public class CsvOptionListGetLogicImpl implements CsvOptionListGetLogic {

    /** 商品CSVDLオプションDao */
    private final CsvOptionDao csvOptionDao;

    @Autowired
    public CsvOptionListGetLogicImpl(CsvOptionDao csvOptionDao) {
        this.csvOptionDao = csvOptionDao;
    }

    /**
     * 商品のCSVDLオプションのリストを取得する
     *
     * @return 商品CSVDLオプション一覧
     */
    @Override
    public List<CsvOptionEntity> execute() {
        return csvOptionDao.getList();
    }
}
