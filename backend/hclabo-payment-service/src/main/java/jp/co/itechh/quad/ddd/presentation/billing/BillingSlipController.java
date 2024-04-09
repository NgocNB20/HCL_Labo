/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.billing;

import com.gmo_pg.g_pay.client.output.SecureTran2Output;
import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.billingslip.presentation.api.PaymentsApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipAddressUpdateRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipCheckRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionCancelRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionCancelResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionDepositedRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionDepositedResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionOpenSalesRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionOpenSalesResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipMethodUpdateRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipModernizeRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipOpenRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipOpenResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipRegistRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.CheckBillingSlipForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.GetBillingSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.OpenBillingSlipReviseRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.OpenBillingSlipReviseResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyEntrustRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyEntrustSecureResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyInterimEntrustRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentInterimRestoreRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.PublishBillingSlipForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.ReAuthBillingSlipForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.ReleasePaymentAgencyForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.SettingBillingAddressForRevisionRequest;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.common.CheckMessageDto;
import jp.co.itechh.quad.core.utility.CommunicateUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.billing.entity.ExecutePaymentRequestDto;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.usecase.billing.CancelBillingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.CancelBillingSlipForRevisionUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.CheckBillingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.CheckBillingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.DepositedBillingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.DepositedBillingSlipForRevisionUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.DoReAuthForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.EntrustInterimPaymentAgencyUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.EntrustPaymentAgencyUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.GetBillingSlipByTransactionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.GetBillingSlipByTransactionIdUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.GetBillingSlipForRevisionByTransactionRevisionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.ModernizeBillingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.OpenBillingSlipReviseUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.OpenBillingSlipReviseUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.OpenBillingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.OpenBillingSlipUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.billing.OpenSalesForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.PublishBillingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.PublishBillingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.RestoreInterimPaymentUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.SettingBillingAddressForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.SettingBillingAddressUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.SettingPaymentAgencyReleaseForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.billing.SettingPaymentMethodUseCase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 請求伝票エンドポイント Controller
 */
@RestController
public class BillingSlipController extends AbstractController implements PaymentsApi {

    /** 請求伝票発行ユースケース */
    private final PublishBillingSlipUseCase publishBillingSlipUseCase;

    /** 請求伝票最新化ユースケース */
    private final ModernizeBillingSlipUseCase modernizeBillingSlipUseCase;

    /** 請求伝票チェックユースケース */
    private final CheckBillingSlipUseCase checkBillingSlipUseCase;

    /** 取引に紐づく請求伝票取得ユースケース */
    private final GetBillingSlipByTransactionIdUseCase getBillingSlipByTransactionIdUseCase;

    /** 請求先設定ユースケース */
    private final SettingBillingAddressUseCase settingBillingAddressUseCase;

    /** 決済方法設定ユースケース */
    private final SettingPaymentMethodUseCase settingPaymentMethodUseCase;

    /** 決済代行請求委託（注文決済確定）ユースケース */
    private final EntrustPaymentAgencyUseCase entrustPaymentAgencyUseCase;

    /** 途中決済請求委託ユースケース */
    private final EntrustInterimPaymentAgencyUseCase entrustInterimPaymentAgencyUseCase;

    /** 請求伝票確定ユースケース */
    private final OpenBillingSlipUseCase openBillingSlipUseCase;

    /** 改訂用請求伝票取消ユースケース */
    private final CancelBillingSlipForRevisionUseCase cancelBillingSlipForRevisionUseCase;

    /** 途中決済の取消ユースケース */
    private final RestoreInterimPaymentUseCase restoreInterimPaymentUseCase;

    /** 改訂用売上確定ユースケース */
    private final OpenSalesForRevisionUseCase openSalesForRevisionUseCase;

    /** ヘルパー */
    private final BillingSlipHelper billingSlipHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 改訂用請求伝票チェック　ユースケース */
    private final PublishBillingSlipForRevisionUseCase publishBillingSlipForRevisionUseCase;

    /** 改訂用請求伝票チェック　ユースケース */
    private final CheckBillingSlipForRevisionUseCase checkBillingSlipForRevisionUseCase;

