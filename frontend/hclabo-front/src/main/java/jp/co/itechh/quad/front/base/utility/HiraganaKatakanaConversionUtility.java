/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.base.utility;

import org.springframework.stereotype.Component;

/**
 * カタカナ ⇔ ひらがな の変換を行うヘルパークラス
 * <pre>
 * 文字コード上ひらがなとカタカナは以下の【文字一覧】のように対応する並びになっており
 * それを利用して変換処理を行う。
 *
 * 【文字一覧】
 * -------------------------------------------------------------------
 * ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞ
 * ただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽ
 * まみむめもゃやゅゆょよらりるれろゎわゐゑをん
 *
 * ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾ
 * タダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポ
 * マミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵヶ
 * -------------------------------------------------------------------
 *
 * ユニコードのコードポイントを10進数で表すと以下のようになる。
 * ぁ = 12353, あ = 12354, ぃ = 12355, い = 12356, ・・・以降1ずつ増える   　
 * ァ = 12449, ア = 12450, ィ = 12451, イ = 12452, ・・・以降1ずつ増える
 *
 * つまり、コードポイントを±96することで変換できることになる。
 *
 * ただし、カタカナの最後の3文字「ヴ」「ヵ」「ヶ」に関しては対応するひらがなが無いため
 * 当クラスでは変換対象外とする。
 * あえて変換するとすれば以下のような感じなるか。。。
 * ・ヴ -> う゛
 * ・ヵ -> か
 * ・ヶ -> け
 * </pre>
 *
 * @author negishi
 *
 */
@Component
public class HiraganaKatakanaConversionUtility {

    /**
     * ひらがな -> カタカナ に変換する
     *
     * @param hiraganaString 変換したい文字列
     * @return 変換後の文字列
     */
    public String toKatakana(String hiraganaString) {
        StringBuilder sb = new StringBuilder(hiraganaString);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c >= 'ぁ' && c <= 'ん') {
                sb.setCharAt(i, (char) (c - 'ぁ' + 'ァ'));
            }
        }
        return sb.toString();
    }

    /**
     * カタカナ -> ひらがな に変換する
     *
     * @param katakanaString 変換したい文字列
     * @return 変換後の文字列
     */
    public String toHiragana(String katakanaString) {
        StringBuilder sb = new StringBuilder(katakanaString);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            // 「ヴ」「ヵ」「ヶ」は変換対象外にするため、「ン」まで
            if (c >= 'ァ' && c <= 'ン') {
                sb.setCharAt(i, (char) (c - 'ァ' + 'ぁ'));
            }
        }
        return sb.toString();
    }

}
