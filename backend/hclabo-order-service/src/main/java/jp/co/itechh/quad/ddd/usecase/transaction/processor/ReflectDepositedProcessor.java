package jp.co.itechh.quad.ddd.usecase.transaction.processor;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.usecase.transaction.ConfirmHMPaymentThenSettingPaidStatusUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.ConfirmHMPaymentThenSettingPaidStatusUseCaseDto;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MQErrorNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionReflectDepositedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HM入金結果データを確認して受注を入金済みにするProcessor
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
@Scope("prototype")
public class ReflectDepositedProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReflectDepositedProcessor.class);

    /** 受注を入金済みにする実行クラス */
    private final ConfirmHMPaymentThenSettingPaidStatusUseCase confirmPaymentThenSettingPaidStatusUseCase;

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** コンストラクタ */
    @Autowired
    public ReflectDepositedProcessor(ConfirmHMPaymentThenSettingPaidStatusUseCase confirmPaymentThenSettingPaidStatusUseCase,
                                     NotificationSubApi notificationSubApi,
                                     AsyncService asyncService) {
        this.confirmPaymentThenSettingPaidStatusUseCase = confirmPaymentThenSettingPaidStatusUseCase;
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
    }

    /**
     * HM入金結果データを確認して受注を入金済みにするProcessor
     * 取引の出荷実績を登録する
     *
     * @param batchQueueMessage キューメッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) {

        LOGGER.info("【Subscribe】ルーティングキー： order-reflectDeposited-routing");
        TransactionReflectDepositedRequest reflectDepositedRequest = new TransactionReflectDepositedRequest();
        try {
            // バッチのジョブ情報取得
            Integer administratorSeq = batchQueueMessage.getAdministratorSeq();
            reflectDepositedRequest.setOrderCode(batchQueueMessage.getOrderCode());

            // HM入金結果データを確認して受注を入金済みにする
            ConfirmHMPaymentThenSettingPaidStatusUseCaseDto dto =
                            confirmPaymentThenSettingPaidStatusUseCase.reflectDeposited(
                                            reflectDepositedRequest.getOrderCode(), administratorSeq);
            confirmPaymentThenSettingPaidStatusUseCase.asyncAfterProcess(dto);

            // 正常に完了したらメールを送信する。
            MulpayNotificationRecoveryAdministratorRequest successMailRequest = new MulpayNotificationRecoveryAdministratorRequest();
            successMailRequest.setNormalWorkCount(1);
            successMailRequest.setNormalErrorCount(0);

            Object[] successMailRequestObj = new Object[] {successMailRequest};
            Class<?>[] successMailRequestObjClass = new Class<?>[] {MulpayNotificationRecoveryAdministratorRequest.class};

            asyncService.asyncService(notificationSubApi, "mulpayNotificationRecoveryAdministratorMail", successMailRequestObj, successMailRequestObjClass);
        } catch (Exception e) {
            LOGGER.error("処理中に予期せぬエラーが発生したため異常終了しています。", e);

            sendErrorMail(e, reflectDepositedRequest.getOrderCode());
            throw e;
        }
    }

    /**
     * エラーメール送信　TODO　MQ異常系暫定対応
     *
     * @param e
     * @param orderCode
     */
    private void sendErrorMail(Exception e, String orderCode) {

        String exceptionInfo = "";

        if (e instanceof AppLevelListException) {
            for (AppLevelException ae : ((AppLevelListException) e).getErrorList()) {
                exceptionInfo = exceptionInfo + "：" + ae.getMessageCode() + ae.getMessage() + "\n";
            }

        } else if (e instanceof DomainException) {
            Set<Map.Entry<String, List<ExceptionContent>>> entries = ((DomainException) e).getMessageMap().entrySet();

            for (Map.Entry<String, List<ExceptionContent>> entry : entries) {
                for (ExceptionContent ec : entry.getValue()) {
                    exceptionInfo = exceptionInfo + "：" + ec.getCode() + " " + ec.getMessage() + "\n";
                }
            }
        } else {
            exceptionInfo = e.getClass().getName();
        }

        Map<String, String> placeHolders = new LinkedHashMap<>();
        String process = PropertiesUtil.getSystemPropertiesValue("reflect.deposited.process");
        placeHolders.put("PROCESS", process);
        placeHolders.put("ORDER_CODE", orderCode);
        String recovery = PropertiesUtil.getSystemPropertiesValue("reflect.deposited.recovery.message");
        placeHolders.put("RECOVERY", recovery);

        MQErrorNotificationRequest mqErrorNotificationRequest = new MQErrorNotificationRequest();
        mqErrorNotificationRequest.setExceptionInfo(exceptionInfo);
        mqErrorNotificationRequest.setPlaceHolders(placeHolders);

        Object[] args = new Object[] {mqErrorNotificationRequest};
        Class<?>[] argsClass = new Class<?>[] {MQErrorNotificationRequest.class};
        asyncService.asyncService(notificationSubApi, "mqErrorNotification", args, argsClass);

        MulpayNotificationRecoveryAdministratorErrorRequest mailErrorRequest = new MulpayNotificationRecoveryAdministratorErrorRequest();
        mailErrorRequest.setWorkCount(0);
        mailErrorRequest.setMessage(e.getMessage());
        mailErrorRequest.setExceptionName(exceptionInfo);

        // 処理が異常終了した場合に管理者メールを送信します。
        Object[] mailErrorRequestObj = new Object[] {mailErrorRequest};
        Class<?>[] mailErrorRequestObjClass = new Class<?>[] {MulpayNotificationRecoveryAdministratorErrorRequest.class};

        asyncService.asyncService(notificationSubApi, "mulpayNotificationRecoveryAdministratorErrorMail", mailErrorRequestObj, mailErrorRequestObjClass);
    }

}