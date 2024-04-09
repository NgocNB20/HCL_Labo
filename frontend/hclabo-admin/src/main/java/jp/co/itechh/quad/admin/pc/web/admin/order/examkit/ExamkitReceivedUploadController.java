package jp.co.itechh.quad.admin.pc.web.admin.order.examkit;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.admin.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.admin.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.admin.dto.order.ExamKitCsvDto;
import jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload.GoodsBundledUploadModel;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitReceivedEntryBatchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
 * 検査キット受領アップロード画面コントロール
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Controller
@PreAuthorize("hasAnyAuthority('ORDER:8')")
@RequestMapping("/order/examkitReceivedUpload")
@SessionAttributes(value = "examkitReceivedUploadModel")
public class ExamkitReceivedUploadController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamkitReceivedUploadController.class);

    /** 検査API */
    private final ExaminationApi examinationApi;

    /** コンストラクタ */
    @Autowired
    public ExamkitReceivedUploadController(ExaminationApi examinationApi) {
        this.examinationApi = examinationApi;
    }

    /**
     * 検査キット受領データアップロード画面表示
     *
     * @param examkitReceivedUploadModel
     * @return
     */
    @GetMapping(value = "/")
    public String doLoadUpload(ExamkitReceivedUploadModel examkitReceivedUploadModel) {
        return "order/examkitReceivedUpload/index";
    }

    /**
     * 検査キット受領ファイルアップロード処理
     *
     * @param examkitReceivedUploadModel
     * @param model
     * @return 遷移先ページ
     */
    @PostMapping(value = "/", params = "doOnceFileUpload")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/order/examkitReceivedUpload/complete/")
    public String doOnceFileUpload(@Validated ExamkitReceivedUploadModel examkitReceivedUploadModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        if (error.hasErrors()) {
            return "order/examkitReceivedUpload/index";
        }

        MultipartFile uploaded = examkitReceivedUploadModel.getRegistUploadFile();

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

        // アップロード検査キット受領登録データファイルをテンプファイルとして保存
        String filePath = csvUtility.getBatchTargetFileName("EXAM_KIT_RECEIVED_ENTRY");
        String putFileStr =
            putFile(examkitReceivedUploadModel.getRegistUploadFile(), filePath, redirectAttributes, model);

        if (StringUtils.isNotEmpty(putFileStr)) {
            return putFileStr;
        }

        // 非同期バッチをキックする
        doStartBatch(filePath, examkitReceivedUploadModel, error, redirectAttributes, model);

        return "redirect:/order/examkitReceivedUpload/complete/";
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
     * 検査キット受領登録データアップロード終了画面表示
     *
     * @return 検査キット受領登録データアップロード終了画面
     */
    @GetMapping(value = "/complete/")
    public String doLoadComplete() {
        return "order/examkitReceivedUpload/complete";
    }

    /**
     * ファイル移動処理<br/>
     *
     * @param src 移動元ファイル
     * @param dst 移動先ファイル名
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
            return "redirect:/order/examkitReceivedUpload";
        }
        return null;
    }

    private void doStartBatch(String filePath,
                              ExamkitReceivedUploadModel examkitReceivedUploadModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // ファイル操作Helper取得
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        // --------------------------------------------------------------------
        // アップロードされた検査キット受領登録CSVの事前バリデーション
        // --------------------------------------------------------------------
        CsvUploadResult csvUploadResult = validateReceiveTestKitCsvDto(new File(filePath));

        CsvValidationResult result = csvUploadResult.getCsvValidationResult();
        List<ExamkitReceivedUploadModelItem> resultItems = new ArrayList<>();
        examkitReceivedUploadModel.setResultItems(resultItems);

        if (result.isValid() && csvUploadResult.getRecordCount() > 0) {
            // 画面に件数セット（ヘッダー分マイナス）
            examkitReceivedUploadModel.setRegistCount(csvUploadResult.getRecordCount() - 1);

            // --------------------------------------------------------------------
            // 非同期バッチをキックする
            // --------------------------------------------------------------------
            try {
                ExamKitReceivedEntryBatchRequest request = new ExamKitReceivedEntryBatchRequest();
                request.setStartType(HTypeBatchStartType.MANUAL.getValue());
                request.setFilePath(filePath);
                BatchExecuteResponse batchExecuteResponse = examinationApi.examKitReceivedEntryBatch(request);
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

        } else {
            // バリデータエラーありの場合
            examkitReceivedUploadModel.setRegistCount(0);
            for (InvalidDetail detail : result.getInvalidDetailList()) {
                ExamkitReceivedUploadModelItem item =
                    ApplicationContextUtility.getBean(ExamkitReceivedUploadModelItem.class);
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
                this.throwMessage(ExamkitReceivedUploadModel.MSGCD_FAIL_DELETE);
            }
        }
    }

    private CsvUploadResult validateReceiveTestKitCsvDto(File csvFile) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();
        CsvReaderModule csvReaderModule = ApplicationContextUtility.getBean(CsvReaderModule.class);

        /* Csvファイルを読み込み */
        List<ExamKitCsvDto> examKitCsvDtoList = new ArrayList<>();
        try {
            // CSVの全行分の DTO を作成してみる
            examKitCsvDtoList =
                (List<ExamKitCsvDto>) csvReaderModule.readCsv(csvFile, ExamKitCsvDto.class, csvUploadResult,
                    csvReaderOptionDto
                );
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            // CSV読み込みで有り得ない例外が発生した場合
            CsvValidationResult csvValidationResult = new CsvValidationResult();
            csvReaderModule.createUnexpectedExceptionMsg(csvValidationResult);
            csvUploadResult.setCsvValidationResult(csvValidationResult);
            return csvUploadResult;
        }

        return csvUploadResult;
    }

}