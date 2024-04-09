package jp.co.itechh.quad.clear.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.clear.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.clear.presentation.api.param.ClearBatchRequest;
import jp.co.itechh.quad.clear.presentation.api.param.UnnecessaryBillingSlipCancellationRequest;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.usecase.billing.DeleteBillingSlipUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * クリアバッチ Controller
 *
 * @author Doan Thang (VJP)
 */
@RestController
public class ClearController implements PaymentApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClearController.class);

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** 請求伝票削除ユースケース */
    private final DeleteBillingSlipUseCase deleteBillingSlipUseCase;

    /**
     * コンストラクタ
     *
     * @param messagePublisherService  キューパブリッシャーサービス
     * @param headerParamsUtil         ヘッダパラメーターユーティル
     * @param deleteBillingSlipUseCase 請求伝票削除ユースケース
     */
    @Autowired
    public ClearController(MessagePublisherService messagePublisherService,
                           HeaderParamsUtility headerParamsUtil,
                           DeleteBillingSlipUseCase deleteBillingSlipUseCase) {
        this.messagePublisherService = messagePublisherService;
        this.headerParamsUtil = headerParamsUtil;
        this.deleteBillingSlipUseCase = deleteBillingSlipUseCase;
    }

    /**
     * POST /payment/clear : クリアバッチ
     * クリアバッチ
     *
     * @param clearBatchRequest クリアバッチリクエスト (required)
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> execute(
                    @ApiParam(value = "クリアバッチリクエスト", required = true) @Valid @RequestBody
                                    ClearBatchRequest clearBatchRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.clear.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminId());
        message.setStartType(clearBatchRequest.getStartType());

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
            LOGGER.error("クリアバッチ" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /payment/cancellation : 不要請求伝票取消
     * 不要請求伝票取消
     *
     * @param unnecessaryBillingSlipCancellationRequest 不要請求伝票取消リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(@NotNull @Valid
                                                       UnnecessaryBillingSlipCancellationRequest unnecessaryBillingSlipCancellationRequest) {

        // 取引ID（APIパラメータ）に紐づく以下のテーブルを削除
        deleteBillingSlipUseCase.deleteUnnecessaryByTransactionId(
                        unnecessaryBillingSlipCancellationRequest.getTransactionId(),
                        unnecessaryBillingSlipCancellationRequest.getOrderCode()
                                                                 );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 管理者SEQを取得する
     *
     * @return customerId 顧客ID
     */
    private Integer getAdminId() {
        if (this.headerParamsUtil.getAdministratorSeq() == null) {
            return null;
        } else {
            return Integer.valueOf(this.headerParamsUtil.getAdministratorSeq());
        }
    }
}