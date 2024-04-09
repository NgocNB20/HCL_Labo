package jp.co.itechh.quad.product.presentation.api;

import jp.co.itechh.quad.core.entity.goods.goods.GoodsTagEntity;
import jp.co.itechh.quad.tag.presentation.api.param.GoodsTagResponse;
import jp.co.itechh.quad.tag.presentation.api.param.ProductTagListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * タグ Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class TagsHelper {

    /**
     * レスポンスに変換
     *
     * @param goodsTagEntities タグ商品リスト
     * @return 商品タグ一覧レスポンス
     */
    public ProductTagListResponse toProductTagListResponse(List<GoodsTagEntity> goodsTagEntities) {
        ProductTagListResponse productTagListResponse = new ProductTagListResponse();
        List<GoodsTagResponse> goodsTagList = new ArrayList<>();

        for (GoodsTagEntity entity : goodsTagEntities) {
            GoodsTagResponse goodsTagResponse = new GoodsTagResponse();
            goodsTagResponse.setGoodsTag(entity.getTag());
            goodsTagList.add(goodsTagResponse);
        }
        productTagListResponse.setGoodsTagList(goodsTagList);

        return productTagListResponse;
    }
}
