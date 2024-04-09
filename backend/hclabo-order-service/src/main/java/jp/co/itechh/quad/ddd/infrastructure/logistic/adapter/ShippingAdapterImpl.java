/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.UpdateShippingConditionParam;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSettingAddressForRevisionRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipCheckRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipCheckResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionCancelRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionCheckRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionOpenRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionPublishRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionShipmentRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipInventorySecureRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipModernizeRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipOpenRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipRegistRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingUpdateConditionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 配送アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class ShippingAdapterImpl implements IShippingSlipAdapter {

    /** 配送伝票API */
    private final ShippingSlipApi shippingSlipApi;

    /** 配送アダプターHelperクラス */
    private final ShippingSlipAdapterHelper shippingSlipAdapterHelper;

    /** コンストラクタ */
    @Autowired
    public ShippingAdapterImpl(ShippingSlipApi shippingSlipApi,
                               HeaderParamsUtility headerParamsUtil,
                               ShippingSlipAdapterHelper shippingSlipAdapterHelper) {
        this.shippingSlipApi = shippingSlipApi;
        this.shippingSlipAdapterHelper = shippingSlipAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.shippingSlipApi.getApiClient());
    }

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票発行
     *
     * @param transactionId 取引ID
     */
    @Override
    public void publishShippingSlip(TransactionId transactionId) {

        ShippingSlipRegistRequest shippingSlipRegistRequest = new ShippingSlipRegistRequest();
        shippingSlipRegistRequest.setTransactionId(transactionId.getValue());

        shippingSlipApi.regist(shippingSlipRegistRequest);
    }

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票最新化
     *
     * @param transactionId 取引ID
     */
    @Override
    public void modernizeShippingSlip(TransactionId transactionId) {

        ShippingSlipModernizeRequest shippingSlipModernizeRequest = new ShippingSlipModernizeRequest();
        shippingSlipModernizeRequest.setTransactionId(transactionId.getValue());

        shippingSlipApi.modernize(shippingSlipModernizeRequest);
    }

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票チェック
     *
     * @param transactionId 取引ID
     */
    @Override
    public void checkShippingSlip(TransactionId transactionId) {

        ShippingSlipCheckRequest shippingSlipCheckRequest = new ShippingSlipCheckRequest();
        shippingSlipCheckRequest.setTransactionId(transactionId.getValue());

        shippingSlipApi.check(shippingSlipCheckRequest);
    }

    /**
     * 物流マイクロサービス<br/>
     * 在庫確保
     *
     * @param transactionId 取引ID
     */
    @Override
    public void secureShippingProductInventory(TransactionId transactionId) {

        ShippingSlipInventorySecureRequest shippingSlipInventorySecureRequest =
                        new ShippingSlipInventorySecureRequest();
        shippingSlipInventorySecureRequest.setTransactionId(transactionId.getValue());

        shippingSlipApi.secureInventory(shippingSlipInventorySecureRequest);
    }

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票確定
     *
     * @param transactionId 取引ID
     */
    @Override
    public void openShippingSlip(TransactionId transactionId) {

        ShippingSlipOpenRequest shippingSlipOpenRequest = new ShippingSlipOpenRequest();
        shippingSlipOpenRequest.setTransactionId(transactionId.getValue());

        shippingSlipApi.open(shippingSlipOpenRequest);
    }

    /**
     * 改訂用配送伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param revisionOpenFlag      改訂確定フラグ
     */
    @Override
    public void cancelShippingSlipForRevision(TransactionRevisionId transactionRevisionId, Boolean revisionOpenFlag) {

        ShippingSlipForRevisionCancelRequest shippingSlipForRevisionCancelRequest =
                        new ShippingSlipForRevisionCancelRequest();

        shippingSlipForRevisionCancelRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        shippingSlipForRevisionCancelRequest.setRevisionOpenFlag(revisionOpenFlag);

        shippingSlipApi.cancelForRevision(shippingSlipForRevisionCancelRequest);
    }

    /**
     * 改訂用出荷実績登録
     *
     * @param transactionRevisionId     改訂用取引ID
     * @param shipmentStatusConfirmCode 配送状況確認番号
     * @param completeShipmentDate      出荷完了日時
     */
    @Override
    public void registShipmentResultForRevision(TransactionRevisionId transactionRevisionId,
                                                String shipmentStatusConfirmCode,
                                                Date completeShipmentDate,
                                                Boolean revisionOpenFlag) {

        ShippingSlipForRevisionShipmentRequest shippingSlipForRevisionShipmentRequest =
                        new ShippingSlipForRevisionShipmentRequest();

        shippingSlipForRevisionShipmentRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        shippingSlipForRevisionShipmentRequest.setShipmentStatusConfirmCode(shipmentStatusConfirmCode);
        shippingSlipForRevisionShipmentRequest.setCompleteShipmentDate(completeShipmentDate);
        shippingSlipForRevisionShipmentRequest.setRevisionOpenFlag(revisionOpenFlag);

        shippingSlipApi.shipmentForRevision(shippingSlipForRevisionShipmentRequest);
    }

    /**
     * 改訂用配送伝票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void publishShippingSlipForRevision(TransactionId transactionId,
                                               TransactionRevisionId transactionRevisionId) {

        ShippingSlipForRevisionPublishRequest shippingSlipForRevisionPublishRequest =
                        new ShippingSlipForRevisionPublishRequest();

        shippingSlipForRevisionPublishRequest.setTransactionId(transactionId.getValue());
        shippingSlipForRevisionPublishRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        shippingSlipApi.publishShippingSlipForRevision(shippingSlipForRevisionPublishRequest);
    }

    /**
     * 改訂用配送伝票チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param transactionId         改訂元取引ID
     * @return 警告メッセージマップ
     */
    @Override
    public Map<String, List<WarningContent>> checkShippingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                                          TransactionId transactionId) {

        ShippingSlipForRevisionCheckRequest shippingSlipForRevisionCheckRequest =
                        new ShippingSlipForRevisionCheckRequest();

        shippingSlipForRevisionCheckRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        ShippingSlipCheckResponse shippingSlipCheckResponse =
                        shippingSlipApi.checkShippingSlipForRevision(shippingSlipForRevisionCheckRequest);

        return shippingSlipAdapterHelper.setWarningMessageForTransactionCheckResponse(shippingSlipCheckResponse);
    }

    /**
     * 改訂用配送伝票の配送条件更新
     *
     * @param updateShippingConditionParam 改訂用配送伝票の配送条件更新パラメータ
     */
    @Override
    public void updateShippingConditionOfShippingSlipForRevision(UpdateShippingConditionParam updateShippingConditionParam) {

        ShippingUpdateConditionForRevisionRequest shippingUpdateConditionForRevisionRequest =
                        new ShippingUpdateConditionForRevisionRequest();

        shippingUpdateConditionForRevisionRequest.setTransactionRevisionId(
                        updateShippingConditionParam.getTransactionRevisionId().getValue());
        shippingUpdateConditionForRevisionRequest.setShippingMethodId(
                        updateShippingConditionParam.getShippingMethodId());
        shippingUpdateConditionForRevisionRequest.setReceiverDate(updateShippingConditionParam.getReceiverDate());
        shippingUpdateConditionForRevisionRequest.setReceiverTimeZone(
                        updateShippingConditionParam.getReceiverTimeZone());
        shippingUpdateConditionForRevisionRequest.setInvoiceNecessaryFlag(
                        updateShippingConditionParam.isInvoiceNecessaryFlag());
        shippingUpdateConditionForRevisionRequest.setShipmentStatusConfirmCode(
                        updateShippingConditionParam.getShipmentStatusConfirmCode());

        shippingSlipApi.updateShippingConditionForRevision(shippingUpdateConditionForRevisionRequest);
    }

    /**
     * 改訂用配送伝票の配送先住所更新
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param shippingAddressId     配送先住所ID
     */
    @Override
    public void updateShippingAddressOfShippingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                               String shippingAddressId) {

        ShippingSettingAddressForRevisionRequest shippingSettingAddressForRevisionRequest =
                        new ShippingSettingAddressForRevisionRequest();

        shippingSettingAddressForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        shippingSettingAddressForRevisionRequest.setShippingAddressId(shippingAddressId);

        shippingSlipApi.settingShippingAddressForRevision(shippingSettingAddressForRevisionRequest);
    }

    /**
     * 改訂用配送伝票を確定
     *
     * @param transactionRevisionId       改訂用取引ID
     * @param inventorySettlementSkipFlag 在庫決済スキップフラグ
     */
    @Override
    public void openShippingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                            Boolean inventorySettlementSkipFlag) {

        ShippingSlipForRevisionOpenRequest shippingSlipForRevisionOpenRequest =
                        new ShippingSlipForRevisionOpenRequest();

        shippingSlipForRevisionOpenRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        shippingSlipForRevisionOpenRequest.setInventorySkipFlag(inventorySettlementSkipFlag);

        shippingSlipApi.openShippingSlipForRevision(shippingSlipForRevisionOpenRequest);
    }

}