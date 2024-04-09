/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.ddd.presentation.linkpay;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.presentation.card.CardController;
import jp.co.itechh.quad.ddd.usecase.linkpay.CheckLaterDatePaymentExpiredUseCase;
import jp.co.itechh.quad.ddd.usecase.linkpay.CheckLaterDatePaymentReminderUseCase;
import jp.co.itechh.quad.ddd.usecase.linkpay.ReceiveLinkPayUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.linkpay.ReceiveLinkPaymentResultUseCase;
import jp.co.itechh.quad.linkpay.presentation.api.PaymentsApi;
import jp.co.itechh.quad.linkpay.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.linkpay.presentation.api.param.LaterDatePaymentExpiredCheckRequest;
import jp.co.itechh.quad.linkpay.presentation.api.param.LaterDatePaymentExpiredCheckResponse;
import jp.co.itechh.quad.linkpay.presentation.api.param.LaterDatePaymentReminderCheckRequest;
import jp.co.itechh.quad.linkpay.presentation.api.param.LaterDatePaymentReminderCheckResponse;
import jp.co.itechh.quad.linkpay.presentation.api.param.ReceiveLinkPaymentRequest;
import jp.co.itechh.quad.linkpay.presentation.api.param.ReceiveLinkPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * リンク決済　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class LinkPayController extends AbstractController implements PaymentsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** リンク決済(後日払い)の支払期限切れ間近を判定ユースケース */
    private final CheckLaterDatePaymentReminderUseCase checkLaterDatePaymentReminderUseCase;

    /** リンク決済(後日払い)の支払期限切れ判定ユースケース */
    private final CheckLaterDatePaymentExpiredUseCase checkLaterDatePaymentExpiredUseCase;

    /** リンク決済結果を受取るユースケース */
    private final ReceiveLinkPaymentResultUseCase receiveLinkPaymentResultUseCase;

    /** コンストラクタ */
    public LinkPayController(CheckLaterDatePaymentReminderUseCase checkLaterDatePaymentReminderUseCase,
                             CheckLaterDatePaymentExpiredUseCase checkLaterDatePaymentExpiredUseCase,
                             ReceiveLinkPaymentResultUseCase receiveLinkPaymentResultUseCase,
                             MessagePublisherService messagePublisherService) {
        this.checkLaterDatePaymentReminderUseCase = checkLaterDatePaymentReminderUseCase;
        this.checkLaterDatePaymentExpiredUseCase = checkLaterDatePaymentExpiredUseCase;
        this.receiveLinkPaymentResultUseCase = receiveLinkPaymentResultUseCase;
        this.messagePublisherService = messagePublisherService;
    }

    /**
     * GET /payments/link-pay/later-date-payment/payment-expired : リンク決済(後日払い)の支払期限切れ判定する
     * リンク決済(後日払い)の支払期限切れ判定する
     *
     * @param laterDatePaymentExpiredCheck 支払期限切れ判定リクエスト (required)
     * @return 支払期限切れ判定レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<LaterDatePaymentExpiredCheckResponse> checkLaterDatePaymentExpired(@NotNull
                                                                                             @ApiParam(value = "支払期限切れ判定リクエスト",
                                                                                                       required = true)
                                                                                             @Valid LaterDatePaymentExpiredCheckRequest laterDatePaymentExpiredCheck) {
        LaterDatePaymentExpiredCheckResponse laterDatePaymentExpiredCheckResponse =
                        new LaterDatePaymentExpiredCheckResponse();
        laterDatePaymentExpiredCheckResponse.setExpiredFlag(
                        checkLaterDatePaymentExpiredUseCase.laterDatePaymentExpiredCheck(
                                        laterDatePaymentExpiredCheck.getTransactionId()));
        return new ResponseEntity<>(laterDatePaymentExpiredCheckResponse, HttpStatus.OK);
    }

    /**
     * GET /payments/link-pay/later-date-payment/payment-reminder : リンク決済(後日払い)の支払期限切れ間近を判定する
     * リンク決済(後日払い)の支払期限切れ間近を判定する
     *
     * @param laterDatePaymentReminderCheck 支払期限切れ間近判定リクエスト (required)
     * @return 支払期限切れ間近判定レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<LaterDatePaymentReminderCheckResponse> checkLaterDatePaymentReminder(@NotNull
                                                                                               @ApiParam(value = "支払期限切れ間近判定リクエスト",
                                                                                                         required = true)
                                                                                               @Valid LaterDatePaymentReminderCheckRequest laterDatePaymentReminderCheck) {
        LaterDatePaymentReminderCheckResponse laterDatePaymentReminderCheckResponse =
                        new LaterDatePaymentReminderCheckResponse();
        laterDatePaymentReminderCheckResponse.setDueDateFlag(
                        checkLaterDatePaymentReminderUseCase.laterDatePaymentReminderCheck(
                                        laterDatePaymentReminderCheck.getTransactionId()));
        return new ResponseEntity<>(laterDatePaymentReminderCheckResponse, HttpStatus.OK);
    }

    /**
     * POST /payments/link-pay/cancel-forget/reminder-batch : GMO決済キャンセル漏れ検知バッチ
     * GMO決済キャンセル漏れ検知バッチ
     *
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> cancelForgetReminderBatch() {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.linkpaycancelforget.routing");

        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            this.messagePublisherService.publish(routing, new BatchQueueMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            response.setExecuteMessage("バッチの手動起動を受け付けました。");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error(HTypeBatchName.BATCH_LINK_PAY_CANCEL_REMINDER.getLabel() + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /payments/link-pay/result/receive : リンク決済結果を受け取る
     * リンク決済から決済せずに「戻る」された場合は、レスポンスボディなしの400番エラーとなる
     *
     * @param receiveLinkPaymentRequest リンク決済結果を受け取るリクエスト (required)
     * @return リンク決済結果を受け取るレスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ReceiveLinkPaymentResponse> receive(@Valid ReceiveLinkPaymentRequest receiveLinkPaymentRequest) {

        if (receiveLinkPaymentRequest.getLinkPayJsonText() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ReceiveLinkPayUseCaseDto receiveLinkPayUseCaseDto = receiveLinkPaymentResultUseCase.receiveLinkPayResult(
                        receiveLinkPaymentRequest.getLinkPayJsonText());
        // 決済処理なしでHMへ戻る場合
        if (receiveLinkPayUseCaseDto.isNoProcessBack()) {
            ApplicationException appException = new ApplicationException();
            appException.addMessage("PAYMENT_LINK0001-I", new String[] {receiveLinkPayUseCaseDto.getTransactionId()});
            throw appException;
        }

        ReceiveLinkPaymentResponse receiveLinkPaymentResponse = new ReceiveLinkPaymentResponse();
        receiveLinkPaymentResponse.setTransactionId(receiveLinkPayUseCaseDto.getTransactionId());

        return new ResponseEntity<>(receiveLinkPaymentResponse, HttpStatus.OK);
    }
}