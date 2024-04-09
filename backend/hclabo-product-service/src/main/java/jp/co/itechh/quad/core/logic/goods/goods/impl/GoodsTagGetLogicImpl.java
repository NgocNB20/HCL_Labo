package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.dao.goods.goodstag.GoodsTagDao;
import jp.co.itechh.quad.core.dto.goods.goodstag.GoodsTagDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsTagEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsTagGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * タグ商品ロジック
 *
 * @author Pham Quang Dieu
 */
@Component
public class GoodsTagGetLogicImpl extends AbstractShopLogic implements GoodsTagGetLogic {
    /** 商品Daoクラス */
    private final GoodsTagDao goodsTagDao;

    @Autowired
    public GoodsTagGetLogicImpl(GoodsTagDao goodsTagDao) {
        this.goodsTagDao = goodsTagDao;
    }

    /**
     *
     * 商品タグリスト取得
     *
     * @param dto タグ商品Dtoクラス
     * @return タグ商品リスト
     */
    @Override
    public List<GoodsTagEntity> execute(GoodsTagDto dto) {
        return goodsTagDao.getGoodsTagList(dto, dto.getPageInfo().getSelectOptions());
    }
}
