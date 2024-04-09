/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.service.common.impl;

import jp.co.itechh.quad.authentication.presentation.api.AuthenticationApi;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginResponse;
import jp.co.itechh.quad.front.application.commoninfo.impl.HmFrontUserDetails;
import jp.co.itechh.quad.front.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.core.module.crypto.CryptoModule;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.web.ClientErrorMessageUtility;
import jp.co.itechh.quad.user.presentation.api.UsersApi;
import jp.co.itechh.quad.user.presentation.api.param.CustomerIdResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.List;

/**
 * Spring-Security Front認証サービスクラス
 * DB認証用カスタマイズ
 * ※ログイン失敗メッセージはSpring-Security標準メッセージをプロパティファイルにて上書
 *
 * @author kaneda
 */
@Service
public class HmFrontUserDetailsServiceImpl implements UserDetailsService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HmFrontUserDetailsServiceImpl.class);

    /** 認証Api */
    private final AuthenticationApi authenticationApi;

    /** ユーザーApi */
    private final UsersApi usersApi;

    /**  ログイン失敗メッセージ */
    private static final String LOGIN_FAIL_MSGCD = "AbstractUserDetailsAuthenticationProvider.badCredentials";

    /** メールアドレスの正規表現 */
    private static final String MAIL_ADDRESS_REGEX =
                    "^[a-zA-Z0-9!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\|\\}~\\.]+@(([-a-z0-9]+\\.)*[a-z]+|\\[\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\])";

    // ゲストユーザー用顧客ID関連 >>>>>>>>>>
    /** Cookie 名：ゲスト顧客ID */
    private static final String COOKIE_NAME_GUEST_CUSTOMER_ID = "GCI";
    /** Cookie有効期限:90日(3ヶ月) */
    private static final int COOKIE_EXPIRY = 60 * 60 * 24 * 90;
    /** クッキー有効期限:0 削除 */
    private static final int COOKIE_CLEAR = 0;
    /** 文字コード UTF-8 */
    private static final String UTF_8 = "UTF-8";
    /** クッキー保存先パス取得用プロパティキー */
    protected static final String COOKIE_SAVE_PATH_PROP_KEY = "common.cookies.save.path";

    /** コンストラクタ */
    @Autowired
    public HmFrontUserDetailsServiceImpl(AuthenticationApi authenticationApi, UsersApi usersApi) {
        this.authenticationApi = authenticationApi;
        this.usersApi = usersApi;
    }

    /**
     * ユーザ認証処理
     *
     * @param memberInfoId 会員ID
     */
    @Override
    public UserDetails loadUserByUsername(String memberInfoId) throws AuthenticationException {

        // 入力チェックのエラーメッセージ
        String errorMessage;

        // DB負荷を減らすためチェックを行う
        try {
            // 半角変換
            memberInfoId = Normalizer.normalize(memberInfoId, Normalizer.Form.NFKC);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            e.printStackTrace();
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("LMC002011E", null).getMessage();
            throw new BadCredentialsException(errorMessage);
        }

        // 必須チェック
        // メールアドレス
        if (StringUtils.isEmpty(memberInfoId)) {
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("SMM002002W", new Object[] {"メールアドレス"}).getMessage();
            throw new BadCredentialsException(errorMessage);
        }

        // ログイン後のユーザー情報・メールアドレスなどを変更した場合は、以下のチェックを対象外とする
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Boolean isCheckInfo = (Boolean) request.getAttribute("isCheckInfo");
        if (isCheckInfo == null || !isCheckInfo) {
            // パスワード
            // Remember-Meによる自動ログインする時には、パスワードのバリデーションを対象外とする
            String rememberMeCookie = extractRememberMeCookie(request);
            if (rememberMeCookie == null) {
                String memberInfoPassWord = request.getParameter("memberInfoPassWord");
                if (StringUtils.isEmpty(memberInfoPassWord)) {
                    errorMessage = AppLevelFacesMessageUtil.getAllMessage("SMM002002W", new Object[] {"パスワード"})
                                                           .getMessage();
                    throw new BadCredentialsException(errorMessage);
                }
            }
        }
        // メールアドレスの正規表現チェック
        if (!memberInfoId.matches(MAIL_ADDRESS_REGEX)) {
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("LMC002012E", null).getMessage();
            throw new BadCredentialsException(errorMessage);
        }

        // 文字数チェック
        if (memberInfoId.length() > 160) {
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("USERNAME.LENGTH.E", new Object[] {"メールアドレス", "160"})
                                                   .getMessage();
            throw new BadCredentialsException(errorMessage);
        }

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = login(memberInfoId);
        if (memberInfoEntity == null) {
            errorMessage = AppLevelFacesMessageUtil.getAllMessage(LOGIN_FAIL_MSGCD, null).getMessage();
            throw new BadCredentialsException(errorMessage);
        }
        return new HmFrontUserDetails(memberInfoEntity);
    }

    /**
     * セッションのユーザー情報を更新
     *
     * @param memberInfoId
     */
    public void updateUserInfo(String memberInfoId) {
        UserDetails userDetails = loadUserByUsername(memberInfoId);
        Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuthentication =
                        new UsernamePasswordAuthenticationToken(userDetails, oldAuthentication.getCredentials(),
                                                                userDetails.getAuthorities()
                        );
        newAuthentication.setDetails(new WebAuthenticationDetails(
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()));
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

    }

    /**
     * Remember-MeのCookieを取得してみる
     *
     * @param request
     * @return
     */
    public String extractRememberMeCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if ((cookies == null) || (cookies.length == 0)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 会員情報取得
     *
     * @param memberInfoId 会員ID
     * @return 会員クラス
     */
    private MemberInfoEntity login(String memberInfoId) {
        MemberLoginRequest loginRequest = new MemberLoginRequest();
        loginRequest.setMemberId(memberInfoId);
        try {
            MemberLoginResponse loginResponse = authenticationApi.memberLogin(loginRequest);

            if (loginResponse != null) {
                MemberInfoEntity entity = new MemberInfoEntity();

                entity.setMemberInfoSeq(loginResponse.getMemberInfoSeq());
                entity.setMemberInfoStatus(EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class,
                                                                         loginResponse.getMemberInfoStatus()
                                                                        ));
                entity.setMemberInfoUniqueId(loginResponse.getMemberInfoUniqueId());
                entity.setMemberInfoId(loginResponse.getMemberInfoId());
                entity.setMemberInfoPassword(loginResponse.getMemberInfoPassword());
                entity.setMemberInfoLastName(loginResponse.getMemberInfoLastName());
                entity.setMemberInfoFirstName(loginResponse.getMemberInfoFirstName());
                entity.setMemberInfoLastKana(loginResponse.getMemberInfoLastKana());
                entity.setMemberInfoFirstKana(loginResponse.getMemberInfoFirstKana());
                entity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                      loginResponse.getMemberInfoSex()
                                                                     ));
                if (loginResponse.getMemberInfoBirthday() != null) {
                    entity.setMemberInfoBirthday(new Timestamp(loginResponse.getMemberInfoBirthday().getTime()));
                }
                entity.setMemberInfoTel(loginResponse.getMemberInfoTel());
                entity.setMemberInfoMail(loginResponse.getMemberInfoMail());
                entity.setMemberInfoAddressId(loginResponse.getMemberInfoAddressId());
                entity.setShopSeq(1001);
                entity.setAccessUid(loginResponse.getAccessUid());
                entity.setVersionNo(loginResponse.getVersionNo());
                entity.setAdmissionYmd(loginResponse.getAdmissionYmd());
                entity.setSecessionYmd(loginResponse.getSecessionYmd());
                entity.setMemo(loginResponse.getMemo());
                if (loginResponse.getLastLoginTime() != null) {
                    entity.setLastLoginTime(new Timestamp(loginResponse.getLastLoginTime().getTime()));
                }
                entity.setLastLoginUserAgent(loginResponse.getLastLoginUserAgent());
                if (loginResponse.getRegistTime() != null) {
                    entity.setRegistTime(new Timestamp(loginResponse.getRegistTime().getTime()));
                }
                if (loginResponse.getUpdateTime() != null) {
                    entity.setUpdateTime(new Timestamp(loginResponse.getUpdateTime().getTime()));
                }
                return entity;
            } else {
                return null;
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error(e.getMessage());
            // サービスでエラーがある場合はエラーメッセージをセット
            if (e.getStatusCode().is5xxServerError()) {
                // サーバーエラー
                throw new RuntimeException();
            } else {
                ClientErrorMessageUtility clientErrorMessageUtility =
                                ApplicationContextUtility.getBean(ClientErrorMessageUtility.class);
                List<String> errorMsgList = clientErrorMessageUtility.getMessage(e.getResponseBodyAsString());
                if (errorMsgList != null && !errorMsgList.isEmpty()) {
                    throw new BadCredentialsException(
                                    AppLevelFacesMessageUtil.getAllMessage(LOGIN_FAIL_MSGCD, null).getMessage());
                } else {
                    return null;
                }
            }
        }
    }

    // ゲストユーザー用顧客ID関連 >>>>>>>>>>

    /**
     * 顧客ID取得
     *
     * @return 顧客ID
     */
    public String getCustomerId() {
        // Cookieから顧客IDを取得
        String customerId = getCustomerIdFromCookie();
        if (StringUtils.isEmpty(customerId)) {
            return null;
        }

        // 顧客IDをCookieに保存し有効期間を延長
        setCustomerIdToCookie(customerId);
        // 顧客IDを返却
        return customerId;
    }

    /**
     * 顧客ID取得・生成
     * ※取得できなければ生成する
     *
     * @return 顧客ID
     */
    public String getOrCreateCustomerId() {
        // 引数はtrue（Cookie保存する）
        return getOrCreateCustomerId(true);
    }

    /**
     * 顧客ID取得・生成
     * ※取得できなければ生成する
     *
     * @param isSaveCookie 生成した顧客IDをCookie保存するかどうか true...保存する
     * @return 顧客ID
     */
    public String getOrCreateCustomerId(boolean isSaveCookie) {
        // 顧客IDを取得
        String customerId = getCustomerId();
        if (StringUtils.isEmpty(customerId)) {
            // 顧客IDを取得できなければ新規採番

            // ユーザーAPIより顧客IDを取得
            customerId = getCustomerIdFromApi();
        }
        // 顧客IDをCookieに保存
        if (isSaveCookie) {
            setCustomerIdToCookie(customerId);
        }
        // 顧客IDを返却
        return customerId;
    }

    /**
     * 顧客IDクリア
     */
    public void clearCustomerId() {
        // Cookieから顧客IDを削除
        deleteCookieCustomerId();
    }

    /**
     * ユーザーAPIより顧客IDを取得
     *
     * @return 顧客ID
     */
    private String getCustomerIdFromApi() {
        // ユーザーAPIより顧客IDを取得
        CustomerIdResponse customerIdResponse = null;
        try {
            customerIdResponse = usersApi.getCustomerId();
        } catch (RestClientResponseException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new RuntimeException("顧客IDの払い出しに失敗しました");
        }

        if (customerIdResponse == null) {
            throw new RuntimeException("顧客IDの払い出しに失敗しました");
        }
        return customerIdResponse.getCustomerId();
    }

    /**
     * Cookieより顧客IDを取得
     *
     * @return 顧客ID
     */
    public static String getCustomerIdFromCookie() {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }

        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // Cookieから顧客IDを取得
        Cookie cookie = getCookie(request, COOKIE_NAME_GUEST_CUSTOMER_ID);
        if (cookie != null) {
            String encryptedCustomerId = null;
            try {
                // Cookie値を取得
                encryptedCustomerId = URLDecoder.decode(cookie.getValue(), UTF_8);

                // 暗号化されているので復号する
                CryptoModule cryptoModule = ApplicationContextUtility.getBean(CryptoModule.class);
                return cryptoModule.decryptCustomerId(encryptedCustomerId);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                throw new RuntimeException("Cookie decrypt fail customerId:" + encryptedCustomerId, e);
            }
        }
        return null;
    }

    /**
     * Cookieに顧客IDを設定
     *
     * @param customerId 顧客ID
     */
    private void setCustomerIdToCookie(String customerId) {
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            //
            // 顧客IDを暗号化
            CryptoModule cryptoModule = ApplicationContextUtility.getBean(CryptoModule.class);
            String encryptedCustomerId = cryptoModule.encryptCustomerId(customerId);

            // Cookieに暗号化した顧客IDを設定
            Cookie cookie = getCookie(request, COOKIE_NAME_GUEST_CUSTOMER_ID);
            if (cookie == null) {
                cookie = new Cookie(COOKIE_NAME_GUEST_CUSTOMER_ID, encryptedCustomerId);
            }
            updateCookie(request, response, cookie);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new RuntimeException("Cookie encrypt fail customerId:" + customerId, e);
        }
    }

    /**
     * Cookieより顧客IDを削除
     */
    private void deleteCookieCustomerId() {
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // Cookie取得
        Cookie cookie = getCookie(request, COOKIE_NAME_GUEST_CUSTOMER_ID);
        if (cookie != null) {
            // Cookie削除
            deleteCookie(request, response, cookie);
        }
    }

    /**
     * クッキー取得<br/>
     *
     * @param request    リクエスト
     * @param cookieName クッキー名
     * @return Cookie クッキー情報
     */
    private static Cookie getCookie(HttpServletRequest request, String cookieName) {
        // 現行HM HttpUtilityから移植
        // クッキーから取得
        Cookie[] cookieArray = request.getCookies();
        if (cookieArray != null) {
            for (Cookie cookie : cookieArray) {
                if (StringUtils.equals(cookieName, cookie.getName())) {
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
    private void updateCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
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
    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
        // クッキーがある場合
        if (cookie != null) {
            // 有効期間
            cookie.setMaxAge(COOKIE_CLEAR);
            cookie.setPath(PropertiesUtil.getSystemPropertiesValue(COOKIE_SAVE_PATH_PROP_KEY));
            response.addCookie(cookie);
        }
    }
}