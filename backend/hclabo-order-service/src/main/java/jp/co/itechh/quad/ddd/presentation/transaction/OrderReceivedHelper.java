/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.transaction;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedByTransactionIdUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryModel;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryQueryModel;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistory;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistoryListResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 受注Helperクラス
 *
 * @author yt23807
 */
@Component
public class OrderReceivedHelper {

    /**
     * 顧客注文履歴一覧レスポンスに変換
     *
     * @param customerOrderHistoryModel
     * @return CustomerHistoryListResponse 顧客注文履歴一覧レスポンス
     */
    CustomerHistoryListResponse toCustomerHistoryListResponse(CustomerOrderHistoryModel customerOrderHistoryModel) {

        // 戻り値
        CustomerHistoryListResponse customerHistoryListResponse = new CustomerHistoryListResponse();

        List<CustomerHistory> customerHistoryList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(customerOrderHistoryModel.getCustomerOrderHistoryQueryModelList())) {

            for (CustomerOrderHistoryQueryModel customerOrderHistoryQueryModel : customerOrderHistoryModel.getCustomerOrderHistoryQueryModelList()) {

                CustomerHistory customerHistory = new CustomerHistory();
                customerHistory.setOrderStatus(customerOrderHistoryQueryModel.getOrderStatus().toString());
                customerHistory.setOrderReceivedDate(customerOrderHistoryQueryModel.getOrderReceivedDate());
                customerHistory.setOrderCode(customerOrderHistoryQueryModel.getOrderCode());
                customerHistory.setPaymentPrice(customerOrderHistoryQueryModel.getPaymentPrice());

                customerHistoryList.add(customerHistory);
            }
        }

        customerHistoryListResponse.setCustomerHistoryList(customerHistoryList);
        customerHistoryListResponse.setPageInfo(customerOrderHistoryModel.getPageInfoResponse());

        return customerHistoryListResponse;
    }

    /**
     * 受注取得レスポンスに変換
     *
     * @param getOrderReceivedUseCaseDto
     * @return OrderReceivedResponse 受注取得レスポンス
     */
    OrderReceivedResponse toOrderReceivedResponse(GetOrderReceivedUseCaseDto getOrderReceivedUseCaseDto) {

        if (ObjectUtils.isEmpty(getOrderReceivedUseCaseDto)) {
            return null;
        }
        return toOrderReceivedResponse(
                        getOrderReceivedUseCaseDto.getOrderReceivedEntity(),
                        getOrderReceivedUseCaseDto.getTransactionEntity(), getOrderReceivedUseCaseDto.getOrderStatus()
                                      );
    }

    /**
     * 受注取得レスポンスに変換
     *
     * @param getOrderReceivedByTransactionIdUseCaseDto
     * @return OrderReceivedResponse 受注取得レスポンス
     */
    OrderReceivedResponse toOrderReceivedResponse(GetOrderReceivedByTransactionIdUseCaseDto getOrderReceivedByTransactionIdUseCaseDto) {

        if (ObjectUtils.isEmpty(getOrderReceivedByTransactionIdUseCaseDto)) {
            return null;
        }
        return toOrderReceivedResponse(
                        getOrderReceivedByTransactionIdUseCaseDto.getOrderReceivedEntity(),
                        getOrderReceivedByTransactionIdUseCaseDto.getTransactionEntity(),
                        getOrderReceivedByTransactionIdUseCaseDto.getOrderStatus()
                                      );
    }

    /**
     * 受注取得レスポンスに変換
     *
     * @param orderReceivedEntity
     * @param transactionEntity
     * @param orderStatus
     * @return OrderReceivedResponse 受注取得レスポンス
     */
    private OrderReceivedResponse toOrderReceivedResponse(OrderReceivedEntity orderReceivedEntity,
                                                          TransactionEntity transactionEntity,
                                                          OrderStatus orderStatus) {
        OrderReceivedResponse orderReceivedResponse = new OrderReceivedResponse();

        // 受注項目セット
        orderReceivedResponse.setOrderCode(orderReceivedEntity.getOrderCode().getValue());
        orderReceivedResponse.setOrderReceivedId(orderReceivedEntity.getOrderReceivedId().getValue());
        orderReceivedResponse.setOrderReceivedDate(orderReceivedEntity.getOrderReceivedDate());
        orderReceivedResponse.setCancelDate(orderReceivedEntity.getCancelDate());
        orderReceivedResponse.setLatestTransactionId(orderReceivedEntity.getLatestTransactionId().getValue());
        if (orderStatus != null) {
            orderReceivedResponse.setOrderStatus(orderStatus.toString());
        }

        // 取引項目セット
        orderReceivedResponse.setTransactionStatus(transactionEntity.getTransactionStatus().toString());
        orderReceivedResponse.setPaidFlag(transactionEntity.isPaidFlag());
        if (transactionEntity.getPaymentStatusDetail() != null) {
            orderReceivedResponse.setPaymentStatusDetail(
                            transactionEntity.getPaymentStatusDetail().getStatusValue(transactionEntity.isPaidFlag()));
        }
        orderReceivedResponse.setShippedFlag(transactionEntity.isShippedFlag());
        orderReceivedResponse.setBillPaymentErrorFlag(transactionEntity.isBillPaymentErrorFlag());
        orderReceivedResponse.setNotificationFlag(transactionEntity.isNotificationFlag());
        orderReceivedResponse.setReminderSentFlag(transactionEntity.isReminderSentFlag());
        orderReceivedResponse.setExpiredSentFlag(transactionEntity.isExpiredSentFlag());
        orderReceivedResponse.setAdminMemo(transactionEntity.getAdminMemo());
        orderReceivedResponse.setCustomerId(transactionEntity.getCustomerId());
        orderReceivedResponse.setProcessTime(transactionEntity.getProcessTime());
        orderReceivedResponse.setProcessType(EnumTypeUtil.getValue(transactionEntity.getProcessType()));
        orderReceivedResponse.setProcessPersonName(transactionEntity.getProcessPersonName());
        orderReceivedResponse.setNoveltyPresentJudgmentStatus(
                        EnumTypeUtil.getValue(transactionEntity.getNoveltyPresentJudgmentStatus()));

        return orderReceivedResponse;
    }
}