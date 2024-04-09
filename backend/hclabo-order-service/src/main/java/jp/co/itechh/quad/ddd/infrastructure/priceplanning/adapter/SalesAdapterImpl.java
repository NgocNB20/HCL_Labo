/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.AddAdjustmentAmountToSalesSlipForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.ApplyOriginCommissionAndCarriageForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.CalcAndCheckSalesSlipForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.CalcAndCheckSalesSlipForRevisionResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCheckRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipForRevisionCancelRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipModernizeRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipOpenForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipOpenRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipPublishForRevisionRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipRegistRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.UpdateCouponUseFlagOfSalesSlipForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 販売アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class SalesAdapterImpl implements ISalesSlipAdapter {

    /** 販売伝票API */
    private final SalesSlipApi salesSlipApi;

    /** 販売アダプター実装Helperクラス */
    private final SalesAdapterHelper salesAdapterHelper;

    /**
     * コンストラクタ
     *  @param salesSlipApi     販売伝票API
     * @param headerParamsUtil ヘッダパラメーターユーティル
     * @param salesAdapterHelper
     */
    @Autowired
    public SalesAdapterImpl(SalesSlipApi salesSlipApi,
                            HeaderParamsUtility headerParamsUtil,
                            SalesAdapterHelper salesAdapterHelper) {
        this.salesSlipApi = salesSlipApi;
        this.salesAdapterHelper = salesAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.salesSlipApi.getApiClient());
    }

    /**
     * 販売企画マイクロサービス<br/>
     * 販売伝票発行
     *
     * @param transactionId 取引ID
     */
    @Override
    public void publishSalesSlip(TransactionId transactionId) {

        SalesSlipRegistRequest salesSlipRegistRequest = new SalesSlipRegistRequest();
        salesSlipRegistRequest.setTransactionId(transactionId.getValue());

        salesSlipApi.regist(salesSlipRegistRequest);
    }

    /**
     * 販売企画マイクロサービス<br/>
     * 販売伝票最新化
     *
     * @param transactionId 取引ID
     */
    @Override
    public void modernizeSalesSlip(TransactionId transactionId) {

        SalesSlipModernizeRequest salesSlipModernizeRequest = new SalesSlipModernizeRequest();
        salesSlipModernizeRequest.setTransactionId(transactionId.getValue());

        salesSlipApi.modernize(salesSlipModernizeRequest);
    }

    /**
     * 販売企画マイクロサービス<br/>
     * 販売伝票の計算&チェック
     *
     * @param transactionId 取引ID
     * @param contractConfirmFlag 契約確定フラグ
     */
    @Override
    public void calcAndCheckSalesSlip(TransactionId transactionId, boolean contractConfirmFlag) {

        SalesSlipCheckRequest salesSlipCheckRequest = new SalesSlipCheckRequest();
        salesSlipCheckRequest.setTransactionId(transactionId.getValue());
        salesSlipCheckRequest.setContractConfirmFlag(contractConfirmFlag);

        salesSlipApi.check(salesSlipCheckRequest);
    }

    /**
     * 販売企画マイクロサービス<br/>
     * 販売伝票確定
     *
     * @param transactionId 取引ID
     */
    @Override
    public void openSalesSlip(TransactionId transactionId) {

        SalesSlipOpenRequest salesSlipOpenRequest = new SalesSlipOpenRequest();
        salesSlipOpenRequest.setTransactionId(transactionId.getValue());

        salesSlipApi.open(salesSlipOpenRequest);
    }

    /**
     * 改訂用販売伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void cancelSalesSlipForRevision(TransactionRevisionId transactionRevisionId) {

        SalesSlipForRevisionCancelRequest salesSlipCancelRequest = new SalesSlipForRevisionCancelRequest();

        salesSlipCancelRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        salesSlipApi.cancelForRevision(salesSlipCancelRequest);
    }

    /**
     * 改訂用販売伝票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void publishSalesSlipForRevision(TransactionId transactionId, TransactionRevisionId transactionRevisionId) {

        SalesSlipPublishForRevisionRequest salesSlipPublishForRevisionRequest =
                        new SalesSlipPublishForRevisionRequest();

        salesSlipPublishForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        salesSlipPublishForRevisionRequest.setTransactionOriginId(transactionId.getValue());

        salesSlipApi.publishSalesSlipForRevision(salesSlipPublishForRevisionRequest);
    }

    /**
     * 改訂用販売伝票計算&チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param transactionId         改訂元取引ID
     * @param contractConfirmFlag   契約確定フラグ
     */
    @Override
    public Map<String, List<WarningContent>> calcAndCheckSalesSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                                              TransactionId transactionId,
                                                                              boolean contractConfirmFlag) {

        CalcAndCheckSalesSlipForRevisionRequest calcAndCheckSalesSlipForRevisionRequest =
                        new CalcAndCheckSalesSlipForRevisionRequest();

        calcAndCheckSalesSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        calcAndCheckSalesSlipForRevisionRequest.setContractConfirmFlag(contractConfirmFlag);

        CalcAndCheckSalesSlipForRevisionResponse response =
                        salesSlipApi.calcAndCheckSalesSlip(calcAndCheckSalesSlipForRevisionRequest);

        return salesAdapterHelper.toTransactionWarningMessageMap(response);
    }

    /**
     * 改訂用販売伝票のクーポン利用フラグを更新
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param couponUseFlag         クーポン利用フラグ
     */
    @Override
    public void updateCouponUseFlagOfSalesSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                          boolean couponUseFlag) {

        UpdateCouponUseFlagOfSalesSlipForRevisionRequest updateCouponUseFlagOfSalesSlipForRevisionRequest =
                        new UpdateCouponUseFlagOfSalesSlipForRevisionRequest();

        updateCouponUseFlagOfSalesSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        updateCouponUseFlagOfSalesSlipForRevisionRequest.setUseCouponFlag(couponUseFlag);

        salesSlipApi.updateCouponUseFlagOfSalesSlipForRevision(updateCouponUseFlagOfSalesSlipForRevisionRequest);
    }

    /**
     * 改訂用販売伝票に調整金額を追加
     *
     * @param adjustName  調整項目名
     * @param adjustPrice 調整金額
     */
    @Override
    public void addAdjustmentAmountOfSalesSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                          String adjustName,
                                                          Integer adjustPrice) {

        AddAdjustmentAmountToSalesSlipForRevisionRequest addAdjustmentAmountToSalesSlipForRevisionRequest =
                        new AddAdjustmentAmountToSalesSlipForRevisionRequest();

        addAdjustmentAmountToSalesSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        addAdjustmentAmountToSalesSlipForRevisionRequest.setAdjustName(adjustName);
        addAdjustmentAmountToSalesSlipForRevisionRequest.setAdjustPrice(adjustPrice);

        salesSlipApi.addAdjustmentAmountToSalesSlipForRevision(addAdjustmentAmountToSalesSlipForRevisionRequest);
    }

    /**
     * 改訂用販売伝票を確定
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void openSalesSlipForRevision(TransactionRevisionId transactionRevisionId) {

        SalesSlipOpenForRevisionRequest salesSlipOpenForRevisionRequest = new SalesSlipOpenForRevisionRequest();

        salesSlipOpenForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        salesSlipApi.openSalesSlipForRevision(salesSlipOpenForRevisionRequest);
    }

    /**
     * 改訂用販売伝票の改訂前手数料/送料適用フラグを更新する
     *
     * @param transactionRevisionId
     * @param originCommissionApplyFlag
     * @param originCarriageApplyFlag
     */
    @Override
    public void updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase(TransactionRevisionId transactionRevisionId,
                                                                             boolean originCommissionApplyFlag,
                                                                             boolean originCarriageApplyFlag) {

        ApplyOriginCommissionAndCarriageForRevisionRequest applyOriginCommissionAndCarriageForRevisionRequest =
                        new ApplyOriginCommissionAndCarriageForRevisionRequest();

        applyOriginCommissionAndCarriageForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCommissionApplyFlag(originCommissionApplyFlag);
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCarriageApplyFlag(originCarriageApplyFlag);

        salesSlipApi.applyOriginCommissionAndCarriageForRevision(applyOriginCommissionAndCarriageForRevisionRequest);
    }

    /**
     * 販売企画マイクロサービス
     * 販売伝票取得
     *
     * @param transactionId 取引ID
     * @return SalesSlip 販売伝票
     */
    @Override
    public SalesSlip getSaleSlip(TransactionId transactionId) {
        SalesSlipGetRequest request = new SalesSlipGetRequest();
        request.setTransactionId(transactionId.getValue());

        SalesSlipResponse response = salesSlipApi.get(request);
        return salesAdapterHelper.toSalesSlip(response);
    }
}