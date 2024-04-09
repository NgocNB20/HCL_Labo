/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.core.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDayCsvDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayDeleteLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayRegistUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryImpossibleDayCsvUpLoadService;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * お届け不可日CSVアップロードサービス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class DeliveryImpossibleDayCsvUpLoadServiceImpl extends AbstractShopService
                implements DeliveryImpossibleDayCsvUpLoadService {

    /**
     * メッセージコード：年月日重複エラー
     */
    public static final String MSGCD_DATE_DUPLICATE_ERROR = "HM34-4001-001-L-";

    /**
     * メッセージコード：登録失敗
     */
    public static final String MSGCD_REGIST_FAILURE = "HM34-4001-002-L-";

    /**
     * メッセージコード：CSV読込例外発生
     */
    public static final String UNEXPECTED_EXCEPTION_MSG_CD = "CSV-UPLOAD-003-E";

    /**
     * お届け不可日情報登録ロジック
     */
    private final DeliveryImpossibleDayRegistUpdateLogic deliveryImpossibleDayRegistLogic;

    /**
     * お届け不可日情報削除ロジック
     */
    private final DeliveryImpossibleDayDeleteLogic deliveryImpossibleDayDeleteLogic;

    /**
     * CSV読み込みのモジュール
     */
    private final CsvReaderModule csvReaderModule;

    @Autowired
    public DeliveryImpossibleDayCsvUpLoadServiceImpl(DeliveryImpossibleDayRegistUpdateLogic deliveryImpossibleDayRegistLogic,
                                                     DeliveryImpossibleDayDeleteLogic deliveryImpossibleDayDeleteLogic,
                                                     CsvReaderModule csvReaderModule) {
        this.deliveryImpossibleDayRegistLogic = deliveryImpossibleDayRegistLogic;
        this.deliveryImpossibleDayDeleteLogic = deliveryImpossibleDayDeleteLogic;
        this.csvReaderModule = csvReaderModule;
    }

    /**
     * お届け不可日CSV登録処理
     *
     * @param uploadedFile      アップロードファイル
     * @param validLimit        バリデータエラー限界値(行数)
     * @param year              年
     * @param deliveryMethodSeq 配送方法SEQ
     * @return CSVアップロード結果
     */
    @Override
    public CsvUploadResult execute(File uploadedFile, int validLimit, Integer year, Integer deliveryMethodSeq) {

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();

        /* 入力チェック エラーの場合 終了 */
        if (validate(uploadedFile, csvUploadResult, deliveryMethodSeq)) {
            return csvUploadResult;
        }

        /* 登録処理 エラーの場合 終了 */
        if (registProcess(uploadedFile, year, deliveryMethodSeq, csvUploadResult)) {
            return csvUploadResult;
        }

        // 正常終了時は、処理件数を返す
        return csvUploadResult;
    }

    /**
     * 登録処理
     *
     * @param uploadedFile      アップロードファイル
     * @param year              年
     * @param deliveryMethodSeq 配送方法SEQ
     * @param csvUploadResult   CSVアップロード結果
     * @return エラーの有無 true=エラー、false=エラーなし
     */
    protected boolean registProcess(File uploadedFile,
                                    Integer year,
                                    Integer deliveryMethodSeq,
                                    CsvUploadResult csvUploadResult) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(-1);

        try (CSVParser csvParser = csvReaderModule.readCsv(
                        uploadedFile.getPath(), DeliveryImpossibleDayCsvDto.class, csvReaderOptionDto)) {

            // CSVファイルにデータが存在しなかった場合
            if (csvParser == null || !csvParser.iterator().hasNext()) {
                List<CsvUploadError> csvUploadErrorList = new ArrayList<>();
                csvUploadErrorList.add(createCsvUploadError(0, UNEXPECTED_EXCEPTION_MSG_CD, null));
                csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
                return true;
            }

            // これから登録する年のデータを削除
            DeliveryImpossibleDayEntity deliveryImpossibleDayDeleteEntity = new DeliveryImpossibleDayEntity();
            deliveryImpossibleDayDeleteEntity.setYear(year);
            deliveryImpossibleDayDeleteEntity.setDeliveryMethodSeq(deliveryMethodSeq);
            delete(deliveryImpossibleDayDeleteEntity);

            /* 処理件数が気になるので1件づつ処理 */

            // 日付重複チェック用セット
            Set<Date> hashSet = new HashSet<>();
            // CSV のレコードを DTO に変換する
            int recordCount = 2; // CSVファイルの行数にあわす為、2始まり
            while (csvParser.iterator().hasNext()) {
                DeliveryImpossibleDayCsvDto deliveryImpossibleDayCsvDto =
                                csvReaderModule.convertCsv2Dto(csvParser.iterator().next(),
                                                             DeliveryImpossibleDayCsvDto.class
                                                            );
                DeliveryImpossibleDayEntity deliveryImpossibleDayEntity =
                                createDeliveryImpossibleDayEntity(deliveryImpossibleDayCsvDto);

                // 年を設定
                deliveryImpossibleDayEntity.setYear(year);

                /* 登録処理 */
                if (regist(deliveryImpossibleDayEntity, recordCount, csvUploadResult, hashSet)) {
                    csvUploadResult.setRecordCount(recordCount);
                    return true;
                }

                recordCount++;
            }
            csvUploadResult.setRecordCount(recordCount);
        } catch (Exception e) {
            List<CsvUploadError> csvUploadErrorList = new ArrayList<>();
            csvUploadErrorList.add(createCsvUploadError(0, UNEXPECTED_EXCEPTION_MSG_CD, null));
            csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
            return true;
        }
        return false;
    }

    /**
     * 削除処理
     *
     * @param deliveryImpossibleDayDeleteEntity お届け不可日エンティティ
     * @return 削除件数
     */
    protected int delete(DeliveryImpossibleDayEntity deliveryImpossibleDayDeleteEntity) {
        // お届け不可日情報
        return deliveryImpossibleDayDeleteLogic.execute(deliveryImpossibleDayDeleteEntity);
    }

    /**
     * 登録処理
     *
     * @param deliveryImpossibleDayEntity お届け不可日エンティティ
     * @param recordCount                 レコード数
     * @param csvUploadResult             Csvアップロード結果Dto
     * @param hashSet                     CSV日付リスト
     * @return エラーの有無 true=エラー、false=エラーなし
     */
    protected boolean regist(DeliveryImpossibleDayEntity deliveryImpossibleDayEntity,
                             int recordCount,
                             CsvUploadResult csvUploadResult,
                             Set<Date> hashSet) {

        List<CsvUploadError> csvUploadErrorList = new ArrayList<>();

        // 年月日の年と、選択された年が異なる場合エラー
        LocalDate localDate =
                        deliveryImpossibleDayEntity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int deliveryEntityYear = localDate.getYear();
        if (deliveryEntityYear != deliveryImpossibleDayEntity.getYear()) {
            csvUploadErrorList.add(createCsvUploadError(recordCount, "SYH000203E", new Object[] {"年", "年月日"}));
            csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
            return true;
        }

        // 年月日の重複チェック
        if (!hashSet.add(deliveryImpossibleDayEntity.getDate())) {
            // エラーメッセージを作成・セット
            csvUploadErrorList.add(createCsvUploadError(recordCount, MSGCD_DATE_DUPLICATE_ERROR, null));
            csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
            return true;
        }

        // お届け不可日情報登録
        if (deliveryImpossibleDayRegistLogic.execute(deliveryImpossibleDayEntity) != 1) {
            // エラーメッセージを作成・セット
            csvUploadErrorList.add(createCsvUploadError(recordCount, MSGCD_REGIST_FAILURE, null));
            csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
            return true;
        }
        return false;
    }

    /**
     * CSVアップロードエラーDto作成
     *
     * @param recordCount レコード数
     * @param messageCode メッセージコード
     * @param args        引数
     * @return CSVアップロードエラーDto
     */
    protected CsvUploadError createCsvUploadError(Integer recordCount, String messageCode, Object[] args) {
        CsvUploadError csvUploadError = new CsvUploadError();
        csvUploadError.setRow(recordCount);
        csvUploadError.setMessageCode(messageCode);
        csvUploadError.setArgs(args);
        csvUploadError.setMessage(AppLevelFacesMessageUtil.getAllMessage(messageCode, args).getMessage());
        return csvUploadError;
    }

    /**
     * CSVからお届け不可日エンティティ作成
     *
     * @param deliveryImpossibleDayCsvDto CsvDto
     * @return お届け不可日エンティティ
     */
    protected DeliveryImpossibleDayEntity createDeliveryImpossibleDayEntity(DeliveryImpossibleDayCsvDto deliveryImpossibleDayCsvDto) {
        DeliveryImpossibleDayEntity deliveryImpossibleDayEntity = new DeliveryImpossibleDayEntity();
        deliveryImpossibleDayEntity.setDate(deliveryImpossibleDayCsvDto.getDate());
        deliveryImpossibleDayEntity.setReason(deliveryImpossibleDayCsvDto.getReason());
        deliveryImpossibleDayEntity.setDeliveryMethodSeq(deliveryImpossibleDayCsvDto.getDeliveryMethodSeq());
        return deliveryImpossibleDayEntity;
    }

    /**
     * csvValidationResult
     *
     * @param uploadedFile      アップロードファイル
     * @param csvUploadResult   CSVアップロード結果
     * @param deliveryMethodSeq 配送方法SEQ
     * @return バリデーション結果Dto
     */
    protected boolean validate(File uploadedFile, CsvUploadResult csvUploadResult, Integer deliveryMethodSeq) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(-1);

        try (CSVParser csvParser = csvReaderModule.readCsv(
                        uploadedFile.getPath(), DeliveryImpossibleDayCsvDto.class, csvReaderOptionDto)) {

            // CSVファイルにデータが存在しなかった場合
            if (csvParser == null || !csvParser.iterator().hasNext()) {
                List<CsvUploadError> csvUploadErrorList = new ArrayList<>();
                csvUploadErrorList.add(createCsvUploadError(1, UNEXPECTED_EXCEPTION_MSG_CD, null));
                csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
                return true;
            }

            // 処理件数はヘッダー行分を含めて1からカウント
            int recordCounter = 1;
            // バリデータエラーカウンタ
            int errorCounter = 0;

            while (csvParser.iterator().hasNext()) {
                recordCounter++;

                DeliveryImpossibleDayCsvDto csvDto = csvReaderModule.convertCsv2Dto(csvParser.iterator().next(),
                                                                                  DeliveryImpossibleDayCsvDto.class
                                                                                 );

                if (csvDto == null) {
                    errorCounter++;

                } else {
                    // 配送方法SEQのチェック
                    // ---------------------------------------------------------------------------------------
                    // 現行HIT-MALL3の実装方法だと、以下のケースならhelper.getValidationResult()が空だと思う
                    // ・CSV読み込みでエラー発生しない ⇒ バリデーション結果（ValidationResult）が空のハズ
                    // ・配送方法SEQの妥当性チェックでエラーが発生する ⇒ ValidationResultにエラー内容を登録しないといけない
                    //
                    // ValidationResultが空のため、csvUploadResultにセットするとしても意味がない
                    // Controllerで使われる csvUploadResult.isInValid() チェックで判定結果がfalseになるハズ
                    // ---------------------------------------------------------------------------------------
                    if (!csvDto.getDeliveryMethodSeq().equals(deliveryMethodSeq)) {
                        errorCounter++;
                        // 検証NG詳細リストにエラー内容を追加する
                        csvReaderModule.getCsvValidationResult()
                                     .getDetailList()
                                     .add(new InvalidDetail(recordCounter, "配送方法SEQ",
                                                            AppLevelFacesMessageUtil.getAllMessage(
                                                                                                    CsvReaderModule.UNEXPECTED_VALUE_MSG_CD, null)
                                                                                    .getMessage()
                                     ));
                    }
                }
            }
            // エラーあり
            if (errorCounter != 0) {
                csvUploadResult.setCsvValidationResult(csvReaderModule.getCsvValidationResult());
                csvUploadResult.setRecordCount(1); // ヘッダー行の1をセット
                csvUploadResult.setErrorCount(errorCounter);
                return true;
            }
        } catch (Exception e) {
            List<CsvUploadError> csvUploadErrorList = new ArrayList<>();
            csvUploadErrorList.add(createCsvUploadError(1, UNEXPECTED_EXCEPTION_MSG_CD, null));
            csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
            return true;
        }
        // エラーなし
        return false;
    }

}