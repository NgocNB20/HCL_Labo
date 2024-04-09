/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.sales;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.usecase.sales.AddAdjustmentAmountToSalesSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.ApplyCouponUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.CalcAndCheckSalesSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.CalcAndCheckSalesSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.CancelCouponUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.CancelSalesSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.GetSalesSlipByTransactionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.GetSalesSlipForRevisionByTransactionRevisionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.ModernizeSalesSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.OpenSalesSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.OpenSalesSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.PublishSalesSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.PublishSalesSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.UpdateCouponUseFlagForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.sales.UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase;
import jp.co.itechh.quad.salesslip.presentation.api.PricePlanningApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.AddAdjustmentAmountToSalesSlipForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.ApplyOriginCommissionAndCarriageForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.CalcAndCheckSalesSlipForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.CalcAndCheckSalesSlipForRevisionResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCheckRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCouponApplyRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCouponCancelRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipForRevisionCancelRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipModernizeRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipOpenForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipOpenRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipPublishForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipRegistRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.UpdateCouponUseFlagOfSalesSlipForRevisionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 販売伝票エンドポイント Controller
 */
@RestController
public class SalesSlipController extends AbstractController implements PricePlanningApi {

    /** 改改訂用販売伝票のクーポン利用制限フラグ更新 ユースケース */
    private final AddAdjustmentAmountToSalesSlipForRevisionUseCase addAdjustmentAmountToSalesSlipForRevisionUseCase;

    /** クーポン適用 ユースケース */
    private final ApplyCouponUseCase applyCouponUseCase;

    /** 販売伝票最新化ユースケース */
    private final ModernizeSalesSlipUseCase modernizeSalesSlipUseCase;

    /** 改訂用販売伝票計算&チェックユースケース */
    private final CalcAndCheckSalesSlipForRevisionUseCase calcAndCheckSalesSlipForRevisionUseCase;

    /** 販売伝票計算&チェックユースケース */
    private final CalcAndCheckSalesSlipUseCase calcAndCheckSalesSlipUseCase;

    /** クーポン取消 ユースケース */
    private final CancelCouponUseCase cancelCouponUseCase;

    /** 販売伝票取消 ユースケース */
    private final CancelSalesSlipForRevisionUseCase cancelSalesSlipUseCase;

    /** 取引に紐づく販売伝票取得 ユースケース */
    private final GetSalesSlipByTransactionIdUseCase getSalesSlipByTransactionIdUseCase;

    /** 改訂用取引IDに紐づく改訂用販売伝票取得 ユースケース */
    private final GetSalesSlipForRevisionByTransactionRevisionIdUseCase
                    getSalesSlipForRevisionByTransactionRevisionIdUseCase;

    /** 改訂用販売伝票を確定する ユースケース */
    private final OpenSalesSlipForRevisionUseCase openSalesSlipForRevisionUseCase;

    /** 販売伝票確定 ユースケース */
    private final OpenSalesSlipUseCase openSalesSlipUseCase;

    /** 改訂用販売伝票発行 ユースケース */
    private final PublishSalesSlipForRevisionUseCase publishSalesSlipForRevisionUseCase;

    /** 販売伝票発行ユースケース */
    private final PublishSalesSlipUseCase publishSalesSlipUseCase;

    /** 改訂用販売伝票のクーポン利用制限フラグ更新 ユースケース */
    private final UpdateCouponUseFlagForRevisionUseCase updateCouponUseFlagForRevisionUseCase;

    /** 改訂用販売伝票の改訂前手数料/送料適用フラグ更新 ユースケース */
    private final UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase
                    updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase;

