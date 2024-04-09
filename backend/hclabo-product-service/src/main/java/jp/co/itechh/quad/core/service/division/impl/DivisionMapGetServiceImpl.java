/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.service.division.impl;

import jp.co.itechh.quad.core.logic.shop.tax.TaxGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.division.DivisionMapGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 区分値リスト取得サービス
 *
 * @author kaneda
 */
@Service
public class DivisionMapGetServiceImpl extends AbstractShopService implements DivisionMapGetService {

    private TaxGetLogic taxGetLogic;

    /**
     * コンストラクタ
     *
     * @param taxGetLogic
     */
    @Autowired
    public DivisionMapGetServiceImpl(TaxGetLogic taxGetLogic) {
        this.taxGetLogic = taxGetLogic;
    }

    @Override
    public Map<BigDecimal, String> getTaxRateMapList() {

        return taxGetLogic.getItemMapList();
    }

}