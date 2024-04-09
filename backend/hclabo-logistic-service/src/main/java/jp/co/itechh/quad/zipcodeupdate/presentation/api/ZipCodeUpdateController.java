package jp.co.itechh.quad.zipcodeupdate.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.zipcodeupdate.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.zipcodeupdate.presentation.api.param.ZipCodeUpdateExecuteRequest;
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
 * 郵便番号自動更新 Controller
 *
 * @author Doan Thang (VJP)
 */
@RestController
public class ZipCodeUpdateController implements ShippingsApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ZipCodeUpdateController.class);

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /**
     * コンストラクタ
     * @param messagePublisherService キューパブリッシャーサービス
     * @param headerParamsUtil ヘッダパラメーターユーティル
     */
    @Autowired
    public ZipCodeUpdateController(MessagePublisherService messagePublisherService, HeaderParamsUtility headerParamsUtil) {
        this.messagePublisherService = messagePublisherService;
        this.headerParamsUtil = headerParamsUtil;
    }

    /**
     * POST /shippings/zipcode-update : 郵便番号自動更新
     * 郵便番号自動更新
     *
     * @param zipCodeUpdateExecuteRequest 郵便番号自動更新リクエスト (required)
     * @return 結果レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> execute(
                    @ApiParam(value = "郵便番号自動更新リクエスト", required = true) @Valid @RequestBody
                                    ZipCodeUpdateExecuteRequest zipCodeUpdateExecuteRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.zipcodeupdate.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminId());
        message.setStartType(zipCodeUpdateExecuteRequest.getStartType());
        message.setAllFlag(zipCodeUpdateExecuteRequest.getAllFlag());

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
            LOGGER.error("郵便番号自動更新" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

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