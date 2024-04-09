package jp.co.itechh.quad.ddd.domain.gmo.adapter;

import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreateGmoPaymentUrlDto;

import java.util.Map;

/**
 * GMOリンク決済URL取得アダプター
 */
public interface IGetLinkplusUrlPaymentAdapter {

    /** 通信処理中エラー発生時 */
    String MSGCD_LINKPAY_COM_FAIL = "LMC001001";

    /**
     * リンク決済URL取得実施
     *
     * @param urlParam
     * @param transaction
     * @param customer
     * @param settingId
     * @param merpay
     * @return LinkPay取引登録決済出力
     */
    CreateGmoPaymentUrlDto createGetLinkPaymentURL(Map<String, String> urlParam,
                                                   Map<String, String> transaction,
                                                   Map<String, String> customer,
                                                   String settingId,
                                                   Map<String, Object> merpay);
}