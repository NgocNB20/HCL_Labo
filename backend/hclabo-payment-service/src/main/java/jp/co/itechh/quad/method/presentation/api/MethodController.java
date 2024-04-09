/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.method.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;
import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodConfigGetService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodConfigurationCheckService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodEntityListGetService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodOrderDisplayUpdateService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodRegistService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodUpdateService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.ddd.usecase.method.GetCommissionUseCase;
import jp.co.itechh.quad.ddd.usecase.method.GetSelectablePaymentMethodListUseCase;
import jp.co.itechh.quad.ddd.usecase.method.GetSelectablePaymentMethodListUseCaseDto;
import jp.co.itechh.quad.method.presentation.api.param.CommissionGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodCheckRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodLinkListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodRegistRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethodListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 決済方法　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */

@RestController
public class MethodController extends AbstractController implements PaymentsApi {

    /** 決済方法詳細設定取得サービス */
    private final SettlementMethodConfigGetService settlementMethodConfigGetService;

    /** 決済方法 Helper */
    private final MethodHelper methodHelper;

    /** 決済方法取得サービス */
    private final SettlementMethodEntityListGetService settlementMethodEntityListGetService;

    /** 決済方法設定チェックサービス */
    private final SettlementMethodConfigurationCheckService settlementMethodConfigurationCheckService;

    /** 決済方法更新 */
    private final SettlementMethodUpdateService settlementMethodUpdateService;

    /** 決済方法表示順更新サービス */
    private final SettlementMethodOrderDisplayUpdateService settlementMethodOrderDisplayUpdateService;

    /** 決済方法登録サービス */
    private final SettlementMethodRegistService settlementMethodRegistService;

    /** ここからDDD設計範囲 */

    /** 選択可能決済方法一覧取得ユースケース */
    private final GetSelectablePaymentMethodListUseCase getSelectablePaymentMethodListUseCase;

    /** 手数料取得ユースケース */
    private final GetCommissionUseCase getCommissionUseCase;

    /** ここまでDDD設計範囲 */

    /**
     * コンストラクタ
     *
     * @param settlementMethodConfigurationCheckService 決済方法設定チェックサービス
     * @param settlementMethodUpdateService             決済方法更新
     * @param settlementMethodConfigGetService          決済方法詳細設定取得
     * @param settlementMethodOrderDisplayUpdateService 決済方法表示順更新サービス
     * @param settlementMethodRegistService             決済方法登録サービス
     * @param methodHelper                              決済方法 Helper
     * @param settlementMethodEntityListGetService      決済方法取得サービス
     * @param getSelectablePaymentMethodListUseCase     選択可能決済方法一覧取得ユースケース
     * @param getCommissionUseCase                      手数料取得ユースケース
     */
    public MethodController(SettlementMethodConfigurationCheckService settlementMethodConfigurationCheckService,
                            SettlementMethodUpdateService settlementMethodUpdateService,
                            SettlementMethodConfigGetService settlementMethodConfigGetService,
                            SettlementMethodOrderDisplayUpdateService settlementMethodOrderDisplayUpdateService,
                            SettlementMethodRegistService settlementMethodRegistService,
                            MethodHelper methodHelper,
                            SettlementMethodEntityListGetService settlementMethodEntityListGetService,
                            GetSelectablePaymentMethodListUseCase getSelectablePaymentMethodListUseCase,
                            GetCommissionUseCase getCommissionUseCase) {
        this.settlementMethodConfigurationCheckService = settlementMethodConfigurationCheckService;
        this.settlementMethodUpdateService = settlementMethodUpdateService;
        this.settlementMethodConfigGetService = settlementMethodConfigGetService;
        this.settlementMethodOrderDisplayUpdateService = settlementMethodOrderDisplayUpdateService;
        this.settlementMethodRegistService = settlementMethodRegistService;
        this.methodHelper = methodHelper;
        this.settlementMethodEntityListGetService = settlementMethodEntityListGetService;
        this.getSelectablePaymentMethodListUseCase = getSelectablePaymentMethodListUseCase;
        this.getCommissionUseCase = getCommissionUseCase;
    }

