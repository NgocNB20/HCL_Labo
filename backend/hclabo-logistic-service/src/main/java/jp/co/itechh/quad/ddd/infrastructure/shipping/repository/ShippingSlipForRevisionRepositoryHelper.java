/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.repository;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShipmentStatusConfirmCode;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingAddressId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingCount;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.SecuredShippingItemForRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.ShippingSlipForRevisionDbEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送伝票リポジトリHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingSlipForRevisionRepositoryHelper {

    /**
     * 改訂用配送伝票DBエンティティに変換
     *
     * @param shippingSlipForRevisionEntity 改訂用配送伝票 エンティティ
     * @return 改訂用配送伝票DBエンティティ
     */
    public ShippingSlipForRevisionDbEntity toShippingSlipForRevisionDbEntityFromShippingSlipForRevisionEntity(
                    ShippingSlipForRevisionEntity shippingSlipForRevisionEntity) {

        if (shippingSlipForRevisionEntity == null) {
            return null;
        }

        ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity = new ShippingSlipForRevisionDbEntity();

        if (shippingSlipForRevisionEntity.getShippingSlipId() != null) {
            shippingSlipForRevisionDbEntity.setShippingSlipId(
                            shippingSlipForRevisionEntity.getShippingSlipId().getValue());
        }

        if (shippingSlipForRevisionEntity.getShippingSlipRevisionId() != null) {
            shippingSlipForRevisionDbEntity.setShippingSlipRevisionId(
                            shippingSlipForRevisionEntity.getShippingSlipRevisionId().getValue());
        }

        if (shippingSlipForRevisionEntity.getShippingStatus() != null) {
            shippingSlipForRevisionDbEntity.setShippingStatus(shippingSlipForRevisionEntity.getShippingStatus().name());
        }

        if (shippingSlipForRevisionEntity.getShipmentStatusConfirmCode() != null) {
            shippingSlipForRevisionDbEntity.setShipmentStatusConfirmCode(
                            shippingSlipForRevisionEntity.getShipmentStatusConfirmCode().getValue());
        }

        if (shippingSlipForRevisionEntity.getShippingAddressId() != null) {
            shippingSlipForRevisionDbEntity.setShippingAddressId(
                            shippingSlipForRevisionEntity.getShippingAddressId().getValue());
        }

        shippingSlipForRevisionDbEntity.setShippingMethodId(shippingSlipForRevisionEntity.getShippingMethodId());
        shippingSlipForRevisionDbEntity.setShippingMethodName(shippingSlipForRevisionEntity.getShippingMethodName());
        shippingSlipForRevisionDbEntity.setTransactionId(shippingSlipForRevisionEntity.getTransactionId());
        shippingSlipForRevisionDbEntity.setTransactionRevisionId(
                        shippingSlipForRevisionEntity.getTransactionRevisionId());
        shippingSlipForRevisionDbEntity.setInvoiceNecessaryFlag(shippingSlipForRevisionEntity.isInvoiceNecessaryFlag());
        shippingSlipForRevisionDbEntity.setReceiverDate(shippingSlipForRevisionEntity.getReceiverDate());
        shippingSlipForRevisionDbEntity.setReceiverTimeZone(shippingSlipForRevisionEntity.getReceiverTimeZone());
        shippingSlipForRevisionDbEntity.setCompleteShipmentDate(
                        shippingSlipForRevisionEntity.getCompleteShipmentDate());
        shippingSlipForRevisionDbEntity.setRegistDate(shippingSlipForRevisionEntity.getRegistDate());

        return shippingSlipForRevisionDbEntity;
    }

    /**
     * 改訂用配送商品DB値オブジェクトリストに変換
     *
     * @param shippingItemList 配送商品 値オブジェクトリスト
     * @return 改訂用配送商品DB値オブジェクトリスト
     */
    public List<SecuredShippingItemForRevisionDbEntity> toShippingItemForRevisionDbEntityList(List<SecuredShippingItem> shippingItemList) {

        if (shippingItemList == null) {
            return new ArrayList<>();
        }

        List<SecuredShippingItemForRevisionDbEntity> shippingItemDbEntities = new ArrayList<>();

        for (SecuredShippingItem shippingItem : shippingItemList) {

            SecuredShippingItemForRevisionDbEntity securedShippingItemForRevisionDbEntity =
                            new SecuredShippingItemForRevisionDbEntity();

            securedShippingItemForRevisionDbEntity.setShippingItemSeq(shippingItem.getShippingItemSeq());
            securedShippingItemForRevisionDbEntity.setItemId(shippingItem.getItemId());
            securedShippingItemForRevisionDbEntity.setItemName(shippingItem.getItemName());
            securedShippingItemForRevisionDbEntity.setUnitTitle1(shippingItem.getUnitTitle1());
            securedShippingItemForRevisionDbEntity.setUnitValue1(shippingItem.getUnitValue1());
            securedShippingItemForRevisionDbEntity.setUnitTitle2(shippingItem.getUnitTitle2());
            securedShippingItemForRevisionDbEntity.setUnitValue2(shippingItem.getUnitValue2());
            securedShippingItemForRevisionDbEntity.setShippingCount(shippingItem.getShippingCount().getValue());

            shippingItemDbEntities.add(securedShippingItemForRevisionDbEntity);
        }

        return shippingItemDbEntities;
    }

    /**
     * 改訂用配送伝票 エンティティに変換
     *
     * @param shippingSlipForRevisionDbEntity 改訂用配送伝票DBエンティティ
     * @param shippingItemForRevisionDbEntities 改訂用配送商品DB値オブジェクト
     * @return 改訂用配送伝票 エンティティ
     */
    public ShippingSlipForRevisionEntity toShippingSlipForRevisionEntityFromShippingSlipForRevisionDbEntity(
                    ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity,
                    List<SecuredShippingItemForRevisionDbEntity> shippingItemForRevisionDbEntities) {

        if (shippingSlipForRevisionDbEntity == null) {
            return null;
        }

        List<SecuredShippingItem> shippingItems =
            this.toShippingItemForRevisionList(shippingItemForRevisionDbEntities);

        return new ShippingSlipForRevisionEntity(
                        new ShippingSlipId(shippingSlipForRevisionDbEntity.getShippingSlipId()),
                        EnumTypeUtil.getEnum(ShippingStatus.class, shippingSlipForRevisionDbEntity.getShippingStatus()),
                        new ShipmentStatusConfirmCode(shippingSlipForRevisionDbEntity.getShipmentStatusConfirmCode()),
                        shippingSlipForRevisionDbEntity.getShippingMethodId(),
                        shippingSlipForRevisionDbEntity.getShippingMethodName(), shippingItems,
                        new ShippingAddressId(shippingSlipForRevisionDbEntity.getShippingAddressId()),
                        shippingSlipForRevisionDbEntity.getTransactionId(),
                        shippingSlipForRevisionDbEntity.isInvoiceNecessaryFlag(),
                        shippingSlipForRevisionDbEntity.getReceiverDate(),
                        shippingSlipForRevisionDbEntity.getReceiverTimeZone(),
                        shippingSlipForRevisionDbEntity.getCompleteShipmentDate(),
                        shippingSlipForRevisionDbEntity.getRegistDate(),
                        new ShippingSlipRevisionId(shippingSlipForRevisionDbEntity.getShippingSlipRevisionId()),
                        shippingSlipForRevisionDbEntity.getTransactionRevisionId()
        );
    }

    /**
     * 確保済み配送商品 値オブジェクトリストに変換
     *
     * @param shippingItemForRevisionDbEntities 改訂用配送商品DB値オブジェクトリスト
     * @return 確保済み配送商品 値オブジェクトリスト
     */
    public List<SecuredShippingItem> toShippingItemForRevisionList(List<SecuredShippingItemForRevisionDbEntity> shippingItemForRevisionDbEntities) {

        if (shippingItemForRevisionDbEntities == null) {
            return null;
        }

        List<SecuredShippingItem> shippingItems = new ArrayList<>();

        for (SecuredShippingItemForRevisionDbEntity securedShippingItemForRevisionDbEntity : shippingItemForRevisionDbEntities) {

            SecuredShippingItem shippingItem = new SecuredShippingItem(
                            (int) securedShippingItemForRevisionDbEntity.getShippingItemSeq(),
                            securedShippingItemForRevisionDbEntity.getItemId(),
                            securedShippingItemForRevisionDbEntity.getItemName(),
                            securedShippingItemForRevisionDbEntity.getUnitTitle1(),
                            securedShippingItemForRevisionDbEntity.getUnitValue1(),
                            securedShippingItemForRevisionDbEntity.getUnitTitle2(),
                            securedShippingItemForRevisionDbEntity.getUnitValue2(),
                            new ShippingCount((int) securedShippingItemForRevisionDbEntity.getShippingCount())
            );

            shippingItems.add(shippingItem);

        }

        return shippingItems;
    }
}