package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.dao.goods.goods.GoodsStockDisplayDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockDisplayGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoodsStockDisplayGetLogicImpl extends AbstractShopLogic implements GoodsStockDisplayGetLogic {

    /** 商品Daoクラス */
    private final GoodsStockDisplayDao goodsStockDisplayDao;

    @Autowired
    public GoodsStockDisplayGetLogicImpl(GoodsStockDisplayDao goodsStockDisplayDao) {
        this.goodsStockDisplayDao = goodsStockDisplayDao;
    }

    /**
     * * 商品の公開&販売状態チェック
     *
     * @param goodsSeqList 商品SEQリスト
     *
     * @return GoodsStockDisplayEntity 商品在庫表示
     */
    @Override
    public List<GoodsStockDisplayEntity> execute(List<Integer> goodsSeqList) {
        return goodsStockDisplayDao.getGoodsStockDisplayList(goodsSeqList);
    }
}