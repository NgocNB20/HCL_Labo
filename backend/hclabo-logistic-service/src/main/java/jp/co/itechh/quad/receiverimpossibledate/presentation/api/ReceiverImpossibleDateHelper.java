/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.receiverimpossibledate.presentation.api;

import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.core.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;
import jp.co.itechh.quad.receiveimpossibledate.presentation.api.ShippingsApi;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.CsvUploadErrorResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.CsvValidationResultResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateRegistRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesCsvUploadResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesDownloadCsvRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDatesInvalidDetail;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * お届け不可日 Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ReceiverImpossibleDateHelper implements ShippingsApi {

    /**
     * 検索条件生成<br/>
     *
     * @param receiverImpossibleDateListRequest お届け不可日一覧リクエスト
     * @param deliveryMethodSeq                 配送方法SEQ
     * @return 検索条件Dto
     */
    public DeliveryImpossibleDaySearchForDaoConditionDto toConditionDto(Integer deliveryMethodSeq,
                                                                        ReceiverImpossibleDateListRequest receiverImpossibleDateListRequest) {
        // 検索条件Dto取得
        DeliveryImpossibleDaySearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(DeliveryImpossibleDaySearchForDaoConditionDto.class);

        conditionDto.setYear(receiverImpossibleDateListRequest.getYear());
        conditionDto.setDeliveryMethodSeq(deliveryMethodSeq);

        return conditionDto;
    }

    /**
     * 検索条件生成<br/>
     *
     * @param receiverImpossibleDatesDownloadCsvRequest お届け不可日CSVDLリクエスト
     * @param deliveryMethodSeq                         配送方法SEQ
     * @return 検索条件Dto
     */
    public DeliveryImpossibleDaySearchForDaoConditionDto toConditionDto(Integer deliveryMethodSeq,
                                                                        ReceiverImpossibleDatesDownloadCsvRequest receiverImpossibleDatesDownloadCsvRequest) {
        // 検索条件Dto取得
        DeliveryImpossibleDaySearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(DeliveryImpossibleDaySearchForDaoConditionDto.class);

        conditionDto.setYear(receiverImpossibleDatesDownloadCsvRequest.getYear());
        conditionDto.setDeliveryMethodSeq(deliveryMethodSeq);

        return conditionDto;
    }

    /**
     * レスポンスに変換<br/>
     *
     * @param deliveryImpossibleDayEntities の値をエンティティへコピー
     * @return お届け不可日一覧レスポンス
     */
    public ReceiverImpossibleDateListResponse toReceiverImpossibleDateListResponses(List<DeliveryImpossibleDayEntity> deliveryImpossibleDayEntities) {

        ReceiverImpossibleDateListResponse receiverImpossibleDateListResponse =
                        new ReceiverImpossibleDateListResponse();
        List<ReceiverImpossibleDateResponse> impossibleAreaList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(deliveryImpossibleDayEntities)) {
            for (DeliveryImpossibleDayEntity deliveryImpossibleDayEntity : deliveryImpossibleDayEntities) {

                ReceiverImpossibleDateResponse receiverImpossibleDateResponse =
                                toReceiverImpossibleDateResponse(deliveryImpossibleDayEntity);

                impossibleAreaList.add(receiverImpossibleDateResponse);
            }

            receiverImpossibleDateListResponse.setImpossibleAreaList(impossibleAreaList);
        }

        return receiverImpossibleDateListResponse;
    }

    /**
     * リクエスト値をエンティティへコピーします
     *
     * @param deliveryMethodSeq                   配送方法SEQ
     * @param receiverImpossibleDateRegistRequest お届け不可日登録リクエスト
     * @return お届け不可日クラス
     */
    public DeliveryImpossibleDayEntity toDeliveryImpossibleDayEntityForRegistUpdate(Integer deliveryMethodSeq,
                                                                                    ReceiverImpossibleDateRegistRequest receiverImpossibleDateRegistRequest) {

        DeliveryImpossibleDayEntity entity = ApplicationContextUtility.getBean(DeliveryImpossibleDayEntity.class);
        entity.setDate(receiverImpossibleDateRegistRequest.getDate());

        // 年
        // 入力値から年を得るため、Date型をCalendar型に変換
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(receiverImpossibleDateRegistRequest.getDate());
        entity.setYear(calendar.get(Calendar.YEAR));

        // 名前
        entity.setReason(receiverImpossibleDateRegistRequest.getReason());

        // 配送方法SEQ
        entity.setDeliveryMethodSeq(deliveryMethodSeq);

        return entity;

    }

    /**
     * お届け不可日レスポンスに変換
     *
     * @param deliveryImpossibleDayEntity お届け不可日エンティティ
     * @return お届け不可日レスポンス
     */
    public ReceiverImpossibleDateResponse toReceiverImpossibleDateResponse(DeliveryImpossibleDayEntity deliveryImpossibleDayEntity) {

        ReceiverImpossibleDateResponse receiverImpossibleDateResponse = new ReceiverImpossibleDateResponse();

        receiverImpossibleDateResponse.setDate(deliveryImpossibleDayEntity.getDate());
        receiverImpossibleDateResponse.setDeliveryMethodSeq(deliveryImpossibleDayEntity.getDeliveryMethodSeq());
        receiverImpossibleDateResponse.setYear(deliveryImpossibleDayEntity.getYear());
        receiverImpossibleDateResponse.setReason(deliveryImpossibleDayEntity.getReason());
        receiverImpossibleDateResponse.setRegistTime(deliveryImpossibleDayEntity.getRegistTime());

        return receiverImpossibleDateResponse;
    }

    /**
     * お届け不可日CSVアップロードレスポンスに変換
     *
     * @param csvUploadResult お届け不可日CSVアップロードレスポンス
     * @return お届け不可日CSVアップロードレスポンス
     */
    public ReceiverImpossibleDatesCsvUploadResponse toReceiverImpossibleDatesCsvUploadResponse(CsvUploadResult csvUploadResult) {
        ReceiverImpossibleDatesCsvUploadResponse receiverImpossibleDatesCsvUploadResponse =
                        new ReceiverImpossibleDatesCsvUploadResponse();

        receiverImpossibleDatesCsvUploadResponse.setRecordCount(csvUploadResult.getRecordCount());
        receiverImpossibleDatesCsvUploadResponse.setValidLimitFlg(csvUploadResult.isValidLimitFlg());
        if (CollectionUtils.isNotEmpty(csvUploadResult.getCsvUploadErrorlList())) {
            receiverImpossibleDatesCsvUploadResponse.setCsvUploadErrorList(
                            toCsvUploadErrorList(csvUploadResult.getCsvUploadErrorlList()));
        }
        if (csvUploadResult.getCsvValidationResult() != null) {
            receiverImpossibleDatesCsvUploadResponse.setCsvValidationResult(
                            toCsvValidationResult(csvUploadResult.getCsvValidationResult()));
        }

        return receiverImpossibleDatesCsvUploadResponse;
    }

    /**
     * Csvアップロードエラー格納レスポンスに変換
     *
     * @param csvUploadErrors Csvアップロードエラー格納Dto
     * @return Csvアップロードエラー格納レスポンス
     */
    private List<CsvUploadErrorResponse> toCsvUploadErrorList(List<CsvUploadError> csvUploadErrors) {

        List<CsvUploadErrorResponse> result = new ArrayList<>();

        for (CsvUploadError csvUploadError : csvUploadErrors) {
            CsvUploadErrorResponse csv = new CsvUploadErrorResponse();

            csv.setMessage(csvUploadError.getMessage());
            csv.setRow(csvUploadError.getRow());

            result.add(csv);
        }

        return result;
    }

    /**
     * CSVバリテーションレスポンスに変換
     *
     * @param csvValidationResult  CSVバリテーション結果
     * @return CSVバリテーションレスポンス
     */
    private CsvValidationResultResponse toCsvValidationResult(CsvValidationResult csvValidationResult) {

        CsvValidationResultResponse result = new CsvValidationResultResponse();
        List<ReceiverImpossibleDatesInvalidDetail> detailList = new ArrayList<>();

        for (InvalidDetail invalidDetail : csvValidationResult.getDetailList()) {
            ReceiverImpossibleDatesInvalidDetail receiverImpossibleDatesInvalidDetail =
                            new ReceiverImpossibleDatesInvalidDetail();
            receiverImpossibleDatesInvalidDetail.setMessage(invalidDetail.getMessage());
            receiverImpossibleDatesInvalidDetail.setRow(invalidDetail.getRow());
            receiverImpossibleDatesInvalidDetail.setColumnLabel(invalidDetail.getColumnLabel());

            detailList.add(receiverImpossibleDatesInvalidDetail);
        }

        result.setDetailList(detailList);

        return result;
    }

}