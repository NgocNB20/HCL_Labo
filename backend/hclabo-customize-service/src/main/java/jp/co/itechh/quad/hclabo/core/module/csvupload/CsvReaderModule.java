/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.module.csvupload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jp.co.itechh.quad.hclabo.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.hclabo.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.hclabo.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.hclabo.core.base.utility.DateUtility;
import jp.co.itechh.quad.hclabo.core.base.utility.SupplyDateTimeUtility;
import jp.co.itechh.quad.hclabo.core.constant.type.EnumType;
import jp.co.itechh.quad.hclabo.core.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 新HIT-MALLのCSVアップロード機能
 * CSV読み込みのユーティリティクラス
 * 作成日：2021/05/26
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Component
public class CsvReaderModule {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReaderModule.class);

    // ---------------------------------------------------------------------------
    // 必要な定数を定義する
    // ---------------------------------------------------------------------------
    private static final String SERIAL_VERSION = "serialVersion";
    private static final String SUPPLY_DATE_TIME_FORMAT = DateUtility.YMD_SLASH_HMS;
    private static final String ENUM_TYPE_VALUE = "value";
    private static final String EMPTY_STRING = "";
    private static final Set<Class<?>> PRIMITIVE_NUMBERS =
                    Stream.of(int.class, long.class, float.class, double.class, byte.class, short.class)
                          .collect(Collectors.toSet());
    private static final String CONVERSION_UNEXPECTED_ERROR_MSG_CD = "CSV-UPLOAD-001-E";
    public static final String UNEXPECTED_VALUE_MSG_CD = "CSV-UPLOAD-002-E";
    private static final String UNEXPECTED_EXCEPTION_MSG_CD = "CSV-UPLOAD-003-E";

    // ---------------------------------------------------------------------------
    // エラーメッセージ内容
    // ---------------------------------------------------------------------------
    private String conversionUnexpectedErrorMsg;
    private String unexpectedValueMsg;
    private String unexpectedExceptionMsg;

    /** アップロードされたCSVのフィールドマップ */
    private Map<String, Field> csvDtoClassFieldMap;
    /** 対象CSVファイルのヘッダー有無のフラグ */
    private boolean isHasHeader;

    /** ユーティリティクラス */
    private final SupplyDateTimeUtility supplyDateTimeUtility;

    /** 登録されたコンバータをアプリケーションコンテキストから取得し、
     * バインダーにセットする
     */
    private final FormatterRegistry formatterRegistry;

    /** メッセージソース */
    private final MessageSource messageSource;

    /** バリデーター */
    private final Validator validator;

    /**
     *  コンストラクタ
     *
     */
    public CsvReaderModule() {
        // 必要なユーティリティクラスを取得する
        this.supplyDateTimeUtility = ApplicationContextUtility.getBean(SupplyDateTimeUtility.class);

        // 登録されたコンバータをアプリケーションコンテキストから取得し、バインダーにセットする
        this.formatterRegistry = ApplicationContextUtility.getBean(FormatterRegistry.class);

        // プロパティファイルからメッセージを取得する
        this.messageSource = ApplicationContextUtility.getBean(MessageSource.class);

        // バリデーター利用の宣言
        this.validator = ApplicationContextUtility.getBeanByName("csvReaderValidator", Validator.class);

        // ---------------------------------------------------------------------------
        // エラーメッセージ内容を定義ファイルから取得する
        // ---------------------------------------------------------------------------
        this.conversionUnexpectedErrorMsg =
                        AppLevelFacesMessageUtil.getAllMessage(CONVERSION_UNEXPECTED_ERROR_MSG_CD, null).getMessage();
        this.unexpectedValueMsg = AppLevelFacesMessageUtil.getAllMessage(UNEXPECTED_VALUE_MSG_CD, null).getMessage();
        this.unexpectedExceptionMsg =
                        AppLevelFacesMessageUtil.getAllMessage(UNEXPECTED_EXCEPTION_MSG_CD, null).getMessage();
    }

    /**
     * CSV読み込みの共通メソッド
     * ・STEP-1：Apache Common CSV ライブラリを利用してCSVファイルの読み込みを行う
     * ・STEP-2：読み込まれた内容からCsvDtoインスタンスを生成する
     * ・STEP-3：必要なコンバージョン及びバリデーションを行う
     *
     * @param targetFilePath
     * @param csvDtoClass
     * @param csvUploadResult
     * @param csvReaderOptionDto
     * @return
     * @throws IOException
     */
    public List<?> readCsv(File targetFilePath,
                           Class<?> csvDtoClass,
                           CsvUploadResult csvUploadResult,
                           CsvReaderOptionDto csvReaderOptionDto) throws IOException {
        // バリデータエラー限界値(行数)の取得
        int validLimit = csvReaderOptionDto.getValidLimit();
        // 処理件数はヘッダー行分を含めて1からカウント
        int recordCount = 1;
        // エラー件数（CSVデータの行ごとにカウント）
        int errorCount = 0;
        // Csvバリデーション結果
        CsvValidationResult csvValidationResult = new CsvValidationResult();

        // アップロードされたCSVのフィールドマップを取得する
        this.csvDtoClassFieldMap = getCsvDtoClassFieldMap(csvDtoClass);
        // ヘッダー有無のフラグを設定する
        this.isHasHeader = csvReaderOptionDto.isInputHeader();

        // ---------------------------------------------------------------------------
        // Apache Common CSV によるCSV読み込みを実行する
        // ---------------------------------------------------------------------------
        Reader reader;

        // ファイルが存在しない
        if (targetFilePath == null || !targetFilePath.exists()) {
            LOGGER.error("[ERROR] ファイルが存在しない " + (targetFilePath != null ? targetFilePath.getAbsolutePath() : ""));
            throw new IllegalArgumentException(
                            "[ERROR] ファイルが存在しない " + (targetFilePath != null ? targetFilePath.getAbsolutePath() : ""));
        }

        if (csvReaderOptionDto.getCharset() != null) {
            reader = Files.newBufferedReader(Paths.get(targetFilePath.getPath()), csvReaderOptionDto.getCharset());
        } else {
            // デフォルトで Charset = MS932 を設定する
            reader = Files.newBufferedReader(Paths.get(targetFilePath.getPath()), Charset.forName("MS932"));
        }

        // ヘッダー行の有無によってCSVFormatを設定する
        CSVFormat readerCsvFormat;
        if (csvReaderOptionDto.isInputHeader()) {
            readerCsvFormat = getReaderCsvFormatWithHeader(csvReaderOptionDto);
        } else {
            readerCsvFormat = getReaderCsvFormatWithoutHeader(csvReaderOptionDto);
        }

        try (CSVParser csvParser = new CSVParser(reader, readerCsvFormat)) {

            // ---------------------------------------------------------------------------
            // CSV内容を読み込んで、Apache Common CSV を特化したCSVRecordから対象CsvDtoに変換する
            // ---------------------------------------------------------------------------
            List<Object> resultCsvDtoList = new ArrayList<>();
            Object resultCsvDto;
            for (CSVRecord csvRecord : csvParser) {
                recordCount++;
                try {
                    // CsvDtoへのバインディングを行い、エラーがあった場合、nullで返却される
                    resultCsvDto = convertCsv2Dto(csvRecord, csvDtoClass, csvValidationResult, recordCount);
                    if (resultCsvDto == null) {
                        errorCount++;
                    } else {
                        resultCsvDtoList.add(resultCsvDto);
                    }
                } catch (Exception e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // 予想外のエラーが発生した場合
                    errorCount++;
                    // 検証NG詳細リストにエラー内容を追加する
                    csvValidationResult.getDetailList()
                                       .add(new InvalidDetail(recordCount, EMPTY_STRING, this.unexpectedValueMsg));
                }

                // バリデータエラー行数限界値の場合 終了
                if (validLimit != -1 && errorCount >= validLimit) {
                    // more表示フラグON
                    csvUploadResult.setValidLimitFlg(true);
                    // ヘッダー行の1をセット
                    recordCount = 1;
                    break;
                }
            }

            // 処理された件数をcsvUploadResultに設定する
            csvUploadResult.setRecordCount(recordCount);

            // エラー件数をcsvUploadResultに設定する
            csvUploadResult.setErrorCount(errorCount);

            // Csvバリデーション結果をcsvUploadResultに設定する
            csvUploadResult.setCsvValidationResult(csvValidationResult);

            return resultCsvDtoList;

        }
    }

    /**
     * Apache Common CSV を特化したCSVRecordからCsvDtoへ変換する
     * （コンバージョン＆バリデーション処理を含む）
     *
     * @param csvRecord
     * @param csvDtoClass
     * @param csvValidationResult
     * @param recordCount
     * @return 生成されたCsvDtoインスタンス
     */
    private Object convertCsv2Dto(CSVRecord csvRecord,
                                  Class<?> csvDtoClass,
                                  CsvValidationResult csvValidationResult,
                                  int recordCount) {
        // 以降の処理で必要な変数を宣言する
        Map<String, Object> csvDtoFieldValueMap = new HashMap<>();
        Field field;
        CsvColumn fieldAnnotation;
        String fieldNameEn;
        String fieldNameJp;
        String fieldValueStr;
        Object fieldValueObj;
        int fieldCount = 0;

        // CsvDtoオブジェクトのコンバージョンフラグ
        boolean formatFlag = true;

        // --------------------------------------------------------------------
        // 全フィールドをループし、バインディング〜コンバージョン〜バリデーション処理を行う
        // --------------------------------------------------------------------
        for (Map.Entry<String, Field> entry : this.csvDtoClassFieldMap.entrySet()) {
            // フィールド名称（英語）
            fieldNameEn = entry.getKey();
            // フィールド本体（フィールドの属性を全て持っている）
            field = entry.getValue();
            // フィールドに付いてあるCsvColumnアノテーション
            fieldAnnotation = field.getAnnotation(CsvColumn.class);
            // フィールド名称（日本語）
            fieldNameJp = fieldAnnotation.columnLabel();
            // フィールド値（CSVから読み込まれた値）
            if (StringUtils.isEmpty(fieldNameJp) || !this.isHasHeader) {
                fieldValueStr = csvRecord.get(fieldCount);
            } else {
                try {
                    fieldValueStr = csvRecord.get(fieldNameJp);
                } catch (IllegalArgumentException e) {
                    // 部分アップロードの場合は、アップロードされなかった項目に対しては、nullを設定
                    fieldValueStr = null;
                }
            }
            // フィールドのバインディング後のオブジェクト（データ型がString/Date/Integer等）
            fieldValueObj = null;

            // ---------------------------
            // 日付へのバインディングを行う
            // ---------------------------
            if (!StringUtils.isEmpty(fieldValueStr) && !StringUtils.isEmpty(fieldAnnotation.dateFormat())) {
                // 日付フォーマッタを宣言する
                SimpleDateFormat format;
                // 年月日時分秒フォーマットの補完が必要な場合
                if (!StringUtils.isEmpty(fieldAnnotation.supplyDateType())) {
                    fieldValueStr = supplyDateTimeUtility.supplyDateTime(fieldValueStr,
                                                                         fieldAnnotation.supplyDateType()
                                                                        );
                    // 時分秒補完を使っているため、「yyyy/MM/dd HH:mm:ss」のフォーマットでバインディングを行う
                    format = new SimpleDateFormat(SUPPLY_DATE_TIME_FORMAT);
                } else {
                    // 時分秒補完を使っていない場合は、アノテーションによる日付フォーマットでバインディングを行う
                    format = new SimpleDateFormat(fieldAnnotation.dateFormat());
                }
                format.setLenient(false);
                try {
                    fieldValueObj = format.parse(fieldValueStr);
                } catch (ParseException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // 日付への変換ができなかった場合
                    addErrorToCsvValidationResult(
                                    csvValidationResult, "HDateValidator.NOT_DATE_detail", recordCount, fieldNameJp);
                    return null;
                }
            }

            // ---------------------------
            // 数値へのバインディングを行う
            // ---------------------------
            if (!StringUtils.isEmpty(fieldValueStr) && isNumericType(field.getType())) {
                // CSVから読み込まれた値が数値であるかのチェック
                try {
                    fieldValueObj = NumberFormat.getInstance().parse(fieldValueStr);
                } catch (ParseException e) {
                    // 数値への変換ができなかった場合
                    addErrorToCsvValidationResult(csvValidationResult, "HNumberValidator.NOT_NUMBER_detail",
                                                  recordCount, fieldNameJp
                                                 );
                    return null;
                }
                // 数値のフォーマットが要求された場合、以下の処理を行う
                if (!StringUtils.isEmpty(fieldAnnotation.numberFormat())) {
                    DecimalFormat format = new DecimalFormat(fieldAnnotation.numberFormat());
                    try {
                        fieldValueObj = format.parse(fieldValueStr);
                    } catch (ParseException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        // 数値への変換ができなかった場合
                        addErrorToCsvValidationResult(csvValidationResult, "HNumberValidator.NOT_NUMBER_detail",
                                                      recordCount, fieldNameJp
                                                     );
                        return null;
                    }
                }
            }

            // ---------------------------
            // HTYPEへのバインディングを行う
            // ---------------------------
            if (!StringUtils.isEmpty(fieldAnnotation.enumOutputType())) {
                Class<? extends EnumType> htypeClass = (Class<? extends EnumType>) field.getType();
                EnumType enumType;
                if (ENUM_TYPE_VALUE.equals(fieldAnnotation.enumOutputType())) {
                    // 区分値からHTYPEオブジェクトへ変換する
                    enumType = EnumTypeUtil.getEnumFromValue(htypeClass, fieldValueStr);
                } else {
                    // ラベルからHTYPEオブジェクトへ変換する
                    enumType = EnumTypeUtil.getEnumFromLabel(htypeClass, fieldValueStr);
                }
                // HTYPE項目は、必ずnullでない訳ではないため、下記のチェックは一旦コメントアウトとする
                //                if (enumType != null) {
                //                    fieldValueObj = enumType;
                //                } else {
                //                    // HTYPEへの変換ができなかった場合
                //                    addErrorToCsvValidationResult(csvValidationResult, "HItemsValidator.INVALID_detail", recordCount, fieldNameJp);
                //                    return null;
                //                }
                // HTYPEへの変換ができなかった場合は、nullのままを設定する
                fieldValueObj = enumType;
            }

            // 普通のStringの場合、そのまま設定する
            if (!StringUtils.isEmpty(fieldValueStr) && fieldValueObj == null) {
                fieldValueObj = fieldValueStr;
            } else if (fieldAnnotation.isConvertToBlank()) {
                formatFlag = false;
                fieldValueObj = "";
            }

            // CsvDtoのフィールド値マップに登録する
            csvDtoFieldValueMap.put(fieldNameEn, fieldValueObj);

            // フィールドをカウントアップ
            fieldCount++;
        }

        // CsvDtoオブジェクトへフィールド値マップから変換する（CsvDtoインスタンスの生成）
        ObjectMapper mapper = new ObjectMapper();
        Object csvDtoResult;
        try {
            csvDtoResult = mapper.convertValue(csvDtoFieldValueMap, csvDtoClass);
        } catch (IllegalArgumentException e) {
            LOGGER.error("例外処理が発生しました", e);
            // CsvDtoインスタンスの生成ができなかった場合、検証NG詳細リストにエラー内容を追加する
            csvValidationResult.getDetailList()
                               .add(new InvalidDetail(recordCount, EMPTY_STRING, this.unexpectedValueMsg));
            return null;
        }

        // ----------------------------------------------
        // 生成されたCsvDtoオブジェクトのバリデーションを行う
        // ----------------------------------------------
        if (!validateCsvDto(csvDtoResult, csvValidationResult, recordCount)) {
            return null;
        }

        // ----------------------------------------------
        // 生成されたCsvDtoオブジェクトのコンバージョンを行う
        // ----------------------------------------------
        if (formatFlag) {
            if (!formatCsvDto(csvDtoResult, csvValidationResult, recordCount)) {
                return null;
            }
        }

        return csvDtoResult;
    }

    /**
     * CsvDtoオブジェクトのバリデーション
     * （HIT-MALLの独自バリデーター @HV を実行させるため）
     *
     * @param csvDtoObj
     * @param csvValidationResult
     * @param recordCount
     * @return バリデーション結果
     */
    private boolean validateCsvDto(Object csvDtoObj, CsvValidationResult csvValidationResult, int recordCount) {
        // バリデーションを実行する
        Set<ConstraintViolation<Object>> bindingResult = validator.validate(csvDtoObj);

        // バインディング結果を検証する
        if (!bindingResult.isEmpty()) {
            // エラー内容を整形する
            makeCsvValidationResult(bindingResult, csvValidationResult, recordCount);
            return false;
        }

        // バリデーションエラーがなかった場合
        return true;
    }

    /**
     * CsvDtoオブジェクトのコンバージョン
     * （HIT-MALLの独自コンバーター @HC を実行させるため）
     *
     * @param csvDtoObj
     * @param csvValidationResult
     * @param recordCount
     * @return コンバージョン結果
     */
    private boolean formatCsvDto(Object csvDtoObj, CsvValidationResult csvValidationResult, int recordCount) {
        // JavaオブジェクトからMapに変換する
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> objValueMap = mapper.convertValue(
                        csvDtoObj,
                        TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class)
                                                             );

        // バインディング処理をキックする
        MutablePropertyValues propertyValues = new MutablePropertyValues(objValueMap);
        DataBinder binder = new DataBinder(csvDtoObj);
        // 登録されたコンバータをアプリケーションコンテキストから取得し、バインダーにセットする
        ConversionService conversionService = (ConversionService) formatterRegistry;
        binder.setConversionService(conversionService);
        // 主に文字列の全角変換等を行うため、例えjava.sql.Timestampでエラーになるとしても無視する
        binder.setIgnoreInvalidFields(true);
        // コンバート処理を実行させる
        binder.bind(propertyValues);

        // コンバージョン結果を検証する
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            // エラー内容を整形する
            return !makeCsvValidationResult(bindingResult, csvValidationResult, recordCount);
        }

        // コンバージョンエラーがなかった場合
        return true;
    }

    /**
     * バインディング結果（Set型）をバリデーション結果（CsvValidationResult型）に反映させる
     *
     * @param bindingResult
     * @param csvValidationResult
     * @param recordCount
     */
    private void makeCsvValidationResult(Set<ConstraintViolation<Object>> bindingResult,
                                         CsvValidationResult csvValidationResult,
                                         int recordCount) {
        // エラーメッセージを取得する
        String message;
        InvalidDetail invalidDetail;
        String fieldNameJp;

        // エラーリストを繰り返して、エラーメッセージの整形を行う
        for (ConstraintViolation<Object> error : bindingResult) {
            fieldNameJp = this.csvDtoClassFieldMap.get(error.getPropertyPath().toString())
                                                  .getAnnotation(CsvColumn.class)
                                                  .columnLabel();
            if (!error.getMessage().startsWith("{")) {
                // Javaxバリデーションのデフォルトメッセージ
                message = error.getMessage();
            } else {
                // HIT-MALL独自バリデーション用のメッセージ
                message = messageSource.getMessage(
                                error.getMessageTemplate().substring(1, error.getMessageTemplate().length() - 1), null,
                                Locale.getDefault()
                                                  );
            }
            invalidDetail = new InvalidDetail(recordCount, fieldNameJp, message);
            csvValidationResult.getDetailList().add(invalidDetail);
        }
    }

    /**
     * バインディング結果（BindingResult型）をバリデーション結果（CsvValidationResult型）に反映させる
     *
     * @param bindingResult
     * @param csvValidationResult
     * @param recordCount
     * @return
     */
    private boolean makeCsvValidationResult(Errors bindingResult,
                                            CsvValidationResult csvValidationResult,
                                            int recordCount) {
        List<FieldError> bindingResultErrorList = bindingResult.getFieldErrors();
        InvalidDetail invalidDetail;
        String fieldNameJp;
        boolean isErrorType = false;

        // エラーリストを繰り返して、エラーメッセージの整形を行う
        for (FieldError error : bindingResultErrorList) {
            // java.sql.Timestampはデフォルトのjava.beans.PropertyEditorSupportが対応しないようなため、
            // バインドする時にいつもエラーが発生してしまっているので無視とする
            String defaultMessage = error.getDefaultMessage();
            if (StringUtils.isNotEmpty(defaultMessage) && defaultMessage.contains("java.sql.Timestamp")) {
                // 特に処理不要
                continue;
            } else {
                // java.sql.Timestamp以外のエラーならエラーメッセージの出力を行う必要がある
                isErrorType = true;
                // エラー内容の設定
                fieldNameJp = this.csvDtoClassFieldMap.get(error.getField())
                                                      .getAnnotation(CsvColumn.class)
                                                      .columnLabel();
                invalidDetail = new InvalidDetail(recordCount, fieldNameJp, this.conversionUnexpectedErrorMsg);
                csvValidationResult.getDetailList().add(invalidDetail);
            }
        }

        return isErrorType;
    }

    /**
     * 検証NG詳細リストにエラー内容を追加する
     *
     * @param csvValidationResult
     * @param msgId
     * @param recordCount
     * @param fieldNameJp
     */
    private void addErrorToCsvValidationResult(CsvValidationResult csvValidationResult,
                                               String msgId,
                                               int recordCount,
                                               String fieldNameJp) {
        // エラーメッセージを取得する
        String message = messageSource.getMessage(msgId, null, Locale.getDefault());
        // 検証NG詳細リストにエラー内容を追加する
        csvValidationResult.getDetailList().add(new InvalidDetail(recordCount, fieldNameJp, message));
    }

    /**
     * Apache Common CSV を特化した 読み込み用のCSVFormatを設定する
     * （ヘッダー行がある場合）
     *
     * @return CSVFormat
     */
    private CSVFormat getReaderCsvFormatWithHeader(CsvReaderOptionDto csvReaderOptionDto) {
        return getReaderCsvFormatWithoutHeader(csvReaderOptionDto).withFirstRecordAsHeader();
    }

    /**
     * Apache Common CSV を特化した 読み込み用のCSVFormatを設定する
     * （ヘッダー行がない場合）
     *
     * @return CSVFormat
     */
    private CSVFormat getReaderCsvFormatWithoutHeader(CsvReaderOptionDto csvReaderOptionDto) {
        CSVFormat readerCsvFormat;
        if (csvReaderOptionDto.isForceQuote()) {
            readerCsvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase()
                                               .withTrim(csvReaderOptionDto.isTrim())
                                               .withQuote(csvReaderOptionDto.getQuoteCharacter())
                                               .withRecordSeparator(csvReaderOptionDto.getLineSeparator())
                                               .withQuoteMode(QuoteMode.ALL);
        } else {
            readerCsvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase()
                                               .withTrim(csvReaderOptionDto.isTrim())
                                               .withQuote(csvReaderOptionDto.getQuoteCharacter())
                                               .withRecordSeparator(csvReaderOptionDto.getLineSeparator())
                                               .withQuoteMode(QuoteMode.MINIMAL);
        }
        return readerCsvFormat;
    }

    /**
     * アップロードされたCSVのフィールドマップを取得する
     * Key:フィールドの英語名称
     * Value:フィールド情報
     *
     * @param csvDtoClass
     * @return フィールドマップ
     */
    private Map<String, Field> getCsvDtoClassFieldMap(Class<?> csvDtoClass) {
        Map<String, Field> csvDtoClassFieldMap = new LinkedHashMap<>();
        List<Field> fields = getFieldList(csvDtoClass);
        CsvColumn csvColumn;
        for (Field field : fields) {
            // serialVersionUIDは処理しない
            if (field.getName().toUpperCase().contains(SERIAL_VERSION.toUpperCase())) {
                continue;
            }
            csvColumn = field.getAnnotation(CsvColumn.class);
            // @CsvColumnアノテーションが付いている項目しか対応しない
            if (csvColumn != null && !csvColumn.isOnlyDownload()) {
                csvDtoClassFieldMap.put(field.getName(), field);
            }
        }
        return csvDtoClassFieldMap;
    }

    /**
     * アップロードされたCSVのフィールドマップを取得する（日本語名称⇔英語名称）
     * Key:フィールドの日本語名称
     * Value:フィールドの英語名称
     *
     * @param csvDtoClass
     * @return フィールドマップ
     */
    private Map<String, String> getCsvDtoClassFieldNameMap(Class<?> csvDtoClass) {
        Map<String, String> csvDtoClassFieldNameMap = new HashMap<>();
        List<Field> fields = getFieldList(csvDtoClass);
        CsvColumn csvColumn;
        for (Field field : fields) {
            // serialVersionUIDは処理しない
            if (field.getName().toUpperCase().contains(SERIAL_VERSION.toUpperCase())) {
                continue;
            }
            csvColumn = field.getAnnotation(CsvColumn.class);
            // @CsvColumnアノテーションが付いている項目しか対応しない
            if (csvColumn != null && !csvColumn.isOnlyDownload()) {
                csvDtoClassFieldNameMap.put(csvColumn.columnLabel(), field.getName());
            }
        }
        return csvDtoClassFieldNameMap;
    }

    /**
     * CsvDtoクラスからCSVヘッダーを取得する
     *
     * @param csvDtoClass
     * @return
     */
    public List<String> getCsvHeader(Class<?> csvDtoClass) {
        List<String> csvHeader = new ArrayList<>();
        List<Field> fields = getFieldList(csvDtoClass);
        CsvColumn csvColumn;
        String columnHeader;
        for (Field field : fields) {
            // serialVersionUIDは処理しない
            if (field.getName().toUpperCase().contains(SERIAL_VERSION.toUpperCase())) {
                continue;
            }
            csvColumn = field.getAnnotation(CsvColumn.class);
            // @CsvColumnアノテーションが付いている項目しか対応しない
            if (csvColumn != null && !csvColumn.isOnlyDownload()) {
                columnHeader = csvColumn.columnLabel();
                csvHeader.add(columnHeader);
            }
        }
        return csvHeader;
    }

    /**
     * アップロードされたCSVのヘッダー行の取得
     * @param uploadedCsvFile
     * @param csvReaderOptionDto
     * @return
     */
    public List<String> getUploadedCsvHeader(File uploadedCsvFile, CsvReaderOptionDto csvReaderOptionDto) {
        try {
            // Apache Common CSV のリーダーの宣言
            try (Reader reader = Files.newBufferedReader(
                            Paths.get(uploadedCsvFile.getPath()), csvReaderOptionDto.getCharset())) {
                // Apache Common CSV のCSVフォーマッターの宣言
                CSVFormat readerCsvFormat = getReaderCsvFormatWithHeader(csvReaderOptionDto);
                // Apache Common CSV のパーサーの宣言
                CSVParser csvParser = new CSVParser(reader, readerCsvFormat);
                // アップロードされたCSVのヘッダー行の取得
                return csvParser.getHeaderNames();
            }
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            return new ArrayList<>();
        }
    }

    /**
     * アップロードされたCSVのヘッダー行の取得（英語名へ変換）
     * @param uploadedCsvFile
     * @param csvReaderOptionDto
     * @return
     */
    public List<String> getUploadedCsvHeaderEng(Class<?> csvDtoClass,
                                                File uploadedCsvFile,
                                                CsvReaderOptionDto csvReaderOptionDto) {
        Map<String, String> csvDtoClassFieldNameMap = getCsvDtoClassFieldNameMap(csvDtoClass);
        List<String> uploadedCsvHeader = getUploadedCsvHeader(uploadedCsvFile, csvReaderOptionDto);
        List<String> uploadedCsvHeaderEng = new ArrayList<>();
        for (String fieldNameJp : uploadedCsvHeader) {
            uploadedCsvHeaderEng.add(csvDtoClassFieldNameMap.get(fieldNameJp));
        }
        return uploadedCsvHeaderEng;
    }

    /**
     * 対象のCSVDtoクラスの全フィールドをList形式で取得する
     *
     * @param csvDtoClass
     * @return フィールドリスト
     */
    private List<Field> getFieldList(Class<?> csvDtoClass) {
        return Arrays.asList(csvDtoClass.getDeclaredFields());
    }

    /**
     * フィールドのデータ型が数値であるかの検証
     *
     * @param clazz
     * @return boolean
     */
    private boolean isNumericType(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return PRIMITIVE_NUMBERS.contains(clazz);
        } else {
            return Number.class.isAssignableFrom(clazz);
        }
    }

    /**
     * CSV読み込みで有り得ない例外が発生した場合のエラーメッセージを生成する
     * （入力データ型がCsvUploadResult）
     *
     * @param csvUploadResult
     */
    public void createUnexpectedExceptionMsg(CsvUploadResult csvUploadResult) {
        List<CsvUploadError> csvUploadErrorList = new ArrayList<>();
        CsvUploadError csvUploadError = new CsvUploadError();
        csvUploadError.setRow(1); // ヘッダー行の1をセット
        csvUploadError.setMessage(this.unexpectedExceptionMsg);
        csvUploadErrorList.add(csvUploadError);
        csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
    }

    /**
     * CSV読み込みで有り得ない例外が発生した場合のエラーメッセージを生成する
     * （入力データ型がCsvValidationResult）
     *
     * @param csvValidationResult
     */
    public void createUnexpectedExceptionMsg(CsvValidationResult csvValidationResult) {
        // 行目（row）はヘッダー行として1をセット
        csvValidationResult.getDetailList().add(new InvalidDetail(1, EMPTY_STRING, this.unexpectedExceptionMsg));
    }

    /**
     * アップロードされたファイルの検証を行う
     *
     * @param csvFile               ファイル
     * @param counts                返却用レコード件数
     * @param maximumErrorDetection 検出するエラーの最大数
     * @return バリデーション結果
     */
    public <T> CsvValidationResult validateCSV(File csvFile,
                                               List<Integer> counts,
                                               int maximumErrorDetection,
                                               List<T> csvDtoList,
                                               Class<?> csvDtoClass) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(maximumErrorDetection);

        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();

        try {
            // CSVの全行分の DTO を作成してみる
            csvDtoList.addAll((List<T>) this.readCsv(csvFile, csvDtoClass, csvUploadResult, csvReaderOptionDto));

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            // CSV読み込みで有り得ない例外が発生した場合
            CsvValidationResult csvValidationResult = new CsvValidationResult();
            this.createUnexpectedExceptionMsg(csvValidationResult);
            return csvValidationResult;
        }

        // 出荷登録データ件数をセット (ヘッダー行分を-1している)
        counts.add(csvUploadResult.getRecordCount() - 1);

        return csvUploadResult.getCsvValidationResult();
    }

}