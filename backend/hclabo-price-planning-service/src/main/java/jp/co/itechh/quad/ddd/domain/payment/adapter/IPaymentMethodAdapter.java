/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

/**
 * 決済方法 アダプター
 */
public interface IPaymentMethodAdapter {

    /**
     * 手数料取得
     * <pre>
     *   引数パラメータを元に、手数料を取得するAPIを呼び出し
     * </pre>
     *
     * @param paymentMethodId             決済方法ID（決済方法SEQ）
     * @param priceForLargeAmountDiscount 計算対象金額（一律手数料高額割引）：商品金額合計（税抜）
     * @param priceForPriceCommission     計算対象金額（金額別手数料）：商品金額合計（税抜）＋送料（税抜）＋調整金額（税抜）＋消費税－クーポン支払い額
     * @return 手数料
     */
    Integer getCommission(String paymentMethodId, Integer priceForLargeAmountDiscount, Integer priceForPriceCommission);

}
