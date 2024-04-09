package jp.co.itechh.quad.registasynchronous.presentation.api.processor;

import jp.co.itechh.quad.batch.logging.BatchLogging;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryProductBatchRequest;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.core.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.constant.type.HTypeUploadType;
import jp.co.itechh.quad.core.service.goods.goods.GoodsCsvUpLoadAsynchronousService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品登録非同期 Processor
 *
 * @author kimura
 */
@Component
@Scope("prototype")
public class RegistAsynchronousBatchProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(RegistAsynchronousBatchProcessor.class);

    /** タブ */
    private static final String TAB = "\t";

    /** 改行(キャリッジリターン) */
    private static final String LINE_FEED = "\r\n";

    /** 商品一括アップロードサービス */
    private final GoodsCsvUpLoadAsynchronousService goodsCsvUpLoadAsynchronousService;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** カテゴリApi */
    private final CategoryApi categoryApi;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** コンストラクタ */
    @Autowired
    public RegistAsynchronousBatchProcessor(GoodsCsvUpLoadAsynchronousService goodsCsvUpLoadAsynchronousService,
                                            AsyncService asyncService,
                                            NotificationSubApi notificationSubApi,
                                            CategoryApi categoryApi,
                                            HeaderParamsUtility headerParamsUtil) {
        this.goodsCsvUpLoadAsynchronousService = goodsCsvUpLoadAsynchronousService;
        this.asyncService = asyncService;
        this.notificationSubApi = notificationSubApi;
        this.categoryApi = categoryApi;
        this.headerParamsUtil = headerParamsUtil;
    }

    /**
     * Processor
     *
     * @param batchQueueMessage バッチキューメッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {

        // 管理者SEQにパラメーターを設定する
        headerParamsUtil.setAdministratorSeq(
                        notificationSubApi.getApiClient(), String.valueOf(batchQueueMessage.getAdministratorSeq()));

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_GOODS_ASYNCHRONOUS");
        batchLogging.setBatchName("商品登録非同期");
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("商品登録非同期");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        // バッチのジョッブ情報取得
        Integer administratorSeq = batchQueueMessage.getAdministratorSeq();
        Integer shopSeq = 1001;
        String filePath = batchQueueMessage.getFilePath();
        String uploadType = batchQueueMessage.getUploadType();

        CsvUploadResult uploadResult;
        try {
            // 商品登録更新成功SEQリスト
            List<Integer> goodsGroupSeqSuccessList = new ArrayList<>();

            // サービスを呼び商品登録を実行する
            uploadResult = callService(administratorSeq, filePath, uploadType, shopSeq, goodsGroupSeqSuccessList);

            if (ObjectUtils.isEmpty(uploadResult)) {
                throw new NullPointerException();
            }

            if (goodsGroupSeqSuccessList.size() > 0) {
                CategoryProductBatchRequest categoryProductBatchRequest = new CategoryProductBatchRequest();

                categoryProductBatchRequest.setGoodsGroupSeqList(goodsGroupSeqSuccessList);

                Object[] objectRequest = new Object[] {categoryProductBatchRequest};
                Class<?>[] typeClass = new Class<?>[] {CategoryProductBatchRequest.class};

                // カテゴリ商品更新バッチを呼び出す
                asyncService.asyncService(categoryApi, "updateCategoryProductBatch", objectRequest, typeClass);
            }

            GoodsAsynchronousRequest goodsAsynchronousRequest = toGoodsAsynchronousRequest(uploadResult);
            Object[] args = new Object[] {goodsAsynchronousRequest};
            Class<?>[] argsClass = new Class<?>[] {GoodsAsynchronousRequest.class};
            // メール送信
            asyncService.asyncService(notificationSubApi, "goodsAsynchronous", args, argsClass);

            if (uploadResult.isCsvUploadError()) {
                reportString.append("エラー有り終了\r\n詳細はメールにてご確認ください\n");
            } else {
                reportString.append("エラーなし正常終了\n");
            }
            batchLogging.setProcessCount(uploadResult.getRecordCount());
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception error) {

            // メール送信
            GoodsAsynchronousErrorRequest goodsAsynchronousErrorRequest = new GoodsAsynchronousErrorRequest();
            goodsAsynchronousErrorRequest.setErrorMessage(error.getClass().getName());

            Object[] args = new Object[] {goodsAsynchronousErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {GoodsAsynchronousErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "goodsAsynchronousError", args, argsClass);

            reportString.append("例外発生異常終了\n");

            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());
            throw error;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("商品登録非同期");
        }
    }

    /**
     * アップロードサービス呼び出し
     *
     * @param administratorSeq         管理者SEQ
     * @param targetFilePath           インポート対象のファイル
     * @param uploadType               アップロード種別
     * @param goodsGroupSeqSuccessList 商品登録更新成功SEQリスト
     * @return Csvアップロード結果
     */
    public CsvUploadResult callService(Integer administratorSeq,
                                       String targetFilePath,
                                       String uploadType,
                                       Integer shopSeq,
                                       List<Integer> goodsGroupSeqSuccessList) throws Exception {
        // 商品一括登録処理実行
        return goodsCsvUpLoadAsynchronousService.execute(administratorSeq, new File(targetFilePath),
                                                         EnumTypeUtil.getEnumFromValue(HTypeUploadType.class,
                                                                                       uploadType
                                                                                      ), shopSeq,
                                                         goodsGroupSeqSuccessList
                                                        );
    }

    /**
     * GoodsAsynchronousRequestに変換
     *
     * @param csvUploadResult CSVアップロード結果
     * @return GoodsAsynchronousRequest
     */
    public GoodsAsynchronousRequest toGoodsAsynchronousRequest(CsvUploadResult csvUploadResult) {

        // csvデータ行数(ヘッダー行を除く)
        int totalCount = csvUploadResult.getCsvRowCount();

        int successCount = csvUploadResult.getSuccessCount();

        // エラー件数
        int errorCount = countCsvUploadError(csvUploadResult);

        StringBuilder errorMessages = new StringBuilder();

        // CSVアップロードにエラーが合った場合、メール本文にエラーメッセージを付与
        if (csvUploadResult.isCsvUploadError()) {

            // バリデーションチェックエラーがある場合
            if (csvUploadResult.isInValid()) {
                CsvValidationResult csvValidationResult = csvUploadResult.getCsvValidationResult();
                for (InvalidDetail detail : csvValidationResult.getInvalidDetailList()) {
                    errorMessages.append(
                                    createMessage(detail.getRow(), null, detail.getColumnLabel(), detail.getMessage()));
                }
            }

            // データチェックエラーがある場合
            if (csvUploadResult.isError()) {
                for (CsvUploadError csvUploadError : csvUploadResult.getCsvUploadErrorlList()) {
                    errorMessages.append(createMessage(csvUploadError.getRow(), csvUploadError.getRowSpan(), null,
                                                       csvUploadError.getMessage()
                                                      ));
                }
            }
        }

        GoodsAsynchronousRequest goodsAsynchronousRequest = new GoodsAsynchronousRequest();
        // 総処理件数をメールリクエストに追加
        goodsAsynchronousRequest.setGoodsGroupSum(totalCount);
        // 成功件数をメールリクエストに追加
        goodsAsynchronousRequest.setSuccessSum(successCount);
        // 失敗件数をメールリクエストに追加
        goodsAsynchronousRequest.setErrorSum(errorCount);
        // エラー結果
        goodsAsynchronousRequest.setErrorMessage(errorMessages.toString());

        return goodsAsynchronousRequest;
    }

    /**
     * メール本文のメッセージを生成<br/>
     *
     * @param row        行番号
     * @param row        行数
     * @param columnName カラム名
     * @param message    内容
     * @return Item
     */
    public String createMessage(Integer row, Integer rowSpan, String columnName, String message) {
        StringBuilder sb = new StringBuilder();
        if (rowSpan != null && rowSpan > 1) {
            sb.append(row - (rowSpan - 1)).append("行目").append("～").append(row).append("行目").append(TAB);
        } else {
            sb.append(row).append("行目").append(TAB);
        }
        if (StringUtil.isNotEmpty(columnName)) {
            sb.append("項目名:").append(columnName).append(TAB);
        }
        sb.append(replaceLessGreaterThan(message)).append(LINE_FEED);
        return sb.toString();
    }

    /**
     * &lt;&gt;を置換しメッセージを返す<br/>
     *
     * @param message メッセージ
     * @return 置換メッセージ
     */
    public String replaceLessGreaterThan(String message) {

        if (StringUtil.isEmpty(message)) {
            return message;
        }

        return message.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }

    /**
     * CSVアップロードのエラー件数をカウント
     *
     * @param uploadResult CSVアップロード結果
     * @return errorCount エラー件数
     */
    public int countCsvUploadError(CsvUploadResult uploadResult) {
        int errorCount = 0;
        // アップロードCSVがバリデーションチェックか相関チェックでエラー判定を受けていた場合
        // バリデーションチェックエラーの場合
        if (uploadResult.isInValid()) {
            // バリデーションチェックエラーが起きた行数をカウント
            errorCount = uploadResult.getCsvValidationResult().getInvalidDetailList().size();
            // 相関チェックエラーの場合
        } else if (uploadResult.isError()) {
            // 相関チェックエラーの数をカウント
            errorCount = getErrorMap(uploadResult.getCsvUploadErrorlList()).size();
        }
        return errorCount;
    }

    /**
     * エラーの行数のmapを返す<br/>
     * ※失敗件数カウント用<br/>
     *
     * @param errorList エラー詳細リスト
     * @return Map エラー行数マップ
     */
    private Map<Integer, Integer> getErrorMap(List<CsvUploadError> errorList) {
        if (errorList.size() == 0) {
            return new HashMap<>();
        }

        Map<Integer, Integer> returnMap = new HashMap<>();
        for (CsvUploadError csvUploadError : errorList) {
            returnMap.put(csvUploadError.getRow(), csvUploadError.getRow());
        }
        return returnMap;
    }

}