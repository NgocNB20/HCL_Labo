package jp.co.itechh.quad.ddd.infrastructure.billing.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 改訂用請求伝票Dbエンティティ
 */
@Entity
@Table(name = "BillingSlipForRevision")
@Data
@Component
@Scope("prototype")
public class BillingSlipForRevisionDbEntity {

    /**
     * 改訂用請求伝票ID
     */
    @Id
    private String billingSlipRevisionId;

    /**
     * 改訂用取引ID
     */
    private String transactionRevisionId;

    /**
     * 注文決済ID
     */
    private String orderPaymentRevisionId;

    /**
     * 請求伝票ID
     */
    private String billingSlipId;

    /**
     * 請求ステータス
     */
    private String billingStatus;

    /**
     * 請求済金額
     */
    private int billedPrice;

    /**
     * 請求種別
     */
    private String billingType;

    /**
     * 入金日時
     */
    protected Date moneyReceiptTime;

    /**
     * 累計入金額
     */
    protected int moneyReceiptAmountTotal;

    /**
     * 登録日時
     */
    private Date registDate;

    /**
     * 取引ID
     */
    private String transactionId;

    /**
     * 請求先住所ID
     */
    private String billingAddressId;

}