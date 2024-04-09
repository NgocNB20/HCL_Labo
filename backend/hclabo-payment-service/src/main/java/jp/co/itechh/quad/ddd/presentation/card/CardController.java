/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.card;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.card.presentation.api.PaymentsApi;
import jp.co.itechh.quad.card.presentation.api.param.AuthExpirationApproachingNotificationRequest;
import jp.co.itechh.quad.card.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.card.presentation.api.param.CardInfoResponse;
import jp.co.itechh.quad.card.presentation.api.param.CreditLineReleaseRequest;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.usecase.card.DeleteCardInfoUseCase;
import jp.co.itechh.quad.ddd.usecase.card.GetCardInfoUseCase;
import jp.co.itechh.quad.ddd.usecase.card.GetCardInfoUseCaseDto;
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
 * クレジットカードエンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class CardController extends AbstractController implements PaymentsApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** クレジットカード情報取得ユースケース */
    private final GetCardInfoUseCase getCardInfoUseCase;

    /** クレジットカード情報削除ユースケース */
    private final DeleteCardInfoUseCase deleteCardInfoUseCase;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** ヘルパー */
    private final CardHelper cardHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param getCardInfoUseCase
     * @param deleteCardInfoUseCase
     * @param messagePublisherService
     * @param cardHelper
     * @param headerParamsUtil
     */
    @Autowired
    public CardController(GetCardInfoUseCase getCardInfoUseCase,
                          DeleteCardInfoUseCase deleteCardInfoUseCase,
                          MessagePublisherService messagePublisherService,
                          CardHelper cardHelper,
                          HeaderParamsUtility headerParamsUtil,
                          ConversionUtility conversionUtility) {
        this.getCardInfoUseCase = getCardInfoUseCase;
        this.deleteCardInfoUseCase = deleteCardInfoUseCase;
        this.messagePublisherService = messagePublisherService;
        this.cardHelper = cardHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
    }

    /**
     * GET /payments/cards : カード情報参照
     *
     * @return クレジットカード情報レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CardInfoResponse> get() {

        GetCardInfoUseCaseDto dto = getCardInfoUseCase.getCardInfo(getCustomerId());

        return new ResponseEntity<>(cardHelper.toCardInfoResponse(dto), HttpStatus.OK);
    }

    /**
     * DELETE /payments/cards : カード情報削除
     *
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete() {

        this.deleteCardInfoUseCase.deleteCardInfo(getCustomerId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /payments/cards/auth/notifications : オーソリ期限間近注文警告通知
     *
     * @param authExpirationApproachingNotificationRequest オーソリ期限間近通知リクエスト (required)
     * @return バッチ起動結果レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> notice(
                    @ApiParam(value = "オーソリ期限間近通知リクエスト", required = true) @Valid @RequestBody
                                    AuthExpirationApproachingNotificationRequest authExpirationApproachingNotificationRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.authnotification.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setStartType(authExpirationApproachingNotificationRequest.getStartType());
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;
        try {
            // キューにパブリッシュー
            this.messagePublisherService.publish(routing, message);

            // レスポンス作成

            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            response.setExecuteMessage("バッチの手動起動を受け付けました。");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {

            LOGGER.error("オーソリ期限間近注文警告通知" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /payments/cards/creditline/release : 与信枠解放
     *
     * @param creditLineReleaseRequest 与信枠解放リクエスト (required)
     * @return バッチ起動結果レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> release(
                    @ApiParam(value = "与信枠解放リクエスト", required = true) @Valid @RequestBody
                                    CreditLineReleaseRequest creditLineReleaseRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.creditlinerelease.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setStartType(creditLineReleaseRequest.getStartType());

        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;
        try {
            // キューにパブリッシュー
            this.messagePublisherService.publish(routing, message);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            response.setExecuteMessage("バッチの手動起動を受け付けました。");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {

            LOGGER.error("与信枠解放" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 顧客IDを取得する
     *
     * @return customerId 顧客ID
     */
    private String getCustomerId() {
        // PR層でチェックはしない。memberSeqがNullの場合、customerIdにNullが設定される
        return this.headerParamsUtil.getMemberSeq();
    }

    /**
     * 管理者SEQを取得する
     *
     * @return AdminSeq 管理者SEQ
     */
    private Integer getAdminSeq() {
        return this.conversionUtility.toInteger(this.headerParamsUtil.getAdministratorSeq());
    }

}