package jp.co.itechh.quad.ddd.domain.shipping.valueobject;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 配送商品連番ファクトリ
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 *
 */
public class ShippingItemSeqFactory {

    /**
     * 配送商品連番構築
     *
     * @param securedShippingItemList 確保済み配送商品 値オブジェクトリスト
     */
    public static int constructShippingItemSeq(List<SecuredShippingItem> securedShippingItemList) {

        int shippingItemSeqVal = 0;

        if (CollectionUtils.isEmpty(securedShippingItemList)) {
            return shippingItemSeqVal;
        }

        // 注文商品連番の最大値を設定
        for (SecuredShippingItem securedShippingItem : securedShippingItemList) {
            if (shippingItemSeqVal < securedShippingItem.getShippingItemSeq()) {
                shippingItemSeqVal = securedShippingItem.getShippingItemSeq();
            }
        }

        return ++shippingItemSeqVal;
    }
}
