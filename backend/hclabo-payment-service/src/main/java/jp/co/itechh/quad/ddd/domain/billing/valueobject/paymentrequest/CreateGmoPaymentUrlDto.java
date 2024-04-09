package jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmo_pg.g_pay.client.output.ErrHolder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * LinkPay取引登録決済出力
 */
@Data
public class CreateGmoPaymentUrlDto {

    /** オーダーID */
    @JsonProperty("OrderID")
    private String orderId;

    /** 決済URL */
    @JsonProperty("LinkUrl")
    private String linkUrl;

    /** 処理実行日時 */
    @JsonProperty("ProcessDate")
    private String processDate;

    /** エラー詳細コード */
    private List<ErrHolder> errList;

    /** ワーニング情報 */
    @JsonProperty("WarnList")
    private List<Map<String, String>> warnList;

}
