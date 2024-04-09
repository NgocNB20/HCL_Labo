package jp.co.itechh.quad.shop.presentation.api;

import jp.co.itechh.quad.core.constant.type.HTypeAutoApprovalFlag;
import jp.co.itechh.quad.core.entity.shop.ShopEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.shop.presentation.api.param.ShopResponse;
import jp.co.itechh.quad.shop.presentation.api.param.ShopUpdateRequest;
import org.springframework.stereotype.Component;

/**
 * ショップ　Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class ShopHelper {

    /**
     * ショップ情報レスポンスに変換
     *
     * @param shopEntity ショップエンティティ
     * @return ショップ情報レスポンス
     */
    public ShopResponse toShopResponse(ShopEntity shopEntity) {

        ShopResponse shopResponse = new ShopResponse();

        shopResponse.setShopId(shopEntity.getShopId());
        shopResponse.setShopNamePC(shopEntity.getShopNamePC());
        shopResponse.setUrlPC(shopEntity.getUrlPC());
        shopResponse.setMetaDescription(shopEntity.getMetaDescription());
        shopResponse.setMetaKeyword(shopEntity.getMetaKeyword());
        shopResponse.setVersionNo(shopEntity.getVersionNo());
        shopResponse.setAutoApprovalFlag(EnumTypeUtil.getValue(shopEntity.getAutoApprovalFlag()));

        return shopResponse;
    }

    /**
     * ショップエンティティに変換
     *
     * @param shopUpdateRequest   ショップ情報リクエスト
     * @param shopSeq ショップSEQ
     * @return ショップエンティティ
     */
    public ShopEntity toShopEntity(ShopUpdateRequest shopUpdateRequest, Integer shopSeq) {

        ShopEntity shopEntity = new ShopEntity();

        shopEntity.setShopSeq(shopSeq);
        shopEntity.setShopId(shopUpdateRequest.getShopId());
        shopEntity.setShopNamePC(shopUpdateRequest.getShopNamePC());
        shopEntity.setUrlPC(shopUpdateRequest.getUrlPC());
        shopEntity.setMetaDescription(shopUpdateRequest.getMetaDescription());
        shopEntity.setMetaKeyword(shopUpdateRequest.getMetaKeyword());
        shopEntity.setVersionNo(shopUpdateRequest.getVersionNo());
        shopEntity.setAutoApprovalFlag(EnumTypeUtil.getEnumFromValue(HTypeAutoApprovalFlag.class,
                                                                     shopUpdateRequest.getAutoApprovalFlag()
                                                                    ));

        return shopEntity;
    }

}