    /** 改訂用取引に紐づく改訂用請求伝票取得ユースケース */
    private final GetBillingSlipForRevisionByTransactionRevisionIdUseCase
                    getBillingSlipForRevisionByTransactionRevisionIdUseCase;

    /** 改訂用請求伝票確定ユースケース */
    private final OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase;

    /** 改訂用請求伝票 請求先更新ユースケース */
    private final SettingBillingAddressForRevisionUseCase settingBillingAddressForRevisionUseCase;

    /** 改訂用請求伝票再オーソリ実行ユースケース */
    private final DoReAuthForRevisionUseCase doReAuthForRevisionUseCase;

    /** 改訂用請求伝票決済代行連携解除フラグ設定ユースケース */
    private final SettingPaymentAgencyReleaseForRevisionUseCase settingPaymentAgencyReleaseForRevisionUseCase;

    /** 改訂用請求伝票入金ユースケース */
    private final DepositedBillingSlipForRevisionUseCase depositedBillingSlipForRevisionUseCase;

    /** コンストラクタ */
    @Autowired
    public BillingSlipController(PublishBillingSlipUseCase publishBillingSlipUseCase,
                                 ModernizeBillingSlipUseCase modernizeBillingSlipUseCase,
                                 CheckBillingSlipUseCase checkBillingSlipUseCase,
                                 GetBillingSlipByTransactionIdUseCase getBillingSlipByTransactionIdUseCase,
                                 SettingBillingAddressUseCase settingBillingAddressUseCase,
                                 SettingPaymentMethodUseCase settingPaymentMethodUseCase,
                                 EntrustPaymentAgencyUseCase entrustPaymentAgencyUseCase,
                                 EntrustInterimPaymentAgencyUseCase entrustInterimPaymentAgencyUseCase,
                                 OpenBillingSlipUseCase openBillingSlipUseCase,
                                 CancelBillingSlipForRevisionUseCase cancelBillingSlipForRevisionUseCase,
                                 RestoreInterimPaymentUseCase restoreInterimPaymentUseCase,
                                 OpenSalesForRevisionUseCase openSalesForRevisionUseCase,
                                 BillingSlipHelper billingSlipHelper,
                                 HeaderParamsUtility headerParamsUtil,
                                 PublishBillingSlipForRevisionUseCase publishBillingSlipForRevisionUseCase,
                                 CheckBillingSlipForRevisionUseCase checkBillingSlipForRevisionUseCase,
                                 GetBillingSlipForRevisionByTransactionRevisionIdUseCase getBillingSlipForRevisionByTransactionRevisionIdUseCase,
                                 OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase,
                                 SettingBillingAddressForRevisionUseCase settingBillingAddressForRevisionUseCase,
                                 DoReAuthForRevisionUseCase doReAuthForRevisionUseCase,
                                 SettingPaymentAgencyReleaseForRevisionUseCase settingPaymentAgencyReleaseForRevisionUseCase,
                                 DepositedBillingSlipForRevisionUseCase depositedBillingSlipForRevisionUseCase) {
        this.publishBillingSlipUseCase = publishBillingSlipUseCase;
        this.modernizeBillingSlipUseCase = modernizeBillingSlipUseCase;
        this.checkBillingSlipUseCase = checkBillingSlipUseCase;
        this.getBillingSlipByTransactionIdUseCase = getBillingSlipByTransactionIdUseCase;
        this.settingBillingAddressUseCase = settingBillingAddressUseCase;
        this.settingPaymentMethodUseCase = settingPaymentMethodUseCase;
        this.entrustPaymentAgencyUseCase = entrustPaymentAgencyUseCase;
        this.entrustInterimPaymentAgencyUseCase = entrustInterimPaymentAgencyUseCase;
        this.openBillingSlipUseCase = openBillingSlipUseCase;
        this.cancelBillingSlipForRevisionUseCase = cancelBillingSlipForRevisionUseCase;
        this.restoreInterimPaymentUseCase = restoreInterimPaymentUseCase;
        this.openSalesForRevisionUseCase = openSalesForRevisionUseCase;
        this.billingSlipHelper = billingSlipHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.publishBillingSlipForRevisionUseCase = publishBillingSlipForRevisionUseCase;
        this.checkBillingSlipForRevisionUseCase = checkBillingSlipForRevisionUseCase;
        this.getBillingSlipForRevisionByTransactionRevisionIdUseCase =
                        getBillingSlipForRevisionByTransactionRevisionIdUseCase;
        this.openBillingSlipReviseUseCase = openBillingSlipReviseUseCase;
        this.settingBillingAddressForRevisionUseCase = settingBillingAddressForRevisionUseCase;
        this.doReAuthForRevisionUseCase = doReAuthForRevisionUseCase;
        this.settingPaymentAgencyReleaseForRevisionUseCase = settingPaymentAgencyReleaseForRevisionUseCase;
        this.depositedBillingSlipForRevisionUseCase = depositedBillingSlipForRevisionUseCase;
    }

