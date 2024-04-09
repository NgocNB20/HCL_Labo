/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.thymeleaf;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * EscapeUtil
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class EscapeViewUtil {

    /** 正規表現：改行コード */
    private static final String REG_LINE_SEPARATOR = "\r\n|\n";

    /** 正規表現：インラインのJavaScriptコード除去用 */
    private static final String REG_SCRIPT = "<script.+?</script>";

    /** 正規表現：インラインのCSSコード除去用 */
    private static final String REG_STYLE = "<style.+?</style>";

    /** 正規表現：HTMLタグ除去用 */
    private static final String REG_TAG = "<.+?>";

    /** HTML エスケープ対象のメタ名 */
    protected Map<String, String> reverseEscapeMap = new HashMap<String, String>() {
        /** シリアルID */
        private static final long serialVersionUID = 1L;

        {
            put("&amp;lt;", "&lt;");
            put("&amp;gt;", "&gt;");
            put("&amp;quot;", "&quot;");
            put("&amp;#39;", "&#39;");
            put("&amp;amp;", "&amp;");
        }
    };

    /** コンストラクタ */
    public EscapeViewUtil() {
    }

    // CHECKSTYLE:OFF

    /**
     * 対象文字列のHTMLタグの除去等を行う
     *
     * <pre>
     * {@code
     * 1. 改行コードの除去
     * 2. インラインのJavaScript、CSSのコードを除去
     *      正規表現 <script.+?</script>|<style.+?</style> に該当する文字を除去
     *    HTMLタグの除去
     *      正規表現 <.+?> に該当する文字を除去
     * 3. 特殊記号を参照文字に変換
     * }
     * </pre>
     *
     * @param value 変換対象の文字
     * @return 変換後の文字
     */
    // CHECKSTYLE:ON
    public String escapeHtml(String value) {
        return escapeHtml(value, true);
    }

    /**
     * 上記参照<br/>
     *
     * @param value 値
     * @param removeHtmlTagFlg htmlタグ除去フラグ
     * @return エスケープ後の値
     */
    public String escapeHtml(String value, boolean removeHtmlTagFlg) {

        if (StringUtils.isEmpty(value)) {
            return "";
        }

        // HTML タグ除去
        String str = value;
        if (removeHtmlTagFlg) {
            str = removeHtmlTag(str);
        }

        // HTML 特殊文字エスケープ
        return escapeHtmlOnly(str);
    }

    /**
     * HTMLエスケープのみを実行する<br/>
     * 2回エスケープしたものは、戻す。<br/>
     *
     * @param value 値
     * @return HTMLエスケープ値
     */
    public String removeHtmlTag(String value) {

        if (StringUtils.isEmpty(value)) {
            return "";
        }

        // HTML タグ除去
        String str = value;
        str = str.replaceAll(REG_LINE_SEPARATOR, "");
        str = str.replaceAll("(?i)" + REG_LINE_SEPARATOR + "|" + REG_SCRIPT + "|" + REG_STYLE + "|" + REG_TAG, "");
        return str;
    }

    /**
     * HTMLエスケープのみを実行する<br/>
     * 2回エスケープしたものは、戻す。<br/>
     *
     * @param value 値
     * @return HTMLエスケープ値
     */
    public String escapeHtmlOnly(String value) {

        if (StringUtils.isEmpty(value)) {
            return "";
        }

        // HTML 特殊文字エスケープ
        String str = encodeAll(value);

        // 2回エスケープしたものは、1回に戻す。
        for (Map.Entry<String, String> reverse : reverseEscapeMap.entrySet()) {
            str = str.replaceAll(reverse.getKey(), reverse.getValue());
        }

        return str;
    }

    /**
     * シングルクォーテーションのエスケープを行う。<br />
     * ※ダブルクォーテーションにも対応する。<br />
     * 「\'」の連続文字列も考慮し、バックスラッシュのエスケープも行う
     *
     * @param source エスケープ元文字列
     * @return エスケープ後文字列
     */
    public String escapeQuote(String source) {
        if (StringUtils.isEmpty(source)) {
            return source;
        }
        source = source.replace("\\", "\\\\").replace("\'", "\\'");
        return source.replace("\"", "\\\"");

    }

    /**
     * HTML 特殊文字エスケープ
     * @param s エスケープ元文字列
     * @return エスケープ後文字列
     */
    private String encodeAll(final String s) {
        return encode(s, true, true);
    }

    /**
     * HTML 特殊文字エスケープ
     *
     * @param s エスケープ元文字列
     * @param quote
     * @param amp
     * @return エスケープ後文字列
     */
    private String encode(final String s, final boolean quote, final boolean amp) {
        char[] chars = s.toCharArray();
        StringBuffer sb = new StringBuffer(s.length() + 64);
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            encodeEach(sb, c, quote, amp);
        }
        return new String(sb);
    }

    /**
     * HTML 特殊文字エスケープ
     *
     * @param sb StringBuffer
     * @param c エスケープ元文字列
     * @param quote
     * @param amp
     */
    private void encodeEach(final StringBuffer sb, final char c, final boolean quote, final boolean amp) {
        if ((int) c == '\u00A0') {
            sb.append("&nbsp;");
        } else if (c == '<') {
            sb.append("&lt;");
        } else if (c == '>') {
            sb.append("&gt;");
        } else if (amp && c == '&') {
            sb.append("&amp;");
        } else if (c == '"') {
            sb.append("&quot;");
        } else if (quote && c == '\'') {
            sb.append("&#39;");
        } else if ((int) c == '\u00A5') {
            sb.append("&yen;");
        } else {
            sb.append(c);
        }
    }
}