/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.priceplanning.adapter;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;

import java.util.List;
import java.util.Map;

/**
 * 販売伝票アダプター<br/>
 * 販売企画マイクロサービス
 */
public interface ISalesSlipAdapter {

    /**
     * 販売伝票発行
     *
     * @param transactionId 取引ID
     */
    void publishSalesSlip(TransactionId transactionId);

    /**
     * 販売伝票最新化
     *
     * @param transactionId 取引ID
     */
    void modernizeSalesSlip(TransactionId transactionId);

    /**
     * 販売伝票計算&チェック
     *
     * @param transactionId       取引ID
     * @param contractConfirmFlag 契約確定フラグ
     */
    void calcAndCheckSalesSlip(TransactionId transactionId, boolean contractConfirmFlag);

    /**
     * 販売伝票確定
     *
     * @param transactionId 取引ID
     */
    void openSalesSlip(TransactionId transactionId);

    /**
     * 改訂用販売伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    void cancelSalesSlipForRevision(TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用販売伝票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    void publishSalesSlipForRevision(TransactionId transactionId, TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用販売伝票の計算&チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param transactionId         改訂元取引ID
     * @param contractConfirmFlag   契約確定フラグ
     * @return 警告メッセージ
     */
    Map<String, List<WarningContent>> calcAndCheckSalesSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                                       TransactionId transactionId,
                                                                       boolean contractConfirmFlag);

    /**
     * 改訂用販売伝票のクーポン利用フラグを更新
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param couponUseFlag         クーポン利用フラグ
     */
    void updateCouponUseFlagOfSalesSlipForRevision(TransactionRevisionId transactionRevisionId, boolean couponUseFlag);

    /**
     * 改訂用販売伝票に調整金額を追加
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param adjustName            調整項目名
     * @param adjustPrice           調整金額
     */
    void addAdjustmentAmountOfSalesSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                   String adjustName,
                                                   Integer adjustPrice);

    /**
     * 改訂用販売伝票を確定
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    void openSalesSlipForRevision(TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用販売伝票の改訂前手数料/送料適用フラグを更新する
     *
     * @param transactionRevisionId
     * @param originCommissionApplyFlag
     * @param originCarriageApplyFlag
     */
    void updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase(TransactionRevisionId transactionRevisionId,
                                                                      boolean originCommissionApplyFlag,
                                                                      boolean originCarriageApplyFlag);

    /**
     * 販売企画マイクロサービス
     * 販売伝票取得
     *
     * @param transactionId 取引ID
     * @return SalesSlip 販売伝票
     */
    SalesSlip getSaleSlip(TransactionId transactionId);
}