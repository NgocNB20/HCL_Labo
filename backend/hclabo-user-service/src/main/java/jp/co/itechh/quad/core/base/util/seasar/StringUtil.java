package jp.co.itechh.quad.core.base.util.seasar;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 機能概要：＜修正要＞
 * 作成日：2021/02/25
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class StringUtil {

    /**
     * 空の文字列の配列です。
     */
    public static final String[] EMPTY_STRINGS = new String[0];

    /**
     *
     */
    protected StringUtil() {
    }

    /**
     * 文字列が<code>null</code>または空文字列なら<code>true</code>を返します。
     *
     * @param text
     *            文字列
     * @return 文字列が<code>null</code>または空文字列なら<code>true</code>
     */
    public static final boolean isEmpty(final String text) {
        return text == null || text.length() == 0;
    }

    /**
     * 文字列が<code>null</code>でも空文字列でもなければ<code>true</code>を返します。
     *
     * @param text
     *            文字列
     * @return 文字列が<code>null</code>でも空文字列でもなければ<code>true</code>
     * @since 2.4.33
     */
    public static final boolean isNotEmpty(final String text) {
        return !isEmpty(text);
    }

    /**
     * 文字列を置き換えます。
     *
     * @param text
     *            テキスト
     * @param fromText
     *            置き換え対象のテキスト
     * @param toText
     *            置き換えるテキスト
     * @return 結果
     */
    public static final String replace(final String text, final String fromText, final String toText) {

        if (text == null || fromText == null || toText == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(100);
        int pos = 0;
        int pos2 = 0;
        while (true) {
            pos = text.indexOf(fromText, pos2);
            if (pos == 0) {
                buf.append(toText);
                pos2 = fromText.length();
            } else if (pos > 0) {
                buf.append(text.substring(pos2, pos));
                buf.append(toText);
                pos2 = pos + fromText.length();
            } else {
                buf.append(text.substring(pos2));
                break;
            }
        }
        return buf.toString();
    }

    /**
     * 文字列を分割します。
     *
     * @param str
     *            文字列
     * @param delim
     *            分割するためのデリミタ
     * @return 分割された文字列の配列
     */
    public static String[] split(final String str, final String delim) {
        if (isEmpty(str)) {
            return EMPTY_STRINGS;
        }
        List list = new ArrayList();
        StringTokenizer st = new StringTokenizer(str, delim);
        while (st.hasMoreElements()) {
            list.add(st.nextElement());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * JavaBeansの仕様にしたがってキャピタライズを行ないます。大文字が2つ以上続く場合は、大文字にならないので注意してください。
     *
     * @param name
     *            名前
     * @return 結果の文字列
     */
    public static String capitalize(final String name) {
        if (isEmpty(name)) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * ケースインセンシティブで特定の文字列で開始されているかどうかを返します。
     *
     * @param text
     *            テキスト
     * @param fragment
     *            特定の文字列
     * @return ケースインセンシティブで特定の文字列で開始されているかどうか
     * @see #startsWithIgnoreCase(String, String)
     * @deprecated
     */
    public static boolean startsWith(final String text, final String fragment) {
        return startsWithIgnoreCase(text, fragment);
    }

    /**
     * ブランクかどうか返します。
     *
     * @param str
     *            文字列
     * @return ブランクかどうか
     */
    public static boolean isBlank(final String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * charを含んでいるかどうか返します。
     *
     * @param str
     *            文字列
     * @param ch
     *            char
     * @return charを含んでいるかどうか
     */
    public static boolean contains(final String str, final char ch) {
        if (isEmpty(str)) {
            return false;
        }
        return str.indexOf(ch) >= 0;
    }

    /**
     * 文字列を含んでいるかどうか返します。
     *
     * @param s1
     *            文字列
     * @param s2
     *            比較する対象となる文字列
     * @return 文字列を含んでいるかどうか
     */
    public static boolean contains(final String s1, final String s2) {
        if (isEmpty(s1)) {
            return false;
        }
        return s1.indexOf(s2) >= 0;
    }

    /**
     * 文字列同士が等しいかどうか返します。どちらもnullの場合は、<code>true</code>を返します。
     *
     * @param target1
     *            文字列1
     * @param target2
     *            文字列2
     * @return 文字列同士が等しいかどうか
     */
    public static boolean equals(final String target1, final String target2) {
        return (target1 == null) ? (target2 == null) : target1.equals(target2);
    }

    /**
     * ケースインセンシティブで特定の文字ではじまっているのかどうかを返します。
     *
     * @param target1
     *            テキスト
     * @param target2
     *            比較する文字列
     * @return ケースインセンシティブで特定の文字ではじまっているのかどうか
     */
    public static boolean startsWithIgnoreCase(final String target1, final String target2) {
        if (target1 == null || target2 == null) {
            return false;
        }
        int length1 = target1.length();
        int length2 = target2.length();
        if (length1 < length2) {
            return false;
        }
        String s1 = target1.substring(0, target2.length());
        return s1.equalsIgnoreCase(target2);
    }

    /**
     * 16進数の文字列に変換します。
     *
     * @param bytes
     *            バイトの配列
     * @return 16進数の文字列
     */
    public static String toHex(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            appendHex(sb, bytes[i]);
        }
        return sb.toString();
    }

    /**
     * 文字列に、数値を16進数に変換した文字列を追加します。
     *
     * @param buf
     *            追加先の文字列
     * @param i
     *            数値
     */
    public static void appendHex(final StringBuffer buf, final byte i) {
        buf.append(Character.forDigit((i & 0xf0) >> 4, 16));
        buf.append(Character.forDigit((i & 0x0f), 16));
    }
}