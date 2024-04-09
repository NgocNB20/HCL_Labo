package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.CreditPayment;
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

        billingSlip.setBillingSlipId(billingSlipResponse.getBillingSlipId());
        billingSlip.setOrderPaymentId(billingSlipResponse.getOrderPaymentId());
        billingSlip.setBillingAddressId(billingSlipResponse.getBillingAddressId());
        billingSlip.setPaymentMethodId(billingSlipResponse.getPaymentMethodId());

        if (billingSlipResponse.getCreditResponse() != null) {
            CreditPayment creditPayment = new CreditPayment();
            creditPayment.setPaymentToken(billingSlipResponse.getCreditResponse().getPaymentToken());
            creditPayment.setPaymentType(billingSlipResponse.getCreditResponse().getPaymentType());
            creditPayment.setDividedNumber(billingSlipResponse.getCreditResponse().getDividedNumber());
            creditPayment.setExpirationMonth(billingSlipResponse.getCreditResponse().getExpirationMonth());
            creditPayment.setRegistCardFlag(
                            Boolean.TRUE.equals(billingSlipResponse.getCreditResponse().getRegistCardFlag()));
            creditPayment.setUseRegistedCardFlag(
                            Boolean.TRUE.equals(billingSlipResponse.getCreditResponse().getUseRegistedCardFlag()));
            creditPayment.setExpirationYear(billingSlipResponse.getCreditResponse().getExpirationYear());
            creditPayment.setRegistedCardMaskNo(billingSlipResponse.getCreditResponse().getCardMaskNo());
            billingSlip.setCreditPayment(creditPayment);
        }

        return billingSlip;
    }

}