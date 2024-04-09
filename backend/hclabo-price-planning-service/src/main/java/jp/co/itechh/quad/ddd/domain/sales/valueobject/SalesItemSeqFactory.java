package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 販売商品連番ファクトリ
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class SalesItemSeqFactory {

    /**
     * 販売商品連番構築
     *
     * @param itemPurchasePriceSubTotalList 商品購入価格小計 値オブジェクトリスト
     */
    public static int constructSalesItemSeq(List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList) {

        int salesItemSeqVal = 0;

        if (CollectionUtils.isEmpty(itemPurchasePriceSubTotalList)) {
            return salesItemSeqVal;
        }

        // 注文商品連番の最大値を設定
        for (ItemPurchasePriceSubTotal itemPurchasePriceSubTotal : itemPurchasePriceSubTotalList) {
            if (salesItemSeqVal < itemPurchasePriceSubTotal.getSalesItemSeq()) {
                salesItemSeqVal = itemPurchasePriceSubTotal.getSalesItemSeq();
            }
        }

        return ++salesItemSeqVal;
    }
}