    /** Helperクラス */
    private final SalesSlipHelper salesSlipHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /**
     * コンストラクタ
     */
    @Autowired
    public SalesSlipController(AddAdjustmentAmountToSalesSlipForRevisionUseCase addAdjustmentAmountToSalesSlipForRevisionUseCase,
                               ApplyCouponUseCase applyCouponUseCase,
                               ModernizeSalesSlipUseCase modernizeSalesSlipUseCase,
                               CalcAndCheckSalesSlipForRevisionUseCase calcAndCheckSalesSlipForRevisionUseCase,
                               CalcAndCheckSalesSlipUseCase calcAndCheckSalesSlipUseCase,
                               CancelCouponUseCase cancelCouponUseCase,
                               CancelSalesSlipForRevisionUseCase cancelSalesSlipUseCase,
                               GetSalesSlipByTransactionIdUseCase getSalesSlipByTransactionIdUseCase,
                               GetSalesSlipForRevisionByTransactionRevisionIdUseCase getSalesSlipForRevisionByTransactionRevisionIdUseCase,
                               OpenSalesSlipForRevisionUseCase openSalesSlipForRevisionUseCase,
                               OpenSalesSlipUseCase openSalesSlipUseCase,
                               PublishSalesSlipForRevisionUseCase publishSalesSlipForRevisionUseCase,
                               PublishSalesSlipUseCase publishSalesSlipUseCase,
                               UpdateCouponUseFlagForRevisionUseCase updateCouponUseFlagOfSalesSlipForRevisionUseCase,
                               UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase,
                               SalesSlipHelper salesSlipHelper,
                               HeaderParamsUtility headerParamsUtil) {
        this.addAdjustmentAmountToSalesSlipForRevisionUseCase = addAdjustmentAmountToSalesSlipForRevisionUseCase;
        this.applyCouponUseCase = applyCouponUseCase;
        this.modernizeSalesSlipUseCase = modernizeSalesSlipUseCase;
        this.calcAndCheckSalesSlipForRevisionUseCase = calcAndCheckSalesSlipForRevisionUseCase;
        this.calcAndCheckSalesSlipUseCase = calcAndCheckSalesSlipUseCase;
        this.cancelCouponUseCase = cancelCouponUseCase;
        this.cancelSalesSlipUseCase = cancelSalesSlipUseCase;
        this.getSalesSlipByTransactionIdUseCase = getSalesSlipByTransactionIdUseCase;
        this.getSalesSlipForRevisionByTransactionRevisionIdUseCase =
                        getSalesSlipForRevisionByTransactionRevisionIdUseCase;
        this.openSalesSlipForRevisionUseCase = openSalesSlipForRevisionUseCase;
        this.openSalesSlipUseCase = openSalesSlipUseCase;
        this.publishSalesSlipForRevisionUseCase = publishSalesSlipForRevisionUseCase;
        this.publishSalesSlipUseCase = publishSalesSlipUseCase;
        this.updateCouponUseFlagForRevisionUseCase = updateCouponUseFlagOfSalesSlipForRevisionUseCase;
        this.updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase =
                        updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase;
        this.salesSlipHelper = salesSlipHelper;
        this.headerParamsUtil = headerParamsUtil;
    }

