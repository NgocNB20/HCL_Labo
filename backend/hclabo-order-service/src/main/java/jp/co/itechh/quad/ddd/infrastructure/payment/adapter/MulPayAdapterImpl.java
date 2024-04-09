package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IMulPayAdapter;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmDepositedResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultDepositedRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * マルチペイメントアダプタークラス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class MulPayAdapterImpl implements IMulPayAdapter {

    /** マルチペイメントAPI */
    private final MulpayApi mulPayApi;

    /** コンストラクタ */
    @Autowired
    public MulPayAdapterImpl(MulpayApi mulPayApi, HeaderParamsUtility headerParamsUtil) {
        this.mulPayApi = mulPayApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.mulPayApi.getApiClient());
    }

    /**
     * マルチペイメント決済結果が要入金反映処理か確認する
     *
     * @param orderId 受注番号
     * @return MulPayResultConfirmDepositedResponse マルチペイメント決済結果レスポンス
     */
    @Override
    public MulPayResultConfirmDepositedResponse doConfirmDeposited(String orderId) {

        MulPayResultConfirmRequest mulPayResultConfirmRequest = new MulPayResultConfirmRequest();
        mulPayResultConfirmRequest.setOrderCode(orderId);

        return mulPayApi.doConfirmDeposited(mulPayResultConfirmRequest);
    }

    /**
     * 決済代行へ入金を確認する
     *
     * @param orderId 受注番号
     * @return 要入金反映処理フラグ
     */
    @Override
    public Boolean confirmPaymentAgencyResult(String orderId) {

        MulPayResultConfirmRequest mulpayResultConfirmRequest = new MulPayResultConfirmRequest();
        mulpayResultConfirmRequest.setOrderCode(orderId);

        return mulPayApi.doConfirmPaymentAgency(mulpayResultConfirmRequest).getRequiredReflectionProcessingFlag();
    }

    /**
     * マルチペイメント決済結果登録取得
     *
     * @param mulPayResultRequest マルチペイメント決済結果通知受付リクエスト
     * @return 要入金反映処理フラグ
     */
    @Override
    public Boolean mulPayRegistResult(MulPayResultRequest mulPayResultRequest) {

        return mulPayApi.regist(mulPayResultRequest).getRequiredReflectionProcessingFlag();
    }

    /**
     * マルチペイメント決済結果へ入金処理済みを反映
     *
     * @param mulPayResultSeq マルチペイメント決済結果SEQ
     */
    @Override
    public void updateDeposited(Integer mulPayResultSeq) {

        MulPayResultDepositedRequest mulPayResultDepositedRequest = new MulPayResultDepositedRequest();
        mulPayResultDepositedRequest.setMulPayResultSeq(mulPayResultSeq);

        mulPayApi.updateDeposited(mulPayResultDepositedRequest);
    }
}
