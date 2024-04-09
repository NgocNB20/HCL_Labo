/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 新HIT-MALLのCSVファイル出力
 * 作成日：2021/07/26
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class CsvExtractUtility {

    private final Class<?> csvDtoClass;
    private final List<?> csvDtoList;
    private final CsvDownloadOptionDto optionDto;
    private final Stream<Object> resultStream;
    private final String filePath;

    /**
     * デフォルトコンストラクタ
     *
     * @param csvDtoClass
     * @param parameters
     */
    public CsvExtractUtility(Class<?> csvDtoClass, Object... parameters) {
        // 処理対象のCSVクラス
        this.csvDtoClass = csvDtoClass;
        // CSVダウンロードオプション
        CsvDownloadOptionDto optionDtoParam;
        optionDtoParam = (CsvDownloadOptionDto) parameters[0];
        if (optionDtoParam == null) {
            optionDtoParam = new CsvDownloadOptionDto();
        }
        this.optionDto = optionDtoParam;
        // CSVダウンロードデータ（Stream形式）
        if (parameters[1] != null) {
            this.resultStream = (Stream<Object>) parameters[1];
        } else {
            this.resultStream = null;
        }
        // 出力ファイルのパス
        this.filePath = (String) parameters[2];
        // CsvDtoListありの場合
        if (parameters.length > 3 && parameters[3] != null) {
            this.csvDtoList = (List<?>) parameters[3];
        } else {
            this.csvDtoList = null;
        }
    }

    /**
     * CSVファイル出力
     *
     * @return 件数
     * @throws IOException
     */
    public int outCsv() throws IOException {
        // CSVを出力するWRITER
        try (FileWriter csvWriter = new FileWriter(this.filePath, this.optionDto.getCharset())) {

            // Apache Common CSV を特化したCSVフォーマットを準備する
            // 主にHIT-MALL独自のCsvDownloadOptionDtoからCSVFormatに変換する
            CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(this.optionDto);

            // Apache Common CSV を特化したCSVプリンター（設定したCSVフォーマットに沿ってデータを出力）を初期化する
            CSVPrinter printer = new CSVPrinter(csvWriter, outputCsvFormat);

            // CSV出力形式のデフォルト設定をCSVDtoから生成
            Map<String, OptionContentDto> csvDownloadOption =
                            CsvOptionUtil.getDefaultMapOptionContentDto(this.csvDtoClass);

            // CSVヘッダーを出力する
            if (this.optionDto.isOutputHeader()) {
                printer.printRecord(CsvDownloadHandler.outHeader(this.csvDtoClass, csvDownloadOption));
                csvWriter.flush();
            }

            // 出力件数のカウンタ
            int cnt = 0;

            // CSVデータを1件ずつ出力する
            // Stream形式＆List形式の双方を対応する
            if (this.csvDtoList == null) {
                Iterator<Object> resultIterator = this.resultStream.iterator();
                while (resultIterator.hasNext()) {
                    printer.printRecord(CsvDownloadHandler.outCsvRecord(resultIterator.next(), csvDownloadOption));
                    csvWriter.flush();
                    cnt++;
                }
                // 処理終了後に、Streamをクローズする
                this.resultStream.close();
            } else {
                for (Object csvDto : this.csvDtoList) {
                    printer.printRecord(CsvDownloadHandler.outCsvRecord(csvDto, csvDownloadOption));
                    csvWriter.flush();
                    cnt++;
                }
            }
            printer.flush();
            return cnt;
        }
    }

}