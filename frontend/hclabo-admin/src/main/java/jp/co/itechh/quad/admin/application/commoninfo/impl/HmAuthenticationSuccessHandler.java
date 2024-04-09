/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.application.commoninfo.impl;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.utility.CheckPermissionUtility;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring-Security ログイン成功ハンドラ
 *
 * @author kaneda
 */
@Component
public class HmAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * ログイン成功時の処理
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        // ログイン済の場合は、ヘッダに管理者SEQを設定する
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof HmAdminUserDetails) {
                // SpringSecurityの管理する領域から、ユーザ情報が取れればそのインスタンスを返却
                HmAdminUserDetails adminUserDetails = (HmAdminUserDetails) authentication.getPrincipal();
                HeaderParamsHelper headerParamsHelper = ApplicationContextUtility.getBean(HeaderParamsHelper.class);
                headerParamsHelper.setAdminSeq(
                                String.valueOf(adminUserDetails.getAdministratorEntity().getAdministratorSeq()));
            }
        }

        // onAuthenticationSuccessの親メソッドで参照される、SavedRequestの有無によりSpringSecurityの遷移先URLの取得が異なる
        //   ・SavedRequestなし　⇒　DefaultTargetUrl の設定値（getTargetUrlの戻り値）が遷移先
        //   ・SavedRequestあり　⇒　SavedRequest から取得したリクエストURLが遷移先（DefaultTargetUrl の値は無視される）
        // ※「SavedRequestなし」となるケースはログイン画面に直接遷移したときのみ、DefaultTargetUrl に遷移される。それ以外はSavedRequest から取得
        CheckPermissionUtility checkPermissionUtility = ApplicationContextUtility.getBean(CheckPermissionUtility.class);
        super.setDefaultTargetUrl(checkPermissionUtility.getTargetUrl());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}