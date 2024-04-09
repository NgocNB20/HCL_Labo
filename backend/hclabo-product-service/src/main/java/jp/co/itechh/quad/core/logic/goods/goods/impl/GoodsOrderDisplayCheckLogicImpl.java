/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsOrderDisplayCheckLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品の規格表示順チェック
 *
 * @author nose
 *
 */
@Component
public class GoodsOrderDisplayCheckLogicImpl extends AbstractShopLogic implements GoodsOrderDisplayCheckLogic {

    /** 商品DAO */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsOrderDisplayCheckLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     * 商品規格表示順チェック
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsGroupCode 商品グループコード
     */
    @Override
    public void execute(Integer goodsGroupSeq, String goodsGroupCode) {
        // (1)パラメータチェック
        if (goodsGroupSeq == null) {
            // 商品グループ新規登録の場合、チェック対象外とする
            return;
        }
        ArgumentCheckUtil.assertNotNull("goodsGroupCode", goodsGroupCode);

        // (2)共通情報チェック
        // ショップSEQ ： null(or 0) の場合 エラーとして処理を終了する
        Integer shopSeq = 1001;

        // (3)商品規格表示順チェック
        List<Integer> dupulicateOrderDisplayList = goodsDao.getDupulicateOrderDisplayList(shopSeq, goodsGroupSeq);
        if (dupulicateOrderDisplayList != null && !dupulicateOrderDisplayList.isEmpty()) {
            for (Integer orderDisplay : dupulicateOrderDisplayList) {
                addErrorMessage(MSGCD_ORDERDISPLAY_DUPLICATE_FAIL, new Object[] {goodsGroupCode, orderDisplay});
            }
            throwMessage();
        }
    }
}