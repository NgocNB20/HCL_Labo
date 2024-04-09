/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.UpdateShippingConditionParam;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 配送伝票アダプター<br/>
 * ※物流マイクロサービス
 */
public interface IShippingSlipAdapter {

    /**
     * 配送伝票発行
     *
     * @param transactionId 取引ID
     */
    void publishShippingSlip(TransactionId transactionId);

    /**
     * 配送伝票最新化
     *
     * @param transactionId 取引ID
     */
    void modernizeShippingSlip(TransactionId transactionId);

    /**
     * 配送伝票チェック
     *
     * @param transactionId 取引ID
     */
    void checkShippingSlip(TransactionId transactionId);

    /**
     * 在庫確保
     *
     * @param transactionId 取引ID
     */
    void secureShippingProductInventory(TransactionId transactionId);

    /**
     * 配送伝票確定
     *
     * @param transactionId 取引ID
     */
    void openShippingSlip(TransactionId transactionId);

    /**
     * 改訂用配送伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param revisionOpenFlag      改訂確定フラグ
     */
    void cancelShippingSlipForRevision(TransactionRevisionId transactionRevisionId, Boolean revisionOpenFlag);

    /**
     * 改訂用出荷実績登録
     *
     * @param transactionRevisionId     改訂用取引ID
     * @param shipmentStatusConfirmCode 配送状況確認番号
     * @param completeShipmentDate      出荷完了日時
     */
    void registShipmentResultForRevision(TransactionRevisionId transactionRevisionId,
                                         String shipmentStatusConfirmCode,
                                         Date completeShipmentDate,
                                         Boolean revisionOpenFlag);

    /**
     * 改訂用配送伝票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    void publishShippingSlipForRevision(TransactionId transactionId, TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用配送伝票チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param transactionId         改訂元取引ID
     * @return 警告メッセージマップ
     */
    Map<String, List<WarningContent>> checkShippingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                                   TransactionId transactionId);

    /**
     * 改訂用配送伝票の配送条件更新
     *
     * @param updateShippingConditionParam
     */
    void updateShippingConditionOfShippingSlipForRevision(UpdateShippingConditionParam updateShippingConditionParam);

    /**
     * 改訂用配送伝票の配送先住所更新
     *
     * @param transactionRevisionId
     * @param shippingAddressId
     */
    void updateShippingAddressOfShippingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                        String shippingAddressId);

    /**
     * 改訂用配送伝票を確定
     *
     * @param transactionRevisionId
     * @param inventorySettlementSkipFlag
     */
    void openShippingSlipForRevision(TransactionRevisionId transactionRevisionId, Boolean inventorySettlementSkipFlag);
}