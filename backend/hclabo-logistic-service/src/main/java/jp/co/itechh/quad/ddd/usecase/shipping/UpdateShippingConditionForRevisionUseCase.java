/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.ddd.domain.method.proxy.ShippingMethodProxyService;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.UpdateShippingConditionDomainParam;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用配送伝票の配送条件を更新する ユースケース
 */
@Service
public class UpdateShippingConditionForRevisionUseCase {

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** 配送方法プロキシサービス */
    private final ShippingMethodProxyService shippingMethodProxyService;

    /** コンストラクタ */
    @Autowired
    public UpdateShippingConditionForRevisionUseCase(IShippingSlipForRevisionRepository shippingSlipForRevisionRepository,
                                                     ShippingMethodProxyService shippingMethodProxyService) {
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
        this.shippingMethodProxyService = shippingMethodProxyService;
    }

    /**
     * 改訂用取引IDに紐づく改訂用配送伝票を取得する
     *
     * @param transactionRevisionId
     * @param param
     */
    public void updateShippingConditionForRevision(String transactionRevisionId,
                                                   UpdateShippingConditionDomainParam param) {

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (shippingSlipForRevisionEntity == null) {
            throw new DomainException("LOGISTIC-SSRE0001-E", new String[] {transactionRevisionId});
        }
        // 更新配送方法詳細取得
        DeliveryMethodDetailsDto updateDeliveryMethodDto = this.shippingMethodProxyService.getByDeliveryMethodSeq(
                        Integer.parseInt(param.getShippingMethodId()));

        if (updateDeliveryMethodDto != null && updateDeliveryMethodDto.getDeliveryMethodEntity() != null) {
            param.setShippingMethodName(updateDeliveryMethodDto.getDeliveryMethodEntity().getDeliveryMethodName());
        }

        // 改訂用配送条件更新
        shippingSlipForRevisionEntity.updateShippingCondition(param);

        // 改訂用配送伝票更新
        shippingSlipForRevisionRepository.update(shippingSlipForRevisionEntity);
    }

}