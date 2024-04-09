/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.dto.csv;

import lombok.Data;

import java.nio.charset.Charset;

/**
 * 新HIT-MALLのCSV読み込み機能
 * CSV読み込みオプションDTO
 * 作成日：2021/06/11
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
public class CsvReaderOptionDto {

    /** 改行文字。 */
    private String lineSeparator;

    /** 区切り文字。 */
    private String columnSeparator;

    /** 全カラムをダブルクォーテーションで括る場合のフラグ。 */
    private boolean forceQuote;

    /** 出力文字コード。 */
    private Charset charset;

    /** ヘッダ行出力の可否。 */
    private boolean inputHeader;

    /** クォート文字。 */
    private Character quoteCharacter;

    /** トリム */
    private boolean trim;

    /** バリデータエラー限界値(行数)。 */
    int validLimit;

    /**
     * フルコンストラクタ
     *
     * デフォルト CSV ダウンロードオプション
     */
    public CsvReaderOptionDto() {
        this("\r\n", ",", false, Charset.forName("MS932"), true, '"', false);
    }

    /**
     * フルコンストラクタ
     *
     * @param lineSeparator 行区切りに使用する文字
     * @param columnSeparator カラム区切りに使用する文字
     * @param forceQuote カラムを強制的クォーティングするかどうか
     * @param charset 出力文字セット
     * @param inputHeader ヘッダ行入力の有無
     * @param quoteCharacter 引用符に使用する文字
     */
    public CsvReaderOptionDto(String lineSeparator,
                              String columnSeparator,
                              boolean forceQuote,
                              Charset charset,
                              boolean inputHeader,
                              Character quoteCharacter,
                              boolean trim) {
        super();
        this.setLineSeparator(lineSeparator);
        this.setColumnSeparator(columnSeparator);
        this.setForceQuote(forceQuote);
        this.setCharset(charset);
        this.setInputHeader(inputHeader);
        this.setQuoteCharacter(quoteCharacter);
        this.setTrim(trim);
    }

}