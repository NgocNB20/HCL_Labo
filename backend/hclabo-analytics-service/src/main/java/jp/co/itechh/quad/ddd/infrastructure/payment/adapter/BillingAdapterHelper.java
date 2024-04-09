package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 請求アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class BillingAdapterHelper {

    /**
     * 請求伝票に変換
     *
     * @param billingSlipResponse 請求伝票レスポンス
     * @return 請求伝票
     */
    public BillingSlip toBillingSlip(BillingSlipResponse billingSlipResponse) {

        if (billingSlipResponse == null) {
            return null;
        }

        BillingSlip billingSlip = new BillingSlip();

        billingSlip.setBillingAddressId(billingSlipResponse.getBillingAddressId());

        if (StringUtils.isNotEmpty(billingSlipResponse.getPaymentMethodId())) {
            billingSlip.setPaymentMethodId(Integer.parseInt(billingSlipResponse.getPaymentMethodId()));
        }
        billingSlip.setPaymentMethodName(billingSlipResponse.getPaymentMethodName());

        if (billingSlipResponse.getPaymentLinkResponse() != null) {
            billingSlip.setLinkPaymentMethodName(billingSlipResponse.getPaymentLinkResponse().getPayTypeName());
            billingSlip.setLinkPayMethod(billingSlipResponse.getPaymentLinkResponse().getPaymethod());
            billingSlip.setPayType(billingSlipResponse.getPaymentLinkResponse().getPayType());
            billingSlip.setLaterDateLimit(billingSlipResponse.getPaymentLinkResponse().getLaterDateLimit());
        }

        billingSlip.setMoneyReceiptTime(billingSlipResponse.getMoneyReceiptTime());

        return billingSlip;
    }

}