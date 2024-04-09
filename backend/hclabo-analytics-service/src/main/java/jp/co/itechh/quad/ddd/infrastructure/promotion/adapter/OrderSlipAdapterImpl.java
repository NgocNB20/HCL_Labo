package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注文アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSlipAdapterImpl implements IOrderSlipAdapter {

    /**
     * 注文票API
     */
    private final OrderSlipApi orderSlipApi;

    /**
     * 注文アダプターHelperクラス
     */
    private final OrderSlipAdapterHelper orderSlipAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param orderSlipApi           注文票API
     * @param orderSlipAdapterHelper 注文アダプターHelperクラス
     * @param headerParamsUtil       ヘッダパラメーターユーティル
     */
    @Autowired
    public OrderSlipAdapterImpl(OrderSlipApi orderSlipApi,
                                OrderSlipAdapterHelper orderSlipAdapterHelper,
                                HeaderParamsUtility headerParamsUtil) {
        this.orderSlipApi = orderSlipApi;
        this.orderSlipAdapterHelper = orderSlipAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderSlipApi.getApiClient());
    }

    /**
     * プロモーションサービス：トランサクションIDで注文票を取得する
     *
     * @param transactionId 取引ID
     * @return OrderSlip
     */
    @Override
    public OrderSlip getOrderSlipByTransactionId(String transactionId) {

        OrderSlipGetRequest orderSlipGetRequest = new OrderSlipGetRequest();
        orderSlipGetRequest.setTransactionId(transactionId);

        OrderSlipResponse orderSlipResponse = orderSlipApi.get(orderSlipGetRequest);

        return orderSlipAdapterHelper.toOrderSlip(orderSlipResponse);
    }

}