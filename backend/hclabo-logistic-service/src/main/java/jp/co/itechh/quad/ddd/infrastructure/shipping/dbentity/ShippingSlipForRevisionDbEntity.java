/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 改訂用配送伝票DBエンティティ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "ShippingSlipForRevision")
@Data
@Component
@Scope("prototype")
public class ShippingSlipForRevisionDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 改訂用配送伝票ID */
    @Id
    private String shippingSlipRevisionId;

    /** 配送伝票ID */
    private String shippingSlipId;

    /** 配送ステータス */
    private String shippingStatus;

    /** 配送状況確認番号 */
    private String shipmentStatusConfirmCode;

    /** 配送方法ID */
    private String shippingMethodId;

    /** 配送方法名 */
    private String shippingMethodName;

    /** 配送先住所ID */
    private String shippingAddressId;

    /** 取引ID */
    private String transactionId;

    /** 改訂用取引ID */
    private String transactionRevisionId;

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