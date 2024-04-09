/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.billingslip.presentation.api.param.WarningContent;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 改訂用請求伝票取消ユースケース
 */
@Service
public class CancelBillingSlipForRevisionUseCase {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelBillingSlipForRevisionUseCase.class);

    /** 請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 注文決済ドメインサービス */
    private final OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService;

    /** 改訂用請求伝票確定ユースケース */
    private final OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 請求不整合エラーメール文言取得失敗用 */
    private final static String MAIL_CONTENT_FAIL = "(設定失敗)";

    /** コンストラクタ */
    @Autowired
    public CancelBillingSlipForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                               OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService,
                                               OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase,
                                               INotificationSubAdapter notificationSubAdapter) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.orderPaymentForRevisionEntityService = orderPaymentForRevisionEntityService;
        this.openBillingSlipReviseUseCase = openBillingSlipReviseUseCase;
        this.notificationSubAdapter = notificationSubAdapter;
    }

    /**
     * 改訂用請求伝票を取消する
     *
     * @param transactionRevisionId 取引ID
     * @param revisionOpenFlag
     */
    public CancelBillingSlipForRevisionUseCaseDto cancelBillingSlip(String transactionRevisionId,
                                                                    boolean revisionOpenFlag,
                                                                    String administratorId) {
        CancelBillingSlipForRevisionUseCaseDto cancelBillingSlipForRevisionUseCaseDto =
                        new CancelBillingSlipForRevisionUseCaseDto();

        // 改訂用取引IDに紐づく改訂用請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 請求伝票が存在しない、または確定状態でない場合は処理をスキップする
        if (billingSlipForRevisionEntity == null
            || billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderPaymentStatus()
               != OrderPaymentStatus.OPEN) {
            if (revisionOpenFlag) {
                openBillingSlipReviseUseCase.openBillingSlipRevise(transactionRevisionId, true, administratorId);
            }
            return null;
        }

        // 警告メッセージの初期化
        Map<String, List<WarningContent>> warningMessage = new HashMap<>();

        // 改訂用注文決済を取消する
        boolean isGmoExec = orderPaymentForRevisionEntityService.cancelOrderPayment(
                        billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity(), warningMessage);

        try {
            // 改訂用請求伝票を取消する
            if (isGmoExec) {
                billingSlipForRevisionEntity.cancelSlip(new Date(), 0);
            } else {
                // 累計入金額を更新しない
                billingSlipForRevisionEntity.cancelSlip(new Date(), null);
            }

            // 改訂用請求伝票を更新する
            billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);

            cancelBillingSlipForRevisionUseCaseDto.setPaidFlag(billingSlipForRevisionEntity.isPaid());
            cancelBillingSlipForRevisionUseCaseDto.setInsufficientMoneyFlag(
                            billingSlipForRevisionEntity.isInsufficientMoney());
            cancelBillingSlipForRevisionUseCaseDto.setOverMoneyFlag(billingSlipForRevisionEntity.isOverMoney());
            cancelBillingSlipForRevisionUseCaseDto.setGmoCommunicationFlag(isGmoExec);
            cancelBillingSlipForRevisionUseCaseDto.setWarningMessage(warningMessage);

            if (revisionOpenFlag) {
                openBillingSlipReviseUseCase.openBillingSlipRevise(transactionRevisionId, true, administratorId);
            }
        } catch (Exception e) {

            LOGGER.error("改訂用請求伝票確定（キャンセル）でエラー -- 受注番号："
                         + billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode()
                         + "  -- 改訂用取引ID：" + transactionRevisionId, e);

            //　決済済みの場合は請求不整合エラーメール送信
            if (isGmoExec) {

                String orderCodeSendMail = MAIL_CONTENT_FAIL;
                if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode() != null) {
                    orderCodeSendMail = billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode();
                }

                sendSettlementMisMatchMail(orderCodeSendMail, transactionRevisionId);

                throw new DomainException("PAYMENT_EPAR0006-E");
            }

            throw e;
        }

        return cancelBillingSlipForRevisionUseCaseDto;
    }

    /**
     * 請求不整合報告メールを送信する
     *
     * @param orderCode             受注番号
     * @param transactionRevisionId 改訂用取引ID
     */
    private void sendSettlementMisMatchMail(String orderCode, String transactionRevisionId) {
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
            process = PropertiesUtil.getSystemPropertiesValue("settlement.mismatch.processtype.cancelslip");
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