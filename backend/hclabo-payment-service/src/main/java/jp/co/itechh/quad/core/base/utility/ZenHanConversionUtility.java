/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 全角、半角の変換を行うヘルパークラス
 *
 * @author negishi
 * @author tomo (itec) 2012/02/21 #2754
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更
 */
@Component
public class ZenHanConversionUtility {

    /**
     * コンストラクタ
     */
    public ZenHanConversionUtility() {
    }

    /**
     * 半角から全角にするためのマップ
     */
    protected static final Map<Object, Object> HANZEN_MAP = new HashMap<>();

    /**
     * 全角から半角にするためのマップ
     */
    protected static final Map<Object, Object> ZENHAN_MAP = new HashMap<>();

    /**
     * JISX0208 - Windows-31J で差分がある特定の文字は、置換を行うマップ
     */
    protected static final Map<String, String> REPLACE_MAP = new HashMap<>();

    static {
        synchronized (HANZEN_MAP) {
            if (HANZEN_MAP.isEmpty()) {
                // 全角変換用のマッピング情報を作成
                HANZEN_MAP.put("0", "０");
                HANZEN_MAP.put("1", "１");
                HANZEN_MAP.put("2", "２");
                HANZEN_MAP.put("3", "３");
                HANZEN_MAP.put("4", "４");
                HANZEN_MAP.put("5", "５");
                HANZEN_MAP.put("6", "６");
                HANZEN_MAP.put("7", "７");
                HANZEN_MAP.put("8", "８");
                HANZEN_MAP.put("9", "９");
                HANZEN_MAP.put("a", "ａ");
                HANZEN_MAP.put("b", "ｂ");
                HANZEN_MAP.put("c", "ｃ");
                HANZEN_MAP.put("d", "ｄ");
                HANZEN_MAP.put("e", "ｅ");
                HANZEN_MAP.put("f", "ｆ");
                HANZEN_MAP.put("g", "ｇ");
                HANZEN_MAP.put("h", "ｈ");
                HANZEN_MAP.put("i", "ｉ");
                HANZEN_MAP.put("j", "ｊ");
                HANZEN_MAP.put("k", "ｋ");
                HANZEN_MAP.put("l", "ｌ");
                HANZEN_MAP.put("m", "ｍ");
                HANZEN_MAP.put("n", "ｎ");
                HANZEN_MAP.put("o", "ｏ");
                HANZEN_MAP.put("p", "ｐ");
                HANZEN_MAP.put("q", "ｑ");
                HANZEN_MAP.put("r", "ｒ");
                HANZEN_MAP.put("s", "ｓ");
                HANZEN_MAP.put("t", "ｔ");
                HANZEN_MAP.put("u", "ｕ");
                HANZEN_MAP.put("v", "ｖ");
                HANZEN_MAP.put("w", "ｗ");
                HANZEN_MAP.put("x", "ｘ");
                HANZEN_MAP.put("y", "ｙ");
                HANZEN_MAP.put("z", "ｚ");
                HANZEN_MAP.put("A", "Ａ");
                HANZEN_MAP.put("B", "Ｂ");
                HANZEN_MAP.put("C", "Ｃ");
                HANZEN_MAP.put("D", "Ｄ");
                HANZEN_MAP.put("E", "Ｅ");
                HANZEN_MAP.put("F", "Ｆ");
                HANZEN_MAP.put("G", "Ｇ");
                HANZEN_MAP.put("H", "Ｈ");
                HANZEN_MAP.put("I", "Ｉ");
                HANZEN_MAP.put("J", "Ｊ");
                HANZEN_MAP.put("K", "Ｋ");
                HANZEN_MAP.put("L", "Ｌ");
                HANZEN_MAP.put("M", "Ｍ");
                HANZEN_MAP.put("N", "Ｎ");
                HANZEN_MAP.put("O", "Ｏ");
                HANZEN_MAP.put("P", "Ｐ");
                HANZEN_MAP.put("Q", "Ｑ");
                HANZEN_MAP.put("R", "Ｒ");
                HANZEN_MAP.put("S", "Ｓ");
                HANZEN_MAP.put("T", "Ｔ");
                HANZEN_MAP.put("U", "Ｕ");
                HANZEN_MAP.put("V", "Ｖ");
                HANZEN_MAP.put("W", "Ｗ");
                HANZEN_MAP.put("X", "Ｘ");
                HANZEN_MAP.put("Y", "Ｙ");
                HANZEN_MAP.put("Z", "Ｚ");
                HANZEN_MAP.put("ｱ", "ア");
                HANZEN_MAP.put("ｲ", "イ");
                HANZEN_MAP.put("ｳ", "ウ");
                HANZEN_MAP.put("ｴ", "エ");
                HANZEN_MAP.put("ｵ", "オ");
                HANZEN_MAP.put("ｶ", "カ");
                HANZEN_MAP.put("ｷ", "キ");
                HANZEN_MAP.put("ｸ", "ク");
                HANZEN_MAP.put("ｹ", "ケ");
                HANZEN_MAP.put("ｺ", "コ");
                HANZEN_MAP.put("ｻ", "サ");
                HANZEN_MAP.put("ｼ", "シ");
                HANZEN_MAP.put("ｽ", "ス");
                HANZEN_MAP.put("ｾ", "セ");
                HANZEN_MAP.put("ｿ", "ソ");
                HANZEN_MAP.put("ﾀ", "タ");
                HANZEN_MAP.put("ﾁ", "チ");
                HANZEN_MAP.put("ﾂ", "ツ");
                HANZEN_MAP.put("ﾃ", "テ");
                HANZEN_MAP.put("ﾄ", "ト");
                HANZEN_MAP.put("ﾅ", "ナ");
                HANZEN_MAP.put("ﾆ", "ニ");
                HANZEN_MAP.put("ﾇ", "ヌ");
                HANZEN_MAP.put("ﾈ", "ネ");
                HANZEN_MAP.put("ﾉ", "ノ");
                HANZEN_MAP.put("ﾊ", "ハ");
                HANZEN_MAP.put("ﾋ", "ヒ");
                HANZEN_MAP.put("ﾌ", "フ");
                HANZEN_MAP.put("ﾍ", "ヘ");
                HANZEN_MAP.put("ﾎ", "ホ");
                HANZEN_MAP.put("ﾏ", "マ");
                HANZEN_MAP.put("ﾐ", "ミ");
                HANZEN_MAP.put("ﾑ", "ム");
                HANZEN_MAP.put("ﾒ", "メ");
                HANZEN_MAP.put("ﾓ", "モ");
                HANZEN_MAP.put("ﾔ", "ヤ");
                HANZEN_MAP.put("ﾕ", "ユ");
                HANZEN_MAP.put("ﾖ", "ヨ");
                HANZEN_MAP.put("ﾗ", "ラ");
                HANZEN_MAP.put("ﾘ", "リ");
                HANZEN_MAP.put("ﾙ", "ル");
                HANZEN_MAP.put("ﾚ", "レ");
                HANZEN_MAP.put("ﾛ", "ロ");
                HANZEN_MAP.put("ﾜ", "ワ");
                HANZEN_MAP.put("ｦ", "ヲ");
                HANZEN_MAP.put("ﾝ", "ン");
                HANZEN_MAP.put("ｧ", "ァ");
                HANZEN_MAP.put("ｨ", "ィ");
                HANZEN_MAP.put("ｩ", "ゥ");
                HANZEN_MAP.put("ｪ", "ェ");
                HANZEN_MAP.put("ｫ", "ォ");
                HANZEN_MAP.put("ｯ", "ッ");
                HANZEN_MAP.put("ｬ", "ャ");
                HANZEN_MAP.put("ｭ", "ュ");
                HANZEN_MAP.put("ｮ", "ョ");
                HANZEN_MAP.put("ｳﾞ", "ヴ");
                HANZEN_MAP.put("ｶﾞ", "ガ");
                HANZEN_MAP.put("ｷﾞ", "ギ");
                HANZEN_MAP.put("ｸﾞ", "グ");
                HANZEN_MAP.put("ｹﾞ", "ゲ");
                HANZEN_MAP.put("ｺﾞ", "ゴ");
                HANZEN_MAP.put("ｻﾞ", "ザ");
                HANZEN_MAP.put("ｼﾞ", "ジ");
                HANZEN_MAP.put("ｽﾞ", "ズ");
                HANZEN_MAP.put("ｾﾞ", "ゼ");
                HANZEN_MAP.put("ｿﾞ", "ゾ");
                HANZEN_MAP.put("ﾀﾞ", "ダ");
                HANZEN_MAP.put("ﾁﾞ", "ヂ");
                HANZEN_MAP.put("ﾂﾞ", "ヅ");
                HANZEN_MAP.put("ﾃﾞ", "デ");
                HANZEN_MAP.put("ﾄﾞ", "ド");
                HANZEN_MAP.put("ﾊﾞ", "バ");
                HANZEN_MAP.put("ﾋﾞ", "ビ");
                HANZEN_MAP.put("ﾌﾞ", "ブ");
                HANZEN_MAP.put("ﾍﾞ", "ベ");
                HANZEN_MAP.put("ﾎﾞ", "ボ");
                HANZEN_MAP.put("ﾊﾟ", "パ");
                HANZEN_MAP.put("ﾋﾟ", "ピ");
                HANZEN_MAP.put("ﾌﾟ", "プ");
                HANZEN_MAP.put("ﾍﾟ", "ペ");
                HANZEN_MAP.put("ﾎﾟ", "ポ");
                HANZEN_MAP.put(" ", "　");
                HANZEN_MAP.put("!", "！");
                HANZEN_MAP.put("\"", "”");
                HANZEN_MAP.put("#", "＃");
                HANZEN_MAP.put("$", "＄");
                HANZEN_MAP.put("%", "％");
                HANZEN_MAP.put("&", "＆");
                HANZEN_MAP.put("\'", "’");
                HANZEN_MAP.put("(", "（");
                HANZEN_MAP.put(")", "）");
                HANZEN_MAP.put("*", "＊");
                HANZEN_MAP.put("+", "＋");
                HANZEN_MAP.put(",", "，");
                HANZEN_MAP.put("-", "－");
                HANZEN_MAP.put(".", "．");
                HANZEN_MAP.put("/", "／");
                HANZEN_MAP.put(":", "：");
                HANZEN_MAP.put(";", "；");
                HANZEN_MAP.put("<", "＜");
                HANZEN_MAP.put("=", "＝");
                HANZEN_MAP.put(">", "＞");
                HANZEN_MAP.put("?", "？");
                HANZEN_MAP.put("@", "＠");
                HANZEN_MAP.put("[", "［");
                HANZEN_MAP.put("\\", "￥");
                HANZEN_MAP.put("]", "］");
                HANZEN_MAP.put("^", "＾");
                HANZEN_MAP.put("_", "＿");
                HANZEN_MAP.put("`", "｀");
                HANZEN_MAP.put("{", "｛");
                HANZEN_MAP.put("|", "｜");
                HANZEN_MAP.put("}", "｝");
                HANZEN_MAP.put("｡", "。");
                HANZEN_MAP.put("｢", "「");
                HANZEN_MAP.put("｣", "」");
                HANZEN_MAP.put("､", "、");
                HANZEN_MAP.put("･", "・");
                HANZEN_MAP.put("ｰ", "ー");
                HANZEN_MAP.put("ﾞ", "゛");
                HANZEN_MAP.put("ﾟ", "゜");
            }
        }

        synchronized (ZENHAN_MAP) {
            if (ZENHAN_MAP.isEmpty()) {
                // 半角変換用のマッピング情報を作成
                for (Map.Entry<Object, Object> entry : HANZEN_MAP.entrySet()) {
                    ZENHAN_MAP.put(entry.getValue(), entry.getKey());
                }
            }
        }
        // JISX0208 - Windows-31J で差分がある特定の文字を全角変換
        synchronized (REPLACE_MAP) {
            if (REPLACE_MAP.isEmpty()) {
                REPLACE_MAP.put("〜", "～");
                REPLACE_MAP.put("‖", "∥");
                REPLACE_MAP.put("−", "－");
                REPLACE_MAP.put("¢", "￠");
                REPLACE_MAP.put("£", "￡");
                REPLACE_MAP.put("¬", "￢");
            }
        }
    }

