/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsInformationIconDao;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsInformationIconEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsInformationIconGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 商品アイコン表示情報リストを取得する。
 *
 * @author ozaki
 * @version $Revision: 1.4 $
 *
 */
@Component
public class GoodsInformationIconGetLogicImpl extends AbstractShopLogic implements GoodsInformationIconGetLogic {

    /** 商品インフォメーションDAO */
    private final GoodsInformationIconDao goodsInformationIconDao;

    /**
     * 変換ユーティリティクラス
     */
    @Autowired
    public GoodsInformationIconGetLogicImpl(GoodsInformationIconDao goodsInformationIconDao) {
        this.goodsInformationIconDao = goodsInformationIconDao;
    }

    /**
     * 商品インフォメーションアイコン表示情報リスト取得
     * 商品インフォメーションアイコン表示情報エンティティ情報のリストを取得する
     *
     * @param informationIconSeqList 対象商品インフォメーションアイコンリスト
     * @return 表示情報
     */
    @Override
    public List<GoodsInformationIconDetailsDto> execute(List<Integer> informationIconSeqList) {

        // (1) パラメータチェック
        // infomationIconSeqListがnull又はサイズがゼロでないかをチェック
        ArgumentCheckUtil.assertNotEmpty("informationIconSeqList", informationIconSeqList);

        // (2) 商品グループ情報取得
        // （パラメータ）ショップSEQ、（パラメータ）商品グループコードをもとに、商品グループエンティティを取得する
        // 商品グループDaoの商品グループ取得処理を実行する。
        List<GoodsInformationIconDetailsDto> goodsInformationIconDtoList =
                        goodsInformationIconDao.getInformationIconList(informationIconSeqList);
        if (goodsInformationIconDtoList == null) {
            goodsInformationIconDtoList = new ArrayList<>();
        }

        return goodsInformationIconDtoList;
    }

    /**
     * アイコンSEQのリストからエンティティのリストを取得
     *
     * @param iconSeqList アイコンSEQのリスト
     * @return エンティティのリスト
     */
    @Override
    public List<GoodsInformationIconEntity> getEntityListBySeqList(List<Integer> iconSeqList) {
        return goodsInformationIconDao.getEntityListBySeqList(iconSeqList);
    }

    /**
     * アイコン名のリストからエンティティのリストを取得
     *
     * @param iconNameList アイコン名のリスト
     * @return エンティティのリスト
     */
    @Override
    public List<GoodsInformationIconEntity> getEntityListByNameList(List<String> iconNameList) {
        return goodsInformationIconDao.getEntityListByNameList(iconNameList);
    }
}