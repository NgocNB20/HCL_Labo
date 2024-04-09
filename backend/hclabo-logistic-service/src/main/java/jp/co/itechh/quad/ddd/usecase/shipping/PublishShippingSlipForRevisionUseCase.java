/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 改訂用配送伝票発行ユースケース
 */
@Service
public class PublishShippingSlipForRevisionUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public PublishShippingSlipForRevisionUseCase(IShippingSlipRepository shippingSlipRepository,
                                                 IShippingSlipForRevisionRepository shippingSlipForRevisionRepository) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
    }

    /**
     * 改訂用配送伝票を発行する
     *
     * @param transactionId         取引ID
     * @param transactionRevisionId 改訂用取引ID
     * @param customerId            顧客ID
     */
    public void publishShippingSlipForRevision(String transactionId, String transactionRevisionId, String customerId) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = shippingSlipRepository.getByTransactionId(transactionId);
        // 配送伝票が存在しない場合はエラー
        if (shippingSlipEntity == null) {
            throw new DomainException("LOGISTIC-SHIS0001-E", new String[] {transactionId});
        }

        // 改訂用配送伝票を発行
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        new ShippingSlipForRevisionEntity(shippingSlipEntity, transactionRevisionId, new Date());

        // 改訂用配送伝票を登録する
        shippingSlipForRevisionRepository.save(shippingSlipForRevisionEntity);
    }
}