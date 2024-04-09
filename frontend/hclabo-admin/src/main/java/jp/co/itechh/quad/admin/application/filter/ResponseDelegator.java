/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.application.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * HttpServletResposne と ServletOutputStream の処理をフックするための委譲クラス。
 *
 * <pre>
 * メモ：
 * このクラスを使って Response の差し替えを行った場合、ServletAPI の仕様とは若干異なった動作をするようになります。
 * {@link ServletResponse#getWriter()} と {@link ServletResponse#getOutputStream()} の両出力オブジェクト取得時に例外がスローされない。
 * </pre>
 *
 * @author tm27400
 * @author Nishigaki(Itec) 2011/01/12 チケット #2633 対応 クラスの可視レベルを public へ変更
 * @author Kaneko (Itec) 2011/01/17　チケット #2633 ResponseDelegatorの多重ラップを可能にするように対応
 * @author Tomo (Itec) 20011/1/19 チケット #2633 対応 JSP使用時に発生する例外に対応
 * @author matsumoto(itec) 2012/02/07 #2761 対応
 *
 */
public abstract class ResponseDelegator extends ServletOutputStream implements HttpServletResponse {

    /** 委譲先の ServletResponse オブジェクト */
    protected ServletResponse response;

    /** 委譲先の ServletOutputStream オブジェクト */
    protected ServletOutputStream outStream;

    /**
     * コンストラクタ
     *
     * @param res   レスポンス
     */
    public ResponseDelegator(ServletResponse res) {
        this.response = res;

        if (this.response instanceof ResponseDelegator) {
            // 別の ResponseDelegator 継承オブジェクトをラップする場合は、
            // そのオブジェクトをそのまま outStream として使用する。
            this.outStream = (ServletOutputStream) this.response;
            return;
        }

        try {
            // ResponseDelegator 以外の ServletResponse で初期化を行う場合は、outStream には
            // Response#getOutputStream() が返すオブジェクトを設定する。
            this.outStream = this.response.getOutputStream();

            // outputStream 取得済みフラグがオンのままであるとJSP利用時に例外が発生するためフラグはオフにする。
            // Tomcatでは reset() されても取得済みの OutputStream
            // は有効に使用できるが、Tomcat以外のコンテナで使用できるかは未確認
            this.reset();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getOutputStream メソッドの処理をフックし、自インスタンスを変わりに返す。
     *
     * @return 戻り値
     * @throws IOException 発生した例外
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this;
    }

    /**
     * 処理の委譲
     *
     * @throws IOException 発生した例外
     */
    @Override
    public void flushBuffer() throws IOException {
        this.response.flushBuffer();
    }

    /**
     * 処理の委譲
     *
     * @return 戻り値
     */
    @Override
    public int getBufferSize() {
        return this.response.getBufferSize();
    }

    /**
     * 処理の委譲
     *
     * @return 戻り値
     */
    @Override
    public String getCharacterEncoding() {
        return this.response.getCharacterEncoding();
    }

    /**
     * 処理の委譲
     *
     * @return 戻り値
     */
    @Override
    public String getContentType() {
        return this.response.getContentType();
    }

    /**
     * 処理の委譲
     *
     * @return 戻り値
     */
    @Override
    public Locale getLocale() {
        return this.response.getLocale();
    }

    /**
     * 処理の委譲
     *
     * @return 戻り値
     * @throws IOException 発生した例外
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        return this.response.getWriter();
    }

    /**
     * 処理の委譲
     *
     * @return 戻り値
     */
    @Override
    public boolean isCommitted() {
        return this.response.isCommitted();
    }

    /**
     * 処理の委譲
     *
     */
    @Override
    public void reset() {
        this.response.reset();
    }

    /**
     * 処理の委譲
     *
     */
    @Override
    public void resetBuffer() {
        this.response.resetBuffer();
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     */
    @Override
    public void setBufferSize(int arg0) {
        this.response.setBufferSize(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     */
    @Override
    public void setCharacterEncoding(String arg0) {
        this.response.setCharacterEncoding(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     */
    @Override
    public void setContentLength(int arg0) {
        this.response.setContentLength(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     */
    @Override
    public void setContentType(String arg0) {
        this.response.setContentType(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     */
    @Override
    public void setLocale(Locale arg0) {
        this.response.setLocale(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     */
    @Override
    public void addCookie(Cookie arg0) {
        ((HttpServletResponse) this.response).addCookie(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     */
    @Override
    public void addDateHeader(String arg0, long arg1) {
        ((HttpServletResponse) this.response).addDateHeader(arg0, arg1);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     */
    @Override
    public void addHeader(String arg0, String arg1) {
        ((HttpServletResponse) this.response).addHeader(arg0, arg1);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     */
    @Override
    public void addIntHeader(String arg0, int arg1) {
        ((HttpServletResponse) this.response).addIntHeader(arg0, arg1);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @return 戻り値
     */
    @Override
    public boolean containsHeader(String arg0) {
        return ((HttpServletResponse) this.response).containsHeader(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @return 戻り値
     */
    @Override
    public String encodeRedirectURL(String arg0) {
        return ((HttpServletResponse) this.response).encodeRedirectURL(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @return 戻り値
     */
    @Override
    @Deprecated
    public String encodeRedirectUrl(String arg0) {
        return ((HttpServletResponse) this.response).encodeRedirectUrl(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @return 戻り値
     */
    @Override
    public String encodeURL(String arg0) {

        String lc = arg0.toLowerCase();

        // 引数のない画像は encodeURL 対象にしない。（すると ;jsessionid= 等が付与されてしまうので)
        if (lc.endsWith(".jpeg") || lc.endsWith(".jpg") || lc.endsWith(".gif") || lc.endsWith(".png")) {
            return arg0;
        }

        return ((HttpServletResponse) this.response).encodeURL(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @return 戻り値
     */
    @Override
    @Deprecated
    public String encodeUrl(String arg0) {
        return ((HttpServletResponse) this.response).encodeURL(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void sendError(int arg0) throws IOException {
        ((HttpServletResponse) this.response).sendError(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void sendError(int arg0, String arg1) throws IOException {
        ((HttpServletResponse) this.response).sendError(arg0, arg1);
    }

    /**
     * リダイレクト処理のオーバライド<br />
     * 通常の sendRedirect は絶対パスの URL でレスポンスを送出するが、
     * ここで、スキーマ＋ドメイン名を含まない URI を返すように変更。
     *
     * @param url 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void sendRedirect(String url) throws IOException {
        ((HttpServletResponse) this.response).sendRedirect(url);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     */
    @Override
    public void setDateHeader(String arg0, long arg1) {
        ((HttpServletResponse) this.response).setDateHeader(arg0, arg1);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     */
    @Override
    public void setHeader(String arg0, String arg1) {
        ((HttpServletResponse) this.response).setHeader(arg0, arg1);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     */
    @Override
    public void setIntHeader(String arg0, int arg1) {
        ((HttpServletResponse) this.response).setIntHeader(arg0, arg1);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     */
    @Override
    public void setStatus(int arg0) {
        ((HttpServletResponse) this.response).setStatus(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @param arg1 引数
     */
    @Override
    @Deprecated
    public void setStatus(int arg0, String arg1) {
        ((HttpServletResponse) this.response).setStatus(arg0, arg1);
    }

    /**
     * 処理の委譲
     *
     * @param b 引数
     * @param off 引数
     * @param len 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.outStream.write(b, off, len);
    }

    /**
     * 処理の委譲
     *
     * @param b 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void write(int b) throws IOException {
        this.outStream.write(b);

    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void print(String arg0) throws IOException {
        this.outStream.print(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param s 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void println(String s) throws IOException {
        this.outStream.println(s);
    }

    /**
     * 処理の委譲
     *
     * @param b 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void write(byte[] b) throws IOException {
        this.outStream.write(b);
    }

    /**
     * 処理の委譲
     *
     * @param arg0 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void print(boolean arg0) throws IOException {
        this.outStream.print(arg0);
    }

    /**
     * 処理の委譲
     *
     * @param c 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void print(char c) throws IOException {
        this.outStream.print(c);
    }

    /**
     * 処理の委譲
     *
     * @param d 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void print(double d) throws IOException {
        this.outStream.print(d);
    }

    /**
     * 処理の委譲
     *
     * @param f 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void print(float f) throws IOException {
        this.outStream.print(f);
    }

    /**
     * 処理の委譲
     *
     * @param i 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void print(int i) throws IOException {
        this.outStream.print(i);
    }

    /**
     * 処理の委譲
     *
     * @param l 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void print(long l) throws IOException {
        this.outStream.print(l);
    }

    /**
     * 処理の委譲
     *
     * @throws IOException 発生した例外
     */
    @Override
    public void println() throws IOException {
        this.outStream.println();
    }

    /**
     * 処理の委譲
     *
     * @param b 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void println(boolean b) throws IOException {
        this.outStream.println(b);
    }

    /**
     * 処理の委譲
     *
     * @param c 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void println(char c) throws IOException {
        this.outStream.println(c);
    }

    /**
     * 処理の委譲
     *
     * @param d 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void println(double d) throws IOException {
        this.outStream.println(d);
    }

    /**
     * 処理の委譲
     *
     * @param f 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void println(float f) throws IOException {
        this.outStream.println(f);
    }

    /**
     * 処理の委譲
     *
     * @param i 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void println(int i) throws IOException {
        this.outStream.println(i);
    }

    /**
     * 処理の委譲
     *
     * @param l 引数
     * @throws IOException 発生した例外
     */
    @Override
    public void println(long l) throws IOException {
        this.outStream.println(l);
    }

}