    /**
     * 半角を全角に変換します
     *
     * @param string 文字列
     * @return 全角に変換された文字列
     */
    public String toZenkaku(String string) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }
        StringBuffer sbBuf = new StringBuffer();
        String partsString1 = null;
        String partsString2 = null;
        String convertedString = null;

        for (int i = 0; i < string.length(); i++) {
            // １文字目取得
            partsString1 = string.substring(i, i + 1);
            // ２文字目が取得できるか
            if (i + 2 <= string.length()) {
                // ２文字目取得
                partsString2 = string.substring(i + 1, i + 2);
            } else {
                // 最後の文字のため２文字目が取得できないので初期化しておく。
                partsString2 = "";
            }

            // ２文字目が "ﾞ" または "ﾟ" だった場合
            if (partsString2.equals("ﾞ") || partsString2.equals("ﾟ")) {
                // 濁音文字かどうか
                convertedString = (String) HANZEN_MAP.get(partsString1 + partsString2);

                if (convertedString == null) {
                    // 濁音文字じゃなかったので、１文字ずつ変換
                    convertedString = (String) HANZEN_MAP.get(partsString1);

                } else {
                    // 濁音文字だったので２文字処理と見なし、インデックスをインクリメント
                    i++;
                }

            } else {
                convertedString = (String) HANZEN_MAP.get(partsString1);
            }

            // マッピング情報がなかった場合
            if (convertedString == null) {
                // 変換不可能だったので、変換前の状態にしておく（ひらがな、漢字...etc）
                convertedString = partsString1;
            }
            sbBuf.append(convertedString);
        }

        // 変換後に文字を置き換える
        String tmpValue = sbBuf.toString();
        for (Map.Entry<String, String> entry : REPLACE_MAP.entrySet()) {
            tmpValue = tmpValue.replaceAll(entry.getKey(), entry.getValue());
        }
        return tmpValue;
    }

    /**
     * 全角を半角に変換します
     *
     * @param string 文字列
     * @return 半角に変換された文字列
     */
    public String toHankaku(String string) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }
        StringBuilder sbBuf = new StringBuilder();
        String partsString = null;
        String convertedString = null;

        for (int i = 0; i < string.length(); i++) {
            partsString = string.substring(i, i + 1);
            convertedString = (String) ZENHAN_MAP.get(partsString);
            if (convertedString == null) {
                convertedString = partsString;
            }
            sbBuf.append(convertedString);
        }

        return sbBuf.toString();
    }

}
