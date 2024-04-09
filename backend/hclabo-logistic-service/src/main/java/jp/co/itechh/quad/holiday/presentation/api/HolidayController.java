/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.holiday.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.delivery.HolidayDao;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.HolidayCsvDto;
import jp.co.itechh.quad.core.dto.shop.delivery.HolidaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.logic.shop.delivery.HolidayCsvListGetByYearLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.HolidayGetByYearAndDateLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.HolidayListGetByYearLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.HolidayRegistUpdateLogic;
import jp.co.itechh.quad.core.service.shop.delivery.HolidayCsvUpLoadService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.holiday.presentation.api.param.ErrorResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayCsvUploadRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayCsvUploadResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayDeleteRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayDownloadCsvRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayListGetRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayListResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayRegistRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.PageInfoResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 休日エンドポイント Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@RestController
public class HolidayController extends AbstractController implements ShippingsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayController.class);

    /** 休日検索Helper */
    private final HolidayHelper holidayHelper;

    /** 休日登録ロジック */
    private final HolidayRegistUpdateLogic holidayRegistLogic;

    /** 休日取得ロジック */
    private final HolidayGetByYearAndDateLogic holidayGetByYearAndDateLogic;

    /** 休日取得ロジック */
    private final HolidayListGetByYearLogic holidayListGetByYearLogic;

    /** 休日Dao */
    private final HolidayDao holidayDao;

    /** CSVアップロードサービス */
    private final HolidayCsvUpLoadService holidayCsvUpLoadService;

    /** 休日CSVダウンロードロジック */
    private final HolidayCsvListGetByYearLogic deliveryHolidayAllCsvDownloadLogic;

    /**
     * コンストラクタ
     *
     * @param holidayHelper 休日検索Helper
     * @param holidayRegistLogic 休日登録ロジック
     * @param holidayGetByYearAndDateLogic 休日取得ロジック
     * @param holidayListGetByYearLogic 休日取得ロジック
     * @param holidayDao 休日Dao
     * @param deliveryHolidayAllCsvDownloadLogic 休日CSVダウンロードロジック
     */
    @Autowired
    public HolidayController(HolidayHelper holidayHelper,
                             HolidayRegistUpdateLogic holidayRegistLogic,
                             HolidayGetByYearAndDateLogic holidayGetByYearAndDateLogic,
                             HolidayListGetByYearLogic holidayListGetByYearLogic,
                             HolidayDao holidayDao,
                             HolidayCsvUpLoadService holidayCsvUpLoadService,
                             HolidayCsvListGetByYearLogic deliveryHolidayAllCsvDownloadLogic) {
        this.holidayHelper = holidayHelper;
        this.holidayRegistLogic = holidayRegistLogic;
        this.holidayGetByYearAndDateLogic = holidayGetByYearAndDateLogic;
        this.holidayListGetByYearLogic = holidayListGetByYearLogic;
        this.holidayDao = holidayDao;
        this.holidayCsvUpLoadService = holidayCsvUpLoadService;
        this.deliveryHolidayAllCsvDownloadLogic = deliveryHolidayAllCsvDownloadLogic;
    }

    /**
     * GET /shippings/methods/{deliveryMethodSeq}/holidays : 休日一覧取得
     * 休日一覧取得
     *
     * @param deliveryMethodSeq 配送方法SEQ (required)
     * @param holidayListGetRequest 休日一覧取得リクエスト (required)
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return 倉庫休日一覧レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<HolidayListResponse> getByDeliveryMethodSeq(
                    @ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                    Integer deliveryMethodSeq,
                    @NotNull @ApiParam(value = "休日一覧取得リクエスト", required = true) @Valid
                                    HolidayListGetRequest holidayListGetRequest,
                    @ApiParam(value = "ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest) {
        // 検索条件作成
        HolidaySearchForDaoConditionDto conditionDto =
                        holidayHelper.toHolidaySearchForDaoConditionDto(deliveryMethodSeq, holidayListGetRequest);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        // 取得
        List<HolidayEntity> holidayEntityList = holidayListGetByYearLogic.execute(conditionDto);
        HolidayListResponse holidayListResponse = holidayHelper.toHolidayListResponse(holidayEntityList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        holidayListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(holidayListResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/methods/{deliveryMethodSeq}/holidays : 休日登録
     * 休日登録
     *
     * @param deliveryMethodSeq 配送方法SEQ (required)
     * @param holidayRegistRequest 倉庫休日登録リクエスト (required)
     * @return 倉庫休日レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<HolidayResponse> regist(
                    @ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                    Integer deliveryMethodSeq,
                    @ApiParam(value = "倉庫休日登録リクエスト", required = true) @Valid @RequestBody
                                    HolidayRegistRequest holidayRegistRequest) {
        HolidayEntity holidayEntity = holidayHelper.toHolidayEntityForRegist(deliveryMethodSeq, holidayRegistRequest);

        // システム日付を取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Date now = dateUtility.getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        // 入力した年が今年または翌月でない場合
        if (holidayEntity.getYear() != calendar.get(Calendar.YEAR)
            && holidayEntity.getYear() != calendar.get(Calendar.YEAR) + 1) {
            // エラーメッセージを設定し、更新処理は行わない
            throwMessage("AYH000101", new Object[] {"年月日"});
        }

        // 登録更新結果
        int result = holidayRegistLogic.execute(holidayEntity);
        if (result == 0) {
            // エラーメッセージを設定し、更新処理は行わない
            throwMessage("AYH000103", null);
        }
        HolidayEntity holidayEntityResponse = holidayGetByYearAndDateLogic.execute(holidayRegistRequest.getYear(),
                                                                                   holidayRegistRequest.getDate(),
                                                                                   deliveryMethodSeq
                                                                                  );
        HolidayResponse holidayResponse = holidayHelper.toHolidayResponse(holidayEntityResponse);
        return new ResponseEntity<>(holidayResponse, HttpStatus.OK);
    }

    /**
     * DELETE /shippings/methods/{deliveryMethodSeq}/holidays : 休日削除
     * 休日削除
     *
     * @param deliveryMethodSeq 配送方法SEQ (required)
     * @param holidayDeleteRequest 休日削除リクエスト (required)
     * @return その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<ErrorResponse> delete(
                    @ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                    Integer deliveryMethodSeq,
                    @NotNull @ApiParam(value = "休日削除リクエスト", required = true) @Valid
                                    HolidayDeleteRequest holidayDeleteRequest) {

        // 削除対象休日取得
        Date deleteDate = null;
        try {
            deleteDate = DateUtils.parseDate(holidayDeleteRequest.getDeleteDate(), new String[] {"yyyy/MM/dd"});
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
        HolidayEntity holidayEntity = holidayGetByYearAndDateLogic.execute(calendar.get(Calendar.YEAR), deleteDate,
                                                                           deliveryMethodSeq
                                                                          );

        // 削除結果
        int result = 0;
        if (holidayEntity != null) {
            // 削除処理を実行
            result = holidayDao.delete(holidayEntity);
        }

        if (result == 0) {
            // エラーメッセージを設定し、更新処理は行わない
            throwMessage("AYH000105", null);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /shippings/methods/{deliveryMethodSeq}/holidays/csv : 休日一括CSVDL
     * 休日一括CSVDL
     *
     * @param deliveryMethodSeq 配送方法SEQ (required)
     * @param holidayDownloadCsvRequest 休日一括CSVDLリクエスト (optional)
     * @return 成功 (status code 200)
     *         or その他エラー (status code 500)
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ResponseEntity<Void> holidayDownloadCSV(
                    @ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                    Integer deliveryMethodSeq,
                    @ApiParam(value = "年") @Valid HolidayDownloadCsvRequest holidayDownloadCsvRequest) {

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
            printer.printRecord(CsvDownloadHandler.outHeader(HolidayCsvDto.class));
            writer.write(stringWriter.toString());
            stringWriter.getBuffer().setLength(0);
            writer.flush();

            // 検索条件作成
            HolidaySearchForDaoConditionDto conditionDto =
                            holidayHelper.toHolidaySearchForDaoConditionDto(deliveryMethodSeq,
                                                                            holidayDownloadCsvRequest
                                                                           );

            try (Stream<HolidayCsvDto> holidayCsvDtoStream = deliveryHolidayAllCsvDownloadLogic.execute(conditionDto)) {
                holidayCsvDtoStream.forEach((holidayCsvDto -> {
                    try {
                        printer.printRecord(CsvDownloadHandler.outCsvRecord(holidayCsvDto));
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

    /**
     * POST /shippings/methods/{deliveryMethodSeq}/holidays/csv : 休日アップロード
     * 休日アップロード
     *
     * @param deliveryMethodSeq 配送方法SEQ (required)
     * @param holidayCsvUploadRequest 倉庫休日CSVアップロードリクエスト (required)
     * @return 倉庫休日レスポンス (status code 200)
     *         or 倉庫休日CSVアップロードレスポンス (status code 200)
     */
    @Override
    public ResponseEntity<HolidayCsvUploadResponse> updateCsv(
                    @ApiParam(value = "配送方法SEQ", required = true) @PathVariable("deliveryMethodSeq")
                                    Integer deliveryMethodSeq,
                    @ApiParam(value = "倉庫休日CSVアップロードリクエスト", required = true) @Valid @RequestBody
                                    HolidayCsvUploadRequest holidayCsvUploadRequest) {

        // ファイルを作成
        File upLoadFile = new File(holidayCsvUploadRequest.getUpLoadFilePath());
        CsvUploadResult csvUploadResult =
                        holidayCsvUpLoadService.execute(upLoadFile, CsvUploadResult.CSV_UPLOAD_VALID_LIMIT,
                                                        holidayCsvUploadRequest.getYear(), deliveryMethodSeq
                                                       );

        HolidayCsvUploadResponse holidayCsvUploadResponse = holidayHelper.toHolidayCsvUploadResponse(csvUploadResult);
        return new ResponseEntity<>(holidayCsvUploadResponse, HttpStatus.OK);
    }

}