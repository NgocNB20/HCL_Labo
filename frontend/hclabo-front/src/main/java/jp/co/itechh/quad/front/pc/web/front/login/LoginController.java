/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.login;

import jp.co.itechh.quad.front.web.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * ログイン Controller
 * ※SpringSecurityでパラメータ制御されるのでModelのSession化は行わない
 *
 * @author kaneda
 */
@Controller
public class LoginController extends AbstractController {

    /** ログインHelper */
    private final LoginHelper loginHelper;

    /**
     * コンストラクタ
     *
     * @param loginHelper ログインHelper
     */
    @Autowired
    public LoginController(LoginHelper loginHelper) {
        this.loginHelper = loginHelper;
    }

    /**
     * ログイン画面表示処理<br/>
     *
     * @param loginModel
     * @param response
     * @return ログイン画面
     */
    @RequestMapping(value = "/login/", method = {RequestMethod.GET})
    protected String doLoadIndex(LoginModel loginModel, HttpServletResponse response) {

        loginHelper.toPageForLoad(loginModel);

        // "Cache-Control"はCacheControlFilterで管理をしているが、ログイン成功直後にブラウザバックし、再ログインを行うと SpringSecurity が不正操作と判断するため一時的に指定
        response.addHeader("Cache-Control", "no-store, no-cache, max-age=0, must-revalidate");

        return "login/index";
    }
}
