/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.transaction;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.usecase.transaction.BillingUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.transaction.ConfirmOpenOfTransactionUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.transaction.OrderItemCountUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.transaction.ReceiverUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.transaction.RegistInputContentToTransactionForRevisionUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.transaction.RegistShipmentResultUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.transaction.query.OrderProcessHistoryQueryModel;
import jp.co.itechh.quad.ddd.usecase.transaction.service.ShipmentRegisterMessageDto;
import jp.co.itechh.quad.transaction.presentation.api.param.BillingRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.MessageContent;
import jp.co.itechh.quad.transaction.presentation.api.param.MessageResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.OrderItemCountRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistory;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistoryListResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.ReceiverRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.RegistInputContentToTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionConfirmOpenResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionRegistResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionShipmentInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 取引Helperクラス
 *
 * @author kimura
 */
@Component
public class TransactionHelper {

    /** グローバルメッセージ用フィールド名 */
    public static final String GLOBAL_MESSAGE_FIELD_NAME = "common";

    /**
     * 取引IDを取引開始レスポンスに変換
     *
     * @param transactionId 取引ID
     * @return response 取引開始レスポンス
     */
    public TransactionRegistResponse toTransactionRegistResponse(String transactionId) {

        if (StringUtils.isEmpty(transactionId)) {
            return null;
        }

        TransactionRegistResponse response = new TransactionRegistResponse();
        response.setTransactionId(transactionId);
        return response;
    }

    /**
     * 取引確定確認Dtoから取引確定確認レスポンスに変換
     *
     * @param dto 取引確定可能確認Dto
     * @return response 取引確定可能確認レスポンス
     */
    public TransactionConfirmOpenResponse toTransactionConfirmOpenResponse(ConfirmOpenOfTransactionUseCaseDto dto) {

        if (dto == null) {
            return null;
        }

        TransactionConfirmOpenResponse response = new TransactionConfirmOpenResponse();

        response.setRedirectUrl(dto.getRedirectUrl());
        return response;
    }

    /**
     * 取引確定確認Dtoからリンク決済URLレスポンスに変換
     *
     * @param dto 取引確定可能確認Dto
     * @return response 取引確定可能確認レスポンス
     */
    public TransactionConfirmOpenResponse toPaymentLinkUrlResponse(ConfirmOpenOfTransactionUseCaseDto dto) {

        if (dto == null) {
            return null;
        }

        TransactionConfirmOpenResponse response = new TransactionConfirmOpenResponse();

        response.setRedirectUrl(dto.getRedirectUrl());
        return response;
    }

    /**
     * 出荷実績登録ユースケース用の出荷情報パラメータリストに変換
     *
     * @param transactionShipmentInfoList 取引出荷情報リスト
     * @return paramList 出荷実績登録ユースケース用の出荷情報パラメータリスト
     */
    public List<RegistShipmentResultUseCaseParam> toRegistShipmentUseCaseParam(List<TransactionShipmentInfo> transactionShipmentInfoList) {

        if (transactionShipmentInfoList == null) {
            return null;
        }

        List<RegistShipmentResultUseCaseParam> paramList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(transactionShipmentInfoList)) {
            for (TransactionShipmentInfo transactionShipmentInfo : transactionShipmentInfoList) {
                RegistShipmentResultUseCaseParam param = new RegistShipmentResultUseCaseParam();
                param.setOrderCode(transactionShipmentInfo.getOrderCode());
                param.setShipmentStatusConfirmCode(transactionShipmentInfo.getShipmentStatusConfirmCode());
                param.setCompleteShipmentDate(transactionShipmentInfo.getCompleteShipmentDate());
                paramList.add(param);
            }
        }

