/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.transaction;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.usecase.transaction.AddAdjustmentAmountOfTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.AddOrderItemToTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.CancelTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.CheckTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.CheckTransactionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.ConfirmOpenOfTransactionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.ConfirmOpenOfTransactionUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.transaction.DeleteOrderItemFromTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.DisableCouponOfTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.DoReAuthOfTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.EnableCouponOfTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderProcessHistoryListUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.GetTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.ModernizeTransactionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.OpenTransactionReviseUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.OpenTransactionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.RegistInputContentToSuspendedTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.RegistInputContentToTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.RegistInputContentToTransactionForRevisionUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.transaction.RegistShipmentResultUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.RegistShipmentResultUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.transaction.StartTransactionReviseUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.StartTransactionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.UpdateCanceledTransactionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.query.OrderProcessHistoryQueryModel;
import jp.co.itechh.quad.ddd.usecase.transaction.service.ShipmentRegisterMessageDto;
import jp.co.itechh.quad.transaction.presentation.api.OrderApi;
import jp.co.itechh.quad.transaction.presentation.api.param.AddAdjustmentAmountOfTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.AddOrderItemToTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.ApplyOriginCommissionAndCarriageForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.CancelTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.CancelTransactionForRevisionResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.CanceledTransactionUpdateRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.CheckTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.CheckTransactionForRevisionResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.DeleteOrderItemFromTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.DisableCouponOfTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.EnableCouponOfTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.GetTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.MessageResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.OpenTransactionReviseRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistoryListGetRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistoryListResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.RegistInputContentToSuspendedTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.RegistInputContentToTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.StartTransactionReviseRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.StartTransactionReviseResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionCheckRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionConfirmOpenRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionModernizeRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionOpenRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionReAuthRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionReflectDepositedRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionRegistResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionShipmentRegistBatchRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionShipmentRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 取引エンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class TransactionController extends AbstractController implements OrderApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** 改訂用取引に調整金額を追加する ユースケース */
    private final AddAdjustmentAmountOfTransactionForRevisionUseCase addAdjustmentAmountOfTransactionForRevisionUseCase;

    /** 改訂用取引に注文商品を追加 ユースケース */
    private final AddOrderItemToTransactionForRevisionUseCase addOrderItemToTransactionForRevisionUseCase;

    /** 改訂用取引取消ユースケース */
    private final CancelTransactionForRevisionUseCase cancelTransactionForRevisionUseCase;

    /** 改訂取引全体チェックユースケース */
    private final CheckTransactionForRevisionUseCase checkTransactionForRevisionUseCase;

    /** 取引全体チェックユースケース */
    private final CheckTransactionUseCase checkTransactionUseCase;

    /** 取引全体最新化ユースケース */
    private final ModernizeTransactionUseCase modernizeTransactionUseCase;

    /** 取引確定可能確認ユースケース */
    private final ConfirmOpenOfTransactionUseCase confirmTransactionForOpenUseCase;

    /** 改訂用取引から注文商品を削除 ユースケース */
    private final DeleteOrderItemFromTransactionForRevisionUseCase deleteOrderItemFromTransactionForRevisionUseCase;

    /** 改訂用取引のクーポンを無効化する ユースケース */
    private final DisableCouponOfTransactionForRevisionUseCase disableCouponOfTransactionForRevisionUseCase;

    /** 改訂用取引のクーポンを有効化する ユースケース */
    private final EnableCouponOfTransactionForRevisionUseCase enableCouponOfTransactionForRevisionUseCase;

    /** 改訂用取引取得ユースケース */
    private final GetTransactionForRevisionUseCase getTransactionForRevisionUseCase;

    /** 取引改訂を確定するユースケース */
    private final OpenTransactionReviseUseCase openTransactionReviseUseCase;

    /** 取引確定ユースケース */
    private final OpenTransactionUseCase openTransactionUseCase;

    /** 入力内容を改訂用取引へ反映するユースケース */
    private final RegistInputContentToTransactionForRevisionUseCase registInputContentToTransactionForRevisionUseCase;

    /** 入力内容を改訂用一時停止取引へ反映するユースケース */
    private final RegistInputContentToSuspendedTransactionForRevisionUseCase
                    registInputContentToSuspendedTransactionForRevisionUseCase;

    /** 出荷実績登録ユースケース */
    private final RegistShipmentResultUseCase registShipmentResultUseCase;

    /** 取引改訂開始ユースケース */
    private final StartTransactionReviseUseCase startTransactionReviseUseCase;

    /** 取引開始ユースケース */
    private final StartTransactionUseCase startTransactionUseCase;

    /** 受注処理履歴一覧取得 ユースケース */
    private final GetOrderProcessHistoryListUseCase getOrderProcessHistoryListUseCase;

    /** 取消済み取引更新ユースケース */
    private final UpdateCanceledTransactionUseCase updateCanceledTransactionUseCase;

    /** 改訂用取引再オーソリ実行ユースケース */
    private final DoReAuthOfTransactionForRevisionUseCase doReAuthOfTransactionForRevisionUseCase;

    /** 改訂用取引の改訂前手数料/送料適用フラグ更新 ユースケース */
    private final UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase
                    updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** 取引Helperクラス */
    private final TransactionHelper transactionHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public TransactionController(AddAdjustmentAmountOfTransactionForRevisionUseCase addAdjustmentAmountOfTransactionForRevisionUseCase,
                                 AddOrderItemToTransactionForRevisionUseCase addOrderItemToTransactionForRevisionUseCase,
                                 CancelTransactionForRevisionUseCase cancelTransactionForRevisionUseCase,
                                 CheckTransactionForRevisionUseCase checkTransactionForRevisionUseCase,
                                 CheckTransactionUseCase checkTransactionUseCase,
                                 ModernizeTransactionUseCase modernizeTransactionUseCase,
                                 ConfirmOpenOfTransactionUseCase confirmTransactionForOpenUseCase,
                                 DeleteOrderItemFromTransactionForRevisionUseCase deleteOrderItemFromTransactionForRevisionUseCase,
                                 DisableCouponOfTransactionForRevisionUseCase disableCouponOfTransactionForRevisionUseCase,
                                 EnableCouponOfTransactionForRevisionUseCase enableCouponOfTransactionForRevisionUseCase,
                                 GetTransactionForRevisionUseCase getTransactionForRevisionUseCase,
                                 OpenTransactionReviseUseCase openTransactionReviseUseCase,
                                 OpenTransactionUseCase openTransactionUseCase,
                                 RegistInputContentToTransactionForRevisionUseCase registInputContentToTransactionForRevisionUseCase,
                                 RegistInputContentToSuspendedTransactionForRevisionUseCase registInputContentToSuspendedTransactionForRevisionUseCase,
                                 RegistShipmentResultUseCase registShipmentResultUseCase,
                                 StartTransactionReviseUseCase startTransactionReviseUseCase,
                                 StartTransactionUseCase startTransactionUseCase,
                                 GetOrderProcessHistoryListUseCase getOrderProcessHistoryListUseCase,
                                 UpdateCanceledTransactionUseCase updateCanceledTransactionUseCase,
                                 DoReAuthOfTransactionForRevisionUseCase doReAuthOfTransactionForRevisionUseCase,
                                 UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase,
                                 MessagePublisherService messagePublisherService,
                                 TransactionHelper transactionHelper,
                                 HeaderParamsUtility headerParamsUtil,
                                 ConversionUtility conversionUtility) {
        this.addAdjustmentAmountOfTransactionForRevisionUseCase = addAdjustmentAmountOfTransactionForRevisionUseCase;
        this.addOrderItemToTransactionForRevisionUseCase = addOrderItemToTransactionForRevisionUseCase;
        this.cancelTransactionForRevisionUseCase = cancelTransactionForRevisionUseCase;
        this.checkTransactionForRevisionUseCase = checkTransactionForRevisionUseCase;
        this.checkTransactionUseCase = checkTransactionUseCase;
        this.modernizeTransactionUseCase = modernizeTransactionUseCase;
        this.confirmTransactionForOpenUseCase = confirmTransactionForOpenUseCase;
        this.deleteOrderItemFromTransactionForRevisionUseCase = deleteOrderItemFromTransactionForRevisionUseCase;
        this.disableCouponOfTransactionForRevisionUseCase = disableCouponOfTransactionForRevisionUseCase;
        this.enableCouponOfTransactionForRevisionUseCase = enableCouponOfTransactionForRevisionUseCase;
        this.getTransactionForRevisionUseCase = getTransactionForRevisionUseCase;
        this.openTransactionReviseUseCase = openTransactionReviseUseCase;
        this.openTransactionUseCase = openTransactionUseCase;
        this.registInputContentToTransactionForRevisionUseCase = registInputContentToTransactionForRevisionUseCase;
        this.registInputContentToSuspendedTransactionForRevisionUseCase =
                        registInputContentToSuspendedTransactionForRevisionUseCase;
        this.registShipmentResultUseCase = registShipmentResultUseCase;
        this.startTransactionReviseUseCase = startTransactionReviseUseCase;
        this.startTransactionUseCase = startTransactionUseCase;
        this.getOrderProcessHistoryListUseCase = getOrderProcessHistoryListUseCase;
        this.updateCanceledTransactionUseCase = updateCanceledTransactionUseCase;
        this.doReAuthOfTransactionForRevisionUseCase = doReAuthOfTransactionForRevisionUseCase;
        this.updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase =
                        updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase;
        this.messagePublisherService = messagePublisherService;
        this.transactionHelper = transactionHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
    }

    /**
     * POST /order/transactions : 取引開始
     *
     * @return 取引開始レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<TransactionRegistResponse> regist(@RequestHeader(value = "customerBirthday", required = true)
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                            Date customerBirthday,
                                                            @RequestHeader(value = "addressId", required = true)
                                                                            String addressId) {

        String transactionId =
                        this.startTransactionUseCase.startTransaction(getCustomerId(), customerBirthday, addressId);

        return new ResponseEntity<>(this.transactionHelper.toTransactionRegistResponse(transactionId), HttpStatus.OK);
    }

    /**
     * POST /order/transactions/modernize : 取引全体最新化
     *
     * @param transactionModernizeRequest 取引全体最新化リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> modernize(@ApiParam(value = "取引全体最新化リクエスト", required = true) @Valid @RequestBody
                                                          TransactionModernizeRequest transactionModernizeRequest) {

        modernizeTransactionUseCase.modernizeTransaction(transactionModernizeRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/check : 取引全体チェック
     *
     * @param transactionCheckRequest 取引全体チェックリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "取引全体チェックリクエスト", required = true) @Valid @RequestBody
                                                      TransactionCheckRequest transactionCheckRequest,
                                      @RequestHeader(value = "customerBirthday", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date customerBirthday) {

        checkTransactionUseCase.checkTransaction(transactionCheckRequest.getTransactionId(), customerBirthday,
                                                 setDefaultBoolean(transactionCheckRequest.getContractConfirmFlag())
                                                );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/confirm/open : 取引確定可能か確認
     * 取引確定可能か確認（注文確定前に在庫確保や決済等の事前処理を行う）&lt;br&gt; 3Dセキュア認証が必要な場合202レスポンスとなる
     *
     * @param transactionConfirmOpenRequest 取引確定可能確認リクエスト (required)
     * @return 3Dセキュアレスポンス (status code 202)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity confirmOpen(@ApiParam(value = "取引確定可能確認リクエスト", required = true) @Valid @RequestBody
                                                      TransactionConfirmOpenRequest transactionConfirmOpenRequest) {

        ConfirmOpenOfTransactionUseCaseDto dto = confirmTransactionForOpenUseCase.confirmOpenOfTransaction(
                        transactionConfirmOpenRequest.getTransactionId(),
                        transactionConfirmOpenRequest.getSecurityCode(),
                        transactionConfirmOpenRequest.getCallBackType(),
                        transactionConfirmOpenRequest.getCreditTdResultReceiveUrl(),
                        transactionConfirmOpenRequest.getPaymentLinkReturnUrl()
                                                                                                          );

        if (dto != null) {
            if (dto.getStatusCode() == HttpStatus.ACCEPTED.value()) {
                return new ResponseEntity<>(
                                transactionHelper.toTransactionConfirmOpenResponse(dto), HttpStatus.ACCEPTED);
            } else if (dto.getStatusCode() == HttpStatus.CREATED.value()) {
                return new ResponseEntity<>(transactionHelper.toPaymentLinkUrlResponse(dto), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * POST /order/transactions/open : 取引確定
     * 取引確定（注文確定）
     *
     * @param transactionOpenRequest 取引確定リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> open(@ApiParam(value = "取引確定リクエスト", required = true) @Valid @RequestBody
                                                     TransactionOpenRequest transactionOpenRequest) {

        OrderReceivedEntity orderReceivedEntity =
                        openTransactionUseCase.openTransaction(transactionOpenRequest.getTransactionId());
        openTransactionUseCase.asyncAfterProcess(orderReceivedEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/shipments : 出荷実績登録
     * 出荷実績登録
     *
     * @param transactionShipmentRequest 出荷実績登録リクエスト (required)
     * @return メッセージマップ (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<MessageResponse> shipment(
                    @ApiParam(value = "出荷実績登録リクエスト", required = true) @Valid @RequestBody
                                    TransactionShipmentRequest transactionShipmentRequest) {

        List<RegistShipmentResultUseCaseParam> registShipmentResultUseCaseParamList =
                        transactionHelper.toRegistShipmentUseCaseParam(
                                        transactionShipmentRequest.getTransactionShipmentInfoList());

        List<ShipmentRegisterMessageDto> messageDtoList =
                        registShipmentResultUseCase.registShipmentResult(registShipmentResultUseCaseParamList,
                                                                         getAdminSeq()
                                                                        );

        return new ResponseEntity<>(this.transactionHelper.toMessageResponse(messageDtoList), HttpStatus.OK);
    }

    /**
     * POST /order/transactions/shipments-batch : 出荷実績登録バッチ
     * 出荷実績登録
     *
     * @param transactionShipmentRegistBatchRequest 出荷実績登録バッチリクエスト (required)
     * @return バッチ起動結果レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> shipmentBatch(
                    @ApiParam(value = "出荷実績登録バッチリクエスト", required = true) @Valid @RequestBody
                                    TransactionShipmentRegistBatchRequest transactionShipmentRegistBatchRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.shipmentregist.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setStartType(transactionShipmentRegistBatchRequest.getStartType());
        message.setFilePath(transactionShipmentRegistBatchRequest.getFilePath());
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

            LOGGER.error(HTypeBatchName.BATCH_SHIPMENT_REGIST.getLabel() + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /order/transactions/revise : 取引改訂の開始
     * 取引改訂（受注修正）の開始 ※受注修正画面の初期表示で呼び出す
     *
     * @param startTransactionReviseRequest 取引改訂開始リクエスト (required)
     * @return 取引開始レスポンス (status code 200)
     * or システムエラー (status code 200)
     */

    @Override
    public ResponseEntity<StartTransactionReviseResponse> startTransactionRevise(
                    @ApiParam(value = "取引改訂開始リクエスト", required = true) @Valid @RequestBody
                                    StartTransactionReviseRequest startTransactionReviseRequest) {

        StartTransactionReviseResponse startTransactionReviseResponse = new StartTransactionReviseResponse();

        String transactionRevisionId = startTransactionReviseUseCase.startTransactionRevise(
                        startTransactionReviseRequest.getTransactionId());
        startTransactionReviseResponse.setTransactionRevisionId(transactionRevisionId);

        return new ResponseEntity<>(startTransactionReviseResponse, HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/check : 改訂取引全体チェック
     * 改訂取引全体チェック
     *
     * @param checkTransactionForRevisionRequest 改訂取引全体チェックリクエスト (required)
     * @return 改訂取引全体チェックレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CheckTransactionForRevisionResponse> checkTransactionForRevision(
                    @ApiParam(value = "改訂取引全体チェックリクエスト", required = true) @Valid @RequestBody
                                    CheckTransactionForRevisionRequest checkTransactionForRevisionRequest) {

        CheckTransactionForRevisionResponse checkTransactionForRevisionResponse =
                        new CheckTransactionForRevisionResponse();
        checkTransactionForRevisionResponse.setWarningMessage(checkTransactionForRevisionUseCase.checkTransaction(
                        checkTransactionForRevisionRequest.getTransactionRevisionId(),
                        setDefaultBoolean(checkTransactionForRevisionRequest.getContractConfirmFlag())
                                                                                                                 ));

        return new ResponseEntity<>(checkTransactionForRevisionResponse, HttpStatus.OK);
    }

    /**
     * GET /order/transactions/for-revision : 改訂用取引取得
     * 改訂用取引取得
     *
     * @param getTransactionForRevisionRequest 改訂用取引取得リクエスト (required)
     * @return 改訂用取引レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<TransactionForRevisionResponse> getTransactionForRevision(
                    @NotNull @ApiParam(value = "改訂用取引取得リクエスト", required = true) @Valid
                                    GetTransactionForRevisionRequest getTransactionForRevisionRequest) {

        TransactionForRevisionEntity transactionForRevisionEntity =
                        getTransactionForRevisionUseCase.getTransactionForRevision(
                                        getTransactionForRevisionRequest.getTransactionRevisionId());

        TransactionForRevisionResponse transactionForRevisionResponse =
                        transactionHelper.toGetTransactionForRevisionResponse(transactionForRevisionEntity);

        return new ResponseEntity<>(transactionForRevisionResponse, HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/cancel : 改訂用取引取消
     * 改訂用取引取消（受注キャンセル）する。このAPI呼出し後に、「取引改訂を確定する」呼出しが必要
     *
     * @param cancelTransactionForRevisionRequest 改訂用取引取消リクエスト (required)
     * @return 改訂用取引取消レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CancelTransactionForRevisionResponse> cancelTransactionForRevision(
                    @ApiParam(value = "改訂用取引取消リクエスト", required = true) @Valid @RequestBody
                                    CancelTransactionForRevisionRequest cancelTransactionForRevisionRequest) {
        CancelTransactionForRevisionResponse cancelTransactionForRevisionResponse =
                        new CancelTransactionForRevisionResponse();
        Map<String, List<WarningContent>> messageMapForResponse = new HashMap<>();

        OrderReceivedEntity orderReceivedEntity = cancelTransactionForRevisionUseCase.cancelTransactionForRevision(
                        cancelTransactionForRevisionRequest.getTransactionId(),
                        cancelTransactionForRevisionRequest.getAdminMemo(),
                        cancelTransactionForRevisionRequest.getCouponDisableFlag(), messageMapForResponse
                                                                                                                  );
        cancelTransactionForRevisionUseCase.asyncAfterProcess(orderReceivedEntity);

        cancelTransactionForRevisionResponse.setWarningMessage(messageMapForResponse);

        return new ResponseEntity<>(cancelTransactionForRevisionResponse, HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/input-content : 入力内容を改訂用取引へ反映する
     * 入力内容を改訂用取引へ反映する
     *
     * @param registInputContentToTransactionForRevisionRequest 入力内容を改訂用取引へ反映するリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> registInputContentToTransactionForRevision(
                    @ApiParam(value = "入力内容を改訂用取引へ反映するリクエスト", required = true) @Valid @RequestBody
                                    RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest) {

        RegistInputContentToTransactionForRevisionUseCaseParam useCaseParam =
                        transactionHelper.toRegistInputContentToTransactionForRevisionUseCaseParam(
                                        registInputContentToTransactionForRevisionRequest);

        registInputContentToTransactionForRevisionUseCase.registInputContentToTransactionForRevision(useCaseParam);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/input-content/for-suspended : 入力内容を改訂用一時停止取引へ反映する
     * 入力内容を改訂用一時停止取引へ反映する
     *
     * @param registInputContentToSuspendedTransactionForRevisionRequest 入力内容を改訂用一時停止取引へ反映するリクエスト (required)
     * @return 成功 (status code 200)  or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> registInputContentToSuspendedTransactionForRevision(
                    @ApiParam(value = "入力内容を改訂用一時停止取引へ反映するリクエスト", required = true) @Valid @RequestBody
                                    RegistInputContentToSuspendedTransactionForRevisionRequest registInputContentToSuspendedTransactionForRevisionRequest) {

        registInputContentToSuspendedTransactionForRevisionUseCase.registInputContentToSuspendedTransactionForRevision(
                        registInputContentToSuspendedTransactionForRevisionRequest.getTransactionRevisionId(),
                        registInputContentToSuspendedTransactionForRevisionRequest.getAdminMemo(), setDefaultBoolean(
                                        registInputContentToSuspendedTransactionForRevisionRequest.getPaymentAgencyReleaseFlag())
                                                                                                                      );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/order-item/add : 改訂用取引に注文商品を追加
     * 改訂用取引に注文商品を追加
     *
     * @param addOrderItemToTransactionForRevisionRequest 改訂用取引に注文商品を追加 リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> addOrderItemToTransactionForRevision(
                    @ApiParam(value = "改訂用取引に注文商品を追加 リクエスト", required = true) @Valid @RequestBody
                                    AddOrderItemToTransactionForRevisionRequest addOrderItemToTransactionForRevisionRequest) {

        addOrderItemToTransactionForRevisionUseCase.addOrderItemToTransactionForRevision(
                        addOrderItemToTransactionForRevisionRequest.getTransactionRevisionId(),
                        addOrderItemToTransactionForRevisionRequest.getItemIdList()
                                                                                        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/order-item/delete : 改訂用取引から注文商品を削除
     * 改訂用取引から注文商品を削除
     *
     * @param deleteOrderItemFromTransactionForRevisionRequest 改訂用取引から注文商品を削除 (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> deleteOrderItemFromTransactionForRevision(
                    @ApiParam(value = "改訂用取引から注文商品を削除", required = true) @Valid @RequestBody
                                    DeleteOrderItemFromTransactionForRevisionRequest deleteOrderItemFromTransactionForRevisionRequest) {

        deleteOrderItemFromTransactionForRevisionUseCase.deleteOrderItemToTransactionForRevision(
                        deleteOrderItemFromTransactionForRevisionRequest.getTransactionRevisionId(),
                        deleteOrderItemFromTransactionForRevisionRequest.getItemSeqList()
                                                                                                );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/coupon/disable : 改訂用取引のクーポンを無効化する
     * 改訂用取引のクーポンを無効化する
     *
     * @param disableCouponOfTransactionForRevisionRequest 改訂用取引のクーポンを無効化する (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */

    @Override
    public ResponseEntity<Void> disableCouponOfTransactionForRevision(
                    @ApiParam(value = "改訂用取引のクーポンを無効化する", required = true) @Valid @RequestBody
                                    DisableCouponOfTransactionForRevisionRequest disableCouponOfTransactionForRevisionRequest) {

        disableCouponOfTransactionForRevisionUseCase.disableCouponOfTransactionForRevision(
                        disableCouponOfTransactionForRevisionRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/coupon/enable : 改訂用販売伝票のクーポン利用フラグを有効化
     * 改訂用販売伝票のクーポン利用フラグを有効化
     *
     * @param enableCouponOfTransactionForRevisionRequest 改訂用販売伝票のクーポン利用フラグを有効化 (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> enableCouponOfTransactionForRevision(
                    @ApiParam(value = "改訂用販売伝票のクーポン利用フラグを有効化", required = true) @Valid @RequestBody
                                    EnableCouponOfTransactionForRevisionRequest enableCouponOfTransactionForRevisionRequest) {

        enableCouponOfTransactionForRevisionUseCase.enableCouponOfTransactionForRevision(
                        enableCouponOfTransactionForRevisionRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/adjustment-amount/add : 改訂用取引に調整金額を追加する
     * 改訂用取引に調整金額を追加する
     *
     * @param addAdjustmentAmountOfTransactionForRevisionRequest 改訂用取引に調整金額を追加する リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> addAdjustmentAmountOfTransactionForRevision(
                    @ApiParam(value = "改訂用取引に調整金額を追加する リクエスト", required = true) @Valid @RequestBody
                                    AddAdjustmentAmountOfTransactionForRevisionRequest addAdjustmentAmountOfTransactionForRevisionRequest) {

        addAdjustmentAmountOfTransactionForRevisionUseCase.addAdjustmentAmountOfTransactionForRevision(
                        addAdjustmentAmountOfTransactionForRevisionRequest.getTransactionRevisionId(),
                        addAdjustmentAmountOfTransactionForRevisionRequest.getAdjustName(),
                        addAdjustmentAmountOfTransactionForRevisionRequest.getAdjustPrice()
                                                                                                      );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/for-revision/commission-and-carriage/apply-origin : 改訂用取引の改訂前手数料/送料の適用フラグ設定
     *
     * @param applyOriginCommissionAndCarriageForRevisionRequest 改訂前手数料/送料の適用フラグ設定 (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> applyOriginCommissionAndCarriageForRevision(
                    @ApiParam(value = "改訂前手数料/送料の適用フラグ設定", required = true) @Valid @RequestBody
                                    ApplyOriginCommissionAndCarriageForRevisionRequest applyOriginCommissionAndCarriageForRevisionRequest) {

        boolean originCommissionApplyFlag = Boolean.TRUE.equals(
                        applyOriginCommissionAndCarriageForRevisionRequest.getOriginCommissionApplyFlag());
        boolean originCarriageApplyFlag = Boolean.TRUE.equals(
                        applyOriginCommissionAndCarriageForRevisionRequest.getOriginCarriageApplyFlag());

        updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase.updateCouponUseFlagOfSalesSlipForRevision(
                        applyOriginCommissionAndCarriageForRevisionRequest.getTransactionRevisionId(),
                        originCommissionApplyFlag, originCarriageApplyFlag
                                                                                                              );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/revise/open : 取引改訂を確定する
     * 取引改訂を確定する
     *
     * @param openTransactionReviseRequest 取引改訂を確定するリクエスト (required)
     * @return 成功 (status code 200)
     */
    @Override
    public ResponseEntity<Void> openTransactionRevise(
                    @ApiParam(value = "取引改訂を確定するリクエスト", required = true) @Valid @RequestBody
                                    OpenTransactionReviseRequest openTransactionReviseRequest) {

        HTypeProcessType processType = EnumTypeUtil.getEnumFromValue(HTypeProcessType.class,
                                                                     openTransactionReviseRequest.getProcessType()
                                                                    );

        OrderReceivedEntity orderReceivedEntity = openTransactionReviseUseCase.openTransactionRevise(
                        openTransactionReviseRequest.getTransactionRevisionId(), getAdminSeq(), processType,
                        openTransactionReviseRequest.getInventorySettlementSkipFlag()
                                                                                                    );
        openTransactionReviseUseCase.asyncAfterProcess(orderReceivedEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /order/transactions/process-histories : 受注処理履歴一覧取得
     * 受注管理用に受注処理履歴一覧を取得する
     *
     * @param processHistoryListGetRequest 受注処理履歴一覧取得リクエスト (required)
     * @return 受注処理履歴一覧レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ProcessHistoryListResponse> getProcessHistoryList(
                    @NotNull @ApiParam(value = "受注処理履歴一覧取得リクエスト", required = true) @Valid
                                    ProcessHistoryListGetRequest processHistoryListGetRequest) {

        List<OrderProcessHistoryQueryModel> orderProcessHistoryQueryModelList =
                        getOrderProcessHistoryListUseCase.getOrderProcessHistoryList(
                                        processHistoryListGetRequest.getOrderCode());
        ProcessHistoryListResponse processHistoryListResponse =
                        transactionHelper.toProcessHistoryListResponse(orderProcessHistoryQueryModelList);

        return new ResponseEntity<>(processHistoryListResponse, HttpStatus.OK);
    }

    /**
     * POST /order/transactions/canceled-transaction : 取消済み取引更新
     * 取消済み取引を更新する
     *
     * @param canceledTransactionUpdateRequest 取消済み取引更新リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> updateCanceledTransaction(
                    @ApiParam(value = "取消済み取引更新リクエスト", required = true) @Valid @RequestBody
                                    CanceledTransactionUpdateRequest canceledTransactionUpdateRequest) {

        OrderReceivedEntity orderReceivedEntity = updateCanceledTransactionUseCase.updateCanceledTransaction(
                        canceledTransactionUpdateRequest.getTransactionId(), getAdminSeq(),
                        canceledTransactionUpdateRequest.getAdminMemo()
                                                                                                            );
        updateCanceledTransactionUseCase.asyncAfterProcess(orderReceivedEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/re-auth : 再オーソリ実行
     * 再オーソリを実行する
     *
     * @param transactionReAuthRequest 再オーソリ実行リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> doReAuth(@ApiParam(value = "再オーソリ実行リクエスト", required = true) @Valid @RequestBody
                                                         TransactionReAuthRequest transactionReAuthRequest) {

        OrderReceivedEntity orderReceivedEntity =
                        doReAuthOfTransactionForRevisionUseCase.doReAuth(transactionReAuthRequest.getTransactionId(),
                                                                         getAdminSeq()
                                                                        );
        doReAuthOfTransactionForRevisionUseCase.asyncAfterProcess(orderReceivedEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /order/transactions/mulPayNotification/recovery-batch : 決済代行入金結果受取予備処理バッチ
     * 未入金受注の入金状態を決済代行へ確認し、入金済みなら受注データへ反映する
     *
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> mulPayNotificationRecoveryBatch() {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.mulpaynotificationrecovery.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setStartType(HTypeBatchStartType.MANUAL.getValue());
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

            LOGGER.error(HTypeBatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY.getLabel() + messageLogParam
                         + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /order/transactions/payment-reminder-batch : 支払督促バッチ
     * 支払督促バッチ
     *
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> paymentReminderBatch() {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.reminderpayment.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setStartType(HTypeBatchStartType.MANUAL.getValue());
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
            LOGGER.error(HTypeBatchName.BATCH_REMINDER_PAYMENT.getLabel() + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /order/transactions/payment-expired-batch : 支払期限切れバッチ
     * 支払期限切れバッチ
     *
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> paymentExpiredBatch() {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.expiredpayment.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setStartType(HTypeBatchStartType.MANUAL.getValue());

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
            LOGGER.error(HTypeBatchName.BATCH_EXPIRED_PAYMENT.getLabel() + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT /order/transactions/reflect-deposited : HM入金結果データを確認して受注を入金済みにする
     * HM入金結果データを確認して受注を入金済みにする
     *
     * @param transactionReflectDepositedRequest 入金反映 リクエスト (required)
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> reflectDeposited(
                    @ApiParam(value = "入金反映 リクエスト", required = true) @Valid @RequestBody
                                    TransactionReflectDepositedRequest transactionReflectDepositedRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.reflectdeposited.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setOrderCode(transactionReflectDepositedRequest.getOrderCode());

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messagePublisherService.publish(routing, message);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("HM入金結果データを確認して受注を入金済みにする" + messageLogParam + e.getMessage());

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

    /**
     * Boolean(null)にboolean(false)をセット<br/>
     * Booleanで宣言されている項目がNullであり、UCメソッドでbooleanの場合に変換する
     *
     * @param target API引数に指定されたBoolean項目
     * @return targetがnullの場合false / 指定されている場合booleanに変換
     */
    private boolean setDefaultBoolean(Boolean target) {

        if (target == null) {
            return false;
        }

        return target;
    }

}