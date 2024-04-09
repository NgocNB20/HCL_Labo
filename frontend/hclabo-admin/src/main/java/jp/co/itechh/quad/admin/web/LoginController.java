/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ログインコントローラ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Controller
public class LoginController extends AbstractController {

    /**
     * ログイン画面表示処理
     *
     * @return ログイン画面
     */
    @RequestMapping(value = "/login/", method = {RequestMethod.GET})
    public String doLoadIndex() {

        return "login";
    }

}
