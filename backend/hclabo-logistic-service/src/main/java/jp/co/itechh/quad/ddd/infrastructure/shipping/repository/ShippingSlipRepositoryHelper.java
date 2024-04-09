/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.repository;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShipmentStatusConfirmCode;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingAddressId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingCount;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.SecuredShippingItemDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.ShippingSlipDbEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 注文票リポジトリHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingSlipRepositoryHelper {

    /**
     * 配送伝票DBエンティティに変換
     *
     * @param shippingSlipEntity 配送伝票 エンティティ
     * @return 配送伝票DBエンティティ
     */
    public ShippingSlipDbEntity toShippingSlipDbEntityFromShippingSlipEntity(ShippingSlipEntity shippingSlipEntity) {

        if (shippingSlipEntity == null) {
            return null;
        }

        ShippingSlipDbEntity shippingSlipDbEntity = new ShippingSlipDbEntity();

        if (shippingSlipEntity.getShippingSlipId() != null) {
            shippingSlipDbEntity.setShippingSlipId(shippingSlipEntity.getShippingSlipId().getValue());
        }

        if (shippingSlipEntity.getShippingStatus() != null) {
            shippingSlipDbEntity.setShippingStatus(shippingSlipEntity.getShippingStatus().name());
        }

        if (shippingSlipEntity.getShipmentStatusConfirmCode() != null) {
            shippingSlipDbEntity.setShipmentStatusConfirmCode(
                            shippingSlipEntity.getShipmentStatusConfirmCode().getValue());
        }

        if (shippingSlipEntity.getShippingAddressId() != null) {
            shippingSlipDbEntity.setShippingAddressId(shippingSlipEntity.getShippingAddressId().getValue());
        }

        shippingSlipDbEntity.setShippingMethodId(shippingSlipEntity.getShippingMethodId());
        shippingSlipDbEntity.setShippingMethodName(shippingSlipEntity.getShippingMethodName());
        shippingSlipDbEntity.setTransactionId(shippingSlipEntity.getTransactionId());
        shippingSlipDbEntity.setInvoiceNecessaryFlag(shippingSlipEntity.isInvoiceNecessaryFlag());
        shippingSlipDbEntity.setReceiverDate(shippingSlipEntity.getReceiverDate());
        shippingSlipDbEntity.setReceiverTimeZone(shippingSlipEntity.getReceiverTimeZone());
        shippingSlipDbEntity.setCompleteShipmentDate(shippingSlipEntity.getCompleteShipmentDate());
        shippingSlipDbEntity.setRegistDate(shippingSlipEntity.getRegistDate());

        return shippingSlipDbEntity;
    }

    /**
     * 配送商品DB値オブジェクトリストに変換
     *
     * @param shippingItemList 配送商品 値オブジェクトリスト
     * @return 配送商品DB値オブジェクトリストリスト
     */
    public List<SecuredShippingItemDbEntity> toShippingItemDbEntity(List<SecuredShippingItem> shippingItemList) {

        if (shippingItemList == null) {
            return new ArrayList<>();
        }

        List<SecuredShippingItemDbEntity> shippingItemDbEntities = new ArrayList<>();

        for (SecuredShippingItem shippingItem : shippingItemList) {

            SecuredShippingItemDbEntity securedShippingItemDbEntity = new SecuredShippingItemDbEntity();

            securedShippingItemDbEntity.setItemId(shippingItem.getItemId());
            securedShippingItemDbEntity.setShippingItemSeq(shippingItem.getShippingItemSeq());
            securedShippingItemDbEntity.setItemName(shippingItem.getItemName());
            securedShippingItemDbEntity.setUnitTitle1(shippingItem.getUnitTitle1());
            securedShippingItemDbEntity.setUnitValue1(shippingItem.getUnitValue1());
            securedShippingItemDbEntity.setUnitTitle2(shippingItem.getUnitTitle2());
            securedShippingItemDbEntity.setUnitValue2(shippingItem.getUnitValue2());
            securedShippingItemDbEntity.setShippingCount(shippingItem.getShippingCount().getValue());

            shippingItemDbEntities.add(securedShippingItemDbEntity);
        }

        return shippingItemDbEntities;
    }

    /**
     * 配送伝票 エンティティに変換
     *
     * @param shippingSlipDbEntity 配送伝票DBエンティティ
     * @param shippingItemDbEntities 配送商品DB値オブジェクト
     * @return 配送伝票 エンティティ
     */
    public ShippingSlipEntity toShippingSlipEntityFromShippingSlipDbEntity(ShippingSlipDbEntity shippingSlipDbEntity,
                                                                           List<SecuredShippingItemDbEntity> shippingItemDbEntities) {

        if (shippingSlipDbEntity == null) {
            return null;
        }

        List<SecuredShippingItem> shippingItems = this.toShippingItemList(shippingItemDbEntities);

        ShippingAddressId shippingAddressId = null;
        if (shippingSlipDbEntity.getShippingAddressId() != null) {
            shippingAddressId = new ShippingAddressId(shippingSlipDbEntity.getShippingAddressId());
        }
        return new ShippingSlipEntity(
                        new ShippingSlipId(shippingSlipDbEntity.getShippingSlipId()),
                        EnumTypeUtil.getEnum(ShippingStatus.class, shippingSlipDbEntity.getShippingStatus()),
                        new ShipmentStatusConfirmCode(shippingSlipDbEntity.getShipmentStatusConfirmCode()),
                        shippingSlipDbEntity.getShippingMethodId(), shippingSlipDbEntity.getShippingMethodName(),
                        shippingItems, shippingAddressId, shippingSlipDbEntity.getTransactionId(),
                        shippingSlipDbEntity.isInvoiceNecessaryFlag(), shippingSlipDbEntity.getReceiverDate(),
                        shippingSlipDbEntity.getReceiverTimeZone(), shippingSlipDbEntity.getCompleteShipmentDate(),
                        shippingSlipDbEntity.getRegistDate()
        );
    }

    /**
     * 配送商品 値オブジェクトリストに変換
     *
     * @param shippingItemDbEntities 配送商品DB値オブジェクトリスト
     * @return 配送商品 値オブジェクトリスト
     */
    public List<SecuredShippingItem> toShippingItemList(List<SecuredShippingItemDbEntity> shippingItemDbEntities) {

        if (shippingItemDbEntities == null) {
            return new ArrayList<>();
        }

        List<SecuredShippingItem> shippingItems = new ArrayList<>();

        for (SecuredShippingItemDbEntity securedShippingItemDbEntity : shippingItemDbEntities) {

            SecuredShippingItem shippingItem = new SecuredShippingItem(
                            (int) securedShippingItemDbEntity.getShippingItemSeq(),
                            securedShippingItemDbEntity.getItemId(), securedShippingItemDbEntity.getItemName(),
                            securedShippingItemDbEntity.getUnitTitle1(), securedShippingItemDbEntity.getUnitValue1(),
                            securedShippingItemDbEntity.getUnitTitle2(), securedShippingItemDbEntity.getUnitValue2(),
                            new ShippingCount((int) securedShippingItemDbEntity.getShippingCount())
            );

            shippingItems.add(shippingItem);

        }

        return shippingItems;
    }
}