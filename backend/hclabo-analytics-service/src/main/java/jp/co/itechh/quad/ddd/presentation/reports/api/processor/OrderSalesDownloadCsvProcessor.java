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
import jp.co.itechh.quad.core.dto.OrderSales.OrderSalesCsvDto;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.presentation.reports.api.ReportsHelper;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSales;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesSearchQueryCondition;
import jp.co.itechh.quad.ddd.usecase.reports.OrderSalesUseCase;
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
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 受注・売上集計CSVダウンロードプロセッサー
 *
 * @author Dieu.PhamQuang (VJP)
 */
@Component
@Scope("prototype")
public class OrderSalesDownloadCsvProcessor {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSalesDownloadCsvProcessor.class);

    /**
     * 受注・売上集計ユースケース
     */
    private final OrderSalesUseCase orderSalesUseCase;

    /**
     * 運営者エンドポイントAPI
     */
    private final AdministratorApi administratorApi;

    /**
     * 非同期処理サービス
     */
    private final AsyncService asyncService;

    /**
     * 変換ユーティリティ
     */
    private final ConversionUtility conversionUtility;

    /**
     * 受注・売上集計ヘルパー
     */
    private final ReportsHelper reportsHelper;

    /**
     * 通知サブドメインAPI
     */
    private final NotificationSubApi notificationSubApi;

    /**
     * 受注・売上集計CSVの出力上限
     */
    public static final int MAX_ORDER_SALES_CSV = -1;

    /**
     * 受注・売上集計CSVファイル名
     */
    private static final String FILE_NAME_ORDER_SALES_PRE = "t_Order";

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
    private static final String FILE_PRE = "ordersales/?file=";

    /**
     * バッチ名
     */
    private final String BATCH_NAME_ORDER_SALES = "受注・売上集計CSV";

    /**
     * 商品販売個数集計CSVファイルパス
     */
    private final String FILE_PATH_ORDER_SALES;

    /**
     * コンストラクタ
     *
     * @param orderSalesUseCase  受注・売上集計ユースケース
     * @param administratorApi   運営者エンドポイントAPI
     * @param asyncService       非同期処理サービス
     * @param conversionUtility  変換ユーティリティ
     * @param reportsHelper      受注・売上集計ヘルパー
     * @param notificationSubApi 通知サブドメインAPI
     * @param environment        the environment
     */
    public OrderSalesDownloadCsvProcessor(OrderSalesUseCase orderSalesUseCase,
                                          AdministratorApi administratorApi,
                                          AsyncService asyncService,
                                          ConversionUtility conversionUtility,
                                          ReportsHelper reportsHelper,
                                          NotificationSubApi notificationSubApi,
                                          Environment environment) {
        this.orderSalesUseCase = orderSalesUseCase;
        this.administratorApi = administratorApi;
        this.asyncService = asyncService;
        this.conversionUtility = conversionUtility;
        this.reportsHelper = reportsHelper;
        this.notificationSubApi = notificationSubApi;
        FILE_PATH_ORDER_SALES = environment.getProperty("orderSalesCsvAsynchronous.file.path");
    }

    /**
     * Processor
     *
     * @param queueMessage
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(QueueMessage queueMessage) throws JsonProcessingException {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_ORDER_SALES_CSV_ASYNCHRONOUS");
        batchLogging.setBatchName("受注・売上集計CSV非同期");
        batchLogging.setStartType(HTypeBatchStartType.MANUAL.getValue());
        BatchQueueMessage batchQueueMessage = new BatchQueueMessage();
        batchQueueMessage.setAdministratorSeq(queueMessage.getAdminId());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("受注・売上集計CSV非同期開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            OrderSalesSearchQueryCondition goodsSaleQueryCondition = queueMessage.getOrderSalesSearchQueryCondition();

            // 処理日時を生成 ファイル名、メール文面に利用
            LocalDateTime processingTime = LocalDateTime.now();

            // ファイル名を生成
            String fileName =
                            FILE_NAME_ORDER_SALES_PRE + DateTimeFormatter.ofPattern(FORMAT_DOWN).format(processingTime)
                            + FILE_EXTENSION;

            AtomicInteger cnt = new AtomicInteger();

            try {
                CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
                Map<String, OptionContentDto> csvDownloadOption;

                csvDownloadOptionDto.setOutputHeader(true);
                csvDownloadOption = CsvOptionUtil.getDefaultMapOptionContentDto(OrderSalesCsvDto.class);

                CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(csvDownloadOptionDto);

                FileWriter fileWriter =
                                new FileWriter(FILE_PATH_ORDER_SALES + fileName, csvDownloadOptionDto.getCharset());

                try (CSVPrinter printer = new CSVPrinter(fileWriter, outputCsvFormat)) {

                    if (csvDownloadOptionDto.isOutputHeader()) {
                        printer.printRecord(CsvDownloadHandler.outHeader(OrderSalesCsvDto.class, csvDownloadOption));
                    }

                    try (Stream<OrderSalesQueryModel> queryModelStream = orderSalesUseCase.download(
                                    goodsSaleQueryCondition)) {

                        List<OrderSalesQueryModel> orderSalesQueryModels =
                                        queryModelStream.collect(Collectors.toList());
                        List<OrderSalesQueryModel> newOrderSalesQueryModelList = new ArrayList<>();

                        reportsHelper.addEmptyDate(newOrderSalesQueryModelList, orderSalesQueryModels,
                                                   goodsSaleQueryCondition
                                                  );

                        for (OrderSalesQueryModel orderSalesQueryModel : newOrderSalesQueryModelList) {
                            try {
                                for (OrderSales orderSales : orderSalesQueryModel.getDataList()) {
                                    OrderSalesCsvDto orderSalesCsvDto =
                                                    reportsHelper.toOrderSalesCsvDto(orderSalesQueryModel, orderSales);
                                    printer.printRecord(CsvDownloadHandler.outCsvRecord(orderSalesCsvDto,
                                                                                        csvDownloadOption
                                                                                       ));
                                    cnt.getAndIncrement();
                                }
                            } catch (IOException | InvocationTargetException | IllegalAccessException e) {
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
                        }
                    }
                    printer.close();
                    fileWriter.close();
                }
            } catch (Exception e) {
                reportString.append(new Timestamp(System.currentTimeMillis()))
                            .append(" 予期せぬエラーが発生しました。処理を中断し終了します。")
                            .append("\n");
                reportString.append(e.getMessage());
                batchLogging.setProcessCount(null);
                batchLogging.setReport(reportString);
                batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
                LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

                throw new DomainException("ANALYTICS-ORSS0002-E");
            }

            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(queueMessage.getAdminId());

            DownloadCsvRequest downloadCsvRequest = new DownloadCsvRequest();

            try {
                ZipUtility zipUtility = ApplicationContextUtility.getBean(ZipUtility.class);
                String zipFilePath = zipUtility.zip(FILE_PATH_ORDER_SALES, fileName);
                downloadCsvRequest.setDownloadUrl(FILE_PRE + zipFilePath);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                throw new DomainException("ANALYTICS-ORSS0002-E");
            }

            downloadCsvRequest.setProcessingTime(conversionUtility.convertLocalDateTimeToDate(processingTime));
            downloadCsvRequest.setProcessingCnt(cnt.get());
            downloadCsvRequest.setMail(administratorResponse.getMail());
            downloadCsvRequest.setBatchName(BATCH_NAME_ORDER_SALES);

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
            downloadCsvErrorRequest.setBatchName(BATCH_NAME_ORDER_SALES);

            Object[] args = new Object[] {downloadCsvErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {DownloadCsvErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "downloadCsvError", args, argsClass);

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("受注・売上集計CSV非同期終了");
        }
    }
}