/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.thymeleaf;

import org.apache.commons.lang3.StringUtils;

/**
 * html の pre タグを使用せず同等の表示を行えるようにするためのコンバータ<br/>
 * カスタムユーティリティオブジェク用Utility<br/>
 *
 * @author kimura
 */
public class PreConverterViewUtil {

    /**
     * 改行や空白文字を含む文字列を HTML として出力する
     *
     * @param value
     * @return 変換後の値
     */
    public String convert(final String value) {

        return convertToView(value, 8, true);
    }

    /**
     * 改行や空白文字を含む文字列を HTML として出力する<br/>
     * 「タブ展開サイズ」と「タグのエスケープ」を設定可
     *
     * @param value
     * @param tabSize タブ展開サイズ
     * @param escape  タブをエスケープするか
     * @return 変換後の値
     */
    public String convert(final String value, final int tabSize, final boolean escape) {

        return convertToView(value, tabSize, escape);
    }

    /**
     * viewへの変換処理
     *
     * @param value
     * @param tabSize
     * @param escape
     * @return 変換後の値
     */
    private String convertToView(String value, int tabSize, boolean escape) {

        // 変換する必要がない場合
        if (StringUtils.isEmpty(value)) {
            return "";
        }

        String preString = value.toString();
        if (escape) {
            preString = preString.replaceAll("&", "&amp;");
            preString = preString.replaceAll("<", "&lt;");
            preString = preString.replaceAll(">", "&gt;");
            preString = preString.replaceAll("\t", createTabReplacer(tabSize));
            preString = preString.replaceAll(" ", "&nbsp;");
        }

        preString = preString.replaceAll("(\\r\\n|\\r|\\n)", "<br />");

        return preString;
    }

    /**
     * タブを指定文字数の空白文字で生成<br/>
     * 0以下の場合、空文字を返す
     *
     * @param size サイズ
     * @return tabReplacer
     */
    private String createTabReplacer(int size) {

        String tabReplacer = "";
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                tabReplacer += " ";
            }
        }
        return tabReplacer;
    }
}