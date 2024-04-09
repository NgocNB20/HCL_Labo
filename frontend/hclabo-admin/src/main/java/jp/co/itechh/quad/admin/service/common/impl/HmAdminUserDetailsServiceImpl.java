/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.service.common.impl;

import jp.co.itechh.quad.admin.application.commoninfo.impl.HmAdminUserDetails;
import jp.co.itechh.quad.admin.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.RestClientErrorMessageUtility;
import jp.co.itechh.quad.authentication.presentation.api.AuthenticationApi;
import jp.co.itechh.quad.authentication.presentation.api.param.AdminAuthGroupDetailtResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.AdminLoginRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.AdminLoginResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring-Security Admin認証サービスクラス
 * DB認証用カスタマイズ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class HmAdminUserDetailsServiceImpl implements UserDetailsService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HmAdminUserDetailsServiceImpl.class);

    /**  ログイン失敗メッセージ */
    private static final String LOGIN_FAIL_MSGCD = "AbstractUserDetailsAuthenticationProvider.badCredentials";

    /**
     * 認証Api
     */
    private final AuthenticationApi authenticationApi;

    /**
     * コンストラクタ
     *
     * @param authenticationApi 認証API
     */
    @Autowired
    public HmAdminUserDetailsServiceImpl(AuthenticationApi authenticationApi) {
        this.authenticationApi = authenticationApi;
    }

    /**
     * 管理者認証処理
     *
     * @param administratorId 管理者ID
     */
    @Override
    public UserDetails loadUserByUsername(String administratorId) throws AuthenticationException {

        // 入力チェックのエラーメッセージ
        String errorMessage;

        // DB負荷を減らすためチェックを行う
        try {
            // 半角変換
            administratorId = Normalizer.normalize(administratorId, Normalizer.Form.NFKC);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            e.printStackTrace();
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("LMC002011E", null).getMessage();
            throw new BadCredentialsException(errorMessage);
        }

        // 必須チェック
        // ユーザID
        if (StringUtils.isEmpty(administratorId)) {
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("SMM002002W", new Object[] {"ユーザID"}).getMessage();
            throw new BadCredentialsException(errorMessage);
        }
        // パスワード
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password");
        if (StringUtils.isEmpty(password)) {
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("SMM002002W", new Object[] {"パスワード"}).getMessage();
            throw new BadCredentialsException(errorMessage);
        }

        // 文字数チェック
        if (administratorId.length() > 20) {
            errorMessage = AppLevelFacesMessageUtil.getAllMessage("USERNAME.LENGTH.E", new Object[] {"ユーザID", "20"})
                                                   .getMessage();
            throw new BadCredentialsException(errorMessage);
        }

        // API呼び出し
        try {
            AdminLoginRequest adminLoginRequest = new AdminLoginRequest();
            adminLoginRequest.setAdminId(administratorId);
            AdminLoginResponse adminLoginResponse = authenticationApi.adminLogin(adminLoginRequest);

            // 管理者情報を取得する
            AdministratorEntity administratorEntity = getAdministratorEntity(adminLoginResponse);

            // 権限リストを取得する
            List<String> authorityList = getAuthorityList(adminLoginResponse);

            return new HmAdminUserDetails(administratorEntity, authorityList.toArray(new String[0]));
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            RestClientErrorMessageUtility messageUtil =
                            ApplicationContextUtility.getBean(RestClientErrorMessageUtility.class);
            String msg = messageUtil.getServerErrorMessage(se.getResponseBodyAsString());
            if (msg == null) {
                msg = se.getMessage();
            }
            throw new BadCredentialsException(msg);
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            throw new BadCredentialsException(
                            AppLevelFacesMessageUtil.getAllMessage(LOGIN_FAIL_MSGCD, null).getMessage());
        }
    }

    /**
     * 管理者情報を取得する
     *
     * @param adminLoginResponse 管理者のログインレスポンス
     * @return 管理者クラスエンティティ
     */
    private AdministratorEntity getAdministratorEntity(AdminLoginResponse adminLoginResponse) {
        AdministratorEntity administratorEntity = new AdministratorEntity();
        administratorEntity.setAdministratorSeq(adminLoginResponse.getAdministratorSeq());
        administratorEntity.setShopSeq(1001);
        administratorEntity.setAdministratorStatus(EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class,
                                                                                 adminLoginResponse.getAdministratorStatus()
                                                                                ));
        administratorEntity.setAdministratorId(adminLoginResponse.getAdministratorId());
        administratorEntity.setAdministratorPassword(adminLoginResponse.getAdministratorPassword());
        administratorEntity.setMail(adminLoginResponse.getMail());
        administratorEntity.setAdministratorLastName(adminLoginResponse.getAdministratorLastName());
        administratorEntity.setAdministratorFirstName(adminLoginResponse.getAdministratorFirstName());
        administratorEntity.setAdministratorLastKana(adminLoginResponse.getAdministratorLastKana());
        administratorEntity.setAdministratorFirstKana(adminLoginResponse.getAdministratorFirstKana());
        if (adminLoginResponse.getUseStartDate() != null) {
            administratorEntity.setUseStartDate(new Timestamp(adminLoginResponse.getUseStartDate().getTime()));
        }
        if (adminLoginResponse.getUseEndDate() != null) {
            administratorEntity.setUseEndDate(new Timestamp(adminLoginResponse.getUseEndDate().getTime()));
        }
        administratorEntity.setAdminAuthGroupSeq(adminLoginResponse.getAdminAuthGroupSeq());
        if (adminLoginResponse.getRegistTime() != null) {
            administratorEntity.setRegistTime(new Timestamp(adminLoginResponse.getRegistTime().getTime()));
        }
        if (adminLoginResponse.getUpdateTime() != null) {
            administratorEntity.setUpdateTime(new Timestamp(adminLoginResponse.getUpdateTime().getTime()));
        }
        if (adminLoginResponse.getPasswordChangeTime() != null) {
            administratorEntity.setPasswordChangeTime(
                            new Timestamp(adminLoginResponse.getPasswordChangeTime().getTime()));
        }
        if (adminLoginResponse.getPasswordExpiryDate() != null) {
            administratorEntity.setPasswordExpiryDate(
                            new Timestamp(adminLoginResponse.getPasswordExpiryDate().getTime()));
        }
        administratorEntity.setLoginFailureCount(adminLoginResponse.getLoginFailureCount());
        if (adminLoginResponse.getAccountLockTime() != null) {
            administratorEntity.setAccountLockTime(new Timestamp(adminLoginResponse.getAccountLockTime().getTime()));
        }
        administratorEntity.setPasswordNeedChangeFlag(EnumTypeUtil.getEnumFromValue(HTypePasswordNeedChangeFlag.class,
                                                                                    adminLoginResponse.getPasswordNeedChangeFlag()
                                                                                   ));

        if (adminLoginResponse.getAdminAuthGroup() != null) {
            AdminAuthGroupEntity adminAuthGroupEntity = new AdminAuthGroupEntity();
            adminAuthGroupEntity.setAdminAuthGroupSeq(adminLoginResponse.getAdminAuthGroup().getAdminAuthGroupSeq());
            adminAuthGroupEntity.setShopSeq(1001);
            adminAuthGroupEntity.setAuthGroupDisplayName(
                            adminLoginResponse.getAdminAuthGroup().getAuthGroupDisplayName());
            if (adminLoginResponse.getAdminAuthGroup().getRegistTime() != null) {
                adminAuthGroupEntity.setRegistTime(
                                new Timestamp(adminLoginResponse.getAdminAuthGroup().getRegistTime().getTime()));
            }
            if (adminLoginResponse.getAdminAuthGroup().getUpdateTime() != null) {
                adminAuthGroupEntity.setUpdateTime(
                                new Timestamp(adminLoginResponse.getAdminAuthGroup().getUpdateTime().getTime()));
            }
            adminAuthGroupEntity.setUnmodifiableGroup(adminLoginResponse.getAdminAuthGroup().getUnmodifiableGroup());

            if (adminLoginResponse.getAdminAuthGroup().getAdminAuthGroupDetailList() != null) {
                List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList = new ArrayList<>();
                for (AdminAuthGroupDetailtResponse adminAuthGroupDetailtResponse : adminLoginResponse.getAdminAuthGroup()
                                                                                                     .getAdminAuthGroupDetailList()) {
                    AdminAuthGroupDetailEntity adminAuthGroupDetailEntity = new AdminAuthGroupDetailEntity();
                    adminAuthGroupDetailEntity.setAdminAuthGroupSeq(
                                    adminAuthGroupDetailtResponse.getAdminAuthGroupSeq());
                    adminAuthGroupDetailEntity.setAuthTypeCode(adminAuthGroupDetailtResponse.getAuthTypeCode());
                    adminAuthGroupDetailEntity.setAuthLevel(adminAuthGroupDetailtResponse.getAuthLevel());
                    if (adminAuthGroupDetailtResponse.getRegistTime() != null) {
                        adminAuthGroupDetailEntity.setRegistTime(
                                        new Timestamp(adminAuthGroupDetailtResponse.getRegistTime().getTime()));
                    }
                    if (adminAuthGroupDetailtResponse.getUpdateTime() != null) {
                        adminAuthGroupDetailEntity.setUpdateTime(
                                        new Timestamp(adminAuthGroupDetailtResponse.getUpdateTime().getTime()));
                    }

                    adminAuthGroupDetailList.add(adminAuthGroupDetailEntity);
                }
                adminAuthGroupEntity.setAdminAuthGroupDetailList(adminAuthGroupDetailList);
            }

            administratorEntity.setAdminAuthGroup(adminAuthGroupEntity);
        }
        return administratorEntity;
    }

    /**
     * 権限リストを取得する
     *
     * @param adminLoginResponse 管理者のログインレスポンス
     * @return 権限リスト
     */
    private List<String> getAuthorityList(AdminLoginResponse adminLoginResponse) {
        List<String> authorityList = new ArrayList<>();
        if (adminLoginResponse.getAuthorityList() != null) {
            authorityList = adminLoginResponse.getAuthorityList();
        }

        return authorityList;
    }
}