        return paramList;
    }

    /**
     * 改訂用取引取得レスポンスに変換
     *
     * @param transactionForRevisionEntity 改訂用取引エンティティ
     * @return 改訂用取引取得レスポンス
     */
    public TransactionForRevisionResponse toGetTransactionForRevisionResponse(TransactionForRevisionEntity transactionForRevisionEntity) {

        if (ObjectUtils.isEmpty(transactionForRevisionEntity)) {
            return null;
        }

        TransactionForRevisionResponse response = new TransactionForRevisionResponse();

        response.setTransactionId(transactionForRevisionEntity.getTransactionId().getValue());
        response.setTransactionStatus(transactionForRevisionEntity.getTransactionStatus().name());
        response.setPaidFlag(transactionForRevisionEntity.isPaidFlag());
        response.setShippedFlag(transactionForRevisionEntity.isShippedFlag());
        response.setBillPaymentErrorFlag(transactionForRevisionEntity.isBillPaymentErrorFlag());
        response.setNotificationFlag(transactionForRevisionEntity.isNotificationFlag());
        response.setReminderSentFlag(transactionForRevisionEntity.isReminderSentFlag());
        response.setExpiredSentFlag(transactionForRevisionEntity.isExpiredSentFlag());
        response.setAdminMemo(transactionForRevisionEntity.getAdminMemo());
        response.setCustomerId(transactionForRevisionEntity.getCustomerId());
        response.setOrderReceivedId(transactionForRevisionEntity.getOrderReceivedId().getValue());
        response.setRegistDate(transactionForRevisionEntity.getRegistDate());
        response.setProcessTime(transactionForRevisionEntity.getProcessTime());
        response.setProcessType(EnumTypeUtil.getValue(transactionForRevisionEntity.getProcessType()));
        response.setProcessPersonName(transactionForRevisionEntity.getProcessPersonName());
        response.setTransactionRevisionId(transactionForRevisionEntity.getTransactionRevisionId().getValue());
        response.setNoveltyPresentJudgmentStatus(
                        EnumTypeUtil.getValue(transactionForRevisionEntity.getNoveltyPresentJudgmentStatus()));

        return response;
    }

    /**
     * 入力内容を改訂用取引へ反映するユースケースパラメータに変換
     *
     * @param registInputContentToTransactionForRevisionRequest 入力内容を改訂用取引へ反映するリクエスト
     * @return 入力内容を改訂用取引へ反映するユースケースパラメータ
     */
    public RegistInputContentToTransactionForRevisionUseCaseParam toRegistInputContentToTransactionForRevisionUseCaseParam(
                    RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest) {

        if (ObjectUtils.isEmpty(registInputContentToTransactionForRevisionRequest)) {
            return null;
        }
        RegistInputContentToTransactionForRevisionUseCaseParam param =
                        new RegistInputContentToTransactionForRevisionUseCaseParam();

        param.setTransactionRevisionId(registInputContentToTransactionForRevisionRequest.getTransactionRevisionId());
        param.setAdminMemo(registInputContentToTransactionForRevisionRequest.getAdminMemo());
        param.setCustomerId(registInputContentToTransactionForRevisionRequest.getCustomerId());
        param.setNotificationFlag(registInputContentToTransactionForRevisionRequest.getNotificationFlag());
        param.setOrderItemCountParamList(toOrderItemCountUseCaseParamList(
                        registInputContentToTransactionForRevisionRequest.getOrderItemCountRequest()));
        param.setReceiverParam(
                        toReceiverUseCaseParam(registInputContentToTransactionForRevisionRequest.getReceiverRequest()));
        param.setBillingUseCaseParam(
                        toBillingUseCaseParam(registInputContentToTransactionForRevisionRequest.getBillingRequest()));
        param.setNoveltyPresentJudgmentStatus(
                        registInputContentToTransactionForRevisionRequest.getNoveltyPresentJudgmentStatus());

        return param;
    }

    /**
     * 注文商品パラメータリストに変換
     *
     * @param orderItemCountRequestList 注文商品リクエストリスト
     * @return 注文商品パラメータリスト
     */
    public List<OrderItemCountUseCaseParam> toOrderItemCountUseCaseParamList(List<OrderItemCountRequest> orderItemCountRequestList) {

        if (ListUtils.isEmpty(orderItemCountRequestList)) {
            return null;
        }

        List<OrderItemCountUseCaseParam> paramList = new ArrayList<>();

        orderItemCountRequestList.forEach(item -> {
            OrderItemCountUseCaseParam param = new OrderItemCountUseCaseParam();

            param.setOrderCount(item.getOrderCount());
            param.setOrderItemSeq(item.getOrderItemSeq());

            paramList.add(param);
        });

        return paramList;
    }

    /**
     * お届け先パラメータに変換
     *
     * @param receiverRequest お届け先リクエスト
     * @return お届け先パラメータ
     */
    public ReceiverUseCaseParam toReceiverUseCaseParam(ReceiverRequest receiverRequest) {

        if (ObjectUtils.isEmpty(receiverRequest)) {
            return null;
        }

        ReceiverUseCaseParam param = new ReceiverUseCaseParam();

        param.setShippingAddressId(receiverRequest.getShippingAddressId());
        param.setShippingMethodId(receiverRequest.getShippingMethodId());
        param.setReceiverDate(receiverRequest.getReceiverDate());
        param.setReceiverTimeZone(receiverRequest.getReceiverTimeZone());
        param.setInvoiceNecessaryFlag(receiverRequest.getInvoiceNecessaryFlag());
        param.setShipmentStatusConfirmCode(receiverRequest.getShipmentStatusConfirmCode());

        return param;
    }

    /**
     * 請求パラメータに変換
     *
     * @param billingRequest 取引改訂を確定するリクエスト
     * @return 請求パラメータ
     */
    public BillingUseCaseParam toBillingUseCaseParam(BillingRequest billingRequest) {

        if (ObjectUtils.isEmpty(billingRequest)) {
            return null;
        }

        BillingUseCaseParam param = new BillingUseCaseParam();

        param.setBillingAddressId(billingRequest.getBillingAddressId());

        return param;
    }

    /**
     * 受注処理履歴一覧レスポンスに変換
     *
     * @param orderProcessHistoryQueryModelList
     * @return ProcessHistoryListResponse 受注処理履歴一覧レスポンス
     */
    public ProcessHistoryListResponse toProcessHistoryListResponse(List<OrderProcessHistoryQueryModel> orderProcessHistoryQueryModelList) {

        // 戻り値
        ProcessHistoryListResponse processHistoryListResponse = new ProcessHistoryListResponse();

        List<ProcessHistory> processHistoryList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(orderProcessHistoryQueryModelList)) {

            for (OrderProcessHistoryQueryModel orderProcessHistoryQueryModel : orderProcessHistoryQueryModelList) {

                ProcessHistory processHistory = new ProcessHistory();
                processHistory.setTransactionId(orderProcessHistoryQueryModel.getTransactionId());
                processHistory.setProcessTime(orderProcessHistoryQueryModel.getProcessTime());
                processHistory.setProcessType(EnumTypeUtil.getValue(orderProcessHistoryQueryModel.getProcessType()));
                processHistory.setProcessPersonName(orderProcessHistoryQueryModel.getProcessPersonName());

                processHistoryList.add(processHistory);
            }
        }

        processHistoryListResponse.setProcessHistoryList(processHistoryList);

        return processHistoryListResponse;
    }

    /**
     * メッセージレスポンスに変換
     *
     * @param messageDtoList 出荷登録実行メッセージDtoリスト
     * @return messageResponse メッセージレスポンス
     */
    public MessageResponse toMessageResponse(List<ShipmentRegisterMessageDto> messageDtoList) {

        if (CollectionUtils.isEmpty(messageDtoList)) {
            return null;
        }

        MessageResponse messageResponse = new MessageResponse();
        Map<String, List<MessageContent>> messageMap = new HashMap<>();

        List<MessageContent> messageContentList = new ArrayList<>();
        for (ShipmentRegisterMessageDto messageDto : messageDtoList) {
            MessageContent messageContent = new MessageContent();
            messageContent.setMessageCode(messageDto.getErrCode());
            messageContent.setMessage(messageDto.getErrMessage());
            messageContentList.add(messageContent);
            messageMap.put(GLOBAL_MESSAGE_FIELD_NAME, messageContentList);
        }
        messageResponse.setMessages(messageMap);

        return messageResponse;
    }

}