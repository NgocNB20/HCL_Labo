/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.notification.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.constant.type.HTypeNotificationSubResult;
import jp.co.itechh.quad.notification.queue.MessageQueuePublisher;
import jp.co.itechh.quad.notificationsub.presentation.api.UsersApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.AuthTimeLimitErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.AuthTimeLimitRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CategoryGoodsRegistUpdateErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CertificationCodeRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ClearResultErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ClearResultRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CreditLineReportErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CreditLineReportRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.EmailModificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsEntryRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsNoticeRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamkitReceivedEntryRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GmoMemberCardAlertRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.InquiryRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.LinkpayCancelReminderRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MQErrorNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailMagazineProcessCompleteRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberPreregistrationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberinfoProcessCompleteRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MultiPaymentAlertRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.NotificationsResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderConfirmationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderRegistAlertRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PasswordNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentDepositedRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentExcessAlertRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementAdministratorErrorMailRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementAdministratorMailRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementExpirationNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementReminderRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ShipmentNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ShipmentRegistRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockReleaseAdministratorErrorMailRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockReportRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockStatusRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.TagClearErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ZipcodeUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 通知サブドメイン Controller
 *
 * @author Doan Thang (VJP)
 */
@RestController
public class NotificationSubController implements UsersApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(NotificationSubController.class);

    /** キューパブリッシャーサービス */
    private final MessageQueuePublisher messageQueuePublisher;

    /** キューメッセージコートバッチ */
    private final static String BATCH_MESSAGE_CODE_INFO = "AEB000102";

    /** キューメッセージコートバッチ */
    private final static String BATCH_MESSAGE_CODE_FAIL = "AEB000101";

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "通知";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "通知の失敗： ";

    /**
     * コンストラクタ
     *
     * @param messageQueuePublisher キューパブリッシャーサービス
     */
    @Autowired
    public NotificationSubController(MessageQueuePublisher messageQueuePublisher) {
        this.messageQueuePublisher = messageQueuePublisher;
    }

    /**
     * パスワード再設定メール送信
     *
     * @param passwordNotificationRequest パスワード再設定メール送信リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> passwordNotification(
                    @ApiParam(value = "パスワード再設定メール送信リクエスト", required = true) @Valid @RequestBody
                                    PasswordNotificationRequest passwordNotificationRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.password-notification.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, passwordNotificationRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("パスワード再設定メール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * オーソリ期限切れ間近注文通知
     *
     * @param authTimeLimitRequest オーソリ期限切れ間近注文通知リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> authTimeLimit(
                    @ApiParam(value = "オーソリ期限切れ間近注文通知リクエスト", required = true) @Valid @RequestBody
                                    AuthTimeLimitRequest authTimeLimitRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.auth-time-limit.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, authTimeLimitRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("オーソリ期限切れ間近注文通知" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * オーソリ期限切れ間近注文異常
     *
     * @param authTimeLimitErrorRequest オーソリ期限切れ間近注文異常リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> authTimeLimitError(
                    @ApiParam(value = "オーソリ期限切れ間近注文異常リクエスト", required = true) @Valid @RequestBody
                                    AuthTimeLimitErrorRequest authTimeLimitErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.auth-time-limit-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, authTimeLimitErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("オーソリ期限切れ間近注文異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * クリア通知
     *
     * @param clearResultRequest クリア通知リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> clearResult(
                    @ApiParam(value = "クリア通知リクエスト", required = true) @Valid @RequestBody
                                    ClearResultRequest clearResultRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.clear-result.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, clearResultRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("クリア通知" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * クリア異常
     *
     * @param clearResultErrorRequest クリア異常リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> clearResultError(
                    @ApiParam(value = "クリア異常リクエスト", required = true) @Valid @RequestBody
                                    ClearResultErrorRequest clearResultErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.clear-result-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, clearResultErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("クリア異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理者に与信枠解放メールを送信する
     *
     * @param creditLineReportRequest クレジットラインレポートリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> creditLineReport(
                    @ApiParam(value = "クレジットラインレポートリクエスト", required = true) @Valid @RequestBody
                                    CreditLineReportRequest creditLineReportRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.credit-line-report.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, creditLineReportRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("管理者に与信枠解放メールを送信する" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理者向けエラー通知メールを送信する
     *
     * @param creditLineReportErrorRequest クレジットラインレポートエラーリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> creditLineReportError(
                    @ApiParam(value = "クレジットラインレポートエラーリクエスト", required = true) @Valid @RequestBody
                                    CreditLineReportErrorRequest creditLineReportErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.credit-line-report-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, creditLineReportErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("管理者向けエラー通知メールを送信する" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 商品グループ規格画像更新（商品一括アップロード）通知
     *
     * @param goodsAsynchronousRequest 商品グループ規格画像更新（商品一括アップロード）通知リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> goodsAsynchronous(
                    @ApiParam(value = "商品グループ規格画像更新（商品一括アップロード）通知リクエスト", required = true) @Valid @RequestBody
                                    GoodsAsynchronousRequest goodsAsynchronousRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.goods-asynchronous.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, goodsAsynchronousRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("商品グループ規格画像更新（商品一括アップロード）通知" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 商品グループ規格画像更新（商品一括アップロード）異常
     *
     * @param goodsAsynchronousErrorRequest 商品グループ規格画像更新（商品一括アップロード）異常リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> goodsAsynchronousError(
                    @ApiParam(value = "商品グループ規格画像更新（商品一括アップロード）異常リクエスト", required = true) @Valid @RequestBody
                                    GoodsAsynchronousErrorRequest goodsAsynchronousErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.goods-asynchronous-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, goodsAsynchronousErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("商品グループ規格画像更新（商品一括アップロード）異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 商品グループ規格画像更新（商品画像更新）通知
     *
     * @param goodsImageUpdateRequest 商品グループ規格画像更新通知リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> goodsImageUpdate(
                    @ApiParam(value = "商品グループ規格画像更新通知リクエスト", required = true) @Valid @RequestBody
                                    GoodsImageUpdateRequest goodsImageUpdateRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.goods-image-update.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, goodsImageUpdateRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("商品グループ規格画像更新（商品画像更新）通知" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理者向けメールを送信する
     *
     * @param goodsImageUpdateErrorRequest クレジットラインレポートリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> goodsImageUpdateError(
                    @ApiParam(value = "クレジットラインレポートリクエスト", required = true) @Valid @RequestBody
                                    GoodsImageUpdateErrorRequest goodsImageUpdateErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.goods-image-update-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, goodsImageUpdateErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("管理者向けメールを送信する" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * メルマガ登録完了
     *
     * @param mailMagazineProcessCompleteRequest メルマガ登録完了リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> mailMagazineProcessComplete(
                    @ApiParam(value = "メルマガ登録完了リクエスト", required = true) @Valid @RequestBody
                                    MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.mail-magazine-process-complete.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, mailMagazineProcessCompleteRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("メルマガ登録完了" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 仮会員登録
     *
     * @param memberPreregistrationRequest 仮会員登録リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> memberPreregistration(
                    @ApiParam(value = "仮会員登録リクエスト", required = true) @Valid @RequestBody
                                    MemberPreregistrationRequest memberPreregistrationRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.member-preregistration.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, memberPreregistrationRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("仮会員登録" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * マルチペイメントアラート
     *
     * @param multiPaymentAlertRequest マルチペイメントアラートリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> multiPaymentAlert(
                    @ApiParam(value = "マルチペイメントアラートリクエスト", required = true) @Valid @RequestBody
                                    MultiPaymentAlertRequest multiPaymentAlertRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.multi-payment-alert.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, multiPaymentAlertRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("マルチペイメントアラート" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 【管理者向け】入金結果受付予備処理バッチ結果メール送信要求
     *
     * @param mulpayNotificationRecoveryAdministratorRequest 入金結果受付予備処理結果メールリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> mulpayNotificationRecoveryAdministratorMail(
                @ApiParam(value = "入金結果受付予備処理結果メールリクエスト", required = true) @Valid @RequestBody
                        MulpayNotificationRecoveryAdministratorRequest mulpayNotificationRecoveryAdministratorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.mulpay-notification-recovery-administrator.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, mulpayNotificationRecoveryAdministratorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("管理者向けメールを送信する" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 【管理者向け】入金結果受付予備処理バッチ結果異常メール送信要求
     *
     * @param mulpayNotificationRecoveryAdministratorErrorRequest 入金結果受付予備処理結果異常メールリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> mulpayNotificationRecoveryAdministratorErrorMail(
                @ApiParam(value = "入金結果受付予備処理結果異常メールリクエスト", required = true) @Valid @RequestBody
                MulpayNotificationRecoveryAdministratorErrorRequest mulpayNotificationRecoveryAdministratorErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.mulpay-notification-recovery-administrator-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, mulpayNotificationRecoveryAdministratorErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("管理者向けエラーメールを送信する" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 注文確認
     *
     * @param orderConfirmationRequest 注文確認リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> orderConfirmation(
                    @ApiParam(value = "注文確認リクエスト", required = true) @Valid @RequestBody
                                    OrderConfirmationRequest orderConfirmationRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.order-confirmation.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, orderConfirmationRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("注文確認" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DL用メールを送信
     *
     * @param downloadCsvRequest CSVダウンロードメールリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> downloadCsv(@Valid DownloadCsvRequest downloadCsvRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.download-csv.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, downloadCsvRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("DL用メールを送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理者宛てエラーメールを送信
     *
     * @param downloadCsvErrorRequest CSVダウンロードエラーメールエラーリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> downloadCsvError(
                    @Valid DownloadCsvErrorRequest downloadCsvErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.download-csv-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, downloadCsvErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("管理者宛てエラーメールを送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 【管理者向け】支払督促メール送信／支払期限切れ処理バッチ結果メール送信要求
     *
     * @param settlementAdministratorMailRequest 支払督促メール送信／支払期限切れ処理結果リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> settlementAdministratorMail(
            @Valid SettlementAdministratorMailRequest settlementAdministratorMailRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.settlement-administrator.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, settlementAdministratorMailRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("受注決済期限切れメール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 【管理者向け】支払督促メール送信／支払期限切れ処理バッチ結果エラーメール送信要求
     *
     * @param settlementAdministratorErrorMailRequest 支払督促メール送信／支払期限切れ処理結果エラーリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> settlementAdministratorErrorMail(
                @ApiParam(value = "支払督促メール送信／支払期限切れ処理結果エラーリクエスト", required = true) @Valid @RequestBody
                SettlementAdministratorErrorMailRequest settlementAdministratorErrorMailRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.settlement-administrator-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, settlementAdministratorErrorMailRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("受注決済期限切れメール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 受注決済期限切れメール送信
     *
     * @param settlementExpirationNotificationRequest 受注決済期限切れメール送信リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> settlementExpirationNotificationMail(
                    @ApiParam(value = "受注決済期限切れメール送信リクエスト", required = true) @Valid @RequestBody
                                    SettlementExpirationNotificationRequest settlementExpirationNotificationRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.settlement-expiration-notification.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, settlementExpirationNotificationRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("受注決済期限切れメール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 受注決済督促
     *
     * @param settlementReminderRequest 受注決済督促リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> settlementReminder(
                    @ApiParam(value = "受注決済督促リクエスト", required = true) @Valid @RequestBody
                                    SettlementReminderRequest settlementReminderRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.settlement-reminder.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, settlementReminderRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("受注決済督促" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 出荷完了メール送信
     *
     * @param shipmentNotificationRequest 出荷完了メール送信リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> shipmentNotification(
                    @ApiParam(value = "出荷完了メール送信リクエスト", required = true) @Valid @RequestBody
                                    ShipmentNotificationRequest shipmentNotificationRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.shipment-notification.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, shipmentNotificationRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("出荷完了メール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 出荷登録異常
     *
     * @param shipmentRegistRequest 出荷登録異常リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> shipmentRegist(
                    @ApiParam(value = "出荷登録異常リクエスト", required = true) @Valid @RequestBody
                                    ShipmentRegistRequest shipmentRegistRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.shipment-regist.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, shipmentRegistRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("出荷登録異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 在庫開放
     *
     * @param stockReleaseAdministratorErrorMailRequest 在庫開放リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> stockRelease(
                    @ApiParam(value = "在庫開放リクエスト", required = true) @Valid @RequestBody
                                    StockReleaseAdministratorErrorMailRequest stockReleaseAdministratorErrorMailRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.stock-release.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, stockReleaseAdministratorErrorMailRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("在庫開放" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 在庫状況を管理者にメール送信
     *
     * @param stockReportRequest 在庫状況を管理者にメール送信リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> stockReport(
                    @ApiParam(value = "在庫状況を管理者にメール送信リクエスト", required = true) @Valid @RequestBody
                                    StockReportRequest stockReportRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.stock-report.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, stockReportRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("在庫状況を管理者にメール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 商品グループ在庫状態更新異常
     *
     * @param stockStatusRequest 商品グループ在庫状態更新異常リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> stockStatus(
                    @ApiParam(value = "商品グループ在庫状態更新異常リクエスト", required = true) @Valid @RequestBody
                                    StockStatusRequest stockStatusRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.stock-status.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, stockStatusRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("商品グループ在庫状態更新異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /users/mail/mq-error-notification : 非同期処理(MQ)エラー通知
     *
     * @param mqErrorNotificationRequest 非同期処理(MQ)エラー通知リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> mqErrorNotification(
                    @Valid MQErrorNotificationRequest mqErrorNotificationRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.mq-error-notification.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, mqErrorNotificationRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage(BATCH_MESSAGE_CODE_INFO,
                                                                     new Object[] {messageResponseParam}
                                                                    ).getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("非同期処理(MQ)エラー通知処理異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage(BATCH_MESSAGE_CODE_FAIL,
                                                                     new Object[] {messageResponseParam}
                                                                    ).getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理者向け処理完了メールを送信する
     *
     * @param zipcodeUpdateRequest マルチペイメントリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> zipcodeUpdate(
                    @ApiParam(value = "マルチペイメントリクエスト", required = true) @Valid @RequestBody
                                    ZipcodeUpdateRequest zipcodeUpdateRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.zipcode-update.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, zipcodeUpdateRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("管理者向け処理完了メールを送信する" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 注文データ作成アラート
     *
     * @param orderRegistAlertRequest 注文データ作成アラートリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> orderRegistAlert(
                    @ApiParam(value = "注文データ作成アラートリクエスト", required = true) @Valid @RequestBody
                                    OrderRegistAlertRequest orderRegistAlertRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.order-regist-alert.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, orderRegistAlertRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("注文データ作成アラート" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * メールアドレス変更確認
     *
     * @param emailModificationRequest メールアドレス変更確認リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> emailModification(
                    @ApiParam(value = "メールアドレス変更確認リクエスト", required = true) @Valid @RequestBody
                                    EmailModificationRequest emailModificationRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.email-modification.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, emailModificationRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("メールアドレス変更確認" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GMO会員カードアラート
     *
     * @param gmoMemberCardAlertRequest GMO会員カードアラートリクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> gmoMemberCardAlert(
                    @ApiParam(value = "GMO会員カードアラートリクエスト", required = true) @Valid @RequestBody
                                    GmoMemberCardAlertRequest gmoMemberCardAlertRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.gmo-member-card-alert.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, gmoMemberCardAlertRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("GMO会員カードアラート" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 問い合わせメール送信
     *
     * @param inquiryRequest 問い合わせメール送信リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> inquiry(
                    @ApiParam(value = "問い合わせメール送信リクエスト", required = true) @Valid @RequestBody
                                    InquiryRequest inquiryRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.inquiry.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, inquiryRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("問い合わせメール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 会員処理完了メール送信
     *
     * @param memberinfoProcessCompleteRequest 会員処理完了メール送信リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> memberinfoProcessComplete(
                    @ApiParam(value = "会員処理完了メール送信リクエスト", required = true) @Valid @RequestBody
                                    MemberinfoProcessCompleteRequest memberinfoProcessCompleteRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.memberinfo-process-complete.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, memberinfoProcessCompleteRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("会員処理完了メール送信" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 請求不整合報告
     *
     * @param settlementMismatchRequest 請求不整合報告リクエスト
     * @return 通知サブドメインレスポンス
     */
    @Override
    public ResponseEntity<NotificationsResponse> settlementMismatch(
                    @ApiParam(value = "請求不整合報告リクエスト", required = true) @Valid @RequestBody
                                    SettlementMismatchRequest settlementMismatchRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.settlement-mismatch.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, settlementMismatchRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("請求不整合報告" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /users/mail/tag-clear-error : タグクリアバッチ異常
     * タグクリアバッチ異常
     *
     * @param tagClearErrorRequest タグクリアバッチ異常リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> tagClearError(
                    @ApiParam(value = "タグクリアバッチ異常リクエスト", required = true) @Valid @RequestBody
                                    TagClearErrorRequest tagClearErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.tag-clear.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, tagClearErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("タグクリアバッチ異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /users/mail/categoryGoods-registUpdate-error : カテゴリ商品更新バッチ異常
     * カテゴリ商品更新バッチ異常
     *
     * @param categoryGoodsRegistUpdateErrorRequest カテゴリ商品更新バッチ異常リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> categoryGoodsRegistUpdateError(
                    @ApiParam(value = "カテゴリ商品更新バッチ異常リクエスト", required = true) @Valid @RequestBody
                                    CategoryGoodsRegistUpdateErrorRequest categoryGoodsRegistUpdateErrorRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.category-goods-regist-update-error.routing");

        // レスポンス
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, categoryGoodsRegistUpdateErrorRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("カテゴリ商品更新バッチ異常" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /users/mail/link-pay/cancel-reminder : GMO決済キャンセル漏れメール送信要求
     * GMO決済キャンセル漏れメール送信要求
     *
     * @param linkpayCancelReminderRequest GMO決済キャンセル漏れメール送信要求リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> linkpayCancelReminder(
                    @ApiParam(value = "GMO決済キャンセル漏れメール送信要求リクエスト", required = true) @Valid @RequestBody
                                    LinkpayCancelReminderRequest linkpayCancelReminderRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.linkpay-cancel-reminder.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, linkpayCancelReminderRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("請求不整合報告" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /users/mail/payment-deposited : 入金完了メール送信要求
     * 入金完了メール送信要求
     *
     * @param paymentDepositedRequest 入金完了メール送信要求求リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> paymentDeposited(
                    @Valid @RequestBody PaymentDepositedRequest paymentDepositedRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.payment-deposited.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;
        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, paymentDepositedRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("請求不整合報告" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * POST /users/mail/payment-excess-alert : 入金過不足アラートメール送信要求（管理者）
     * 入金過不足アラートメール送信要求
     *
     * @param paymentExcessAlertRequest 入金過不足アラートメール送信要求リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> paymentExcessAlert(
                    @ApiParam(value = "入金過不足アラートメール送信要求リクエスト", required = true) @Valid @RequestBody
                                    PaymentExcessAlertRequest paymentExcessAlertRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.payment-excess-alert.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, paymentExcessAlertRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("入金過不足アラートメール送信パブリッシュー失敗" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /users/mail/certificationcode : 認証コードメール送信要求
     * <span class="ldMUmp">カスタマイズ</span>
     * - 認証コードメールを非同期メール送信バッチに依頼する
     * <h5 class="eONCmm">業務詳細</h5>
     * - パラメータの会員SEQに該当するユーザーに向けて、発行した認証コードをメールにて送付する
     * <h5 class="eONCmm">制約/ルール</h5>
     * - メール送信の失敗は非同期のため検知不可とする
     *
     * @param certificationCodeRequest 認証コードメール送信リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> certificationCode(
            @ApiParam(value = "認証コードメール送信リクエスト", required = true) @Valid @RequestBody
            CertificationCodeRequest certificationCodeRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.certification-code.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, certificationCodeRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                    .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("認証コードメール送信パブリッシュー失敗" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                    .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /users/mail/examkit-received-entry : 検査キット受領登録異常要求 <br/>
     *  <span class="ldMUmp">カスタマイズ</span>
     *  - 検査キット受領登録異常要求  <br/>
     *  <h5 class="eONCmm">業務詳細</h5>
     *  - 検査キット受領登録処理内でエラーが発生した場合や、異常終了時に管理者へ`検査キット受領登録バッチ（エラー通知）メール`を送信する  <br/>
     *  <h5 class="eONCmm">制約/ルール</h5>
     *  ‐ 特になし
     *
     * @param examkitReceivedEntryRequest 検査キット受領登録バッチ（エラー通知）異常リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> examkitReceivedEntry(@Valid ExamkitReceivedEntryRequest examkitReceivedEntryRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.examkit-received-entry.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, examkitReceivedEntryRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000103", new Object[]{messageResponseParam})
                .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("検査キット受領登録異常トメール送信パブリッシュー失敗" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[]{messageResponseParam})
                .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * POST /users/mail/exam-results-entry : 検査結果登録異常要求 <br/>
     * <span class="ldMUmp">カスタマイズ</span>
     * - 検査結果登録異常要求 <br/>
     * <h5 class="eONCmm">業務詳細</h5>
     * - 検査結果登録処理内でエラーが発生した場合や、異常終了時に管理者へ`検査結果登録バッチ（エラー通知）メール`のみ送信する <br/>
     * <h5 class="eONCmm">制約/ルール</h5>
     * - 特になし <br/>
     *
     * @param examResultsEntryRequest 検査結果登録バッチ（エラー通知）異常リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> examResultsEntry(
            @ApiParam(value = "検査結果登録バッチ（エラー通知）異常リクエスト", required = true)
            @RequestBody @Valid ExamResultsEntryRequest examResultsEntryRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.exam-results-entry.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, examResultsEntryRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000103", new Object[]{messageResponseParam})
                    .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("検査結果登録異常トメール送信パブリッシュー失敗" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[]{messageResponseParam})
                    .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * POST /users/mail/exam-results-notice : 検査結果通知メール送信要求 <br/>
     * <span class="ldMUmp">カスタマイズ</span>
     * - 検査結果通知メール送信要求 <br/>
     * <h5 class="eONCmm">業務詳細</h5>
     * - 検査キット情報や受注情報からメッセージ本文を組み立てて、注文主へ`検査結果通知メール`を送信する <br/>
     * <h5 class="eONCmm">制約/ルール</h5>
     * - 特になし <br/>
     *
     * @param examResultsNoticeRequest 検査結果通知メール送信リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<NotificationsResponse> examResultsNotice(
            @ApiParam(value = "検査結果通知メール送信リクエスト", required = true)
            @RequestBody @Valid ExamResultsNoticeRequest examResultsNoticeRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.exam-results-notice.routing");

        // レスポンス作成
        NotificationsResponse response = new NotificationsResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messageQueuePublisher.publish(routing, examResultsNoticeRequest);

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000103", new Object[]{messageResponseParam})
                    .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("検査結果通知メール送信パブリッシュー失敗" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeNotificationSubResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[]{messageResponseParam})
                    .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}