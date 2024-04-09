/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.logic.common;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.core.module.crypto.CryptoModule;
import jp.co.itechh.quad.front.utility.AccessDeviceUtility;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.user.presentation.api.UsersApi;
import jp.co.itechh.quad.user.presentation.api.param.AccessUidResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * 共通処理ロジックの抽象クラス<br/>
 *
 * @author natume
 */
@Component
@Data
public abstract class AbstractCommonProcessLogic {
    /**
     * ログ
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommonProcessLogic.class);

    /**
     * Cookie 名：端末識別番号  accessUid
     */
    protected static final String COOKIE_NAME_ACCESSUID = "AUI";

    /**
     * 文字コード UTF-8<br/>
     */
    protected static final String UTF_8 = "UTF-8";

    /**
     * クッキー有効期限:90日(3ヶ月)/view/<br/>
     */
    protected static final int COOKIE_EXPIRY = 60 * 60 * 24 * 90;

    /**
     * クッキー有効期限:0 削除/view/<br/>
     */
    protected static final int COOKIE_CLEAR = 0;

    /**
     * コロン：
     */
    protected static final String COLON = ":";

    /**
     * クッキー保存先パス取得用プロパティキー
     */
    protected static final String COOKIE_SAVE_PATH_PROP_KEY = "common.cookies.save.path";

    /**
     * 共通情報ユーティリティ
     */
    protected final CommonInfoUtility commonInfoUtility;

    /**
     * ApplicationLog ユーティリティ
     */
    public ApplicationLogUtility applicationLogUtility;

    /**
     * アクセスデバイスの解析用Utility
     */
    private final AccessDeviceUtility accessDeviceUtility;

    /**
     * コンストラクタ<br/>
     *
     * @param commonInfoUtility
     * @param applicationLogUtility
     * @param accessDeviceUtility
     */
    @Autowired
    public AbstractCommonProcessLogic(CommonInfoUtility commonInfoUtility,
                                      ApplicationLogUtility applicationLogUtility,
                                      AccessDeviceUtility accessDeviceUtility) {
        this.commonInfoUtility = commonInfoUtility;
        this.applicationLogUtility = applicationLogUtility;
        this.accessDeviceUtility = accessDeviceUtility;
    }

    /**
     * クッキーに端末番号をセット<br/>
     * ※PCの場合のみ<br/>
     *
     * @param request    リクエスト
     * @param response   レスポンス
     * @param commonInfo 共通情報
     */
    protected void setAccessUid(HttpServletRequest request, HttpServletResponse response, CommonInfo commonInfo) {

        // クッキーから取得
        Cookie cookie = getCookie(request, COOKIE_NAME_ACCESSUID);

        // 端末識別番号取得
        String accessUid = commonInfo.getCommonInfoBase().getAccessUid();

        // クッキーの端末識別番号の暗号化
        String encryptedAccessUid;
        try {
            CryptoModule cryptoModule = ApplicationContextUtility.getBean(CryptoModule.class);
            encryptedAccessUid = cryptoModule.encryptAccessUid(accessUid);
        } catch (Exception e) {
            RuntimeException re = new RuntimeException("Cookie encrypt fail accessUid:" + accessUid, e);
            applicationLogUtility.writeExceptionLog(re);
            LOG.error("The deletion of the cookie is executed.");

            /* 暗号化失敗 クッキー削除 */
            deleteCookie(request, response, cookie);
            return;
        }

        // クッキーがある場合
        if (cookie == null) {
            cookie = new Cookie(COOKIE_NAME_ACCESSUID, encryptedAccessUid);
        } else {
            cookie.setValue(encryptedAccessUid);
        }

        // 有効期間
        updateCookie(request, response, cookie);

    }

    /**
     * クッキー取得<br/>
     *
     * @param request    リクエスト
     * @param cookieName クッキー名
     * @return Cookie クッキー情報
     */
    protected Cookie getCookie(HttpServletRequest request, String cookieName) {
        // 現行HM HttpUtilityから移植
        // クッキーから取得
        Cookie[] cookieArray = request.getCookies();
        if (cookieArray != null) {
            for (Cookie cookie : cookieArray) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * クッキーを更新する<br/>
     *
     * @param request  リクエスト
     * @param response レスポンス
     * @param cookie   クッキー
     */
    protected void updateCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {

        // クッキーがある場合
        if (cookie != null) {

            // 有効期間
            cookie.setMaxAge(COOKIE_EXPIRY);
            cookie.setPath(PropertiesUtil.getSystemPropertiesValue(COOKIE_SAVE_PATH_PROP_KEY));
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);
        }
    }

    /**
     * クッキーを削除する<br/>
     *
     * @param request  リクエスト
     * @param response レスポンス
     * @param cookie   クッキー
     */
    protected void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {

        // クッキーがある場合
        if (cookie != null) {

            // 有効期間
            cookie.setMaxAge(COOKIE_CLEAR);
            cookie.setPath(PropertiesUtil.getSystemPropertiesValue(COOKIE_SAVE_PATH_PROP_KEY));
            response.addCookie(cookie);
        }
    }

    /**
     * 起動アプリのショップSEQを取得<br/>
     * ※ショップSEQをどこで指定するかは変更される可能性があるので、
     * 専用メソッドを作成しておく
     *
     * @return ショップSEQ
     */
    public Integer getShopSeq() {
        // 現行ApplicationUtilityから移植
        // system.propertiesから取得
        String shopSeqStr = PropertiesUtil.getSystemPropertiesValue("shopseq");
        if (shopSeqStr == null || !shopSeqStr.matches("^[1-9]$|^[1-9][0-9]{1,3}$")) {
            throw new RuntimeException("system.properties shopseqの値が不正です。");
        }
        return Integer.valueOf(shopSeqStr);
    }

    /**
     * 端末識別番号のセット
     *
     * @param request リクエスト
     * @return 端末識別番号 access uid
     */
    public String getAccessUid(HttpServletRequest request) {

        // クッキーから取得
        String accessUid = null;
        String encryptedAccessUid = null;
        Cookie cookie = getCookie(request, COOKIE_NAME_ACCESSUID);
        if (cookie != null) {
            try {
                // 暗号化された端末識別番号
                encryptedAccessUid = URLDecoder.decode(cookie.getValue(), UTF_8);

                // 復号
                CryptoModule cryptoModule = ApplicationContextUtility.getBean(CryptoModule.class);
                accessUid = cryptoModule.decryptAccessUid(encryptedAccessUid);
            } catch (Exception e) {
                RuntimeException re = new RuntimeException("Cookie decrypt fail accessUid:" + encryptedAccessUid, e);
                applicationLogUtility.writeExceptionLog(re);
            }
        }

        // ない場合作成
        if (accessUid == null) {
            accessUid = createAccessUid();
        }
        return accessUid;
    }

    /**
     * 端末識別番号の作成<br/>
     *
     * @return 端末識別番号
     */
    protected String createAccessUid() {

        // 端末識別番号作成

        UsersApi usersApi = ApplicationContextUtility.getBean(UsersApi.class);

        AccessUidResponse accessUidResponse = usersApi.getAccessUid();

        return accessUidResponse.getAccessUid();
    }

}