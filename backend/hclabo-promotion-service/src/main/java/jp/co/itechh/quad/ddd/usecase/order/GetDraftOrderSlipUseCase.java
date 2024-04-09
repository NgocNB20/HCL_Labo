/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipId;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 下書き注文票取得ユースケース
 */
@Service
public class GetDraftOrderSlipUseCase {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    @Autowired
    public GetDraftOrderSlipUseCase(IOrderSlipRepository orderSlipRepository) {
        this.orderSlipRepository = orderSlipRepository;
    }

    /**
     * 下書き注文票を取得する
     *
     * @param customerId    顧客ID
     * @param orderSlipId   注文票ID ※任意
     * @param transactionId 取引ID ※任意
     * @return 存在する ... 下書き注文票 / 存在しない ... Null
     */
    public OrderSlipEntity getDraftOrderSlip(String customerId, String orderSlipId, String transactionId) {

        OrderSlipEntity result = null;

        if (!StringUtils.isBlank(orderSlipId)) {
            // 注文票IDが存在する場合、注文票IDで下書き注文票を取得する
            result = this.orderSlipRepository.getDraftOrderSlipByOrderSlipId(new OrderSlipId(orderSlipId));

            if (result != null) {
                // 取得できたため返却
                return result;
            }
        }

        if (!StringUtils.isBlank(transactionId)) {
            // 取引IDが存在する場合、取引IDで下書き注文票を取得する
            result = this.orderSlipRepository.getDraftOrderSlipByTransactionId(transactionId);

            if (result != null) {
                // 取得できたため返却
                return result;
            }
        }

        // 顧客IDで下書き注文票を取得する
        return this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerId);
    }

}