    /**
     * GET /payments/billing-slips : 請求伝票取得
     *
     * @param billingSlipGetRequest 請求伝票取得リクエスト (required)
     * @return 請求伝票レスポンス (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<BillingSlipResponse> get(@NotNull @ApiParam(value = "請求伝票取得リクエスト", required = true) @Valid
                                                                   BillingSlipGetRequest billingSlipGetRequest) {

        GetBillingSlipByTransactionIdUseCaseDto dto =
                        getBillingSlipByTransactionIdUseCase.getBillingSlipByTransactionId(
                                        billingSlipGetRequest.getTransactionId());

        return new ResponseEntity<>(this.billingSlipHelper.toBillingSlipResponse(dto), HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips : 請求伝票発行（取引開始）
     * 下書き状態の請求伝票発行し、取引（注文フロー）開始する&lt;br&gt;取引サービスの取引開始API内から本APIが呼ばれて、取引が開始される
     *
     * @param addressId                請求伝票発行（取引開始）リクエストヘッダー&lt;br/&gt;暫定対応のため、住所IDのみ定義&lt;br/&gt;フェーズ3では、IDトークンでその他の顧客情報も含まれる想定 (required)
     * @param billingSlipRegistRequest 請求伝票発行（取引開始）リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> regist(@ApiParam(value = "請求伝票発行（取引開始）リクエストヘッダー 住所ID", required = true)
                                       @RequestHeader(value = "addressId", required = true) String addressId,
                                       @ApiParam(value = "請求伝票発行（取引開始）リクエスト", required = true) @Valid @RequestBody
                                                       BillingSlipRegistRequest billingSlipRegistRequest) {

        publishBillingSlipUseCase.publishBillingSlip(getCustomerId(), addressId,
                                                     billingSlipRegistRequest.getTransactionId(),
                                                     billingSlipRegistRequest.getOrderCode()
                                                    );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /payments/billing-slips/address : 請求先設定
     * 請求先となる住所情報を請求伝票に設定する
     *
     * @param billingSlipAddressUpdateRequest 請求先設定リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> updateAddress(@ApiParam(value = "請求先設定リクエスト", required = true) @Valid @RequestBody
                                                              BillingSlipAddressUpdateRequest billingSlipAddressUpdateRequest) {

        settingBillingAddressUseCase.settingBillingAddress(billingSlipAddressUpdateRequest.getTransactionId(),
                                                           billingSlipAddressUpdateRequest.getBillingAddressId()
                                                          );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /payments/billing-slips/methods : 決済方法設定
     *
     * @param billingSlipMethodUpdateRequest 決済方法設定リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> updateMethod(@ApiParam(value = "決済方法設定リクエスト", required = true) @Valid @RequestBody
                                                             BillingSlipMethodUpdateRequest billingSlipMethodUpdateRequest) {

        this.settingPaymentMethodUseCase.settingPaymentMethod(billingSlipMethodUpdateRequest.getTransactionId(),
                                                              billingSlipMethodUpdateRequest.getPaymentMethodId(),
                                                              billingSlipMethodUpdateRequest.getPaymentToken(),
                                                              billingSlipMethodUpdateRequest.getMaskedCardNo(),
                                                              billingSlipMethodUpdateRequest.getExpirationMonth(),
                                                              billingSlipMethodUpdateRequest.getExpirationYear(),
                                                              billingSlipMethodUpdateRequest.getPaymentType(),
                                                              billingSlipMethodUpdateRequest.getDividedNumber(),
                                                              setDefaultBoolean(
                                                                              billingSlipMethodUpdateRequest.getRegistCardFlag()),
                                                              setDefaultBoolean(
                                                                              billingSlipMethodUpdateRequest.getUseRegistedCardFlag())
                                                             );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/modernize : 請求伝票最新化
     * 請求伝票の最新化を行う
     *
     * @param billingSlipModernizeRequest 請求伝票最新化リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> modernize(@ApiParam(value = "請求伝票最新化リクエスト", required = true) @Valid @RequestBody
                                                          BillingSlipModernizeRequest billingSlipModernizeRequest) {

        modernizeBillingSlipUseCase.modernizeBillingSlip(billingSlipModernizeRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/check : 請求伝票チェック
     * 請求伝票のチェックを行う
     *
     * @param billingSlipCheckRequest 請求伝票チェックリクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "請求伝票チェックリクエスト", required = true) @Valid @RequestBody
                                                      BillingSlipCheckRequest billingSlipCheckRequest) {

        checkBillingSlipUseCase.checkBillingSlip(billingSlipCheckRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/payment-agency/entrust : 決済代行に請求委託（注文決済確定）
     * 決済代行に請求委託を実行し、注文決済の取引を確定する&lt;br&gt;クレジット本人認証が必要な場合は、注文決済確定は行わず、ステータスコード202でそのパラメータを返却する&lt;br&gt;本人認証後は再度本APIを呼び出す必要がある
     *
     * @param paymentAgencyEntrustRequest 決済代行委託リクエスト (required)
     * @return 成功 (status code 200)
     * or 3Dセキュア認証が必要（決済代行本人認証レスポンス） (status code 202)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity entrustPaymentAgency(@ApiParam(value = "決済代行委託リクエスト", required = true) @RequestBody
                                                               PaymentAgencyEntrustRequest paymentAgencyEntrustRequest) {

        ExecutePaymentRequestDto executePaymentRequestSecureDto =
                        entrustPaymentAgencyUseCase.entrustPaymentAgency(getCustomerId(),
                                                                         paymentAgencyEntrustRequest.getTransactionId(),
                                                                         paymentAgencyEntrustRequest.getCallBackType(),
                                                                         paymentAgencyEntrustRequest.getCreditTdResultReceiveUrl(),
                                                                         paymentAgencyEntrustRequest.getSecurityCode(),
                                                                         paymentAgencyEntrustRequest.getPaymentLinkReturnUrl()
                                                                        );

        if (executePaymentRequestSecureDto == null) {
            // 決済要求情報がない場合は、決済委託完了

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 決済要求情報がある場合

            PaymentAgencyEntrustSecureResponse response = new PaymentAgencyEntrustSecureResponse();

            if (!StringUtils.isBlank(executePaymentRequestSecureDto.getSecureRedirectUrl())) {
                response.setRedirectUrl(executePaymentRequestSecureDto.getSecureRedirectUrl());
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            } else if (!StringUtils.isBlank(executePaymentRequestSecureDto.getLinkPayRedirectUrl())) {
                response.setRedirectUrl(executePaymentRequestSecureDto.getLinkPayRedirectUrl());
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                throw new NullPointerException("RedirectUrl of ExecutePaymentRequestDto is null");
            }
        }
    }

    /**
     * POST /payments/billing-slips/payment-agency/interim/entrust : 途中決済を決済代行に請求委託
     * クレジット本人認証が必要だった場合の途中決済を決済代行に請求委託を実行する
     *
     * @param paymentAgencyInterimEntrustRequest 途中決済の決済代行委託リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity entrustInterimPaymentAgency(
                    @ApiParam(value = "途中決済の決済代行委託リクエスト", required = true) @Valid @RequestBody
                                    PaymentAgencyInterimEntrustRequest paymentAgencyInterimEntrustRequest) {

        SecureTran2Output secureTran2Output = entrustInterimPaymentAgencyUseCase.entrustInterimPaymentAgency(
                        paymentAgencyInterimEntrustRequest.getAccessId());

        CommunicateUtility communicateUtility = ApplicationContextUtility.getBean(CommunicateUtility.class);
        List<CheckMessageDto> errList = communicateUtility.checkOutput(secureTran2Output);

        if (!CollectionUtils.isEmpty(errList)) {
            // GMOからは複数メッセージが返却されるためApplicationExceptionにセット
            ApplicationException appException = new ApplicationException();
            for (CheckMessageDto err : errList) {
                appException.addMessage(
                                err.getMessageId(), err.getArgs() != null ?
                                                Arrays.stream(err.getArgs()).toArray(String[]::new) :
                                                null);
            }
            throw appException;
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * POST /payments/billing-slips/payment/interim/restore : 途中決済を復元
     * クレジット決済の登録・実行・3Dセキュア認証後の実行でエラー（キャンセル含む）時に、再決済できるように途中決済を復元する
     *
     * @param paymentInterimRestoreRequest 途中決済復元リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> restoreInterimPayment(
                    @ApiParam(value = "途中決済復元リクエスト", required = true) @Valid @RequestBody
                                    PaymentInterimRestoreRequest paymentInterimRestoreRequest) {

        restoreInterimPaymentUseCase.restoreInterimPayment(
                        paymentInterimRestoreRequest.getTransactionId(), paymentInterimRestoreRequest.getOrderCode());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/open : 請求伝票確定
     * 請求伝票を確定する
     *
     * @param billingSlipOpenRequest 請求伝票確定リクエスト (required)
     * @return 請求伝票確定レスポンス (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<BillingSlipOpenResponse> open(
                    @ApiParam(value = "請求伝票確定リクエスト", required = true) @Valid @RequestBody
                                    BillingSlipOpenRequest billingSlipOpenRequest) {

        OpenBillingSlipUseCaseDto dto =
                        openBillingSlipUseCase.openBillingSlip(billingSlipOpenRequest.getTransactionId());

        return new ResponseEntity<>(billingSlipHelper.toBillingSlipOpenResponse(dto), HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/for-revision/cancel : 改訂用請求伝票取消
     * 改訂用請求伝票を取消する
     *
     * @param billingSlipForRevisionCancelRequest 改訂用請求伝票取消リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<BillingSlipForRevisionCancelResponse> cancelForRevision(
                    @ApiParam(value = "改訂用請求伝票取消リクエスト", required = true) @Valid @RequestBody
                                    BillingSlipForRevisionCancelRequest billingSlipForRevisionCancelRequest) {

        CancelBillingSlipForRevisionUseCaseDto dto = cancelBillingSlipForRevisionUseCase.cancelBillingSlip(
                        billingSlipForRevisionCancelRequest.getTransactionRevisionId(),
                        Boolean.TRUE.equals(billingSlipForRevisionCancelRequest.getRevisionOpenFlag()),
                        getAdministratorId()
                                                                                                          );

        BillingSlipForRevisionCancelResponse cancelResponse = null;
        if (dto != null) {
            cancelResponse = new BillingSlipForRevisionCancelResponse();
            cancelResponse.setPaidFlag(dto.isPaidFlag());
            cancelResponse.setInsufficientMoneyFlag(dto.isInsufficientMoneyFlag());
            cancelResponse.setOverFlag(dto.isOverMoneyFlag());
            cancelResponse.setGmoCommunicationFlag(dto.isGmoCommunicationFlag());
            cancelResponse.setWarningMessage(dto.getWarningMessage());
        }

        return new ResponseEntity<>(cancelResponse, HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/for-revision/open-sales : 改訂用請求伝票売上確定
     * 後払い決済の売上を確定する&lt;br&gt;出荷時に呼び出して、改訂用売上確定する（別途、改訂伝票の確定が必要）
     *
     * @param billingSlipForRevisionOpenSalesRequest 請求伝票売上確定リクエスト (required)
     * @return 改訂用請求伝票売上確定レスポンス (status code 200)  or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<BillingSlipForRevisionOpenSalesResponse> openSalesForRevision(
                    @ApiParam(value = "改訂用請求伝票売上確定リクエスト", required = true) @Valid @RequestBody
                                    BillingSlipForRevisionOpenSalesRequest billingSlipForRevisionOpenSalesRequest) {

        BillingSlipForRevisionOpenSalesResponse response = openSalesForRevisionUseCase.openSalesForRevision(
                        billingSlipForRevisionOpenSalesRequest.getTransactionRevisionId(),
                        Boolean.TRUE.equals(billingSlipForRevisionOpenSalesRequest.getRevisionOpenFlag()),
                        getAdministratorId()
                                                                                                           );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/for-revision/check : 改訂用請求伝票チェック
     * 改訂用請求伝票チェック
     *
     * @param checkBillingSlipForRevisionRequest 改訂用請求伝票チェックリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> checkBillingSlipForRevision(
                    @ApiParam(value = "改訂用請求伝票チェックリクエスト", required = true) @Valid @RequestBody
                                    CheckBillingSlipForRevisionRequest checkBillingSlipForRevisionRequest) {

        checkBillingSlipForRevisionUseCase.checkBillingSlipForRevision(
                        checkBillingSlipForRevisionRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * GET /payments/billing-slips/for-revision : 改訂用取引に紐づく改訂用請求伝票取得
     * 改訂用取引に紐づく改訂用請求伝票取得
     *
     * @param billingSlipGetRequest 改訂用取引に紐づく改訂用請求伝票取得リクエスト (required)
     * @return 改訂用取引に紐づく改訂用請求伝票レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<BillingSlipForRevisionByTransactionRevisionIdResponse> getBillingSlipForRevisionByTransactionRevisionId(
                    @NotNull @ApiParam(value = "改訂用取引に紐づく改訂用請求伝票取得リクエスト", required = true) @Valid
                                    GetBillingSlipForRevisionByTransactionRevisionIdRequest billingSlipGetRequest) {

        GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto response =
                        getBillingSlipForRevisionByTransactionRevisionIdUseCase.getBillingSlipForRevisionByTransactionRevisionId(
                                        billingSlipGetRequest.getTransactionRevisionId());

        return new ResponseEntity<>(billingSlipHelper.toBillingSlipForRevisionByTransactionRevisionIdResponse(response),
                                    HttpStatus.OK
        );

    }

    /**
     * POST /payments/billing-slips/for-revision/open : 改訂用請求伝票確定
     * 改訂用請求伝票確定
     *
     * @param openBillingSlipReviseRequest 改訂用請求伝票確定リクエスト (required)
     * @return 改訂用請求伝票確定レスポンス (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OpenBillingSlipReviseResponse> openBillingSlipRevise(
                    @ApiParam(value = "改訂用請求伝票確定リクエスト", required = true) @Valid @RequestBody
                                    OpenBillingSlipReviseRequest openBillingSlipReviseRequest) {

        OpenBillingSlipReviseResponse response = new OpenBillingSlipReviseResponse();

        OpenBillingSlipReviseUseCaseDto openBillingSlipReviseUseCaseDto =
                        openBillingSlipReviseUseCase.openBillingSlipRevise(
                                        openBillingSlipReviseRequest.getTransactionRevisionId(),
                                        !Boolean.FALSE.equals(openBillingSlipReviseRequest.getSettlementSkipFlag()),
                                        getAdministratorId()
                                                                          );

        response.setGmoCommunicationFlag(openBillingSlipReviseUseCaseDto.isGmoCommunicationExec());
        response.setBillPaymentErrorReleaseFlag(openBillingSlipReviseUseCaseDto.isBillPaymentErrorReleaseFlag());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * PUT /payments/billing-slips/for-revision/billing-address : 改訂用請求先更新
     * 改訂用請求先更新
     *
     * @param settingBillingAddressForRevisionRequest 改訂用請求先更新リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> settingBillingAddressForRevision(
                    @ApiParam(value = "改訂用請求伝票　請求先更新リクエスト", required = true) @Valid @RequestBody
                                    SettingBillingAddressForRevisionRequest settingBillingAddressForRevisionRequest) {

        settingBillingAddressForRevisionUseCase.settingBillingAddressForRevision(
                        settingBillingAddressForRevisionRequest.getTransactionRevisionId(),
                        settingBillingAddressForRevisionRequest.getBillingAddressId()
                                                                                );

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * POST /payments/billing-slips/for-revision : 改訂用請求伝票を発行する
     * 改訂用請求伝票を発行する
     *
     * @param publishBillingSlipForRevisionRequest 改訂用請求伝票確定リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> publishBillingSlipForRevision(
                    @Valid PublishBillingSlipForRevisionRequest publishBillingSlipForRevisionRequest) {

        publishBillingSlipForRevisionUseCase.publishBillingSlipForRevision(
                        publishBillingSlipForRevisionRequest.getTransactionId(),
                        publishBillingSlipForRevisionRequest.getTransactionRevisionId()
                                                                          );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/for-revision/re-auth : 改訂用請求伝票再オーソリ
     * 改訂用請求伝票の再オーソリを実行する
     *
     * @param reAuthBillingSlipForRevisionRequest 改訂用請求伝票再オーソリリクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> doReAuthBillingSlipForRevision(
                    @ApiParam(value = "改訂用請求伝票再オーソリリクエスト", required = true) @Valid @RequestBody
                                    ReAuthBillingSlipForRevisionRequest reAuthBillingSlipForRevisionRequest) {

        doReAuthForRevisionUseCase.doReAuth(
                        reAuthBillingSlipForRevisionRequest.getTransactionRevisionId(),
                        reAuthBillingSlipForRevisionRequest.getRevisionOpenFlag()
                                           );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /payments/billing-slips/for-revision/release-payment-agency : 改訂用請求伝票連携解除
     * 改訂用請求伝票の決済代行連携解除を設定する
     *
     * @param releasePaymentAgencyForRevisionRequest 改訂用請求伝票決済代行連携解除リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> settingReleasePaymentAgencyBillingSlipForRevision(
                    @ApiParam(value = "改訂用請求伝票決済代行連携解除リクエスト", required = true) @Valid @RequestBody
                                    ReleasePaymentAgencyForRevisionRequest releasePaymentAgencyForRevisionRequest) {

        settingPaymentAgencyReleaseForRevisionUseCase.settingPaymentAgencyReleaseForRevision(
                        releasePaymentAgencyForRevisionRequest.getTransactionRevisionId(),
                        setDefaultBoolean(releasePaymentAgencyForRevisionRequest.getPaymentAgencyReleaseFlag())
                                                                                            );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 顧客IDを取得する
     *
     * @return customerId 顧客ID
     */
    private String getCustomerId() {
        return this.headerParamsUtil.getMemberSeq();
    }

