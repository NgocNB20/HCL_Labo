/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.admin.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsCsvDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ZipUploadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload.validation.GoodsBundleUploadValidator;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductsImagesZipUploadRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductsImagesZipUploadResponse;
import jp.co.itechh.quad.registasynchronous.presentation.api.RegistAsynchronousApi;
import jp.co.itechh.quad.registasynchronous.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.registasynchronous.presentation.api.param.RegistAsynchronousExecuteRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理 商品一括アップロード<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/goods/bundledupload")
@Controller
@SessionAttributes(value = "goodsBundledUploadModel")
@PreAuthorize("hasAnyAuthority('GOODS:8')")
public class GoodsBundledUploadController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsBundledUploadController.class);

    /**
     * Helper<br/>
     */
    private final GoodsBundledUploadHelper goodsBundledUploadIndexHelper;

    /**
     * 商品登録非同期API
     */
    private final RegistAsynchronousApi registAsynchronousApi;

    /**
     * 商品Api
     */
    private final ProductApi productApi;

    /**
     * 商品一括アップロード画面の動的バリデータ
     */
    private final GoodsBundleUploadValidator goodsBundleUploadValidator;

    /**
     * コンストラクタ
     *
     * @param registAsynchronousApi         商品登録非同期API
     * @param productApi                    商品Api
     * @param goodsBundledUploadIndexHelper Helper
     * @param goodsBundleUploadValidator    商品一括アップロード画面の動的バリデータ
     */
    @Autowired
    public GoodsBundledUploadController(RegistAsynchronousApi registAsynchronousApi,
                                        ProductApi productApi,
                                        GoodsBundledUploadHelper goodsBundledUploadIndexHelper,
                                        GoodsBundleUploadValidator goodsBundleUploadValidator) {
        this.registAsynchronousApi = registAsynchronousApi;
        this.productApi = productApi;
        this.goodsBundledUploadIndexHelper = goodsBundledUploadIndexHelper;
        this.goodsBundleUploadValidator = goodsBundleUploadValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder error) {
        // 商品一括アップロード画面の動的バリデータをセット
        error.addValidators(goodsBundleUploadValidator);
    }

    /**
     * 商品一括アップロード画面表示(CSV)
     *
     * @param goodsBundledUploadModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @GetMapping(value = "/csv/")
    protected String doLoadCsv(GoodsBundledUploadModel goodsBundledUploadModel,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // モデル初期化
        clearModel(GoodsBundledUploadModel.class, goodsBundledUploadModel, model);

        // アップロードモードのデフォルト値を設定
        goodsBundledUploadModel.setUploadType("0");

        return "goods/bundledupload/csvUpload";

    }

    /**
     * 商品一括アップロード画面表示(画像)
     *
     * @param goodsBundledUploadModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @GetMapping(value = "/image/")
    protected String doLoadImage(GoodsBundledUploadModel goodsBundledUploadModel,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        // モデル初期化
        clearModel(GoodsBundledUploadModel.class, goodsBundledUploadModel, model);

        // アップロード対象のデフォルト値を設定
        goodsBundledUploadModel.setZipImageTarget("0");

        return "goods/bundledupload/imageUpload";

    }

    /**
     * 商品一括アップロード処理実行<br/>
     *
     * @param goodsBundledUploadModel
     * @return 一括アップロード結果画面／自画面
     */
    @PostMapping(value = "/csv/", params = "doOnceUpload")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/goods/bundledupload/csvUploadComplete/")
    public String doOnceUpload(@Validated(UploadGroup.class) GoodsBundledUploadModel goodsBundledUploadModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (error.hasErrors()) {
            return "goods/bundledupload/csvUpload";
        }

        // フラグ初期化を行う
        initFlag(goodsBundledUploadModel);

        // CsvUtility取得
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);

        // アップロード商品データファイルをテンプファイルとして保存
        String tmpFileName = csvUtility.getBatchTargetFileName("GOODS_RESULT");
        String putFileStr = putFile(goodsBundledUploadModel.getUploadCsvFile(), tmpFileName, redirectAttributes, model);

        if (StringUtils.isNotEmpty(putFileStr)) {
            return putFileStr;
        }

        RegistAsynchronousExecuteRequest registAsynchronousExecuteRequest = new RegistAsynchronousExecuteRequest();
        registAsynchronousExecuteRequest.setFilePath(tmpFileName);
        registAsynchronousExecuteRequest.setUploadType(goodsBundledUploadModel.getUploadType());
        registAsynchronousExecuteRequest.setStartType(HTypeBatchStartType.MANUAL.getValue());

        // 非同期バッチをキックする
        doStartBatch(tmpFileName, goodsBundledUploadModel, error, registAsynchronousExecuteRequest, redirectAttributes,
                     model
                    );

        if (error.hasErrors()) {
            return "goods/bundledupload/csvUpload";
        }

        // 正常終了時
        return "redirect:/goods/bundledupload/csvUploadComplete/";

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
            return "redirect:/goods/bundledupload/csv/";
        }
        return null;
    }

    /**
     * 商品一括アップロードタスク登録処理
     *
     * @param tmpFileName
     * @param goodsBundledUploadModel
     * @param registAsynchronousExecuteRequest
     * @param redirectAttributes
     * @param model
     */
    private void doStartBatch(String tmpFileName,
                              GoodsBundledUploadModel goodsBundledUploadModel,
                              BindingResult error,
                              RegistAsynchronousExecuteRequest registAsynchronousExecuteRequest,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // --------------------------------------------------------------------
        // アップロードされた商品CSVの事前バリデーション
        // --------------------------------------------------------------------
        CsvUploadResult csvUploadResult = this.validateGoodsCsv(new File(tmpFileName));

        if (csvUploadResult.isInValid()) {
            goodsBundledUploadIndexHelper.toPageForCsvUploadResultDto(goodsBundledUploadModel, csvUploadResult);

            // ファイル操作Helper取得
            FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
            try {
                // NGファイルは削除する
                fileOperationUtility.remove(tmpFileName);
                // バッチ起動をせずにそのまま終了させる
                return;
            } catch (IOException e) {
                LOGGER.error("例外処理が発生しました", e);
                // ファイルの削除に失敗した場合
                this.throwMessage(GoodsBundledUploadModel.MSGCD_FAIL_DELETE);
            }
        } else {
            goodsBundledUploadIndexHelper.toPageForCsvUploadResultDto(goodsBundledUploadModel, null);
        }

        // --------------------------------------------------------------------
        // 非同期バッチをキックする
        // --------------------------------------------------------------------
        try {
            BatchExecuteResponse batchExecuteResponse = registAsynchronousApi.execute(registAsynchronousExecuteRequest);
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
     * 商品部分アップロード処理実行<br/>
     *
     * @param goodsBundledUploadModel
     * @return 一括アップロード結果画面／自画面
     */
    @PostMapping(value = "/csv/", params = "doOncePartialUpload")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/goods/bundledupload/csvUploadComplete/")
    public String doOncePartialUpload(@Validated(UploadGroup.class) GoodsBundledUploadModel goodsBundledUploadModel,
                                      BindingResult error,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (error.hasErrors()) {
            return "goods/bundledupload/csvUpload";
        }

        // ヘッダ行のバリデートを回避するだけのため、doイベントはそのまま呼ぶ。
        return doOnceUpload(goodsBundledUploadModel, error, redirectAttributes, model);
    }

    /**
     * 戻り遷移(CSV)<br/>
     *
     * @return 遷移元画面
     */
    @PostMapping(value = "/csv/", params = "doReturn")
    public String doCsvReturn() {
        return "redirect:/goods/";
    }

    /**
     * 戻り遷移(Image)<br/>
     *
     * @return 遷移元画面
     */
    @PostMapping(value = "/image/", params = "doReturn")
    public String doImageReturn() {
        return "redirect:/goods/";
    }

    /**
     * Zipファイルのアップロード処理を実行します
     *
     * @param goodsBundledUploadModel
     * @param error
     * @return String
     */
    @PostMapping(value = "/image/", params = "doOnceZipUpload")
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/bundledupload/imageUpload")
    public String doOnceZipUpload(@Validated(ZipUploadGroup.class) GoodsBundledUploadModel goodsBundledUploadModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) throws IOException {

        if (error.hasErrors()) {
            return "goods/bundledupload/imageUpload";
        }

        // CsvUtility取得
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);

        String zipImageTarget = goodsBundledUploadModel.getZipImageTarget();

        String tmpFileName =
                        csvUtility.getBatchTargetImageName(zipImageTarget) + goodsBundledUploadModel.getZipImageFile()
                                                                                                    .getOriginalFilename();

        // ファイル操作Helper取得
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
        fileOperationUtility.put(goodsBundledUploadModel.getZipImageFile(), tmpFileName);

        ProductsImagesZipUploadRequest productsImagesZipUploadRequest = new ProductsImagesZipUploadRequest();
        productsImagesZipUploadRequest.setZipImageTarget(zipImageTarget);
        productsImagesZipUploadRequest.setZipImageUrl(tmpFileName);

        ProductsImagesZipUploadResponse productsImagesZipUploadResponse = new ProductsImagesZipUploadResponse();
        try {
            productsImagesZipUploadResponse = productApi.registImageZip(productsImagesZipUploadRequest);
            if (!"1".equals(zipImageTarget)) {
                if (productsImagesZipUploadResponse.getExecuteCode() == null
                    || productsImagesZipUploadResponse.getExecuteCode().equals(HTypeBatchResult.FAILED.getValue())) {
                    // 異常の場合
                    addMessage("AEB000101", new Object[] {"バッチの起動"}, redirectAttributes, model);
                } else {
                    // 正常の場合
                    addMessage("AEB000102", new Object[] {"バッチの起動"}, redirectAttributes, model);
                }
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage("AEB000101", new Object[] {"バッチの起動"}, redirectAttributes, model);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("zipImageUrl", "originalFilename");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "goods/bundledupload/imageUpload";
        }

        int fileListSize = productsImagesZipUploadResponse.getFileListSize();

        goodsBundledUploadModel.setUploadFileCount(fileListSize);

        return "redirect:/goods/bundledupload/imageUploadComplete/";
    }

    /**
     * フラグ初期化<br/>
     */
    protected void initFlag(GoodsBundledUploadModel goodsBundledUploadModel) {
        goodsBundledUploadModel.setNormalEndMsg(false);
        goodsBundledUploadModel.setValidationEndMsg(false);
        goodsBundledUploadModel.setErrorEndMeg(false);
    }

    /**
     * アップロードされた商品CSVの事前バリデーション
     *
     * @param csvFile
     * @return CsvUploadResult
     */
    private CsvUploadResult validateGoodsCsv(File csvFile) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();
        CsvReaderModule csvReaderModule = ApplicationContextUtility.getBean(CsvReaderModule.class);

        /* Csvファイルを読み込み */
        List<GoodsCsvDto> goodsCsvDtoList;
        try {
            // CSVの全行分の DTO を作成してみる
            goodsCsvDtoList = (List<GoodsCsvDto>) csvReaderModule.readCsv(csvFile, GoodsCsvDto.class, csvUploadResult,
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

    /**
     * 結果画面ロード<br/>
     *
     * @return 自画面
     */
    @GetMapping(value = "/csvUploadComplete/")
    protected String doLoadComplete() {

        return "goods/bundledupload/csvUploadComplete";

    }

    /**
     * 商品画像zipアップロード完了画面表示
     *
     * @return
     */
    @GetMapping(value = "/imageUploadComplete/")
    protected String doLoadZip() {

        return "goods/bundledupload/imageUploadComplete";

    }

}