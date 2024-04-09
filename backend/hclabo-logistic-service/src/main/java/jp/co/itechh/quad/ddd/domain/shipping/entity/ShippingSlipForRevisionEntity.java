/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.entity;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.constant.type.HTypeShipedGoodsStockReturn;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShipmentStatusConfirmCode;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingAddressId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 改訂用配送伝票 エンティティ
 */
@Getter
public class ShippingSlipForRevisionEntity extends ShippingSlipEntity {

    /** 改訂用配送伝票ID */
    private ShippingSlipRevisionId shippingSlipRevisionId;

    /** 改訂用取引ID */
    private String transactionRevisionId;

    /** 出荷済み商品の在庫戻し可否 */
    public static final HTypeShipedGoodsStockReturn SHIPPED_ITEM_STOCK_RETURN =
                    EnumTypeUtil.getEnumFromValue(HTypeShipedGoodsStockReturn.class,
                                                  PropertiesUtil.getSystemPropertiesValue("shipedGoodsStockReturn")
                                                 );

    /**
     * コンストラクタ<br/>
     * 改訂用配送伝票発行
     */
    public ShippingSlipForRevisionEntity(ShippingSlipEntity originShippingSlipEntity,
                                         String transactionRevisionId,
                                         Date registDate) {

        super();

        // チェック
        AssertChecker.assertNotNull("originShippingSlipEntity is null", originShippingSlipEntity);
        AssertChecker.assertNotEmpty("transactionRevisionId is empty", transactionRevisionId);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 設定 */
        // 元取引をコピー
        super.copyProperties(originShippingSlipEntity);
        // 改訂販売伝票用設定
        this.shippingSlipRevisionId = new ShippingSlipRevisionId();
        this.transactionRevisionId = transactionRevisionId;
        this.registDate = registDate;
    }

    /**
     * 伝票取消
     */
    public void cancelSlip() {

        // チェック
        // 出荷完了・確定状態でないならエラー
        if (this.shippingStatus != ShippingStatus.OPEN && this.shippingStatus != ShippingStatus.SHIPMENT_COMPLETED) {
            throw new DomainException("LOGISTIC-SHOD0001-E");
        }

        // 配送ステータス取消
        this.shippingStatus = ShippingStatus.CANCEL;
        // 配送商品クリア
        this.securedShippingItemList = new ArrayList<>();
    }

    /**
     * 出荷完了
     *
     * @param shipmentStatusConfirmCode 配送状況確認番号(任意)
     * @param completeShipmentDate      出荷完了日時(任意)
     */
    public void completeShipment(ShipmentStatusConfirmCode shipmentStatusConfirmCode, Date completeShipmentDate) {

        // チェック
        // 出荷完了日時が未設定の場合、現在日を設定
        if (completeShipmentDate == null) {
            completeShipmentDate = new Date();
        }
        // 確定状態でないならエラー
        if (this.shippingStatus != ShippingStatus.OPEN) {
            throw new DomainException("LOGISTIC-SHOD0001-E");
        }

        // 設定
        this.shipmentStatusConfirmCode = shipmentStatusConfirmCode;
        this.completeShipmentDate = completeShipmentDate;
        // 配送ステータス出荷完了
        this.shippingStatus = ShippingStatus.SHIPMENT_COMPLETED;
    }

    /**
     * 配送条件更新
     *
     * @param param
     */
    public void updateShippingCondition(UpdateShippingConditionDomainParam param) {

        this.shippingMethodId = param.getShippingMethodId();
        this.shippingMethodName = param.getShippingMethodName();
        this.receiverDate = param.getReceiverDate();
        this.receiverTimeZone = param.getReceiverTimeZone();
        this.invoiceNecessaryFlag = param.isInvoiceNecessaryFlag();
        this.shipmentStatusConfirmCode = new ShipmentStatusConfirmCode(param.getShipmentStatusConfirmCode());
    }

    /**
     * 配送先住所更新
     *
     * @param shippingAddressId 配送先住所ID
     */
    public void updateShippingAddress(ShippingAddressId shippingAddressId) {

        // 設定
        this.shippingAddressId = shippingAddressId;
    }

    /**
     * 配送商品設定
     * ※ドメインサービス用
     *
     * @param securedShippingItemList
     */
    void setSecuredShippingItemList(List<SecuredShippingItem> securedShippingItemList) {
        this.securedShippingItemList = securedShippingItemList;
    }

    /**
     * ステータス設定
     * ※ドメインサービス用
     *
     * @param securedInventory
     */
    public void setShippingStatus(ShippingStatus securedInventory) {
        this.shippingStatus = securedInventory;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     *
     * @param shippingSlipId
     * @param shippingStatus
     * @param shipmentStatusConfirmCode
     * @param shippingMethodId
     * @param shippingMethodName
     * @param shippingItemList
     * @param shippingAddressId
     * @param transactionId
     * @param invoiceNecessaryFlag
     * @param receiverDate
     * @param receiverTimeZone
     * @param completeShipmentDate
     * @param registDate
     */
    public ShippingSlipForRevisionEntity(ShippingSlipId shippingSlipId,
                                         ShippingStatus shippingStatus,
                                         ShipmentStatusConfirmCode shipmentStatusConfirmCode,
                                         String shippingMethodId,
                                         String shippingMethodName,
                                         List<SecuredShippingItem> shippingItemList,
                                         ShippingAddressId shippingAddressId,
                                         String transactionId,
                                         boolean invoiceNecessaryFlag,
                                         Date receiverDate,
                                         String receiverTimeZone,
                                         Date completeShipmentDate,
                                         Date registDate,
                                         ShippingSlipRevisionId shippingSlipRevisionId,
                                         String transactionRevisionId) {
        super(shippingSlipId, shippingStatus, shipmentStatusConfirmCode, shippingMethodId, shippingMethodName,
              shippingItemList, shippingAddressId, transactionId, invoiceNecessaryFlag, receiverDate, receiverTimeZone,
              completeShipmentDate, registDate
             );
        this.shippingSlipRevisionId = shippingSlipRevisionId;
        this.transactionRevisionId = transactionRevisionId;
    }
}