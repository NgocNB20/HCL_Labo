/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.holiday.presentation.api;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.core.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.HolidaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;
import jp.co.itechh.quad.holiday.presentation.api.param.CsvUploadErrorResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.CsvValidationResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayCsvUploadResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayDownloadCsvRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayInvalidDetail;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayListGetRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayListResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayRegistRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * 休日検索Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class HolidayHelper {

    /**
     * Pageの値をエンティティへコピーします
     *
     * @param holidayRegistRequest ページ
     * @return entity HolidayEntity 休日エンティティ
     */
    public HolidayEntity toHolidayEntityForRegist(Integer deliveryMethodSeq,
                                                  HolidayRegistRequest holidayRegistRequest) {
        HolidayEntity entity = ApplicationContextUtility.getBean(HolidayEntity.class);
        entity.setDate(holidayRegistRequest.getDate());

        // 年
        // 入力値から年を得るため、Date型をCalendar型に変換
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(holidayRegistRequest.getDate());
        entity.setYear(calendar.get(Calendar.YEAR));
        // 名前
        entity.setName(holidayRegistRequest.getName());
        // 配送方法SEQ
        entity.setDeliveryMethodSeq(deliveryMethodSeq);

        return entity;
    }

    /**
     * レスポンスに変換<br/>
     *
     * @param holidayEntity 休日クラス
     * @return 倉庫休日レスポンス
     */
    public HolidayResponse toHolidayResponse(HolidayEntity holidayEntity) {
        HolidayResponse holidayResponse = new HolidayResponse();
        holidayResponse.setDeliveryMethodSeq(holidayEntity.getDeliveryMethodSeq());
        holidayResponse.setDate(holidayEntity.getDate());
        holidayResponse.setYear(holidayEntity.getYear());
        holidayResponse.setName(holidayEntity.getName());
        holidayResponse.setRegistTime(holidayEntity.getRegistTime());

        return holidayResponse;
    }

    /**
     * 検索条件生成<br/>
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @param holidayListGetRequest 休日一覧取得リクエスト
     * @return 検索条件Dto
     */
    public HolidaySearchForDaoConditionDto toHolidaySearchForDaoConditionDto(Integer deliveryMethodSeq,
                                                                             HolidayListGetRequest holidayListGetRequest) {
        // 検索条件Dto取得
        HolidaySearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(HolidaySearchForDaoConditionDto.class);

        conditionDto.setYear(holidayListGetRequest.getYear());
        conditionDto.setDeliveryMethodSeq(deliveryMethodSeq);

        return conditionDto;
    }

    /**
     * 検索条件生成<br/>
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @param holidayDownloadCsvRequest 休日一覧取得リクエスト
     * @return 検索条件Dto
     */
    public HolidaySearchForDaoConditionDto toHolidaySearchForDaoConditionDto(Integer deliveryMethodSeq,
                                                                             HolidayDownloadCsvRequest holidayDownloadCsvRequest) {
        // 検索条件Dto取得
        HolidaySearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(HolidaySearchForDaoConditionDto.class);

        conditionDto.setYear(holidayDownloadCsvRequest.getYear());
        conditionDto.setDeliveryMethodSeq(deliveryMethodSeq);

        return conditionDto;
    }

    /**
     * レスポンスに変換<br/>
     *
     * @param holidayEntityList 休日エンティティリスト
     * @return 倉庫休日一覧レスポンス
     */
    public HolidayListResponse toHolidayListResponse(List<HolidayEntity> holidayEntityList) {

        HolidayListResponse holidayListResponse = new HolidayListResponse();
        List<HolidayResponse> holidayResponseList = new ArrayList<>();

        holidayEntityList.forEach(holidayEntity -> {
            HolidayResponse holidayResponse = toHolidayResponse(holidayEntity);
            holidayResponseList.add(holidayResponse);
        });

        holidayListResponse.setHolidayList(holidayResponseList);

        return holidayListResponse;
    }

    /**
     * 倉庫休日CSVアップロードレスポンスに変換
     *
     * @param csvUploadResult Csvアップロード結果Dto
     * @return 倉庫休日CSVアップロードレスポンス
     */
    public HolidayCsvUploadResponse toHolidayCsvUploadResponse(CsvUploadResult csvUploadResult) {
        HolidayCsvUploadResponse holidayCsvUploadResponse = new HolidayCsvUploadResponse();

        holidayCsvUploadResponse.setCsvValidationResult(
                        toCsvValidationResult(csvUploadResult.getCsvValidationResult()));
        holidayCsvUploadResponse.setCsvUploadErrorList(toCsvUploadErrorlList(csvUploadResult.getCsvUploadErrorlList()));
        holidayCsvUploadResponse.setValidLimitFlg(csvUploadResult.isValidLimitFlg());
        holidayCsvUploadResponse.setRecordCount(csvUploadResult.getRecordCount());

        return holidayCsvUploadResponse;
    }

    /**
     * Csvバリデータ結果に変換
     *
     * @param csvValidationResult Csvバリデータ結果
     * @return Csvバリデータ結果
     */
    public CsvValidationResponse toCsvValidationResult(CsvValidationResult csvValidationResult) {
        if (csvValidationResult == null) {
            return new CsvValidationResponse();
        }

        CsvValidationResponse csvValidationResponse = new CsvValidationResponse();

        csvValidationResponse.setDetailList(toHolidayInvalidDetailList(csvValidationResult.getDetailList()));

        return csvValidationResponse;
    }

    /**
     * 検証NG詳細リストに変換
     *
     * @param invalidDetailList 検証NG詳細リスト
     * @return 検証NG詳細リスト
     */
    public List<HolidayInvalidDetail> toHolidayInvalidDetailList(List<InvalidDetail> invalidDetailList) {
        List<HolidayInvalidDetail> holidayInvalidDetailList = new ArrayList<>();

        if (CollectionUtil.isEmpty(invalidDetailList)) {
            return holidayInvalidDetailList;
        }

        for (InvalidDetail invalidDetail : invalidDetailList) {
            HolidayInvalidDetail holidayInvalidDetail = new HolidayInvalidDetail();

            holidayInvalidDetail.setRow(invalidDetail.getRow());
            holidayInvalidDetail.setColumnLabel(invalidDetail.getColumnLabel());
            holidayInvalidDetail.setMessage(invalidDetail.getMessage());

            holidayInvalidDetailList.add(holidayInvalidDetail);
        }

        return holidayInvalidDetailList;
    }

    /**
     * Csvアップロードエラー格納Dtoに変換
     *
     * @param csvUploadErrorlList Csvアップロードエラー格納Dto
     * @return Csvアップロードエラー格納Dto
     */
    public List<CsvUploadErrorResponse> toCsvUploadErrorlList(List<CsvUploadError> csvUploadErrorlList) {
        List<CsvUploadErrorResponse> csvUploadErrorListResponse = new ArrayList<>();

        if (csvUploadErrorlList != null) {

            for (CsvUploadError csvUploadError : csvUploadErrorlList) {
                CsvUploadErrorResponse csvUploadErrorResponse = new CsvUploadErrorResponse();

                csvUploadErrorResponse.setRow(csvUploadError.getRow());
                csvUploadErrorResponse.setMessage(csvUploadError.getMessage());

                csvUploadErrorListResponse.add(csvUploadErrorResponse);
            }
        }
        return csvUploadErrorListResponse;
    }
}