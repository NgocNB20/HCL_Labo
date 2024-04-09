package jp.co.itechh.quad.stockreport.presentation.api.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.batch.logging.BatchLogging;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.CsvExtractUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dao.goods.stock.StockDao;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDownloadCsvDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockSearchForDaoConditionDto;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.CsvUtility;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateFile;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockReportRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 在庫状況確認メール送信 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class StockReportBatchProcessor {
    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(StockReportBatchProcessor.class);

    /** 在庫管理テーブル DAO */
    private final StockDao stockDao;

    /** CsvUtility */
    private final CsvUtility csvUtility;

    /** 添付ファイル名プリフィックス */
    private final String prefix = "STOCK_REPORT";

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 添付ファイル名日付フォーマット */
    private String dateFormat = "yyyyMMddHHmm";

    /** 添付ファイルサフィックス */
    private String surffix = ".csv";

    /**
     * コンストラクタ
     *
     * @param stockDao   在庫管理テーブルDao
     * @param csvUtility CsvUtility
     * @param notificationSubApi 通知サブドメイン
     * @param headerParamsUtil ヘッダパラメーターユーティル
     * @param asyncService 非同期処理サービス
     */
    @Autowired
    public StockReportBatchProcessor(StockDao stockDao,
                                     CsvUtility csvUtility,
                                     NotificationSubApi notificationSubApi,
                                     HeaderParamsUtility headerParamsUtil,
                                     AsyncService asyncService) {
        this.stockDao = stockDao;
        this.csvUtility = csvUtility;
        this.notificationSubApi = notificationSubApi;
        this.headerParamsUtil = headerParamsUtil;
        this.asyncService = asyncService;
    }

    /**
     * Consumer
     *
     * @param message メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage message) throws JsonProcessingException {

        // 管理者SEQにパラメーターを設定する
        headerParamsUtil.setAdministratorSeq(
                        notificationSubApi.getApiClient(), String.valueOf(message.getAdministratorSeq()));

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_STOCK_REPORT");
        batchLogging.setBatchName("在庫状況確認メール送信");
        batchLogging.setStartType(Objects.requireNonNull(
                        EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, message.getStartType())).getLabel());
        batchLogging.setInputData(message);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("在庫状況確認メール送信開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        Integer shopSeq = 1001;

        try {
            LOGGER.info("添付ファイルを作成");
            String attachFilePath = this.getCsvContent(shopSeq);
            if (StringUtils.isEmpty(attachFilePath)) {
                LOGGER.error("在庫CSVの出力に失敗しました。");
                reportString.append(new Timestamp(System.currentTimeMillis())).append(" 予期せぬエラーが発生しました。処理を中断し終了します。");
                batchLogging.setProcessCount(null);
                batchLogging.setReport(reportString);
                batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
            }

            // メール送信
            StockReportRequest stockReportRequest = getStockReportRequest(attachFilePath);

            Object[] args = new Object[] {stockReportRequest};
            Class<?>[] argsClass = new Class<?>[] {StockReportRequest.class};
            asyncService.asyncService(notificationSubApi, "stockReport", args, argsClass);

            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());
        } catch (Exception e) {
            reportString.append(new Timestamp(System.currentTimeMillis())).append(" 予期せぬエラーが発生しました。処理を中断し終了します。");
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
            LOGGER.error("例外処理が発生しました", e);
            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("在庫状況確認メール送信終了");
        }
    }

    /**
     * Get StockReport Request
     *
     * @param attachFilePath 添付ファイル：パス方式で渡す場合（複数添付ファイルの対応が余力があれば追加）
     * @return StockReportRequest
     */
    public StockReportRequest getStockReportRequest(String attachFilePath) {
        SimpleDateFormat sdf = new SimpleDateFormat(this.dateFormat);
        StockReportRequest stockReportRequest = new StockReportRequest();
        List<GoodsImageUpdateFile> goodsImageUpdateFileList = new ArrayList<>();

        GoodsImageUpdateFile goodsImageUpdateFile = new GoodsImageUpdateFile();
        goodsImageUpdateFile.setFileName(this.prefix + sdf.format(new Date()) + this.surffix);
        goodsImageUpdateFile.setFilePath(attachFilePath);

        goodsImageUpdateFileList.add(goodsImageUpdateFile);

        stockReportRequest.setFileList(goodsImageUpdateFileList);
        return stockReportRequest;
    }

    /**
     * 添付用CSV作成
     *
     * @return CSVファイルのバイト配列
     */
    protected String getCsvContent(Integer shopSeq) {
        // 在庫状況を取得するための条件 DTO
        StockSearchForDaoConditionDto dto = ApplicationContextUtility.getBean(StockSearchForDaoConditionDto.class);
        dto.setShopSeq(shopSeq);
        dto.setSite(HTypeSiteType.FRONT_PC.getValue());

        // セットした検索条件を列挙
        LOGGER.info("検索条件：");
        LOGGER.info("shopSeq：" + dto.getShopSeq());

        // Stream方式でCSVダウンロードサービスから取得する
        Stream<StockDownloadCsvDto> resultStream = this.stockDao.exportCsvByStockSearchForDaoConditionDto(dto);

        // CsvUtility取得
        String filePath = csvUtility.getBatchTargetFileName(this.prefix);

        // CSVダウンロードオプションを設定する
        CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
        csvDownloadOptionDto.setOutputHeader(true);

        // 出力ファイルの設定
        CsvExtractUtility csvExtractUtility =
                        new CsvExtractUtility(StockDownloadCsvDto.class, csvDownloadOptionDto, resultStream, filePath,
                                              null
                        );

        // CSV 作成
        LOGGER.info("CSVヘッダ行 & CSVボディ行を書き出し");

        try {
            LOGGER.info("CSVの作成が完了");
            csvExtractUtility.outCsv();
            return filePath;
        } catch (IOException e) {
            LOGGER.error("在庫CSVの出力に失敗しました。", e);
            return null;
        }
    }
}