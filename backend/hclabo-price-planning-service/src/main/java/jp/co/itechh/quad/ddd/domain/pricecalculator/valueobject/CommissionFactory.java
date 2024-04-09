/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.ddd.domain.payment.adapter.IPaymentMethodAdapter;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 手数料値オブジェクト ファクトリ
 */
@Service
public class CommissionFactory {

    /** 決済方法アダプター */
    private final IPaymentMethodAdapter paymentMethodAdapter;

    /**
     * コンストラクタ
     *
     * @param paymentMethodAdapter 決済方法アダプター
     */
    @Autowired
    public CommissionFactory(IPaymentMethodAdapter paymentMethodAdapter) {
        this.paymentMethodAdapter = paymentMethodAdapter;
    }

    /**
     * 手数料 算出コンストラクト
     *
     * @param paymentMethodId 決済方法ID（決済方法SEQ）
     * @param itemSalesPriceTotal 商品販売金額合計
     * @param carriage 送料
     * @param adjustmentAmountList 調整金額リスト
     * @param couponPaymentPrice クーポン支払い額
     * @return 手数料
     */
    public Commission constructCommission(String paymentMethodId,
                                          ItemSalesPriceTotal itemSalesPriceTotal,
                                          Carriage carriage,
                                          List<AdjustmentAmount> adjustmentAmountList,
                                          CouponPaymentPrice couponPaymentPrice) {

        // チェック
        AssertChecker.assertNotEmpty("paymentMethodId is empty", paymentMethodId);
        AssertChecker.assertNotNull("itemSalesPriceTotal is null", itemSalesPriceTotal);
        AssertChecker.assertNotNull("carriage is null", carriage);
        AssertChecker.assertNotNull("couponPaymentPrice is null", couponPaymentPrice);

        // 計算対象金額
        //   一律手数料高額割引：商品金額合計（税抜）
        //   金額別手数料     ：商品金額合計（税抜）＋ 送料（税抜）＋ 調整金額（税抜）＋ 消費税 － クーポン支払い額

        // 商品金額合計（税抜）
        int itemPriceTotal = itemSalesPriceTotal.getItemPriceTotal();

        // 商品金額合計・送料・調整金額に対する消費税
        // メモ：本当の消費税額ではない、決済手数料が含まれていないため
        //  なので、調整金額マイナスで入れ過ぎて0円割り込んだりすると、計算結果が1円ずれることがある
        SalesPriceConsumptionTax tax =
                        new SalesPriceConsumptionTax(itemSalesPriceTotal, carriage, null, adjustmentAmountList);

        // 金額別手数料 計算対象金額
        int priceForPriceCommission =
                        itemPriceTotal + carriage.getValue() + getAdjustmentAmountTotal(adjustmentAmountList)
                        + tax.getSalesPriceConsumptionTax() - couponPaymentPrice.getValue();

        // 手数料取得
        Integer commission =
                        paymentMethodAdapter.getCommission(paymentMethodId, itemPriceTotal, priceForPriceCommission);
        if (commission == null) {
            // メモ：ここに来るのは、決済方法が取得できないか、金額別手数料の該当区間がない時
            // 画面にハイフン表示するため、0ではなくnullリターンとなっている
            return null;
        }

        return new Commission(commission);
    }

    /**
     * 調整金額合計を取得
     * TODO 商品販売金額合計のように値オブジェクト化したいが、現状諦めている
     *  INTの最大値Overチェックとかも入れておいた方が良い
     *
     * @param adjustmentAmountList 調整金額リスト
     * @return 調整金額合計
     */
    private int getAdjustmentAmountTotal(List<AdjustmentAmount> adjustmentAmountList) {

        if (CollectionUtils.isEmpty(adjustmentAmountList)) {
            return 0;
        }

        return adjustmentAmountList.stream().mapToInt(AdjustmentAmount::getAdjustPrice).sum();
    }

}
