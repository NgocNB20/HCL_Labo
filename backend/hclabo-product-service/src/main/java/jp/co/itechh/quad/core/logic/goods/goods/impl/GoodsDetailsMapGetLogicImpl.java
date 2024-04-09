/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsDetailsMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageGetLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品詳細情報MAP取得
 * 商品詳細Dtoを保持するマップを取得する。
 *
 * @author ozaki
 *
 */
@Component
public class GoodsDetailsMapGetLogicImpl extends AbstractShopLogic implements GoodsDetailsMapGetLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsDetailsMapGetLogicImpl.class);

    /** 商品情報DAO */
    private GoodsDao goodsDao;

    /** 商品グループ画像取得Logic */
    private GoodsGroupImageGetLogic goodsGroupImageGetLogic;

    /**
     * コンストラクタ
     *
     * @param goodsDao
     * @param goodsGroupImageGetLogic
     */
    @Autowired
    public GoodsDetailsMapGetLogicImpl(GoodsDao goodsDao, GoodsGroupImageGetLogic goodsGroupImageGetLogic) {
        this.goodsDao = goodsDao;
        this.goodsGroupImageGetLogic = goodsGroupImageGetLogic;
    }

    /**
     * 商品詳細Dtoを保持するマップを取得する。
     *
     * @param goodsSeqList 商品シーケンスリスト
     * @return 商品詳細情報MAP
     */
    @Override
    public Map<Integer, GoodsDetailsDto> execute(List<Integer> goodsSeqList) {
        // (1) パラメータチェック
        // 商品SEQリストがnullでないことをチェック
        ArgumentCheckUtil.assertNotNull("goodsSeqList", goodsSeqList);

        List<GoodsDetailsDto> goodsDetailsDtoList = goodsDao.getGoodsDetailsList(goodsSeqList);

        // 編集した商品詳細マップを返す。
        return createGoodsDetailsMap(goodsDetailsDtoList);
    }

    /**
     *
     * 商品詳細情報MAP取得
     * 商品詳細Dtoを保持するマップを取得する。
     *
     * @param goodsDetailsDtoList 商品詳細Dtoリスト
     * @return 商品詳細情報MAP
     */
    protected Map<Integer, GoodsDetailsDto> createGoodsDetailsMap(List<GoodsDetailsDto> goodsDetailsDtoList) {
        // (3) 商品グループ画像情報取得
        // (2) で取得した商品詳細DTOリストから商品詳細DTO．商品グループSEQのリストを作成する。
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        for (GoodsDetailsDto goodsDetailsDto : goodsDetailsDtoList) {
            goodsGroupSeqList.add(goodsDetailsDto.getGoodsGroupSeq());
        }

        // 商品グループ画像取得Logicを利用して、商品画像マップを取得する
        // Logic GoodsGroupImageGetLogic
        // パラメータ 商品グループSEQリスト
        // 戻り値 商品グループ画像マップ
        Map<Integer, List<GoodsGroupImageEntity>> goodsGroupImageMap =
                        goodsGroupImageGetLogic.execute(goodsGroupSeqList);

        // 商品規格画像取得Logicを利用して、商品画像マップを取得する
        // Map<Integer, List<GoodsImageDto>> goodsImageMap =
        // goodsImageGetLogic.getGoodsImageDtoMap(goodsGroupSeqList, true);

        // (3) 商品詳細マップの生成
        // 商品詳細マップを初期生成する。
        // ・（(2)で取得した）商品詳細DTOリストの件数分、以下の処理を繰り返す
        // ①商品詳細DTO．商品グループSEQをキー項目として(3)で取得した商品グループ画像マップから商品グループ画像エンティティリストを取得し、
        // 商品詳細DTOにセットする。
        // ※取得できない場合はエラーログを出力する
        // ②キー項目：商品詳細DTO. 商品SEQ、 設定値：商品詳細DTO で商品詳細マップに追加する。
        Map<Integer, GoodsDetailsDto> goodsDetailsMap = new HashMap<>();
        for (GoodsDetailsDto goodsDetailsDto : goodsDetailsDtoList) {

            List<GoodsGroupImageEntity> goodsImageEntityList =
                            goodsGroupImageMap.get(goodsDetailsDto.getGoodsGroupSeq());
            goodsDetailsDto.setGoodsGroupImageEntityList(goodsImageEntityList);

            if (goodsImageEntityList == null) {
                // log
                LOGGER.debug("「商品SEQ：" + goodsDetailsDto.getGoodsGroupSeq() + "の商品画像が存在しません。");
            }
            // List<GoodsImageDto> goodsImageDtoList =
            // goodsImageMap.get(goodsDetailsDto.getGoodsGroupSeq());
            // goodsDetailsDto.setGoodsImageDtoList(goodsImageDtoList);

            // if (goodsImageDtoList == null) {
            // // log
            // LOGGER.debug("「商品SEQ：" + goodsDetailsDto.getGoodsGroupSeq() +
            // "の商品画像が存在しません。");
            // }
            goodsDetailsMap.put(goodsDetailsDto.getGoodsSeq(), goodsDetailsDto);
        }

        // (4) 戻り値
        // 編集した商品詳細マップを返す。
        return goodsDetailsMap;
    }

}