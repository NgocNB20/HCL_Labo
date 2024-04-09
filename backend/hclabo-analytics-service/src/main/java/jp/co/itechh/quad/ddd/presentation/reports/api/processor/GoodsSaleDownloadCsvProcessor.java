package jp.co.itechh.quad.ddd.presentation.reports.api.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.ZipUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.dto.soldgoods.SoldGoodsCSVDto;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.presentation.reports.api.ReportsHelper;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryModel;
import jp.co.itechh.quad.ddd.usecase.reports.GoodsSaleUseCase;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 商品販売個数集計 Csvダウンロード　processor
 */
@Component
@Scope("prototype")
public class GoodsSaleDownloadCsvProcessor {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsSaleDownloadCsvProcessor.class);

    /**
     * 商品販売個数集計ユーザケース
     */
    private final GoodsSaleUseCase goodsSaleUseCase;

    /**
     * 受注・売上集計ヘルパー
     */
    private final ReportsHelper reportsHelper;

    /**
     * 運営者エンドポイントAPI
     */
    private final AdministratorApi administratorApi;

    /**
     * 非同期処理サービス
     */
    private final AsyncService asyncService;

    /**
     * 通知サブドメインAPI
     */
    private final NotificationSubApi notificationSubApi;

    /**
     * 変換Utility
     */
    private final ConversionUtility conversionUtility;

    private static final String FILE_NAME_SOLD_GOODS_PRE = "t_SoldGoods";

    /**
     * 時間フォーマット(ダウンロード用)
     */
    private static final String FORMAT_DOWN = "yyyyMMdd_HHmmss";

    /**
     * ファイル拡張子
     */
    private static final String FILE_EXTENSION = ".csv";

    /**
     * ファイルパス
     */
    private static final String FILE_PRE = "goodssales/?file=";

    /**
     * 商品販売個数集計CSVファイルパス
     */
    private final String FILE_PATH_SOLD_GOODS;

    /**
     * バッチ名
     */
    private final String BATCH_NAME_SOLD_GOODS = "商品販売個数集計CSV";

    /**
     * コンストラクタ
     *
     * @param goodsSaleUseCase   商品販売個数集計ユーザケース
     * @param reportsHelper      受注・売上集計ヘルパー
     * @param administratorApi   運営者エンドポイントAPI
     * @param asyncService       非同期処理サービス
     * @param notificationSubApi 通知サブドメインAPI
     * @param conversionUtility  変換Utility
     * @param environment        environment
     */
    public GoodsSaleDownloadCsvProcessor(GoodsSaleUseCase goodsSaleUseCase,
                                         ReportsHelper reportsHelper,
                                         AdministratorApi administratorApi,
                                         AsyncService asyncService,
                                         NotificationSubApi notificationSubApi,
                                         ConversionUtility conversionUtility,
                                         Environment environment) {
        this.goodsSaleUseCase = goodsSaleUseCase;
        this.reportsHelper = reportsHelper;
        this.administratorApi = administratorApi;
        this.asyncService = asyncService;
        this.notificationSubApi = notificationSubApi;
        this.conversionUtility = conversionUtility;
        FILE_PATH_SOLD_GOODS = environment.getProperty("soldGoodsCsvAsynchronous.file.path");
    }

    /**
     * Processor
     *
     * @param queueMessage
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(QueueMessage queueMessage) throws JsonProcessingException {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_GOODS_SALES_CSV_ASYNCHRONOUS");
        batchLogging.setBatchName("商品販売個数集計CSV非同期");
        batchLogging.setStartType(HTypeBatchStartType.MANUAL.getValue());
        BatchQueueMessage batchQueueMessage = new BatchQueueMessage();
        batchQueueMessage.setAdministratorSeq(queueMessage.getAdminId());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("商品販売個数集計CSV非同期開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            GoodsSaleQueryCondition goodsSaleQueryCondition = queueMessage.getGoodsSaleQueryCondition();

            // 処理日時を生成 ファイル名、メール文面に利用
            LocalDateTime processingTime = LocalDateTime.now();

            // ファイル名を生成
            String fileName = FILE_NAME_SOLD_GOODS_PRE + DateTimeFormatter.ofPattern(FORMAT_DOWN).format(processingTime)
                              + FILE_EXTENSION;

            AtomicInteger cnt = new AtomicInteger();

            try {
                CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
                Map<String, OptionContentDto> csvDownloadOption;

                csvDownloadOptionDto.setOutputHeader(true);
                csvDownloadOption = CsvOptionUtil.getDefaultMapOptionContentDto(SoldGoodsCSVDto.class);

                CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(csvDownloadOptionDto);

                FileWriter fileWriter =
                                new FileWriter(FILE_PATH_SOLD_GOODS + fileName, csvDownloadOptionDto.getCharset());

                CSVPrinter printer = new CSVPrinter(fileWriter, outputCsvFormat);

                if (csvDownloadOptionDto.isOutputHeader()) {
                    List<String> outHeader = CsvDownloadHandler.outHeader(SoldGoodsCSVDto.class, csvDownloadOption);
                    CsvDownloadHandler.outHeaderForDateFromTo(outHeader, goodsSaleQueryCondition.getAggregateUnit(),
                                                              goodsSaleQueryCondition.getAggregateTimeFrom(),
                                                              goodsSaleQueryCondition.getAggregateTimeTo()
                                                             );
                    printer.printRecord(outHeader);
                }

                try (Stream<GoodsSaleQueryModel> queryModelStream = goodsSaleUseCase.download(
                                goodsSaleQueryCondition)) {
                    queryModelStream.forEach(queryModel -> {
                        try {
                            SoldGoodsCSVDto soldGoodsCSVDto =
                                            reportsHelper.toSoldGoodsCSVDto(queryModel, goodsSaleQueryCondition);
                            List<String> outCsvRecord =
                                            CsvDownloadHandler.outCsvRecord(soldGoodsCSVDto, csvDownloadOption);
                            CsvDownloadHandler.outCsvRecordForDateFromTo(
                                            outCsvRecord, soldGoodsCSVDto.getDateCSVDtoList());
                            printer.printRecord(outCsvRecord);
                            cnt.getAndIncrement();
                        } catch (IOException e) {
                            reportString.append(new Timestamp(System.currentTimeMillis()))
                                        .append(" 予期せぬエラーが発生しました。処理を中断し終了します。")
                                        .append("\n");
                            reportString.append(e.getMessage());
                            batchLogging.setProcessCount(null);
                            batchLogging.setReport(reportString);
                            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
                            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());
                            LOGGER.error(e.getMessage());
                            try {
                                printer.close();
                                fileWriter.close();
                            } catch (IOException ioException) {
                                LOGGER.error("例外処理が発生しました", ioException);
                                throw new RuntimeException(ioException);
                            }
                            throw new RuntimeException(e);
                        }
                    });
                }
                printer.close();
                fileWriter.close();
            } catch (Exception e) {
                reportString.append(new Timestamp(System.currentTimeMillis()))
                            .append(" 予期せぬエラーが発生しました。処理を中断し終了します。")
                            .append("\n");
                reportString.append(e.getMessage());
                batchLogging.setProcessCount(null);
                batchLogging.setReport(reportString);
                batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
                LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

                throw new DomainException("ANALYTICS-GOODSALESE0003-E");
            }

            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(queueMessage.getAdminId());

            DownloadCsvRequest downloadCsvRequest = new DownloadCsvRequest();

            try {
                ZipUtility zipUtility = ApplicationContextUtility.getBean(ZipUtility.class);
                String zipFilePath = zipUtility.zip(FILE_PATH_SOLD_GOODS, fileName);
                downloadCsvRequest.setDownloadUrl(FILE_PRE + zipFilePath);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                throw new DomainException("ANALYTICS-GOODSALESE0003-E");
            }

            downloadCsvRequest.setProcessingTime(conversionUtility.convertLocalDateTimeToDate(processingTime));
            downloadCsvRequest.setProcessingCnt(cnt.get());
            downloadCsvRequest.setMail(administratorResponse.getMail());
            downloadCsvRequest.setBatchName(BATCH_NAME_SOLD_GOODS);

            Object[] args = new Object[] {downloadCsvRequest};
            Class<?>[] argsClass = new Class<?>[] {DownloadCsvRequest.class};
            asyncService.asyncService(notificationSubApi, "downloadCsv", args, argsClass);
            reportString.append("登録件数[").append(cnt.get()).append("]で処理が終了しました。").append("\n");
            batchLogging.setProcessCount(cnt.get());
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());
        } catch (Exception e) {
            reportString.append(new Timestamp(System.currentTimeMillis()))
                        .append(" 予期せぬエラーが発生しました。処理を中断し終了します。")
                        .append("\n");
            reportString.append(e.getMessage());
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

            LOGGER.error(e.getMessage());

            // 処理日時を生成 ファイル名、メール文面に利用
            LocalDateTime processingTime = LocalDateTime.now();
            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(queueMessage.getAdminId());
            DownloadCsvErrorRequest downloadCsvErrorRequest = new DownloadCsvErrorRequest();
            downloadCsvErrorRequest.setMail(administratorResponse.getMail());
            downloadCsvErrorRequest.setProcessingTime(conversionUtility.convertLocalDateTimeToDate(processingTime));
            downloadCsvErrorRequest.setBatchName(BATCH_NAME_SOLD_GOODS);

            Object[] args = new Object[] {downloadCsvErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {DownloadCsvErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "downloadCsvError", args, argsClass);

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("商品販売個数集計CSV非同期終了");
        }
    }
}