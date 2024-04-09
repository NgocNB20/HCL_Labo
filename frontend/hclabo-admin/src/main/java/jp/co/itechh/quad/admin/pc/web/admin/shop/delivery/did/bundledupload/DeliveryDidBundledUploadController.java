package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.did.bundledupload;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryImpossibleDayCsvDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.ReceiverImpossibleDateApi;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesCsvUploadRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesCsvUploadResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 一括アップロード<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/delivery/did/bundledupload")
@Controller
@SessionAttributes(value = "deliveryDidBundledUploadModel")
@PreAuthorize("hasAnyAuthority('SETTING:8')")
public class DeliveryDidBundledUploadController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryDidBundledUploadController.class);

    /**
     * メッセージコード：不正操作
     */
    public static final String MSGCD_ILLEGAL_OPERATION = "AYD000601";

    /**
     * 一括アップロードHelper
     */
    private DeliveryDidBundledUploadHelper deliveryDidBundledUploadHelper;

    /**
     * 配送方法API
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * お届け不可日API
     */
    private final ReceiverImpossibleDateApi receiverImpossibleDateApi;

    /**
     * コンストラクタ
     *
     * @param deliveryDidBundledUploadHelper 一括アップロードHelper
     * @param shippingMethodApi              配送方法API
     * @param receiverImpossibleDateApi      お届け不可日API
     */
    @Autowired
    public DeliveryDidBundledUploadController(DeliveryDidBundledUploadHelper deliveryDidBundledUploadHelper,
                                              ShippingMethodApi shippingMethodApi,
                                              ReceiverImpossibleDateApi receiverImpossibleDateApi) {
        this.deliveryDidBundledUploadHelper = deliveryDidBundledUploadHelper;
        this.shippingMethodApi = shippingMethodApi;
        this.receiverImpossibleDateApi = receiverImpossibleDateApi;
    }

    /**
     * 画像表示処理<br/>
     * 初期表示用メソッド<br/>
     *
     * @param deliveryDidBundledUploadModel 一括アップロード
     * @param redirectAttributes
     * @param model
     * @return 自画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/did/bundledupload/upload")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> yearParam,
                                 @RequestParam(required = false) Optional<String> dmcdParam,
                                 DeliveryDidBundledUploadModel deliveryDidBundledUploadModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        boolean isNotChangeOfDmcdParam =
                        dmcdParam.isPresent() && Objects.equals(deliveryDidBundledUploadModel.getDmcd(),
                                                                Integer.parseInt(dmcdParam.get())
                                                               );
        boolean isNotChangeOfYearParam =
                        yearParam.isPresent() && Objects.equals(deliveryDidBundledUploadModel.getYear(),
                                                                Integer.parseInt(yearParam.get())
                                                               );

        if (isNotChangeOfDmcdParam && isNotChangeOfYearParam) {
            return "delivery/did/bundledupload/upload";
        }

        // モデルのクリア処理
        clearModel(DeliveryDidBundledUploadModel.class, deliveryDidBundledUploadModel, model);

        yearParam.ifPresent(item -> deliveryDidBundledUploadModel.setRedirectYear(Integer.parseInt(item)));
        dmcdParam.ifPresent(item -> deliveryDidBundledUploadModel.setRedirectDmcd(Integer.parseInt(item)));

        // 年、配送方法SEQが設定されている場合、引き継ぎ用の年にセット
        if (deliveryDidBundledUploadModel.getRedirectYear() != null
            && deliveryDidBundledUploadModel.getRedirectDmcd() != null) {
            deliveryDidBundledUploadModel.setYear(deliveryDidBundledUploadModel.getRedirectYear());
            deliveryDidBundledUploadModel.setDmcd(deliveryDidBundledUploadModel.getRedirectDmcd());
        }

        Integer year = deliveryDidBundledUploadModel.getYear();
        Integer dcmd = deliveryDidBundledUploadModel.getDmcd();

        // 年または配送方法SEQが存在しない場合、エラー画面へ遷移
        if (year == null || dcmd == null) {
            return "redirect:/error";
        }

        try {
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryDidBundledUploadModel.getDmcd());
            DeliveryMethodEntity resultEntity =
                            deliveryDidBundledUploadHelper.toDeliveryMethodEntity(shippingMethodResponse);

            // 不正操作チェック。配送マスタは物理削除されないので、結果がないのはURLパラメータをいじられた以外ありえない。
            if (resultEntity == null) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/";

            } else {
                deliveryDidBundledUploadHelper.convertToRegistPageForResult(
                                deliveryDidBundledUploadModel, resultEntity);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        return "delivery/did/bundledupload/upload";
    }

    /**
     * アップロード<br/>
     *
     * @param deliveryDidBundledUploadModel 一括アップロード
     * @return class
     */
    @PostMapping(value = "/", params = "doOnceUpload")
    @HEHandler(exception = AppLevelListException.class,
               returnView = "redirect:/delivery/did/bundledupload/uploadfinish/")
    public String doOnceUpload(@Validated DeliveryDidBundledUploadModel deliveryDidBundledUploadModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (error.hasErrors()) {
            return "delivery/did/bundledupload/upload";
        }

        // CSVHelper取得
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);

        Integer year = deliveryDidBundledUploadModel.getYear();
        Integer dcmd = deliveryDidBundledUploadModel.getDmcd();
        if (year == null || dcmd == null) {
            throwMessage();
        }

        // アップロードファイルをテンプファイルとして保存
        String tmpFileName = csvUtility.getUploadCsvTmpFileName("did");

        // ファイル操作Helper取得
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        try {
            fileOperationUtility.put(deliveryDidBundledUploadModel.getUploadFile(), tmpFileName);
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(DeliveryDidBundledUploadModel.MSGCD_FAIL_DELETE, redirectAttributes, model);
            return "redirect:/delivery/did/bundledupload/";
        }

        // --------------------------------------------------------------------
        // アップロードされたお届け不可日CSVの事前バリデーション
        // --------------------------------------------------------------------
        CsvUploadResult csvUploadResult = this.validateDeliveryImpossibleDayCsv(new File(tmpFileName));

        if (csvUploadResult.isInValid()) {
            try {
                // NGファイルは削除する
                fileOperationUtility.remove(tmpFileName);
            } catch (IOException e) {
                LOGGER.error("例外処理が発生しました", e);
                // ファイルの削除に失敗した場合
                this.throwMessage(DeliveryDidBundledUploadModel.MSGCD_FAIL_DELETE);
            }
            // 処理結果DxoでPageへ反映
            deliveryDidBundledUploadHelper.toPageForCsvUploadResultDto(deliveryDidBundledUploadModel, csvUploadResult);

            // 異常終了時
            return "redirect:/delivery/did/bundledupload/uploadfinish/";
        }

        ReceiverImpossibleDatesCsvUploadRequest receiverImpossibleDatesCsvUploadRequest =
                        new ReceiverImpossibleDatesCsvUploadRequest();
        receiverImpossibleDatesCsvUploadRequest.setCsvUploadValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);
        receiverImpossibleDatesCsvUploadRequest.setUpLoadFilePath(tmpFileName);
        receiverImpossibleDatesCsvUploadRequest.setYear(year);

        ReceiverImpossibleDatesCsvUploadResponse receiverImpossibleDatesCsvUploadResponse = null;

        try {
            receiverImpossibleDatesCsvUploadResponse =
                            receiverImpossibleDateApi.uploadCsv(dcmd, receiverImpossibleDatesCsvUploadRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            deliveryDidBundledUploadModel.setUploadCount(0);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "redirect:/delivery/did/bundledupload/uploadfinish/";
        }


        if (receiverImpossibleDatesCsvUploadResponse != null) {
            // アップロードサービス実行
             csvUploadResult =
                            deliveryDidBundledUploadHelper.toCsvUploadResult(receiverImpossibleDatesCsvUploadResponse);
            // 処理結果DxoでPageへ反映
            deliveryDidBundledUploadHelper.toPageForCsvUploadResultDto(deliveryDidBundledUploadModel, csvUploadResult);
        }

        // 正常終了時
        return "redirect:/delivery/did/bundledupload/uploadfinish/";
    }

    /**
     * 戻る
     *
     * @param deliveryDidBundledUploadModel 一括アップロード
     * @param redirectAttrs                 リダイレクトアトリビュート
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doReturn")
    public String doReturn(DeliveryDidBundledUploadModel deliveryDidBundledUploadModel,
                           RedirectAttributes redirectAttrs) {
        // アプリケーションが変わるため、選択年、配送方法SEQをリダイレクトスコープで引き渡す
        String yearDid = deliveryDidBundledUploadModel.getYear() != null ?
                        String.valueOf(deliveryDidBundledUploadModel.getYear()) :
                        "";
        String dmcdDid = deliveryDidBundledUploadModel.getDmcd() != null ?
                        String.valueOf(deliveryDidBundledUploadModel.getDmcd()) :
                        "";

        return "redirect:/delivery/did/?yearParam=" + yearDid + "&dmcdParam=" + dmcdDid + "&isUpload=" + true;
    }

    /**
     * 一括アップロード完了画面表示
     *
     * @param deliveryDidBundledUploadModel
     * @return
     */
    @GetMapping(value = "/uploadfinish/")
    protected String doLoadFinish(DeliveryDidBundledUploadModel deliveryDidBundledUploadModel) {

        // ブラウザバックの場合、処理しない
        if (deliveryDidBundledUploadModel.getYear() == null || deliveryDidBundledUploadModel.getDmcd() == null) {
            return "redirect:/delivery/did/bundledupload/";
        }

        return "delivery/did/bundledupload/uploadfinish";
    }

    /**
     * インデックス画面へ戻る<br/>
     *
     * @param deliveryDidBundledUploadModel 一括アップロード
     * @param redirectAttributes            リダイレクトアトリビュート
     * @return 自画面
     */
    @PostMapping(value = "/uploadfinish/", params = "doFinish")
    public String doFinish(DeliveryDidBundledUploadModel deliveryDidBundledUploadModel,
                           RedirectAttributes redirectAttributes) {
        // アプリケーションが変わるため、選択年、配送方法SEQをリダイレクトスコープで引き渡す
        String yearDid = deliveryDidBundledUploadModel.getYear() != null ?
                        String.valueOf(deliveryDidBundledUploadModel.getYear()) :
                        "";
        String dmcdDid = deliveryDidBundledUploadModel.getDmcd() != null ?
                        String.valueOf(deliveryDidBundledUploadModel.getDmcd()) :
                        "";

        return "redirect:/delivery/did/?yearParam=" + yearDid + "&dmcdParam=" + dmcdDid + "&isUpload=" + true;
    }

    /**
     * アップロードされたお届け不可日CSVの事前バリデーション
     *
     * @param csvFile
     * @return CsvUploadResult
     */
    private CsvUploadResult validateDeliveryImpossibleDayCsv(File csvFile) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();
        CsvReaderModule csvReaderModule = ApplicationContextUtility.getBean(CsvReaderModule.class);

        /* Csvファイルを読み込み */
        List<DeliveryImpossibleDayCsvDto> deliveryImpossibleDayCsvDtoList;
        try {
            // CSVの全行分の DTO を作成してみる
            deliveryImpossibleDayCsvDtoList = (List<DeliveryImpossibleDayCsvDto>) csvReaderModule.readCsv(csvFile, DeliveryImpossibleDayCsvDto.class, csvUploadResult,
                    csvReaderOptionDto);
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