package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * 集計用販売データ（集計機能共通用データ）
 */
@Getter
@Setter
@NoArgsConstructor
public class ReportsQueryModel {

    /**
     * Id
     */
    @Id
    private String _id;

    /**
     * 受注番号
     */
    private String orderCode;

    /**
     * 取引ID
     */
    private String transactionId;

    /**
     * 処理日時
     */
    private Date processTime;

    /**
     * 受注デバイス
     */
    private String orderDevice;

    /**
     * 受注ステータス
     */
    private String orderStatus;

    /**
     * 処理ステータス
     */
    private String processStatus;

    /**
     * 会員情報
     */
    private ReportsCustomer customer;

    /**
     * 配送先情報
     */
    private ReportsShipping shipping;

    /**
     * 請求先情報
     */
    private ReportsBilling billing;

    /**
     * 出荷状態
     */
    private ReportsPayment payment;

    /**
     * 割引
     */
    private ReportsDiscount discount;

    /**
     * 金額情報
     */
    private ReportsPrice price;

    /**
     * 注文商品リスト情報
     */
    private List<ReportsProductItem> orderItemList;

}
