package jp.co.itechh.quad.ddd.infrastructure.transaction.dto;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 顧客注文履歴 ベース情報DTO
 */
@Entity
@Data
@Component
@Scope("prototype")
public class CustomerOrderHistoryBaseInfoDto implements Serializable {

    /** 取引ステータス */
    private String transactionStatus;

    /** 入金済みフラグ */
    private boolean paidFlag;

    /** 出荷済みフラグ */
    private boolean shippedFlag;

    /** 請求決済エラーフラグ */
    private boolean billPaymentErrorFlag;

    /** 受注日時 */
    private Date orderReceivedDate;

    /** 受注番号 */
    private String orderCode;

    /** 取引ID */
    private String transactionId;
}
