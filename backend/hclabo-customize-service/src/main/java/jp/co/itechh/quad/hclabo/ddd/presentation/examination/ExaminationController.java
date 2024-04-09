package jp.co.itechh.quad.hclabo.ddd.presentation.examination;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.examination.presentation.api.HclaboCustomizeApi;
import jp.co.itechh.quad.examination.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ConfigInfoResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitReceivedEntryBatchRequest;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitRequest;
import jp.co.itechh.quad.examination.presentation.api.param.ExamResultsEntryBatchRequest;
import jp.co.itechh.quad.examination.presentation.api.param.RegistExamKitRequest;
import jp.co.itechh.quad.hclabo.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.hclabo.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.hclabo.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.hclabo.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.hclabo.core.web.AbstractController;
import jp.co.itechh.quad.hclabo.core.web.HeaderParamsUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.ExamKitRegistrationUseCase;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.GetExamKitByOrderItemListUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 検査 Controller
 */
@RestController
public class ExaminationController extends AbstractController implements HclaboCustomizeApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExaminationController.class);

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtil headerParamsUtil;

    /** 検査キット登録ユースケース */
    private final ExamKitRegistrationUseCase examKitRegistrationUseCase;

    /** 注文商品IDリストをもとに検査キットを取得するユースケース */
    private final GetExamKitByOrderItemListUseCase getExamKitByOrderItemListUseCase;

    /** 検査Helperクラス */
    private final ExaminationHelper examinationHelper;

    /** キューパブリッシャーサービス */
    private MessagePublisherService messagePublisherService;

    /**
     * コンストラクタ
     */
    @Autowired
    public ExaminationController(ExamKitRegistrationUseCase examKitRegistrationUseCase,
                                 GetExamKitByOrderItemListUseCase getExamKitByOrderItemListUseCase,
                                 ExaminationHelper examinationHelper,
                                 HeaderParamsUtil headerParamsUtil,
                                 MessagePublisherService messagePublisherService) {
        this.examKitRegistrationUseCase = examKitRegistrationUseCase;
        this.getExamKitByOrderItemListUseCase = getExamKitByOrderItemListUseCase;
        this.examinationHelper = examinationHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.messagePublisherService = messagePublisherService;
    }

    /**
     * POST /hclabo-customize/examination/examkits : 検査キット登録 <br/>
     * <span class="ldMUmp">カスタマイズ</span>
     * - 検査キットを新規登録する <br/>
     * <h5 class="eONCmm">業務詳細</h5>
     * - モデルの各項目を以下の通り更新する <br/>
     * - 検査キット番号：新規採番値 <br/>
     * - 注文商品ID：渡された注文商品ID <br/>
     * - 受注番号：渡された受注番号 <br/>
     * - 検査キット番号の採番方法は、渡された受注番号の後にランダムな6桁の値を追加する <br/>
     * <h5 class="eONCmm">制約/ルール</h5>
     * - 新規採番する検査キット番号はユニークであること <br/>
     * - 以下の引数は必ず値が設定されていること <br/>
     * - 受注番号、注文商品ID
     *
     * @param registExamKitRequest 検査キット登録リクエスト (required)
     * @return 成功 (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> registExamKit(@ApiParam(value = "検査キット登録リクエスト")
                                              @Valid RegistExamKitRequest registExamKitRequest) {
        this.examKitRegistrationUseCase.registExamKit(registExamKitRequest.getOrderCode(),
                registExamKitRequest.getOrderItemIdList());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /hclabo-customize/examination/examkits : 検査キット取得 <br/>
     * <span class="ldMUmp">カスタマイズ</span>
     * - 注文商品IDにひもづく検査キットを取得する <br/>
     * <h5 class="eONCmm">業務詳細</h5>
     * - 渡された検索条件で検査キットを検索する <br/>
     * - 検索条件 <br/>
     * - 検査キット．注文商品ID　=　渡された注文商品IDリスト <br/>
     * - 検査キット番号にひもづく検査結果も取得しリストにセットする <br/>
     * <h5 class="eONCmm">制約/ルール</h5>
     * - 検査結果リストの順番は表示順の昇順とする <br/>

     * @param examKitRequest 検査キットリクエスト  (required)
     * @return 検査キットレスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ExamKitListResponse> getExamKitList(@ApiParam(value = "注文商品IDリスト")
                                                              @NotNull @Valid ExamKitRequest examKitRequest) {

        List<ExamKitEntity> examKitEntityList =
                this.getExamKitByOrderItemListUseCase.getExamKitByOrderItemList(examKitRequest.getOrderItemIdList());

        ExamKitListResponse examKitListResponse = examinationHelper.toExamKitListResponse(examKitEntityList);

        return new ResponseEntity<>(examKitListResponse, HttpStatus.OK);
    }

    /**
     * POST /hclabo-customize/examination/batch/examkit-received-entry : 検査キット受領登録バッチ実行
     *
     * @param examKitReceivedEntryBatchRequest 検査キット受領登録バッチリクエスト
     * @return 結果レスポンス
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> examKitReceivedEntryBatch(@Valid ExamKitReceivedEntryBatchRequest examKitReceivedEntryBatchRequest) {
        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.examkitreceivedentry.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminId());
        message.setStartType(examKitReceivedEntryBatchRequest.getStartType());
        message.setFilePath(examKitReceivedEntryBatchRequest.getFilePath());

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;
        try {
            // キューにパブリッシュー
            messagePublisherService.publish(routing, message);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            response.setExecuteMessage("バッチの手動起動を受け付けました。");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(HTypeBatchName.BATCH_EXAMKIT_RECEIVED_ENTRY.getLabel() + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST /hclabo-customize/examination/batch/exam-results-entry : 検査結果登録バッチ実行 <br/>
     * <span class="ldMUmp">カスタマイズ</span>
     * - 検査結果登録バッチを非同期で実行する <br/>
     * <h5 class="eONCmm">業務詳細</h5>
     * - アップロードされた`検査結果CSV`を読み込み、対象検査キットの検査結果と検査状態を「一部検査完了／検査完了」に更新する <br/>
     * - アップロードされた`検査結果PDF`をサーバに保存する <br/>
     * - 検査結果登録が完了した注文の注文者に`検査結果通知メール`を送信する <br/>
     * <h5 class="eONCmm">制約/ルール</h5>
     * - 検査キット番号に紐付く注文商品が返品の状態（数量=0）であっても登録処理を行う <br/>
     * - 登録処理失敗時は、対象の`検査キット番号`をログ出力し処理を続行する <br/>
     * - 検査結果通知メール送信失敗は、当バッチでの検知は不可とする <br/>
     *
     * @param examResultsEntryBatchRequest 検査結果登録バッチリクエスト (required)
     * @return 結果レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> examResultsEntryBatch(
            @ApiParam(value = "検査結果登録バッチリクエスト")
            @Valid ExamResultsEntryBatchRequest examResultsEntryBatchRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.examresultsentry.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminId());
        message.setStartType(examResultsEntryBatchRequest.getStartType());
        message.setFilePath(examResultsEntryBatchRequest.getFilePath());
        message.setUploadType(examResultsEntryBatchRequest.getUploadType());

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;
        try {
            // キューにパブリッシュー
            messagePublisherService.publish(routing, message);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            response.setExecuteMessage("バッチの手動起動を受け付けました。");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(HTypeBatchName.BATCH_EXAM_RESULTS_ENTRY.getLabel() + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                    .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /hclabo-customize/examination/configInfos : 環境設定情報取得
     *
     * @return 環境設定情報レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ConfigInfoResponse> getConfigInfo() {
        ConfigInfoResponse configInfoResponse = new ConfigInfoResponse();

        // 検査結果PDFの格納場所
        String examResultsPdfStoragePath = PropertiesUtil.getSystemPropertiesValue("examresults.pdf.storage.path");
        // 「診療・診察時のお願い」のPDFのファイル名（フルパス）
        String examinationRulePdfPath = PropertiesUtil.getSystemPropertiesValue("examinationrulepdf.path");
        // 検索条件：検査キット番号（複数番号検索用）検索できる件数の最大数
        int examKitCodeListLength = Integer.parseInt(PropertiesUtil.getSystemPropertiesValue("examkitcode.list.length"));
        // 検索条件：検査キット番号（複数番号検索用）選択肢区切り文字
        String examKitCodeListDivchar = PropertiesUtil.getSystemPropertiesValue("examkitcode.list.divchar");

        configInfoResponse.setExamresultsPdfStoragePath(examResultsPdfStoragePath);
        configInfoResponse.setExaminationRulePdfPath(examinationRulePdfPath);
        configInfoResponse.setExamKitCodeListLength(examKitCodeListLength);
        configInfoResponse.setExamKitCodeListDivchar(examKitCodeListDivchar);

        return new ResponseEntity<>(configInfoResponse, HttpStatus.OK);
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
