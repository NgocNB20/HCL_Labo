/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.mulpay;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import jp.co.itechh.quad.core.logic.multipayment.MulPayShopLogic;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import jp.co.itechh.quad.ddd.usecase.mulpay.PaymentAgencyMulpayConfirmUseCase;
import jp.co.itechh.quad.ddd.usecase.mulpay.ReflectHmDepositedUseCase;
import jp.co.itechh.quad.mulpay.presentation.api.PaymentsApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmDepositedResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultDepositedRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayShopIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * マルチペイメントエンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class MulPayController extends AbstractController implements PaymentsApi {

    /** マルチペイメントショップロジック */
    private final MulPayShopLogic mulPayShopLogic;

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** 決済代行へ入金を確認して結果を登録する ユースケース */
    private final PaymentAgencyMulpayConfirmUseCase paymentAgencyMulpayConfirmUseCase;

    /** HM入金済みをマルペイ決済結果へ反映 */
    private final ReflectHmDepositedUseCase reflectHmDepositedUseCase;

    /** ヘルパー */
    private final MulPayHelper mulPayHelper;

    /** コンストラクタ */
    @Autowired
    public MulPayController(MulPayShopLogic mulPayShopLogic,
                            MulPayProxyService mulPayProxyService,
                            PaymentAgencyMulpayConfirmUseCase paymentAgencyMulpayConfirmUseCase,
                            ReflectHmDepositedUseCase reflectHmDepositedUseCase,
                            MulPayHelper mulPayHelper) {
        this.mulPayShopLogic = mulPayShopLogic;
        this.mulPayProxyService = mulPayProxyService;
        this.paymentAgencyMulpayConfirmUseCase = paymentAgencyMulpayConfirmUseCase;
        this.reflectHmDepositedUseCase = reflectHmDepositedUseCase;
        this.mulPayHelper = mulPayHelper;
    }

    /**
     * GET /payments/mulpay/shopid : マルペイショップID取得
     * マルペイショップID取得
     *
     * @return マルペイショップIDレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<MulPayShopIdResponse> get() {

        Integer shopSeq = 1001;
        String shopId = mulPayShopLogic.getMulPayShopId(shopSeq);

        MulPayShopIdResponse mulpayShopIdResponse = new MulPayShopIdResponse();
        mulpayShopIdResponse.setMulPayShopId(shopId);

        return new ResponseEntity<>(mulpayShopIdResponse, HttpStatus.OK);
    }

    /**
     * GET /payments/mulpay: マルチペイメント請求取得
     * マルチペイメント請求取得
     *
     * @param mulPayBillRequest マルチペイメント請求リクエスト (required)
     * @return マルチペイメント請求レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<MulPayBillResponse> getByOrderCode(
                    @NotNull @ApiParam(value = "マルチペイメント請求リクエスト", required = true) @Valid
                                    MulPayBillRequest mulPayBillRequest) {

        MulPayBillEntity mulPayBillEntity = mulPayProxyService.getMulPayBillByOrderId(mulPayBillRequest.getOrderCode());
        MulPayBillResponse mulpayBillResponse = mulPayHelper.toPaymentMulpayBillResponse(mulPayBillEntity);

        return new ResponseEntity<>(mulpayBillResponse, HttpStatus.OK);
    }

    /**
     * POST /payments/mulpay/result : マルチペイメント決済結果登録
     * マルチペイメント決済結果登録
     *
     * @param mulPayResultRequest マルチペイメント決済結果通知受付リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<MulPayResultResponse> regist(@Valid MulPayResultRequest mulPayResultRequest) {
        MulPayResultResponse mulPayResultResponse = new MulPayResultResponse();

        mulPayResultResponse.setRequiredReflectionProcessingFlag(
                        mulPayProxyService.registMulpayResult(mulPayResultRequest,
                                                              mulPayResultRequest.getReceiveMode()
                                                             ));

        return new ResponseEntity<>(mulPayResultResponse, HttpStatus.OK);
    }

    /**
     * GET /payments/payment-agency/mulpay/result : 決済代行へ入金を確認する
     * 決済代行へ入金を確認する
     *
     * @param mulpayResultConfirmRequest マルチペイメント決済結果確認リクエスト (required)
     * @return 決済結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<MulPayResultResponse> doConfirmPaymentAgency(
                    @NotNull @Valid MulPayResultConfirmRequest mulpayResultConfirmRequest) {
        MulPayResultResponse mulPayResultResponse = new MulPayResultResponse();

        mulPayResultResponse.setRequiredReflectionProcessingFlag(
                        paymentAgencyMulpayConfirmUseCase.confirmPaymentAgencyResultAndRegist(
                                        Objects.requireNonNull(mulpayResultConfirmRequest.getOrderCode())));

        return new ResponseEntity<>(mulPayResultResponse, HttpStatus.OK);
    }

    /**
     * GET /payments/mulpay/result/confirm-deposited : マルチペイメント決済結果が要入金反映処理か確認する
     * マルチペイメント決済結果が要入金反映処理か確認する
     *
     * @param mulpayResultConfirmRequest マルチペイメント決済結果確認リクエスト (required)
     * @return マルチペイメント決済結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<MulPayResultConfirmDepositedResponse> doConfirmDeposited(
                    @NotNull @ApiParam(value = "マルチペイメント決済結果確認リクエスト", required = true) @Valid
                                    MulPayResultConfirmRequest mulpayResultConfirmRequest) {

        MulPayResultEntity mulPayResultEntity =
                        mulPayProxyService.getRequiredReflectionProcessing(mulpayResultConfirmRequest);

        return new ResponseEntity<>(
                        mulPayHelper.toMulPayResultConfirmDepositedResponse(mulPayResultEntity), HttpStatus.OK);
    }

    /**
     * PUT /payments/mulpay/result/reflect-deposited : マルチペイメント決済結果へ入金処理済の更新を行う
     * マルチペイメント決済結果入金反映
     *
     * @param mulPayResultDepositedRequest マルチペイメント決済結果入金反映リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> updateDeposited(
                    @ApiParam(value = "マルチペイメント決済結果入金反映リクエスト", required = true) @Valid @RequestBody
                                    MulPayResultDepositedRequest mulPayResultDepositedRequest) {

        reflectHmDepositedUseCase.reflectHmDeposited(mulPayResultDepositedRequest.getMulPayResultSeq());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}