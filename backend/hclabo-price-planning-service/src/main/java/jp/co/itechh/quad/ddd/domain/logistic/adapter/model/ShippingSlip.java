package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 配送伝票
 */
@Data
public class ShippingSlip {

    /** 配送伝票ID */
    private String shippingSlipId;

    /** 配送ステータス */
    private String shippingStatus;

    /** 配送追跡番号 */
    private String trackingNumber;

    /** 配送方法ID */
    private String shippingMethodId;

    /** 配送方法名 */
    private String shippingMethodName;

    /** 配送商品 */
    private List<ShippingItem> shippingItemList;

    /** 配送先住所ID */
    private String shippingAddressId;

    /** 取引ID */
    private String transactionId;

    /** 納品書要否フラグ */
    private boolean invoiceNecessaryFlag;

    /** お届け希望日 */
    private Date receiverDate;

    /** お届け希望時間帯 */
    private String receiverTimeZone;

    /** 出荷完了日時 */
    private Date completeShipmentDate;

    /** 登録日時 */
    private Date registDate;

}
