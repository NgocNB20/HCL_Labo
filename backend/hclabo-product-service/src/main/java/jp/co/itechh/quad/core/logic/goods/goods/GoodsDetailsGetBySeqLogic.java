package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;

/**
 *
 * 商品詳細情報取得クラス(商品SEQ)
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public interface GoodsDetailsGetBySeqLogic {

    /**
     *
     * 商品詳細情報取得
     * 商品詳細情報取得
     *
     * @param goodsSeq 商品SEQ
     * @return 商品詳細Dtoクラス
     */
    GoodsDetailsDto execute(Integer goodsSeq);
}
