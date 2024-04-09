package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.holiday.bundledupload;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.HolidayCsvDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.holiday.presentation.api.HolidayApi;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayCsvUploadRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayCsvUploadResponse;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
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
@RequestMapping("/delivery/holiday/bundledupload")
@Controller
@SessionAttributes(value = "deliveryHolidayBundledUploadModel")
@PreAuthorize("hasAnyAuthority('SETTING:8')")
public class DeliveryHolidayBundledUploadController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryHolidayBundledUploadController.class);

    /**
     * メッセージコード：不正操作
     */
    public static final String MSGCD_ILLEGAL_OPERATION = "AYD000601";

    /**
     * 一括アップロードHelper
     */
    private final DeliveryHolidayBundledUploadHelper deliveryHolidayBundledUploadHelper;

    /**
     * 配送方法API
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 休日API
     */
    private final HolidayApi holidayApi;

    /**
     * コンストラクタ
     *
     * @param deliveryHolidayBundledUploadHelper 一括アップロードHelper
     * @param shippingMethodApi                  配送方法API
     * @param holidayApi                         休日API
     */
    @Autowired
    public DeliveryHolidayBundledUploadController(DeliveryHolidayBundledUploadHelper deliveryHolidayBundledUploadHelper,
                                                  HolidayApi holidayApi,
                                                  ShippingMethodApi shippingMethodApi) {
        this.deliveryHolidayBundledUploadHelper = deliveryHolidayBundledUploadHelper;
        this.shippingMethodApi = shippingMethodApi;
        this.holidayApi = holidayApi;
    }

    /**
     * 画像表示処理<br/>
     * 初期表示用メソッド<br/>
     *
     * @param deliveryHolidayBundledUploadModel
     * @param redirectAttributes
     * @param model
     * @return 自画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/holiday/bundledupload/upload")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> yearParam,
                                 @RequestParam(required = false) Optional<String> dmcdParam,
                                 DeliveryHolidayBundledUploadModel deliveryHolidayBundledUploadModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        boolean isNotChangeOfDmcdParam =
                        dmcdParam.isPresent() && Objects.equals(deliveryHolidayBundledUploadModel.getDmcd(),
                                                                Integer.parseInt(dmcdParam.get())
                                                               );
        boolean isNotChangeOfYearParam =
                        yearParam.isPresent() && Objects.equals(deliveryHolidayBundledUploadModel.getYear(),
                                                                Integer.parseInt(yearParam.get())
                                                               );

        if (isNotChangeOfDmcdParam && isNotChangeOfYearParam) {
            return "delivery/holiday/bundledupload/upload";
        }

        // モデルのクリア処理
        clearModel(DeliveryHolidayBundledUploadModel.class, deliveryHolidayBundledUploadModel, model);

        yearParam.ifPresent(item -> deliveryHolidayBundledUploadModel.setRedirectYear(Integer.parseInt(item)));
        dmcdParam.ifPresent(item -> deliveryHolidayBundledUploadModel.setRedirectDmcd(Integer.parseInt(item)));

        // 年、配送方法SEQが設定されている場合、引き継ぎ用の年にセット
        if (deliveryHolidayBundledUploadModel.getRedirectYear() != null
            && deliveryHolidayBundledUploadModel.getRedirectDmcd() != null) {
            deliveryHolidayBundledUploadModel.setYear(deliveryHolidayBundledUploadModel.getRedirectYear());
            deliveryHolidayBundledUploadModel.setDmcd(deliveryHolidayBundledUploadModel.getRedirectDmcd());
        }

        Integer year = deliveryHolidayBundledUploadModel.getYear();
        Integer dcmd = deliveryHolidayBundledUploadModel.getDmcd();

        // 年または配送方法SEQが存在しない場合、エラー画面へ遷移
        if (year == null || dcmd == null) {
            return "redirect:/error";
        }

        try {
            ShippingMethodResponse shippingMethodResponse =
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryHolidayBundledUploadModel.getDmcd());
            DeliveryMethodEntity resultEntity =
                            deliveryHolidayBundledUploadHelper.toDeliveryMethodEntity(shippingMethodResponse);

            // 不正操作チェック。配送マスタは物理削除されないので、結果がないのはURLパラメータをいじられた以外ありえない。
            if (resultEntity == null) {
                addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
                return "redirect:/delivery/";

            } else {
                deliveryHolidayBundledUploadHelper.convertToRegistPageForResult(
                                deliveryHolidayBundledUploadModel, resultEntity);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("deliveryMethodSeq", "dmcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "delivery/holiday/bundledupload/upload";
        }

        return "delivery/holiday/bundledupload/upload";
    }

    /**
     * アップロード<br/>
     *
     * @param deliveryHolidayBundledUploadModel
     * @return class
     */
    @PostMapping(value = "/", params = "doOnceUpload")
    @HEHandler(exception = AppLevelListException.class,
               returnView = "redirect:/delivery/holiday/bundledupload/uploadfinish/")
    public String doOnceUpload(@Validated DeliveryHolidayBundledUploadModel deliveryHolidayBundledUploadModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (error.hasErrors()) {
            return "delivery/holiday/bundledupload/upload";
        }

        // CSVHelper取得
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);

        Integer year = deliveryHolidayBundledUploadModel.getYear();
        Integer dcmd = deliveryHolidayBundledUploadModel.getDmcd();
        if (year == null || dcmd == null) {
            throwMessage();
        }

        // アップロードファイルをテンプファイルとして保存
        String tmpFileName = csvUtility.getUploadCsvTmpFileName("holiday");

        // ファイル操作Helper取得
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        try {
            fileOperationUtility.put(deliveryHolidayBundledUploadModel.getUploadFile(), tmpFileName);
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(DeliveryHolidayBundledUploadModel.MSGCD_FAIL_DELETE, redirectAttributes, model);
            return "redirect:/delivery/holiday/bundledupload/";
        }

        // --------------------------------------------------------------------
        // アップロードされた倉庫休日CSVの事前バリデーション
        // --------------------------------------------------------------------
        CsvUploadResult csvUploadResult = this.validateHolidayCsv(new File(tmpFileName));

        if (csvUploadResult.isInValid()) {
            try {
                // NGファイルは削除する
                fileOperationUtility.remove(tmpFileName);
                // バッチ起動をせずにそのまま終了させる
            } catch (IOException e) {
                LOGGER.error("例外処理が発生しました", e);
                // ファイルの削除に失敗した場合
                this.throwMessage(deliveryHolidayBundledUploadModel.MSGCD_FAIL_DELETE);
            }
            // 処理結果DxoでPageへ反映
            deliveryHolidayBundledUploadHelper.toPageForCsvUploadResultDto(deliveryHolidayBundledUploadModel, csvUploadResult);

            // 異常終了時
            return "redirect:/delivery/holiday/bundledupload/uploadfinish/";
        }

        // アップロードサービス実行
        HolidayCsvUploadRequest holidayCsvUploadRequest = new HolidayCsvUploadRequest();
        holidayCsvUploadRequest.setUpLoadFilePath(tmpFileName);
        holidayCsvUploadRequest.setYear(year);
        HolidayCsvUploadResponse holidayCsvUploadResponse = null;

        try {
            holidayCsvUploadResponse = holidayApi.updateCsv(dcmd, holidayCsvUploadRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            deliveryHolidayBundledUploadModel.setUploadCount(0);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/holiday/bundledupload/upload";
        }

        if (holidayCsvUploadResponse != null) {
            csvUploadResult = deliveryHolidayBundledUploadHelper.toCsvUploadResult(holidayCsvUploadResponse);
            deliveryHolidayBundledUploadHelper.toPageForCsvUploadResultDto(
                            deliveryHolidayBundledUploadModel, csvUploadResult);
        }

        // 正常終了時
        return "redirect:/delivery/holiday/bundledupload/uploadfinish/";
    }

    /**
     * 戻る
     *
     * @param deliveryHolidayBundledUploadModel
     * @param redirectAttrs
     * @return class
     */
    @PostMapping(value = "/", params = "doReturn")
    public String doReturn(DeliveryHolidayBundledUploadModel deliveryHolidayBundledUploadModel,
                           RedirectAttributes redirectAttrs) {
        // アプリケーションが変わるため、選択年、配送方法SEQをリダイレクトスコープで引き渡す
        String yearHoliday = deliveryHolidayBundledUploadModel.getYear() != null ?
                        String.valueOf(deliveryHolidayBundledUploadModel.getYear()) :
                        "";
        String dmcdHoliday = deliveryHolidayBundledUploadModel.getDmcd() != null ?
                        String.valueOf(deliveryHolidayBundledUploadModel.getDmcd()) :
                        "";
        return "redirect:/delivery/holiday/?yearParam=" + yearHoliday + "&dmcdParam=" + dmcdHoliday + "&isUpload="
               + true;
    }

    /**
     * 一括アップロード完了画面表示
     *
     * @param deliveryHolidayBundledUploadModel
     * @return
     */
    @GetMapping(value = "/uploadfinish/")
    protected String doLoadFinish(DeliveryHolidayBundledUploadModel deliveryHolidayBundledUploadModel) {

        // ブラウザバックの場合、処理しない
        if (deliveryHolidayBundledUploadModel.getYear() == null
            || deliveryHolidayBundledUploadModel.getDmcd() == null) {
            return "redirect:/delivery/holiday/bundledupload/";
        }

        return "delivery/holiday/bundledupload/uploadfinish";
    }

    /**
     * インデックス画面へ戻る<br/>
     *
     * @param deliveryHolidayBundledUploadModel
     * @return class
     */
    @PostMapping(value = "/uploadfinish/", params = "doFinish")
    public String doFinish(DeliveryHolidayBundledUploadModel deliveryHolidayBundledUploadModel,
                           RedirectAttributes redirectAttributes) {
        // アプリケーションが変わるため、選択年、配送方法SEQをリダイレクトスコープで引き渡す
        String yearHoliday = deliveryHolidayBundledUploadModel.getYear() != null ?
                        String.valueOf(deliveryHolidayBundledUploadModel.getYear()) :
                        "";
        String dmcdHoliday = deliveryHolidayBundledUploadModel.getDmcd() != null ?
                        String.valueOf(deliveryHolidayBundledUploadModel.getDmcd()) :
                        "";

        return "redirect:/delivery/holiday/?yearParam=" + yearHoliday + "&dmcdParam=" + dmcdHoliday + "&isUpload="
               + true;
    }

    /**
     * アップロードされた倉庫休日CSVの事前バリデーション
     *
     * @param csvFile
     * @return CsvUploadResult
     */
    private CsvUploadResult validateHolidayCsv(File csvFile) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(CsvUploadResult.CSV_UPLOAD_VALID_LIMIT);

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();
        CsvReaderModule csvReaderModule = ApplicationContextUtility.getBean(CsvReaderModule.class);

        /* Csvファイルを読み込み */
        List<HolidayCsvDto> holidayCsvDtoList;
        try {
            // CSVの全行分の DTO を作成してみる
            holidayCsvDtoList = (List<HolidayCsvDto>) csvReaderModule.readCsv(csvFile, HolidayCsvDto.class, csvUploadResult,
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