/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.transaction.query;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderStatusFactory;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.CustomerOrderHistoryBaseInfoDto;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryQueryModel;
import org.springframework.stereotype.Component;

/**
 * 顧客注文履歴一覧 Helperクラス
 */
@Component
public class GetCustomerOrderHistoryListQueryHelper {

    /**
     * 顧客注文履歴一覧用 QueryModeに変換
     *
     * @param customerOrderHistoryBaseInfoDto
     * @param billingSlipResponse
     * @param preClaimFlag
     * @return CustomerOrderHistoryQueryModel
     */
    public CustomerOrderHistoryQueryModel toCustomerOrderHistoryQueryModel(CustomerOrderHistoryBaseInfoDto customerOrderHistoryBaseInfoDto,
                                                                           BillingSlipResponse billingSlipResponse,
                                                                           Boolean preClaimFlag) {

        // 戻り値
        CustomerOrderHistoryQueryModel customerOrderHistoryQueryModel = new CustomerOrderHistoryQueryModel();

        customerOrderHistoryQueryModel.setOrderStatus(OrderStatusFactory.constructOrderStatus(
                        EnumTypeUtil.getEnum(TransactionStatus.class,
                                             customerOrderHistoryBaseInfoDto.getTransactionStatus()
                                            ), customerOrderHistoryBaseInfoDto.isPaidFlag(),
                        customerOrderHistoryBaseInfoDto.isShippedFlag(),
                        customerOrderHistoryBaseInfoDto.isBillPaymentErrorFlag(), preClaimFlag));

        customerOrderHistoryQueryModel.setOrderReceivedDate(customerOrderHistoryBaseInfoDto.getOrderReceivedDate());
        customerOrderHistoryQueryModel.setOrderCode(customerOrderHistoryBaseInfoDto.getOrderCode());
        customerOrderHistoryQueryModel.setPaymentPrice(billingSlipResponse.getBillingPrice());

        return customerOrderHistoryQueryModel;
    }

}