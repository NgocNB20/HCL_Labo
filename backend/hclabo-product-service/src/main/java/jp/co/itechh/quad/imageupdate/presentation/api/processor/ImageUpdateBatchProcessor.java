package jp.co.itechh.quad.imageupdate.presentation.api.processor;

import jp.co.itechh.quad.batch.logging.BatchLogging;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.util.common.DiffUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.CsvExtractUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupImageDao;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsImageProcErrorCsvDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsImageProcSuccessCsvDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupImageOpenstatusDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateFile;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateResultData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品画像更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class ImageUpdateBatchProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ImageUpdateBatchProcessor.class);

    /** 処理日時（ファイル出力日時） */
    private Timestamp outputTime;

    /************
     * ディレクトリ
     ************/
    /** 画像ディレクトリ */
    private String imageDir;

    /** 画像追加ディレクトリ */
    private String inputDir;

    /** エラーディレクトリ */
    private String errorDir;

    /** バックアップディレクトリ */
    private String backupDir;

    /** 作業ディレクトリ */
    private String workDir;

    /************
     * 集計
     ************/
    /** 処理結果出力用リスト 商品画像処理CSV */
    private List<GoodsImageProcSuccessCsvDto> procSuccessCsvDtoList;

    /** 処理結果出力用リスト 商品画像処理エラーCSV */
    private List<GoodsImageProcErrorCsvDto> procErrorCsvDtoList;

    /** 集計用件数リスト 商品管理番号 合計件数 */
    private Map<String, String> cntGoodsGroupSumMap;

    /** 集計用件数リスト 商品管理番号 商品管理番号が存在しない */
    private Map<String, String> cntGoodsGroupNotExistMap;

    /** 集計用件数リスト 商品管理番号 画像ファイル名が不適切 */
    private Map<String, String> cntGoodsGroupFileNameNotMatchMap;

    /** 集計用件数リスト 商品管理番号 商品が削除されている */
    private Map<String, String> cntGoodsGroupOpenStateDelMap;

    /** 集計用件数リスト 商品管理番号 商品画像 追加 */
    private Map<String, String> cntGoodsGroupUpdateMap;

    /** 集計用件数リスト 商品管理番号 商品画像 削除 */
    private Map<String, String> cntGoodsGroupRegistMap;

    /** 集計用件数リスト 商品管理番号 商品画像 更新 */
    private Map<String, String> cntGoodsGroupDeleteMap;

    /** 商品グループ画像 */
    /** 集計用件数 画像 合計件数 */
    private int cntGoodsGroupImageSum;

    /** 集計用件数 画像 商品管理番号が存在しない */
    private int cntGoodsGroupImageNotExist;

    /** 集計用件数 画像 画像ファイル名が不適切 */
    private int cntGoodsGroupImageFileNameNotMatch;

    /** 集計用件数 画像 商品が削除されている */
    private int cntGoodsGroupImageOpenStateDel;

    /** 集計用件数 画像 商品画像 追加 */
    private int cntGoodsGroupImageRegist;

    /** 集計用件数 画像 商品画像 更新 */
    private int cntGoodsGroupImageUpdate;

    /** 集計用件数 画像 商品画像 削除 */
    private int cntGoodsGroupImageDelete;

    /** 画像ファイル画像ディレクトリ移動時エラーメッセージリスト */
    private List<String> toImageDirCopyErrorMsgList;

    /** 画像ファイル画像削除エラーメッセージリスト */
    private List<String> toImageDirDeleteErrorMsgList;

    /** 画像ファイルエラーディレクトリ移動時エラーメッセージリスト */
    private List<String> toErrorDirCopyErrorMsgList;

    /**
     * 削除処理の処理件数<br />
     * 商品グループ画像テーブル及び商品規格画像テーブルを全件処理する為、数回に分けて取得する。<br />
     * リミット件数分のレコードが、メモリに保持される。<br />
     */
    private static final int DELETE_TARGET_LIMIT = 100000;

    /** 最大商品グループ画像数 */
    private int goodsGroupImageMaxCnt;

    /** 商品画像格納先URI */
    private String imageDirUri;

    /** 説明文字列 */
    private static final String EXPLAIN_NOTEXIST = "商品管理番号が存在しません";

    /** 説明文字列 */
    private static final String EXPLAIN_STATE_DEL = "商品が削除されています";

    /** 説明文字列 */
    private static final String EXPLAIN_FILENAME_ERROR = "画像ファイル名が不適切です";

    // 共通
    /** 詳細文字列 */
    private static final String DETAILE_EXTENSION_ERROR = "jpeg ファイルではありません。({0})";

    /** 詳細文字列 */
    private static final String DETAILE_GOODSGROUPCODE_ERROR = "フォルダ名の商品管理番号とファイル名の商品管理番号が不一致です。({0})";

    /** 詳細文字列 */
    private static final String DETAILE_FILENAME_SPLIT_ERROR = "画像ファイルのファイル命名規則と不一致です。({0})";

    // 商品グループ画像
    /** 詳細文字列 */
    private static final String DETAILE_VERSIONNO_DETAILEIMAGE_RANGE_ERROR =
                    "商品グループ画像表示順は0埋め2桁で「1～{0}」の範囲で設定してください。({1})";

    /** 画像種別内連番 正規表現(数値二桁)*/
    private static final String MATCH_VERSION_NO = "^[0-9]{2}$";

    /** 処理種別文字列 */
    private static final String MODE_UPDATE = "更新";

    /** 処理種別文字列 */
    private static final String MODE_INSERT = "追加";

    /** 処理種別文字列 */
    private static final String MODE_DELETE = "削除";

    /** 結果CSV　商品画像処理CSVファイル名 */
    private String csvFileNameProcSuccess;

    /** 結果CSV　商品画像処理エラーCSVファイル名 */
    private String csvFileNameProcError;

    /** 結果CSV　商品画像処理CSVファイル名の接中辞 */
    private static final String CSV_FILENAME_PROC_SUCCESS_INFIX = "goodsImageProc_";

    /** 結果CSV　商品画像処理エラーCSVファイル名の接中辞 */
    private static final String CSV_FILENAME_PROC_ERROR_INFIX = "goodsImageProcError_";

    /** 結果CSV　拡張子 */
    private static final String FILE_NAME_EXTENSION_CSV = ".csv";

    /** 画像ファイルセパレーター */
    private static final String FILE_NAME_SEPARATOR = "_";

    /** 画像ファイル拡張子(.jpg) */
    private static final String FILE_NAME_EXTENSION_JPG = ".jpg";

    /** 画像ファイル拡張子(.jpeg) */
    private static final String FILE_NAME_EXTENSION_JPEG = ".jpeg";

    /** 画像ファイル拡張子(.jpe) */
    private static final String FILE_NAME_EXTENSION_JPE = ".jpe";

    /** 画像ファイル拡張子(.jfif) */
    private static final String FILE_NAME_EXTENSION_JFIF = ".jfif";

    /** 画像ファイル拡張子(.jfi) */
    private static final String FILE_NAME_EXTENSION_JFI = ".jfi";

    /** 画像ファイル拡張子(.jif) */
    private static final String FILE_NAME_EXTENSION_JIF = ".jif";

    /** 画像ファイル拡張子リスト */
    private static final List<String> EXTENSION_LIST;

    static {
        EXTENSION_LIST = List.of(FILE_NAME_EXTENSION_JPG, FILE_NAME_EXTENSION_JPEG, FILE_NAME_EXTENSION_JPE,
                                 FILE_NAME_EXTENSION_JFIF, FILE_NAME_EXTENSION_JFI, FILE_NAME_EXTENSION_JIF
                                );
    }

    /** ファイルマジックナンバー */
    private static final byte[] MAGIC_NUMBER1 = {(byte) 0xFF, (byte) 0xD8};

    /** 改行(キャリッジリターン) */
    private static final String LINE_FEED_CR = "\r\n";

    /** 区切り文字(スラッシュ) */
    private static final String DELIMITER_SLASH = "/";

    /************
     * Dao
     ************/
    /** 商品グループDao */
    private final GoodsGroupDao goodsGroupDao;

    /** 商品グループ画像Dao(ext) */
    private final GoodsGroupImageDao goodsGroupImageDao;

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /**
     * コンストラクタ
     *
     */
    @Autowired
    public ImageUpdateBatchProcessor(NotificationSubApi notificationSubApi,
                                     HeaderParamsUtility headerParamsUtil,
                                     AsyncService asyncService) {
        this.notificationSubApi = notificationSubApi;
        this.headerParamsUtil = headerParamsUtil;
        this.asyncService = asyncService;
        // falseでバッチ起動時にBatchTaskテーブルに対応するレコードを作成する
        this.goodsGroupDao = ApplicationContextUtility.getBean(GoodsGroupDao.class);
        this.goodsGroupImageDao = ApplicationContextUtility.getBean(GoodsGroupImageDao.class);
    }

    /**
     * Consumer
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {

        // 管理者SEQにパラメーターを設定する
        headerParamsUtil.setAdministratorSeq(
                        notificationSubApi.getApiClient(), String.valueOf(batchQueueMessage.getAdministratorSeq()));

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_GOODSIMAGE_UPDATE");
        batchLogging.setBatchName("商品画像更新");
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("商品画像更新開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        // 初期設定
        String msg = init();
        if (msg.length() != 0) {
            LOGGER.info("設定ファイルエラー" + LINE_FEED_CR + msg);
            reportString.append(msg).append("\n");
            batchLogging.setReport(reportString);
            batchLogging.setProcessCount(null);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            // メール送信
            GoodsImageUpdateErrorRequest goodsImageUpdateErrorRequest = getGoodsImageUpdateErrorRequest("設定エラー", msg);

            Object[] args = new Object[] {goodsImageUpdateErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {GoodsImageUpdateErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "goodsImageUpdateError", args, argsClass);

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());
        }

        try {
            // 商品画像の削除処理を行います。
            deleteGoodsGroupImage();

            // 登録更新処理を行います。
            registUpdate();

            // 結果csvを生成します。
            outputResultCsv();

            // 商品画像ファイルを移動します。
            moveDirImageFile();

            // レポート内容に表示する内容を設定
            reportString = new StringBuilder(createReportMsg());

            batchLogging.setReport(reportString);
            batchLogging.setProcessCount(cntGoodsGroupImageRegist + cntGoodsGroupImageDelete + cntGoodsGroupImageUpdate
                                         + cntGoodsGroupImageNotExist + cntGoodsGroupImageFileNameNotMatch
                                         + cntGoodsGroupImageOpenStateDel);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

            // メール送信
            GoodsImageUpdateRequest goodsImageUpdateRequest = getGoodsImageUpdateRequest();

            Object[] args = new Object[] {goodsImageUpdateRequest};
            Class<?>[] argsClass = new Class<?>[] {GoodsImageUpdateRequest.class};
            asyncService.asyncService(notificationSubApi, "goodsImageUpdate", args, argsClass);

        } catch (Exception error) {

            // メール送信
            GoodsImageUpdateErrorRequest goodsImageUpdateErrorRequest =
                            getGoodsImageUpdateErrorRequest(error.getClass().getName(), null);

            Object[] args = new Object[] {goodsImageUpdateErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {GoodsImageUpdateErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "goodsImageUpdateError", args, argsClass);

            batchLogging.setReport(reportString);
            batchLogging.setProcessCount(null);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

            throw error;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("商品画像更新終了");
        }
    }

    /**
     * Get GoodsImage Update Request<br />
     *
     * @return GoodsImageUpdateRequest
     */
    public GoodsImageUpdateRequest getGoodsImageUpdateRequest() {

        GoodsImageUpdateRequest goodsImageUpdateRequest = new GoodsImageUpdateRequest();
        GoodsImageUpdateResultData goodsImageUpdateResultData = new GoodsImageUpdateResultData();

        goodsImageUpdateResultData.setImageDir(imageDir);
        goodsImageUpdateResultData.setCntGoodsGroupSumMapSize(cntGoodsGroupSumMap.size());
        goodsImageUpdateResultData.setCntGoodsGroupNotExistMapSize(cntGoodsGroupNotExistMap.size());
        goodsImageUpdateResultData.setCntGoodsGroupFileNameNotMatchMapSize(cntGoodsGroupFileNameNotMatchMap.size());
        goodsImageUpdateResultData.setCntGoodsGroupOpenStateDelMapSize(cntGoodsGroupOpenStateDelMap.size());
        goodsImageUpdateResultData.setCntGoodsGroupRegistMapSize(cntGoodsGroupRegistMap.size());
        goodsImageUpdateResultData.setCntGoodsGroupDeleteMapSize(cntGoodsGroupDeleteMap.size());
        goodsImageUpdateResultData.setCntGoodsGroupUpdateMapSize(cntGoodsGroupUpdateMap.size());
        goodsImageUpdateResultData.setCntGoodsGroupImageSum(cntGoodsGroupImageSum);
        goodsImageUpdateResultData.setCntGoodsGroupImageNotExist(cntGoodsGroupImageNotExist);
        goodsImageUpdateResultData.setCntGoodsGroupImageFileNameNotMatch(cntGoodsGroupImageFileNameNotMatch);
        goodsImageUpdateResultData.setCntGoodsGroupImageOpenStateDel(cntGoodsGroupImageOpenStateDel);
        goodsImageUpdateResultData.setCntGoodsGroupImageRegist(cntGoodsGroupImageRegist);
        goodsImageUpdateResultData.setCntGoodsGroupImageDelete(cntGoodsGroupImageDelete);
        goodsImageUpdateResultData.setCntGoodsGroupImageUpdate(cntGoodsGroupImageUpdate);

        goodsImageUpdateRequest.setResultData(goodsImageUpdateResultData);

        GoodsImageUpdateFile goodsImageUpdateFileSuccess = new GoodsImageUpdateFile();
        goodsImageUpdateFileSuccess.setFileName(csvFileNameProcSuccess);
        goodsImageUpdateFileSuccess.setFilePath(workDir + DELIMITER_SLASH + csvFileNameProcSuccess);

        GoodsImageUpdateFile goodsImageUpdateFileError = new GoodsImageUpdateFile();
        goodsImageUpdateFileError.setFileName(csvFileNameProcError);
        goodsImageUpdateFileError.setFilePath(workDir + DELIMITER_SLASH + csvFileNameProcError);

        List<GoodsImageUpdateFile> goodsImageUpdateFiles = new ArrayList<>();
        goodsImageUpdateFiles.add(goodsImageUpdateFileSuccess);
        goodsImageUpdateFiles.add(goodsImageUpdateFileError);

        goodsImageUpdateRequest.setFileList(goodsImageUpdateFiles);

        return goodsImageUpdateRequest;
    }

    /**
     * Get GoodsImageUpdate Error Request<br />
     *
     * @param exceptionName
     * @param msg
     * @return GoodsImageUpdateErrorRequest
     */
    public GoodsImageUpdateErrorRequest getGoodsImageUpdateErrorRequest(String exceptionName, String msg) {

        GoodsImageUpdateErrorRequest goodsImageUpdateErrorRequest = new GoodsImageUpdateErrorRequest();
        goodsImageUpdateErrorRequest.setExceptionName(exceptionName);
        goodsImageUpdateErrorRequest.setMsg(msg);

        return goodsImageUpdateErrorRequest;
    }

    /**
     * 初期設定<br />
     * @return 設定結果
     */
    protected String init() {

        StringBuilder sb = new StringBuilder();

        // 集計用リストの初期化
        procSuccessCsvDtoList = new ArrayList<>();
        procErrorCsvDtoList = new ArrayList<>();

        // 集計用件数の初期化(商品管理番号毎)
        cntGoodsGroupSumMap = new HashMap<>();
        cntGoodsGroupNotExistMap = new HashMap<>();
        cntGoodsGroupFileNameNotMatchMap = new HashMap<>();
        cntGoodsGroupOpenStateDelMap = new HashMap<>();
        cntGoodsGroupUpdateMap = new HashMap<>();
        cntGoodsGroupRegistMap = new HashMap<>();
        cntGoodsGroupDeleteMap = new HashMap<>();

        // 集計用件数の初期化(画像毎)
        // 商品グループ画像
        cntGoodsGroupImageSum = 0;
        cntGoodsGroupImageNotExist = 0;
        cntGoodsGroupImageFileNameNotMatch = 0;
        cntGoodsGroupImageOpenStateDel = 0;
        cntGoodsGroupImageRegist = 0;
        cntGoodsGroupImageUpdate = 0;
        cntGoodsGroupImageDelete = 0;

        // 画像コピーエラーリスト
        toImageDirCopyErrorMsgList = new ArrayList<>();
        toImageDirDeleteErrorMsgList = new ArrayList<>();
        toErrorDirCopyErrorMsgList = new ArrayList<>();

        // 商品画像格納先URIの取得
        imageDirUri = PropertiesUtil.getSystemPropertiesValue("goodsimage.directory.uri");

        // 出力日時
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        outputTime = dateUtility.getCurrentTime();
        String datetime = new SimpleDateFormat(DateUtility.YMD + DateUtility.HMS_NON_COLON).format(outputTime);

        // ディレクトリ設定を読込み
        imageDir = PropertiesUtil.getSystemPropertiesValue("real.images.path.goods");
        inputDir = PropertiesUtil.getSystemPropertiesValue("goodsimage.input.directory");
        errorDir = PropertiesUtil.getSystemPropertiesValue("goodsimage.error.directory");
        workDir = PropertiesUtil.getSystemPropertiesValue("goodsimage.csv.work.directory");
        backupDir = PropertiesUtil.getSystemPropertiesValue("goodsimage.csv.backup.directory");

        if (!new File(imageDir).exists()) {
            sb.append("[").append(imageDir).append("]が存在しません。").append(LINE_FEED_CR);
        }
        if (!new File(inputDir).exists()) {
            sb.append("[").append(inputDir).append("]が存在しません。").append(LINE_FEED_CR);
        }
        if (!new File(errorDir).exists()) {
            sb.append("[").append(errorDir).append("]が存在しません。").append(LINE_FEED_CR);
        }
        if (!new File(workDir).exists()) {
            sb.append("[").append(workDir).append("]が存在しません。").append(LINE_FEED_CR);
        }
        if (!new File(backupDir).exists()) {
            sb.append("[").append(backupDir).append("]が存在しません。").append(LINE_FEED_CR);
        }
        errorDir = errorDir + DELIMITER_SLASH + datetime;

        // 各CSVに付与するプレフィックス
        String csvPrefix = PropertiesUtil.getSystemPropertiesValue("shopid") + FILE_NAME_SEPARATOR;
        csvFileNameProcSuccess = csvPrefix + CSV_FILENAME_PROC_SUCCESS_INFIX;
        csvFileNameProcError = csvPrefix + CSV_FILENAME_PROC_ERROR_INFIX;

        // 最大商品グループ画像数
        String cnt = PropertiesUtil.getSystemPropertiesValue("goodsimage.max.count");
        if (StringUtils.isEmpty(cnt)) {
            sb.append("[goodsgroupimage.max.count]が設定されていません。").append(LINE_FEED_CR);
        }
        goodsGroupImageMaxCnt = Integer.parseInt(cnt);

        return sb.toString();
    }

    /**
     * 商品グループ画像削除処理<br />
     * 処理対象となる画像を取得します。<br />
     * 取得した画像に対して、処理方法を決定します。<br />
     *
     * ・商品グループ画像テーブルに画像が登録されている場合<br />
     *  商品グループ画像テーブルの画像を削除します。<br />
     *  「⑤商品画像 削除」で集計します。<br />
     *
     */
    protected void deleteGoodsGroupImage() {
        LOGGER.info("▼商品グループ画像削除処理開始");
        int offset = 0;
        // メモリを考慮し、limit件数毎に処理を行う。
        for (; ; ) {
            // 全商品の商品グループ画像を取得します。
            List<GoodsGroupImageOpenstatusDto> list =
                            goodsGroupImageDao.getGoodsGroupImageOpenstatusList(offset, DELETE_TARGET_LIMIT);
            if (list == null || list.size() == 0 || list.isEmpty()) {
                break;
            }
            // 取得した画像分処理を行います。
            for (GoodsGroupImageOpenstatusDto imageDto : list) {
                File imageFile = new File(imageDir + DELIMITER_SLASH + imageDto.getImageFileName());
                if (imageFile.exists()) {
                    // g_images配下にファイルが存在している場合。処理なし
                    continue;
                }
                // 削除処理（物理削除）
                if (deleteGoodsGroupImageExecute(imageDto)) {
                    // 商品グループの公開状態が、削除以外の場合。「⑤商品画像 削除」で集計します。
                    addCntDelete(imageDto.getGoodsGroupCode(), imageFile, null);
                }
            }
            offset = offset + DELETE_TARGET_LIMIT;
        }
        LOGGER.info("▲商品グループ画像削除処理終了");
    }

    /**
     * 商品グループ画像・商品規格画像登録処理<br />
     * 処理対象となる画像ファイルを取得します。<br />
     * 取得した画像に対して、処理方法を決定します。<br />
     *
     * ・商品グループが取得できない場合<br />
     *  「①商品管理番号が存在しない」で集計を行います。<br />
     *
     * ・商品グループの公開状態が削除の場合<br />
     *  「②商品が削除されている」で集計します。<br />
     *
     * ・商品グループ画像テーブルもしくは商品規格画像テーブルに既に画像が登録されている場合<br />
     *  「③商品画像 更新」で集計します。<br />
     *
     * ・商品グループ画像テーブルもしくは商品規格画像テーブルに画像が登録されていない場合<br />
     *  「④商品画像 登録」で集計します。<br />
     *
     * @throws Exception exception
     */
    protected void registUpdate() throws Exception {
        LOGGER.info("▼登録更新処理開始");

        // 画像登録ディレクトリから、処理対象となる商品管理番号ディレクトリを取得します。
        File[] goodsGroupDirs = getInputGoodsGroupCodeDir();

        // 商品管理番号ディレクトリが存在しない場合。
        if (goodsGroupDirs == null || goodsGroupDirs.length == 0) {
            return;
        }

        // 取得した商品管理番号分処理を行います。
        for (File goodsGroupDir : goodsGroupDirs) {

            // 商品管理番号ディレクトリから、処理対象となる画像ファイルを取得します。
            File[] imageFiles = getInputImageFile(goodsGroupDir);
            if (imageFiles == null || imageFiles.length == 0) {
                // 画像ファイルが存在しない場合。
                continue;
            }
            // 商品グループを取得します。
            GoodsGroupEntity goodsGroupEntity = getGoodsGroupEntity(goodsGroupDir.getName());
            List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();
            if (goodsGroupEntity != null) {
                // 商品グループ画像情報リストを取得します。
                goodsGroupImageEntityList = goodsGroupImageDao.getGoodsGroupImageListByGoodsGroupSeq(
                                goodsGroupEntity.getGoodsGroupSeq());
            }

            // list of fileName without extension
            List<String> fileNameList = new ArrayList<>();
            // 取得した画像ファイル分処理を行います。
            for (File imageFile : imageFiles) {

                // 商品グループが取得できない場合。「①商品管理番号が存在しない」で集計を行います。
                if (goodsGroupEntity == null) {
                    addCntNotExist(goodsGroupDir.getName(), imageFile);
                    continue;
                }

                // If same files with different extension present then skip the
                // processing.
                String fileNameWithoutExt = imageFile.getName().split("\\.")[0];
                if (fileNameList.contains(fileNameWithoutExt)) {
                    String fileName = goodsGroupDir.getName().concat(DELIMITER_SLASH).concat(imageFile.getName());
                    moveErrorDir(goodsGroupEntity.getGoodsGroupCode(), fileName);
                    continue;
                }

                fileNameList.add(fileNameWithoutExt);

                // 商品グループの公開状態が、削除の場合。「②商品が削除されている」で集計します。
                if (HTypeOpenDeleteStatus.DELETED.equals(goodsGroupEntity.getGoodsOpenStatusPC())) {
                    String fileName = goodsGroupDir.getName().concat(DELIMITER_SLASH).concat(imageFile.getName());
                    addCntOpenStateDel(goodsGroupEntity.getGoodsGroupCode(), fileName, null);
                    continue;
                }

                // 商品グループ画像登録更新処理
                registUpdateGoodsGroupImage(goodsGroupEntity, goodsGroupDir, imageFile, goodsGroupImageEntityList);
            }
        }
        LOGGER.info("▲登録更新処理終了");
    }

    /**
     * 処理結果CSVを出力します。<br />
     *　・商品画像処理CSV<br />
     *　・商品画像処理エラーCSV<br />
     *
     */
    protected void outputResultCsv() {
        LOGGER.info("▼処理結果CSV出力処理開始");

        String datetime = new SimpleDateFormat(DateUtility.YMD + DateUtility.HMS_NON_COLON).format(outputTime);

        csvFileNameProcSuccess = csvFileNameProcSuccess + datetime + FILE_NAME_EXTENSION_CSV;
        csvFileNameProcError = csvFileNameProcError + datetime + FILE_NAME_EXTENSION_CSV;

        // 商品管理番号/規格コード/画像ファイル名の昇順でソート
        procErrorCsvDtoList.sort(Comparator.comparing(GoodsImageProcErrorCsvDto::getGoodsGroupCode));

        outputCsvExecute(GoodsImageProcSuccessCsvDto.class, procSuccessCsvDtoList, csvFileNameProcSuccess);
        LOGGER.info("結果CSV 商品画像処理CSVを出力しました");
        outputCsvExecute(GoodsImageProcErrorCsvDto.class, procErrorCsvDtoList, csvFileNameProcError);
        LOGGER.info("結果CSV 商品画像処理エラーCSVを出力しました");

        LOGGER.info("▲処理結果CSV出力処理終了");
    }

    /**
     * 画像追加ディレクトリの画像ファイルをg_imagesに移動します。<bre />
     *
     * @throws Exception 例外
     */
    protected void moveDirImageFile() throws Exception {

        // 登録できなかった画像をerrorに移動します。
        LOGGER.info("▼error移動処理開始");
        for (GoodsImageProcErrorCsvDto errorDto : procErrorCsvDtoList) {

            File file = new File(inputDir + DELIMITER_SLASH + errorDto.getGoodsGroupCode());

            try {
                if (file.exists()) {
                    // 追加用ディレクトリの画像ファイルをerrorディレクトリに移動します。
                    if (moveErrorDir(errorDto.getGoodsGroupCode(), errorDto.getImageFileName())) {
                        LOGGER.info("error移動処理成功 " + file.getParent() + DELIMITER_SLASH + errorDto.getImageFileName());
                    } else {
                        LOGGER.info("error移動処理失敗 " + file.getParent() + DELIMITER_SLASH + errorDto.getImageFileName());
                        toErrorDirCopyErrorMsgList.add(
                                        inputDir + DELIMITER_SLASH + errorDto.getGoodsGroupCode() + DELIMITER_SLASH
                                        + errorDto.getImageFileName());
                    }
                }
            } catch (Exception e) {
                // ログ出力し、次の画像の処理を進める。
                LOGGER.error("error移動処理中に例外が発生しました。[" + file.getAbsolutePath() + "]", e);
                toErrorDirCopyErrorMsgList.add(
                                inputDir + DELIMITER_SLASH + errorDto.getGoodsGroupCode() + DELIMITER_SLASH
                                + errorDto.getImageFileName());
            }
        }
        LOGGER.info("▲error移動処理終了");
        // 登録完了した画像を、g_imagesにファイルをコピーします。
        // 全て正常にコピーできた場合、追加用ディレクトリから削除します。
        LOGGER.info("▼正常移動処理開始");
        for (GoodsImageProcSuccessCsvDto regDto : procSuccessCsvDtoList) {
            if (MODE_DELETE.equals(regDto.getMode())) {
                // 処理区分：削除の場合は、ファイルの移動処理は不要
                continue;
            }

            File file = new File(regDto.getImageUrl()
                                       .replaceAll(imageDirUri + DELIMITER_SLASH, inputDir + DELIMITER_SLASH));

            // 削除フラグ
            boolean inputDelFlg = true;

            if (file.exists() && file.isFile()) {

                // g_imagesに移動します。
                String gImageGoodsGroupPath = imageDir + DELIMITER_SLASH + regDto.getGoodsGroupCode() + DELIMITER_SLASH;
                try {
                    // 移動用ディレクトリ作成
                    if (!new File(gImageGoodsGroupPath).exists()) {
                        new File(gImageGoodsGroupPath).mkdirs();
                    }

                    // 追加用ディレクトリの画像ファイルをg_imagesに移動します。
                    // 拡張子違いのファイルを削除
                    cleanUpFile(gImageGoodsGroupPath + file.getName());
                    if (copyFile(file.getAbsolutePath(), gImageGoodsGroupPath + file.getName(), false)) {
                        LOGGER.debug("g_images移動処理成功 " + gImageGoodsGroupPath + file.getName());
                    } else {
                        LOGGER.info("g_images移動処理失敗 " + gImageGoodsGroupPath + file.getName());
                        toImageDirCopyErrorMsgList.add(
                                        inputDir + DELIMITER_SLASH + regDto.getGoodsGroupCode() + DELIMITER_SLASH
                                        + file.getName());
                        inputDelFlg = false;
                    }

                } catch (Exception e) {
                    // ログ出力し、次の画像の処理を進める。
                    LOGGER.error("g_images移動処理中に例外が発生しました。[" + gImageGoodsGroupPath + file.getName() + "]", e);
                    toImageDirCopyErrorMsgList.add(
                                    inputDir + DELIMITER_SLASH + regDto.getGoodsGroupCode() + DELIMITER_SLASH
                                    + file.getName());
                    inputDelFlg = false;
                }
            }

            if (inputDelFlg) {
                // 画像追加用ディレクトリの削除を行います。
                FileOperationUtility fileOperationUtility =
                                ApplicationContextUtility.getBean(FileOperationUtility.class);
                try {
                    // ファイルの削除
                    String filePath = inputDir + DELIMITER_SLASH + regDto.getGoodsGroupCode() + DELIMITER_SLASH
                                      + file.getName();
                    fileOperationUtility.remove(filePath);
                } catch (IOException e) {
                    LOGGER.info("ファイルの削除に失敗しました。（" + inputDir + DELIMITER_SLASH + regDto.getGoodsGroupCode()
                                + DELIMITER_SLASH + "）");
                    toImageDirDeleteErrorMsgList.add(
                                    inputDir + DELIMITER_SLASH + regDto.getGoodsGroupCode() + DELIMITER_SLASH
                                    + file.getName());
                }
                String[] fileList = file.getParentFile().list();
                int fileListLength = 0;
                if (fileList != null) {
                    fileListLength = fileList.length;
                }

                if (fileListLength == 0) {
                    try {
                        // 最後の画像ファイルだった場合、親ディレクトリも削除
                        fileOperationUtility.removeDir(
                                        inputDir + DELIMITER_SLASH + regDto.getGoodsGroupCode() + DELIMITER_SLASH);
                    } catch (IOException e) {
                        LOGGER.info("ディレクトリの削除に失敗しました。（" + inputDir + DELIMITER_SLASH + regDto.getGoodsGroupCode()
                                    + DELIMITER_SLASH + "）");
                    }
                }

            }
        }
        LOGGER.info("▲正常移動処理終了");
    }

    /**
     * 商品グループ画像削除処理を行います。<br />
     *
     * @param imageDto 商品グループ画像Dto
     * @return true:成功　false:失敗
     */
    protected boolean deleteGoodsGroupImageExecute(GoodsGroupImageOpenstatusDto imageDto) {

        GoodsGroupImageEntity goodsGroupImageEntity = ApplicationContextUtility.getBean(GoodsGroupImageEntity.class);
        goodsGroupImageEntity.setGoodsGroupSeq(imageDto.getGoodsGroupSeq());
        goodsGroupImageEntity.setImageTypeVersionNo(imageDto.getImageTypeVersionNo());

        int cnt = goodsGroupImageDao.delete(goodsGroupImageEntity);

        if (cnt == 0) {
            return false;
        }
        return true;
    }

    /**
     * 集計「⑤商品画像 削除」<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param imageFile 画像ファイル
     * @param groupImage 商品グループ画像エンティティ
     */
    protected void addCntDelete(String goodsGroupCode, File imageFile, GoodsGroupImageEntity groupImage) {
        LOGGER.debug("⑤商品画像 削除 - " + goodsGroupCode + " - " + imageFile.getName());

        // 画像URLリストに追加します。
        addProcSuccessCsvDtoList(goodsGroupCode, imageFile, groupImage, MODE_DELETE);

        // cnt追加します。
        cntGoodsGroupDeleteMap.put(goodsGroupCode, goodsGroupCode);
        cntGoodsGroupSumMap.put(goodsGroupCode, goodsGroupCode);

        cntGoodsGroupImageDelete++;
        cntGoodsGroupImageSum++;
    }

    /**
     * レポートに出力する文言を生成します。<br />
     *
     * @return 出力メッセージ
     */
    protected String createReportMsg() {
        StringBuilder sb = new StringBuilder();

        // 成功件数算出
        int goodsGroupImageSuccessCnt = cntGoodsGroupImageRegist + cntGoodsGroupImageDelete + cntGoodsGroupImageUpdate;
        // 失敗件数算出
        int goodsGroupImageErrorCnt = cntGoodsGroupImageNotExist + cntGoodsGroupImageFileNameNotMatch
                                      + cntGoodsGroupImageOpenStateDel;

        // レポート内容作成
        sb.append("-----").append(LINE_FEED_CR);
        sb.append("■総処理件数").append(LINE_FEED_CR);
        sb.append("商品管理番号：").append(cntGoodsGroupSumMap.size()).append(" 件").append(LINE_FEED_CR);
        sb.append("グループ画像：").append(cntGoodsGroupImageSum).append(" 件").append(LINE_FEED_CR).append(LINE_FEED_CR);
        sb.append("■成功件数").append(LINE_FEED_CR);
        sb.append("グループ画像：").append(goodsGroupImageSuccessCnt).append(" 件").append(LINE_FEED_CR).append(LINE_FEED_CR);
        sb.append("■失敗件数").append(LINE_FEED_CR);
        sb.append("グループ画像：").append(goodsGroupImageErrorCnt).append(" 件").append(LINE_FEED_CR);
        sb.append("-----").append(LINE_FEED_CR);

        // 処理件数0件の場合、メール送信しないので出力しない
        int count = cntGoodsGroupSumMap.size() + cntGoodsGroupImageSum;
        if (count != 0) {
            sb.append("詳細はメールにてご確認ください。").append(LINE_FEED_CR);
        }

        if (toImageDirCopyErrorMsgList.size() != 0 || toErrorDirCopyErrorMsgList.size() != 0) {
            sb.append("※移動処理に失敗した画像があります。").append(LINE_FEED_CR);
        }

        if (toImageDirDeleteErrorMsgList.size() != 0) {
            sb.append("※削除処理に失敗した画像があります。").append(LINE_FEED_CR);
        }

        return sb.toString();
    }

    /**
     * 商品画像処理CSVリストにレコードを追加します。<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param imageFile 画像ファイル
     * @param groupImage 商品グループ画像エンティティ
     * @param mode 処理種別
     */
    protected void addProcSuccessCsvDtoList(String goodsGroupCode,
                                            File imageFile,
                                            GoodsGroupImageEntity groupImage,
                                            String mode) {
        GoodsImageProcSuccessCsvDto csvDto = ApplicationContextUtility.getBean(GoodsImageProcSuccessCsvDto.class);
        csvDto.setGoodsGroupCode(goodsGroupCode);
        csvDto.setImageUrl(imageDirUri + DELIMITER_SLASH + goodsGroupCode + DELIMITER_SLASH + imageFile.getName());
        csvDto.setMode(mode);
        csvDto.setGoodsGroupImageEntity(groupImage);
        procSuccessCsvDtoList.add(csvDto);
    }

    /**
     * 追加用ディレクトリを参照し、商品管番号ディレクトリを返します。<br />
     * 「error」「tmp」は、特殊ディレクトリとする為、排除します。<br />
     *
     * @return 商品管理番号ディレクトリの配列
     */
    protected File[] getInputGoodsGroupCodeDir() {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.equals("error") || name.equals("tmp")) {
                    return false;
                }
                return true;
            }
        };
        File[] items = new File(inputDir).listFiles(filter);
        return items;
    }

    /**
     * 引数の商品管理番号ディレクトリを参照し、商品画像ファイルを返します。<br />
     *
     * @param goodsGroupDir 商品管理番号ディレクトリ
     * @return 画像ファイルの配列
     * @throws Exception exception
     */
    protected File[] getInputImageFile(File goodsGroupDir) throws Exception {
        // フィルタ作成
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.equals("Thumbs.db")) {
                    try {
                        FileOperationUtility fileOperationUtility =
                                        ApplicationContextUtility.getBean(FileOperationUtility.class);
                        fileOperationUtility.remove(
                                        inputDir + DELIMITER_SLASH + dir.getName() + DELIMITER_SLASH + "Thumbs.db");
                    } catch (Exception e) {
                        LOGGER.info("ファイル削除に失敗しました。（" + inputDir + DELIMITER_SLASH + dir.getName() + DELIMITER_SLASH
                                    + "Thumbs.db" + "）");
                        LOGGER.error("ERROR : getInputImageFile", e);
                    }
                    return false;
                } else {
                    return true;
                }
            }
        };
        File[] items = goodsGroupDir.listFiles(filter);
        return items;
    }

    /**
     * 引数の商品管理番号の、商品グループエンティティを返します。<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @return 商品グループエンティティ
     */
    protected GoodsGroupEntity getGoodsGroupEntity(String goodsGroupCode) {
        List<String> goodsGroupCodeList = new ArrayList<>();
        goodsGroupCodeList.add(goodsGroupCode);
        List<GoodsGroupEntity> goodsGroupEntityList = goodsGroupDao.getGoodsGroupByCodeList(1001, goodsGroupCodeList);
        if (goodsGroupEntityList == null || goodsGroupEntityList.isEmpty()) {
            return null;
        }
        return goodsGroupEntityList.get(0);
    }

    /**
     * 集計「①商品管理番号が存在しない」<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param imageFile 画像ファイル
     */
    protected void addCntNotExist(String goodsGroupCode, File imageFile) {
        LOGGER.debug("①商品管理番号が存在しない - " + goodsGroupCode + " - " + imageFile.getName());

        // ファイル名を作成する
        String imageFileName = goodsGroupCode.concat(DELIMITER_SLASH).concat(imageFile.getName());
        // 削除リストに追加します。
        addProcErrorCsvDtoList(goodsGroupCode, EXPLAIN_NOTEXIST, imageFileName, null);

        // cnt追加します。
        cntGoodsGroupNotExistMap.put(goodsGroupCode, goodsGroupCode);
        cntGoodsGroupSumMap.put(goodsGroupCode, goodsGroupCode);

        cntGoodsGroupImageNotExist++;
        cntGoodsGroupImageSum++;
    }

    /**
     * 削除リストにレコードを追加します。<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param explain 説明
     * @param imageFileName 画像ファイル名
     * @param details 詳細
     */
    protected void addProcErrorCsvDtoList(String goodsGroupCode, String explain, String imageFileName, String details) {
        GoodsImageProcErrorCsvDto csvDto = ApplicationContextUtility.getBean(GoodsImageProcErrorCsvDto.class);

        // 商品管理番号、規格コード、画像ファイル名が一致している場合は追加処理は行わない
        for (GoodsImageProcErrorCsvDto errorCsvDto : procErrorCsvDtoList) {
            if (errorCsvDto.getGoodsGroupCode().equals(goodsGroupCode) && errorCsvDto.getImageFileName()
                                                                                     .equals(imageFileName)) {
                return;
            }
        }
        csvDto.setGoodsGroupCode(goodsGroupCode);
        csvDto.setExplain(explain);
        csvDto.setImageFileName(imageFileName);
        csvDto.setDetails(details);

        procErrorCsvDtoList.add(csvDto);
    }

    /**
     * 集計リストCSVを出力します。<br />
     *
     * @param dtoClass Dtoクラス
     * @param dtoList 集計リスト
     * @param fileName csvファイル名
     */
    protected void outputCsvExecute(Class<?> dtoClass, List<?> dtoList, String fileName) {

        // CSVファイルパス
        String workFilePath = workDir + DELIMITER_SLASH + fileName;

        // CSVダウンロードオプションを設定する
        CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
        csvDownloadOptionDto.setOutputHeader(true);

        // 出力ファイルの設定
        CsvExtractUtility csvExtractUtility =
                        new CsvExtractUtility(dtoClass, csvDownloadOptionDto, null, workFilePath, dtoList);

        try {
            csvExtractUtility.outCsv();
        } catch (IOException e) {
            LOGGER.error("集計リストCSVの出力に失敗しました。", e);
        }
    }

    /**
     * 指定ファイルをErrorディレクトリにコピーします。<br />
     * @param goodsGroupCode 商品管理番号
     * @param imageFileName 商品画像ファイル名
     * @return true:成功 false:失敗
     * @throws Exception コピーエラー
     */
    protected boolean moveErrorDir(String goodsGroupCode, String imageFileName) throws Exception {

        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        try {
            String errorPath = errorDir + DELIMITER_SLASH + goodsGroupCode + DELIMITER_SLASH;

            // 移動用ディレクトリ作成
            if (!new File(errorPath).exists()) {
                new File(errorPath).mkdirs();
            }

            fileOperationUtility.put(inputDir + DELIMITER_SLASH + imageFileName, errorPath, true);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return false;
        }
        File dir = new File(inputDir + DELIMITER_SLASH + goodsGroupCode);
        File[] fileList = dir.listFiles();
        int fileListLength = 0;
        if (fileList != null) {
            fileListLength = fileList.length;
        }
        if (dir.exists() && fileListLength == 0) {
            // 移動元ディレクトリが空になった場合、移動元ディレクトリを削除する
            try {
                fileOperationUtility.remove(dir);
            } catch (IOException e) {
                LOGGER.info("ディレクトリの削除に失敗しました。（" + dir + "）");
            }
        }

        return true;
    }

    /**
     * 指定ファイルをコピーします。<br />
     * @param fromPath コピー元
     * @param toPath コピー先
     * @param removeSrc true：コピー元削除 false:コピー先削除
     * @return true:成功 false:失敗
     * @throws Exception コピーエラー
     */
    protected boolean copyFile(String fromPath, String toPath, boolean removeSrc) throws Exception {
        try {
            FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
            fileOperationUtility.put(fromPath, toPath, removeSrc);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return false;
        }
        return true;
    }

    /**
     * 集計「②商品が削除されている」<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param fileName 画像ファイル名
     * @param details 詳細
     */
    protected void addCntOpenStateDel(String goodsGroupCode, String fileName, String details) {
        LOGGER.debug("②商品が削除されている - " + goodsGroupCode + " - " + fileName);

        // 削除リストに追加します。
        addProcErrorCsvDtoList(goodsGroupCode, EXPLAIN_STATE_DEL, fileName, details);

        // cnt追加します。
        cntGoodsGroupOpenStateDelMap.put(goodsGroupCode, goodsGroupCode);
        cntGoodsGroupSumMap.put(goodsGroupCode, goodsGroupCode);

        cntGoodsGroupImageOpenStateDel++;
        cntGoodsGroupImageSum++;
    }

    /**
     * 集計「④商品画像 追加」<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param imageFile 画像ファイル
     * @param groupImage 商品グループ画像エンティティ
     */
    protected void addCntRegist(String goodsGroupCode, File imageFile, GoodsGroupImageEntity groupImage) {
        LOGGER.debug("④商品画像 追加 - " + goodsGroupCode + " - " + imageFile.getName());

        // 画像URLリストに追加します。
        addProcSuccessCsvDtoList(goodsGroupCode, imageFile, groupImage, MODE_INSERT);

        // cnt追加します。
        cntGoodsGroupRegistMap.put(goodsGroupCode, goodsGroupCode);
        cntGoodsGroupSumMap.put(goodsGroupCode, goodsGroupCode);

        cntGoodsGroupImageRegist++;
        cntGoodsGroupImageSum++;
    }

    /**
     * 集計「画像ファイル名が不適切です」<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param fileName 画像ファイル名
     * @param details 詳細
     */
    protected void addFileNameNotMatch(String goodsGroupCode, String fileName, String details) {
        LOGGER.info("画像ファイル名が不適切です - " + goodsGroupCode + " - " + fileName);

        // 削除リストに追加します。
        addProcErrorCsvDtoList(goodsGroupCode, EXPLAIN_FILENAME_ERROR, fileName, details);

        // cnt追加します。
        cntGoodsGroupSumMap.put(goodsGroupCode, goodsGroupCode);
        cntGoodsGroupFileNameNotMatchMap.put(goodsGroupCode, goodsGroupCode);

        cntGoodsGroupImageSum++;
        cntGoodsGroupImageFileNameNotMatch++;
    }

    /**
     * 商品グループ画像の登録更新処理を行う
     * @param goodsGroupEntity 商品グループエンティティ
     * @param goodsGroupDir 商品管理ディレクトリ
     * @param imageFile 画像ファイル
     * @param goodsGroupImageList 商品グループ画像リスト
     */
    protected void registUpdateGoodsGroupImage(GoodsGroupEntity goodsGroupEntity,
                                               File goodsGroupDir,
                                               File imageFile,
                                               List<GoodsGroupImageEntity> goodsGroupImageList) {
        // ファイル名を作成する
        String imageFileName = goodsGroupDir.getName().concat(DELIMITER_SLASH).concat(imageFile.getName());

        // ファイル名の妥当性チェックを行い、登録用に商品グループ画像エンティティを作成する
        GoodsGroupImageEntity registGoodsGroupImage =
                        createGoodsGroupImageEntity(goodsGroupEntity, imageFile, goodsGroupDir, goodsGroupImageList);
        if (registGoodsGroupImage == null) {
            // ファイル名が不適切の為、次のファイルを処理する
            return;
        }

        boolean isFileExistFlag = false;
        for (GoodsGroupImageEntity entity : goodsGroupImageList) {
            if (isSameFileName(imageFileName, entity.getImageFileName())) {
                isFileExistFlag = true;
                // 商品グループ画像が取得できた場合。「③商品画像 更新」で集計し、DBを更新します。
                addCntUpdate(goodsGroupEntity.getGoodsGroupCode(), imageFile, registGoodsGroupImage);
                goodsGroupImageDao.update(registGoodsGroupImage);
                break;
            }
        }

        if (!isFileExistFlag) {
            // 商品グループ画像が取得できなかった場合。「④商品画像 登録」で集計します。
            goodsGroupImageDao.insert(registGoodsGroupImage);
            addCntRegist(goodsGroupEntity.getGoodsGroupCode(), imageFile, registGoodsGroupImage);
        }

    }

    /**
     * 商品グループ画像ファイル名の妥当性チェックを行い、登録用に商品規格画像エンティティを作成する<br />
     *
     * @param goodsGroupEntity 商品グループエンティティ
     * @param imageFile 画像ファイル
     * @param goodsGroupDir 商品管理ディレクトリ
     * @param goodsGroupImageOpenstatusDtoList 商品グループ画像情報リスト
     * @return 商品グループ画像エンティティ ファイル名が不正な場合はNULLを返却
     */
    protected GoodsGroupImageEntity createGoodsGroupImageEntity(GoodsGroupEntity goodsGroupEntity,
                                                                File imageFile,
                                                                File goodsGroupDir,
                                                                List<GoodsGroupImageEntity> goodsGroupImageOpenstatusDtoList) {

        GoodsGroupImageEntity goodsGroupImageEntity = ApplicationContextUtility.getBean(GoodsGroupImageEntity.class);

        // 画像ファイルから登録する値を生成します。
        String filename = imageFile.getName();
        String[] filenameArray = filename.split(FILE_NAME_SEPARATOR);

        // ファイル名を作成する
        String imageFileName = goodsGroupDir.getName().concat(DELIMITER_SLASH).concat(filename);

        // ファイル命名規則チェック
        if (filenameArray.length != 2) {
            addFileNameNotMatch(goodsGroupEntity.getGoodsGroupCode(), imageFileName,
                                MessageFormat.format(DETAILE_FILENAME_SPLIT_ERROR, filename)
                               );
            return null;
        }

        // jpegファイルかのチェック
        if (!confirm(imageFile)) {
            addFileNameNotMatch(goodsGroupEntity.getGoodsGroupCode(), imageFileName,
                                MessageFormat.format(DETAILE_EXTENSION_ERROR, getExtension(filename))
                               );
            return null;
        }

        // 画像種別内連番チェック
        // 画像種別内連番の桁数チェック

        String versionString = null;
        try {
            versionString = filenameArray[1].substring(0, filenameArray[1].lastIndexOf("."));
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            addFileNameNotMatch(goodsGroupEntity.getGoodsGroupCode(), imageFileName,
                                MessageFormat.format(DETAILE_VERSIONNO_DETAILEIMAGE_RANGE_ERROR, goodsGroupImageMaxCnt,
                                                     versionString
                                                    )
                               );
            return null;
        }

        if (!versionString.matches(MATCH_VERSION_NO)) {
            addFileNameNotMatch(goodsGroupEntity.getGoodsGroupCode(), imageFileName,
                                MessageFormat.format(DETAILE_VERSIONNO_DETAILEIMAGE_RANGE_ERROR, goodsGroupImageMaxCnt,
                                                     versionString
                                                    )
                               );
            return null;
        }
        // 画像種別内連番の範囲チェック
        int versionNo = Integer.parseInt(versionString);
        // 商品グループ画像の詳細画像の場合、１～最大値
        if (versionNo < 1 || versionNo > goodsGroupImageMaxCnt) {
            addFileNameNotMatch(goodsGroupEntity.getGoodsGroupCode(), imageFileName,
                                MessageFormat.format(DETAILE_VERSIONNO_DETAILEIMAGE_RANGE_ERROR, goodsGroupImageMaxCnt,
                                                     versionString
                                                    )
                               );
            return null;
        }

        // 商品管理番号のチェック
        if (!goodsGroupDir.getName().equals(filenameArray[0])) {
            addFileNameNotMatch(goodsGroupEntity.getGoodsGroupCode(), imageFileName,
                                MessageFormat.format(DETAILE_GOODSGROUPCODE_ERROR, filenameArray[0])
                               );
            return null;
        }

        // エンティティに値を設定する
        goodsGroupImageEntity.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
        goodsGroupImageEntity.setImageTypeVersionNo(versionNo);
        goodsGroupImageEntity.setImageFileName(imageFileName);
        goodsGroupImageEntity.setRegistTime(outputTime);
        goodsGroupImageEntity.setUpdateTime(outputTime);
        return goodsGroupImageEntity;
    }

    /**
     *
     * ファイル名が同一視できるか確認する<br/>
     * jpgとjpegの違いを吸収する<br/>
     *
     * @param src1 ファイル名1
     * @param src2 ファイル名2
     * @return true 同一視可能 / false 同一視不可能
     */
    protected boolean isSameFileName(String src1, String src2) {
        if (StringUtils.isEmpty(src1)) {
            return false;
        }
        if (StringUtils.isEmpty(src2)) {
            return false;
        }
        if (src1.equals(src2)) {
            return true;
        }
        for (String ext : EXTENSION_LIST) {
            // 拡張子をjpegに寄せてチェックする
            if (src1.toLowerCase()
                    .replaceAll(ext + "$", FILE_NAME_EXTENSION_JPEG)
                    .equals(src2.toLowerCase().replaceAll(ext + "$", FILE_NAME_EXTENSION_JPEG))) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * ファイルパスの拡張子違いのファイルを削除する<br/>
     * ※ファイルが存在しない場合の例外は握りつぶす
     * ※拡張子が小文字の場合のみ対応
     * ※拡張子がJpEgなど大文字小文字が混在している場合には対応していない
     *
     * @param path 対象ファイルパス
     */
    protected void cleanUpFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        // パスの拡張子を取得
        int i = path.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = path.substring(i);
        }
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
        for (String ext : EXTENSION_LIST) {
            String targetPath = path.replace(extension, ext);
            try {
                fileOperationUtility.remove(targetPath);
            } catch (IOException ie) {
                LOGGER.error("例外処理が発生しました", ie);
                // 握りつぶす
            }
        }
    }

    /**
     * 集計「③商品画像 更新」<br />
     *
     * @param goodsGroupCode 商品管理番号
     * @param imageFile 画像ファイル
     * @param groupImage 商品グループ画像エンティティ
     */
    protected void addCntUpdate(String goodsGroupCode, File imageFile, GoodsGroupImageEntity groupImage) {
        LOGGER.debug("③商品画像 更新 - " + goodsGroupCode + " - " + imageFile.getName());

        // 画像URLリストに追加します。
        addProcSuccessCsvDtoList(goodsGroupCode, imageFile, groupImage, MODE_UPDATE);

        // cnt追加します。
        cntGoodsGroupUpdateMap.put(goodsGroupCode, goodsGroupCode);
        cntGoodsGroupSumMap.put(goodsGroupCode, goodsGroupCode);

        cntGoodsGroupImageUpdate++;
        cntGoodsGroupImageSum++;
    }

    /**
     * 画像ファイルから拡張子を取得する<br />
     *
     * @param filename 画像ファイル
     * @return 拡張子
     */
    protected String getExtension(String filename) {
        String extension = "";
        int indexPeriod = filename.lastIndexOf(".");
        if (indexPeriod != -1) {
            extension = filename.substring(indexPeriod);
        }
        return extension;
    }

    /**
     * jpeg ファイルかを検証する
     *
     * @param file ファイル
     * @return 検証結果
     */
    protected boolean confirm(File file) {

        try {
            // マジックナンバー検証
            if (!confirmMagicNumber(file, MAGIC_NUMBER1)) {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return false;
        }

        // 拡張子検証
        if (confirmExtension(file)) {
            return true;
        }

        return false;
    }

    /**
     * 拡張子の検証
     *
     * @param file ファイル
     * @return 検証結果
     */
    protected boolean confirmExtension(File file) {
        for (String extension : EXTENSION_LIST) {
            if (file.getName().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * マジックナンバーの検証
     *
     * @param file          ファイル
     * @param magicNumber   あるべきマジックナンバー
     * @return 検証結果
     * @throws Exception    発生した例外
     */
    protected boolean confirmMagicNumber(File file, final byte[] magicNumber) throws Exception {

        // マジックナンバーすら確認できない場合
        if (file.length() < magicNumber.length) {
            return false;
        }

        InputStream inStream = null;

        try {
            inStream = new FileInputStream(file);
            byte[] fileMagicNumber = new byte[magicNumber.length];
            inStream.read(fileMagicNumber);

            final List<String> comp = DiffUtil.diff(magicNumber, fileMagicNumber);
            return comp.isEmpty();

        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }

    }
}