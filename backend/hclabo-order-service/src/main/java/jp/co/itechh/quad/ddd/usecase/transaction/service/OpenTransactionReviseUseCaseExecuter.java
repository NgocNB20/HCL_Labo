/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction.service;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.OpenBillingSlipReviseResponse;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.analytics.adapter.IAggregateSalesAdapter;
import jp.co.itechh.quad.ddd.domain.analytics.adapter.IOrderSearchAdapter;
import jp.co.itechh.quad.ddd.domain.customize.adapter.IExaminationAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.AdministratorDto;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.IAdministratorAdapter;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 取引改訂確定ユースケース内部ロジック<br/>
 * ※親トランザクションがある場合の呼び出し用（ユースケースから取引改訂確定ユースケース呼出したい場合に利用）
 */
@Service
public class OpenTransactionReviseUseCaseExecuter {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OpenTransactionReviseUseCaseExecuter.class);

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 請求伝票アダプター */
    private final IBillingSlipAdapter billingSlipAdapter;

    /** 配送伝票アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 販売伝票アダプター */
    private final ISalesSlipAdapter salesAdapter;

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 運営者アダプター */
    private final IAdministratorAdapter administratorAdapter;

    /** 受注検索アダプター */
    private final IOrderSearchAdapter orderSearchAdapter;

    /** 集計用販売アダプター */
    private final IAggregateSalesAdapter aggregateSalesAdapter;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 検査アダプター */
    private final IExaminationAdapter examinationAdapter;

    /** 請求不整合エラーメール文言取得失敗用 */
    private final static String MAIL_CONTENT_FAIL = "(設定失敗)";

    /** コンストラクタ */
    @Autowired
    public OpenTransactionReviseUseCaseExecuter(ITransactionForRevisionRepository transactionForRevisionRepository,
                                                IOrderReceivedRepository orderReceivedRepository,
                                                ITransactionRepository transactionRepository,
                                                IBillingSlipAdapter billingSlipAdapter,
                                                IShippingSlipAdapter shippingAdapter,
                                                ISalesSlipAdapter salesAdapter,
                                                IOrderSlipAdapter orderSlipAdapter,
                                                IAdministratorAdapter administratorAdapter,
                                                IOrderSearchAdapter orderSearchAdapter,
                                                IAggregateSalesAdapter aggregateSalesAdapter,
                                                INotificationSubAdapter notificationSubAdapter,
                                                IExaminationAdapter examinationAdapter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.orderReceivedRepository = orderReceivedRepository;
        this.transactionRepository = transactionRepository;
        this.billingSlipAdapter = billingSlipAdapter;
        this.shippingAdapter = shippingAdapter;
        this.salesAdapter = salesAdapter;
        this.orderSlipAdapter = orderSlipAdapter;
        this.administratorAdapter = administratorAdapter;
        this.orderSearchAdapter = orderSearchAdapter;
        this.aggregateSalesAdapter = aggregateSalesAdapter;
        this.notificationSubAdapter = notificationSubAdapter;
        this.examinationAdapter = examinationAdapter;
    }

    /**
     * 取引改訂を確定する内部ロジック（DB楽観ロックチェックあり）
     *
     * @param transactionRevisionId
     * @param administratorSeq
     * @param processType
     * @param inventorySettlementSkipFlag       在庫確保/決済実行のスキップフラグ（受注修正用の在庫調整とGMO決済変更実施するかのフラグ）
     * @param openBillingSlipReviseSkipFlag     請求伝票確定スキップフラグ（トランザクション制御用に確定タイミング変更用）
     * @param openShippingSlipForReviseSkipFlag 配送票確定スキップフラグ（トランザクション制御用に確定タイミング変更用）
     * @return
     */
    public OrderReceivedEntity openTransactionReviseInnerLogic(String transactionRevisionId,
                                                               Integer administratorSeq,
                                                               HTypeProcessType processType,
                                                               boolean inventorySettlementSkipFlag,
                                                               boolean openBillingSlipReviseSkipFlag,
                                                               boolean openShippingSlipForReviseSkipFlag) {

        // アサートチェック
        AssertChecker.assertNotNull("processType is null", processType);

        TransactionRevisionId transactionRevisionIdVo = new TransactionRevisionId(transactionRevisionId);

        // 改訂用取引を取得
        TransactionForRevisionEntity transactionForRevisionEntity =
                        transactionForRevisionRepository.get(transactionRevisionIdVo);
        // 改訂用取引が存在しない場合はエラー
        if (transactionForRevisionEntity == null) {
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
        }

        // 改訂用取引にひもづく改訂用請求伝票を取得する
        BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipForRevision =
                        billingSlipAdapter.getBillingSlipForRevision(transactionRevisionIdVo);

        // 請求伝票確定時に請求決済エラーを解消した場合
        if (ObjectUtils.isNotEmpty(billingSlipForRevision) && Boolean.TRUE.equals(
                        billingSlipForRevision.getPaymentAgencyReleaseFlag())
            && transactionForRevisionEntity.isBillPaymentErrorFlag()) {
            // 改訂用取引の請求決済エラーを解消
            transactionForRevisionEntity.releaseBillPaymentError();
        }

        // 運営者情報
        AdministratorDto administratorDto = null;
        if (administratorSeq != null) {
            administratorDto = administratorAdapter.get(administratorSeq);
            if (administratorDto == null) {
                throw new DomainException("ORDER-ADMN0001-E", new String[] {String.valueOf(administratorSeq)});
            }
        }

        // 受注を取得
        OrderReceivedEntity orderReceivedEntity =
                        orderReceivedRepository.get(transactionForRevisionEntity.getOrderReceivedId());
        // 受注が存在しない場合はエラー
        if (orderReceivedEntity == null) {
            throw new DomainException("ORDER-ODER0002-E",
                                      new String[] {transactionForRevisionEntity.getOrderReceivedId().getValue()}
            );
        }

        // 改訂用販売伝票を確定
        salesAdapter.openSalesSlipForRevision(transactionRevisionIdVo);

        // 改訂用注文票を確定
        orderSlipAdapter.openOrderSlipForRevision(transactionRevisionIdVo);

        // 登録日時
        Date registDate = new Date();

        // 取引改訂を実行
        TransactionEntity transactionEntity = new TransactionEntity(transactionForRevisionEntity, registDate);

        // 改訂済みの取引に処理履歴情報を設定
        if (administratorDto != null) {
            transactionEntity.settingProcessHistoryInfo(
                            registDate, processType, administratorDto.getAdministratorLastName() + "　"
                                                     + administratorDto.getAdministratorFirstName());
        } else {
            transactionEntity.settingProcessHistoryInfo(registDate, processType, null);
        }

        // 改訂済みの取引を新規登録する
        transactionRepository.save(transactionEntity);

        // 受注の最新取引IDを改訂済みの取引IDに更新
        orderReceivedEntity.settingLatestTransaction(transactionEntity.getTransactionId());

        // 受注を更新する（DB楽観ロックチェックあり）
        orderReceivedRepository.updateWithTranCheck(
                        orderReceivedEntity, transactionForRevisionEntity.getTransactionId().getValue());

        // 取引IDで改訂用注文票を取得する（プロモーションサービス）
        OrderSlipForRevision orderSlipForRevision =
            orderSlipAdapter.getOrderSlipForRevision(transactionRevisionId);

        // 改訂用注文商品リストと注文商品リストで追加対象となる「注文商品IDのリスト」を作成する
        Set<String> orderItemIdRegist = new HashSet<>();
        orderSlipForRevision.getRevisionOrderItemList().forEach(
            revisionOrderItem -> {
                if (HTypeNoveltyGoodsType.NORMAL_GOODS.getValue().equals(revisionOrderItem.getNoveltyGoodsType())) {
                    orderItemIdRegist.add(revisionOrderItem.getOrderItemId());
                }
            });
        orderSlipForRevision.getOrderItemList().forEach(
            orderItem -> orderItemIdRegist.remove(orderItem.getOrderItemId()));

        // 検査キットを新規登録する（兵庫臨床カスタマイズサービス）
        if (!orderItemIdRegist.isEmpty()) {
            this.examinationAdapter.registExamKit(orderReceivedEntity.getOrderCode().getValue(), new ArrayList<>(orderItemIdRegist));
        }

        // 改訂用請求伝票を確定
        OpenBillingSlipReviseResponse openBillingSlipReviseResponse = null;
        if (!openBillingSlipReviseSkipFlag) {
            openBillingSlipReviseResponse = billingSlipAdapter.openBillingSlipForRevision(transactionRevisionIdVo,
                                                                                          inventorySettlementSkipFlag
                                                                                         );
        }

        // 改訂用配送伝票を確定
        if (!openShippingSlipForReviseSkipFlag) {
            try {
                shippingAdapter.openShippingSlipForRevision(transactionRevisionIdVo, inventorySettlementSkipFlag);
            } catch (Exception e) {

                LOGGER.error("改訂用配送伝票を確定処理を実行中にエラーが発生しました。 -- 受注番号：" + orderReceivedEntity.getOrderCode().getValue()
                             + "-- 改訂用取引ID：" + transactionRevisionIdVo.getValue(), e);

                //　決済済みの場合は請求不整合エラーメール送信
                if (openBillingSlipReviseResponse != null && Boolean.TRUE.equals(
                                openBillingSlipReviseResponse.getGmoCommunicationFlag())) {

                    String orderCodeSendMail = MAIL_CONTENT_FAIL;
                    if (orderReceivedEntity.getOrderCode() != null
                        && orderReceivedEntity.getOrderCode().getValue() != null) {
                        orderCodeSendMail = orderReceivedEntity.getOrderCode().getValue();
                    }

                    sendReportMail(orderCodeSendMail, transactionRevisionId);

                    throw new DomainException("PAYMENT_EPAR0006-E");
                }

                throw e;
            }
        }

        return orderReceivedEntity;
    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcessInnerLogic(OrderReceivedEntity orderReceivedEntity) {

        if (orderReceivedEntity != null) {
            // 分析サービスへ受注検索用データを登録・更新する
            orderSearchAdapter.registUpdateOrderSearch(orderReceivedEntity.getOrderReceivedId());

            TransactionRevisionId transactionRevisionId =
                            new TransactionRevisionId(orderReceivedEntity.getLatestTransactionId().getValue());

            // 改訂前のトランザクションを取得
            TransactionForRevisionEntity transactionForRevisionEntity =
                            transactionForRevisionRepository.get(transactionRevisionId);

            // 集計用販売データ登録
            aggregateSalesAdapter.aggregateSalesData(orderReceivedEntity.getLatestTransactionId().getValue(),
                                                     transactionForRevisionEntity.getTransactionId().getValue()
                                                    );
        }
    }

    /**
     * 請求不整合報告メールを送信する
     *
     * @param orderCode             受注番号
     * @param transactionRevisionId 改訂用取引ID
     */
    private void sendReportMail(String orderCode, String transactionRevisionId) {

        // プレースホルダの準備
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        String system = MAIL_CONTENT_FAIL;
        String time = MAIL_CONTENT_FAIL;
        String server = MAIL_CONTENT_FAIL;
        String process = MAIL_CONTENT_FAIL;
        String error = MAIL_CONTENT_FAIL;
        String recovery = MAIL_CONTENT_FAIL;

        try {
            system = PropertiesUtil.getSystemPropertiesValue("alert.mail.system");
            time = dateUtility.format(dateUtility.getCurrentTime(), DateUtility.YMD_SLASH_HMS);
            server = PropertiesUtil.getSystemPropertiesValue("system.name");
            process = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.processtype.openslip");
            error = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.error.message");
            recovery = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.recovery.message");
        } catch (Exception e) {
            LOGGER.error("請求不整合メール用文言取得失敗しました。", e);
            system = MAIL_CONTENT_FAIL;
            time = MAIL_CONTENT_FAIL;
            server = MAIL_CONTENT_FAIL;
            process = MAIL_CONTENT_FAIL;
            error = MAIL_CONTENT_FAIL;
            recovery = MAIL_CONTENT_FAIL;
        }

        Map<String, String> placeHolders = new LinkedHashMap<>();
        // メールに記載するシステム名
        placeHolders.put("SYSTEM", system);
        placeHolders.put("TIME", time);
        placeHolders.put("SERVER", server);
        placeHolders.put("ORDERCODE", orderCode);
        placeHolders.put("PROCESS", process);
        placeHolders.put("MULPAY_PROCESS", "改訂用取引ID：" + transactionRevisionId);
        placeHolders.put("ERROR", error);
        placeHolders.put("RECOVERY", recovery);

        // メールを送信する
        SettlementMismatchRequest request = new SettlementMismatchRequest();
        request.setPlaceHolders(placeHolders);

        notificationSubAdapter.settlementMisMatch(request);

        LOGGER.info("請求不整合報告メールを送信しました。改訂用取引ID：" + transactionRevisionId);
    }
}