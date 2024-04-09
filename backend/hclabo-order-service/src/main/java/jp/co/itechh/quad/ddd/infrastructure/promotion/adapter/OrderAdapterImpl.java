/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.UpdateOrderSlipForRevisionParam;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.AddOrderItemForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.CancelOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.ChangeOrderItemCountForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.DeleteOrderItemForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.GetOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OpenOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipCheckRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionCheckRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipModernizeRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipOpenRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipTransactionRegistRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.PublishOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.UpdateOrderSlipForRevisionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 注文アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class OrderAdapterImpl implements IOrderSlipAdapter {

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** 注文アダプターHelperクラス */
    private final OrderAdapterHelper orderAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param orderSlipApi 注文票API
     * @param headerParamsUtil   ヘッダパラメーターユーティル
     * @param orderAdapterHelper 注文アダプターHelperクラス
     */
    @Autowired
    public OrderAdapterImpl(OrderSlipApi orderSlipApi,
                            HeaderParamsUtility headerParamsUtil,
                            OrderAdapterHelper orderAdapterHelper) {
        this.orderSlipApi = orderSlipApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderSlipApi.getApiClient());

        this.orderAdapterHelper = orderAdapterHelper;
    }

    /**
     * プロモーションマイクロサービス<br/>
     * 取引開始
     *
     * @param transactionId    取引ID
     * @param customerBirthday 顧客生年月日
     */
    @Override
    public void startTransaction(TransactionId transactionId, Date customerBirthday) {

        OrderSlipTransactionRegistRequest orderSlipTransactionRegistRequest = new OrderSlipTransactionRegistRequest();
        orderSlipTransactionRegistRequest.setTransactionId(transactionId.getValue());

        orderSlipApi.registTransaction(customerBirthday, orderSlipTransactionRegistRequest);
    }

    /**
     * 下書き注文票チェック
     *
     * @param transactionId 取引ID
     * @param customerBirthday 顧客生年月日
     */
    @Override
    public void checkDraftOrderSlip(TransactionId transactionId, Date customerBirthday) {

        OrderSlipCheckRequest orderSlipCheckRequest = null;
        if (transactionId != null) {
            orderSlipCheckRequest = new OrderSlipCheckRequest();
            orderSlipCheckRequest.setTransactionId(transactionId.getValue());
        }
        orderSlipApi.checkDraft(customerBirthday, orderSlipCheckRequest);
    }

    /**
     * プロモーションマイクロサービス<br/>
     * 注文票最新化
     *
     * @param transactionId 取引ID
     */
    @Override
    public void modernizeOrderSlip(TransactionId transactionId) {

        OrderSlipModernizeRequest orderSlipModernizeRequest = new OrderSlipModernizeRequest();
        orderSlipModernizeRequest.setTransactionId(transactionId.getValue());

        orderSlipApi.modernize(orderSlipModernizeRequest);
    }

    /**
     * プロモーションマイクロサービス<br/>
     * 注文票確定
     *
     * @param transactionId 取引ID
     */
    @Override
    public void openOrderSlip(TransactionId transactionId) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();
        this.orderSlipApi.getApiClient().setUserAgent(request.getHeader("User-Agent"));

        OrderSlipOpenRequest orderSlipOpenRequest = new OrderSlipOpenRequest();
        orderSlipOpenRequest.setTransactionId(transactionId.getValue());
        orderSlipApi.open(orderSlipOpenRequest);
    }

    /**
     * 改訂用注文票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void publishTransactionForRevision(TransactionId transactionId,
                                              TransactionRevisionId transactionRevisionId) {

        PublishOrderSlipForRevisionRequest publishOrderSlipForRevisionRequest =
                        new PublishOrderSlipForRevisionRequest();

        publishOrderSlipForRevisionRequest.setTransactionId(transactionId.getValue());
        publishOrderSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        orderSlipApi.publishOrderSlipForRevision(publishOrderSlipForRevisionRequest);
    }

    /**
     * 改訂用注文票チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void checkOrderSlipForRevision(TransactionRevisionId transactionRevisionId, TransactionId transactionId) {

        OrderSlipForRevisionCheckRequest orderSlipForRevisionCheckRequest = new OrderSlipForRevisionCheckRequest();

        orderSlipForRevisionCheckRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        orderSlipApi.checkOrderSlipForRevision(orderSlipForRevisionCheckRequest);
    }

    /**
     * 改訂用配送伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void cancelOrderSlipForRevision(TransactionRevisionId transactionRevisionId) {

        CancelOrderSlipForRevisionRequest cancelOrderSlipForRevisionRequest = new CancelOrderSlipForRevisionRequest();

        cancelOrderSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        orderSlipApi.cancelOrderSlipForRevision(cancelOrderSlipForRevisionRequest);
    }

    /**
     * 改訂用注文票更新
     *
     * @param orderSlipForRevisionParam
     */
    @Override
    public void updateOrderSlipForRevision(UpdateOrderSlipForRevisionParam orderSlipForRevisionParam) {

        List<ChangeOrderItemCountForRevisionRequest> changeItemList =
                        orderAdapterHelper.toChangeOrderItemCountForRevisionRequestList(
                                        orderSlipForRevisionParam.getOrderItemCountParamList());

        UpdateOrderSlipForRevisionRequest updateOrderSlipForRevisionRequest = new UpdateOrderSlipForRevisionRequest();

        updateOrderSlipForRevisionRequest.setTransactionRevisionId(
                        orderSlipForRevisionParam.getTransactionRevisionId().getValue());
        updateOrderSlipForRevisionRequest.setChangeItemList(changeItemList);

        orderSlipApi.updateOrderSlipForRevision(updateOrderSlipForRevisionRequest);
    }

    /**
     * 改訂用注文票に注文商品を追加
     *
     * @param transactionRevisionId
     * @param itemId
     * @param itemIdCount
     */
    @Override
    public void addOrderItemToOrderSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                   String itemId,
                                                   Integer itemIdCount) {

        AddOrderItemForRevisionRequest addOrderItemForRevisionRequest = new AddOrderItemForRevisionRequest();

        addOrderItemForRevisionRequest.setItemId(itemId);
        addOrderItemForRevisionRequest.setOrderCount(itemIdCount);
        addOrderItemForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        orderSlipApi.addOrderItemForRevision(addOrderItemForRevisionRequest);
    }

    /**
     * 改訂用注文票から注文商品を削除
     *
     * @param transactionRevisionId
     * @param itemSeq
     */
    @Override
    public void deleteOrderItemToOrderSlipForRevision(TransactionRevisionId transactionRevisionId, Integer itemSeq) {

        DeleteOrderItemForRevisionRequest deleteOrderItemForRevisionRequest = new DeleteOrderItemForRevisionRequest();

        deleteOrderItemForRevisionRequest.setOrderItemSeq(itemSeq);
        deleteOrderItemForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        orderSlipApi.deleteOrderItemForRevision(deleteOrderItemForRevisionRequest);
    }

    /**
     * 改訂用注文票を確定
     *
     * @param transactionRevisionId
     */
    @Override
    public void openOrderSlipForRevision(TransactionRevisionId transactionRevisionId) {

        OpenOrderSlipForRevisionRequest openOrderSlipForRevisionRequest = new OpenOrderSlipForRevisionRequest();

        openOrderSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        orderSlipApi.openOrderSlipForRevision(openOrderSlipForRevisionRequest);
    }

    /**
     * 注文票取得
     *
     * @param transactionId 取引ID
     * @return 注文票
     */
    @Override
    public OrderSlip getOrderSlipByTransactionId(TransactionId transactionId) {

        OrderSlipGetRequest orderSlipGetRequest = new OrderSlipGetRequest();
        orderSlipGetRequest.setTransactionId(transactionId.getValue());

        OrderSlipResponse orderSlipResponse = orderSlipApi.get(orderSlipGetRequest);

        return orderAdapterHelper.toOrderSlip(orderSlipResponse);
    }

    /**
     * 改訂用注文票取得
     *
     * @param transactionRevisionId 取引ID
     * @return OrderSlipForRevision 改訂用注文票
     */
    @Override
    public OrderSlipForRevision getOrderSlipForRevision(String transactionRevisionId) {

        GetOrderSlipForRevisionRequest request = new GetOrderSlipForRevisionRequest();
        request.setTransactionRevisionId(transactionRevisionId);

        OrderSlipForRevisionResponse orderSlipForRevisionResponse = orderSlipApi.getOrderSlipForRevision(request);

        return orderAdapterHelper.toOrderSlipForRevision(orderSlipForRevisionResponse);
    }

}