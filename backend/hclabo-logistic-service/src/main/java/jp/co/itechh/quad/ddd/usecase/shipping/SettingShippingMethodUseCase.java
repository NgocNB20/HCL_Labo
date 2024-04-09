/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntityService;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 配送方法設定ユースケース
 */
@Service
public class SettingShippingMethodUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 配送伝票ドメインサービス */
    private final ShippingSlipEntityService shippingSlipEntityService;

    /** コンストラクタ */
    @Autowired
    public SettingShippingMethodUseCase(IShippingSlipRepository shippingSlipRepository,
                                        ShippingSlipEntityService shippingSlipEntityService) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.shippingSlipEntityService = shippingSlipEntityService;
    }

    /**
     * 配送方法を設定する
     *
     * @param transactionId        取引ID
     * @param shippingMethodId     配送方法ID
     * @param receiverDate         お届け日
     * @param receiverTimeZone     お届け希望時間帯
     * @param invoiceNecessaryFlag 納品書要否フラグ
     */
    public void settingShippingMethod(String transactionId,
                                      String shippingMethodId,
                                      Date receiverDate,
                                      String receiverTimeZone,
                                      boolean invoiceNecessaryFlag) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = shippingSlipRepository.getByTransactionId(transactionId);

        // 配送伝票が取得できない場合、不正な呼び出しとみなしエラー
        if (shippingSlipEntity == null) {
            // 配送伝票取得失敗
            throw new DomainException("LOGISTIC-SHIS0001-E", new String[] {transactionId});
        }

        // 配送方法と配送希望条件を設定
        shippingSlipEntityService.settingShippingMethod(
                        shippingSlipEntity, shippingMethodId, receiverDate, receiverTimeZone, invoiceNecessaryFlag);

        // 配送伝票を更新する
        shippingSlipRepository.update(shippingSlipEntity);
    }

}