package jp.co.itechh.quad.ddd.infrastructure.order.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceivedCount;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedCountRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedCountResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 受注アダプター実装クラス
 */
@Component
public class OrderReceivedAdapterImpl implements IOrderReceivedAdapter {

    /**
     * 受注API
     */
    private final OrderReceivedApi orderReceivedApi;

    /**
     * 受注 アダプターHelperクラス
     */
    private final OrderReceivedAdapterHelper orderReceivedAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param orderReceivedApi           受注API
     * @param orderReceivedAdapterHelper 受注 アダプターHelperクラス
     * @param headerParamsUtil           ヘッダパラメーターユーティル
     */
    @Autowired
    public OrderReceivedAdapterImpl(OrderReceivedApi orderReceivedApi,
                                    OrderReceivedAdapterHelper orderReceivedAdapterHelper,
                                    HeaderParamsUtility headerParamsUtil) {
        this.orderReceivedApi = orderReceivedApi;
        this.orderReceivedAdapterHelper = orderReceivedAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderReceivedApi.getApiClient());
    }

    /**
     * 受注件数取得
     *
     * @param orderReceivedId 受注ID
     * @return 受注
     */
    @Override
    public OrderReceived getByOrderReceivedId(String orderReceivedId) {
        OrderReceivedResponse orderReceivedResponse = orderReceivedApi.getByOrderReceivedId(orderReceivedId);

        return orderReceivedAdapterHelper.toOrderReceived(orderReceivedResponse);
    }

    /**
     * 受注件数取得
     *
     * @param transactionId 受注ID
     * @return 受注
     */
    @Override
    public OrderReceived getByTransactionId(String transactionId) {
        GetOrderReceivedRequest orderReceivedRequest = new GetOrderReceivedRequest();
        orderReceivedRequest.setTransactionId(transactionId);
        OrderReceivedResponse orderReceivedResponse = orderReceivedApi.get(orderReceivedRequest);

        return orderReceivedAdapterHelper.toOrderReceived(orderReceivedResponse);
    }

    /**
     * 顧客ごとの受注件数取得
     *
     * @param customerId 顧客ID
     * @return OrderReceivedCount 受注件数
     */
    @Override
    public OrderReceivedCount getOrderReceivedCount(String customerId) {

        GetOrderReceivedCountRequest getOrderReceivedCountRequest = new GetOrderReceivedCountRequest();
        getOrderReceivedCountRequest.setCustomerId(customerId);

        OrderReceivedCountResponse orderReceivedCountResponse =
                        orderReceivedApi.getOrderReceivedCount(getOrderReceivedCountRequest);

        return orderReceivedAdapterHelper.toOrderReceivedCount(orderReceivedCountResponse);
    }
}