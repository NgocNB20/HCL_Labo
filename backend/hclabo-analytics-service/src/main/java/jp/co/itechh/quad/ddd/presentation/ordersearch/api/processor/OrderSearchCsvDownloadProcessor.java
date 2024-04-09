package jp.co.itechh.quad.ddd.presentation.ordersearch.api.processor;

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
import jp.co.itechh.quad.core.dto.order.OrderCSVDto;
import jp.co.itechh.quad.core.dto.shipment.ShipmentCSVDto;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoModule;
import jp.co.itechh.quad.ddd.presentation.ordersearch.api.OrderSearchHelper;
import jp.co.itechh.quad.ddd.usecase.order.OrderSearchCsvOptionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.OrderSearchUseCase;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchCsvOptionQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryCondition;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.DownloadCsvRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 受注検索 CSVダウンロード Processor
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class OrderSearchCsvDownloadProcessor {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSearchCsvDownloadProcessor.class);

    /**
     * 受注検索ユーザケース
     */
    private final OrderSearchUseCase orderSearchUseCase;

    /**
     * 受注検索Helperクラス
     */
    private final OrderSearchHelper orderSearchHelper;

    /**
     * ページ情報モジュール
     */
    private final PageInfoModule pageInfoModule;

    /**
     * 通知サブドメインAPI
     */
    private final NotificationSubApi notificationSubApi;

    /**
     * 運営者エンドポイントAPI
     */
    private final AdministratorApi administratorApi;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * 非同期処理サービス
     */
    private final AsyncService asyncService;

    /**
     * 受注検索CSVオプション ユースケース
     */
    private final OrderSearchCsvOptionUseCase optionUseCase;

    /**
     * 受注CSVファイルパス
     */
    private final String FILE_PATH_ORDER;

    /**
     * 出荷CSVファイルパス
     */
    private final String FILE_PATH_SHIPMENT;

    /**
     * 受注CSVファイル名
     */
    private static final String FILE_NAME_ORDER_PRE = "order";

    /**
     * 出荷CSVファイル
     */
    private static final String FILE_NAME_SHIPMENT_PRE = "shipment";

    /**
     * ファイルパス
     */
    private static final String FILE_PRE = "order/?file=";

    /**
     * ファイル拡張子
     */
    private static final String FILE_EXTENSION = ".csv";

    /**
     * 時間フォーマット(ダウンロード用)
     */
    private static final String FORMAT_DOWN = "yyyyMMdd_HHmmss";

    /**
     * バッチ名
     */
    private final String BATCH_NAME_ORDER;

    /**
     * バッチ名
     */
    private final String BATCH_NAME_SHIPMENT;

    /**
     * コンストラクタ
     *
     * @param orderSearchUseCase 受注検索ユーザケース
     * @param orderSearchHelper  受注検索Helperクラス
     * @param pageInfoModule     ページ情報モジュール
     * @param notificationSubApi 通知サブドメインAPI
     * @param administratorApi   運営者エンドポイントAPI
     * @param conversionUtility  変換ユーティリティクラス
     * @param asyncService       非同期処理サービス
     * @param optionUseCase      受注検索CSVオプションユースケース
     * @param environment        environment
     */
    @Autowired
    public OrderSearchCsvDownloadProcessor(OrderSearchUseCase orderSearchUseCase,
                                           OrderSearchHelper orderSearchHelper,
                                           PageInfoModule pageInfoModule,
                                           NotificationSubApi notificationSubApi,
                                           AdministratorApi administratorApi,
                                           ConversionUtility conversionUtility,
                                           AsyncService asyncService,
                                           OrderSearchCsvOptionUseCase optionUseCase,
                                           Environment environment) {
        this.orderSearchUseCase = orderSearchUseCase;
        this.orderSearchHelper = orderSearchHelper;
        this.pageInfoModule = pageInfoModule;
        this.notificationSubApi = notificationSubApi;
        this.administratorApi = administratorApi;
        this.conversionUtility = conversionUtility;
        this.asyncService = asyncService;
        this.optionUseCase = optionUseCase;
        FILE_PATH_ORDER = environment.getProperty("orderCsvAsynchronous.file.path");
        FILE_PATH_SHIPMENT = environment.getProperty("shipmentCsvAsynchronous.file.path");
        BATCH_NAME_ORDER = "受注CSV";
        BATCH_NAME_SHIPMENT = "出荷CSV";
    }

    /**
     * 受注CSVDL処理
     *
     * @param queueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processorOrderCsv(QueueMessage queueMessage) throws JsonProcessingException {
        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_ORDER_CSV_ASYNCHRONOUS");
        batchLogging.setBatchName("受注CSV非同期");
        batchLogging.setStartType(HTypeBatchStartType.MANUAL.getValue());
        BatchQueueMessage batchQueueMessage = new BatchQueueMessage();
        batchQueueMessage.setAdministratorSeq(queueMessage.getAdminId());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("受注CSV非同期開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            OrderSearchQueryCondition orderSearchQueryCondition = queueMessage.getOrderSearchQueryCondition();

            orderSearchQueryCondition.setPageInfoForQuery(
                            pageInfoModule.setPageInfoForQuery(orderSearchQueryCondition.getPage(),
                                                               orderSearchQueryCondition.getLimit(),
                                                               orderSearchQueryCondition.getOrderBy(),
                                                               orderSearchQueryCondition.getSort()
                                                              ));

            // 処理日時を生成 ファイル名、メール文面に利用
            LocalDateTime processingTime = LocalDateTime.now();

            // ファイル名を生成
            String fileName = FILE_NAME_ORDER_PRE + DateTimeFormatter.ofPattern(FORMAT_DOWN).format(processingTime)
                              + FILE_EXTENSION;

            AtomicInteger cnt = new AtomicInteger();

            try {
                // Apache Common CSV を特化したCSVプリンター（設定したCSVフォーマットに沿ってデータを出力）を初期化する
                CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
                Map<String, OptionContentDto> csvDownloadOption;

                // OptionIdを指定する場合
                if (queueMessage.getOption() != null && StringUtils.isNotEmpty(
                                queueMessage.getOption().getOptionId())) {
                    // OptionIdでクエリモデルを取得する
                    OrderSearchCsvOptionQueryModel queryModel =
                                    optionUseCase.getOrderSearchCsvOptionById(queueMessage.getOption().getOptionId());

                    // CsvダウンロードオプションDtoを作成する
                    csvDownloadOptionDto.setOutputHeader(queryModel.isOutHeader());
                    csvDownloadOption = new LinkedHashMap<>();
                    for (OptionContentDto optionContentDto : queryModel.getOptionContent()) {
                        if (optionContentDto != null) {
                            csvDownloadOption.put(optionContentDto.getItemName(), optionContentDto);
                        }
                    }
                    // OptionIdを指定しない場合（デフォルト）
                } else {
                    csvDownloadOptionDto.setOutputHeader(true);
                    csvDownloadOption = CsvOptionUtil.getDefaultMapOptionContentDto(OrderCSVDto.class);
                }

                CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(csvDownloadOptionDto);

                FileWriter fileWriter = new FileWriter(FILE_PATH_ORDER + fileName, csvDownloadOptionDto.getCharset());

                CSVPrinter printer = new CSVPrinter(fileWriter, outputCsvFormat);

                if (csvDownloadOptionDto.isOutputHeader()) {
                    printer.printRecord(CsvDownloadHandler.outHeader(OrderCSVDto.class, csvDownloadOption));
                }

                try (Stream<OrderCSVDto> orderCsvStream = orderSearchUseCase.download(orderSearchQueryCondition)) {
                    orderCsvStream.forEach((order -> {
                        try {
                            printer.printRecord(CsvDownloadHandler.outCsvRecord(order, csvDownloadOption));
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
                    }));
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

                throw new DomainException("ANALYTICS-OSC0001-E");
            }

            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(queueMessage.getAdminId());

            DownloadCsvRequest downloadCsvRequest = new DownloadCsvRequest();

            if ("zip".equals(queueMessage.getType())) {
                try {
                    ZipUtility zipUtility = ApplicationContextUtility.getBean(ZipUtility.class);
                    String zipFilePath = zipUtility.zip(FILE_PATH_ORDER, fileName);
                    downloadCsvRequest.setDownloadUrl(FILE_PRE + zipFilePath);
                } catch (Exception e) {
                    LOGGER.error("例外処理が発生しました", e);
                    throw new DomainException("ANALYTICS-OSC0001-E");
                }
            } else {
                downloadCsvRequest.setDownloadUrl(FILE_PRE + fileName);
            }
            downloadCsvRequest.setProcessingTime(conversionUtility.convertLocalDateTimeToDate(processingTime));
            downloadCsvRequest.setProcessingCnt(cnt.get());
            downloadCsvRequest.setMail(administratorResponse.getMail());
            downloadCsvRequest.setBatchName(BATCH_NAME_ORDER);

            Object[] args = new Object[] {downloadCsvRequest};
            Class<?>[] argsClass = new Class<?>[] {DownloadCsvRequest.class};
            asyncService.asyncService(notificationSubApi, "downloadCsv", args, argsClass);
            reportString.append("登録件数[").append(cnt.get()).append("]で処理が終了しました。").append("\n");
            ;
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
            downloadCsvErrorRequest.setBatchName(BATCH_NAME_ORDER);

            Object[] args = new Object[] {downloadCsvErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {DownloadCsvErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "downloadCsvError", args, argsClass);

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("受注CSV非同期終了");
        }
    }

    /**
     * 出荷CSVDL処理
     *
     * @param queueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processorShipmentCsv(QueueMessage queueMessage) throws JsonProcessingException {
        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_SHIPMENT_CSV_ASYNCHRONOUS");
        batchLogging.setBatchName("出荷CSV非同期");
        batchLogging.setStartType(HTypeBatchStartType.MANUAL.getValue());
        BatchQueueMessage batchQueueMessage = new BatchQueueMessage();
        batchQueueMessage.setAdministratorSeq(queueMessage.getAdminId());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("出荷CSV非同期開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            OrderSearchQueryCondition orderSearchQueryCondition = queueMessage.getOrderSearchQueryCondition();

            orderSearchQueryCondition.setPageInfoForQuery(
                            pageInfoModule.setPageInfoForQuery(orderSearchQueryCondition.getPage(),
                                                               orderSearchQueryCondition.getLimit(),
                                                               orderSearchQueryCondition.getOrderBy(),
                                                               orderSearchQueryCondition.getSort()
                                                              ));

            // 処理日時を生成 ファイル名、メール文面に利用
            LocalDateTime processingTime = LocalDateTime.now();

            // ファイル名を生成
            String fileName = FILE_NAME_SHIPMENT_PRE + DateTimeFormatter.ofPattern(FORMAT_DOWN).format(processingTime)
                              + FILE_EXTENSION;

            AtomicInteger cnt = new AtomicInteger();

            try {
                // Apache Common CSV を特化したCSVプリンター（設定したCSVフォーマットに沿ってデータを出力）を初期化する
                CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
                Map<String, OptionContentDto> csvDownloadOption;

                csvDownloadOptionDto.setOutputHeader(true);
                csvDownloadOption = CsvOptionUtil.getDefaultMapOptionContentDto(ShipmentCSVDto.class);

                CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(csvDownloadOptionDto);

                FileWriter fileWriter =
                                new FileWriter(FILE_PATH_SHIPMENT + fileName, csvDownloadOptionDto.getCharset());

                CSVPrinter printer = new CSVPrinter(fileWriter, outputCsvFormat);

                if (csvDownloadOptionDto.isOutputHeader()) {
                    printer.printRecord(CsvDownloadHandler.outHeader(ShipmentCSVDto.class, csvDownloadOption));
                }

                try (Stream<ShipmentCSVDto> queryModelStream = orderSearchUseCase.shipment(orderSearchQueryCondition)) {
                    queryModelStream.forEach((queryModel -> {
                        try {
                            printer.printRecord(CsvDownloadHandler.outCsvRecord(queryModel, csvDownloadOption));
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
                    }));
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

                throw new DomainException("ANALYTICS-OSC0001-E");
            }

            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(queueMessage.getAdminId());

            DownloadCsvRequest downloadCsvRequest = new DownloadCsvRequest();

            if ("zip".equals(queueMessage.getType())) {
                try {
                    ZipUtility zipUtility = ApplicationContextUtility.getBean(ZipUtility.class);
                    String zipFilePath = zipUtility.zip(FILE_PATH_SHIPMENT, fileName);
                    downloadCsvRequest.setDownloadUrl(FILE_PRE + zipFilePath);
                } catch (Exception e) {
                    LOGGER.error("例外処理が発生しました", e);
                    throw new DomainException("ANALYTICS-OSC0001-E");
                }
            } else {
                downloadCsvRequest.setDownloadUrl(FILE_PRE + fileName);
            }
            downloadCsvRequest.setProcessingTime(conversionUtility.convertLocalDateTimeToDate(processingTime));
            downloadCsvRequest.setProcessingCnt(cnt.get());
            downloadCsvRequest.setMail(administratorResponse.getMail());
            downloadCsvRequest.setBatchName(BATCH_NAME_SHIPMENT);

            Object[] args = new Object[] {downloadCsvRequest};
            Class<?>[] argsClass = new Class<?>[] {DownloadCsvRequest.class};
            asyncService.asyncService(notificationSubApi, "downloadCsv", args, argsClass);
            reportString.append("登録件数[").append(cnt.get()).append("]で処理が終了しました。").append("\n");
            ;
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
            downloadCsvErrorRequest.setBatchName(BATCH_NAME_SHIPMENT);

            Object[] args = new Object[] {downloadCsvErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {DownloadCsvErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "downloadCsvError", args, argsClass);

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("出荷CSV非同期終了");
        }
    }
}