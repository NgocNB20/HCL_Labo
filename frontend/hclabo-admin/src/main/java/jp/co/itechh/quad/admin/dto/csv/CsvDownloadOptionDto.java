/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.csv;

import lombok.Data;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * 新HIT-MALLのCSVダウンロード機能
 * ダウンロードオプションDTO
 * 作成日：2021/04/12
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
public class CsvDownloadOptionDto {

    /** 改行文字。 */
    private String lineSeparator;

    /** 区切り文字。 */
    private String columnSeparator;

    /** 全カラムをダブルクォーテーションで括る場合のフラグ。 */
    private boolean forceQuote;

    /** 出力文字コード。 */
    private Charset charset;

    /** ヘッダ行出力の可否。 */
    private boolean outputHeader;

    /** クォート文字。 */
    private Character quoteCharacter;

    /** 特殊なエスケープ文字。 */
    private Character escapeCharacter;

    /** Microsoft Excel 用出力かどうか */
    private boolean msExcelExpression;

    /** 旧来のMicrosoft Excel 用出力かどうか */
    private boolean oldMsExcelExpression;

    /** 出力対象外項目一覧 */
    private List<String> omitFieldsName;

    /** ヘッダー上書きの項目一覧 */
    private Map<String, String> headerOverrideList;

    /**
     * コンストラクタ
     *
     * デフォルト CSV ダウンロードオプション
     */
    public CsvDownloadOptionDto() {
        this('"', ",", "\r\n", null, false, Charset.forName("MS932"), false, false, false);
    }

    /**
     * フルコンストラクタ
     *
     * @param quoteCharacter 引用符に使用する文字
     * @param colSeparator カラム区切りに使用する文字
     * @param lineSeparator 行区切りに使用する文字
     * @param forceQuote カラムを強制的クォーティングするかどうか
     * @param charset 出力文字セット
     * @param header ヘッダ行出力の有無
     * @param msExcel MS-Excel 形式での出力有無
     * @param oldMsExcel MS-Excel 形式(旧仕様)での出力有無
     */
    public CsvDownloadOptionDto(final Character quoteCharacter,
                                final String colSeparator,
                                final String lineSeparator,
                                final Character escapeCharacter,
                                final boolean forceQuote,
                                final Charset charset,
                                final boolean header,
                                final boolean msExcel,
                                final boolean oldMsExcel) {
        super();
        this.setQuoteCharacter(quoteCharacter);
        this.setColumnSeparator(colSeparator);
        this.setLineSeparator(lineSeparator);
        this.setEscapeCharacter(escapeCharacter);
        this.setForceQuote(forceQuote);
        this.setCharset(charset);
        this.setOutputHeader(header);
        this.setMsExcelExpression(msExcel);
        this.setOldMsExcelExpression(oldMsExcel);
        this.setOmitFieldsName(null);
        this.setHeaderOverrideList(null);
    }

}