    /**
     * POST /payments/methods : 決済方法登録
     * 決済方法登録
     *
     * @param paymentMethodRegistRequest 決済方法登録リクエスト (required)
     * @return 決済方法レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<PaymentMethodResponse> regist(
                    @ApiParam(value = "決済方法登録リクエスト", required = true) @Valid @RequestBody
                                    PaymentMethodRegistRequest paymentMethodRegistRequest) {

        SettlementMethodDto settlementMethodDto = methodHelper.toSettlementMethodDtoRegist(paymentMethodRegistRequest);

        // 決済方法詳細設定チェック
        settlementMethodConfigurationCheckService.execute(settlementMethodDto);

        if (settlementMethodRegistService.execute(settlementMethodDto) == 0) {
            throwMessage("AYC000301");
        }

        // 決済方法DTO取得処理
        SettlementMethodDto settlementMethodRes = settlementMethodConfigGetService.execute(
                        settlementMethodDto.getSettlementMethodEntity().getSettlementMethodSeq());
        PaymentMethodResponse paymentMethodResponse = methodHelper.toPaymentMethodResponse(settlementMethodRes);

        return new ResponseEntity<>(paymentMethodResponse, HttpStatus.OK);
    }

    /**
     * POST /payments/methods/check : 決済方法設定チェック 決済方法設定チェック
     *
     * @param paymentMethodCheckRequest 決済方法チェックリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "決済方法チェックリクエスト", required = true) @Valid @RequestBody
                                                      PaymentMethodCheckRequest paymentMethodCheckRequest) {

        SettlementMethodDto settlementMethodDto = methodHelper.toSettlementMethodDtoCheck(paymentMethodCheckRequest);
        settlementMethodConfigurationCheckService.execute(settlementMethodDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /payments/methods/{settlementMethodSeq} : 決済方法更新 決済方法更新
     *
     * @param settlementMethodSeq        決済方法SEQ (required)
     * @param paymentMethodUpdateRequest 決済方法更新リクエスト (required)
     * @return 決済方法レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<PaymentMethodResponse> update(
                    @ApiParam(value = "決済方法SEQ", required = true) @PathVariable("settlementMethodSeq")
                                    Integer settlementMethodSeq,
                    @ApiParam(value = "決済方法更新リクエスト", required = true) @Valid @RequestBody
                                    PaymentMethodUpdateRequest paymentMethodUpdateRequest) {

        SettlementMethodDto settlementMethodDto =
                        methodHelper.toSettlementMethodDtoUpdate(settlementMethodSeq, paymentMethodUpdateRequest);
        settlementMethodUpdateService.execute(settlementMethodDto);

        // 決済方法DTO取得処理
        SettlementMethodDto settlementMethodRes = settlementMethodConfigGetService.execute(settlementMethodSeq);
        PaymentMethodResponse paymentMethodResponse = methodHelper.toPaymentMethodResponse(settlementMethodRes);

        return new ResponseEntity<>(paymentMethodResponse, HttpStatus.OK);
    }

    /**
     * PUT /payments/methods : 決済方法一覧更新 決済方法一覧更新
     *
     * @param paymentMethodListUpdateRequest 決済方法一覧更新リクエスト (required)
     * @return 決済方法一覧レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<PaymentMethodListResponse> updateList(
                    @ApiParam(value = "決済方法一覧更新リクエスト", required = true) @Valid @RequestBody
                                    PaymentMethodListUpdateRequest paymentMethodListUpdateRequest) {
        // 決済方法リスト取得
        List<SettlementMethodEntity> settlementMethodList =
                        methodHelper.toSettlementMethodEntityList(paymentMethodListUpdateRequest);

        // 更新処理実行
        settlementMethodOrderDisplayUpdateService.execute(settlementMethodList);

        List<SettlementMethodEntity> resultList = new ArrayList<>();
        for (SettlementMethodEntity settlementMethodEntity : settlementMethodList) {
            // 決済方法DTO取得処理
            SettlementMethodDto settlementMethodDto =
                            settlementMethodConfigGetService.execute(settlementMethodEntity.getSettlementMethodSeq());
            if (ObjectUtils.isEmpty(settlementMethodDto)) {
                throwMessage("AYC000203");
            }
            SettlementMethodEntity settlementMethodEntityRes = settlementMethodDto.getSettlementMethodEntity();
            if (ObjectUtils.isNotEmpty(settlementMethodEntityRes)) {
                resultList.add(settlementMethodEntityRes);
            }
        }

        // 決済方法一覧レスポンスに変換
        PaymentMethodListResponse paymentMethodListResponse = methodHelper.toPaymentMethodListResponse(resultList);

        return new ResponseEntity<>(paymentMethodListResponse, HttpStatus.OK);
    }

    /**
     * GET /payments/methods/{settlementMethodSeq} : 決済方法取得
     * 決済方法取得
     *
     * @param settlementMethodSeq 決済方法SEQ (required)
     * @return 決済方法レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<PaymentMethodResponse> getBySettlementMethodSeq(Integer settlementMethodSeq) {
        // 決済方法DTO取得処理
        SettlementMethodDto settlementMethodDto = settlementMethodConfigGetService.execute(settlementMethodSeq);

        if (settlementMethodDto == null) {
            throwMessage("AYC000203");
        }
        // 決済方法レスポンスに変換
        PaymentMethodResponse paymentMethodResponse = methodHelper.toPaymentMethodResponse(settlementMethodDto);

        return new ResponseEntity<>(paymentMethodResponse, HttpStatus.OK);
    }

    /**
     * GET /payments/methods : 決済方法一覧取得
     * 決済方法一覧取得
     *
     * @return 決済方法一覧レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<PaymentMethodListResponse> get() {

        List<SettlementMethodEntity> resultList = settlementMethodEntityListGetService.execute();

        // 決済方法一覧レスポンスに変換
        PaymentMethodListResponse paymentMethodListResponse = methodHelper.toPaymentMethodListResponse(resultList);

        return new ResponseEntity<>(paymentMethodListResponse, HttpStatus.OK);
    }

    /** ここからDDD設計範囲 */

