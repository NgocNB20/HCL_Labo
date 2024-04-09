/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.tax;

import jp.co.itechh.quad.core.constant.type.HTypeTaxRateType;
import jp.co.itechh.quad.core.entity.shop.tax.TaxEntity;
import jp.co.itechh.quad.core.entity.shop.tax.TaxRateEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * 消費税情報取得Logic
 *
 * @author kumazawa
 *
 */
public interface TaxGetLogic {

    /**
     * 現在施行中の消費税情報（標準税率）を取得する
     *
     * @return 消費税情報エンティティ
     */
    TaxEntity getCurrentStandardTaxEntity();

    /**
     * 現在施行中税率取得
     *
     * @return 現在施行中税率マップ
     */
    Map<HTypeTaxRateType, TaxRateEntity> getEffectiveTaxRateMap();

    /**
     * 消費税SEQから税率を取得
     *
     * @param taxSeq 消費税SEQ
     * @return 税率マップ
     */
    Map<HTypeTaxRateType, TaxRateEntity> getEffectiveTaxRateMapByTaxSeq(Integer taxSeq);

    /**
     * 選択項目リストの作成に利用するデータを返却する
     *
     * @return 消費税情報を格納したMap
     */
    Map<BigDecimal, String> getItemMapList();

}