package jp.co.itechh.quad.ddd.usecase.transaction.query;

import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderStatus;
import lombok.Data;

import java.util.Date;

/**
 * 顧客注文履歴一覧用 QueryModel
 */
@Data
public class CustomerOrderHistoryQueryModel {

    /** 注文状況 */
    private OrderStatus orderStatus;

    /** 注文日時 */
    private Date orderReceivedDate;

    /** 受注番号 */
    private String orderCode;

    /** 支払金額 */
    private Integer paymentPrice;
}
