/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsDetailsGetByCodeLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 商品詳細情報取得クラス(商品コード)
 * 商品詳細情報取得クラス(商品コード)
 *
 * @author ozaki
 * @version $Revision: 1.5 $
 *
 */
@Component
public class GoodsDetailsGetByCodeLogicImpl extends AbstractShopLogic implements GoodsDetailsGetByCodeLogic {

    /**
     * 商品DAO
     */
    private final GoodsDao goodsDao;

    /** 商品グループ画像取得Logic */
    private final GoodsGroupImageGetLogic goodsGroupImageGetLogic;

    @Autowired
    public GoodsDetailsGetByCodeLogicImpl(GoodsDao goodsDao, GoodsGroupImageGetLogic goodsGroupImageGetLogic) {
        this.goodsDao = goodsDao;
        this.goodsGroupImageGetLogic = goodsGroupImageGetLogic;
    }

    /**
     *
     * 商品詳細情報取得
     *
     * @param shopSeq ショップSEQ
     * @param code 商品コード
     * @param siteType サイト区分
     * @param goodsOpenStatus 公開状態
     * @return 商品詳細DTO
     */
    @Override
    public GoodsDetailsDto execute(Integer shopSeq,
                                   String code,
                                   HTypeSiteType siteType,
                                   HTypeOpenDeleteStatus goodsOpenStatus) {
        // (1) パラメータチェック
        // ショップSEQが null でないかをチェック
        // 商品コードが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotEmpty("code", code);

        // (2) 商品詳細情報取得
        // 商品情報Daoの商品詳細Dto取得処理を実行する。
        // DAO GoodsDao
        // メソッド 商品詳細Dto getGoodsDetailsByShopSeqAndCode( ショップSEQ, 商品コード)
        GoodsDetailsDto goodsDetailsDto =
                        goodsDao.getGoodsDetailsByShopSeqAndCode(shopSeq, code, siteType, goodsOpenStatus);
        if (goodsDetailsDto == null) {
            return null;
        }

        // (3) 商品グループ画像情報取得
        // (2) で取得した商品詳細DTOから商品詳細DTO．商品グループSEQリスト（1件）を作成する。
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        goodsGroupSeqList.add(goodsDetailsDto.getGoodsGroupSeq());

        // 商品グループ画像取得Logicを利用して、商品画像マップを取得する
        // Logic GoodsGroupImageGetLogic
        // パラメータ 商品グループSEQリスト
        // 戻り値 商品グループ画像マップ
        Map<Integer, List<GoodsGroupImageEntity>> goodsGroupImageMap =
                        goodsGroupImageGetLogic.execute(goodsGroupSeqList);

        // (4) 商品DTOの編集
        // （(2)で取得した）商品詳細DTO．商品グループSEQをキー項目として(3)で取得した商品グループ画像マップから商品グループ画像エンティティリストを取得し、
        // （(2)で取得した）商品詳細DTOにセットする。
        // ※取得できない場合はエラーログを出力する
        goodsDetailsDto.setGoodsGroupImageEntityList(goodsGroupImageMap.get(goodsDetailsDto.getGoodsGroupSeq()));

        // (3) 戻り値
        // 取得した商品詳細Dtoを返す。
        return goodsDetailsDto;
    }
}