/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.application.commoninfo.impl;

import jp.co.itechh.quad.authentication.presentation.api.AuthenticationApi;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginUpdateRequest;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.utility.AccessDeviceUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

        // TODO 認証成功処理を実装するクラスが２つあるが、どっちかに統一したほうが良いかも？
        //      ※「HmAuthenticationSuccessHandler」と「AuthenticationEventListeners」の２クラス
        //        何か理由があって２クラス必要なら、その旨どこかに明記したいですね。。
        //      【追記】現状、本メソッドは不要だが、「AuthenticationEventListeners」の中身をこちらに移行するケース考慮して削除は行っていない

        // onAuthenticationSuccessの親メソッドで参照される、SavedRequestの有無によりSpringSecurityの遷移先URLの取得が異なる
        //   ・SavedRequestなし　⇒　DefaultTargetUrl の設定値（会員マイページ）が遷移先
        //   ・SavedRequestあり　⇒　SavedRequest から取得したリクエストURLが遷移先（DefaultTargetUrl の値は無視される）
        // ※「SavedRequestなし」となるケースはログイン画面に直接遷移したときのみ、DefaultTargetUrl に遷移される。それ以外はSavedRequest から取得
        super.setDefaultTargetUrl("/member/");
        super.onAuthenticationSuccess(request, response, authentication);

        // 会員のログイン情報を更新
        AccessDeviceUtility accessDeviceUtility = ApplicationContextUtility.getBean(AccessDeviceUtility.class);
        String userAgent = accessDeviceUtility.getUserAgent(request);
        Integer memberInfoSeq = getMemberInfoSeq(authentication);
        if (StringUtils.isNotEmpty(userAgent) && ObjectUtils.isNotEmpty(memberInfoSeq)) {
            MemberLoginUpdateRequest memberLoginUpdateRequest = new MemberLoginUpdateRequest();
            memberLoginUpdateRequest.setMemberInfoSeq(memberInfoSeq);
            memberLoginUpdateRequest.setUserAgent(userAgent);
            AuthenticationApi authenticationApi = ApplicationContextUtility.getBean(AuthenticationApi.class);
            authenticationApi.memberLoginUpdate(memberLoginUpdateRequest);
        }
    }

    /**
     * SpringSecurityの管理領域から会員SEQを取得
     *
     * @param authentication
     * @return MemberInfoSeq
     */
    private Integer getMemberInfoSeq(Authentication authentication) {
        HmFrontUserDetails userDetails = (HmFrontUserDetails) authentication.getPrincipal();
        return ObjectUtils.isNotEmpty(userDetails) ? userDetails.getMemberInfoEntity().getMemberInfoSeq() : null;
    }

}