    /**
     * GET /price-planning/sales-slips : 販売伝票取得
     * 販売伝票（注文金額内訳）取得
     *
     * @param salesSlipGetRequest 販売伝票取得リクエスト (required)
     * @return 販売伝票レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<SalesSlipResponse> get(@NotNull @ApiParam(value = "販売伝票取得リクエスト", required = true)
                                                 @Valid SalesSlipGetRequest salesSlipGetRequest) {

        SalesSlipEntity salesSlipEntity = getSalesSlipByTransactionIdUseCase.getSalesSlipByTransactionId(
                        salesSlipGetRequest.getTransactionId());
        SalesSlipResponse response = salesSlipHelper.toSalesSlipResponse(salesSlipEntity);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips : 販売伝票発行（取引開始）
     * 販売伝票を下書き状態で発行し、取引（注文フロー）開始する&lt;br&gt;取引サービスの取引開始API内から本APIが呼ばれて、取引が開始される
     *
     * @param salesSlipRegistRequest 取引開始リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> regist(@ApiParam(value = "取引開始リクエスト", required = true) @Valid @RequestBody
                                                       SalesSlipRegistRequest salesSlipRegistRequest) {

        publishSalesSlipUseCase.publishSalesSlip(salesSlipRegistRequest.getTransactionId(), getCustomerId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/modernize : 販売伝票最新化
     * 販売伝票の最新化を行う
     *
     * @param salesSlipModernizeRequest 販売伝票最新化リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> modernize(@ApiParam(value = "販売伝票最新化リクエスト", required = true) @Valid @RequestBody
                                                          SalesSlipModernizeRequest salesSlipModernizeRequest) {

        modernizeSalesSlipUseCase.modernizeSalesSlip(salesSlipModernizeRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/check : 販売伝票チェック
     * 販売伝票の注文データチェックを行う
     *
     * @param salesSlipCheckRequest 販売伝票チェックリクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "販売伝票チェックリクエスト", required = true) @Valid @RequestBody
                                                      SalesSlipCheckRequest salesSlipCheckRequest) {

        calcAndCheckSalesSlipUseCase.calcAndCheckSalesSlip(
                        salesSlipCheckRequest.getTransactionId(), setDefaultBoolean(
                                        salesSlipCheckRequest.getContractConfirmFlag()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/coupons : クーポン適用
     * 販売伝票にクーポンを適用する
     *
     * @param salesSlipCouponApplyRequest クーポン適用リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> applyCoupon(@ApiParam(value = "クーポン適用リクエスト", required = true) @Valid @RequestBody
                                                            SalesSlipCouponApplyRequest salesSlipCouponApplyRequest) {

        applyCouponUseCase.applyCoupon(
                        salesSlipCouponApplyRequest.getTransactionId(), salesSlipCouponApplyRequest.getCouponCode());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE /price-planning/sales-slips/coupons : クーポン取消
     * 販売伝票のクーポンを取消する
     *
     * @param couponCancelRequest クーポン取消リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> cancelCoupon(@NotNull @ApiParam(value = "クーポン取消リクエスト", required = true)
                                             @Valid SalesSlipCouponCancelRequest couponCancelRequest) {

        cancelCouponUseCase.cancelCoupon(couponCancelRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/open : 販売伝票確定
     * 販売伝票の取引を確定する
     *
     * @param salesSlipOpenRequest 販売伝票確定リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> open(@ApiParam(value = "販売伝票確定リクエスト", required = true) @Valid @RequestBody
                                                     SalesSlipOpenRequest salesSlipOpenRequest) {

        openSalesSlipUseCase.openSalesSlip(salesSlipOpenRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/for-revision/cancel : 改訂用販売伝票取消
     * 改訂用販売伝票を取消する
     *
     * @param salesSlipForRevisionCancelRequest 改訂用販売伝票取消リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> cancelForRevision(
                    @ApiParam(value = "改訂用販売伝票取消リクエスト", required = true) @Valid @RequestBody
                                    SalesSlipForRevisionCancelRequest salesSlipForRevisionCancelRequest) {
        cancelSalesSlipUseCase.cancelSalesSlipForRevision(salesSlipForRevisionCancelRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/for-revision/adjustment-amount : 改訂用販売伝票に調整金額を追加
     * 改訂用販売伝票に調整金額を追加
     *
     * @param addAdjustmentAmountToSalesSlipForRevisionRequest 改訂用販売伝票に調整金額を追加リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> addAdjustmentAmountToSalesSlipForRevision(
                    @ApiParam(value = "改改訂用販売伝票のクーポン利用制限フラグ更新リクエスト", required = true) @Valid @RequestBody
                                    AddAdjustmentAmountToSalesSlipForRevisionRequest addAdjustmentAmountToSalesSlipForRevisionRequest) {

        addAdjustmentAmountToSalesSlipForRevisionUseCase.addAdjustmentAmountToSalesSlipForRevision(
                        addAdjustmentAmountToSalesSlipForRevisionRequest.getTransactionRevisionId(),
                        addAdjustmentAmountToSalesSlipForRevisionRequest.getAdjustName(),
                        addAdjustmentAmountToSalesSlipForRevisionRequest.getAdjustPrice()
                                                                                                  );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/for-revision/calc-and-check : 改訂用販売伝票を計算&amp;チェックする
     * 改訂用販売伝票を計算&amp;チェックする
     *
     * @param calcAndCheckSalesSlipForRevisionRequest 改訂用販売伝票を計算&amp;チェックするリクエスト (required)
     * @return 改訂用販売伝票計算&チェックレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CalcAndCheckSalesSlipForRevisionResponse> calcAndCheckSalesSlip(
                    @ApiParam(value = "改訂用販売伝票計算&チェックリクエスト", required = true) @Valid @RequestBody
                                    CalcAndCheckSalesSlipForRevisionRequest calcAndCheckSalesSlipForRevisionRequest) {

        CalcAndCheckSalesSlipForRevisionResponse calcAndCheckSalesSlipForRevisionResponse =
                        new CalcAndCheckSalesSlipForRevisionResponse();
        calcAndCheckSalesSlipForRevisionResponse.setWarningMessage(
                        calcAndCheckSalesSlipForRevisionUseCase.calcAndCheckSalesSlip(
                                        calcAndCheckSalesSlipForRevisionRequest.getTransactionRevisionId(),
                                        calcAndCheckSalesSlipForRevisionRequest.getContractConfirmFlag()
                                                                                     ));

        return new ResponseEntity<>(calcAndCheckSalesSlipForRevisionResponse, HttpStatus.OK);
    }

    /**
     * GET /price-planning/sales-slips/for-revision : 改訂用販売伝票取得
     * 改訂用取引IDに紐づく改訂用販売伝票取得
     *
     * @param getSalesSlipForRevisionByTransactionRevisionIdRequest 改訂用取引IDに紐づく改訂用販売伝票取得リクエスト (required)
     * @return 改訂用取引IDに紐づく改訂用販売伝票取得レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<GetSalesSlipForRevisionByTransactionRevisionIdResponse> getSalesSlipForRevisionByTransactionRevisionId(
                    @NotNull @ApiParam(value = "改訂用取引IDに紐づく改訂用販売伝票取得リクエスト", required = true)
                    @Valid GetSalesSlipForRevisionByTransactionRevisionIdRequest getSalesSlipForRevisionByTransactionRevisionIdRequest) {

        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        getSalesSlipForRevisionByTransactionRevisionIdUseCase.getSalesSlipByTransactionId(
                                        getSalesSlipForRevisionByTransactionRevisionIdRequest.getTransactionRevisionId());

        GetSalesSlipForRevisionByTransactionRevisionIdResponse response =
                        salesSlipHelper.toGetSalesSlipForRevisionByTransactionRevisionIdResponse(
                                        salesSlipForRevisionEntity);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/for-revision/open : 改訂用販売伝票を確定する
     * 改訂用販売伝票を確定する
     *
     * @param salesSlipOpenForRevisionRequest 改訂用販売伝票を確定するリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> openSalesSlipForRevision(
                    @ApiParam(value = "改訂用販売伝票を確定するリクエスト", required = true) @Valid @RequestBody
                                    SalesSlipOpenForRevisionRequest salesSlipOpenForRevisionRequest) {

        openSalesSlipForRevisionUseCase.openSalesSlipForRevision(
                        salesSlipOpenForRevisionRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/for-revision : 改訂用販売伝票発行
     * 改訂用販売伝票発行
     *
     * @param salesSlipPublishForRevisionRequest 改訂用販売伝票発行リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> publishSalesSlipForRevision(
                    @ApiParam(value = "改訂用販売伝票発行リクエスト", required = true) @Valid @RequestBody
                                    SalesSlipPublishForRevisionRequest salesSlipPublishForRevisionRequest) {

        publishSalesSlipForRevisionUseCase.publishSalesSlip(salesSlipPublishForRevisionRequest.getTransactionOriginId(),
                                                            salesSlipPublishForRevisionRequest.getTransactionRevisionId()
                                                           );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /price-planning/sales-slips/for-revision/coupon-use-flag : 改訂用販売伝票のクーポン利用フラグ更新
     * 改訂用販売伝票のクーポン利用フラグ更新
     *
     * @param updateCouponUseFlagOfSalesSlipForRevisionRequest 改訂用販売伝票のクーポン利用フラグ更新リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> updateCouponUseFlagOfSalesSlipForRevision(
                    @ApiParam(value = "改訂用販売伝票のクーポン利用フラグ更新リクエスト", required = true) @Valid @RequestBody
                                    UpdateCouponUseFlagOfSalesSlipForRevisionRequest updateCouponUseFlagOfSalesSlipForRevisionRequest) {
        updateCouponUseFlagForRevisionUseCase.updateCouponUseFlagOfSalesSlipForRevision(
                        updateCouponUseFlagOfSalesSlipForRevisionRequest.getTransactionRevisionId(),
                        updateCouponUseFlagOfSalesSlipForRevisionRequest.getUseCouponFlag()
                                                                                       );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /price-planning/sales-slips/for-revision/commission-and-carriage/apply-origin : 改訂用販売伝票の改訂前手数料/送料の適用フラグ設定
     *
     * @param applyOriginCommissionAndCarriageForRevisionRequest 改訂用販売伝票のクーポン利用フラグ更新リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> applyOriginCommissionAndCarriageForRevision(
                    @ApiParam(value = "改訂用販売伝票のクーポン利用フラグ更新リクエスト", required = true) @Valid @RequestBody
                                    ApplyOriginCommissionAndCarriageForRevisionRequest applyOriginCommissionAndCarriageForRevisionRequest) {

        boolean originCommissionApplyFlag = Boolean.TRUE.equals(
                        applyOriginCommissionAndCarriageForRevisionRequest.getOriginCommissionApplyFlag());
        boolean originCarriageApplyFlag = Boolean.TRUE.equals(
                        applyOriginCommissionAndCarriageForRevisionRequest.getOriginCarriageApplyFlag());
        updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase.updateOriginCommissionAndCarriageApplyFlagForRevision(
                        applyOriginCommissionAndCarriageForRevisionRequest.getTransactionRevisionId(),
                        originCommissionApplyFlag, originCarriageApplyFlag
                                                                                                                          );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 顧客IDを取得する
     *
     * @return customerId 顧客ID
     */
    private String getCustomerId() {
        // PR層でチェックはしない。memberSeqがNullの場合、customerIdにNullが設定される
        return this.headerParamsUtil.getMemberSeq();
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

}