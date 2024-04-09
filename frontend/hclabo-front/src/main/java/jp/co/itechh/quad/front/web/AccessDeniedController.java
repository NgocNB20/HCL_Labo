/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.web;

import jp.co.itechh.quad.front.config.twofactorauth.TwoFactorAuthentication;
import jp.co.itechh.quad.web.doublesubmit.annotation.DoubleSubmitCheck;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * AccessDeniedController
 *
 * @author kimura
 */
@Controller
@DoubleSubmitCheck(disable = true) // CSRFチェックでエラーになった後、このコントローラでさらにダブルサブミットエラーになるため回避
public class AccessDeniedController extends AbstractController {

    /** エラーメッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION = "ACX000104";

    /**
     * アクセス制御<br/>
     * エラーとなったリクエストに対して、リクエスト元の画面にリダイレクトを行う<br/>
     * 【呼び出されるケース】<br/>
     * ・SpringSecurityによるCSRFチェックでNGとなった場合（全画面のPOST通信が対象）
     * ・セッション切れ後にPOST通信が行われた場合（全画面のPOST通信が対象。SpringSecurityがCSRFチェックNGとして振る舞う）
     *
     * @param request
     * @param redirectAttributes
     * @param model
     * @return 遷移元画面 / TOP画面（異常時）
     */
    @RequestMapping(value = "/accessdenied/", method = {RequestMethod.GET, RequestMethod.POST})
    protected String accessDenied(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isTwoFactor = authentication instanceof TwoFactorAuthentication;
        if (isTwoFactor) {
            SecurityContext context = SecurityContextHolder.getContext();
            SecurityContextHolder.clearContext();
            context.setAuthentication(null);
            return "redirect:/login/";
        }

        boolean isAuthenticated = authentication instanceof UsernamePasswordAuthenticationToken;
        if (isAuthenticated && request.getSession().getAttribute("redirect") != null) {
            return "redirect:" + request.getSession().getAttribute("redirect");
        }

        // エラーとなったリクエストのリファラ情報を取得する
        String referer = request.getHeader("referer");

        addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);

        // 遷移元にリダイレクトを行う　※遷移元が認証画面の場合は、リダイレクト先でSpringSecurityがログイン画面を表示
        return StringUtils.isBlank(referer) ? "redirect:/" : "redirect:" + referer;
    }

}
