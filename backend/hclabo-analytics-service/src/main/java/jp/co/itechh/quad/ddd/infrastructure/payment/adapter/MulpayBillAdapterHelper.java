package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.ddd.domain.payment.adapter.model.MulpayBill;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

/**
 * マルチペイメントアダプターHelperクラス
 */
@Component
public class MulpayBillAdapterHelper {

    /**
     * マルチペイメントに変換
     *
     * @param response マルチペイメント請求レスポンス
     * @return マルチペイメント
     */
    public MulpayBill toMulpayBill(MulPayBillResponse response) {
        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        MulpayBill mulpayBill = new MulpayBill();
        mulpayBill.setForward(response.getForward());
        mulpayBill.setConvenience(response.getConvenience());

        return mulpayBill;
    }
}