    /**
     * GET /payments/methods/selectable : 選択可能決済方法一覧取得
     *
     * @param selectablePaymentMethodListGetRequest 選択可能決済方法一覧取得リクエスト (required)
     * @return 選択可能決済方法一覧レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<SelectablePaymentMethodListResponse> getSelectable(@NotNull
                                                                             @ApiParam(value = "選択可能決済方法一覧取得リクエスト",
                                                                                       required = true)
                                                                             @Valid SelectablePaymentMethodListGetRequest selectablePaymentMethodListGetRequest) {

        List<GetSelectablePaymentMethodListUseCaseDto> dtoList =
                        getSelectablePaymentMethodListUseCase.getSelectablePaymentMethodList(
                                        selectablePaymentMethodListGetRequest.getTransactionId());

        return new ResponseEntity<>(methodHelper.toSelectablePaymentMethodListResponse(dtoList), HttpStatus.OK);
    }

    /**
     * GET /payments/methods/commission/{paymentMethodId} : 手数料取得
     * パラメータの決済方法ID・計算対象金額をもとに、手数料を取得する
     *
     * @param paymentMethodId 決済方法ID（決済方法SEQ） (required)
     * @param commissionGetRequest 手数料取得リクエスト (required)
     * @return 手数料レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Integer> getCommission(
                    @ApiParam(value = "決済方法ID（決済方法SEQ）", required = true) @PathVariable("paymentMethodId")
                                    String paymentMethodId,
                    @NotNull @ApiParam(value = "手数料取得リクエスト", required = true)
                    @Valid CommissionGetRequest commissionGetRequest) {

        Integer commission = getCommissionUseCase.getCommission(paymentMethodId,
                                                                commissionGetRequest.getPriceForLargeAmountDiscount(),
                                                                commissionGetRequest.getPriceForPriceCommission()
                                                               );

        return new ResponseEntity<>(commission, HttpStatus.OK);
    }

    /**
     * GET /payments/methods/link : リンク決済リスト取得
     *
     * @return リンク決済リストレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<PaymentMethodLinkListResponse> getPaymentsMethodLink() {

        List<SettlementMethodLinkEntity> settlementMethodLinkEntities =
                        settlementMethodEntityListGetService.getPaymentMethodLink();

        PaymentMethodLinkListResponse paymentMethodLinkListResponse =
                        methodHelper.toPaymentMethodLinkListResponse(settlementMethodLinkEntities);

        return new ResponseEntity<>(paymentMethodLinkListResponse, HttpStatus.OK);
    }

    /** ここまでDDD設計範囲 */

}
