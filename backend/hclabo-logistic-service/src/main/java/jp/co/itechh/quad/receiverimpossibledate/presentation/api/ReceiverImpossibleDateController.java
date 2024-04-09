/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.receiverimpossibledate.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleDayDao;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDayCsvDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayCsvListGetByYearLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayGetByYearAndDateLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayListGetByYearLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayRegistUpdateLogic;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryImpossibleDayCsvUpLoadService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.receiveimpossibledate.presentation.api.ShippingsApi;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateDeleteRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateRegistRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesCsvUploadRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesCsvUploadResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesDownloadCsvRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * お届け不可日 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class ReceiverImpossibleDateController extends AbstractController implements ShippingsApi {

    /** お届け不可日取得ロジック */
    private final DeliveryImpossibleDayListGetByYearLogic deliveryImpossibleDayListGetByYearLogic;

    /**  お届け不可日取得ロジック */
    private final DeliveryImpossibleDayGetByYearAndDateLogic deliveryImpossibleDayGetByYearAndDateLogic;

    /**  お届け不可日検索Helper */
    private final ReceiverImpossibleDateHelper receiverImpossibleDateHelper;

    /** お届け不可日登録ロジック */
    private final DeliveryImpossibleDayRegistUpdateLogic deliveryImpossibleDayRegistLogic;

    /** お届け不可日Dao */
    private final DeliveryImpossibleDayDao deliveryImpossibleDayDao;

    /** CSVアップロードサービス */
    private final DeliveryImpossibleDayCsvUpLoadService deliveryImpossibleDayCsvUpLoadService;

    /** お届け不可日CSVダウンロードロジック */
    private final DeliveryImpossibleDayCsvListGetByYearLogic deliveryDidAllCsvDownloadLogic;

    /** メッセージコード：年月日チェックエラー */
    public static final String MSGCD_DATE_CHECK_ERROR = "HM34-4001-001-A-";

    /** メッセージコード：登録失敗 */
    public static final String MSGCD_DELETE_FAILURE = "HM34-4001-005-A-";

    /** メッセージコード：登録失敗 */
    public static final String MSGCD_REGIST_FAILURE = "HM34-4001-003-A-";

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverImpossibleDateController.class);

    /**
     * コンストラクタ
     *
     * @param receiverImpossibleDateHelper               お届け不可日 helper
     * @param deliveryImpossibleDayListGetByYearLogic    お届け不可日取得ロジック
     * @param deliveryImpossibleDayGetByYearAndDateLogic お届け不可日取得ロジック
     * @param deliveryImpossibleDayRegistLogic           お届け不可日登録ロジック
     * @param deliveryImpossibleDayDao                   お届け不可日Dao
     * @param deliveryImpossibleDayCsvUpLoadService      CSVアップロードサービス
     */
    public ReceiverImpossibleDateController(ReceiverImpossibleDateHelper receiverImpossibleDateHelper,
                                            DeliveryImpossibleDayListGetByYearLogic deliveryImpossibleDayListGetByYearLogic,
                                            DeliveryImpossibleDayGetByYearAndDateLogic deliveryImpossibleDayGetByYearAndDateLogic,
                                            DeliveryImpossibleDayRegistUpdateLogic deliveryImpossibleDayRegistLogic,
                                            DeliveryImpossibleDayDao deliveryImpossibleDayDao,
                                            DeliveryImpossibleDayCsvUpLoadService deliveryImpossibleDayCsvUpLoadService,
                                            DeliveryImpossibleDayCsvListGetByYearLogic deliveryDidAllCsvDownloadLogic) {
        this.deliveryImpossibleDayGetByYearAndDateLogic = deliveryImpossibleDayGetByYearAndDateLogic;
        this.deliveryImpossibleDayListGetByYearLogic = deliveryImpossibleDayListGetByYearLogic;
        this.receiverImpossibleDateHelper = receiverImpossibleDateHelper;
        this.deliveryImpossibleDayRegistLogic = deliveryImpossibleDayRegistLogic;
        this.deliveryImpossibleDayDao = deliveryImpossibleDayDao;
        this.deliveryImpossibleDayCsvUpLoadService = deliveryImpossibleDayCsvUpLoadService;
        this.deliveryDidAllCsvDownloadLogic = deliveryDidAllCsvDownloadLogic;
    }

    /**
     * DELETE /shippings/methods/{deliveryMethodSeq}/receiver-impossible-dates : お届け不可日削除
     * お届け不可日削除
     *
     * @param deliveryMethodSeq                   配送方法SEQ (required)
     * @param receiverImpossibleDateDeleteRequest お届け不可日削除リクエスト (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<Void> delete(@ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                                       Integer deliveryMethodSeq,
                                       @NotNull @ApiParam(value = "お届け不可日削除リクエスト", required = true) @Valid
                                                       ReceiverImpossibleDateDeleteRequest receiverImpossibleDateDeleteRequest) {

        // 削除対象お届け不可日取得
        Date deleteDate = null;
        try {
            deleteDate = DateUtils.parseDate(receiverImpossibleDateDeleteRequest.getDeleteDate(),
                                             new String[] {"yyyy/MM/dd"}
                                            );
        } catch (ParseException e) {
            LOGGER.error("例外処理が発生しました", e);
            List<AppLevelException> appLevelExceptions = new ArrayList<>();
            AppLevelException appLevelException = new AppLevelException(e);
            appLevelException.setAppLevelFacesMessage(new AppLevelFacesMessage());
            appLevelExceptions.add(appLevelException);
            throw new AppLevelListException(appLevelExceptions);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deleteDate);
        DeliveryImpossibleDayEntity deliveryImpossibleDayEntity =
                        deliveryImpossibleDayGetByYearAndDateLogic.execute(calendar.get(Calendar.YEAR), deleteDate,
                                                                           deliveryMethodSeq
                                                                          );

        // 削除結果
        int result = 0;
        if (deliveryImpossibleDayEntity != null) {
            // 削除処理を実行
            result = deliveryImpossibleDayDao.delete(deliveryImpossibleDayEntity);
        }

        if (result <= 0) {
            // エラーメッセージを設定
            throwMessage(MSGCD_DELETE_FAILURE, null);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /shippings/methods/{deliveryMethodSeq}/receiver-impossible-dates : お届け不可日一覧取得
     * お届け不可日一覧取得
     *
     * @param deliveryMethodSeq                 配送方法SEQ (required)
     * @param receiverImpossibleDateListRequest お届け不可日一覧リクエスト (required)
     * @param pageInfoRequest                   ページ情報リクエスト（ページネーションのため） (optional)
     * @return お届け不可日一覧レスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<ReceiverImpossibleDateListResponse> get(Integer deliveryMethodSeq,
                                                                  @NotNull @Valid
                                                                                  ReceiverImpossibleDateListRequest receiverImpossibleDateListRequest,
                                                                  @Valid PageInfoRequest pageInfoRequest) {

        DeliveryImpossibleDaySearchForDaoConditionDto conditionDto =
                        receiverImpossibleDateHelper.toConditionDto(deliveryMethodSeq,
                                                                    receiverImpossibleDateListRequest
                                                                   );

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<DeliveryImpossibleDayEntity> list = deliveryImpossibleDayListGetByYearLogic.execute(conditionDto);
        ReceiverImpossibleDateListResponse receiverImpossibleDateListResponse =
                        receiverImpossibleDateHelper.toReceiverImpossibleDateListResponses(list);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        receiverImpossibleDateListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(receiverImpossibleDateListResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/methods/{deliveryMethodSeq}/receiver-impossible-dates : お届け不可日登録
     * お届け不可日登録
     *
     * @param deliveryMethodSeq                   配送方法SEQ (required)
     * @param receiverImpossibleDateRegistRequest お届け不可日登録リクエスト (required)
     * @return お届け不可日レスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<ReceiverImpossibleDateResponse> regist(Integer deliveryMethodSeq,
                                                                 @Valid
                                                                                 ReceiverImpossibleDateRegistRequest receiverImpossibleDateRegistRequest) {
        // Pageの値をエンティティへコピー
        DeliveryImpossibleDayEntity deliveryImpossibleDayEntity =
                        receiverImpossibleDateHelper.toDeliveryImpossibleDayEntityForRegistUpdate(deliveryMethodSeq,
                                                                                                  receiverImpossibleDateRegistRequest
                                                                                                 );

        // システム日付を取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Date now = dateUtility.getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        // 入力した年が今年または翌月でない場合
        if (deliveryImpossibleDayEntity.getYear() != calendar.get(Calendar.YEAR)
            && deliveryImpossibleDayEntity.getYear() != calendar.get(Calendar.YEAR) + 1) {
            // エラーメッセージを設定し、更新処理は行わない
            throwMessage(MSGCD_DATE_CHECK_ERROR, new Object[] {"年月日"});
        }

        // 登録更新結果
        int result = deliveryImpossibleDayRegistLogic.execute(deliveryImpossibleDayEntity);
        if (result <= 0) {
            // エラーメッセージを設定
            throwMessage(MSGCD_REGIST_FAILURE);
        }

        DeliveryImpossibleDayEntity deliveryImpossibleDayEntityResponse =
                        deliveryImpossibleDayGetByYearAndDateLogic.execute(
                                        receiverImpossibleDateRegistRequest.getYear(),
                                        receiverImpossibleDateRegistRequest.getDate(), deliveryMethodSeq
                                                                          );
        ReceiverImpossibleDateResponse receiverImpossibleDateResponse =
                        receiverImpossibleDateHelper.toReceiverImpossibleDateResponse(
                                        deliveryImpossibleDayEntityResponse);

        return new ResponseEntity<>(receiverImpossibleDateResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/methods/{deliveryMethodSeq}/receiver-impossible-dates/csv : お届け不可日アップロード
     * お届け不可日アップロード
     *
     * @param deliveryMethodSeq                       配送方法SEQ (required)
     * @param receiverImpossibleDatesCsvUploadRequest お届け不可日CSVアップロードリクエスト (required)
     * @return お届け不可日CSVアップロードレスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<ReceiverImpossibleDatesCsvUploadResponse> uploadCsv(
                    @ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                    Integer deliveryMethodSeq,
                    @ApiParam(value = "お届け不可日CSVアップロードリクエスト", required = true) @Valid @RequestBody
                                    ReceiverImpossibleDatesCsvUploadRequest receiverImpossibleDatesCsvUploadRequest) {

        File upLoadFile = new File(receiverImpossibleDatesCsvUploadRequest.getUpLoadFilePath());

        CsvUploadResult csvUploadResult = deliveryImpossibleDayCsvUpLoadService.execute(upLoadFile,
                                                                                        receiverImpossibleDatesCsvUploadRequest.getCsvUploadValidLimit(),
                                                                                        receiverImpossibleDatesCsvUploadRequest.getYear(),
                                                                                        deliveryMethodSeq
                                                                                       );

        ReceiverImpossibleDatesCsvUploadResponse receiverImpossibleDatesCsvUploadResponse =
                        receiverImpossibleDateHelper.toReceiverImpossibleDatesCsvUploadResponse(csvUploadResult);

        return new ResponseEntity<>(receiverImpossibleDatesCsvUploadResponse, HttpStatus.OK);
    }

    /**
     * GET /shippings/methods/{deliveryMethodSeq}/receiver-impossible-dates/csv : お届け不可日CSVDL
     * お届け不可日CSVDL
     *
     * @param deliveryMethodSeq                         配送方法SEQ (required)
     * @param receiverImpossibleDatesDownloadCsvRequest お届け不可日CSVDLリクエスト (optional)
     * @return 成功 (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ResponseEntity<Void> receiverImpossibleDateDownloadCsv(
                    @ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                    Integer deliveryMethodSeq,
                    @ApiParam(value = "年") @Valid
                                    ReceiverImpossibleDatesDownloadCsvRequest receiverImpossibleDatesDownloadCsvRequest) {
        try {
            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(
                            RequestContextHolder.getRequestAttributes())).getResponse();

            assert response != null;
            response.setCharacterEncoding("MS932");

            // Apache Common CSV を特化したCSVフォーマットを準備する
            // 主にHIT-MALL独自のCsvDownloadOptionDtoからCSVFormatに変換する
            CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
            csvDownloadOptionDto.setOutputHeader(true);
            CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(csvDownloadOptionDto);

            // Apache Common CSV を特化したCSVプリンター（設定したCSVフォーマットに沿ってデータを出力）を初期化する
            StringWriter stringWriter = new StringWriter();
            CSVPrinter printer = new CSVPrinter(stringWriter, outputCsvFormat);

            PrintWriter writer = response.getWriter();
            printer.printRecord(CsvDownloadHandler.outHeader(DeliveryImpossibleDayCsvDto.class));
            writer.write(stringWriter.toString());
            stringWriter.getBuffer().setLength(0);
            writer.flush();

            // 検索条件作成
            DeliveryImpossibleDaySearchForDaoConditionDto conditionDto =
                            receiverImpossibleDateHelper.toConditionDto(deliveryMethodSeq,
                                                                        receiverImpossibleDatesDownloadCsvRequest
                                                                       );

            try (Stream<DeliveryImpossibleDayCsvDto> deliveryImpossibleDayCsvDtoStream = deliveryDidAllCsvDownloadLogic.execute(
                            conditionDto)) {
                deliveryImpossibleDayCsvDtoStream.forEach((deliveryImpossibleDayCsvDto -> {
                    try {
                        printer.printRecord(CsvDownloadHandler.outCsvRecord(deliveryImpossibleDayCsvDto));
                        writer.write(stringWriter.toString());
                        stringWriter.getBuffer().setLength(0);
                    } catch (IOException e) {
                        LOGGER.error("例外処理が発生しました", e);
                    }
                }));
                writer.flush();
            }
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}