/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction.service;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionOpenSalesResponse;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 出荷登録実行クラス
 */
@Service
public class ShipmentRegistSingleExecuter {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ShipmentRegistSingleExecuter.class);

    /** 請求不整合エラーメール文言取得失敗用 */
    private final static String MAIL_CONTENT_FAIL = "(設定失敗)";

    /** 請求エラー発生用メッセージ */
    public static final String MSGCD_BILL_PAYMENT_ERROR = "ORDER-SHIPPING04-E";

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** 配送アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 取引改訂開始ユースケース内部ロジック */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 取引改訂確定ユースケース 内部ロジック */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** コンストラクタ */
    @Autowired
    public ShipmentRegistSingleExecuter(ITransactionForRevisionRepository transactionForRevisionRepository,
                                        IBillingSlipAdapter billingAdapter,
                                        IShippingSlipAdapter shippingAdapter,
                                        StartTransactionReviseUseCaseExecuter startTransactionForRevisionUseCaseExecuter,
                                        OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter,
                                        IOrderReceivedRepository orderReceivedRepository,
                                        ITransactionRepository transactionRepository,
                                        INotificationSubAdapter notificationSubAdapter) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.billingAdapter = billingAdapter;
        this.shippingAdapter = shippingAdapter;
        this.startTransactionReviseUseCaseExecuter = startTransactionForRevisionUseCaseExecuter;
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
        this.orderReceivedRepository = orderReceivedRepository;
        this.transactionRepository = transactionRepository;
        this.notificationSubAdapter = notificationSubAdapter;
    }

    /**
     * 取引の出荷実績を登録する
     *
     * @param orderCode                 受注番号
     * @param shipmentStatusConfirmCode 配送状況確認番号
     * @param completeShipmentDate      出荷完了日時
     * @param administratorSeq          運営者SEQ
     * @return messageDto
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ShipmentRegisterMessageDto execute(String orderCode,
                                              String shipmentStatusConfirmCode,
                                              Date completeShipmentDate,
                                              Integer administratorSeq) {

        // チェック
        AssertChecker.assertNotNull("orderCode is null", orderCode);
        AssertChecker.assertNotNull("administratorSeq is null", administratorSeq);

        OrderReceivedEntity orderReceivedEntityByOrderCode =
                        orderReceivedRepository.getByOrderCode(new OrderCode(orderCode));
        // 対象受注が存在しない場合はエラー
        if (orderReceivedEntityByOrderCode == null) {
            throw new DomainException("ORDER-SHIPPING05-E");
        }

        String transactionId = ObjectUtils.isNotEmpty(orderReceivedEntityByOrderCode.getLatestTransactionId()) ?
                        orderReceivedEntityByOrderCode.getLatestTransactionId().getValue() :
                        null;

        // 戻り値生成
        ShipmentRegisterMessageDto messageDto = new ShipmentRegisterMessageDto();

        // 取引改訂を開始する ユースケース呼出し
        String transactionRevisionId =
                        startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(transactionId);

        // 改訂用取引ID
        TransactionRevisionId transactionRevisionIdVo = new TransactionRevisionId(transactionRevisionId);

        // 上記で開始した改訂用取引を取得
        TransactionForRevisionEntity transactionForRevisionEntity =
                        transactionForRevisionRepository.get(transactionRevisionIdVo);
        // 改訂用取引が存在しない場合はエラー
        if (transactionForRevisionEntity == null) {
            throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
        }

        // すでに出荷済みの場合はエラー
        if (transactionForRevisionEntity.isShippedFlag()) {
            toShipmentRegisterMessageDto("ORDER-SHIPPING02-I", new String[] {orderCode}, messageDto);
            return messageDto;
        }

        // キャンセル済みの場合はエラー
        if (TransactionStatus.CANCEL.equals(transactionForRevisionEntity.getTransactionStatus())) {
            toShipmentRegisterMessageDto("ORDER-SHIPPING03-I", new String[] {orderCode}, messageDto);
            return messageDto;
        }

        // 改訂取引を出荷確定する
        transactionForRevisionEntity.openShipment();
        // 非同期処理用メール通知対象設定
        messageDto.setMailOrderCode(new OrderCode(orderCode));

        // 改訂用取引を更新する
        transactionForRevisionRepository.update(transactionForRevisionEntity);

        // 改訂取引を確定する ユースケース呼出し
        OrderReceivedEntity orderReceivedEntity = openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(
                        transactionRevisionIdVo.getValue(), administratorSeq, HTypeProcessType.SHIPMENT, true, true,
                        true
                                                                                                                      );
        // 非同期処理用データ設定
        messageDto.setOrderReceivedEntity(orderReceivedEntity);

        // 改訂用売上を確定する（改訂用請求伝票確定も実施）　※後請求の場合はGMO通信が実施される
        BillingSlipForRevisionOpenSalesResponse response =
                        billingAdapter.openSalesForRevision(new TransactionRevisionId(transactionRevisionId), true);

        try {
            // 取引を取得する
            TransactionEntity transactionEntity = transactionRepository.get(new TransactionId(transactionRevisionId));

            // 売上異常があるかどうかで分岐
            if (ObjectUtils.isNotEmpty(response.getSalesFlag()) && response.getSalesFlag()) {
                // 売上異常がなく後請求の場合は入金確定する
                if (!transactionEntity.isPreClaimFlag()) {
                    transactionEntity.openPayment(new PaymentStatusDetail(false, false));
                }
            } else {
                // 売上フラグがfalse(売上異常)の場合、請求決済エラーを設定
                transactionEntity.settingBillPaymentError();
                // 戻り値へ請求エラー情報を設定
                toShipmentRegisterMessageDto(MSGCD_BILL_PAYMENT_ERROR, new String[] {orderCode}, messageDto);
            }

            // 取引を更新する
            transactionRepository.update(transactionEntity);

            // 改訂用出荷実績を登録する（改訂用出荷伝票の確定も行う）
            shippingAdapter.registShipmentResultForRevision(
                            transactionRevisionIdVo, shipmentStatusConfirmCode, completeShipmentDate, true);

        } catch (Exception e) {

            LOGGER.error("改訂用取引出荷登録実行中にエラーが発生しました。 -- 改訂用取引ID：" + transactionRevisionIdVo.getValue() + "-- 受注番号："
                         + orderReceivedEntity.getOrderCode().getValue(), e);

            //　決済済みの場合は請求不整合エラーメール送信
            if (Boolean.TRUE.equals(response.getGmoCommunicationSuccessFlag())) {
                sendReportMail(orderCode, transactionRevisionId);
                throw new DomainException("PAYMENT_EPAR0006-E");
            }

            throw e;
        }

        return messageDto;
    }

    /**
     * 処理実行後非同期処理
     *
     * @param messageDto
     */
    public void asyncAfterProcess(ShipmentRegisterMessageDto messageDto) {

        if (messageDto != null) {
            if (messageDto.getMailOrderCode() != null) {
                // ユーザーに通知する
                notificationSubAdapter.shipmentNotification(messageDto.getMailOrderCode());
            }

            if (messageDto.getOrderReceivedEntity() != null) {
                // 改訂取引を確定 非同期処理
                openTransactionReviseUseCaseExecuter.asyncAfterProcessInnerLogic(messageDto.getOrderReceivedEntity());
            }
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
            process = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.processtype.shipmentp");
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

    /**
     * 出荷登録実行メッセージDtoに変換
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param messageDto
     */
    private void toShipmentRegisterMessageDto(String messageCode,
                                              String[] args,
                                              ShipmentRegisterMessageDto messageDto) {
        messageDto.setErrCode(messageCode);
        messageDto.setArgs(args);
        messageDto.setErrMessage(AppLevelFacesMessageUtil.getAllMessage(messageCode, args).getMessage());
    }
}