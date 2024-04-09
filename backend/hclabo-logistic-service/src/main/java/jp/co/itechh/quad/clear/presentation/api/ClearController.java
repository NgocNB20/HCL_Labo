package jp.co.itechh.quad.clear.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.clear.presentation.api.param.BatchExecuteRequest;
import jp.co.itechh.quad.clear.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.clear.presentation.api.param.UnnecessaryShippingSlipCancellationRequest;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.usecase.shipping.DeleteShippingUseCase;
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
import java.util.List;

/**
 * クリアバッチ Controller
 *
 * @author Doan Thang (VJP)
 */
@RestController
public class ClearController implements LogisticApi {

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

    /** 配送伝票削除ユースケース */
    private final DeleteShippingUseCase deleteShippingUseCase;

    /**
     * コンストラクタ
     *
     * @param messagePublisherService キューパブリッシャーサービス
     * @param headerParamsUtil        ヘッダパラメーターユーティル
     * @param deleteShippingUseCase   配送伝票削除ユースケース
     */
    @Autowired
    public ClearController(MessagePublisherService messagePublisherService,
                           HeaderParamsUtility headerParamsUtil,
                           DeleteShippingUseCase deleteShippingUseCase) {
        this.messagePublisherService = messagePublisherService;
        this.headerParamsUtil = headerParamsUtil;
        this.deleteShippingUseCase = deleteShippingUseCase;
    }

    /**
     * POST /logistic/clear : クリアバッチ
     * クリアバッチ
     *
     * @param batchExecuteRequest クリアバッチリクエスト (required)
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> execute(
                    @ApiParam(value = "クリアバッチリクエスト", required = true) @Valid @RequestBody
                                    BatchExecuteRequest batchExecuteRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.clear.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminId());
        message.setStartType(batchExecuteRequest.getStartType());

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
     * DELETE /logistic/cancellation : 不要配送伝票取消
     * 不要配送伝票取消
     *
     * @param unnecessaryShippingSlipCancellationRequest 不要配送伝票取消リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(@NotNull @Valid
                                                       UnnecessaryShippingSlipCancellationRequest unnecessaryShippingSlipCancellationRequest) {

        // 不要データ削除処理
        List<Integer> stockChangedGoodsSeqList = deleteShippingUseCase.deleteUnnecessaryByTransactionId(
                        unnecessaryShippingSlipCancellationRequest.getTransactionId(),
                        Boolean.TRUE.equals(unnecessaryShippingSlipCancellationRequest.getStockReleaseFlag())
                                                                                                       );
        deleteShippingUseCase.asyncAfterProcess(stockChangedGoodsSeqList);

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