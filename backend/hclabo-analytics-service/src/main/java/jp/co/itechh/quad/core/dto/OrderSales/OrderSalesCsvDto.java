package jp.co.itechh.quad.core.dto.OrderSales;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 受注・売上集計CSVDto
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class OrderSalesCsvDto {

    /**
     * 日付
     */
    @CsvColumn(order = 10, columnLabel = "日付")
    private String date;

    /**
     * 決済方法
     */
    @CsvColumn(order = 20, columnLabel = "決済方法")
    private String paymentMethodName;

    /**
     * 新規受注_件数
     */
    @CsvColumn(order = 30, columnLabel = "新規受注_件数")
    private Integer newOrderCount;

    /**
     * 新規受注_商品金額合計
     */
    @CsvColumn(order = 40, columnLabel = "新規受注_商品金額合計")
    private Integer newOrderItemSalesPriceTotal;

    /**
     * 新規受注_送料
     */
    @CsvColumn(order = 50, columnLabel = "新規受注_送料")
    private Integer newOrderCarriage;

    /**
     * 新規受注_手数料
     */
    @CsvColumn(order = 60, columnLabel = "新規受注_手数料")
    private Integer newOrderCommission;

    /**
     * 新規受注_その他料金
     */
    @CsvColumn(order = 70, columnLabel = "新規受注_その他料金")
    private Integer newOrderOtherPrice;

    /**
     * 新規受注_消費税
     */
    @CsvColumn(order = 80, columnLabel = "新規受注_消費税")
    private Integer newOrderTax;

    /**
     * 新規受注_クーポン割引額
     */
    @CsvColumn(order = 90, columnLabel = "新規受注_クーポン割引額")
    private Integer newOrderCouponPaymentPrice;

    /**
     * 新規受注_受注金額
     */
    @CsvColumn(order = 100, columnLabel = "新規受注_受注金額")
    private Integer newOrderPrice;

    /**
     * キャンセル・返品_件数
     */
    @CsvColumn(order = 110, columnLabel = "キャンセル・返品_件数")
    private Integer cancelOrderCount;

    /**
     * キャンセル・返品_商品金額合計
     */
    @CsvColumn(order = 120, columnLabel = "キャンセル・返品_商品金額合計")
    private Integer cancelOrderItemSalesPriceTotal;

    /**
     * キャンセル・返品_送料
     */
    @CsvColumn(order = 130, columnLabel = "キャンセル・返品_送料")
    private Integer cancelOrderCarriage;

    /**
     * キャンセル・返品_手数料
     */
    @CsvColumn(order = 140, columnLabel = "キャンセル・返品_手数料")
    private Integer cancelOrderCommission;

    /**
     * キャンセル・返品_その他料金
     */
    @CsvColumn(order = 150, columnLabel = "キャンセル・返品_その他料金")
    private Integer cancelOrderOtherPrice;

    /**
     * キャンセル・返品_消費税
     */
    @CsvColumn(order = 160, columnLabel = "キャンセル・返品_消費税")
    private Integer cancelOrderTax;

    /**
     * キャンセル・返品_クーポン割引額
     */
    @CsvColumn(order = 170, columnLabel = "キャンセル・返品_クーポン割引額")
    private Integer cancelOrderCouponPaymentPrice;

    /**
     * キャンセル・返品_受注金額
     */
    @CsvColumn(order = 180, columnLabel = "キャンセル・返品_受注金額")
    private Integer cancelOrderPrice;

    /**
     * 変更_商品金額合計
     */
    @CsvColumn(order = 190, columnLabel = "変更_商品金額合計")
    private Integer updateOrderItemSalesPriceTotal;

    /**
     * 変更_送料
     */
    @CsvColumn(order = 200, columnLabel = "変更_送料")
    private Integer updateOrderCarriage;

    /**
     * 変更_手数料
     */
    @CsvColumn(order = 210, columnLabel = "変更_手数料")
    private Integer updateOrderCommission;

    /**
     * 変更_その他料金
     */
    @CsvColumn(order = 220, columnLabel = "変更_その他料金")
    private Integer updateOrderOtherPrice;

    /**
     * 変更_消費税
     */
    @CsvColumn(order = 230, columnLabel = "変更_消費税")
    private Integer updateOrderTax;

    /**
     * 変更_クーポン割引額
     */
    @CsvColumn(order = 240, columnLabel = "変更_クーポン割引額")
    private Integer updateOrderCouponPaymentPrice;

    /**
     * 変更_受注金額
     */
    @CsvColumn(order = 250, columnLabel = "変更_受注金額")
    private Integer updateOrderPrice;

    /**
     * 小計_商品金額合計
     */
    @CsvColumn(order = 260, columnLabel = "小計_商品金額合計")
    private Integer subTotalItemSalesPriceTotal;

    /**
     * 小計_送料
     */
    @CsvColumn(order = 270, columnLabel = "小計_送料")
    private Integer subTotalCarriage;

    /**
     * 小計_手数料
     */
    @CsvColumn(order = 280, columnLabel = "小計_手数料")
    private Integer subTotalCommission;

    /**
     * 小計_その他料金
     */
    @CsvColumn(order = 290, columnLabel = "小計_その他料金")
    private Integer subTotalOtherPrice;

    /**
     * 小計_消費税
     */
    @CsvColumn(order = 300, columnLabel = "小計_消費税")
    private Integer subTotalTax;

    /**
     * 小計_クーポン割引額
     */
    @CsvColumn(order = 310, columnLabel = "小計_クーポン割引額")
    private Integer subTotalCouponPaymentPrice;

    /**
     * 小計_受注金額
     */
    @CsvColumn(order = 320, columnLabel = "小計_受注金額")
    private Integer subTotalOrderPrice;
}