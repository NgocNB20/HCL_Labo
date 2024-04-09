/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.admin.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.admin.dto.order.ShipmentCsvDto;
import jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload.GoodsBundledUploadModel;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionShipmentRegistBatchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 出荷アップロード画面コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/order/shipmentUpload")
@Controller
@SessionAttributes(value = "orderShipmentUploadModel")
@PreAuthorize("hasAnyAuthority('ORDER:8')")
public class OrderShipmentUploadController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderShipmentUploadController.class);

    /** 取引API */
    private final TransactionApi transactionApi;

    /** コンストラクタ */
    public OrderShipmentUploadController(TransactionApi transactionApi) {
        this.transactionApi = transactionApi;
    }

    /**
     * 出荷データアップロード画面表示
     *
     * @param orderShipmentUploadModel
     * @return
     */
    @GetMapping(value = "/")
    public String doLoadUpload(OrderShipmentUploadModel orderShipmentUploadModel) {
        return "order/shipmentUpload";
    }

    /**
     * 出荷ファイルアップロード処理
     *
     * @param orderShipmentUploadModel
     * @param model
     * @return 遷移先ページ
     */
    @PostMapping(value = "/", params = "doOnceFileUpload")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/order/shipmentUpload/complete/")
    public String doOnceFileUpload(@Validated OrderShipmentUploadModel orderShipmentUploadModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        if (error.hasErrors()) {
            return "order/shipmentUpload";
        }

        MultipartFile uploaded = orderShipmentUploadModel.getRegistUploadFile();

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

        // アップロード出荷データファイルをテンプファイルとして保存
        String filePath = csvUtility.getBatchTargetFileName("SHIPMENT_REGIST");
        String putFileStr =
                        putFile(orderShipmentUploadModel.getRegistUploadFile(), filePath, redirectAttributes, model);

        if (StringUtils.isNotEmpty(putFileStr)) {
            return putFileStr;
        }

        // 非同期バッチをキックする
        doStartBatch(filePath, orderShipmentUploadModel, error, redirectAttributes, model);

        return "redirect:/order/shipmentUpload/complete/";
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
     * 出荷データアップロード終了画面表示
     *
     * @return 出荷データアップロード終了画面
     */
    @GetMapping(value = "/complete/")
    public String doLoadComplete() {

        return "order/shipmentUploadComplete";
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
            return "redirect:/order/shipmentUpload";
        }
        return null;
    }

    /**
     * 出荷CSVアップロードタスク登録処理
     *
     * @param filePath
     * @param orderShipmentUploadModel
     * @param redirectAttributes
     * @param model
     */
    private void doStartBatch(String filePath,
                              OrderShipmentUploadModel orderShipmentUploadModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // ファイル操作Helper取得
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        // --------------------------------------------------------------------
        // アップロードされた出荷CSVの事前バリデーション
        // --------------------------------------------------------------------
        CsvUploadResult csvUploadResult = validateShipmentCsvDto(new File(filePath));
        CsvValidationResult result = csvUploadResult.getCsvValidationResult();
        List<OrderShipmentUploadModelItem> resultItems = new ArrayList<>();
        orderShipmentUploadModel.setResultItems(resultItems);

        if (result.isValid() && csvUploadResult.getRecordCount() > 0) {
            // 画面に件数セット（ヘッダー分マイナス）
            orderShipmentUploadModel.setRegistCount(csvUploadResult.getRecordCount() - 1);

            // --------------------------------------------------------------------
            // 非同期バッチをキックする
            // --------------------------------------------------------------------
            try {
                TransactionShipmentRegistBatchRequest request = new TransactionShipmentRegistBatchRequest();
                request.setStartType(HTypeBatchStartType.MANUAL.getValue());
                request.setFilePath(filePath);
                BatchExecuteResponse batchExecuteResponse = transactionApi.shipmentBatch(request);
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
            orderShipmentUploadModel.setRegistCount(0);
            for (InvalidDetail detail : result.getInvalidDetailList()) {
                OrderShipmentUploadModelItem item =
                                ApplicationContextUtility.getBean(OrderShipmentUploadModelItem.class);
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
                this.throwMessage(OrderShipmentUploadModel.MSGCD_FAIL_DELETE);
            }
        }
    }

    /**
     * アップロードされた出荷アップロードCSVの事前バリデーション
     *
     * @param csvFile
     * @return CsvUploadResult
     */
    private CsvUploadResult validateShipmentCsvDto(File csvFile) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();
        CsvReaderModule csvReaderModule = ApplicationContextUtility.getBean(CsvReaderModule.class);

        /* Csvファイルを読み込み */
        List<ShipmentCsvDto> shipmentCsvDtoList = new ArrayList<>();
        try {
            // CSVの全行分の DTO を作成してみる
            shipmentCsvDtoList =
                            (List<ShipmentCsvDto>) csvReaderModule.readCsv(csvFile, ShipmentCsvDto.class, csvUploadResult,
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