package jp.co.itechh.quad.admin.pc.web.admin.order.examresults;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.admin.constant.type.HTypeExamResultsUploadType;
import jp.co.itechh.quad.admin.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.admin.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.admin.dto.order.ExamResultsCsvDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload.GoodsBundledUploadModel;
import jp.co.itechh.quad.admin.pc.web.admin.order.examresults.validation.ExamResultsUploadValidator;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamResultsEntryBatchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 検査結果アップロード画面コントロール<br/>
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */

@Controller
@PreAuthorize("hasAnyAuthority('ORDER:8')")
@RequestMapping("/order/examResultsUpload")
@SessionAttributes(value = "examResultsUploadModel")
public class ExamResultsUploadController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsUploadController.class);

    /** 検査Api */
    private final ExaminationApi examinationApi;

    /** 検査結果一括アップロードの動的バリデータ */
    private final ExamResultsUploadValidator examResultsUploadValidator;

    /**
     * コンストラクター
     *
     * @param examinationApi             検査Api
     * @param examResultsUploadValidator 検査結果一括アップロードの動的バリデータ
     */
    @Autowired
    public ExamResultsUploadController(ExaminationApi examinationApi,
                                       ExamResultsUploadValidator examResultsUploadValidator) {
        this.examinationApi = examinationApi;
        this.examResultsUploadValidator = examResultsUploadValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder error) {
        // 検査結果一括アップロードの動的バリデータ
        error.addValidators(examResultsUploadValidator);
    }

    /**
     * 検査結果データアップロード画面表示
     *
     * @param examResultsUploadModel 検査結果アップロード画面モデル
     * @param model                  Model
     * @return 遷移先のビュー名
     */
    @GetMapping(value = "/")
    public String doLoadUpload(ExamResultsUploadModel examResultsUploadModel, Model model) {

        // モデル初期化
        clearModel(ExamResultsUploadModel.class, examResultsUploadModel, model);

        // アップロード対象のデフォルト値を設定
        examResultsUploadModel.setUploadTarget("0");

        return "order/examResultsUpload/index";
    }

    /**
     * 検査結果ファイルアップロード処理
     *
     * @param examResultsUploadModel 検査結果アップロード画面モデル
     * @param model                  Model
     * @return 遷移先ページ
     */
    @PostMapping(value = "/", params = "doOnceFileUpload")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/order/examResultsUpload/complete/")
    public String doOnceFileUpload(@Validated(UploadGroup.class) ExamResultsUploadModel examResultsUploadModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        if (error.hasErrors()) {
            return "order/examResultsUpload/index";
        }

        MultipartFile uploaded = examResultsUploadModel.getRegistUploadFile();

        String uploadTarget = examResultsUploadModel.getUploadTarget();

        // ----------------------------------------------------------------
        // HIT-MALL3では、uploadedファイルがnullであるかのチェックがあったが、
        // 新HIT-MALLの新規作成された @HVMultipartFile を付与することで、
        // フォームのバインディングの際に自動的にチェックを行うはず。
        // ----------------------------------------------------------------
        // ファイルがアップロードされていない場合
        if (uploaded == null) {
            return null;
        }

        // CsvUtility取得
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);

        String filePath = "";

        if (HTypeExamResultsUploadType.CSV.getValue().equals(uploadTarget)) {
            // アップロード検査結果データファイルをテンプファイルとして保存
            filePath = csvUtility.getBatchTargetFileName("EXAM_RESULTS_ENTRY");
        } else {
            filePath = csvUtility.getPdfTargetFileName(uploaded.getOriginalFilename());
        }

        String putFileStr =
            putFile(examResultsUploadModel.getRegistUploadFile(), filePath, redirectAttributes, model);

        if (StringUtils.isNotEmpty(putFileStr)) {
            return putFileStr;
        }

        // 非同期バッチをキックする
        doStartBatch(filePath, uploadTarget, examResultsUploadModel, error, redirectAttributes, model);

        return "redirect:/order/examResultsUpload/complete/";
    }

    /**
     * 戻る<br/>
     *
     * @return 受注検索画面
     */
    @PostMapping(value = "/", params = "doReturn")
    public String doReturn() {
        return "redirect:/order/";
    }

    /**
     * 検査結果データアップロード終了画面表示
     *
     * @return 検査結果データアップロード終了画面
     */
    @GetMapping(value = "/complete/")
    public String doLoadComplete() {
        return "order/examResultsUpload/complete";
    }

    /**
     * ファイル移動処理<br/>
     *
     * @param src                移動元ファイル
     * @param dst                移動先ファイル名
     * @param redirectAttributes redirectAttributes
     * @param model              model
     */
    protected String putFile(final MultipartFile src,
                             final String dst,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        // ファイル操作Utility取得
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
        try {
            fileOperationUtility.put(src, dst);
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(GoodsBundledUploadModel.MSGCD_FAIL_DELETE, redirectAttributes, model);
            return "redirect:/order/examResultsUpload";
        }
        return null;
    }

    /**
     * 検査結果SVアップロードタスク登録処理
     *
     * @param filePath               ファイルのパス
     * @param uploadTarget           アップロード対象
     * @param examResultsUploadModel 検査結果アップロード画面モデル
     * @param error                  エラー
     * @param redirectAttributes     redirectAttributes
     * @param model                  model
     */
    private void doStartBatch(String filePath,
                              String uploadTarget,
                              ExamResultsUploadModel examResultsUploadModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (HTypeExamResultsUploadType.PDF.getValue().equals(uploadTarget)) {
            // 画面に件数セット（1件固定）
            examResultsUploadModel.setRegistCount(1);
            executeBatch(filePath, uploadTarget, error, redirectAttributes, model);

        } else {

            // ファイル操作Helper取得
            FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

            // --------------------------------------------------------------------
            // アップロードされた検査結果CSVの事前バリデーション
            // --------------------------------------------------------------------
            CsvUploadResult csvUploadResult = validateExamResultsCsvDto(new File(filePath));

            CsvValidationResult result = csvUploadResult.getCsvValidationResult();
            List<ExamResultsUploadModelItem> resultItems = new ArrayList<>();
            examResultsUploadModel.setResultItems(resultItems);

            if (result.isValid() && csvUploadResult.getRecordCount() > 0) {
                // 画面に件数セット（ヘッダー分マイナス）
                examResultsUploadModel.setRegistCount(csvUploadResult.getRecordCount() - 1);

                executeBatch(filePath, uploadTarget, error, redirectAttributes, model);

            } else {
                // バリデータエラーありの場合
                examResultsUploadModel.setRegistCount(0);
                for (InvalidDetail detail : result.getInvalidDetailList()) {
                    ExamResultsUploadModelItem item =
                            ApplicationContextUtility.getBean(ExamResultsUploadModelItem.class);
                    item.setRowNumber(detail.getRow());
                    item.setColumnLabel(detail.getColumnLabel());
                    item.setErrContent(detail.getMessage());
                    resultItems.add(item);
                }
                try {
                    // NGファイルは削除する
                    fileOperationUtility.remove(filePath);
                } catch (IOException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // ファイルの削除に失敗した場合
                    this.throwMessage(ExamResultsUploadModel.MSGCD_FAIL_DELETE);
                }
            }
        }
    }

    /**
     * 検査結果登録バッチ処理
     *
     * @param filePath               ファイルのパス
     * @param uploadTarget           アップロード対象
     * @param error                  エラー
     * @param redirectAttributes     redirectAttributes
     * @param model                  model
     */
    private void executeBatch(String filePath,
                              String uploadTarget,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // --------------------------------------------------------------------
        // 非同期バッチをキックする
        // --------------------------------------------------------------------
        try {
            ExamResultsEntryBatchRequest request = new ExamResultsEntryBatchRequest();
            request.setStartType(HTypeBatchStartType.MANUAL.getValue());
            request.setUploadType(uploadTarget);
            request.setFilePath(filePath);
            BatchExecuteResponse batchExecuteResponse = examinationApi.examResultsEntryBatch(request);
            if (batchExecuteResponse.getExecuteCode() == null || batchExecuteResponse.getExecuteCode()
                    .equals(HTypeBatchResult.FAILED.getValue())) {
                // 異常の場合
                addMessage("AEB000101", new Object[] {"バッチの起動"}, redirectAttributes, model);
            } else {
                // 正常の場合
                addMessage("AEB000102", new Object[] {"バッチの起動"}, redirectAttributes, model);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage("AEB000101", new Object[] {"バッチの起動"}, redirectAttributes, model);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * アップロードされた検査結果アップロードCSVの事前バリデーション
     *
     * @param csvFile CSVファイル
     * @return CsvUploadResult Csvアップロード結果
     */
    private CsvUploadResult validateExamResultsCsvDto(File csvFile) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();
        CsvReaderModule csvReaderUtil = ApplicationContextUtility.getBean(CsvReaderModule.class);

        /* Csvファイルを読み込み */
        List<ExamResultsCsvDto> examResultsCsvDtoList = new ArrayList<>();
        try {
            // CSVの全行分の DTO を作成してみる
            examResultsCsvDtoList =
                (List<ExamResultsCsvDto>) csvReaderUtil.readCsv(csvFile, ExamResultsCsvDto.class, csvUploadResult,
                    csvReaderOptionDto
                );
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            // CSV読み込みで有り得ない例外が発生した場合
            CsvValidationResult csvValidationResult = new CsvValidationResult();
            csvReaderUtil.createUnexpectedExceptionMsg(csvValidationResult);
            csvUploadResult.setCsvValidationResult(csvValidationResult);
            return csvUploadResult;
        }

        return csvUploadResult;
    }
}
