package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;

import java.util.List;

/**
 *
 * 商品在庫表示リスト取得
 *
 * @author PHAM QUANG DIEU
 *
 */
public interface GoodsStockDisplayGetLogic {

    /**
     *
     * 商品の公開&販売状態チェック
     *
     * @param goodsSeqList 商品SEQリスト
     *
     * @return 商品在庫表示リスト
     */
    List<GoodsStockDisplayEntity> execute(List<Integer> goodsSeqList);
}
