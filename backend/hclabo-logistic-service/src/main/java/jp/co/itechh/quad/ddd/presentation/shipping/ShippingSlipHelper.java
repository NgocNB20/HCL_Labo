/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.UpdateShippingConditionDomainParam;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingItem;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingUpdateConditionForRevisionRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送伝票Helperクラス
 *
 * @author kimura
 */
@Component
public class ShippingSlipHelper {
    /**
     * 配送伝票エンティティから配送伝票レスポンスに変換
     *
     * @param entity 配送伝票エンティティ
     * @return response 配送伝票レスポンス
     */
    public ShippingSlipResponse toShippingSlipResponse(ShippingSlipEntity entity) {

        if (entity == null) {
            return null;
        }

        ShippingSlipResponse response = new ShippingSlipResponse();
        response.setShippingSlipId(entity.getShippingSlipId().getValue());
        response.setShippingAddressId(entity.getShippingAddressId().getValue());
        response.setShippingStatus(entity.getShippingStatus().toString());
        response.setShippingMethodId(entity.getShippingMethodId());
        response.setShippingMethodName(entity.getShippingMethodName());
        response.setInvoiceNecessaryFlag(entity.isInvoiceNecessaryFlag());
        response.setReceiverDate(entity.getReceiverDate());
        response.setReceiverTimeZone(entity.getReceiverTimeZone());
        response.setCompleteShipmentDate(entity.getCompleteShipmentDate());
        response.setShipmentStatusConfirmCode(entity.getShipmentStatusConfirmCode().getValue());

        if (!CollectionUtils.isEmpty(entity.getSecuredShippingItemList())) {

            List<ShippingItem> itemList = new ArrayList<>();
            for (SecuredShippingItem securedShippingItem : entity.getSecuredShippingItemList()) {
                ShippingItem item = new ShippingItem();
                item.setShippingItemSeq(securedShippingItem.getShippingItemSeq());
                item.setItemId(securedShippingItem.getItemId());
                item.setItemName(securedShippingItem.getItemName());
                item.setUnitTitle1(securedShippingItem.getUnitTitle1());
                item.setUnitValue1(securedShippingItem.getUnitValue1());
                item.setUnitTitle2(securedShippingItem.getUnitTitle2());
                item.setUnitValue2(securedShippingItem.getUnitValue2());
                item.setShippingCount(securedShippingItem.getShippingCount().getValue());
                itemList.add(item);
            }

            response.setShippingItemList(itemList);
        }

        return response;
    }

    /**
     * 改訂用配送伝票の配送先更新リクエストから改訂用配送伝票の配送条件更新 パラメータに変換
     *
     * @param shippingUpdateConditionForRevisionRequest 改訂用配送伝票の配送先更新リクエスト
     * @return response 改訂用配送伝票の配送条件更新 パラメータ
     */
    public UpdateShippingConditionDomainParam toUpdateShippingConditionDomainParam(
                    ShippingUpdateConditionForRevisionRequest shippingUpdateConditionForRevisionRequest) {

        if (shippingUpdateConditionForRevisionRequest == null) {
            return null;
        }

        UpdateShippingConditionDomainParam updateShippingConditionDomainParam =
                        new UpdateShippingConditionDomainParam();
        updateShippingConditionDomainParam.setShippingMethodId(
                        shippingUpdateConditionForRevisionRequest.getShippingMethodId());
        updateShippingConditionDomainParam.setReceiverDate(shippingUpdateConditionForRevisionRequest.getReceiverDate());
        updateShippingConditionDomainParam.setReceiverTimeZone(
                        shippingUpdateConditionForRevisionRequest.getReceiverTimeZone());
        updateShippingConditionDomainParam.setInvoiceNecessaryFlag(
                        shippingUpdateConditionForRevisionRequest.getInvoiceNecessaryFlag());
        updateShippingConditionDomainParam.setShipmentStatusConfirmCode(
                        shippingUpdateConditionForRevisionRequest.getShipmentStatusConfirmCode());

        return updateShippingConditionDomainParam;
    }
}