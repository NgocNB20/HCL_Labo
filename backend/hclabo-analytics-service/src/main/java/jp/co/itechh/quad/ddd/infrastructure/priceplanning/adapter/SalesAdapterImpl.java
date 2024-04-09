package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 販売アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class SalesAdapterImpl implements ISalesAdapter {

    /**
     * 販売伝票API
     */
    private final SalesSlipApi salesSlipApi;

    /**
     * 販売アダプターHelperクラス
     */
    private final SalesAdapterHelper salesAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param salesSlipApi       販売伝票API
     * @param salesAdapterHelper 販売アダプターHelperクラス
     * @param headerParamsUtil   ヘッダパラメーターユーティル
     */
    @Autowired
    public SalesAdapterImpl(SalesSlipApi salesSlipApi,
                            SalesAdapterHelper salesAdapterHelper,
                            HeaderParamsUtility headerParamsUtil) {
        this.salesSlipApi = salesSlipApi;
        this.salesAdapterHelper = salesAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.salesSlipApi.getApiClient());
    }

    /**
     * 販売企画マイクロサービス
     * 販売伝票取得
     *
     * @param transactionId 取引ID
     * @return SalesSlip 販売伝票
     */
    @Override
    public SalesSlip getSalesSlip(String transactionId) {
        SalesSlipGetRequest request = new SalesSlipGetRequest();
        request.setTransactionId(transactionId);

        SalesSlipResponse response = salesSlipApi.get(request);

        return salesAdapterHelper.toSalesSlip(response);
    }
}