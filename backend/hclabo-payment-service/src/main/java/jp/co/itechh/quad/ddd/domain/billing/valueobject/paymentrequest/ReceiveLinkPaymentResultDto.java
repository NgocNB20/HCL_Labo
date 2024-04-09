package jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest;

import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import lombok.Data;

/**
 * GMOリンク決済結果受取Dto
 */
@Data
public class ReceiveLinkPaymentResultDto {

    /** 取引ID */
    private String transactionId;

    /** マルペイ請求 */
    private MulPayBillEntity mulPayBillEntity;

    /** 処理なしでHMへ戻る 判定 */
    private boolean isNoProcessBack;
}
