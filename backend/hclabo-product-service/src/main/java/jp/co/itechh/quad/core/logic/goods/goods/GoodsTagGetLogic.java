package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goodstag.GoodsTagDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsTagEntity;

import java.util.List;

/**
 * 商品タグ取得
 *
 @author PHAM QUANG DIEU (VJP)
 */
public interface GoodsTagGetLogic {
    /**
     *
     * 商品タグリスト取得
     *
     * @param dto タグ商品Dtoクラス
     * @return タグ商品リスト
     */
    List<GoodsTagEntity> execute(GoodsTagDto dto);
}
