/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.handler.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.core.constant.type.EnumType;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 新HIT-MALLのCSVダウンロード機能
 * CSVダウンロードハンドラー
 * 作成日：2021/04/12
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class CsvDownloadHandler {

    public static final String DEFAULT_FILE_NAME = "download_file";
    private static final String BLANK = "";
    private static final String SERIAL_VERSION = "serialVersion";
    private static final String HTYPE = "HType";
    private static final String ENUM_OUTPUT_TYPE_LABEL = "label";

    /**
     * Apache Common CSV を特化したCSVフォーマットを準備する
     * 主にHIT-MALLM独自のCsvDownloadOptionDtoからApache Common CSVライブラリのCSVFormatに変換する
     *
     * @param optionDto
     * @return
     */
    public static CSVFormat csvFormatBuilder(CsvDownloadOptionDto optionDto) {
        CSVFormat outputCsvFormat;
        if (optionDto.isForceQuote()) {
            outputCsvFormat = CSVFormat.DEFAULT.withQuote(optionDto.getQuoteCharacter())
                                               .withEscape(optionDto.getEscapeCharacter())
                                               .withRecordSeparator(optionDto.getLineSeparator())
                                               .withQuoteMode(QuoteMode.ALL);
        } else {
            outputCsvFormat = CSVFormat.DEFAULT.withQuote(optionDto.getQuoteCharacter())
                                               .withEscape(optionDto.getEscapeCharacter())
                                               .withRecordSeparator(optionDto.getLineSeparator())
                                               .withQuoteMode(QuoteMode.MINIMAL);
        }
        return outputCsvFormat;
    }

    /**
     * 出力するCSVヘッダーを生成する
     * （Apache Common CSV を特化したCSVプリンター用の出力コンテンツをList形式で生成する）
     *
     * @param csvDtoClass
     * @param optionContentDtoMap
     * @return
     */
    public static List<String> outHeader(Class<?> csvDtoClass, Map<String, OptionContentDto> optionContentDtoMap) {
        return getCsvHeader(csvDtoClass, optionContentDtoMap);
    }

    /**
     * 出力するCSVデータを生成する
     * （Apache Common CSV を特化したCSVプリンター用の出力コンテンツをList形式で生成する）
     *
     * @param csvDto
     * @param optionContentDtoMap
     * @return
     */
    public static List<String> outCsvRecord(Object csvDto, Map<String, OptionContentDto> optionContentDtoMap) {
        return getRecordContent(csvDto, optionContentDtoMap);
    }

    /**
     * 出力対象のCSVDtoクラスの全フィールドをList形式で取得する
     *
     * @param csvDtoClass
     * @return
     */
    private static List<Field> getFieldList(Class<?> csvDtoClass) {
        return Arrays.asList(csvDtoClass.getDeclaredFields());
    }

    /**
     * 出力するCSVヘッダーを生成する（メイン処理のメソッド）
     *
     * @param csvDtoClass
     * @return
     */
    private static List<String> getCsvHeader(Class<?> csvDtoClass, Map<String, OptionContentDto> optionContentDtoMap) {
        List<String> csvHeader = new ArrayList<>();
        TreeMap<Integer, String> csvHeaderSorted = new TreeMap<>();
        List<Field> fields = getFieldList(csvDtoClass);
        for (Field field : fields) {
            // serialVersionUIDは処理しない
            if (field.getName().toUpperCase().contains(SERIAL_VERSION.toUpperCase())) {
                continue;
            }

            // @CsvColumnアノテーションが付いている項目しか出力しない
            if (optionContentDtoMap.containsKey(field.getName()) && optionContentDtoMap.get(field.getName())
                                                                                       .isOutFlag()) {
                if (StringUtils.isNotEmpty(optionContentDtoMap.get(field.getName()).getColumnLabel())) {
                    csvHeaderSorted.put(
                                    optionContentDtoMap.get(field.getName()).getOrder(),
                                    optionContentDtoMap.get(field.getName()).getColumnLabel()
                                       );
                } else {
                    csvHeaderSorted.put(
                                    optionContentDtoMap.get(field.getName()).getOrder(),
                                    optionContentDtoMap.get(field.getName()).getDefaultColumnLabel()
                                       );

                }
            }
        }
        // 出力順序に基づいて出力する
        for (Map.Entry<Integer, String> entry : csvHeaderSorted.entrySet()) {
            csvHeader.add(entry.getValue());
        }
        return csvHeader;
    }

    /**
     * CSVDtoのフィールドごとに、出力ヘッダーをCsvColumnアノテーションの属性によってフォーマットする
     *
     * @param field
     * @return
     */
    private static String translateHeader(Field field) {
        CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
        if (csvColumn != null) {
            // @CsvColumnアノテーションが付いている場合は、必ずNOT-BLANKのcolumnLabelを設定する前提
            return csvColumn.columnLabel();
        }
        // @CsvColumnアノテーションが付いていない場合はNULLで返却
        return null;
    }

    /**
     * 出力するCSVデータを生成する（メイン処理のメソッド）
     *
     * @param csvDto
     * @return
     */
    private static List<String> getRecordContent(Object csvDto, Map<String, OptionContentDto> optionContentDtoMap) {
        List<String> recordContent = new ArrayList<>();
        TreeMap<Integer, String> csvRecordContentSorted = new TreeMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> csvDtoMap = objectMapper.convertValue(csvDto, Map.class);
        List<Field> csvDtoFieldList = getFieldList(csvDto.getClass());
        String fieldTranslatedValue;
        for (Field field : csvDtoFieldList) {
            // serialVersionUIDは処理しない
            if (field.getName().toUpperCase().contains(SERIAL_VERSION.toUpperCase())) {
                continue;
            }

            // 出力しない項目であればスキップする
            if (!optionContentDtoMap.containsKey(field.getName()) || !optionContentDtoMap.get(field.getName())
                                                                                         .isOutFlag()) {
                continue;
            }

            fieldTranslatedValue = translateField(field, csvDtoMap.get(field.getName()));
            // @CsvColumnアノテーションが付いている項目しか出力しない
            if (fieldTranslatedValue != null) {
                csvRecordContentSorted.put(optionContentDtoMap.get(field.getName()).getOrder(), fieldTranslatedValue);
            }
        }
        // 出力順序に基づいて出力する
        for (Map.Entry<Integer, String> entry : csvRecordContentSorted.entrySet()) {
            recordContent.add(entry.getValue());
        }
        return recordContent;
    }

    /**
     * CSVDtoのフィールドごとに、出力データをCsvColumnアノテーションの属性によってフォーマットする
     * ・日付フォーマット
     * ・数値フォーマット
     * ・HTYPEフォーマット
     *
     * @param field
     * @param fieldValue
     * @return
     */
    private static String translateField(Field field, Object fieldValue) {
        String returnValue;
        CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
        if (csvColumn != null) {
            // フィールド値がNULLの場合、空白で出力する
            if (fieldValue == null) {
                return BLANK;
            }
            // 日付フォーマットの適用
            if (!StringUtils.isEmpty(csvColumn.dateFormat())) {
                // 日付項目は必ずdateFormatを設定する前提
                Date date = new Date(Long.parseLong(fieldValue.toString()));
                SimpleDateFormat format = new SimpleDateFormat(csvColumn.dateFormat());
                format.setLenient(false);
                returnValue = format.format(date);
                return returnValue;
            }
            // 数値フォーマットの適用
            if (!StringUtils.isEmpty(csvColumn.numberFormat()) && fieldValue instanceof BigDecimal) {
                DecimalFormat format = new DecimalFormat(csvColumn.numberFormat());
                returnValue = format.format(fieldValue);
                return returnValue;
            }
            // HTYPEフォーマットの適用
            if (field.getType().getName().toUpperCase().contains(HTYPE.toUpperCase())) {
                Class<? extends EnumType> htypeClass = (Class<? extends EnumType>) field.getType();
                EnumType enumType = EnumTypeUtil.getEnumFromName(htypeClass, fieldValue.toString());
                if (enumType == null) {
                    // DB上のデータの問題だけ、本番環境では発生しない想定
                    return "NOT_FOUND_HTYPE";
                }
                if (ENUM_OUTPUT_TYPE_LABEL.equals(csvColumn.enumOutputType())) {
                    returnValue = enumType.getLabel();
                } else {
                    returnValue = enumType.getValue();
                }
                return returnValue;
            }

            // それ以外のデータタイプ or フォーマットしない場合は、単純に文字列に変換し出力する
            returnValue = fieldValue.toString();
            return returnValue;
        }
        // @CsvColumnアノテーションが付いていない場合はNULLで返却
        return null;
    }

    /**
     * HTTPレスポンスのヘッダー情報を生成する
     *
     * @param fileName
     * @return
     */
    public static HttpHeaders getHttpHeaders(String fileName) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        // CSVダウンロード時にLogFilterを通さないようにするため、responseTypeをレスポンスヘッダーに設定
        responseHeaders.set("responseType", "Async");
        return responseHeaders;
    }

}