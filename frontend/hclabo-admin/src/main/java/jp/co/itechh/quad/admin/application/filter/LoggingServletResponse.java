/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.application.filter;

import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * レスポンスコードとリダイレクト URL を取得するためのロギングサーブレットレスポンス。<br />
 *
 * 100系や200系やのコードは取得できないので、
 * デフォルトのレスポンスコードは 200 として扱う。
 *
 * @author tm27400
 * @author Tomo (itec) 2010/06/11 チケット #2139 対応
 *
 */
public class LoggingServletResponse extends ResponseDelegator {

    /** 300系レスポンスのリダイレクトロケーション */
    protected String location;

    /** レスポンスコード */
    protected int responseCode;

    /**
     * コンストラクタ
     *
     * @param res   ラップ対象の ServletResponse
     */
    public LoggingServletResponse(ServletResponse res) {
        super(res);
        this.responseCode = 200;
    }

    /**
     * リダイレクトロケーションを返す。
     *
     * @return リダイレクトロケーション
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * レスポンスコードを返す。
     *
     * @return レスポンスコード
     */
    public int getResponseCode() {
        return this.responseCode;
    }

    /**
     * addHeader のフッキング
     *
     * @param name  ヘッダ名
     * @param value ヘッダ値
     */
    @Override
    public void addHeader(String name, String value) {

        if ("location".equalsIgnoreCase(name)) {
            this.location = value;
        }
        super.addHeader(name, value);
    }

    /**
     * setHeader のフッキング
     *
     * @param name  ヘッダ名
     * @param value ヘッダ値
     */
    @Override
    public void setHeader(String name, String value) {
        if ("location".equalsIgnoreCase(name)) {
            this.location = value;
        }
        super.setHeader(name, value);
    }

    /**
     * sendError のフッキング
     *
     * @param sc    レスポンスコード
     * @throws IOException  発生した例外
     */
    @Override
    public void sendError(int sc) throws IOException {
        this.responseCode = sc;
        super.sendError(sc);
    }

    /**
     * sendError のフッキング
     *
     * @param sc    レスポンスコード
     * @param msg   メッセージ
     * @throws IOException  発生した例外
     */
    @Override
    public void sendError(int sc, String msg) throws IOException {
        this.responseCode = sc;
        super.sendError(sc, msg);
    }

    /**
     * sendRedirect のフッキング
     *
     * @param argLocation   リダイレクトロケーション
     * @throws IOException  発生した例外
     */
    @Override
    public void sendRedirect(String argLocation) throws IOException {
        this.responseCode = 302;
        this.location = argLocation;
        super.sendRedirect(argLocation);
    }

    /**
     * setStatus のフッキング
     *
     * @param sc    レスポンスコード
     */
    @Override
    public void setStatus(int sc) {
        this.responseCode = sc;
        super.setStatus(sc);
    }

    /**
     * setStatus のフッキング
     *
     * @param sc    レスポンスコード
     * @param sm    メッセージ？
     * @deprecated 非推奨メソッド
     */
    @Deprecated
    @Override
    public void setStatus(int sc, String sm) {
        this.responseCode = sc;
        super.setStatus(sc, sm);
    }

    /**
     * 抽象メソッド<br/>
     *
     * @param arg0 String
     * @return response.getHeader(arg0)
     */
    @Override
    public String getHeader(String arg0) {
        return ((HttpServletResponse) response).getHeader(arg0);
    }

    /**
     * 抽象メソッド<br/>
     *
     * @return response.getHeaderNames()
     */
    @Override
    public Collection<String> getHeaderNames() {
        return ((HttpServletResponse) response).getHeaderNames();
    }

    /**
     * 抽象メソッド<br/>
     *
     * @param arg0 String
     * @return response.getHeaders(arg0)
     */
    @Override
    public Collection<String> getHeaders(String arg0) {
        return ((HttpServletResponse) response).getHeaders(arg0);
    }

    /**
     * 抽象メソッド<br/>
     *
     * @return response.getStatus()
     */
    @Override
    public int getStatus() {
        return ((HttpServletResponse) response).getStatus();
    }

    @Override
    public void setContentLengthLong(long length) {
        ((HttpServletResponse) response).setContentLengthLong(length);
    }

    @Override
    public boolean isReady() {
        return outStream.isReady();
    }

    @Override
    public void setWriteListener(WriteListener listener) {
        outStream.setWriteListener(listener);

    }
}