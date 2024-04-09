package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 配送伝票
 */
@Data
public class ShippingSlip {

    /**
     * 配送先住所ID
     */
    private String shippingAddressId;

    /**
     * 配送方法ID
     */
    private Integer shippingMethodId;

    /**
     * 配送方法名
     */
    private String shippingMethodName;

    /**
     * 納品書要否フラグ
     */
    private boolean invoiceNecessaryFlag;

    /**
     * お届け希望日
     */
    private Date receiverDate;

    /**
     * お届け希望時間帯
     */
    private String receiverTimeZone;

    /**
     * 配送状況確認番号
     */
    private String shipmentStatusConfirmCode;

    /**
     * 出荷完了日時
     */
    private Date completeShipmentDate;

    /**
     * 配送商品
     */
    private List<ShippingItem> shippingItemList;

}
