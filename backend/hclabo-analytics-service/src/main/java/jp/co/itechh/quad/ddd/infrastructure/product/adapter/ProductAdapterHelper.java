package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDetails;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDisplays;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductIcons;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品アダプター実装Helperクラス
 */
@Component
public class ProductAdapterHelper {

    /**
     * 商品取得リスト
     *
     * @param responseList 商品詳細リストレスポンスクラス
     * @return 商品取得リスト
     */
    public List<ProductDetails> toProductDetailsList(List<GoodsDetailsResponse> responseList) {

        if (CollectionUtils.isEmpty(responseList)) {
            return null;
        }

        List<ProductDetails> productDetailsList = new ArrayList<>();

        responseList.forEach(item -> {
            ProductDetails productDetails = new ProductDetails();

            productDetails.setGoodsGroupSeq(item.getGoodsGroupSeq());
            productDetails.setGoodsSeq(item.getGoodsSeq());
            productDetails.setGoodsGroupCode(item.getGoodsGroupCode());
            productDetails.setGoodsCode(item.getGoodsCode());
            productDetails.setJanCode(item.getJanCode());
            productDetails.setTaxRate(item.getTaxRate());
            productDetails.setSaleStartTime(item.getSaleStartTime());
            productDetails.setOrderSetting1(item.getOrderSetting1());
            productDetails.setOrderSetting2(item.getOrderSetting2());
            productDetails.setOrderSetting3(item.getOrderSetting3());
            productDetails.setOrderSetting4(item.getOrderSetting4());
            productDetails.setOrderSetting5(item.getOrderSetting5());
            productDetails.setOrderSetting6(item.getOrderSetting6());
            productDetails.setOrderSetting7(item.getOrderSetting7());
            productDetails.setOrderSetting8(item.getOrderSetting8());
            productDetails.setOrderSetting9(item.getOrderSetting9());
            productDetails.setOrderSetting10(item.getOrderSetting10());
            productDetails.setNoveltyGoodsType(item.getNoveltyGoodsType());

            productDetailsList.add(productDetails);
        });

        return productDetailsList;
    }

    /**
     * 商品表示に変換
     *
     * @param response 画面表示用商品グループレスポンス
     * @return 商品表示
     */
    public ProductDisplays toProductDisplay(ProductDisplayResponse response) {
        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        ProductDisplays productDisplays = new ProductDisplays();

        productDisplays.setGoodsGroupCode(response.getGoodsGroup().getGoodsGroupCode());
        productDisplays.setIconList(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(response.getGoodsInformationIconDetailsResponseList())) {
            response.getGoodsInformationIconDetailsResponseList().forEach(iconItem -> {
                ProductIcons icon = new ProductIcons();
                icon.setIconId(iconItem.getIconSeq());
                icon.setIconName(iconItem.getIconName());

                productDisplays.getIconList().add(icon);
            });
        }
        productDisplays.setCategorySeqList(new ArrayList<>());

        if (CollectionUtils.isNotEmpty(response.getCategoryGoodsResponseList())) {
            response.getCategoryGoodsResponseList().forEach(categoryItem -> {

                productDisplays.getCategorySeqList().add(categoryItem.getCategorySeq());
            });
        }

        if (ObjectUtils.isNotEmpty(response.getGoodsGroupDisplay())) {
            productDisplays.setGoodsTagList(response.getGoodsGroupDisplay().getGoodsTag());
        }

        return productDisplays;
    }

}
