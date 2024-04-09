/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.csv.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.csv.CsvOptionDao;
import jp.co.itechh.quad.core.entity.goods.csv.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.goods.csv.CsvOptionUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品の CSVDL オプションの更新クラス
 */
@Component
public class CsvOptionUpdateLogicImpl implements CsvOptionUpdateLogic {

    /** 商品CSVDLオプションDao */
    private final CsvOptionDao csvOptionDao;

    @Autowired
    public CsvOptionUpdateLogicImpl(CsvOptionDao csvOptionDao) {
        this.csvOptionDao = csvOptionDao;
    }

    /**
     * 商品CSVDL オプションの更新
     *
     * @param csvOptionEntity 更新対象のエンティティ
     * @return 処理件数
     */
    @Override
    public Integer execute(CsvOptionEntity csvOptionEntity) {
        ArgumentCheckUtil.assertNotNull("csvOptionEntity", csvOptionEntity);

        return csvOptionDao.update(csvOptionEntity);
    }
}
