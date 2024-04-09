/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 改訂用請求伝票確定ユースケース
 */
@Service
public class OpenBillingSlipReviseUseCase {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenBillingSlipReviseUseCase.class);

    /** 改訂用請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 改訂用請求伝票決済請求委託ユースケース */
    private final EntrustPaymentAgencyForRevisionUseCase entrustPaymentAgencyForRevisionUseCase;

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 請求不整合エラーメール文言取得失敗用 */
    private final static String MAIL_CONTENT_FAIL = "(設定失敗)";

    /** コンストラクタ */
    @Autowired
    public OpenBillingSlipReviseUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                        EntrustPaymentAgencyForRevisionUseCase entrustPaymentAgencyForRevisionUseCase,
                                        IBillingSlipRepository billingSlipRepository,
                                        INotificationSubAdapter notificationSubAdapter) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.entrustPaymentAgencyForRevisionUseCase = entrustPaymentAgencyForRevisionUseCase;
        this.billingSlipRepository = billingSlipRepository;
        this.notificationSubAdapter = notificationSubAdapter;
    }

    /**
     * 改訂用請求伝票を確定する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param settlementSkipFlag
     * @param administratorId
     * @return true ... 請求決済エラー解消 / false ... 請求決済エラー未発生 or 未解消
     */
    public OpenBillingSlipReviseUseCaseDto openBillingSlipRevise(String transactionRevisionId,
                                                                 boolean settlementSkipFlag,
                                                                 String administratorId) {

        OpenBillingSlipReviseUseCaseDto openBillingSlipReviseUseCaseDto = new OpenBillingSlipReviseUseCaseDto();

        // 決済スキップフラグ=falseの場合は、決済代行に連携するユースケース呼び出し
        if (!settlementSkipFlag) {
            boolean gmoCommunicationExec =
                            entrustPaymentAgencyForRevisionUseCase.entrustPaymentAgencyForRevision(administratorId,
                                                                                                   transactionRevisionId
                                                                                                  );
            openBillingSlipReviseUseCaseDto.setGmoCommunicationExec(gmoCommunicationExec);
        }

        // 改訂済みチェック
        // 改訂用取引IDをキーに、取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntityForCheck =
                        this.billingSlipRepository.getByTransactionId(transactionRevisionId);
        if (billingSlipEntityForCheck != null) {
            // 改訂済データが存在していれば、処理終了
            openBillingSlipReviseUseCaseDto.setBillPaymentErrorReleaseFlag(false);
            return openBillingSlipReviseUseCaseDto;
        }

        // 改訂用取引IDに紐づく改訂用請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        this.billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (billingSlipForRevisionEntity == null) {
            throw new DomainException("PAYMENT_EPAR0001-E", new String[] {transactionRevisionId});
        }

        // 改訂元の取引IDで請求伝票を取得する
        BillingSlipEntity billingSlipForOriginEntity =
                        this.billingSlipRepository.getByTransactionId(billingSlipForRevisionEntity.getTransactionId());

        try {
            // 請求決済エラー解除判定
            boolean paymentAgencyReleaseFlag = false;
            // クレジット決済の場合
            if (billingSlipForOriginEntity.getOrderPaymentEntity().getPaymentRequest() instanceof CreditPayment) {
                CreditPayment creditPaymentForOrigin =
                                (CreditPayment) billingSlipForOriginEntity.getOrderPaymentEntity().getPaymentRequest();
                paymentAgencyReleaseFlag = creditPaymentForOrigin.isGmoReleaseFlag();
            }

            // 請求伝票改訂を実行
            BillingSlipEntity billingSlipEntity =
                            new BillingSlipEntity(billingSlipForRevisionEntity, new Date(), paymentAgencyReleaseFlag);
            this.billingSlipRepository.save(billingSlipEntity);

            // 請求決済エラーを解除したかどうか
            openBillingSlipReviseUseCaseDto.setBillPaymentErrorReleaseFlag(
                            billingSlipEntity.isReleaseBillPaymentError(billingSlipForRevisionEntity));

        } catch (Exception e) {

            LOGGER.error("改訂用請求伝票確定でエラー -- 受注番号：" + billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                                                                .getOrderCode() + "  -- 改訂用取引ID："
                         + transactionRevisionId, e);

            //　決済済みの場合は請求不整合エラーメール送信
            if (openBillingSlipReviseUseCaseDto.isGmoCommunicationExec()) {

                String orderCodeSendMail = MAIL_CONTENT_FAIL;
                if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode() != null) {
                    orderCodeSendMail = billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode();
                }

                sendSettlementMisMatchMail(orderCodeSendMail, transactionRevisionId);

                throw new DomainException("PAYMENT_EPAR0006-E");
            }

            throw e;
        }
        return openBillingSlipReviseUseCaseDto;
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