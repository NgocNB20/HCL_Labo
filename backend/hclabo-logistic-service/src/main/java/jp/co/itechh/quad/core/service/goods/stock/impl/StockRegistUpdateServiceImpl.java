/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.service.goods.stock.impl;

import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockSettingEntity;
import jp.co.itechh.quad.core.logic.goods.stock.StockRegistAndSupplementLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockSettingRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.stock.StockRegistUpdateService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在庫登録更新サービス
 *
 * @author kimura
 */
@Service
public class StockRegistUpdateServiceImpl extends AbstractShopService implements StockRegistUpdateService {

    /** 在庫設定登録ロジック */
    private final StockSettingRegistLogic stockSettingRegistLogic;

    /** 在庫登録と入庫処理用ロジック */
    private final StockRegistAndSupplementLogic stockRegistAndSupplementLogic;

    /** コンストラクタ */
    @Autowired
    public StockRegistUpdateServiceImpl(StockSettingRegistLogic stockSettingRegistLogic,
                                        StockRegistAndSupplementLogic stockRegistAndSupplementLogic) {
        this.stockSettingRegistLogic = stockSettingRegistLogic;
        this.stockRegistAndSupplementLogic = stockRegistAndSupplementLogic;
    }

    /**
     * 在庫登録更新（トランザクション指定）
     *
     * @param stockSettingEntityList 在庫設定エンティティリスト
     * @param stockDtoList           在庫Dtoリスト
     * @return 登録更新を行った商品SEQのリスト
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public List<Integer> execute(List<StockSettingEntity> stockSettingEntityList, List<StockDto> stockDtoList) {

        List<Integer> goodsSeqResultList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(stockSettingEntityList)) {
            // 在庫設定の登録更新
            goodsSeqResultList.addAll(this.stockSettingRegistLogic.execute(
                            stockSettingEntityList.stream().map(seq -> seq.getGoodsSeq()).collect(Collectors.toList()),
                            stockSettingEntityList
                                                                          ));
        }

        if (CollectionUtils.isNotEmpty(stockDtoList)) {
            // 在庫登録と入庫処理
            goodsSeqResultList.addAll(this.stockRegistAndSupplementLogic.execute(stockDtoList));
        }

        // 重複分を削除して結果SEQリストを返却
        return goodsSeqResultList.stream().distinct().collect(Collectors.toList());
    }

}