package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;

/**
 * 商品タグ更新Logic処理
 *
 * @author Pham Quang Dieu
 */
public interface GoodsTagRegistUpdateLogic {

    /**
     * 商品タグ更新Logic処理
     * @param goodsGroupDisplayEntity 商品グループ表示クラス
     * @throws Exception
     */
    void execute(GoodsGroupDisplayEntity goodsGroupDisplayEntity) throws Exception;
}