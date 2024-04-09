/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 改訂取引全体チェックユースケース
 */
@Service
public class CheckTransactionForRevisionUseCase {

    /**
     * 処理件数マップ　ワーニングメッセージ
     * <code>WARNING_MESSAGE</code>
     */
    private static final String WARNING_MESSAGE = "WarningMessage";

    /** 改訂取引アダプター */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** プロモーションアダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 受注アダプター */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 配送アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 販売アダプター */
    private final ISalesSlipAdapter salesAdapter;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** コンストラクタ */
    @Autowired
    public CheckTransactionForRevisionUseCase(ITransactionForRevisionRepository transactionForRevisionRepository,
                                              IOrderSlipAdapter orderSlipAdapter,
                                              IOrderReceivedRepository orderReceivedRepository,
                                              IShippingSlipAdapter shippingAdapter,
                                              ISalesSlipAdapter salesAdapter,
                                              IBillingSlipAdapter billingAdapter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.orderSlipAdapter = orderSlipAdapter;
        this.orderReceivedRepository = orderReceivedRepository;
        this.shippingAdapter = shippingAdapter;
        this.salesAdapter = salesAdapter;
        this.billingAdapter = billingAdapter;
    }

    /**
     * 改訂用取引全体をチェックする
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param contractConfirmFlag   契約確定フラグ
     * @return 警告メッセージマップ
     */
    public Map<String, List<WarningContent>> checkTransaction(String transactionRevisionId,
                                                              boolean contractConfirmFlag) {

        // 改訂取引取得
        TransactionForRevisionEntity transactionForRevisionEntity =
                        transactionForRevisionRepository.get(new TransactionRevisionId(transactionRevisionId));
        // 取引改訂が存在しない場合はエラー
        if (transactionForRevisionEntity == null) {
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
        }

        // 受注取得
        OrderReceivedEntity orderReceivedEntity =
                        orderReceivedRepository.get(transactionForRevisionEntity.getOrderReceivedId());
        // 受注が存在しない場合はエラー
        if (orderReceivedEntity == null) {
            throw new DomainException(
                            "ORDER-ODER0002-E",
                            new String[] {transactionForRevisionEntity.getOrderReceivedId().getValue()}
            );
        }

        ApplicationException appException = new ApplicationException();
        // 排他チェック
        if (!orderReceivedEntity.getLatestTransactionId()
                                .getValue()
                                .equals(transactionForRevisionEntity.getTransactionId().getValue())) {
            appException.addMessage("ORDER-ODER0001-E");
        }
        // アプリケーションエラー発生確認、発生していれば例外スロー
        if (appException.hasMessage()) {
            throw appException;
        }

        // 改訂用販売伝票を計算&チェックする(計算処理のため以下のチェックより先に実施)
        Map<String, List<WarningContent>> salesSlipWarningMap = salesAdapter.calcAndCheckSalesSlipForRevision(
                        transactionForRevisionEntity.getTransactionRevisionId(),
                        transactionForRevisionEntity.getTransactionId(), contractConfirmFlag
                                                                                                             );

        // 改訂用注文票をチェックする
        orderSlipAdapter.checkOrderSlipForRevision(
                        transactionForRevisionEntity.getTransactionRevisionId(),
                        transactionForRevisionEntity.getTransactionId()
                                                  );

        // 改訂用配送伝票をチェックする
        Map<String, List<WarningContent>> shippingSlipWarningMap = shippingAdapter.checkShippingSlipForRevision(
                        transactionForRevisionEntity.getTransactionRevisionId(),
                        transactionForRevisionEntity.getTransactionId()
                                                                                                               );

        // 改訂用請求伝票をチェックする
        billingAdapter.checkBillingSlipForRevision(
                        transactionForRevisionEntity.getTransactionRevisionId(),
                        transactionForRevisionEntity.getTransactionId()
                                                  );

        // 警告メッセージを設定する
        return this.mergeWarningMessageMaps(salesSlipWarningMap, shippingSlipWarningMap);
    }

    /**
     * 警告メッセージをマージする
     *
     * @param salesSlipMap
     * @param shippingSlipMap
     * @return 警告メッセージマップ
     */
    private Map<String, List<WarningContent>> mergeWarningMessageMaps(Map<String, List<WarningContent>> salesSlipMap,
                                                                      Map<String, List<WarningContent>> shippingSlipMap) {
        Map<String, List<WarningContent>> mergedWarningMap = new HashMap<>();
        List<WarningContent> mergeWarningList = new ArrayList<>();

        if (MapUtils.isNotEmpty(salesSlipMap) && CollectionUtils.isNotEmpty(salesSlipMap.get(WARNING_MESSAGE))) {
            mergeWarningList.addAll(salesSlipMap.get(WARNING_MESSAGE));
        }

        if (MapUtils.isNotEmpty(shippingSlipMap) && CollectionUtils.isNotEmpty(shippingSlipMap.get(WARNING_MESSAGE))) {
            mergeWarningList.addAll(shippingSlipMap.get(WARNING_MESSAGE));
        }
        mergedWarningMap.put(WARNING_MESSAGE, mergeWarningList);

        return mergedWarningMap;
    }
}