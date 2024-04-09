/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.service.goods.stock;

import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockSettingEntity;

import java.util.List;

/**
 * 在庫登録更新サービス
 *
 * @author kimura
 */
public interface StockRegistUpdateService {

    /**
     * 在庫登録更新（トランザクション指定）
     *
     * @param stockSettingEntityList 在庫設定エンティティリスト
     * @param stockDtoList           在庫Dtoリスト
     * @return 登録更新を行った商品SEQのリスト
     */
    List<Integer> execute(List<StockSettingEntity> stockSettingEntityList, List<StockDto> stockDtoList);

}