    /**
     * 管理者SEQを取得する
     *
     * @return AdminSeq 管理者ID
     */
    private String getAdministratorId() {
        return this.headerParamsUtil.getAdministratorSeq();
    }

    /**
     * Boolean(null)にboolean(false)をセット<br/>
     * Booleanで宣言されている項目がNullであり、UCメソッドでbooleanの場合に変換する
     *
     * @param target API引数に指定されたBoolean項目
     * @return targetがnullの場合false / 指定されている場合booleanに変換
     */
    private boolean setDefaultBoolean(Boolean target) {

        if (target == null) {
            return false;
        }

        return target;
    }

    /**
     * PUT /payments/billing-slips/for-revision/deposited : 改訂用請求伝票を入金済みにする
     *
     * @param billingSlipForRevisionDepositedRequest 改訂用請求伝票を入金済リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<BillingSlipForRevisionDepositedResponse> depositedForRevision(
                    @ApiParam(value = "改訂用請求伝票を入金済リクエスト", required = true) @Valid @RequestBody
                                    BillingSlipForRevisionDepositedRequest billingSlipForRevisionDepositedRequest) {

        BillingSlipForRevisionDepositedResponse response = null;

        // 改訂用請求伝票を入金済みにする
        DepositedBillingSlipForRevisionUseCaseDto dto =
                        depositedBillingSlipForRevisionUseCase.depositedBillingSlipForRevision(
                                        billingSlipForRevisionDepositedRequest.getTransactionRevisionId(),
                                        billingSlipForRevisionDepositedRequest.getMulPayResultSeq()
                                                                                              );
        if (dto != null) {
            response = new BillingSlipForRevisionDepositedResponse();
            response.setInsufficientMoneyFlag(dto.isInsufficientMoneyFlag());
            response.setOverMoneyFlag(dto.isOverMoneyFlag());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}