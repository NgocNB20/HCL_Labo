package jp.co.itechh.quad.zipcodeupdate.presentation.api.processor;

import jp.co.itechh.quad.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.core.common.PreProcessModule;
import jp.co.itechh.quad.core.batch.core.throwable.NoDataException;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.offline.zip.dto.ZipCodeCsvDto;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.dao.zipcode.ZipCodeDao;
import jp.co.itechh.quad.core.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.core.entity.zipcode.ZipCodeEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.MailUtility;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 郵便番号自動更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class ZipCodeUpdateProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ZipCodeUpdateProcessor.class);

    /** 郵便番号Dao */
    private final ZipCodeDao zipCodeDao;

    /** 前処理共通機能 */
    private final PreProcessModule preProcessModule;

    /** 全国一括登録フラグ */
    private boolean allFlg;

    /** ダウンロード先 */
    private final String downloadDirectory;

    /** データフルパス */
    private static final String DL_URL = "https://www.post.japanpost.jp/zipcode/dl/kogaki/zip/";

    /** メールUtility */
    private final MailUtility mailUtility;

    /**
     * コンストラクタ<br/>
     */
    public ZipCodeUpdateProcessor(Environment environment, MailUtility mailUtility) {
        this.mailUtility = mailUtility;
        this.zipCodeDao = ApplicationContextUtility.getBean(ZipCodeDao.class);
        this.preProcessModule = ApplicationContextUtility.getBean(PreProcessModule.class);
        this.downloadDirectory = environment.getProperty("batch.file.path");
    }

    /**
     * 郵便番号データの追加、更新を行う<br/>
     *
     * @throws Exception 例外
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_ZIP_CODE");
        batchLogging.setBatchName("郵便番号自動更新");
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("郵便番号自動更新開始");

        // 変数初期化
        int count = 0;
        String msg;

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        // 処理対象年
        String yy;

        // 処理対象月
        String mm;

        // 処理対象日
        String dd;

        // 処理対象ファイル
        String filePath;

        // 初回登録
        if (batchQueueMessage.getAllFlag()) {
            // 全国一括登録フラグ設定
            allFlg = true;

            // 更新対象年月の設定
            LocalDate localDate = LocalDate.now();

            // 処理対象年
            yy = preProcessModule.getYearString(localDate);

            // 処理対象月
            mm = preProcessModule.getMonthString(localDate);

            // 処理対象日
            dd = preProcessModule.getDayString(localDate);

            // 登録データ
            String registData = "ken_all_" + yy + mm + dd + ".csv";

            // ダウンロードファイル名
            String zipFile = "ken_all.zip";

            // 登録ファイルだ取得
            filePath = preProcessModule.zipCodeRegistPreProcess(registData, zipFile, downloadDirectory, DL_URL);

        } else {
            // 全国一括登録フラグ設定
            allFlg = false;

            // 更新対象年月の設定
            LocalDate localDate = LocalDate.now().minusMonths(1);

            // 処理対象年
            yy = preProcessModule.getYearString(localDate);

            // 処理対象月
            mm = preProcessModule.getMonthString(localDate);

            // 更新データ
            String updateData = "update_" + yy + mm;

            // ダウンロードファイル(新規追加)名
            String addZipFile = "add_" + yy + mm + ".zip";

            // ダウンロードファイル(廃止)名
            String delZipFile = "del_" + yy + mm + ".zip";

            // 更新ファイルだ取得
            filePath = preProcessModule.zipCodeUpdatePreProcess(updateData, addZipFile, delZipFile, downloadDirectory,
                                                              DL_URL
                                                             );
        }

        CSVParser csvParser = null;
        try {
            if (StringUtils.isEmpty(filePath)) {
                throw new NoDataException();
            }

            // 処理開始前に引数を確認
            if (!allFlg) {
                // 更新モード
                if (yy == null || yy.isEmpty() || mm == null || mm.isEmpty()) {
                    // 更新モードの場合は引数に年月が必要なのにない
                    LOGGER.error("引数 yy(年)、mm(月) が不正です。");
                    throw new Exception("引数 yy(年)、mm(月) が不正です。");
                }
            }

            // Csvアップロード結果Dto作成
            CsvUploadResult csvUploadResult = new CsvUploadResult();

            // CSV読み込みのモジュールクラスを取得
            CsvReaderModule csvReaderModule = ApplicationContextUtility.getBean(CsvReaderModule.class);

            // CSV読み込みオプションを設定する
            CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
            csvReaderOptionDto.setValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);
            csvReaderOptionDto.setInputHeader(false);

            csvParser = csvReaderModule.readCsv(filePath, ZipCodeCsvDto.class, csvReaderOptionDto);

            // CSVファイルにデータが存在しなかった場合
            if (csvParser == null || !csvParser.iterator().hasNext()) {
                throw new NoDataException();
            }

            // CSVファイルに1件ずつ読み込んで処理する
            while (csvParser.iterator().hasNext()) {
                // CSVデータからDTOに変換
                ZipCodeCsvDto zipCodeCsvDto =
                                csvReaderModule.convertCsv2Dto(csvParser.iterator().next(), ZipCodeCsvDto.class);

                // カウント数取得
                count = csvReaderModule.getRecordCount();

                if (zipCodeCsvDto != null) {
                    // 登録更新
                    insertOrUpdate(zipCodeCsvDto);
                } else {
                    // 不正データがあった場合
                    throw new IOException();
                }

                // バリデータエラー行数限界値の場合 終了
                if (csvReaderModule.getErrorCount() > csvReaderOptionDto.getValidLimit()) {
                    csvUploadResult.setValidLimitFlg(true);
                    throw new Exception();
                }
            }

            // 全データ登録完了
            if (allFlg) {
                msg = "郵便番号の全国一括登録が完了しました。";
            } else {
                msg = yy + " 年 " + mm + " 月の郵便番号更新の反映が完了しました。";
            }

            // レポート処理
            reportString.append(msg).append("\n");
            batchLogging.setProcessCount(count);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            // メール送信
            mailUtility.sendMail(HTypeBatchName.BATCH_ZIP_CODE.getValue(), msg, true,
                                 batchQueueMessage.getAdministratorSeq()
                                );

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

        } catch (IOException e) {
            // CSVファイルの桁数、型がテーブルと一致しない場合に発生
            if (allFlg) {
                msg = "郵便番号の全国一括登録を行おうとしましたが、" + count + "行目に不正なデータが含まれていたため処理は無効になりました。";
            } else {
                msg = yy + " 年 " + mm + " 月の郵便番号の更新を行おうとしましたが、" + count + " 行目に不正なデータが含まれていたため処理は無効になりました。";
            }

            reportString.append(msg).append("\n");
            batchLogging.setProcessCount(count);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());
            LOGGER.error(reportString.toString());

            // メール送信
            mailUtility.sendMail(HTypeBatchName.BATCH_ZIP_CODE.getValue(), msg, false,
                                 batchQueueMessage.getAdministratorSeq()
                                );

            throw new IOException(reportString.toString());
        } catch (NoDataException e) {
            // 更新データが「0」件の場合
            if (allFlg) {
                msg = "郵便番号の全国一括登録を行おうとしましたが、対象のファイルにデータが存在しないため、処理は無効になりました。\n";
                msg += "対象のファイル、ダウンロードサイトをご確認ください。\n";
                msg += "(引数 : all=[" + allFlg + "])";
            } else {
                msg = "郵便番号の月次更新を行おうとしましたが、対象のファイルにデータが存在しないため、処理は無効になりました。\n";
                msg += "対象のファイル、ダウンロードサイトをご確認ください。\n";
                msg += "(引数 : yy=[" + yy + "] mm=[" + mm + "] all=[" + allFlg + "])";
            }

            reportString.append(msg).append("\n");
            batchLogging.setProcessCount(count);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());
            LOGGER.error(String.valueOf(e));
            LOGGER.error(reportString.toString());

            // メール送信
            mailUtility.sendMail(HTypeBatchName.BATCH_ZIP_CODE.getValue(), msg, false,
                                 batchQueueMessage.getAdministratorSeq()
                                );
            throw e;
        } catch (Exception e) {
            if (allFlg) {
                msg = "郵便番号の全国一括登録を行おうとしましたが、処理中に " + e.getClass().getName() + " が発生したため処理は無効になりました。";
                msg += "(引数 : all=[" + allFlg + "])";
            } else {
                msg = "郵便番号の月次更新を行おうとしましたが、処理中に " + e.getClass().getName() + " が発生したため処理は無効になりました。";
                msg += "(引数 : yy=[" + yy + "] mm=[" + mm + "] all=[" + allFlg + "])";
            }

            if (count > 0) {
                // 読み込みが開始していれば、行数も出力する
                LOGGER.error(String.format("%1$,d行目の処理中に想定外の例外が発生しました。", count));
            }

            reportString.append(msg).append("\n");
            batchLogging.setProcessCount(count);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());
            LOGGER.error(reportString.toString());

            // メール送信
            mailUtility.sendMail(HTypeBatchName.BATCH_ZIP_CODE.getValue(), msg, false,
                                 batchQueueMessage.getAdministratorSeq()
                                );
            throw e;
        } finally {
            if (csvParser != null && !csvParser.isClosed()) {
                csvParser.close();
            }
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("郵便番号自動更新終了");
        }
    }

    /**
     * 郵便番号データを登録／更新する<br/>
     * 郵便番号マスタにデータが存在しない場合は登録処理を<br/>
     * データが存在する場合は更新処理を行う。<br/>
     *
     * @param csvDto 郵便番号データ
     */
    protected void insertOrUpdate(ZipCodeCsvDto csvDto) {
        // プライマリーキーでマスタを検索
        // 存在しなければ新規登録、存在すれば更新処理を行う。
        ZipCodeEntity entity = convertToEntity(csvDto);
        String localCode = entity.getLocalCode();
        String oldZipCode = entity.getOldZipCode();
        String zipCode = entity.getZipCode();
        String prefectureName = entity.getPrefectureName();
        String cityName = entity.getCityName();
        String townName = entity.getTownName();

        ZipCodeEntity searchResult =
                        zipCodeDao.getEntity(localCode, oldZipCode, zipCode, prefectureName, cityName, townName);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        if (searchResult == null) {
            // ＤＢ上に存在しない住所なので登録
            entity.setRegistTime(dateUtility.getCurrentTime());
            entity.setUpdateTime(dateUtility.getCurrentTime());
            zipCodeDao.insert(entity);
        } else {
            // ＤＢ上に存在する住所なので更新
            entity.setRegistTime(searchResult.getRegistTime());
            entity.setUpdateTime(dateUtility.getCurrentTime());
            zipCodeDao.update(entity);
        }
    }

    /**
     * 郵便番号データを郵便番号Entityに変換<br/>
     *
     * @param csvDto 郵便番号データ
     * @return 郵便番号Entity
     */
    protected ZipCodeEntity convertToEntity(ZipCodeCsvDto csvDto) {
        ZipCodeEntity entity = ApplicationContextUtility.getBean(ZipCodeEntity.class);
        entity.setLocalCode(csvDto.getLocalCode());
        entity.setOldZipCode(csvDto.getOldZipCode());
        entity.setZipCode(csvDto.getZipCode());
        entity.setPrefectureKana(csvDto.getPrefectureKana());
        entity.setCityKana(csvDto.getCityKana());
        entity.setTownKana(csvDto.getTownKana());
        entity.setPrefectureName(csvDto.getPrefectureName());
        entity.setCityName(csvDto.getCityName());
        entity.setTownName(csvDto.getTownName());
        entity.setAnyZipFlag(csvDto.getAnyZipFlag());
        entity.setNumberFlag1(csvDto.getNumberFlag1());
        entity.setNumberFlag2(csvDto.getNumberFlag2());
        entity.setNumberFlag3(csvDto.getNumberFlag3());
        entity.setUpdateVisibleType(csvDto.getUpdateVisibleType());
        entity.setUpdateNoteType(csvDto.getUpdateNoteType());

        return entity;
    }

}